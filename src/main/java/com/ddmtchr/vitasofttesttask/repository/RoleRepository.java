package com.ddmtchr.vitasofttesttask.repository;

import com.ddmtchr.vitasofttesttask.security.entity.ERole;
import com.ddmtchr.vitasofttesttask.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
