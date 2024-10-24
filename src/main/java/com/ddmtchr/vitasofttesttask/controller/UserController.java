package com.ddmtchr.vitasofttesttask.controller;

import com.ddmtchr.vitasofttesttask.security.entity.User;
import com.ddmtchr.vitasofttesttask.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/by-name")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/{id}/promote")
    public ResponseEntity<?> promoteUser(@PathVariable Long id) {
        userService.promoteUser(id);
        return ResponseEntity.ok().build();
    }

}
