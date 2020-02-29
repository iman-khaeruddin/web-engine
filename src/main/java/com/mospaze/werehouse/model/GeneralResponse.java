package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse {
    private String code;
    private String en_message;
    private String id_message;
    private Object data;

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "code='" + code + '\'' +
                ", en_message='" + en_message + '\'' +
                ", id_message='" + id_message + '\'' +
                ", detail_warehouse=" + data +
                '}';
    }
}
