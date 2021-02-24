package com.reference.controller

import com.reference.model.requestBodies.CreateItemRequest
import com.reference.model.requestBodies.SelectItemRequest
import com.reference.model.responseBodies.ItemResponse
import com.reference.model.responseBodies.handleException
import com.reference.model.responseBodies.handleSuccess
import com.reference.model.service.ItemService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import java.lang.RuntimeException


@RestController
@RequestMapping("/v1/item")
class ItemController(
    val itemService: ItemService
) {
    @GetMapping("")
    fun selectItem(selectItemRequest: SelectItemRequest, pageable: Pageable): ResponseEntity<Map<String, Any?>> {
        return try {
            val itemResponse: ItemResponse = itemService.findByCondition(selectItemRequest, pageable)
            handleSuccess(itemResponse)
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }

    @PostMapping("")
    fun createItem(@RequestBody createItemRequest: CreateItemRequest): ResponseEntity<Map<String, Any?>> {
        return try {
            itemService.createItem(createItemRequest)
            handleSuccess()
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }

    @DeleteMapping("")
    fun deleteItem(@RequestParam id: Long): ResponseEntity<Map<String, Any?>> {
        return try {
            itemService.deleteItem(id)
            handleSuccess()
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }
}