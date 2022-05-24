package com.max.taskmanagermax_api.controller;

import com.max.taskmanagermax_api.entity.User;
import com.max.taskmanagermax_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import static com.max.taskmanagermax_api.utility.CrossOrigin.URL_CROSS_ORIGIN;

@RestController
@RequestMapping ("/api/")
@CrossOrigin(origins = URL_CROSS_ORIGIN)
public class UsersController {
    
    private final UserService userService;
    
    public UsersController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping ("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
