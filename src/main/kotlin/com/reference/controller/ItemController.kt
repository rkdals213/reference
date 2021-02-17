package com.reference.controller

import com.reference.model.requestBodies.CreateItemRequest
import com.reference.model.requestBodies.SelectItemRequest
import com.reference.model.responseBodies.ItemResponse
import com.reference.model.service.ItemService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/item")
class ItemController(
    val itemService: ItemService
) {
    @GetMapping("")
    fun selectItem(selectItemRequest: SelectItemRequest, pageable: Pageable): ItemResponse {
        return itemService.findByCondition(selectItemRequest, pageable)
    }

    @PostMapping("")
    fun createItem(@RequestBody createItemRequest: CreateItemRequest) {
        itemService.createItem(createItemRequest)
    }

    @DeleteMapping("")
    fun deleteItem(@RequestParam id: Long) {
        itemService.deleteItem(id)
    }
}