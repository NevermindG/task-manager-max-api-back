package com.max.taskmanagermax_api.service;

import com.max.taskmanagermax_api.DTO.SignUpDTO;
import com.max.taskmanagermax_api.entity.Role;
import com.max.taskmanagermax_api.entity.User;
import com.max.taskmanagermax_api.enums.RoleName;
import com.max.taskmanagermax_api.exceptions.ResourceNotFoundException;
import com.max.taskmanagermax_api.repository.UserRepository;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.ModelMapper;

@Service
@Transactional
public class UserService {

    private final ModelMapper       modelMapper;
    
    private final UserRepository userRepository;
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    
    public User getByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> getByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }
    
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public void save(User user) {
        userRepository.save(user);
    }
    
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    
//    public SignUpDTO updateUserRole(SignUpDTO userDTO, long id) {
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
//
//        //change the type enum role of the user
//        Set<Role> roles = new HashSet<>();
//        roles.stream().filter(x -> x.getRoleName().equals(RoleName.ROLE_USER)).close();//(RoleService.getByRoleName(RoleName.ROLE_USER).get());
//        user.setRoles(roles);
//
//        User updateUser = userRepository.save(user);
//        return mappingDTO(updateUser);
//    }

    private SignUpDTO mappingDTO(User user) {
        return modelMapper.map(user, SignUpDTO.class);
    }
}
