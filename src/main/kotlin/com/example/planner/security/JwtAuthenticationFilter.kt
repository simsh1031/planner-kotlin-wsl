package com.example.planner.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)

            if (jwtUtil.validateToken(token)) {
                val userId = jwtUtil.getUserId(token)
                val email = jwtUtil.getEmail(token)

                request.setAttribute("userId", userId)

                // ✅ SecurityContextHolder에 인증 정보 등록
                val authentication = UsernamePasswordAuthenticationToken(
                    email,          // principal
                    null,           // credentials
                    listOf(SimpleGrantedAuthority("ROLE_USER"))  // authorities
                )
                SecurityContextHolder.getContext().authentication = authentication

            } else {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid or expired token")
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}