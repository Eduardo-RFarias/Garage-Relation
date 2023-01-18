package br.unb.garage_relation.model.dto.response;

import jakarta.validation.constraints.NotNull;

public record TokenResponseDTO(
        @NotNull String accessToken,
        @NotNull String refreshToken
) {
}
