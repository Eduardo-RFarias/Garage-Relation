package br.unb.garage_relation.service.interfaces;

import br.unb.garage_relation.exception.DatabaseOperationException;
import br.unb.garage_relation.exception.RegisterNotFoundException;
import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.model.dto.response.CarResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ICarService {
    CollectionModel<EntityModel<CarResponseDTO>> findAll();

    EntityModel<CarResponseDTO> findById(Long id) throws RegisterNotFoundException;

    EntityModel<CarResponseDTO> create(CarCreateDTO carCreateDTO) throws DatabaseOperationException;

    void delete(Long id) throws RegisterNotFoundException, DatabaseOperationException;

    EntityModel<CarResponseDTO> update(Long id, CarUpdateDTO carUpdateDTO) throws RegisterNotFoundException, DatabaseOperationException;

    EntityModel<CarResponseDTO> partialUpdate(Long id, CarPartialUpdateDTO carPartialUpdateDTO) throws RegisterNotFoundException, DatabaseOperationException;
}
