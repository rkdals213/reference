package com.reference.model.responseBodies

import com.reference.model.entity.Address

class LoginResponse(
    var result: Result = Result.FAIL,
    var id: Long = 0,
    var email: String = "",
    var name: String = "",
    var address: Address = Address()
) {
    var token: String = ""
    var expiration:String = ""
}

class UpdateResponse(
    var result: Result = Result.FAIL,
    var id: Long = 0,
    var email: String = "",
    var name: String = "",
    var address: Address = Address()
) {
    var token: String = ""
    var expiration:String = ""
}

class RegistResponse(
    var result: Result = Result.FAIL,
    var id: Long = 0,
    var email: String = "",
    var name: String = "",
    var address: Address = Address()
)

class EmailCheckResponse(
    var result: Result = Result.FAIL
)