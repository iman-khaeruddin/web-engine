package com.mospaze.werehouse.model;

import com.mospaze.werehouse.model.validate.RegisterValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import javax.websocket.OnMessage;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @NotNull
    private String first_name;

    @NotBlank
    @NotNull
    private String last_name;

    @NotBlank
    @NotNull
    @Pattern( regexp = "[^@]+@[^\\.]+\\..+", message = "email invalid")
    private String email;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "phone number invalid")
    private String phone_number;

    @NotBlank
    @NotNull
    private String password;

    @NotNull
    @Min(3)
    @Max(4)
    private Integer user_type;

    @NotNull(groups = RegisterValidation.class)
    @NotBlank(groups = RegisterValidation.class)
    private String otp;

    private String request_id;

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", password='" + password + '\'' +
                ", user_type=" + user_type +
                ", otp='" + otp + '\'' +
                ", request_id='" + request_id + '\'' +
                '}';
    }
}
