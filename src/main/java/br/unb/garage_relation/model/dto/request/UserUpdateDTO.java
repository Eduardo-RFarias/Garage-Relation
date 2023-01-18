package br.unb.garage_relation.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(
        @NotNull String username,
        @NotNull String password,
        @NotNull @Email String email,
        @NotNull String fullName
) {
}
