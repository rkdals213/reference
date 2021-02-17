package com.reference.security

import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtInterceptor(private val jwtService: JwtService, private val COOKIE_KEY: String) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        // 인증 target 이 아닌경우 pass
        if (handler !is HandlerMethod || !isAuthenticationPresent(handler)) {
            return true
        }
        val cookie = request.cookies?.firstOrNull { cookie -> COOKIE_KEY == cookie.name }
        val header = request.getHeader("Authorization")?.replace("Bearer", "")?.trim()

        val check = arrayOf(cookie?.value, header)
            .filterNotNull().toTypedArray().first()
            .let { token -> jwtService.isUsable(token) }

        if (check) {
            return true
        } else {
            throw RuntimeException("Unauthorized access!! need to authentication")
        }
    }

    private fun isAuthenticationPresent(handler: HandlerMethod): Boolean {
        return (handler.hasMethodAnnotation(Authenticated::class.java)
                || handler.beanType.isAnnotationPresent(Authenticated::class.java))
    }

    private fun getAuthorizationToken(req: HttpServletRequest): Optional<String> {
        return Optional.ofNullable(req.getHeader("Authorization"))
            .map { token: String ->
                token.replace(
                    "Bearer".toRegex(),
                    ""
                ).trim { it <= ' ' }
            }
    }
}
