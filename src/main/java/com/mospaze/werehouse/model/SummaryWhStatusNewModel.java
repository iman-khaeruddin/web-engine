package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 07/12/2019
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SummaryWhStatusNewModel {
    private String warehouse;

    private String inbound_in_plt;

    private String outbound_in_plt;

    private String throughput_in_plt;

    private String inbound_in_case;

    private String outbound_in_case;

    private String throughput_in_case;

    private String occupancy;

    private String date;

    @Override
    public String toString() {
        return "SummaryWhStatusNewModel{" +
                "warehouse='" + warehouse + '\'' +
                ", inbound_in_plt=" + inbound_in_plt +
                ", outbound_in_plt=" + outbound_in_plt +
                ", throughput_in_plt=" + throughput_in_plt +
                ", inbound_in_case=" + inbound_in_case +
                ", outbound_in_case=" + outbound_in_case +
                ", throughput_in_case=" + throughput_in_case +
                ", occupancy=" + occupancy +
                ", date='" + date + '\'' +
                '}';
    }
}
