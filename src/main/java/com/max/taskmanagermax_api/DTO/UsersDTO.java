package com.max.taskmanagermax_api.DTO;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * This DTO class is for update users roles
 */
@Getter
@Setter
public class UsersDTO {
    
    @NotBlank
    private String      nombre;
    @NotBlank
    private String      apellido;
    @NotBlank
    private String      username;
    @Email
    private String      email;
    private Set<String> roles = new HashSet<>();
    
}
