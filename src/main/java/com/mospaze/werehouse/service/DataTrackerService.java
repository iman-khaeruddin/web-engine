package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.DataTrackerReportResponse;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 23/12/2019
 */
public interface DataTrackerService {
    /**
     * @param month
     * @param date
     * @return list of {@link DataTrackerReportResponse}
     */
    List<DataTrackerReportResponse> reportDataTrackerOs(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of {@link DataTrackerReportResponse}
     */
    List<DataTrackerReportResponse> reportDataTrackerBrowser(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of {@link DataTrackerReportResponse}
     */
    List<DataTrackerReportResponse> reportDataTrackerIpAddress(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of {@link DataTrackerReportResponse}
     */
    List<DataTrackerReportResponse> reportDataTrackerCity(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of {@link DataTrackerReportResponse}
     */
    List<DataTrackerReportResponse> reportDataTrackerDistrict(String month, String date);
}
