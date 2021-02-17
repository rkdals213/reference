package com.reference.model.repo

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.reference.model.entity.*
import com.reference.model.requestBodies.DType
import com.reference.model.requestBodies.SelectItemRequest
import com.reference.model.responseBodies.AlbumDto
import com.reference.model.responseBodies.BookDto
import com.reference.model.responseBodies.ItemDto
import com.reference.model.responseBodies.MovieDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.support.PageableExecutionUtils
import javax.persistence.EntityManager

interface ItemRepository : JpaRepository<Item, Long>, ItemRepositoryQD, QuerydslPredicateExecutor<Item> {
}

interface ItemRepositoryQD {
    fun findItemByCondition(selectItemRequest: SelectItemRequest, pageable: Pageable): Page<ItemDto>
}

class ItemRepositoryQDImpl(
    private val em: EntityManager,
    private val query: JPAQueryFactory = JPAQueryFactory(em)
) : ItemRepositoryQD {
    override fun findItemByCondition(selectItemRequest: SelectItemRequest, pageable: Pageable): Page<ItemDto> {
        val item: QItem = QItem.item
        val category: QCategory = QCategory.category

        val content = query
            .selectFrom(item)
            .join(item.categories, category)
            .where(
                deleted(),
                categoryEq(selectItemRequest.dType),
                keywordContains(selectItemRequest.keyword),
                priceGoe(selectItemRequest.lowPrice),
                priceLoe(selectItemRequest.highPrice)
            )
            .orderBy(item.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery: JPAQuery<Item> = query
            .selectFrom(item)
            .join(item.categories, category)
            .where(
                deleted(),
                categoryEq(selectItemRequest.dType),
                keywordContains(selectItemRequest.keyword),
                priceGoe(selectItemRequest.lowPrice),
                priceLoe(selectItemRequest.highPrice)
            )

        val result: MutableList<ItemDto> = ArrayList()
        for (c in content) {
            when (c.javaClass) {
                Book().javaClass -> {
                    result.add(BookDto(c as Book))
                }
                Album().javaClass -> {
                    result.add(AlbumDto(c as Album))
                }
                Movie().javaClass -> {
                    result.add(MovieDto(c as Movie))
                }
            }
        }

        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetchCount() }
    }

    private fun categoryEq(dType: DType): BooleanExpression? {
        return if (dType == DType.ALL) null
        else QCategory.category.name.eq(dType.name)
    }

    private fun keywordContains(keyword: String): BooleanExpression? {
        return if (keyword == "") null
        else QItem.item.name.contains(keyword)
    }

    private fun priceLoe(highPrice: Int): BooleanExpression? {
        return if (highPrice == 0) null
        else QItem.item.price.loe(highPrice)

    }

    private fun priceGoe(lowPrice: Int): BooleanExpression? {
        return if (lowPrice == 0) null
        else QItem.item.price.goe(lowPrice)
    }

    private fun deleted(): BooleanExpression? {
        return QItem.item.deleted.isFalse
    }
}