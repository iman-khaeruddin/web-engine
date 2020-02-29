package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HandlingService {
    private String product_type;
    private String service_type;
    private String qty;
    private String uom;
    private String price;
}
