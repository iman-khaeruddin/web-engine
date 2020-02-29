package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.ForgotPasswordRequest;
import com.mospaze.werehouse.model.GeneralResponse;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface ForgotPasswordService {
    /**
     * @param forgotPasswordRequest
     * @return {@link GeneralResponse}
     */
    GeneralResponse validateEmail(ForgotPasswordRequest forgotPasswordRequest);

    /**
     * @param token
     * @param username
     * @return {@link GeneralResponse}
     */
    GeneralResponse validateToken(String token, String username);
}
