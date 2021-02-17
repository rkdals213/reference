package com.reference.support

import java.util.function.UnaryOperator
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object HttpSupport {
    fun getCookie(req: HttpServletRequest, name: String): Cookie {
        return req.cookies.first { cookie -> name == cookie.name && cookie.value.isNotEmpty() }
    }

    fun createCookie(composer: UnaryOperator<CookieConfig>): Cookie {
        return composer.apply(CookieConfig()).build()
    }

    fun removeCookie(cookie: Cookie, res: HttpServletResponse) {
        val removed = CookieConfig()
            .name(cookie.name).value("").expires(10)
            .secure(cookie.secure)
            .build()
        res.addCookie(removed)
    }

    class CookieConfig {
        private var name: String = ""
        private var value: String = ""
        private var expires = 0
        private var secure = false
        fun name(name: String): CookieConfig {
            this.name = name
            return this
        }

        fun value(value: String): CookieConfig {
            this.value = value
            return this
        }

        fun expires(expires: Int): CookieConfig {
            this.expires = expires
            return this
        }

        fun secure(secure: Boolean): CookieConfig {
            this.secure = secure
            return this
        }

        fun build(): Cookie {
            val cookie = Cookie(name, value)
            cookie.maxAge = expires
            cookie.secure = secure
            cookie.isHttpOnly = true
            cookie.path = "/"
            return cookie
        }
    }
}
