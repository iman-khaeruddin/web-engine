package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsCities;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.entity.projector.DetailDistrict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MsCitiesDao extends CrudRepository<MsCities, Integer>{

    @Query(value = "select " +
            "    mlc.id as id_cities, " +
            "    mlc.name as cities_name," +
            "    mls.id as id_state," +
            "    mls.name as state_name," +
            "    mlco.id as id_countries," +
            "    mlco.name as countries_name " +
            "from " +
            "    master_location_cities mlc " +
            "join master_location_states mls on mlc.state_id = mls.id " +
            "join master_location_countries mlco on mls.country_id = mlco.id", nativeQuery = true)
    List<DetailCities> getAllCitiesEntities();

    @Query(value = "select " +
            "    mlc.id as id_cities, " +
            "    mlc.name as cities_name," +
            "    mls.id as id_state," +
            "    mls.name as state_name," +
            "    mlco.id as id_countries," +
            "    mlco.name as countries_name " +
            "from " +
            "    master_location_cities mlc " +
            "join master_location_states mls on mlc.state_id = mls.id " +
            "join master_location_countries mlco on mls.country_id = mlco.id " +
            "where " +
            "    mlc.id = :id_city", nativeQuery = true)
    List<DetailCities> getAllCitiesEntitiesByIdCity(@Param("id_city") int id_city);

    @Query(value = "select " +
            "    mlc.id as id_cities, " +
            "    mlc.name as cities_name," +
            "    mls.id as id_state," +
            "    mls.name as state_name," +
            "    mlco.id as id_countries," +
            "    mlco.name as countries_name " +
            "from " +
            "    master_location_cities mlc " +
            "join master_location_states mls on mlc.state_id = mls.id " +
            "join master_location_countries mlco on mls.country_id = mlco.id " +
            "where " +
            "    mlc.id = :id_city", nativeQuery = true)
    Optional<DetailCities> getAllCityEntitiesByIdCity(@Param("id_city") int id_city);

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id", nativeQuery = true)
    List<DetailCities> getAllDistrictsEntities();

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id " +
            "where mld.id = :district_id", nativeQuery = true)
    List<DetailCities> getAllDistrictsEntitiesByIdDistrict(@Param("district_id") Integer district_id);

    @Query(value = "SELECT * FROM master_location_cities WHERE id IN :id_cities_list", nativeQuery = true)
    List<MsCities> getCitiesNameFromId(@Param("id_cities_list") List<Integer> id_cities_list);

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id " +
            "WHERE " +
            " mld.name LIKE CONCAT('%',:search_value,'%') " +
            "AND " +
            " mlc.name NOT LIKE CONCAT('%',:search_value,'%') " +
            "ORDER BY mlc.NAME ASC LIMIT 50", nativeQuery = true)
    List<DetailCities> getAllDistrictsEntities(@Param("search_value") String search_value);

    @Query(value = "select " +
            "    mlc.id as id_cities, " +
            "    mlc.name as cities_name," +
            "    mls.id as id_state," +
            "    mls.name as state_name," +
            "    mlco.id as id_countries," +
            "    mlco.name as countries_name " +
            "from " +
            "    master_location_cities mlc " +
            "join master_location_states mls on mlc.state_id = mls.id " +
            "join master_location_countries mlco on mls.country_id = mlco.id " +
            "WHERE " +
            " mlc.name LIKE CONCAT('%',:search_value,'%') ORDER BY mlc.NAME ASC LIMIT 50", nativeQuery = true)
    List<DetailCities> getAllCitiesEntities(@Param("search_value") String search_value);

    @Query(value = "SELECT " +
            " mld.id as id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id " +
            "where mlc.id = :city_id ORDER BY mld.NAME ASC", nativeQuery = true)
    List<DetailDistrict> getAllDistrictsEntitiesByIdCities(@Param("city_id") int city_id);

    @Query(value = "select " +
            "    mlc.id as id_cities, " +
            "    mlc.name as cities_name," +
            "    mls.id as id_state," +
            "    mls.name as state_name," +
            "    mlco.id as id_countries," +
            "    mlco.name as countries_name " +
            "from " +
            "    master_location_cities mlc " +
            "join master_location_states mls on mlc.state_id = mls.id " +
            "join master_location_countries mlco on mls.country_id = mlco.id LIMIT 50", nativeQuery = true)
    List<DetailCities> getAllCitiesEntitiesLimited();

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id ORDER BY mlc.NAME ASC LIMIT 50", nativeQuery = true)
    List<DetailCities> getAllDistrictsEntitiesLimited();

    @Query(value = "select " +
            "    mlc.id as id_cities, " +
            "    mlc.name as cities_name," +
            "    mls.id as id_state," +
            "    mls.name as state_name," +
            "    mlco.id as id_countries," +
            "    mlco.name as countries_name " +
            "from " +
            "    master_location_cities mlc " +
            "join master_location_states mls on mlc.state_id = mls.id " +
            "join master_location_countries mlco on mls.country_id = mlco.id " +
            "WHERE " +
            " mlc.name LIKE CONCAT('%',:search_value,'%') LIMIT 50", nativeQuery = true)
    List<DetailCities> getCitiesByCitiesName(@Param("search_value") String search_value);

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id " +
            "WHERE " +
            " mld.name LIKE CONCAT('%',:search_value,'%') LIMIT 50 ", nativeQuery = true)
    List<DetailCities> getDistrictByDistrictName(@Param("search_value") String search_value);

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id " +
            "WHERE " +
            " mld.id = :id_district ", nativeQuery = true)
    List<DetailDistrict> getDistrictById(@Param("id_district") int id_district);

    @Query(value = "SELECT " +
            " mld.id AS id_district, " +
            " UPPER( mld.NAME ) AS district_name, " +
            " mlc.id AS id_cities, " +
            " UPPER( mlc.NAME ) AS cities_name, " +
            " mls.id AS id_state, " +
            " UPPER( mls.NAME ) AS state_name, " +
            " mlco.id AS id_countries, " +
            " UPPER( mlco.NAME ) AS countries_name,  " +
            " mld.postcode " +
            "FROM " +
            " master_location_districts mld " +
            " JOIN master_location_cities mlc ON mld.cities_id = mlc.id " +
            " JOIN master_location_states mls ON mlc.state_id = mls.id " +
            " JOIN master_location_countries mlco ON mls.country_id = mlco.id " +
            "WHERE " +
            " mld.name LIKE CONCAT('%',:search_value,'%') " +
            "AND " +
            " mld.cities_id = :id_cities", nativeQuery = true)
    List<DetailDistrict> getDistrictsByDistrictName(@Param("search_value") String search_value, @Param("id_cities") int id_cities);
}
