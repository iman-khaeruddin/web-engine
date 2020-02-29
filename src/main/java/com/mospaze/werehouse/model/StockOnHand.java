package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockOnHand {
    private String product_family;
    private int qty;
    private String percentage;
    private int total_qty;
    private String warehouse_name;

    @Override
    public String toString() {
        return "StockOnHand{" +
                "product_family='" + product_family + '\'' +
                ", qty=" + qty +
                ", percentage='" + percentage + '\'' +
                ", total_qty=" + total_qty +
                ", warehouse_name='" + warehouse_name + '\'' +
                '}';
    }
}
