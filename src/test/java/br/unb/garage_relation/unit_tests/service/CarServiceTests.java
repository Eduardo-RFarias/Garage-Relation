package br.unb.garage_relation.unit_tests.service;

import br.unb.garage_relation.exception.DatabaseOperationException;
import br.unb.garage_relation.exception.RegisterNotFoundException;
import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.User;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.repository.CarRepository;
import br.unb.garage_relation.service.AuthService;
import br.unb.garage_relation.service.CarService;
import br.unb.garage_relation.service.mapper.CarMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarServiceTests {
	@Spy
	private CarMapper carMapper;

	@Mock
	private CarRepository carRepository;

	@Mock
	private AuthService authService;

	@InjectMocks
	private CarService carService;

	@Test
	public void findAllCars__withFourCars__shouldReturnFourCars() {
		// Arrange
		var cars = List.of(
				new Car(1L, "Fiesta", "Ford", 2010),
				new Car(2L, "Civic", "Honda", 2015),
				new Car(3L, "Gol", "Volkswagen", 2018),
				new Car(4L, "Uno", "Fiat", 2019)
		);

		var carsDto = carMapper.toCollectionModel(cars);

		when(carRepository.findAll()).thenReturn(cars);
		// Act
		var result = carService.findAll();

		// Assert
		assertThat(result).isEqualTo(carsDto);
	}

	@Test
	public void findAllCars__withNoCars__shouldReturnEmptyList() {
		// Arrange
		List<Car> cars = List.of();

		when(carRepository.findAll()).thenReturn(cars);
		// Act
		var result = carService.findAll();

		// Assert
		assertThat(result).isEmpty();
	}

	@Test
	public void findCarById__withValidId__shouldReturnCar() throws RegisterNotFoundException {
		// Arrange
		var car = new Car(1L, "Fiesta", "Ford", 2010);
		var carDto = carMapper.toModel(car);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		// Act
		var result = carService.findById(1L);

		// Assert
		assertThat(result).isEqualTo(carDto);
	}

	@Test
	public void findCarById__withInvalidId__shouldThrowsRegisterNotFoundException() {
		// Arrange
		when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Act and Assert
		assertThatThrownBy(
				() -> carService.findById(1L)
		).isInstanceOf(RegisterNotFoundException.class);
	}

	@Test
	public void createCar__withValidInfo__shouldReturnCar() throws DatabaseOperationException {
		// Arrange
		var car = new Car(
				"Fiesta",
				"Ford",
				2010
		);
		var carCreateDto = new CarCreateDTO(
				car.getModel(),
				car.getBrand(),
				car.getYear()
		);
		var carDto = carMapper.toModel(car);

		when(carRepository.save(any(Car.class))).thenReturn(car);
		when(authService.getAuthenticatedUser()).thenReturn(
				new User(1L,
						"username",
						"password",
						"email@email.com",
						"full name"
				)
		);
		// Act
		var result = carService.create(carCreateDto);

		// Assert
		assertThat(result).isEqualTo(carDto);
	}

	@Test
	public void createCar__withInvalidInfo__shouldThrowsDatabaseOperationException() {
		// Arrange
		var carCreateDto = new CarCreateDTO(
				"FIAT",
				"UNO",
				1500
		);

		when(authService.getAuthenticatedUser()).thenReturn(
				new User(1L,
						"username",
						"password",
						"email@email.com",
						"full name"
				)
		);

		doThrow(RuntimeException.class).when(carRepository).save(any(Car.class));

		// Act and Assert
		assertThatThrownBy(
				() -> carService.create(carCreateDto)
		).isInstanceOf(DatabaseOperationException.class);
	}

	@Test
	public void createCar__withInvalidUser__shouldThrowsBadCredentialsException() {
		// Arrange
		var car = new Car(
				"Fiesta",
				"Ford",
				2010
		);
		var carCreateDto = new CarCreateDTO(
				car.getModel(),
				car.getBrand(),
				car.getYear()
		);

		when(carRepository.save(any(Car.class))).thenReturn(car);
		doThrow(BadCredentialsException.class).when(authService).getAuthenticatedUser();

		// Act and Assert
		assertThatThrownBy(
				() -> carService.create(carCreateDto)
		).isInstanceOf(BadCredentialsException.class);
	}

	@Test
	public void updateCar__withValidInfo__shouldReturnCar() throws DatabaseOperationException, RegisterNotFoundException {
		// Arrange
		var originalCar = new Car(
				"Fiesta",
				"Ford",
				2010
		);
		var carUpdateDTO = new CarUpdateDTO(
				originalCar.getModel(),
				originalCar.getBrand(),
				originalCar.getYear()
		);
		var updatedCar = carMapper.updateCar(originalCar, carUpdateDTO);
		var updatedCarDto = carMapper.toModel(updatedCar);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(originalCar));
		when(carRepository.save(updatedCar)).thenReturn(updatedCar);

		// Act
		var result = carService.update(1L, carUpdateDTO);

		// Assert
		assertThat(result).isEqualTo(updatedCarDto);
	}

	@Test
	public void updateCar__withNotRegisteredId__shouldThrowsRegisterNotFoundException() {
		// Arrange
		var carUpdateDTO = new CarUpdateDTO(
				"UNO",
				"FIAT",
				2015
		);

		when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Act and Assert
		assertThatThrownBy(
				() -> carService.update(1L, carUpdateDTO)
		).isInstanceOf(RegisterNotFoundException.class);
	}

	@Test
	public void updateCar__withInvalidInfo__shouldThrowsDatabaseOperationException() {
		// Arrange
		var originalCar = new Car(
				"Fiesta",
				"Ford",
				2010
		);
		var carUpdateDTO = new CarUpdateDTO(
				originalCar.getModel(),
				originalCar.getBrand(),
				originalCar.getYear()
		);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(originalCar));
		doThrow(RuntimeException.class).when(carRepository).save(any(Car.class));

		// Act and Assert
		assertThatThrownBy(
				() -> carService.update(1L, carUpdateDTO)
		).isInstanceOf(DatabaseOperationException.class);
	}

	@Test
	public void partialUpdateCar__withValidInfo__shouldReturnCar() throws DatabaseOperationException, RegisterNotFoundException {
		// Arrange
		var originalCar = new Car(
				"Fiesta",
				"Ford",
				2010
		);
		var carPartialUpdateDTO = new CarPartialUpdateDTO(
				originalCar.getModel(),
				null,
				null
		);
		var updatedCar = carMapper.updateCar(originalCar, carPartialUpdateDTO);
		var updatedCarDto = carMapper.toModel(updatedCar);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(originalCar));
		when(carRepository.save(updatedCar)).thenReturn(updatedCar);

		// Act
		var result = carService.partialUpdate(1L, carPartialUpdateDTO);

		// Assert
		assertThat(result).isEqualTo(updatedCarDto);
	}

	@Test
	public void partialUpdateCar__withNotRegisteredId__shouldThrowsRegisterNotFoundException() {
		// Arrange
		var carPartialUpdateDTO = new CarPartialUpdateDTO(
				"UNO",
				null,
				null
		);

		when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Act and Assert
		assertThatThrownBy(
				() -> carService.partialUpdate(1L, carPartialUpdateDTO)
		).isInstanceOf(RegisterNotFoundException.class);
	}

	@Test
	public void partialUpdateCar__withInvalidInfo__shouldThrowsDatabaseOperationException() {
		// Arrange
		var originalCar = new Car(
				"Fiesta",
				"Ford",
				2010
		);
		var carPartialUpdateDTO = new CarPartialUpdateDTO(
				null,
				originalCar.getBrand(),
				1500
		);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(originalCar));
		doThrow(RuntimeException.class).when(carRepository).save(any(Car.class));

		// Act and Assert
		assertThatThrownBy(
				() -> carService.partialUpdate(1L, carPartialUpdateDTO)
		).isInstanceOf(DatabaseOperationException.class);
	}

	@Test
	public void deleteCar__withValidId__shouldReturnVoid() throws DatabaseOperationException, RegisterNotFoundException {
		// Arrange
		var car = new Car(
				"Fiesta",
				"Ford",
				2010
		);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));

		// Act and Assert
		carService.delete(1L);
	}

	@Test
	public void deleteCar__withNotRegisteredId__shouldThrowsRegisterNotFoundException() {
		// Arrange
		when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

		// Act and Assert
		assertThatThrownBy(
				() -> carService.delete(1L)
		).isInstanceOf(RegisterNotFoundException.class);
	}

	@Test
	public void deleteCar__withUnknownError__shouldThrowsDatabaseOperationException() {
		// Arrange
		var car = new Car(
				"Fiesta",
				"Ford",
				2010
		);

		when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
		doThrow(RuntimeException.class).when(carRepository).delete(any(Car.class));

		// Act and Assert
		assertThatThrownBy(
				() -> carService.delete(1L)
		).isInstanceOf(DatabaseOperationException.class);
	}
}
