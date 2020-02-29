package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.model.DetailWarehouse;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.WarehousePageable;
import com.mospaze.werehouse.service.WarehouseService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(RestConstant.INDEX_WAREHOUSE)
public class SearchController {

    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private Util util;

    /**
     * Return all warehouse with pageable.
     *
     * @param page
     * @param size
     * @return {@link WarehousePageable}
     */
    @GetMapping(value = RestConstant.GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public WarehousePageable getWarehouse(@RequestParam (name = "page") Integer page,
                                                @RequestParam (name = "size") Integer size) {

        return warehouseService.getAllDataWarehousePageable(page, size);
    }

    /**
     * Retrieves a warehouse with the given ID.
     *
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getWarehouseDetail(@PathVariable("id") int id) {

        GeneralResponse generalResponse = new GeneralResponse();

        generalResponse = warehouseService.getDetailWarehouse(id);

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Returns all warehouse with the given PARAMs.
     *
     * @param cities
     * @param size_min
     * @param size_max
     * @param unit
     * @param feature
     * @param space_service
     * @param category
     * @param space
     * @param type
     * @param service
     * @param automation
     * @param is_racking
     * @param temperature
     * @param district
     * @param page
     * @param size
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL_BY_FILTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehousePageable> getWarehouseByFilter(@RequestHeader (name = "session", required = false) String session,
                                                                  @RequestParam (name = "cities", required = false) String cities,
                                                                  @RequestParam (name = "size_min", required = false) Integer size_min,
                                                                  @RequestParam (name = "size_max", required = false) Integer size_max,
                                                                  @RequestParam (name = "unit", required = false) Integer unit,
                                                                  @RequestParam (name = "feature", required = false) String feature,
                                                                  @RequestParam (name = "space_service", required = false) String space_service,
                                                                  @RequestParam (name = "category", required = false) String category,
                                                                  @RequestParam (name = "space", required = false) String space,
                                                                  @RequestParam (name = "type", required = false) String type,
                                                                  @RequestParam (name = "service", required = false) String service,
                                                                  @RequestParam (name = "automation", required = false) String automation,
                                                                  @RequestParam (name = "is_racking", required = false) Integer is_racking,
                                                                  @RequestParam (name = "temperature", required = false) String temperature,
                                                                  @RequestParam (name = "district", required = false) String district,
                                                                  @RequestParam (name = "is_first_class", required = false) Integer is_first_class,
                                                                  @RequestParam (name = "is_partner", required = false) Integer is_partner,
                                                                  @RequestParam (name = "page") Integer page,
                                                                  @RequestParam (name = "size") Integer size) {

        WarehousePageable warehousePageable = new WarehousePageable();

        try{
            if(session != null){
                JWT jwt = util.parseJWTToken(session);

                if (jwt == null) {
                    warehousePageable.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                    warehousePageable.setEn_message(Constant.getEnMessage(warehousePageable.getCode()));
                    warehousePageable.setId_message(Constant.getInMessage(warehousePageable.getCode()));

                    return new ResponseEntity<>(warehousePageable, HttpStatus.OK);
                }

                logger.info("JWT PARSED : {}",jwt.toString());
            }

            if(cities != null)
                Arrays.asList(cities.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(feature != null)
                Arrays.asList(feature.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(space_service != null)
                Arrays.asList(space_service.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(category != null)
                Arrays.asList(category.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(space != null)
                Arrays.asList(space.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(type != null)
                Arrays.asList(type.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(service != null)
                Arrays.asList(service.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(automation != null)
                Arrays.asList(automation.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(temperature != null)
                Arrays.asList(temperature.split(",")).stream().mapToInt(Integer::parseInt).toArray();

            if(district != null) {
                Arrays.asList(district.split(",")).stream().mapToInt(Integer::parseInt).toArray();
            }

            warehousePageable = warehouseService.getAllDataByFilterWarehousePageable(cities, size_min, size_max, feature, space_service, category, space, type, service, automation, unit, is_racking, page, size, temperature, district, is_first_class, is_partner);

        } catch (MalformedJwtException mje){
            logger.error("see stack trace, {}", mje.getMessage());
            logger.error(mje.getMessage());
            mje.printStackTrace();

            warehousePageable.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            warehousePageable.setEn_message(Constant.getEnMessage(warehousePageable.getCode()));
            warehousePageable.setId_message(Constant.getInMessage(warehousePageable.getCode()));
        } catch (ExpiredJwtException eje){
            logger.error("see stack trace, {}", eje.getMessage());
            logger.error(eje.getMessage());
            eje.printStackTrace();

            warehousePageable.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            warehousePageable.setEn_message(Constant.getEnMessage(warehousePageable.getCode()));
            warehousePageable.setId_message(Constant.getInMessage(warehousePageable.getCode()));
        } catch (SignatureException se){
            logger.error("see stack trace, {}", se.getMessage());
            logger.error(se.getMessage());
            se.printStackTrace();

            warehousePageable.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            warehousePageable.setEn_message(Constant.getEnMessage(warehousePageable.getCode()));
            warehousePageable.setId_message(Constant.getInMessage(warehousePageable.getCode()));
        } catch (IncorrectClaimException ice){
            logger.error("see stack trace, {}", ice.getMessage());
            logger.error(ice.getMessage());
            ice.printStackTrace();

            warehousePageable.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            warehousePageable.setEn_message(Constant.getEnMessage(warehousePageable.getCode()));
            warehousePageable.setId_message(Constant.getInMessage(warehousePageable.getCode()));
        } catch (NumberFormatException nfe) {
            logger.error("see stack trace : {}", nfe.getMessage());
            logger.error(nfe.getMessage());
            logger.error(nfe.toString());
            nfe.printStackTrace();

            warehousePageable =  new WarehousePageable();
            warehousePageable.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            warehousePageable.setEn_message("Illegal " + nfe.getMessage());
            warehousePageable.setId_message("Illegal " + nfe.getMessage());

            return new ResponseEntity<>(warehousePageable,HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            warehousePageable =  new WarehousePageable();
            warehousePageable.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            warehousePageable.setEn_message(Constant.getEnMessage(warehousePageable.getCode()));
            warehousePageable.setId_message(Constant.getInMessage(warehousePageable.getCode()));

            return new ResponseEntity<>(warehousePageable,HttpStatus.OK);
        }


        return new ResponseEntity<>(warehousePageable, HttpStatus.OK);
    }

    /**
     * Return all first class warehouse.
     *
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL_FIRST_CLASS_WAREHOUSE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getAllWarehouseFirstClass() {

        GeneralResponse generalResponse = new GeneralResponse();

        generalResponse = warehouseService.getAllWarehouseFirstClass();

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

}
