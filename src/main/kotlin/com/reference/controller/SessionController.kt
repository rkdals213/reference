package com.reference.controller

import com.reference.model.entity.Address
import com.reference.model.requestBodies.LoginRequest
import com.reference.model.requestBodies.UpdateRequest
import com.reference.model.responseBodies.LoginResponse
import com.reference.model.responseBodies.Result
import com.reference.model.responseBodies.UpdateResponse
import com.reference.model.service.MemberService
import com.reference.security.ConsoleJwtService
import com.reference.security.JwtClaim
import com.reference.security.OAuthClient
import com.reference.support.HttpSupport
import com.reference.support.WebConfig.Companion.JWT_COOKIE_NAME
import org.mindrot.jbcrypt.BCrypt
import org.springframework.web.bind.annotation.*
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
    fun createSession(@RequestBody loginRequest: LoginRequest, req: HttpServletRequest, res: HttpServletResponse): LoginResponse {
        val loginResponse = memberService.login(loginRequest) ?: return LoginResponse(Result.FAIL,0, "", "", Address())

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

        return loginResponse
    }

    @GetMapping("")
    fun getSession(req: HttpServletRequest, res: HttpServletResponse): Map<String, Any> {
        return try {
            val cookie = HttpSupport.getCookie(req, JWT_COOKIE_NAME)

            mapOf(
                "result" to "success",
                "name" to cookie.name,
                "value" to cookie.value
            )
        } catch (e: Exception) {
            mapOf(
                "result" to "fail"
            )
        }
    }

    @PutMapping("")
    fun putSession(@JwtClaim("info.id") id: Long, @RequestBody updateRequest: UpdateRequest, req: HttpServletRequest, res: HttpServletResponse): UpdateResponse {
        updateRequest.id = id
        val updateResponse = memberService.update(updateRequest) ?: return UpdateResponse(Result.FAIL,0, "", "", Address())

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
        return updateResponse
    }

    @DeleteMapping("")
    fun revokeSession(req: HttpServletRequest, res: HttpServletResponse): Map<String, Any>? {
        return try {
            val cookie = HttpSupport.getCookie(req, JWT_COOKIE_NAME)
            HttpSupport.removeCookie(cookie, res)

            mapOf(
                "code" to "session.revoked",
                "message" to "t.pirates session has been revoked."
            )
        } catch (e: Exception) {
            mapOf(
                "result" to "fail"
            )
        }
    }
}