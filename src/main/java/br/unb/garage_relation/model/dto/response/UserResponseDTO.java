package br.unb.garage_relation.model.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserResponseDTO(
        @NotNull Long id,
        @NotNull String username,
        @NotNull @Email String email,
        @NotNull String fullName
) {
}
