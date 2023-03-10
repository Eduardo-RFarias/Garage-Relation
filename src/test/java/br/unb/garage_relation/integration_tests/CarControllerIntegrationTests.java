package br.unb.garage_relation.integration_tests;

import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static br.unb.garage_relation.Constants.TEST_SERVER_PORT;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class CarControllerIntegrationTests {
	@Autowired
	private CarRepository carRepository;

	@Test
	public void findAllCars__withTwoCars__shouldReturn200WithTwoCars() {
		// Arrange
		carRepository.saveAll(
				List.of(
						new Car("Ford", "Fiesta", 2010),
						new Car("Ford", "Focus", 2011)
				)
		);

		// Act
		var response = given()
				.basePath("/api/v1/car")
				.port(TEST_SERVER_PORT)
				.auth()
				.basic("user", "password")
				.when()
				.get();

		// Assert
		assertThat(response.statusCode()).isEqualTo(200);

		var carList = response.getBody().jsonPath().getList("_embedded.carResponseDTOList");

		assertThat(carList).hasSize(2);
	}
}
