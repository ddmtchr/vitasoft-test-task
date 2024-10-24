package com.ddmtchr.vitasofttesttask.security.service;

import com.ddmtchr.vitasofttesttask.repository.RoleRepository;
import com.ddmtchr.vitasofttesttask.security.entity.ERole;
import com.ddmtchr.vitasofttesttask.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findRoleByName(ERole name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(name);
            return addRole(newRole);
        }
        return role.get();
    }

    public Role addRole(Role role) {
        return roleRepository.save(role);
    }
}
