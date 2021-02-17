package com.reference.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    var id: Long? = null

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null

    @Embedded
    var address: Address = Address()

    @Enumerated(EnumType.STRING)
    var status : DeliveryStatus? = null
}

class RegistDeliveryRequest {
    var address: Address = Address()
}

enum class DeliveryStatus {
    READY, COMP
}