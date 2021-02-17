package com.reference.controller

import com.reference.model.requestBodies.RegistRequest
import com.reference.model.responseBodies.EmailCheckResponse
import com.reference.model.responseBodies.RegistResponse
import com.reference.model.responseBodies.Result
import com.reference.model.service.MemberService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/account")
class AccountController(
    private val memberService: MemberService
) {
    @PostMapping("")
    fun regist(@RequestBody registRequest: RegistRequest): RegistResponse {
        return memberService.regist(registRequest)
    }

    @PostMapping("/email")
    fun checkEmail(@RequestParam email: String): EmailCheckResponse {
        val count = memberService.countByEmail(email)
        return if (count > 0) EmailCheckResponse() else EmailCheckResponse(Result.SUCCESS)
    }
}