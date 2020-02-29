package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String session;
    private List<String> permission;
    private String user_type;
    private int user_id;
    private Integer id_super_user;
    private String name;
    private String email;
    private String username;
    private int role_id;

    @Override
    public String toString() {
        return "LoginResponse{" +
                "session='" + session + '\'' +
                ", permission=" + permission +
                ", user_type='" + user_type + '\'' +
                ", user_id=" + user_id +
                ", id_super_user=" + id_super_user +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role_id=" + role_id +
                '}';
    }
}
