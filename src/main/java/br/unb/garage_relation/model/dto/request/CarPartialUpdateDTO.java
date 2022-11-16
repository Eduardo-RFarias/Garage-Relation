package br.unb.garage_relation.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record CarPartialUpdateDTO(
        @Length(max = 50) String model,
        @Length(max = 50) String brand,
        @Min(1800) @Max(3000) Integer year
) {
}
