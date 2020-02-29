package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.projector.User;
import com.mospaze.werehouse.entity.projector.UserByRole;
import com.mospaze.werehouse.model.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface UserManagementService {
    /**
     * @param changePasswordRequest
     * @param username
     * @return {@link GeneralResponse}
     *
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    @Deprecated
    GeneralResponse changePassword(ChangePasswordRequest changePasswordRequest, String username) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * @param changePasswordRequest
     * @param username
     * @param token
     * @return {@link GeneralResponse}
     *
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    GeneralResponse forgotPassword(ChangePasswordRequest changePasswordRequest, String username, String token) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * @param role_id
     * @param name_email
     * @param userRole
     * @param page
     * @param size
     * @return list of {@link UserByRole} with pageable
     */
    UsersPageable getUsersByRole(Integer role_id, String name_email, ModelHasRole userRole, Integer page, Integer size);

    /**
     * @param usersManagementRequest
     * @param username
     * @return {@link String}
     *
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    String insertUsers(UsersManagementRequest usersManagementRequest, String username) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * @param id
     * @return {@link GeneralResponse}
     */
    GeneralResponse getDetailUsersById(int id);

    /**
     * @param id
     * @return {@link String}
     */
    String deleteUser(int id);

    /**
     * @param status
     * @param id
     * @return {@link String}
     */
    String changeStatusUser(int status, int id);

    /**
     * @param usersManagementRequest
     * @return {@link String}
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    String updateUser(UpdateUsersRequest usersManagementRequest) throws NoSuchAlgorithmException, InvalidKeyException;

    /**
     * @param name
     * @return list of {@link User}
     */
    List<User> getUserSellerByName(String name);

    /**
     * @param forceChangePassword
     * @return {@link String}
     *
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    String forceChangePassword(ForceChangePassword forceChangePassword) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * @param name
     * @return list of {@link User}
     */
    List<User> getUserBuyerByName(String name);
}
