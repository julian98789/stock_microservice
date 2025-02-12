package com.stock_service.stock.infrastructure.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtDetailsServiceTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtDetailsService jwtDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return UserDetails when JWT is valid")
    void shouldReturnUserDetailsWhenJwtIsValid() {
        String jwt = "valid.jwt.token";
        String username = "testUser";
        String role = "ROLE_USER";

        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(jwtService.extractRole(jwt)).thenReturn(role);

        UserDetails userDetails = jwtDetailsService.loadUserByUsername(jwt);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(role, userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when JWT is invalid")
    void shouldThrowUsernameNotFoundExceptionWhenJwtIsInvalid() {
        String jwt = "invalid.jwt.token";

        when(jwtService.extractUsername(jwt)).thenThrow(new UsernameNotFoundException("Invalid JWT"));

        assertThrows(UsernameNotFoundException.class, () -> jwtDetailsService.loadUserByUsername(jwt));
    }
}