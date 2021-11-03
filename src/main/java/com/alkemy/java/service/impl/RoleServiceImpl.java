package com.alkemy.java.service.impl;

import com.alkemy.java.dto.RoleDto;
import com.alkemy.java.exception.ResourceNotFoundException;
import com.alkemy.java.model.Role;
import com.alkemy.java.repository.RoleRepository;
import com.alkemy.java.service.IRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MessageSource messageSource;

    @Value("error.role.nameNotFound")
    private String messageDontExist;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper mapper) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public Role registerRole (RoleDto roleRequest){

        Role role = mapToEntity(roleRequest);
        role.setName(roleRequest.getName());
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException(messageSource.getMessage(messageDontExist, null, Locale.getDefault())));
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role registerRole(Role role) {
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        return roleRepository.save(role);
    }

    private Role mapToEntity(RoleDto roleRequest) {
        return mapper.map(roleRequest, Role.class);
    }
}
