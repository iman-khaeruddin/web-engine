package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.model.GetNumber;
import com.mospaze.werehouse.repository.CustomWarehouseDao;
import com.mospaze.werehouse.repository.WarehouseDao;
import com.mospaze.werehouse.service.CaltulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.CaltulatorService
 */
@Service
public class CalculatorServiceImpl implements CaltulatorService {

    private final Logger logger = LoggerFactory.getLogger(CalculatorServiceImpl.class);

    @Autowired
    private CustomWarehouseDao customWarehouseDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Override
    public GetNumber getMaxSize(Integer unit, Integer id_cities, Integer temperature) {
        GetNumber getNumber = new GetNumber();
        if(unit == 1)
            getNumber.setMax_size(warehouseDao.getMaxSizeByPallet(id_cities, temperature));
        if(unit == 2)
            getNumber.setMax_size(warehouseDao.getMaxSizeBySQM(id_cities, temperature));
        return getNumber;
    }

    @Override
    public GetNumber getAverage(Integer unit, Integer id_cities, Integer temperature, Integer size, Integer periode) {
        GetNumber getNumber = new GetNumber();
        double average = 0D;
        if(unit == 1)
            average = warehouseDao.getAverageByPallet(id_cities, temperature);
        if(unit == 2)
            average = warehouseDao.getAverageBySQM(id_cities, temperature);

        logger.info("AVERAGE FROM SQL : {}", average);

        average = average * size ;

        logger.info("AVERAGE * SIZE =  {}", average);

        if(periode == 0)    //monthly
            average = average * 1.1;
        if(periode == 1)    //yearly
            average = average * 12 * 1.07;

        logger.info("AVERAGE * PERIODE =  {}", average);

        getNumber.setAverage(new BigDecimal(average).setScale(2, RoundingMode.CEILING));

        return getNumber;
    }
}
