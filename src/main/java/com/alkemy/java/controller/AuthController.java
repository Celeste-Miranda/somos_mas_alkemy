package com.alkemy.java.controller;

import com.alkemy.java.dto.*;
import com.alkemy.java.exception.InvalidDataException;
import com.alkemy.java.service.IUserService;
import com.alkemy.java.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetailsService;


import javax.validation.Valid;


@Api(value= "Authentication Controller")
@RestController
@RequestMapping("/auth" )
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MessageSource messageSource;

    @Value("error.empty.register")
    private String errorUsernamePasswordIncorrect;
    
    
    
    @ApiOperation("User Authentication")
    @ApiResponses({ 
               @ApiResponse(code = 200, message = "Ok"),
               @ApiResponse(code = 400, message = "Bad Request"),
               @ApiResponse(code = 401, message = "Unauthorized"),
               @ApiResponse(code = 404, message = "Not Found"),
               @ApiResponse(code = 500, message = "Internal server error")
    })
    
    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequestDto authenticationRequest, BindingResult result) throws Exception {


         return new ResponseEntity<>(userService.createAuthentication(authenticationRequest), HttpStatus.OK);

    }

    
    @ApiOperation("User Registration")
    @ApiResponses({ 
               @ApiResponse(code = 200, message = "OK"), 
               @ApiResponse(code = 400, message = "Bad Request"),
               @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping( "/register")
    public ResponseEntity<?> registerUser (@Valid @RequestBody UserDtoRequest userDtoRequest,BindingResult result) {
        if (result.hasErrors())
            throw new InvalidDataException(result);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");


         return new ResponseEntity(userService.registerUser(userDtoRequest), headers, HttpStatus.OK);

    }
    
    @ApiOperation("Get User Details")
    @ApiResponses({ 
               @ApiResponse(code = 200, message = "OK"),
               @ApiResponse(code = 401, message= "Unauthorized"),
               @ApiResponse(code = 403, message = "Forbidden Access"),
               @ApiResponse(code = 404, message = "Not Found"),
               @ApiResponse(code = 500, message = "Internal server error")
    })
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserInformation (@PathVariable Long id, @RequestHeader(name = "Authorization", required = true ) String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(userService.getUserInformation(id, token),headers, HttpStatus.OK);
    }
}
