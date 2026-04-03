package com.example.planner.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secret: String
) {
    private val secretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }
    private val expiration = 1000L * 60 * 60 // 1시간

    fun generateToken(userId: Long, email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            // ✅ 최신 API 방식 사용
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getEmail(token: String): String {
        return getClaims(token).subject
    }

    fun getUserId(token: String): Long {
        return getClaims(token)["userId"].toString().toLong()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaims(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    private fun getClaims(token: String): Claims {
        // ✅ deprecated된 Jwts.parser() 대신 parserBuilder() 사용
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}