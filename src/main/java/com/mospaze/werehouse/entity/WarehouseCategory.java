package com.mospaze.werehouse.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "warehouse_category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "warehouse_id")
    private int warehouse_id;

    @Column(name = "category_id")
    private int category_id;

    @Column(name = "inbound_price")
    private String inbound_price;

    @Column(name = "outbound_price")
    private String outbound_price;

    @Column(name = "inbound_uom")
    private String inbound_uom;

    @Column(name = "outbound_uom")
    private String outbound_uom;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "updated_at")
    private Timestamp updated_at;
}
