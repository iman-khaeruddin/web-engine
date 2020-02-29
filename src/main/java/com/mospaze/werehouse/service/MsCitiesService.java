package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.MsCities;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.model.DetailDistrictModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsCitiesService {

	/**
	 * @return list of {@link MsCities}
	 */
	@Deprecated
	List<MsCities> GetCities();

	/**
	 * @param search_value
	 * @return list of {@link DetailCities}
	 */
	List<DetailCities> GetDetailCities(String search_value);

	/**
	 * @param search_value
	 * @return list of {@link DetailCities}
	 */
	List<DetailCities> GetDetailDistricts(String search_value);

	/**
	 * @return list of {@link DetailCities}
	 *
	 * @throws java.io.IOException
	 */
	List<DetailCities> GetDetailCities() throws IOException;

	/**
	 * @return list of {@link DetailCities}
	 *
	 * @throws java.io.IOException
	 */
	List<DetailCities> GetDetailDistricts() throws IOException;


	/**
	 * @param search_value
	 * @return list of {@link DetailDistrictModel}
	 */
	List<DetailDistrictModel> GetDetailDistrictsSearchWarehouse(String search_value);
}
