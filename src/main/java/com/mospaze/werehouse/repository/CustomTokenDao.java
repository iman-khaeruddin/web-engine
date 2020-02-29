package com.mospaze.werehouse.repository;

import java.math.BigInteger;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface CustomTokenDao {
    /**
     * @param token
     * @param username
     * @param token_type
     * @return {@link BigInteger}
     */
    BigInteger isTimeOut(String token, String username, String token_type);

    /**
     * @param token
     * @return {@link BigInteger}
     */
    BigInteger isAlreadyExist(String token);
}
