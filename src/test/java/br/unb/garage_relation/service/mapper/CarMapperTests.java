package br.unb.garage_relation.service.mapper;


import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CarMapperTests {
    @Autowired
    private CarMapper carMapper;

    @Test
    public void mapToCarResponseDTO__withCarEntity__returnsDTO() {
        // Arrange
        var car = new Car(
                "Uno",
                "Fiat",
                2010
        );
        car.setId(1L);

        // Act
        var dto = carMapper.toCarResponseDTO(car);

        // Assert
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.brand()).isEqualTo("Fiat");
        assertThat(dto.model()).isEqualTo("Uno");
        assertThat(dto.year()).isEqualTo(2010);
    }

    @Test
    public void mapToCar__withCarCreateDTO__returnsCar() {
        // Arrange
        var dto = new CarCreateDTO(
                "Uno",
                "Fiat",
                2010
        );

        // Act
        var car = carMapper.toCar(dto);

        // Assert
        assertThat(car.getId()).isNull();
        assertThat(car.getBrand()).isEqualTo("Fiat");
        assertThat(car.getModel()).isEqualTo("Uno");
        assertThat(car.getYear()).isEqualTo(2010);
    }

    @Test
    public void mapToCar__withCarUpdateDTO__returnsCar() {
        // Arrange
        var car = new Car(
                "Uno",
                "Fiat",
                2010
        );
        car.setId(1L);

        var dto = new CarUpdateDTO(
                "Celta",
                "Chevrolet",
                2015
        );

        // Act
        var updatedCar = carMapper.updateCar(car, dto);

        // Assert
        assertThat(updatedCar.getId()).isEqualTo(1L);
        assertThat(updatedCar.getBrand()).isEqualTo("Chevrolet");
        assertThat(updatedCar.getModel()).isEqualTo("Celta");
        assertThat(updatedCar.getYear()).isEqualTo(2015);
    }

    @Test
    public void mapToCar__withCarPartialUpdateDTO__returnsCar() {
        // Arrange
        var car = new Car(
                "Uno",
                "Fiat",
                2010
        );
        car.setId(1L);

        var dto = new CarPartialUpdateDTO(
                "Siena",
                null,
                null
        );

        // Act
        var updatedCar = carMapper.updateCar(car, dto);

        // Assert
        assertThat(updatedCar.getId()).isEqualTo(1L);
        assertThat(updatedCar.getBrand()).isEqualTo("Fiat");
        assertThat(updatedCar.getModel()).isEqualTo("Siena");
        assertThat(updatedCar.getYear()).isEqualTo(2010);
    }

    @Test
    public void mapToModel__withCarEntity__returnsModel() {
        // Arrange
        var car = new Car(
                "Uno",
                "Fiat",
                2010
        );
        car.setId(1L);

        // Act
        var model = carMapper.toModel(car);

        // Assert
        assertThat(model.getLinks()).hasSize(1);

        var content = model.getContent();

        assert content != null;
        assertThat(content.id()).isEqualTo(1L);
        assertThat(content.brand()).isEqualTo("Fiat");
        assertThat(content.model()).isEqualTo("Uno");
        assertThat(content.year()).isEqualTo(2010);
    }

    @Test
    public void mapToCollectionModel__withCarEntityList__returnsCollectionModel() {
        // Arrange
        var carList = List.of(
                new Car(
                        1L,
                        "Uno",
                        "Fiat",
                        2010
                ),
                new Car(
                        2L,
                        "Celta",
                        "Chevrolet",
                        2015
                )
        );

        var carDTOS = carList.stream().map(carMapper::toCarResponseDTO).toList();

        // Act
        var collectionModel = carMapper.toCollectionModel(carList);

        // Assert
        assertThat(collectionModel.getLinks()).hasSize(1);

        var content = collectionModel.getContent().stream().toList();

        assertThat(content).hasSize(2);

        var car1 = content.get(0);

        assertThat(car1.getLinks()).hasSize(1);
        assertThat(car1.getContent()).isEqualTo(carDTOS.get(0));

        var car2 = content.get(1);

        assertThat(car2.getLinks()).hasSize(1);
        assertThat(car2.getContent()).isEqualTo(carDTOS.get(1));
    }
}
