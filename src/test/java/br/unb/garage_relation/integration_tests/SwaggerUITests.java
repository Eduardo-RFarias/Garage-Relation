package br.unb.garage_relation.integration_tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static br.unb.garage_relation.Constants.TEST_SERVER_PORT;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class SwaggerUITests {
	@Test
	public void swaggerUI__shouldReturn200() {
		// Arrange and Act
		var response = given()
				.basePath("/swagger-ui/index.html")
				.port(TEST_SERVER_PORT)
				.auth()
				.basic("user", "password")
				.when()
				.get();

		// Assert
		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.getBody().asString()).contains("Swagger UI");
	}
}
