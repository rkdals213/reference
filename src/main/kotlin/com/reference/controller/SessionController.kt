package com.reference.controller

import com.reference.model.entity.Address
import com.reference.model.requestBodies.LoginRequest
import com.reference.model.requestBodies.UpdateRequest
import com.reference.model.responseBodies.*
import com.reference.model.service.MemberService
import com.reference.security.ConsoleJwtService
import com.reference.security.JwtClaim
import com.reference.security.OAuthClient
import com.reference.support.HttpSupport
import com.reference.support.WebConfig.Companion.JWT_COOKIE_NAME
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/v1/session")
class SessionController(
    private val oauthClient: OAuthClient,
    private val jwtService: ConsoleJwtService,
    private val memberService: MemberService
) {
    @PostMapping("")
    fun createSession(@RequestBody loginRequest: LoginRequest, req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<Map<String, Any?>> {
        return try {
            val loginResponse = memberService.login(loginRequest) ?: return handleFail("가입된 정보가 없습니다")

            val profile = mapOf(
                "id" to loginResponse.id,
                "email" to loginResponse.email,
                "name" to loginResponse.name,
                "address" to loginResponse.address,
            )

            val token = oauthClient.createToken()
            val jwt = jwtService.create { payload ->
                payload.makeExp(token.expiration)
                payload.addClaim("info", profile)
            }

            val jwtCookie: Cookie = HttpSupport.createCookie { conf ->
                conf
                    .name(JWT_COOKIE_NAME)
                    .value(jwt)
                    .expires(60 * 60 * 24)
                    .secure("https" == req.scheme)
            }
            res.addCookie(jwtCookie)

            loginResponse.expiration = token.expiration.toString()
            loginResponse.token = jwt

            handleSuccess(loginResponse)
        } catch (e: RuntimeException) {
            handleException(e)
        }
    }

    @GetMapping("")
    fun getSession(req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<Map<String, Any?>> {
        return try {
            val cookie = HttpSupport.getCookie(req, JWT_COOKIE_NAME)

            handleSuccess(
                mapOf(
                    "name" to cookie.name,
                    "value" to cookie.value
                )
            )
        } catch (e: Exception) {
            handleException(e)
        }
    }

    @PutMapping("")
    fun putSession(@JwtClaim("info.id") id: Long, @RequestBody updateRequest: UpdateRequest, req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<Map<String, Any?>> {
        return try {
            updateRequest.id = id
            val updateResponse = memberService.update(updateRequest) ?: return handleFail("사용자 정보가 없습니다")

            val profile = mapOf(
                "id" to updateResponse.id,
                "email" to updateResponse.email,
                "name" to updateResponse.name,
                "address" to updateResponse.address,
            )

            val token = oauthClient.createToken()
            val jwt = jwtService.create { payload ->
                payload.makeExp(token.expiration)
                payload.addClaim("info", profile)
            }

            val jwtCookie: Cookie = HttpSupport.createCookie { conf ->
                conf
                    .name(JWT_COOKIE_NAME)
                    .value(jwt)
                    .expires(60 * 60 * 24)
                    .secure("https" == req.scheme)
            }
            res.addCookie(jwtCookie)

            updateResponse.expiration = token.expiration.toString()
            updateResponse.token = jwt
            handleSuccess(updateResponse)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    @DeleteMapping("")
    fun revokeSession(req: HttpServletRequest, res: HttpServletResponse): ResponseEntity<Map<String, Any?>> {
        return try {
            val cookie = HttpSupport.getCookie(req, JWT_COOKIE_NAME)
            HttpSupport.removeCookie(cookie, res)

            handleSuccess()
        } catch (e: Exception) {
            handleException(e)
        }
    }
}