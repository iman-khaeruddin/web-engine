package com.mospaze.werehouse.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsCities;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.entity.projector.DetailDistrict;
import com.mospaze.werehouse.model.DetailDistrictModel;
import com.mospaze.werehouse.repository.MsCitiesDao;
import com.mospaze.werehouse.service.MsCitiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsCitiesService
 */
@Service
public class MsCitiesServiceImpl implements MsCitiesService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MsCitiesDao msCitiesDao;
	
	@Override
	@Deprecated
	public List<MsCities> GetCities() {
		return (List<MsCities>) msCitiesDao.findAll();
	}

	@Override
	public List<DetailCities> GetDetailCities(String search_value){
		List<DetailCities> detailCities = new ArrayList<>();

		try {

			if(search_value != null && !search_value.isEmpty()){
				detailCities = msCitiesDao.getAllCitiesEntities(search_value);
			} else {
				detailCities = msCitiesDao.getAllCitiesEntitiesLimited();
			}
		}catch (Exception e){

			logger.error("see stack trace : {}", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();

			detailCities = msCitiesDao.getAllCitiesEntities();
		}

		return detailCities;
	}

	@Override
	public List<DetailCities> GetDetailDistricts(String search_value){
		List<DetailCities> detailCities = new ArrayList<>();

		try {
			if(search_value != null && !search_value.isEmpty()){
				detailCities = msCitiesDao.getAllDistrictsEntities(search_value);
			} else {
				detailCities = msCitiesDao.getAllDistrictsEntitiesLimited();
			}
		}catch (Exception e){

			logger.error("see stack trace : {}", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();

			detailCities = msCitiesDao.getAllDistrictsEntities();
		}
		return detailCities;
	}

	@Override
	public List<DetailCities> GetDetailCities() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Gson gson = new Gson();
		List<DetailCities> detailCities = new ArrayList<>();

		try {
			String cities = Constant.configProperties.get(Constant.DETAIL_CITIES);

			if(cities != null){
				logger.info("GET DETAIL CITIES FROM LOCAL CACHE..!!");
				logger.info(cities);
				return gson.fromJson(cities, ArrayList.class);
			} else {
				detailCities = msCitiesDao.getAllCitiesEntities();
				Constant.configProperties.put(Constant.DETAIL_CITIES, objectMapper.writeValueAsString(detailCities));
				logger.info("{} : {}", Constant.DETAIL_CITIES, objectMapper.writeValueAsString(detailCities));
				return detailCities;
			}
		}catch (Exception e){

			logger.error("see stack trace : {}", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();

			detailCities = msCitiesDao.getAllCitiesEntities();
			Constant.configProperties.put(Constant.DETAIL_CITIES, objectMapper.writeValueAsString(detailCities));
			logger.info("{} : {}", Constant.DETAIL_CITIES, objectMapper.writeValueAsString(detailCities));
			return detailCities;
		}
	}

	@Override
	public List<DetailCities> GetDetailDistricts() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Gson gson = new Gson();
		List<DetailCities> detailCities = new ArrayList<>();

		try {
			String district = Constant.configProperties.get(Constant.DETAIL_DISTRICTS);

			if(district != null){
				logger.info("GET DETAIL CITIES FROM LOCAL CACHE..!!");
				logger.info(district);
				return gson.fromJson(district, ArrayList.class);
			} else {
				detailCities = msCitiesDao.getAllDistrictsEntities();
				Constant.configProperties.put(Constant.DETAIL_DISTRICTS, objectMapper.writeValueAsString(detailCities));
				logger.info("{} : {}", Constant.DETAIL_DISTRICTS, objectMapper.writeValueAsString(detailCities));
				return detailCities;
			}
		}catch (Exception e){

			logger.error("see stack trace : {}", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();

			detailCities = msCitiesDao.getAllDistrictsEntities();
			Constant.configProperties.put(Constant.DETAIL_DISTRICTS, objectMapper.writeValueAsString(detailCities));
			logger.info("{} : {}", Constant.DETAIL_DISTRICTS, objectMapper.writeValueAsString(detailCities));
			return detailCities;
		}
	}

	@Override
	public List<DetailDistrictModel> GetDetailDistrictsSearchWarehouse(String search_value) {
		List<DetailDistrictModel> detailDistrictModelList = new ArrayList<>();
		List<DetailCities> detailCitiesList = new ArrayList<>();
		List<DetailCities> detailDistrictList = new ArrayList<>();

		//get cities first
		if(search_value != null && !search_value.isEmpty()){
			detailCitiesList = msCitiesDao.getAllCitiesEntities(search_value);
		}

		Map<String, Long> id_cities = detailCitiesList.stream().collect(
				Collectors.groupingBy(DetailCities::getId_cities, Collectors.counting()));

		id_cities.forEach((k,v) -> {
			DetailDistrictModel detailDistrictModel = new DetailDistrictModel();

			Optional<DetailCities> detailCities = msCitiesDao.getAllCityEntitiesByIdCity(Integer.parseInt(k));

			detailDistrictModel.setId_cities(Integer.parseInt(detailCities.get().getId_cities()));
			detailDistrictModel.setCities_name(detailCities.get().getCities_name().toUpperCase());
			detailDistrictModel.setId_state(Integer.parseInt(detailCities.get().getId_state()));
			detailDistrictModel.setState_name(detailCities.get().getState_name().toUpperCase());
			detailDistrictModel.setId_countries(Integer.parseInt(detailCities.get().getId_countries()));
			detailDistrictModel.setCountries_name(detailCities.get().getCountries_name().toUpperCase());

			detailDistrictModel.setDetail_district(msCitiesDao.getAllDistrictsEntitiesByIdCities(detailDistrictModel.getId_cities()));

			detailDistrictModelList.add(detailDistrictModel);
		});

        System.err.println(id_cities);

        //and then get district
		if(search_value != null && !search_value.isEmpty()){
			detailCitiesList = msCitiesDao.getAllDistrictsEntities(search_value);
		}

		System.err.println(detailCitiesList);

		if(detailDistrictModelList.size() == 0){
			id_cities = detailCitiesList.stream().collect(
					Collectors.groupingBy(DetailCities::getId_cities, Collectors.counting()));

			id_cities.forEach((k,v) -> {
				DetailDistrictModel detailDistrictModel = new DetailDistrictModel();

				Optional<DetailCities> detailCities = msCitiesDao.getAllCityEntitiesByIdCity(Integer.parseInt(k));

				detailDistrictModel.setId_cities(Integer.parseInt(detailCities.get().getId_cities()));
				detailDistrictModel.setCities_name(detailCities.get().getCities_name().toUpperCase());
				detailDistrictModel.setId_state(Integer.parseInt(detailCities.get().getId_state()));
				detailDistrictModel.setState_name(detailCities.get().getState_name().toUpperCase());
				detailDistrictModel.setId_countries(Integer.parseInt(detailCities.get().getId_countries()));
				detailDistrictModel.setCountries_name(detailCities.get().getCountries_name().toUpperCase());

				detailDistrictModel.setDetail_district(msCitiesDao.getDistrictsByDistrictName(search_value, detailDistrictModel.getId_cities()));

				detailDistrictModelList.add(detailDistrictModel);
			});
		} else {
			Map<String, Long> id_cities2 = detailCitiesList.stream().collect(
					Collectors.groupingBy(DetailCities::getId_cities, Collectors.counting()));

			id_cities2.forEach((k,v) -> {
				DetailDistrictModel detailDistrictModel = new DetailDistrictModel();

				Optional<DetailCities> detailCities = msCitiesDao.getAllCityEntitiesByIdCity(Integer.parseInt(k));

				detailDistrictModel.setId_cities(Integer.parseInt(detailCities.get().getId_cities()));
				detailDistrictModel.setCities_name(detailCities.get().getCities_name().toUpperCase());
				detailDistrictModel.setId_state(Integer.parseInt(detailCities.get().getId_state()));
				detailDistrictModel.setState_name(detailCities.get().getState_name().toUpperCase());
				detailDistrictModel.setId_countries(Integer.parseInt(detailCities.get().getId_countries()));
				detailDistrictModel.setCountries_name(detailCities.get().getCountries_name().toUpperCase());

				detailDistrictModel.setDetail_district(msCitiesDao.getDistrictsByDistrictName(search_value, detailDistrictModel.getId_cities()));

				detailDistrictModelList.add(detailDistrictModel);
			});

			/*for(DetailCities detailCities : detailCitiesList){
				DetailDistrictModel detailDistrictModel = new DetailDistrictModel();

				if(id_cities.get(detailCities.getId_cities()) == null){
					detailDistrictModel.setId_cities(Integer.parseInt(detailCities.getId_cities()));
					detailDistrictModel.setCities_name(detailCities.getCities_name());
					detailDistrictModel.setId_state(Integer.parseInt(detailCities.getId_state()));
					detailDistrictModel.setState_name(detailCities.getState_name());
					detailDistrictModel.setId_countries(Integer.parseInt(detailCities.getId_countries()));
					detailDistrictModel.setCountries_name(detailCities.getCountries_name());

					detailDistrictModel.setDetail_district(msCitiesDao.getDistrictById(Integer.parseInt(detailCities.getId_district())));

					detailDistrictModelList.add(detailDistrictModel);
				}
			}*/
		}

		return detailDistrictModelList;
	}

}
