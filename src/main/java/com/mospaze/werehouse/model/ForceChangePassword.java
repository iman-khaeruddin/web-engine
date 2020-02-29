package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ForceChangePassword {
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "phone number invalid")
    private String username;

    @NotNull
    @NotBlank
    private String old_password;

    @NotNull
    @NotBlank
    private String new_password;

    @Override
    public String toString() {
        return "ForceChangePassword{" +
                "username='" + username + '\'' +
                ", old_password='" + old_password + '\'' +
                ", new_password='" + new_password + '\'' +
                '}';
    }
}
