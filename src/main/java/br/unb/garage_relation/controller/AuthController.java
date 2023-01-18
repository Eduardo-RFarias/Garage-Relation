package br.unb.garage_relation.controller;

import br.unb.garage_relation.model.dto.request.LoginDTO;
import br.unb.garage_relation.model.dto.request.RegisterDTO;
import br.unb.garage_relation.model.dto.response.TokenResponseDTO;
import br.unb.garage_relation.model.dto.response.UserResponseDTO;
import br.unb.garage_relation.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.unb.garage_relation.Constants.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoints")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            value = "/login",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Login",
            description = "Log in with username and password, returning a JWT token",
            tags = "Authentication Endpoints",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logged in successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public TokenResponseDTO login(@RequestBody @Valid LoginDTO credentials, HttpServletResponse response) {
        return authService.login(credentials, response);
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
    public TokenResponseDTO refreshToken(
            @RequestHeader(value = AUTHORIZATION_HEADER, required = false) String headerRefreshToken,
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String cookieRefreshToken,
            HttpServletResponse response
    ) {
        return authService.refresh(headerRefreshToken, cookieRefreshToken, response);
    }

    @DeleteMapping(value = "/logout")
    @Operation(
            summary = "Logout",
            description = "Log out, removing the cookie",
            tags = "Authentication Endpoints",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Logged out successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/signup",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Sign up",
            description = "Sign up with username and password, creating a new user",
            tags = "Authentication Endpoints",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public EntityModel<UserResponseDTO> signup(@RequestBody @Valid RegisterDTO registerDTO) {
        return authService.signup(registerDTO);
    }
}
