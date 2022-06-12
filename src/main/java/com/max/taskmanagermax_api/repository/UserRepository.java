package com.max.taskmanagermax_api.repository;

import com.max.taskmanagermax_api.DTO.SignUpDTO;
import com.max.taskmanagermax_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    User findByUsername(String username);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    Optional<User> findById(Long id);

    //User updateUserRole(SignUpDTO userDTO, long id);
    
}
