package com.eab.openbread.domain.service

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService {
    private val secret = "MI_SECRETO_SUPER_SEGURO_123456789_MI_SECRETO_SUPER_SEGURO"
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + 1000 * 60 * 60)

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): String {
        val claims = Jwts.parser()
            .setSigningKey(secret.toByteArray())
            .parseClaimsJws(token)
            .body

        return claims.subject
    }
}