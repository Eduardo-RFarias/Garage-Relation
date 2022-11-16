package br.unb.garage_relation.service.mapper;

import br.unb.garage_relation.controller.CarController;
import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.model.dto.response.CarResponseDTO;
import br.unb.garage_relation.service.mapper.interfaces.ICarMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CarMapper implements ICarMapper {
    @Override
    public CarResponseDTO toCarResponseDTO(Car car) {
        return new CarResponseDTO(
                car.getId(),
                car.getModel(),
                car.getBrand(),
                car.getYear()
        );
    }

    @Override
    public Car toCar(CarCreateDTO carCreateDTO) {
        return new Car(
                carCreateDTO.model(),
                carCreateDTO.brand(),
                carCreateDTO.year()
        );
    }

    @Override
    public Car updateCar(Car car, CarUpdateDTO carUpdateDTO) {
        car.setModel(carUpdateDTO.model());
        car.setBrand(carUpdateDTO.brand());
        car.setYear(carUpdateDTO.year());

        return car;
    }

    @Override
    public Car updateCar(Car car, CarPartialUpdateDTO carPartialUpdateDTO) {
        if (carPartialUpdateDTO.model() != null) {
            car.setModel(carPartialUpdateDTO.model());
        }

        if (carPartialUpdateDTO.brand() != null) {
            car.setBrand(carPartialUpdateDTO.brand());
        }

        if (carPartialUpdateDTO.year() != null) {
            car.setYear(carPartialUpdateDTO.year());
        }

        return car;
    }

    @Override
    public EntityModel<CarResponseDTO> toModel(Car entity) {
        var dto = toCarResponseDTO(entity);

        var model = EntityModel.of(dto);

        model.add(linkTo(methodOn(CarController.class).findById(dto.id())).withSelfRel());

        return model;
    }

    @Override
    public CollectionModel<EntityModel<CarResponseDTO>> toCollectionModel(Iterable<? extends Car> entities) {
        var models = new ArrayList<EntityModel<CarResponseDTO>>();

        for (var entity : entities) {
            models.add(toModel(entity));
        }

        var selfLink = linkTo(methodOn(CarController.class).list()).withSelfRel();

        return CollectionModel.of(models, selfLink);
    }
}
