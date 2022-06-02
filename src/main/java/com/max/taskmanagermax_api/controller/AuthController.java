package com.max.taskmanagermax_api.controller;

import com.max.taskmanagermax_api.DTO.SignInDTO;
import com.max.taskmanagermax_api.DTO.SignUpDTO;
import com.max.taskmanagermax_api.entity.Role;
import com.max.taskmanagermax_api.entity.User;
import com.max.taskmanagermax_api.enums.RoleName;
import com.max.taskmanagermax_api.security.DTO.JwtAuthResponseDTO;
import com.max.taskmanagermax_api.security.JwtTokenProvider;
import com.max.taskmanagermax_api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.max.taskmanagermax_api.service.UserService;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping ("/api/auth")
@CrossOrigin
@SuppressWarnings({"all"})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService           userService;
    private final RoleService           roleService;
    private final PasswordEncoder  passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider, RoleService roleService) {
        
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @PostMapping ("/signIn")
    public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@RequestBody SignInDTO signInDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDTO.getUsernameOrEmail(), signInDTO.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO(token, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtAuthResponseDTO, HttpStatus.OK);
    }
    
    @GetMapping ("/signIn/{id}")
    public ResponseEntity<User> knownUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.of(userService.findById(id));
    }
    
    
    @PostMapping ("/signUp")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        if (userService.existByUsername(signUpDTO.getUsername())) {
            return new ResponseEntity<>("Ese nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        }
        if (userService.existByEmail(signUpDTO.getEmail())) {
            return new ResponseEntity<>("Ese email de usuario ya existe", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEstado(1);
        user.setNombre(signUpDTO.getNombre());
        user.setApellido(signUpDTO.getApellido());
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));


        Set<Role> role = new HashSet<>();
        role.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
        
        if (signUpDTO.getRoles().contains("ROLE_ADMIN"))
            role.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
        user.setRoles(role);
        userService.save(user);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Usuario creado con éxito");
        response.put("status", HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
