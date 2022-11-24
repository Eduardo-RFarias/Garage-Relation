package br.unb.garage_relation.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String password
) {
}
