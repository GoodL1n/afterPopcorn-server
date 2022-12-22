package ru.project.forpopcorn.payload.request;

import lombok.Data;
import ru.project.forpopcorn.annotations.PasswordMatches;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@PasswordMatches
public class SignupRequest {

    @NotEmpty(message = "Required field")
    private String nickname;
    @NotEmpty(message = "Required field")
    @Min(value = 5, message = "Minimum of 5 characters")
    private String password;
    private String confirmPassword;
}
