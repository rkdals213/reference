package com.reference.model.repo

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.reference.model.entity.*
import com.reference.model.requestBodies.SelectOrderRequest
import com.reference.model.responseBodies.OrderDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.support.PageableExecutionUtils
import javax.persistence.EntityManager

interface OrderRepository : JpaRepository<Order, Long>, OrderRepositoryQD, QuerydslPredicateExecutor<Order> {
}

interface OrderRepositoryQD {
    fun findByCondition(selectOrderRequest: SelectOrderRequest, pageable: Pageable): Page<OrderDto>
}

class OrderRepositoryQDImpl(
    private val em: EntityManager,
    private val query: JPAQueryFactory = JPAQueryFactory(em)
) : OrderRepositoryQD {
    override fun findByCondition(selectOrderRequest: SelectOrderRequest, pageable: Pageable): Page<OrderDto> {
        val order: QOrder = QOrder.order
        val member: QMember = QMember.member
        val delivery: QDelivery = QDelivery.delivery

        val content: List<Order> = query
            .select(order)
            .from(order)
            .join(order.member, member).fetchJoin()
            .join(order.delivery, delivery).fetchJoin()
            .where(
                memberIdEq(selectOrderRequest.memberId),
                deliveryStatusEq(selectOrderRequest.deliveryStatus),
                orderStatusEq(selectOrderRequest.orderStatus)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery: JPAQuery<Order> = query
            .select(order)
            .from(order)
            .join(order.member, member).fetchJoin()
            .join(order.delivery, delivery).fetchJoin()
            .where(
                memberIdEq(selectOrderRequest.memberId),
                deliveryStatusEq(selectOrderRequest.deliveryStatus),
                orderStatusEq(selectOrderRequest.orderStatus)
            )

        val result = content.map { o -> OrderDto(o) }.toList()

        QOrder.order.delivery.status.eq(DeliveryStatus.READY)

        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetchCount() }
    }

    private fun memberIdEq(memberId: Long?): BooleanExpression? {
        return if (memberId == null) null
        else QOrder.order.member.id.eq(memberId)
    }

    private fun deliveryStatusEq(status: DeliveryStatus?): BooleanExpression? {
        return if (status == null) null
        else QOrder.order.delivery.status.eq(status)
    }

    private fun orderStatusEq(status: OrderStatus?): BooleanExpression? {
        return if (status == null) null
        else QOrder.order.status.eq(status)
    }
}