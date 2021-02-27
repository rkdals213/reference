package com.reference.model.responseBodies

import com.reference.model.entity.Address
import com.reference.model.entity.Member

class LoginResponse(member: Member) {
    var id: Long = member.id
    var email: String = member.email
    var name: String = member.name
    var address: Address = member.address
    var token: String = ""
    var expiration: String = ""
}

class UpdateResponse(member: Member) {
    var id: Long = member.id
    var email: String = member.email
    var name: String = member.name
    var address: Address = member.address
    var token: String = ""
    var expiration: String = ""
}

class RegistResponse(
    member: Member
) {
    var id: Long = member.id
    var email: String = member.email
    var name: String = member.name
    var address: Address = member.address
}