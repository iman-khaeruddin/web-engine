package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.MsUnit;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsUnitService {
    /**
     * @return all entities {@link MsUnit}
     */
    Iterable<MsUnit> getMasterMsUnit();
}
