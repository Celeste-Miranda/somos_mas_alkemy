package com.alkemy.java.service;

import com.alkemy.java.dto.*;
import com.alkemy.java.model.User;

import java.util.NoSuchElementException;

import java.util.List;

public interface IUserService {

     JWTAuthResponse registerUser(UserDtoRequest user);
     UserDto updateUser(Long userId, UserDto userDto);
     boolean validedRole(Long id, String token);
     UserDtoResponse getUserInformation(Long id, String token);

     User delete(Long idUser) throws NoSuchElementException;

     User findById(Long idUser);

     List<UserDtoList> findAllUsers();

     boolean existsByEmail(String email);
     User registerUser(User user);

     AuthenticationResponseDto createAuthentication(AuthenticationRequestDto authenticationRequest) throws Exception;


}
