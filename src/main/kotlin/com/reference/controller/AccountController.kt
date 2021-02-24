package com.reference.controller

import com.reference.model.requestBodies.RegistRequest
import com.reference.model.responseBodies.*
import com.reference.model.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@RestController
@RequestMapping("/v1/account")
class AccountController(
    private val memberService: MemberService
) {
    @PostMapping("")
    fun regist(@RequestBody registRequest: RegistRequest): ResponseEntity<Map<String, Any?>> {
        return try {
            val registResponse = memberService.regist(registRequest)
            handleSuccess(registResponse)
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }

    @PostMapping("/email")
    fun checkEmail(@RequestParam email: String): ResponseEntity<Map<String, Any?>> {
        return try {
            val count = memberService.countByEmail(email)
            if (count <= 0) handleFail("이미 가입된 이메일입니다")
            handleSuccess()
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }
}