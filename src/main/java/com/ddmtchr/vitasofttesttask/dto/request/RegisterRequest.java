package com.ddmtchr.vitasofttesttask.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<String> roles;
}
