package com.alkemy.java.controller;


import com.alkemy.java.dto.RoleDto;
import com.alkemy.java.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    IRoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleRequest) throws Exception{
        return new ResponseEntity<>(roleService.registerRole(roleRequest), HttpStatus.CREATED);
    }
}
