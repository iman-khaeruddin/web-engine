package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.RegisterRequest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface RegisterUserService {
    /**
     * @param registerRequest
     * @return {@link String}
     *
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    String registerUser(RegisterRequest registerRequest) throws IOException, InvalidKeyException, NoSuchAlgorithmException;

    /**
     * @param registerRequest
     * @return {@link String}
     *
     * @throws ParseException
     */
    String requestOTPRegister(RegisterRequest registerRequest) throws ParseException;
}
