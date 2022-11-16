package br.unb.garage_relation.service;

import br.unb.garage_relation.exception.DatabaseOperationException;
import br.unb.garage_relation.exception.RegisterNotFoundException;
import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.model.dto.response.CarResponseDTO;
import br.unb.garage_relation.repository.ICarRepository;
import br.unb.garage_relation.service.interfaces.ICarService;
import br.unb.garage_relation.service.mapper.interfaces.ICarMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class CarService implements ICarService {
    private final ICarRepository carRepository;
    private final ICarMapper carMapper;

    public CarService(ICarRepository carRepository, ICarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    @Override
    public CollectionModel<EntityModel<CarResponseDTO>> findAll() {
        var cars = carRepository.findAll();
        return carMapper.toCollectionModel(cars);
    }

    @Override
    public EntityModel<CarResponseDTO> findById(Long id) throws RegisterNotFoundException {
        var car = carRepository.findById(id);

        if (car.isEmpty()) {
            throw new RegisterNotFoundException();
        }

        return carMapper.toModel(car.get());
    }

    @Override
    public EntityModel<CarResponseDTO> create(CarCreateDTO carCreateDTO) throws DatabaseOperationException {
        var car = carMapper.toCar(carCreateDTO);

        try {
            var savedCar = carRepository.save(car);
            return carMapper.toModel(savedCar);
        } catch (Exception e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws RegisterNotFoundException, DatabaseOperationException {
        var car = carRepository.findById(id).orElseThrow(RegisterNotFoundException::new);
        try {
            carRepository.delete(car);
        } catch (Exception e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    @Override
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

    @Override
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
