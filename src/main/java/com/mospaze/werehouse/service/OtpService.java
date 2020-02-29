package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.Otp;
import com.mospaze.werehouse.model.OtpPageable;
import com.mospaze.werehouse.model.ValidateOTPRequest;

import java.text.ParseException;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface OtpService {
    /**
     * @param phone
     * @return list of {@link Otp}
     */
    List<Otp> validatePhone(String phone);

    /**
     * @param phone
     * @param otp
     * @return list of {@link Otp}
     */
    List<Otp> validatePhoneAndOtp(String phone, String otp);

    /**
     * @param otp
     * @return {@link Otp}
     */
    Otp insertOtp(Otp otp);

    /**
     * @param otp
     * @return {@link Otp}
     */
    Otp updateOtp(Otp otp);

    /**
     * @param page
     * @param size
     * @return list of {@link Otp} with pageable
     */
    OtpPageable getListOtp(Integer page,
                           Integer size);

    /**
     * @param phone
     * @return {@link String}
     */
    String resendOtp(String phone);

    /**
     * @param phone
     * @return {@link String}
     *
     * @throws ParseException
     */
    String requestOTPForceChangePassword(String phone) throws ParseException;

    /**
     * @param validateOTPRequest
     * @return {@link String}
     */
    String validateOtp(ValidateOTPRequest validateOTPRequest);
}
