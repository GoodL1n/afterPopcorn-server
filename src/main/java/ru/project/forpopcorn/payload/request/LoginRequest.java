package ru.project.forpopcorn.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty(message = "Nickname can't be empty")
    private String nickname;

    @NotEmpty(message = "Password can't be empty")
    private String password;
}
