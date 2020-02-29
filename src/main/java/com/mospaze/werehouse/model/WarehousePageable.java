package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehousePageable extends GeneralResponse implements Serializable {
    private List<DetailWarehouse> detailWarehouses;
    private BigInteger total_page;
    private BigInteger total_element;
    private Integer from;
    private Integer to;
}
