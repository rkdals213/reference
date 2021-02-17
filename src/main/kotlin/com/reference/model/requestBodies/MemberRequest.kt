package com.reference.model.requestBodies

import com.reference.model.entity.Address

class LoginRequest(
    var email: String = "",
    var password: String = ""
)

class UpdateRequest(
    var id: Long = 0,
    var name: String = "",
    var password: String = "",
    var address: Address = Address()
)

class RegistRequest(
    var email: String = "",
    var name: String = "",
    var password: String = "",
    var address: Address = Address()
)