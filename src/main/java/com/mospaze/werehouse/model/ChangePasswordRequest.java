package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    private String new_password;

    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "new_password='" + new_password + '\'' +
                '}';
    }
}
