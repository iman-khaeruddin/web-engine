package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.UpdateUserProfileRequest;
import com.mospaze.werehouse.model.UpdateUsersRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Iman Khaeruddin
 * @since 23/11/2019
 */
public interface UserProfileService {

    /**
     * @param updateUserProfileRequest
     * @param jwt
     * @return {@link String}
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    String editProfile(UpdateUserProfileRequest updateUserProfileRequest, JWT jwt) throws NoSuchAlgorithmException, InvalidKeyException;

    /**
     * @param id
     * @return {@link GeneralResponse}
     */
    GeneralResponse getDetailUsersById(int id);
}
