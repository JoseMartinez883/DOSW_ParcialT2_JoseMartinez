package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.models.exception.BusinessException;
import edu.dosw.parcial.core.utils.JwtUtil;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Intento de inicio de sesión para correo: [{}]", request.getCorreo());

        UsuarioEntity usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> {
                    log.warn("Credenciales inválidas: correo [{}] no registrado en BD", request.getCorreo());
                    return new BusinessException("E_CREDENCIALES_INVALIDAS", "Correo o contraseña incorrectos.", HttpStatus.UNAUTHORIZED);
                });

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.warn("Credenciales inválidas: contraseña errónea para correo [{}]", request.getCorreo());
            throw new BusinessException("E_CREDENCIALES_INVALIDAS", "Correo o contraseña incorrectos.", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
        log.info("Inicio de sesión exitoso para usuario [{}] con rol [{}]", usuario.getCorreo(), usuario.getRol());

        return LoginResponse.builder()
                .token(token)
                .rol(usuario.getRol())
                .build();
    }
}
