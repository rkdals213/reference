package com.reference.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    var id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member : Member = Member()
        set(member) {
            field = member
            member.orders.plus(this)
        }

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    var orderItems: MutableList<OrderItem> = ArrayList<OrderItem>()

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    var delivery : Delivery = Delivery()
        set(delivery) {
            field = delivery
            delivery.order = this
        }
    var orderDate : LocalDateTime = ZonedDateTime.now().toLocalDateTime()

    @Enumerated(EnumType.STRING)
    var status : OrderStatus? = null

    fun addOrderItem(orderItem: OrderItem) {
        orderItems.add(orderItem)
        orderItem.order = this
    }

    companion object {
        fun createOrder(member: Member, delivery: Delivery, orderItems: MutableList<OrderItem>): Order {
            val order = Order()
            order.member = member
            order.delivery = delivery
            for (orderItem in orderItems) {
                order.addOrderItem(orderItem)
            }
            order.status = OrderStatus.ORDER
            order.orderDate = LocalDateTime.now()
            return order
        }
    }
}


@Entity
@Table(name = "order_item")
class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item : Item? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order : Order = Order()
    var orderPrice = 0
    var count = 0

    companion object {
        fun createOrderItem(item: Item?, orderPrice: Int, count: Int): OrderItem {
            val orderItem = OrderItem()
            orderItem.item = item
            orderItem.orderPrice = orderPrice
            orderItem.count = count
            return orderItem
        }
    }
}

enum class OrderStatus {
    ORDER, CANCEL
}
