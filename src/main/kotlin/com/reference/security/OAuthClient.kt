package com.reference.security

import org.json.JSONException
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Component
class OAuthClient {
    class Token {
        var expiration: ZonedDateTime = ZonedDateTime.now()
    }

    fun createToken(): Token {
        return try {
            val token = Token()
            val now = Instant.now().epochSecond
            token.expiration = Instant.ofEpochSecond(now + 60 * 60 * 24).atZone(ZoneOffset.UTC)
            token
        } catch (e: JSONException) {
            throw RuntimeException("Fail to parse token!!")
        }
    }
}