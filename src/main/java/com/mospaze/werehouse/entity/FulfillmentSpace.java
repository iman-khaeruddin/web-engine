package com.mospaze.werehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "fulfillment_space")
public class FulfillmentSpace implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "booking_request_id")
    private int booking_request_id;

    @Column(name = "inbound_service")
    private int inbound_service;

    @Column(name = "outbound_service")
    private int outbound_service;

    @Column(name = "inbound_service_value")
    private int inbound_service_value;

    @Column(name = "outbound_service_value")
    private int outbound_service_value;

    @Column(name = "unit_inbound")
    private String unit_inbound;

    @Column(name = "unit_outbound")
    private String unit_outbound;

    @Column(name = "weight_inbound")
    private String weight_inbound;

    @Column(name = "weight_outbound")
    private String weight_outbound;

    @Column(name = "re_packing")
    private int re_packing;

    @Column(name = "custom_labeling")
    private int custom_labeling;

    @Column(name = "product_kiting")
    private int product_kiting;

    @Column(name = "return_logistics")
    private int return_logistics;

    @Column(name = "shrink_wrapping")
    private int shrink_wrapping;

    @Column(name = "cross_docking")
    private int cross_docking;

    @Column(name = "merger_in_transit")
    private int merger_in_transit;

    @Column(name = "other")
    private String other;
}
