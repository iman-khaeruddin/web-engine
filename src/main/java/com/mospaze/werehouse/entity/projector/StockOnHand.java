package com.mospaze.werehouse.entity.projector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockOnHand implements Serializable {
    @Id
    Integer id;

    @Column(name = "jml")
    private Integer jml;

    @Column(name = "product_family")
    private String product_family;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "warehouse")
    private String warehouse_name;

    @Override
    public String toString() {
        return "StockOnHand{" +
                "id=" + id +
                ", jml=" + jml +
                ", product_family='" + product_family + '\'' +
                ", qty=" + qty +
                ", warehouse_name='" + warehouse_name + '\'' +
                '}';
    }
}
