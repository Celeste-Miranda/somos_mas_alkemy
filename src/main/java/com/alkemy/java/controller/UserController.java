
package com.alkemy.java.controller;

import com.alkemy.java.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.alkemy.java.model.User;
import com.alkemy.java.dto.UserDto;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.MessageSource;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private IUserService userService;



    @Autowired
    MessageSource messageSource;

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        UserDto user = userService.updateUser(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<?> delete(@PathVariable Long idUser) {
        User user = userService.delete(idUser);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @GetMapping(value = "/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){

        return ResponseEntity.ok().body(userService.findAllUsers());
    }


}