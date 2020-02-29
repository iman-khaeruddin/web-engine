package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ValidateOTPRequest implements Serializable {
    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "phone number invalid")
    private String phone;

    @NotBlank
    @NotNull
    private String otp;

    @Override
    public String toString() {
        return "ValidateOTPRequest{" +
                "phone='" + phone + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
