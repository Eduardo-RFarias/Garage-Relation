package br.unb.garage_relation.model.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record TokenResponseDTO(
        @NotNull String username,
        @NotNull Boolean authenticated,
        @NotNull Date created,
        @NotNull Date expiration,
        @NotNull String accessToken,
        @NotNull String refreshToken
) {
}
