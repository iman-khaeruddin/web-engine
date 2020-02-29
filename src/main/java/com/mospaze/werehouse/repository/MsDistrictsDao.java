package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsDisctrict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 28/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MsDistrictsDao extends CrudRepository<MsDisctrict, Integer> {

    @Query(value = "SELECT * FROM master_location_districts WHERE id IN :id_district_list", nativeQuery = true)
    List<MsDisctrict> getDistrictNameFromId(@Param("id_district_list") List<Integer> id_district_list);
}
