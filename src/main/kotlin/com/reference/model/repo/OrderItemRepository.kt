package com.reference.model.repo

import com.reference.model.entity.Item
import com.reference.model.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findByItem(item: Item): Optional<OrderItem>
}