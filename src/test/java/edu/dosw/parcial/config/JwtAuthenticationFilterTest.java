package edu.dosw.parcial.config;

import edu.dosw.parcial.core.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Debe autenticar al usuario si el token Bearer es válido")
    void doFilterInternal_ValidToken_AuthenticatesUser() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtTokenProvider.validateToken("valid.token")).thenReturn(true);
        when(jwtTokenProvider.extractCorreo("valid.token")).thenReturn("admin@gmail.com");
        when(jwtTokenProvider.extractRol("valid.token")).thenReturn("ADMIN");

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("admin@gmail.com", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("No debe autenticar si no hay cabecera Authorization")
    void doFilterInternal_NoHeader_PassesThrough() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
