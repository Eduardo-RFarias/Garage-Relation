package br.unb.garage_relation.repository;

import br.unb.garage_relation.model.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class CarRepositoryTests {
    @Autowired
    private CarRepository carRepository;

    @Test
    public void findByBrandOrModelContains__withSomeCars__returns4Cars() {
        // Arrange
        var carsToSave = List.of(
                new Car("Fiesta", "Ford", 2010),
                new Car("Focus", "Ford", 2011),
                new Car("Fusion", "Ford", 2012),
                new Car("Gol", "Volkswagen", 2013),
                new Car("Golf", "Volkswagen", 2014),
                new Car("Jetta", "Volkswagen", 2015)
        );

        carRepository.saveAll(carsToSave);

        // Act
        var result = carRepository.findByBrandOrModelContains("f");

        // Assert
        assertThat(result).hasSize(4);
        assertThat(result).contains(
                carsToSave.get(0),
                carsToSave.get(1),
                carsToSave.get(2),
                carsToSave.get(4)
        );
    }
}
