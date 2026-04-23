package pt.unl.fct.iadi.novaevents.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

class TestJwtService {

    @Test
    fun `generates and parses claims`() {
        val service = JwtService("test-jwt-secret-key-for-hs256-signing-123456789")
        val auth = UsernamePasswordAuthenticationToken(
            "alice",
            "ignored",
            listOf(SimpleGrantedAuthority("ROLE_EDITOR"))
        )

        val token = service.generateToken(auth)
        val claims = service.parseClaims(token)

        assertEquals("alice", claims.subject)
        val roles = claims["roles"] as List<*>
        assertTrue(roles.contains("ROLE_EDITOR"))
    }
}

