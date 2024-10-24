package com.ddmtchr.vitasofttesttask.security.service;

import com.ddmtchr.vitasofttesttask.exception.NotFoundException;
import com.ddmtchr.vitasofttesttask.repository.RoleRepository;
import com.ddmtchr.vitasofttesttask.repository.UserRepository;
import com.ddmtchr.vitasofttesttask.security.entity.ERole;
import com.ddmtchr.vitasofttesttask.security.entity.Role;
import com.ddmtchr.vitasofttesttask.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsernameStartingWith(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void promoteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Optional<Role> operatorRole = roleRepository.findByName(ERole.OPERATOR);
        Role role = operatorRole.orElseGet(() -> roleRepository.save(new Role(ERole.OPERATOR)));
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
