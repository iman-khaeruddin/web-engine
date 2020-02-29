package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AdditionalService {
    private String service_name;
    private String uom;
    private String price;
}
