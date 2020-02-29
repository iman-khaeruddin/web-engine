package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Roles;
import com.mospaze.werehouse.entity.Users;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.entity.projector.User;
import com.mospaze.werehouse.model.*;
import com.mospaze.werehouse.repository.*;
import com.mospaze.werehouse.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.UserManagementService
 */
@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private Util util;

    @Autowired
    private CustomUsersDao customUsersDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private MsCitiesDao msCitiesDao;

    @Autowired
    private RolesDao rolesDao;

    @Override
    @Transactional
    @Deprecated
    public GeneralResponse changePassword(ChangePasswordRequest changePasswordRequest, String username) throws InvalidKeyException, NoSuchAlgorithmException {
        GeneralResponse generalResponse = new GeneralResponse();

        Optional<Users> users = usersDao.getUserByPhone(username);

        if(users.isPresent()){
            Users users1 = new Users();
            users1.setPassword(util.encryptPassword(changePasswordRequest.getNew_password()));
            usersDao.save(users1);

            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } else {
            generalResponse.setCode(Constant.Code.USERNAME_NOT_REGISTERED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return generalResponse;
    }

    @Override
    @Transactional
    public GeneralResponse forgotPassword(ChangePasswordRequest changePasswordRequest, String username, String token) throws InvalidKeyException, NoSuchAlgorithmException {
        GeneralResponse generalResponse = new GeneralResponse();

        Optional<Users> users = usersDao.getUserByPhone(username);

        if(users.isPresent()){
            Users users1 = users.get();
            users1.setPassword(util.encryptPassword(changePasswordRequest.getNew_password()));
            users1.setConfirmation_code(token);
            usersDao.save(users1);

            util.killToken(token, username);

            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } else {
            generalResponse.setCode(Constant.Code.USERNAME_NOT_REGISTERED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return generalResponse;
    }

    @Override
    public UsersPageable getUsersByRole(Integer role_id, String name_email, ModelHasRole userRole, Integer page, Integer size) {
        UsersPageable usersPageable = new UsersPageable();
        usersPageable.setDetailUsers(customUsersDao.findByRole(role_id, name_email, userRole, page, size));
        usersPageable.setTotal_page(customUsersDao.getTotalPage(role_id, name_email, userRole, page, size));
        usersPageable.setTotal_element(customUsersDao.getTotalElement(role_id, name_email, userRole, page, size));

        if(usersPageable.getDetailUsers().size() > 0){
            usersPageable.setFrom((page * size) + 1);
            usersPageable.setTo((page * usersPageable.getDetailUsers().size()) + usersPageable.getDetailUsers().size());

            if(usersPageable.getDetailUsers().size() != size)
                usersPageable.setTo(usersPageable.getFrom() + usersPageable.getDetailUsers().size() - 1);
        }

        return usersPageable;
    }

    @Override
    @Transactional
    public String insertUsers(UsersManagementRequest usersManagementRequest, String username) throws InvalidKeyException, NoSuchAlgorithmException {
        Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

        if(usersDao.countByPhoneNumber(usersManagementRequest.getPhone()) == 0) {
            Users users = insertUser(usersManagementRequest);

            if(role_login_user.get().getRole_id() != Constant.Role.SELLER.getRole())
                insertUserType(users, usersManagementRequest, usersManagementRequest.getId_super_seller());
            else
                insertUserType(users, usersManagementRequest, role_login_user.get().getModel_id());

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.PHONE_DUPLICATE.getStatusCode();
        }
    }

    @Override
    public GeneralResponse getDetailUsersById(int id) {
        GeneralResponse generalResponse = new GeneralResponse();
        UsersManagementRequest usersManagementRequest = new UsersManagementRequest();
        Optional<Users> user = usersDao.findById(id);
        if(user.isPresent()){
            Users users = user.get();
            usersManagementRequest.setAddress(users.getAddress_1());
            usersManagementRequest.setFirst_name(users.getFirst_name());
            usersManagementRequest.setLast_name(users.getLast_name());
            usersManagementRequest.setEmail(users.getEmail());
            usersManagementRequest.setPhone(users.getPhone());
            usersManagementRequest.setPostalcode(users.getPostalcode());
            usersManagementRequest.setCompany_name(users.getCompany());
            usersManagementRequest.setStatus(users.getStatus());
            usersManagementRequest.setCity_id(users.getCity_id());
            usersManagementRequest.setState_id(users.getState_id());
            usersManagementRequest.setCountry_id(users.getCountry_id());
            usersManagementRequest.setId(users.getId());

            usersManagementRequest.setCity_name(users.getCity());
            usersManagementRequest.setState_name(users.getState());
            usersManagementRequest.setCoutry_name(users.getCountry());

            Roles roles = rolesDao.getRoleByUserId(id);
            usersManagementRequest.setRole(roles.getId());
            usersManagementRequest.setRole_name(roles.getName());

            if(roles.getId() == Constant.Role.SUB_SELLER.getRole())
                usersManagementRequest.setId_super_seller(modelHasRoleDao.findById(id).get().getSuper_user());

            generalResponse.setData(usersManagementRequest);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        } else {
            generalResponse.setCode(Constant.Code.USER_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }
        return generalResponse;
    }

    @Override
    @Transactional
    public String deleteUser(int id) {
        Optional<Users> users = usersDao.findById(id);
        if(users.isPresent()){
            usersDao.deleteById(id);
            return Constant.Code.SUCCESS.getStatusCode();
        }
        return Constant.Code.USER_NOT_FOUND.getStatusCode();
    }

    @Override
    @Transactional
    public String changeStatusUser(int status, int id) {
        Optional<Users> users = usersDao.findById(id);
        if(users.isPresent()){
            usersDao.changeStatusUser(status, id);
            return Constant.Code.SUCCESS.getStatusCode();
        }
        return Constant.Code.USER_NOT_FOUND.getStatusCode();
    }

    @Override
    @Transactional
    public String updateUser(UpdateUsersRequest updateUsersRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        Optional<Users> user = usersDao.findById(updateUsersRequest.getId());
        if(user.isPresent()){
            Users users1 = user.get();

            users1.setFirst_name(updateUsersRequest.getFirst_name());
            users1.setLast_name(updateUsersRequest.getLast_name());
            users1.setCompany(updateUsersRequest.getCompany_name());
            users1.setAddress_1(updateUsersRequest.getAddress());
            users1.setCity_id(updateUsersRequest.getCity_id());
            users1.setState_id(updateUsersRequest.getState_id());
            users1.setCountry_id(updateUsersRequest.getCountry_id());
            users1.setPostalcode(updateUsersRequest.getPostalcode());

            List<DetailCities> detailCities= msCitiesDao.getAllCitiesEntitiesByIdCity(users1.getCity_id());
            for(DetailCities detailCities1 : detailCities){
                users1.setCity(detailCities1.getCities_name());
                users1.setState(detailCities1.getState_name());
                users1.setCountry(detailCities1.getCountries_name());
            }

            if(updateUsersRequest.getPassword() != null)
                users1.setPassword(util.encryptPassword(updateUsersRequest.getPassword()));

            usersDao.save(users1);

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.USER_NOT_FOUND.getStatusCode();
        }
    }

    @Override
    public List<User> getUserSellerByName(String name) {
        return usersDao.getUserSellerByName(name);
    }

    @Override
    @Transactional
    public String forceChangePassword(ForceChangePassword forceChangePassword) throws InvalidKeyException, NoSuchAlgorithmException {
        Optional<Users> users = usersDao.getUserByPhone(forceChangePassword.getUsername());
        if(users.isPresent()){
            Users users1 = users.get();

            if(!util.encryptPassword(forceChangePassword.getOld_password()).equals(users1.getPassword()))
                return Constant.Code.OLD_PASSWORD_INVALID.getStatusCode();

            users1.setPassword(util.encryptPassword(forceChangePassword.getNew_password()));
            users1.setFlag_change_password(new Date());
            usersDao.save(users1);
            return Constant.Code.SUCCESS.getStatusCode();
        }
        return Constant.Code.USER_NOT_FOUND.getStatusCode();
    }

    @Override
    public List<User> getUserBuyerByName(String name) {
        return usersDao.getUserBuyerByName(name);
    }

    private Users insertUser(UsersManagementRequest usersManagementRequest) throws InvalidKeyException, NoSuchAlgorithmException {
        Users users = new Users();
        users.setUsername(usersManagementRequest.getEmail());
        users.setEmail(usersManagementRequest.getEmail());
        users.setFirst_name(usersManagementRequest.getFirst_name());
        users.setLast_name(usersManagementRequest.getLast_name());
        users.setPhone(usersManagementRequest.getPhone());
        users.setPassword(util.encryptPassword(usersManagementRequest.getPassword()));
        users.setCompany(usersManagementRequest.getCompany_name());
        users.setCountry_id(usersManagementRequest.getCountry_id());
        users.setState_id(usersManagementRequest.getState_id());
        users.setCity_id(usersManagementRequest.getCity_id());
        users.setPostalcode(usersManagementRequest.getPostalcode());
        users.setAddress_1(usersManagementRequest.getAddress());
        users.setVerified_phone(0);
        users.setStatus(1);
        users.setConfirmed(0);
        users.setConfirmation_code(String.valueOf(11111));
        users.setCreated_at(new Date());
        users.setFlag_change_password(new Date());

        List<DetailCities> detailCities= msCitiesDao.getAllCitiesEntitiesByIdCity(users.getCity_id());
        for(DetailCities detailCities1 : detailCities){
            users.setCity(detailCities1.getCities_name());
            users.setState(detailCities1.getState_name());
            users.setCountry(detailCities1.getCountries_name());
        }

        if(usersManagementRequest.getRole() == Constant.Role.SUB_SELLER.getRole())
            users.setIs_multi_user(1);
        else
            users.setIs_primary_user(1);

        return usersDao.save(users);
    }

    private ModelHasRole insertUserType(Users users, UsersManagementRequest usersManagementRequest, Integer userRole) {
        ModelHasRole modelHasRole = new ModelHasRole();
        modelHasRole.setModel_id(users.getId());
        modelHasRole.setRole_id(usersManagementRequest.getRole());
        modelHasRole.setModel_type("Mospaze\\Models\\User\\User");

        if(usersManagementRequest.getRole() == Constant.Role.SUB_SELLER.getRole())
            modelHasRole.setSuper_user(userRole);

        return modelHasRoleDao.save(modelHasRole);
    }
}
