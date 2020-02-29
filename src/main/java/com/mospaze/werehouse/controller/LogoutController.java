package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.LoginRequest;
import com.mospaze.werehouse.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LogoutController {

    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private Util util;

    @Autowired
    private RedisTemplate<String, JWT> redisTemplate;

    /**
     * This controller used for logout. Session will be deleted.
     *
     * @param username
     * @param session
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.INDEX_LOGOUT)
    public ResponseEntity<GeneralResponse> logout(@RequestHeader(name = "username", required = false) String username,
                                                  @RequestHeader(name = "session") String session) {
        logger.info("USERNAME : " + username);
        logger.info("SESSION : " + session);

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    redisTemplate.opsForHash().delete("JWT", session);
                }
            }).start();

            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            e.printStackTrace();
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }
}
