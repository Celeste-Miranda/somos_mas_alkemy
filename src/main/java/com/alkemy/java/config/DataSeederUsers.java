package com.alkemy.java.config;

import com.alkemy.java.model.Role;
import com.alkemy.java.model.User;
import com.alkemy.java.repository.*;
import com.alkemy.java.service.IRoleService;
import com.alkemy.java.service.IUserService;
import com.alkemy.java.util.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class DataSeederUsers {

    private static final Logger log = LoggerFactory.getLogger(DataSeederUsers.class);

    private static final int CANT_USERS = 10;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Bean
    CommandLineRunner initDatabaseUsers(RoleRepository roleRepository,
                                   UserRepository userRepository) {

        return args -> {
            createDefaultRoles();
            createDefaultUsers();
            createDefaultAdmins();
        };
    }

    void createDefaultRoles() {
        Role roleUser = new Role();
        roleUser.setName(Roles.USER.getValue());
        roleUser.setDescription("Role for Users.");

        Role roleAdmin = new Role();
        roleAdmin.setName(Roles.ADMIN.getValue());
        roleAdmin.setDescription("Role for Admins.");

        List<Role> roles = new ArrayList<>();
        Collections.addAll(roles, roleUser, roleAdmin);

        saveRoles(roles);
    }

    void saveRoles(List<Role> roles){
        for (Role role: roles){
            if(!roleService.existsByName(role.getName())){
                roleService.registerRole(role);
            }
        }
    }

    void createDefaultUsers() {
        String nameExample = "Usario%d";
        String lastNameExample = "Nuevo";
        String emailExample = "UsuarioMail%d@mail.com";
        String passwordExample = "123";
        String photoExample = "Foto%d.jpg";

        Role roleExample = roleService.findByName(Roles.USER.getValue());

        List<User> userList = new ArrayList<>();

        for (int i = 0; i <= CANT_USERS; i++) {
            User userAux = new User();
            userAux.setFirstName(String.format(nameExample,i));
            userAux.setLastName(lastNameExample);
            userAux.setEmail(String.format(emailExample,i));
            userAux.setPassword(passwordExample);
            userAux.setPhoto(String.format(photoExample,i));
            userAux.setRole(roleExample);
            userList.add(userAux);
        }
        saveUsers(userList);
    }

    void createDefaultAdmins() {
        String nameExample = "Admin%d";
        String lastNameExample = "Nuevo";
        String emailExample = "AdminMail%d@mail.com";
        String passwordExample = "123";
        String photoExample = "Foto%d.jpg";

        Role roleExample = roleService.findByName(Roles.ADMIN.getValue());

        List<User> adminList = new ArrayList<>();

        for (int i = 0; i <= CANT_USERS; i++) {
            User userAux = new User();
            userAux.setFirstName(String.format(nameExample,i));
            userAux.setLastName(lastNameExample);
            userAux.setEmail(String.format(emailExample,i));
            userAux.setPassword(passwordExample);
            userAux.setPhoto(String.format(photoExample,i));
            userAux.setRole(roleExample);
            adminList.add(userAux);
        }
        saveUsers(adminList);
    }

    void saveUsers(List<User> users) {
        for (User user: users){
            if(!userService.existsByEmail(user.getEmail())){
                userService.registerUser(user);
            }
        }
    }



}
