package com.mospaze.werehouse.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Data
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class LoginRequest {
    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "phone number invalid")
    private String username;

    @NotBlank
    @NotNull
    private String password;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
