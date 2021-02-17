package com.reference.support

import com.reference.security.JwtInterceptor
import com.reference.security.JwtService
import com.reference.security.JwtSessionArgumentResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(jwtService: JwtService) : WebMvcConfigurer {
    private val jwtService: JwtService
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtInterceptor())
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(jwtArgsResolver())
    }

    @Bean
    fun jwtInterceptor(): JwtInterceptor {
        return JwtInterceptor(jwtService, JWT_COOKIE_NAME)
    }

    @Bean
    fun jwtArgsResolver(): JwtSessionArgumentResolver {
        return JwtSessionArgumentResolver(jwtService, JWT_COOKIE_NAME)
    }

    companion object {
        const val JWT_COOKIE_NAME = "my.test.jwt"
    }

    init {
        this.jwtService = jwtService
    }
}