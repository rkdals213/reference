package com.reference.model.requestBodies

import com.reference.model.entity.DeliveryStatus
import com.reference.model.entity.OrderStatus
import com.reference.model.entity.RegistDeliveryRequest
import java.util.*

class SelectOrderRequest(
    var memberId: Long = 0,
    var deliveryStatus: DeliveryStatus? = null,
    var orderStatus: OrderStatus? = null
)

class RegistOrderRequest(
    var memberId: Long = 0,
    var items: MutableList<ItemQty> = ArrayList<ItemQty>(),
    var delivery: RegistDeliveryRequest = RegistDeliveryRequest()
)

class ItemQty(
    var itemId: Long = 0,
    var qty: Int = 1
)