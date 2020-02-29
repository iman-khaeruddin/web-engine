package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mospaze.werehouse.entity.TransactionList;
import com.mospaze.werehouse.entity.TransactionListNew;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrendOccupancyDataResponse implements Serializable {
    private List<Warehouses> warehouse;
    private List<TrendOccupancyData> trend_occupancy_data;
    private List<SummaryWhStatus> summary_wh_status;
    private List<StockOnHand> stock_on_hand;
    private List<TransactionListNew> job_list;
    private List<com.mospaze.werehouse.entity.projector.TransactionList> stock_status;

    private BigDecimal total_page;
    private BigInteger total_element;
    private Integer from;
    private Integer to;

    @Override
    public String toString() {
        return "TrendOccupancyDataResponse{" +
                "warehouse=" + warehouse +
                ", trend_occupancy_data=" + trend_occupancy_data +
                ", summary_wh_status=" + summary_wh_status +
                ", stock_on_hand=" + stock_on_hand +
                ", job_list=" + job_list +
                ", total_page=" + total_page +
                ", total_element=" + total_element +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
