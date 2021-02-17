package com.reference.controller

import com.reference.model.requestBodies.RegistOrderRequest
import com.reference.model.requestBodies.SelectOrderRequest
import com.reference.model.responseBodies.OrderResultResponse
import com.reference.model.responseBodies.SelectOrderResponse
import com.reference.model.service.OrderService
import com.reference.security.JwtClaim
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/order")
class OrderController(
    val orderService: OrderService
) {
    @GetMapping("")
    fun selectOrder(@JwtClaim("info.id") id: Long, selectOrderRequest: SelectOrderRequest, pageable: Pageable): SelectOrderResponse {
        selectOrderRequest.memberId = id
        return orderService.findByCondition(selectOrderRequest, pageable)
    }

    @PostMapping("")
    fun registOrder(@JwtClaim("info.id") id: Long, @RequestBody registOrderRequest: RegistOrderRequest): OrderResultResponse {
        registOrderRequest.memberId = id
        return orderService.registOrder(registOrderRequest)
    }

    @PutMapping("/cancel")
    fun cancelOrder(@JwtClaim("info.id") id: Long, @RequestParam orderId: Long): OrderResultResponse {
        return orderService.cancelOrder(id, orderId)
    }
}