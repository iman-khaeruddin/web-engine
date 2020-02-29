package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SummaryWhStatus {
    private String date;
    private Integer inbound_in_plt;
    private Integer outbound_in_plt;
    private Integer throughput_in_plt;
    private Integer inbound_in_case;
    private Integer outbound_in_case;
    private Integer throughput_in_case;
    private double occupancy;
    private String warehouse_name;

    @Override
    public String toString() {
        return "SummaryWhStatus{" +
                "date='" + date + '\'' +
                ", inbound_in_plt=" + inbound_in_plt +
                ", outbound_in_plt=" + outbound_in_plt +
                ", throughput_in_plt=" + throughput_in_plt +
                ", inbound_in_case=" + inbound_in_case +
                ", outbound_in_case=" + outbound_in_case +
                ", throughput_in_case=" + throughput_in_case +
                ", occupancy=" + occupancy +
                ", warehouse_name='" + warehouse_name + '\'' +
                '}';
    }
}
