package br.unb.garage_relation.controller;

import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.service.CarService;
import br.unb.garage_relation.service.mapper.CarMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
public class CarControllerTests {
    @Autowired
    private CarMapper carMapper;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @Test
    public void listAllCars__withTwoCars__returns200WithTwoCars() {
        // Arrange
        var expectedResponse = CollectionModel.of(
                List.of(
                        carMapper.toModel(new Car(1L, "Uno", "Fiat", 2011)),
                        carMapper.toModel(new Car(2L, "Palio", "Fiat", 2010))
                ),
                linkTo(methodOn(CarController.class).findAll()).withSelfRel()
        );

        when(carService.findAll()).thenReturn(expectedResponse);

        // Act
        var response = carController.findAll();

        // Assert
        assertThat(response).isEqualTo(expectedResponse);
    }
}
