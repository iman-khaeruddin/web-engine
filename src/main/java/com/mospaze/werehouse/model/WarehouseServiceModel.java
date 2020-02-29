package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 22/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class WarehouseServiceModel implements Serializable {
    private int id;
    private int status;
    private String service_price;
    private String services_name;
    private String uom;
}
