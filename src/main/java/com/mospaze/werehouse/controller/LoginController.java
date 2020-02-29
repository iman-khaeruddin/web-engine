package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.LoginRequest;
import com.mospaze.werehouse.model.LoginResponse;
import com.mospaze.werehouse.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private LoginService loginService;

    /**
     * This controller used for login.
     *
     * @param loginRequest
     * @return {@link LoginResponse} there are sessions that are used for requests that require a session.
     *
     * @throws MethodArgumentNotValidException
     */
    @PostMapping(value = RestConstant.INDEX_LOGIN)
    public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("REQUEST : " + loginRequest.toString());

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            generalResponse = loginService.verifyUser(loginRequest);
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("en_message", fieldName + " " + errorMessage);
            errors.put("id_message", fieldName + " " + errorMessage);
            errors.put("code", Constant.Code.GENERAL_ERROR.getStatusCode());
        });
        return errors;
    }
}
