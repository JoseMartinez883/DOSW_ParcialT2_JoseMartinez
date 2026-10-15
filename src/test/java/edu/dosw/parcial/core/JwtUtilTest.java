package edu.dosw.parcial.core;

import edu.dosw.parcial.core.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "bXlfc3VwZXJfc2VjcmV0X2tleV9mb3Jfand0X3ZhbGlkYXRpb25fZG9zd18yMDI0Cg==";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, 3600000);
    }

    @Test
    @DisplayName("Debe generar un token válido y extraer correctamente claims de correo y rol")
    void generateToken_AndExtractClaims() {
        // Arrange
        String correo = "admin@gmail.com";
        String rol = "ADMIN";

        // Act
        String token = jwtUtil.generateToken(correo, rol);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(correo, jwtUtil.extractCorreo(token));
        assertEquals(rol, jwtUtil.extractRol(token));
    }

    @Test
    @DisplayName("Debe retornar false al validar un token malformado")
    void validateToken_MalformedToken_ReturnsFalse() {
        // Arrange
        String invalidToken = "not.a.valid.jwt.token";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }
}
