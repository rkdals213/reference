package com.reference.model.responseBodies

import com.reference.model.entity.Address
import com.reference.model.entity.Order
import com.reference.model.entity.OrderItem
import com.reference.model.entity.OrderStatus
import org.springframework.data.domain.Page
import java.time.LocalDateTime
import java.util.stream.Collectors

class OrderDto(order: Order) {
    var orderId: Long = order.id
    var name: String = order.member?.name!!
    var orderDate: LocalDateTime = order.orderDate!!
    var orderStatus: OrderStatus = order.status!!
    var address: Address = order.delivery?.address!!
    var orderItems: List<OrderItemDto> = order.orderItems.stream()
        .map { orderItem: OrderItem -> OrderItemDto(orderItem) }
        .collect(Collectors.toList())
}

class OrderItemDto(orderItem: OrderItem) {
    var itemName: String? = orderItem.item?.name
    var orderPrice: Int = orderItem.orderPrice
    var count: Int = orderItem.count
}

class SelectOrderResponse(page: Page<OrderDto>) {
    var list: MutableList<OrderDto> = ArrayList()
    var page: PageResponse = PageResponse()

    init {
        this.list = page.content
        this.page.totalElements = page.totalElements
        this.page.last = page.isLast
        this.page.number = page.number
        this.page.totalPages = page.totalPages
    }
}

class OrderResultResponse {
    var email: String = ""
    var name: String = ""
    var address: Address = Address()
    var orderItems: MutableList<OrderItemDto> = ArrayList()

    private fun toOrderItemDtoList(orderItems: MutableList<OrderItem>): MutableList<OrderItemDto> {
        val orderItemDtoList: MutableList<OrderItemDto> = ArrayList()
        for (i in orderItems) orderItemDtoList.add(OrderItemDto(i))
        return orderItemDtoList
    }

    constructor()

    constructor(order: Order) {
        email= order.member.email
        name = order.member.name
        address = order.delivery.address
        orderItems = toOrderItemDtoList(order.orderItems)
    }
}