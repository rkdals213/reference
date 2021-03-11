package com.reference.model.service

import com.reference.model.entity.*
import com.reference.model.repo.ItemRepository
import com.reference.model.repo.MemberRepository
import com.reference.model.repo.OrderRepository
import com.reference.model.requestBodies.RegistOrderRequest
import com.reference.model.requestBodies.SelectOrderRequest
import com.reference.model.responseBodies.OrderResultResponse
import com.reference.model.responseBodies.SelectOrderResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    val orderRepository: OrderRepository,
    val memberRepository: MemberRepository,
    val itemRepository: ItemRepository
) {
    fun findByCondition(selectOrderRequest: SelectOrderRequest, pageable: Pageable): SelectOrderResponse {
        val result = orderRepository.findByCondition(selectOrderRequest, pageable)
        return SelectOrderResponse(result)
    }

    @Transactional
    fun registOrder(registOrderRequest: RegistOrderRequest): OrderResultResponse? {
        val member: Member = memberRepository.findById(registOrderRequest.memberId).get()
        val delivery = Delivery()
        delivery.address = registOrderRequest.delivery.address
        delivery.status = DeliveryStatus.READY
        val orderItems: MutableList<OrderItem> = ArrayList()

        for (i in registOrderRequest.items) {
            val item: Item = itemRepository.findById(i.itemId).get()
            val orderItem: OrderItem = OrderItem.createOrderItem(item, item.price, i.qty)

            if (item.stockQuantity - i.qty < 0) {
                return null
            }
            item.stockQuantity = item.stockQuantity - i.qty

            orderItems.add(orderItem)
        }

        val order: Order = Order.createOrder(member, delivery, orderItems)
        val result: Order = orderRepository.save(order)
        return OrderResultResponse(result)
    }

    @Transactional
    fun cancelOrder(memberId: Long, itemId: Long): OrderResultResponse? {
        val order = orderRepository.findById(itemId)

        if (order.isPresent) {
            val result = order.get()
            if (result.member.id != memberId || result.status == OrderStatus.CANCEL) return null
            result.status = OrderStatus.CANCEL
            for (i in result.orderItems) {
                val item = itemRepository.findById(i.item!!.id).get()
                item.stockQuantity += i.count
            }
            return OrderResultResponse(result)
        }
        return null
    }
}