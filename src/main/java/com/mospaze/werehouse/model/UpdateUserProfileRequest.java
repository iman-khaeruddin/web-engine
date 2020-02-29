package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * @author Iman Khaeruddin
 * @since 25/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateUserProfileRequest {

    private int id;

    @NotNull
    @NotBlank
    private String first_name;

    @NotNull
    @NotBlank
    private String last_name;

    @NotNull
    @NotBlank
    @Pattern( regexp = "[^@]+@[^\\.]+\\..+", message = "email invalid")
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?:[0-9] ?){6,14}[0-9]$", message = "phone number invalid")
    private String phone;

    private String company_name;

    private String company_url;

    private String company_tax_number;

    @NotNull
    @Min(1)
    private Integer country_id;

    private String country_name;

    private String job_tittle;

    @NotNull
    @Min(1)
    private Integer state_id;

    private String state_name;

    @NotNull
    @Min(1)
    private Integer city_id;

    private String city_name;

    @NotNull
    @Min(1)
    private Integer district_id;

    private String district_name;

    private String postalcode;
    private String address;

    private String password;

    @Min(0)@Max(1)
    private int status;

    @Min(1)@Max(5)
    private int role;

    private String role_name;

    private boolean send_email_confirmation;

    @Override
    public String toString() {
        return "UpdateUserProfileRequest{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", company_name='" + company_name + '\'' +
                ", company_url='" + company_url + '\'' +
                ", company_tax_number='" + company_tax_number + '\'' +
                ", country_id=" + country_id +
                ", country_name='" + country_name + '\'' +
                ", job_tittle='" + job_tittle + '\'' +
                ", state_id=" + state_id +
                ", state_name='" + state_name + '\'' +
                ", city_id=" + city_id +
                ", city_name='" + city_name + '\'' +
                ", district_id=" + district_id +
                ", district_name='" + district_name + '\'' +
                ", postalcode='" + postalcode + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", role=" + role +
                ", role_name='" + role_name + '\'' +
                ", send_email_confirmation=" + send_email_confirmation +
                '}';
    }
}
