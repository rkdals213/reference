package com.reference.model.service

import com.reference.model.repo.CategoryRepository
import com.reference.model.repo.ItemRepository
import com.reference.model.repo.OrderItemRepository
import com.reference.model.requestBodies.*
import com.reference.model.responseBodies.ItemResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
    val itemRepository: ItemRepository,
    val categoryRepository: CategoryRepository
) {
    fun findByCondition(selectItemRequest: SelectItemRequest, pageable: Pageable): ItemResponse {
        val result = itemRepository.findItemByCondition(selectItemRequest, pageable)
        return ItemResponse(result)
    }

    @Transactional
    fun createItem(createItemRequest: CreateItemRequest) {
        val category = categoryRepository.findByName(createItemRequest.category.name)
        when (createItemRequest.category) {
            DType.MOVIE -> {
                val movie = itemRepository.save(createMovie(createItemRequest))
                category.items.add(movie)
            }
            DType.ALBUM -> {
                val album = itemRepository.save(createAlbum(createItemRequest))
                category.items.add(album)
            }
            DType.BOOK -> {
                val book = itemRepository.save(createBook(createItemRequest))
                category.items.add(book)
            }
            else -> {}
        }
    }

    @Transactional
    fun deleteItem(id: Long) {
        val item = itemRepository.findById(id).get()
        item.deleted = true
    }
}