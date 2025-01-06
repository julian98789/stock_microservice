package com.stock_service.stock.infrastructure.configuration.security;

import com.stock_service.stock.domain.util.Util;
import com.stock_service.stock.infrastructure.security.service.JwtDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtDetailsService jwtDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should not set authentication when Authorization header is missing")
    void doFilterInternal_NoAuthHeader() throws ServletException, IOException {
        when(request.getHeader(Util.AUTH_HEADER)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should set authentication when JWT is valid")
    void doFilterInternal_ValidJwt() throws ServletException, IOException {
        String jwt = "valid.jwt.token";
        String authHeader = Util.TOKEN_PREFIX + jwt;
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(Util.AUTH_HEADER)).thenReturn(authHeader);
        when(jwtDetailsService.loadUserByUsername(jwt)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertTrue(authToken.getAuthorities().isEmpty());
        verify(filterChain).doFilter(request, response);
    }

}