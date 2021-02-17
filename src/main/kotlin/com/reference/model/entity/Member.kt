package com.reference.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.ArrayList
import javax.persistence.*

@Entity
class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long = 0

    @Column(unique = true)
    var email: String =""

    var password: String =""
    var name: String = ""

    @Embedded
    var address: Address = Address()

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    var orders: MutableList<Order> = ArrayList()
}

@Embeddable
class Address {
    var city: String = ""
    var street: String = ""
    var zipcode: String = ""

    constructor()

    constructor(city: String, street: String, zipcode: String) {
        this.city = city
        this.street = street
        this.zipcode = zipcode
    }
}
