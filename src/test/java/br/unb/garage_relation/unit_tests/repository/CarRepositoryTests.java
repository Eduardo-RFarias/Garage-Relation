package br.unb.garage_relation.unit_tests.repository;

import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.User;
import br.unb.garage_relation.repository.CarRepository;
import br.unb.garage_relation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CarRepositoryTests {
	@Autowired
	private CarRepository carRepository;

	@Autowired
	private UserRepository userRepository;

	private User owner;

	@BeforeEach
	public void setUp() {
		carRepository.deleteAll();
		userRepository.deleteAll();

		this.owner = userRepository.save(
				new User(
						"test",
						"password",
						"email@email.com",
						"test user"
				)
		);
	}

	@Test
	public void findByBrandOrModelContains__withSomeCars__returns4Cars() {
		// Arrange
		var carsToSave = List.of(
				new Car("Fiesta", "Ford", 2010, owner),
				new Car("Focus", "Ford", 2011, owner),
				new Car("Fusion", "Ford", 2012, owner),
				new Car("Gol", "Volkswagen", 2013, owner),
				new Car("Golf", "Volkswagen", 2014, owner),
				new Car("Jetta", "Volkswagen", 2015, owner)
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
