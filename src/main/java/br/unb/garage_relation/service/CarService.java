package br.unb.garage_relation.service;

import br.unb.garage_relation.exception.DatabaseOperationException;
import br.unb.garage_relation.exception.RegisterNotFoundException;
import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.model.dto.response.CarResponseDTO;
import br.unb.garage_relation.repository.CarRepository;
import br.unb.garage_relation.service.mapper.CarMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CarService {
	private final CarRepository carRepository;
	private final AuthService authService;
	private final CarMapper carMapper;

	public CarService(CarRepository carRepository, CarMapper carMapper, AuthService authService) {
		this.carRepository = carRepository;
		this.carMapper = carMapper;
		this.authService = authService;
	}

	public CollectionModel<EntityModel<CarResponseDTO>> findAll() {
		var cars = carRepository.findAll();
		return carMapper.toCollectionModel(cars);
	}

	public EntityModel<CarResponseDTO> findById(Long id) throws RegisterNotFoundException {
		var car = carRepository.findById(id);

		if (car.isEmpty()) {
			throw new RegisterNotFoundException();
		}

		return carMapper.toModel(car.get());
	}

	public EntityModel<CarResponseDTO> create(CarCreateDTO carCreateDTO) {
		var car = carMapper.toCar(carCreateDTO);
		var owner = authService.getAuthenticatedUser();

		car.setOwner(owner);

		try {
			var savedCar = carRepository.save(car);
			return carMapper.toModel(savedCar);
		} catch (Exception e) {
			throw new DatabaseOperationException(e.getMessage());
		}
	}

	public void delete(Long id) throws RegisterNotFoundException, DatabaseOperationException {
		var car = carRepository.findById(id).orElseThrow(RegisterNotFoundException::new);
		try {
			carRepository.delete(car);
		} catch (Exception e) {
			throw new DatabaseOperationException(e.getMessage());
		}
	}

	public EntityModel<CarResponseDTO> update(Long id, CarUpdateDTO carUpdateDTO) throws RegisterNotFoundException, DatabaseOperationException {
		var carToUpdate = carRepository.findById(id).orElseThrow(RegisterNotFoundException::new);
		var carWithNewInfo = carMapper.updateCar(carToUpdate, carUpdateDTO);
		try {
			var updatedCar = carRepository.save(carWithNewInfo);
			return carMapper.toModel(updatedCar);
		} catch (Exception e) {
			throw new DatabaseOperationException(e.getMessage());
		}
	}
	
	public EntityModel<CarResponseDTO> partialUpdate(Long id, CarPartialUpdateDTO carPartialUpdateDTO) throws DatabaseOperationException, RegisterNotFoundException {
		Car carToUpdate = carRepository.findById(id).orElseThrow(RegisterNotFoundException::new);
		var carWithNewInfo = carMapper.updateCar(carToUpdate, carPartialUpdateDTO);
		try {
			var updatedCar = carRepository.save(carWithNewInfo);
			return carMapper.toModel(updatedCar);
		} catch (Exception e) {
			throw new DatabaseOperationException(e.getMessage());
		}
	}
}
