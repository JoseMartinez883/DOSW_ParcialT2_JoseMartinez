package edu.dosw.parcial.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String secret = "bXlfc3VwZXJfc2VjcmV0X2tleV9mb3Jfand0X3ZhbGlkYXRpb25fZG9zd18yMDI0Cg==";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secret, 3600000);
    }

    @Test
    @DisplayName("Debe generar un token válido y extraer correctamente claims de correo y rol")
    void generateToken_AndExtractClaims() {
        String correo = "admin@gmail.com";
        String rol = "ADMIN";

        String token = jwtTokenProvider.generateToken(correo, rol);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(correo, jwtTokenProvider.extractCorreo(token));
        assertEquals(rol, jwtTokenProvider.extractRol(token));
    }

    @Test
    @DisplayName("Debe retornar false al validar un token malformado")
    void validateToken_MalformedToken_ReturnsFalse() {
        String invalidToken = "not.a.valid.jwt.token";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }
}
