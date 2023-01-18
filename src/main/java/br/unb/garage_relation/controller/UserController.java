package br.unb.garage_relation.controller;

import br.unb.garage_relation.model.dto.request.UserPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.UserUpdateDTO;
import br.unb.garage_relation.model.dto.response.UserResponseDTO;
import br.unb.garage_relation.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.unb.garage_relation.Constants.*;

@RestController
@Tag(name = "User", description = "Endpoints for user management")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Finds all users",
            description = "Finds all users in the database",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
            }
    )
    public CollectionModel<EntityModel<UserResponseDTO>> list() {
        return userService.list();
    }

    @GetMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Finds a user",
            description = "Finds a user in the database by its id",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    public EntityModel<UserResponseDTO> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Updates a user",
            description = "Updates a user in the database by its id",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    public EntityModel<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserUpdateDTO userRequestDTO) {
        return userService.update(id, userRequestDTO);
    }

    @PatchMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML},
            consumes = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Partial updates a user",
            description = "Partial updates a user in the database by its id",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    public EntityModel<UserResponseDTO> partialUpdate(@PathVariable Long id, @RequestBody UserPartialUpdateDTO userRequestDTO) {
        return userService.partialUpdate(id, userRequestDTO);
    }

    @DeleteMapping(
            value = "{id}",
            produces = {APPLICATION_JSON, APPLICATION_XML, APPLICATION_YAML}
    )
    @Operation(
            summary = "Deletes a user",
            description = "Deletes a user in the database by its id",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
