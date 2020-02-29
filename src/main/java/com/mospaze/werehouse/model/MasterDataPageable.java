package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterDataPageable implements Serializable {
    private Object detail_master;
    private BigDecimal total_page;
    private BigDecimal total_element;
    private Integer from;
    private Integer to;

    @Override
    public String toString() {
        return "MasterDataPageable{" +
                "detail_master=" + detail_master +
                ", total_page=" + total_page +
                ", total_element=" + total_element +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
