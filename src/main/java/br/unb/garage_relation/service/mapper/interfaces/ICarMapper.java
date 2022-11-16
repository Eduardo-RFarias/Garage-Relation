package br.unb.garage_relation.service.mapper.interfaces;

import br.unb.garage_relation.model.Car;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.model.dto.response.CarResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface ICarMapper extends RepresentationModelAssembler<Car, EntityModel<CarResponseDTO>> {
    CarResponseDTO toCarResponseDTO(Car car);

    Car toCar(CarCreateDTO carCreateDTO);

    Car updateCar(Car car, CarUpdateDTO carUpdateDTO);

    Car updateCar(Car car, CarPartialUpdateDTO carPartialUpdateDTO);
}
