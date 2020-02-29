package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GetNumber {
    private Integer max_size;
    private BigDecimal average;
}
