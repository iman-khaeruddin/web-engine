package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.Roles;
import com.mospaze.werehouse.entity.Users;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.UpdateUserProfileRequest;
import com.mospaze.werehouse.repository.MsCitiesDao;
import com.mospaze.werehouse.repository.RolesDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 23/11/2019
 * @see com.mospaze.werehouse.service.UserProfileService
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private Util util;

    @Autowired
    private MsCitiesDao msCitiesDao;

    @Autowired
    private RolesDao rolesDao;

    @Override
    @Transactional
    public String editProfile(UpdateUserProfileRequest updateUserProfileRequest, JWT jwt) throws NoSuchAlgorithmException, InvalidKeyException {
        Optional<Users> users = usersDao.findById(updateUserProfileRequest.getId());
        if(users.isPresent()){
            Users users1 = users.get();

            if(users1.getId() != jwt.getUser_id())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            users1.setFirst_name(updateUserProfileRequest.getFirst_name());
            users1.setLast_name(updateUserProfileRequest.getLast_name());
            users1.setCompany(updateUserProfileRequest.getCompany_name());
            users1.setAddress_1(updateUserProfileRequest.getAddress());
            users1.setDistrict_id(updateUserProfileRequest.getDistrict_id());
            users1.setCity_id(updateUserProfileRequest.getCity_id());
            users1.setState_id(updateUserProfileRequest.getState_id());
            users1.setCountry_id(updateUserProfileRequest.getCountry_id());
            users1.setPostalcode(updateUserProfileRequest.getPostalcode());
            users1.setJob_tittle(updateUserProfileRequest.getJob_tittle());
            users1.setCompany_url(updateUserProfileRequest.getCompany_url());
            users1.setCompany_tax_number(updateUserProfileRequest.getCompany_tax_number());

            List<DetailCities> detailCities= msCitiesDao.getAllDistrictsEntitiesByIdDistrict(users1.getDistrict_id());
            for(DetailCities detailCities1 : detailCities){
                users1.setCity(detailCities1.getCities_name());
                users1.setState(detailCities1.getState_name());
                users1.setCountry(detailCities1.getCountries_name());
                users1.setDistrict(detailCities1.getDistrict_name());
            }

            if(updateUserProfileRequest.getPassword() != null)
                users1.setPassword(util.encryptPassword(updateUserProfileRequest.getPassword()));

            usersDao.save(users1);

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();
        }
    }

    @Override
    public GeneralResponse getDetailUsersById(int id) {
        GeneralResponse generalResponse = new GeneralResponse();
        UpdateUserProfileRequest updateUserProfileRequest = new UpdateUserProfileRequest();

        Optional<Users> user = usersDao.findById(id);
        if(user.isPresent()){
            Users users = user.get();
            updateUserProfileRequest.setAddress(users.getAddress_1());
            updateUserProfileRequest.setFirst_name(users.getFirst_name());
            updateUserProfileRequest.setLast_name(users.getLast_name());
            updateUserProfileRequest.setEmail(users.getEmail());
            updateUserProfileRequest.setPhone(users.getPhone());
            updateUserProfileRequest.setPostalcode(users.getPostalcode());
            updateUserProfileRequest.setCompany_name(users.getCompany());
            updateUserProfileRequest.setStatus(users.getStatus());
            updateUserProfileRequest.setDistrict_id(users.getDistrict_id());
            updateUserProfileRequest.setCity_id(users.getCity_id());
            updateUserProfileRequest.setState_id(users.getState_id());
            updateUserProfileRequest.setCountry_id(users.getCountry_id());
            updateUserProfileRequest.setId(users.getId());
            updateUserProfileRequest.setJob_tittle(users.getJob_tittle());
            updateUserProfileRequest.setCompany_url(users.getCompany_url());
            updateUserProfileRequest.setCompany_tax_number(users.getCompany_tax_number());

            if(users.getDistrict_id() != null){
                List<DetailCities> detailCities= msCitiesDao.getAllDistrictsEntitiesByIdDistrict(users.getDistrict_id());
                for(DetailCities detailCities1 : detailCities){
                    updateUserProfileRequest.setCity_name(detailCities1.getCities_name());
                    updateUserProfileRequest.setState_name(detailCities1.getState_name());
                    updateUserProfileRequest.setCountry_name(detailCities1.getCountries_name());
                    updateUserProfileRequest.setDistrict_name(detailCities1.getDistrict_name());
                }
            }

            Roles roles = rolesDao.getRoleByUserId(id);
            updateUserProfileRequest.setRole(roles.getId());
            updateUserProfileRequest.setRole_name(roles.getName());

            generalResponse.setData(updateUserProfileRequest);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        } else {
            generalResponse.setCode(Constant.Code.USER_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return generalResponse;
    }
}
