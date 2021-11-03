package com.alkemy.java.service.impl;

import com.alkemy.java.dto.*;
import com.alkemy.java.exception.BadRequestException;
import com.alkemy.java.exception.ForbiddenException;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.User;
import com.alkemy.java.repository.RoleRepository;
import com.alkemy.java.repository.UserRepository;
import com.alkemy.java.service.IEmailService;
import com.alkemy.java.service.IUserService;
import com.alkemy.java.util.Roles;
import lombok.extern.slf4j.Slf4j;
import com.alkemy.java.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ModelMapper mapper = new ModelMapper();

    @Value("error.user.dont.exist")
    private String resourceNotFound;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("error.email.registered")
    private String errorPath;

    @Value("sendgrid.subject.welcome")
    private String welcome;

    @Autowired
    IEmailService emailService;

    @Value("error.service.user.forbidden")
    private String errorForbiddenUser;

    @Autowired
    MessageSource messageSource;

    @Value("error.user.notFoundID")
    private String idNotFound;

    @Value("error.empty.list")
    private String emptyList;

    @Value("error.username.password.incorrect")
    private String errorUsernamePasswordIncorrect;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           MessageSource messageSource,
                           PasswordEncoder passwordEncoder,
                           ModelMapper mapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public JWTAuthResponse registerUser(UserDtoRequest userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null)
            throw new RuntimeException(messageSource.getMessage(errorPath, null, Locale.getDefault()));

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setCreationDate(new Date());
        user.setLastUpdate(new Date());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhoto("url1");
        user.setRole(roleRepository.findById(userDto.getRoleId()).get());
        User newUser = userRepository.save(user);

        emailService.sendEmailWithTemplate(userDto,"WELCOME");

   return new JWTAuthResponse(loginUserRegister(userDto),mapper.map(user,UserDtoResponse.class));

    }


    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow( () ->
                new ResourceNotFoundException(messageSource.getMessage(resourceNotFound, null, Locale.getDefault())));

        if (userDto.getFirstName() != null)
            user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null)
            user.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null)
            user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null)
            user.setPassword(userDto.getPassword());
        if (userDto.getPhoto() != null)
            user.setPhoto(userDto.getPhoto());
        if (userDto.getRole() != null)
            user.setRole(userDto.getRole());
        user.setLastUpdate(new Date());

        user = userRepository.save(user);
        return mapper.map(user, UserDto.class);
    }


    @Override
    @Transactional
    public User delete(Long idUser) {
        Optional<User> userOld = Optional.of(userRepository.getById(idUser));
        User user;
        if (userOld.isPresent()) {
            user = userOld.get();
            userRepository.delete(user);
        } else {
            throw new ResourceNotFoundException(messageSource.getMessage(idNotFound, null, Locale.getDefault()));
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long idUser) throws NoSuchElementException {
        return userRepository.findById(idUser).orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(idNotFound, null, Locale.getDefault())));
    }


    private UserDtoRequest mapToDTO(User user) {
        return mapper.map(user, UserDtoRequest.class);
    }

    private User mapToEntity(UserDtoRequest userDto) {
        return mapper.map(userDto, User.class);
    }


    @Override
    public List<UserDtoList> findAllUsers() {
        List<UserDtoList> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            userDtos.add(mapper.map(user,UserDtoList.class));
        });
        if (userDtos.isEmpty()){
            throw new ResourceNotFoundException(messageSource.getMessage(emptyList, null, Locale.getDefault()));
        }

        return userDtos;
    }

    @Override
    public boolean validedRole(Long id, String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email);

        if (!(user.getId().equals(id) || user.getRole().getName().equals(Roles.ADMIN.getValue()))) {
            throw new ForbiddenException(messageSource.getMessage(errorForbiddenUser, null, Locale.getDefault()));
        }
        return true;
    }

    @Override
    public UserDtoResponse getUserInformation(Long id, String token){

        if(validedRole(id,token)){
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userRepository.findByEmail(email);
            return UserDtoResponse.userToDto(user);
        } else {
            throw new ForbiddenException(messageSource.getMessage(errorForbiddenUser, null, Locale.getDefault()));
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(User user) {
        user.setCreationDate(new Date());
        user.setLastUpdate(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    @Override
    public AuthenticationResponseDto createAuthentication(AuthenticationRequestDto authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadRequestException(messageSource.getMessage(errorUsernamePasswordIncorrect, null, Locale.getDefault()));
        }

       String jwt = jwtUtil.generateToken(userDetailsService.loadUserByUsername(authenticationRequest.getEmail()));

        return  new AuthenticationResponseDto(jwt);
    }

    private String loginUserRegister (UserDtoRequest userDtoRequest){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDtoRequest.getEmail(), userDtoRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(userDtoRequest.getEmail());

        return jwtUtil.generateToken(userDetails);
    }
}
