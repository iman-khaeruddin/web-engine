package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.Token;
import com.mospaze.werehouse.model.ForgotPasswordRequest;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.ForgotPasswordService
 */
@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private Util util;

    @Value("${url.forgot.password}")
    private String url_forgot_password;

    @Override
    @Transactional
    public GeneralResponse validateEmail(ForgotPasswordRequest forgotPasswordRequest) {
        GeneralResponse generalResponse = new GeneralResponse();

        if(usersDao.isEmailRegistered(forgotPasswordRequest.getEmail()) != 1){
            generalResponse.setCode(Constant.Code.EMAIL_NOT_REGISTERED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        Token token = util.insertTokenForgotPassword(forgotPasswordRequest);

        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
        generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        String URL = url_forgot_password+"?token="+token.getToken()+"&username="+token.getUsername();

        Map<String, String> email = new HashMap<>();
        email.put("to", forgotPasswordRequest.getEmail());
        email.put("subject", "Reset Password");
        email.put("content", String.valueOf(Constant.configProperties.get(Constant.EMAIL_WORDING_RESET_PASSWORD))
                .replace("[url]", URL)
                .replace("[url_contact_us]", Constant.configProperties.get(Constant.URL_CONTACT_US)));

        util.sendEmail(email);

        return generalResponse;
    }

    @Override
    public GeneralResponse validateToken(String token, String username) {
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setCode(util.checkTokenForgotPassword(token, username, Constant.TokenType.FORGOT_PASSWORD.getTokenType()) ?
                Constant.Code.SUCCESS.getStatusCode() : Constant.Code.TOKEN_INVALID.getStatusCode());
        generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
        generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        return generalResponse;
    }
}
