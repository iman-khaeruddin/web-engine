package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.model.DetailDistrictModel;
import com.mospaze.werehouse.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(RestConstant.INDEX_WAREHOUSE)
public class WarehouseController {

    private Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    @Autowired
    private MsWarehouseAutomationService msWarehouseAutomationService;

    @Autowired
    private MsWarehouseCategoryService msWarehouseCategoryService;

    @Autowired
    private MsWarehouseFeatureService msWarehouseFeatureService;

    @Autowired
    private MsWarehouseServiceService msWarehouseServiceService;

    @Autowired
    private com.mospaze.werehouse.service.MsWarehouseSpaceService msWarehouseSpaceService;

    @Autowired
    private MsWarehouseSpaceServiceService msWarehouseSpaceServiceService;

    @Autowired
    private MsWarehouseTypeService msWarehouseTypeService;

    @Autowired
    private MsUnitService msUnitService;

    @Autowired
    private MsCitiesService msCitiesService;

    @Autowired
    private MsWarehouseTemperatureService msWarehouseTemperatureService;

    @Autowired
    private MsWarehouseOperatingHourService msWarehouseOperatingHourService;

    @Autowired
    private Util util;


    /**
     * @param search_value
     * @return all cities
     */
    @GetMapping(value = RestConstant.GET_MASTER_CITIES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DetailCities> getCities(@RequestParam(value = "search_value", required = false) String search_value) throws IOException {
        return msCitiesService.GetDetailCities();
    }

    /**
     * This controller used for search district in add or edit warehouse and add or edit user and user profile.
     *
     * @param search_value
     * @return all districts
     */
    @GetMapping(value = RestConstant.GET_MASTER_DISTRICT, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DetailCities> getDistricts(@RequestParam(value = "search_value", required = false) String search_value) throws IOException {
        return msCitiesService.GetDetailDistricts();
    }

    /**
     * This controller used for search district in search warehouse.
     *
     * @param search_value
     * @return all districts
     */
    @GetMapping(value = RestConstant.GET_MASTER_DISTRICT_SEARCH_WAREHOUSE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DetailDistrictModel> getDsitrictsSearchWarehouse(@RequestParam(value = "search_value", required = false) String search_value){
        return msCitiesService.GetDetailDistrictsSearchWarehouse(search_value);
    }

    /**
     * @return all master automation
     */
    @GetMapping(value = RestConstant.GET_MASTER_AUTOMATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseAutomation> getWarehouseAutomation() {
        return msWarehouseAutomationService.getMasterWarehouseAutomation();
    }

    /**
     * @return all master category
     */
    @GetMapping(value = RestConstant.GET_MASTER_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseCategory> getWarehouseCategory() {
        return msWarehouseCategoryService.getMasterWarehouseCategory();
    }

    /**
     * @return all master feature
     */
    @GetMapping(value = RestConstant.GET_MASTER_FEATURE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseFeature> getWarehouseFeature() {
        return msWarehouseFeatureService.getMasterWarehouseFeature();
    }

    /**
     * @return all master service
     */
    @GetMapping(value = RestConstant.GET_MASTER_SERVICE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseService> getWarehouseService() {
        return msWarehouseServiceService.getMasterWarehouseService();
    }

    /**
     * @return all master space
     */
    @GetMapping(value = RestConstant.GET_MASTER_SPACE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseSpace> getWarehouseSpace() {
        return msWarehouseSpaceService.getMasterWarehouseSpace();
    }

    /**
     * @return all master space service
     */
    @GetMapping(value = RestConstant.GET_MASTER_SPACE_SERVICE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<com.mospaze.werehouse.entity.MsWarehouseSpaceService> getWarehouseSpaceService() {
        return msWarehouseSpaceServiceService.getMasterWarehouseSpaceService();
    }

    /**
     * @return all master type
     */
    @GetMapping(value = RestConstant.GET_MASTER_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseType> getWarehouseType() {
        return msWarehouseTypeService.getMasterWarehouseType();
    }

    /**
     * @return all master unit
     */
    @GetMapping(value = RestConstant.GET_MASTER_UNIT, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsUnit> getMsUnit() {
        return msUnitService.getMasterMsUnit();
    }

    /**
     * @return all master temperature
     */
    @GetMapping(value = RestConstant.GET_MASTER_TEMPERATURE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseTemperature> getMsTemperature() {
        return msWarehouseTemperatureService.getMasterWarehouseTemperature();
    }

    /**
     * @return all master operating hour
     */
    @GetMapping(value = RestConstant.GET_MASTER_OPERATING_HOUR, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<MsWarehouseOperatingHour> getMsOperatingHour() {
        return msWarehouseOperatingHourService.getMasterWarehouseOperatingHour();
    }
}
