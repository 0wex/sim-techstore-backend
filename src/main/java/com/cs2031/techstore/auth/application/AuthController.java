package com.cs2031.techstore.auth.application;

import com.cs2031.techstore.auth.domain.AuthService;
import com.cs2031.techstore.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión. Ambos devuelven un token JWT.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    record RegisterRequest(
            @Schema(description = "Correo electrónico válido", example = "estudiante@utec.edu.pe")
            @Email @NotBlank String email,
            @Schema(description = "Contraseña del usuario", example = "miPassword123")
            @NotBlank String password,
            @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
            @NotBlank String name) {}

    record LoginRequest(
            @Schema(description = "Correo registrado", example = "estudiante@utec.edu.pe")
            @Email @NotBlank String email,
            @Schema(description = "Contraseña del usuario", example = "miPassword123")
            @NotBlank String password) {}

    record TokenResponse(
            @Schema(description = "Token JWT. Enviarlo en el header Authorization: Bearer <token>",
                    example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJlc3R1ZGlhbnRlQHV0ZWMuZWR1LnBlIn0.abc123")
            String token) {}

    @Operation(summary = "Registrar usuario",
            description = "Crea una cuenta nueva y devuelve un token JWT listo para usar.")
    @ApiResponse(responseCode = "200", description = "Usuario registrado")
    @ApiResponse(responseCode = "400", description = "Correo ya registrado o campos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest req) {
        String token = authService.register(req.email(), req.password(), req.name());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @Operation(summary = "Iniciar sesión",
            description = "Valida las credenciales y devuelve un token JWT.")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "400", description = "Credenciales inválidas",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req.email(), req.password());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
