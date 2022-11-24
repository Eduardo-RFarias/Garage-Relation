package br.unb.garage_relation.controller;

import br.unb.garage_relation.exception.JwtAuthenticationException;
import br.unb.garage_relation.model.dto.request.LoginDTO;
import br.unb.garage_relation.model.dto.response.TokenResponseDTO;
import br.unb.garage_relation.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static br.unb.garage_relation.Constants.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoints")
public class AuthController {
    private final AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            value = "/signIn",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Sign in",
            description = "Sign in with username and password, returning a JWT token",
            tags = "Authentication Endpoints",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logged in successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public TokenResponseDTO signIn(@RequestBody @Valid LoginDTO credentials) {
        return authService.signIn(credentials);
    }

    @PutMapping(
            value = "/refresh",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Refresh token",
            description = "Create a new JWT token using the refresh token",
            tags = "Authentication Endpoints",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Refreshed token successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public TokenResponseDTO refreshToken(@RequestHeader("Authorization") String refreshToken) throws JwtAuthenticationException {
        return authService.refreshToken(refreshToken);
    }
}
