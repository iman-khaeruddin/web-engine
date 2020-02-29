package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.Users;
import com.mospaze.werehouse.entity.projector.User;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.LoginRequest;
import com.mospaze.werehouse.model.LoginResponse;
import com.mospaze.werehouse.repository.PermissionDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.LoginService
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private Util util;

    @Override
    @Transactional
    public GeneralResponse verifyUser(LoginRequest loginRequest) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        GeneralResponse generalResponse = new GeneralResponse();
        LoginResponse loginResponse = new LoginResponse();

        if(isUsernameRegistered(loginRequest)){
            Optional<Users> users = usersDao.getUserByPhone(loginRequest.getUsername());
            if(users.isPresent()){
                if(users.get().getFlag_change_password() == null){
                    generalResponse.setCode(Constant.Code.FORCE_CHANGE_PASSWORD.getStatusCode());
                    generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                    generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                    return generalResponse;
                }
            }

            loginRequest.setPassword(util.encryptPassword(loginRequest.getPassword()));

            Optional<User> user = isValidUser(loginRequest);

            if (user.isPresent()){
                loginResponse.setPermission(permissionDao.getPermissionByUsername(loginRequest.getUsername()));
                loginResponse.setUser_type(user.get().getUser_type());
                loginResponse.setName(user.get().getOwner_name().toUpperCase());
                loginResponse.setUser_id(user.get().getId());
                loginResponse.setEmail(user.get().getEmail());
                loginResponse.setUsername(loginRequest.getUsername());
                loginResponse.setRole_id(user.get().getRole_id());
                loginResponse.setSession(util.insertSession(loginResponse));

                generalResponse.setData(loginResponse);
                generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            } else {
                generalResponse.setCode(Constant.Code.WRONG_USERNAME_PASSWORD.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            }
        } else {
            generalResponse.setCode(Constant.Code.USERNAME_NOT_REGISTERED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return generalResponse;
    }

    @Override
    @Deprecated
    public GeneralResponse verifyAdminResendOTP(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        GeneralResponse generalResponse = new GeneralResponse();
        LoginResponse loginResponse = new LoginResponse();

        if(isUsernameRegisteredAsAdmin(loginRequest)){

            loginRequest.setPassword(util.encryptPassword(loginRequest.getPassword()));

            Optional<User> user = isValidUser(loginRequest);

            if (user.isPresent()){
                loginResponse.setUser_type(user.get().getUser_type());
                loginResponse.setName(user.get().getOwner_name());
                loginResponse.setUser_id(user.get().getId());
                loginResponse.setEmail(user.get().getEmail());

                generalResponse.setData(loginResponse);
                generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            } else {
                generalResponse.setCode(Constant.Code.WRONG_USERNAME_PASSWORD.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            }
        } else {
            generalResponse.setCode(Constant.Code.USERNAME_NOT_REGISTERED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return generalResponse;
    }

    private boolean isUsernameRegisteredAsAdmin(LoginRequest loginRequest) {
        return usersDao.isUsernameRegisteredAsAdmin(loginRequest.getUsername()) == 1;
    }

    private Optional<User> isValidUser(LoginRequest loginRequest) {
        return usersDao.verify(loginRequest.getUsername(), loginRequest.getPassword());
    }

    private boolean isUsernameRegistered(LoginRequest loginRequest) {
        return usersDao.isUsernameRegistered(loginRequest.getUsername()) == 1;
    }
}
