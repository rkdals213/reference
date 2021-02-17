package com.reference.security

import com.jayway.jsonpath.JsonPath
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest


class JwtSessionArgumentResolver(private val jwtService: JwtService, private val COOKIE_KEY: String) :
    HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(JwtClaim::class.java)?.run { true } ?: false
    }

    @Throws(Exception::class)
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val annotation = parameter.getParameterAnnotation(JwtClaim::class.java)
        val paramType = parameter.parameterType
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java) ?: return null
        val key = if ((annotation?.value != null) and (annotation?.value != "")) annotation?.value else parameter.parameterName

        val cookie = request.cookies?.firstOrNull { cookie -> COOKIE_KEY == cookie.name }
        val header = request.getHeader("Authorization")?.replace("Bearer", "")?.trim()

        val token = arrayOf(cookie?.value, header).filterNotNull().toTypedArray().first()
        val claim = jwtService.parseClaim(token)

        return JsonPath.parse(claim).read("$.$key", paramType)
    }
}