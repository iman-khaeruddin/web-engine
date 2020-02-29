package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.model.GetNumber;
import com.mospaze.werehouse.service.CaltulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_CALCULATOR)
public class CalculatorController {

    private final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Autowired
    private CaltulatorService caltulatorService;

    /**
     * This controller used to get upper limit of warehouse space with the given PARAMs.
     *
     * @param unit
     * @param id_cities
     * @param temperature
     * @param httpServletRequest
     * @return {@link GetNumber}
     */
    @GetMapping(value = RestConstant.GET_MAX_SIZE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetNumber> getMaxSize(@RequestParam(name = "unit") Integer unit,
                                                @RequestParam (name = "id_cities") Integer id_cities,
                                                @RequestParam (name = "temperature") Integer temperature,
                                                HttpServletRequest httpServletRequest) {

        logger.info("REQUEST URL : {}", httpServletRequest.getRequestURL());
        logger.info("RequestParam [unit] : {}", unit);
        logger.info("RequestParam [id_cities] : {}", id_cities);
        logger.info("RequestParam [temperature] : {}", temperature);

        List<Integer> units = Arrays.asList(1,2);
        List<Integer> temperatures = Arrays.asList(1,2);

        if(units.contains(unit) && temperatures.contains(temperature))
            return new ResponseEntity<>(caltulatorService.getMaxSize(unit, id_cities, temperature), HttpStatus.OK);

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    /**
     * This controller used to calculate price (estimated) with the given PARAMs.
     *
     * @param unit
     * @param id_cities
     * @param temperature
     * @param size
     * @param periode
     * @param httpServletRequest
     * @return {@link GetNumber}
     */
    @GetMapping(value = RestConstant.CALCULATE_AREA, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetNumber> calculateArea(@RequestParam(name = "unit") Integer unit,
                                                   @RequestParam (name = "id_cities") Integer id_cities,
                                                   @RequestParam (name = "temperature") Integer temperature,
                                                   @RequestParam (name = "size") Integer size,
                                                   @RequestParam (name = "periode") Integer periode,
                                                   HttpServletRequest httpServletRequest) {

        logger.info("REQUEST URL : {}", httpServletRequest.getRequestURL());
        logger.info("RequestParam [unit] : {}", unit);
        logger.info("RequestParam [id_cities] : {}", id_cities);
        logger.info("RequestParam [temperature] : {}", temperature);
        logger.info("RequestParam [size] : {}", size);
        logger.info("RequestParam [periode] : {}", periode);

        List<Integer> units = Arrays.asList(1,2);
        List<Integer> temperatures = Arrays.asList(1,2);
        List<Integer> periodes = Arrays.asList(0,1);

        int max_size = caltulatorService.getMaxSize(unit, id_cities, temperature).getMax_size();

        if(units.contains(unit) && temperatures.contains(temperature) && periodes.contains(periode) && size <= max_size && size > 0)
            return new ResponseEntity<>(caltulatorService.getAverage(unit, id_cities, temperature, size, periode), HttpStatus.OK);

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}
