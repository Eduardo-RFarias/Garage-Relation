package br.unb.garage_relation.service.mapper;

import br.unb.garage_relation.controller.UserController;
import br.unb.garage_relation.model.User;
import br.unb.garage_relation.model.dto.request.UserPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.UserUpdateDTO;
import br.unb.garage_relation.model.dto.response.UserResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserMapper implements RepresentationModelAssembler<User, EntityModel<UserResponseDTO>> {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }

    public User updateUser(User user, UserUpdateDTO userUpdateDTO) {
        user.setUsername(userUpdateDTO.username());
        user.setEmail(userUpdateDTO.email());
        user.setFullName(userUpdateDTO.fullName());
        user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));

        return user;
    }

    public User updateUser(User user, UserPartialUpdateDTO userPartialUpdateDTO) {
        if (userPartialUpdateDTO.username() != null) {
            user.setUsername(userPartialUpdateDTO.username());
        }

        if (userPartialUpdateDTO.email() != null) {
            user.setEmail(userPartialUpdateDTO.email());
        }

        if (userPartialUpdateDTO.fullName() != null) {
            user.setFullName(userPartialUpdateDTO.fullName());
        }

        if (userPartialUpdateDTO.password() != null) {
            user.setPassword(passwordEncoder.encode(userPartialUpdateDTO.password()));
        }

        return user;
    }

    public EntityModel<UserResponseDTO> toModel(User entity) {
        var dto = toUserResponseDTO(entity);

        var model = EntityModel.of(dto);

        model.add(linkTo(methodOn(UserController.class).findById(dto.id())).withSelfRel());

        return model;
    }

    public CollectionModel<EntityModel<UserResponseDTO>> toCollectionModel(Iterable<? extends User> entities) {
        var models = new ArrayList<EntityModel<UserResponseDTO>>();

        for (var entity : entities) {
            models.add(toModel(entity));
        }

        var selfLink = linkTo(methodOn(UserController.class).list()).withSelfRel();

        return CollectionModel.of(models, selfLink);
    }
}
