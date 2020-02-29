package com.mospaze.werehouse.model;

import lombok.*;

import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JWT  implements Serializable {
    private String username;
    private String name;
    private String email;
    private String user_type;
    private int user_id;
    private int role_id;

    @Override
    public String toString() {
        return "JWT{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", user_type='" + user_type + '\'' +
                ", user_id=" + user_id +
                ", role_id=" + role_id +
                '}';
    }
}
