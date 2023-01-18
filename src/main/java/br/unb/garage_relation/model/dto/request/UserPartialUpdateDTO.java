package br.unb.garage_relation.model.dto.request;

import jakarta.validation.constraints.Email;

public record UserPartialUpdateDTO(
        String username,
        String password,
        @Email String email,
        String fullName
) {
}
