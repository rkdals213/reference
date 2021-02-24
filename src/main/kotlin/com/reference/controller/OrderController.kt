package com.reference.controller

import com.reference.model.requestBodies.RegistOrderRequest
import com.reference.model.requestBodies.SelectOrderRequest
import com.reference.model.responseBodies.*
import com.reference.model.service.OrderService
import com.reference.security.JwtClaim
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@RestController
@RequestMapping("/v1/order")
class OrderController(
    val orderService: OrderService
) {
    @GetMapping("")
    fun selectOrder(@JwtClaim("info.id") id: Long, selectOrderRequest: SelectOrderRequest, pageable: Pageable): ResponseEntity<Map<String, Any?>> {
        return try {
            selectOrderRequest.memberId = id
            val selectOrderResponse = orderService.findByCondition(selectOrderRequest, pageable)
            handleSuccess(selectOrderResponse)
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }

    @PostMapping("")
    fun registOrder(@JwtClaim("info.id") id: Long, @RequestBody registOrderRequest: RegistOrderRequest): ResponseEntity<Map<String, Any?>> {
        return try {
            registOrderRequest.memberId = id
            val orderResultResponse = orderService.registOrder(registOrderRequest)
            handleSuccess(orderResultResponse)
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }

    @PutMapping("/cancel")
    fun cancelOrder(@JwtClaim("info.id") id: Long, @RequestParam orderId: Long): ResponseEntity<Map<String, Any?>> {
        return try {
            val orderResultResponse = orderService.cancelOrder(id, orderId)
            handleSuccess(orderResultResponse)
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }
}