package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.entity.MsUnit;
import com.mospaze.werehouse.repository.MsUnitDao;
import com.mospaze.werehouse.service.MsUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsUnitService
 */
@Service
public class MsUnitServiceImpl implements MsUnitService {

    @Autowired
    private MsUnitDao msUnitDao;

    @Override
    public Iterable<MsUnit> getMasterMsUnit() {
        return msUnitDao.findAllByActiveStatus();
    }
}
