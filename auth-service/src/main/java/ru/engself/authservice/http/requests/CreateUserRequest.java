package ru.engself.authservice.http.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotBlank(message = "Firstname cannot be blank")
    @Size(min = 2, max = 20, message = "Firstname must be between 2 and 20 characters")
    String firstname;

    @NotBlank(message = "Lastname cannot be blank")
    @Size(min = 2, max = 20, message = "Lastname must be between 2 and 20 characters")
    String lastname;
}
