package br.unb.garage_relation.model.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CarResponseDTO(
        @NotNull Long id,
        @NotNull @Length(max = 50) String model,
        @NotNull @Length(max = 50) String brand,
        @NotNull @Min(1800) @Max(3000) Integer year
) {

}
