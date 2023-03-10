package br.unb.garage_relation.controller;

import br.unb.garage_relation.model.dto.request.CarCreateDTO;
import br.unb.garage_relation.model.dto.request.CarPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.CarUpdateDTO;
import br.unb.garage_relation.model.dto.response.CarResponseDTO;
import br.unb.garage_relation.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.unb.garage_relation.Constants.*;
import static org.springframework.hateoas.IanaLinkRelations.SELF;


@RestController
@RequestMapping("/api/v1/car")
@Tag(name = "Car", description = "Endpoints for managing cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Finds all cars",
            description = "Finds all cars in the database",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    public CollectionModel<EntityModel<CarResponseDTO>> findAll() {
        return carService.findAll();
    }

    @GetMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Finds a car",
            description = "Finds a car in the database by its id",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    public EntityModel<CarResponseDTO> findById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @PostMapping(
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Creates a car",
            description = "Creates a car in the database",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<CarResponseDTO>> create(@RequestBody @Valid CarCreateDTO carCreateDTO) {
        var carCreated = carService.create(carCreateDTO);
        var location = carCreated.getRequiredLink(SELF).toUri();

        return ResponseEntity.created(location).body(carCreated);
    }

    @PutMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Updates a car",
            description = "Finds a car in the database by its id and updates it",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public EntityModel<CarResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CarUpdateDTO carUpdateDTO) {
        return carService.update(id, carUpdateDTO);
    }

    @PatchMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Updates a car partially",
            description = "Finds a car in the database by its id and updates it partially",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
            }
    )
    public EntityModel<CarResponseDTO> partialUpdate(@PathVariable Long id, @RequestBody @Valid CarPartialUpdateDTO carPartialUpdateDTO) {
        return carService.partialUpdate(id, carPartialUpdateDTO);
    }

    @DeleteMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Deletes a car",
            description = "Finds a car in the database by its id and deletes it",
            tags = {"Car"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
