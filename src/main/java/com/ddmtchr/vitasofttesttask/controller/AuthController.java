package com.ddmtchr.vitasofttesttask.controller;

import com.ddmtchr.vitasofttesttask.dto.request.LoginRequest;
import com.ddmtchr.vitasofttesttask.dto.request.RegisterRequest;
import com.ddmtchr.vitasofttesttask.dto.response.JwtResponse;
import com.ddmtchr.vitasofttesttask.security.entity.ERole;
import com.ddmtchr.vitasofttesttask.security.entity.Role;
import com.ddmtchr.vitasofttesttask.security.entity.User;
import com.ddmtchr.vitasofttesttask.security.jwt.JwtUtils;
import com.ddmtchr.vitasofttesttask.security.service.RoleService;
import com.ddmtchr.vitasofttesttask.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(generateJwtResponse(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = new User(request.getUsername(), encoder.encode(request.getPassword()));
        Set<String> rolesString = request.getRoles();
        Set<Role> roles = new HashSet<>();
        if (rolesString == null || rolesString.isEmpty()) {
            Role userRole = roleService.findRoleByName(ERole.USER); // by default
            roles.add(userRole);
        } else {
            rolesString.forEach((role) -> {
                ERole eRole = ERole.valueOf(role);
                Role userRole = roleService.findRoleByName(eRole);
                roles.add(userRole);
            });
        }
        user.setRoles(roles);
        userService.addUser(user);
        return new ResponseEntity<>(generateJwtResponse(request.getUsername(), request.getPassword()), HttpStatus.CREATED);
    }

    private JwtResponse generateJwtResponse(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }
}
