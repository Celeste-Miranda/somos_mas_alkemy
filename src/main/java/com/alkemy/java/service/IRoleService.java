package com.alkemy.java.service;

import com.alkemy.java.dto.RoleDto;
import com.alkemy.java.model.Role;

public interface IRoleService {

    Role registerRole (RoleDto roleRequest);

    Role findByName(String name);
    boolean existsByName(String name);
    Role registerRole(Role role);
}
