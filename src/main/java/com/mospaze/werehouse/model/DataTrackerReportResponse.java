package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 23/12/2019
 * @see java.io.Serializable
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataTrackerReportResponse implements Serializable {
    private Integer qty;
    private String os;
    private String browser;
    private String ip_address;
    private String city;
    private String district;
    private String percentage;
    private Integer grand_total;
}
