package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.LoginRequest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface LoginService {
    /**
     * @param loginRequest
     * @return {@link GeneralResponse}
     *
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    GeneralResponse verifyUser(LoginRequest loginRequest) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * @param loginRequest
     * @return {@link GeneralResponse}
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @Deprecated
    GeneralResponse verifyAdminResendOTP(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeyException;
}
