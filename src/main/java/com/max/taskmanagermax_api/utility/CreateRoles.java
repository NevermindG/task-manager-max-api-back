package com.max.taskmanagermax_api.utility;

import com.max.taskmanagermax_api.entity.Role;
import com.max.taskmanagermax_api.enums.RoleName;
import com.max.taskmanagermax_api.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
//public class CreateRoles implements CommandLineRunner {
//
//    private final RoleService roleService;
//
//    public CreateRoles(RoleService roleService) {
//        this.roleService = roleService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        Role adminRole = new Role (RoleName.ROLE_ADMIN);
//        Role userRole= new Role (RoleName.ROLE_USER);
//        roleService.save(adminRole);
//        roleService.save(userRole);
//    }
//
//}
