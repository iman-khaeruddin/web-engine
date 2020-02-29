package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MsUnitDao extends CrudRepository<MsUnit, Integer> {

    @Query(value = "SELECT * FROM master_unit WHERE status = 1", nativeQuery = true)
    Iterable<MsUnit> findAllByActiveStatus();
}
