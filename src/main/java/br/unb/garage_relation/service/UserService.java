package br.unb.garage_relation.service;

import br.unb.garage_relation.exception.DatabaseOperationException;
import br.unb.garage_relation.exception.RegisterNotFoundException;
import br.unb.garage_relation.model.User;
import br.unb.garage_relation.model.dto.request.UserPartialUpdateDTO;
import br.unb.garage_relation.model.dto.request.UserUpdateDTO;
import br.unb.garage_relation.model.dto.response.UserResponseDTO;
import br.unb.garage_relation.repository.UserRepository;
import br.unb.garage_relation.service.mapper.UserMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CollectionModel<EntityModel<UserResponseDTO>> list() {
        var users = userRepository.findAll();
        return userMapper.toCollectionModel(users);
    }

    public EntityModel<UserResponseDTO> findById(Long id) {
        var user = userRepository.findById(id).orElseThrow(RegisterNotFoundException::new);
        return userMapper.toModel(user);
    }

    public EntityModel<UserResponseDTO> update(Long id, UserUpdateDTO userUpdateDTO) {
        var user = userRepository.findById(id).orElseThrow(RegisterNotFoundException::new);

        var userWithNewInfo = userMapper.updateUser(user, userUpdateDTO);

        try {
            var updatedUser = userRepository.save(userWithNewInfo);
            return userMapper.toModel(updatedUser);
        } catch (Exception e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public EntityModel<UserResponseDTO> partialUpdate(Long id, UserPartialUpdateDTO userPartialUpdateDTO) {
        User user = userRepository.findById(id).orElseThrow(RegisterNotFoundException::new);

        var userWithNewInfo = userMapper.updateUser(user, userPartialUpdateDTO);

        try {
            var updatedUser = userRepository.save(userWithNewInfo);
            return userMapper.toModel(updatedUser);
        } catch (Exception e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseOperationException(e.getMessage());
        }
    }
}
