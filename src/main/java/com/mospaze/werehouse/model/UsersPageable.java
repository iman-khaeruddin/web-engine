package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mospaze.werehouse.entity.projector.UserByRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersPageable implements Serializable {
    private List<UserByRole> detailUsers;
    private BigDecimal total_page;
    private BigInteger total_element;
    private Integer from;
    private Integer to;

    @Override
    public String toString() {
        return "UsersPageable{" +
                "detailUsers=" + detailUsers +
                ", total_page=" + total_page +
                ", total_element=" + total_element +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
