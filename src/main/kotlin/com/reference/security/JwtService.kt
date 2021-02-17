package com.reference.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.ZonedDateTime
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

interface JwtService {
    fun create(composer: Consumer<Payload>): String
    fun create(composer: Payload): String
    fun isUsable(token: String): Boolean
    fun parseClaim(token: String): String

    class Payload() {
        var exp: ZonedDateTime = ZonedDateTime.now()
        val claims: MutableMap<String, Any> = HashMap()
        fun addClaim(key: String, value: Any) {
            claims[key] = value
        }
        fun makeExp(expiration: ZonedDateTime) {
            exp = expiration
        }
    }
}

@Component
class ConsoleJwtService : JwtService {
    private val JWT_KEY_SALT = "dlsdjrywngowjreks".toByteArray(StandardCharsets.UTF_8)
    private val JWT_KEY = "dndbsmschzhdndbrkchlrhdi".toByteArray(StandardCharsets.UTF_8)

    private val jsonMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(ConsoleJwtService::class.java)
    private val secretKey: Key
        get() = try {
            val md = MessageDigest.getInstance("SHA-512")
            md.update(JWT_KEY)
            md.update(JWT_KEY_SALT)
            Keys.hmacShaKeyFor(md.digest())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }

    override fun create(composer: Consumer<JwtService.Payload>): String {
        val payload = JwtService.Payload()
        composer.accept(payload)
        return create(payload)
    }

    override fun create(composer: JwtService.Payload): String {
        val builder = Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setExpiration(Date.from(composer.exp.toInstant()))
            .signWith(secretKey)

        for ((key, value) in composer.claims) {
            builder.claim(key, value)
        }

        return builder.compact()
    }

    override fun isUsable(token: String): Boolean {
        return checkJwt(token)
    }

    override fun parseClaim(token: String): String {
        return parseJwt(token)!!
    }

    private fun checkJwt(token: String): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            val expiration = claims.body.expiration
            System.currentTimeMillis() <= expiration.time
        } catch (e: Exception) {
            log.info("Fail to check web token")
            log.debug("Fail to check web token", e)
            false
        }
    }

    private fun parseJwt(token: String): String? {
        return try {
            val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            jsonMapper.writeValueAsString(claims.body)
        } catch (e: Exception) {
            log.info("Fail to parse web token")
            log.debug("Fail to parse web token", e)
            null
        }
    }

}