package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 * @see java.io.Serializable
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseCategoryModel implements Serializable {
    private int id;
    private int status;
    private String category_name;
    private String inbound_price;
    private String outbound_price;
    private String inbound_uom;
    private String outbound_uom;
}
