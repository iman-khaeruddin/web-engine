package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Otp;
import com.mospaze.werehouse.entity.Users;
import com.mospaze.werehouse.model.RegisterRequest;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.OtpService;
import com.mospaze.werehouse.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.RegisterUserService
 */
@Service
public class RegisterServiceImpl implements RegisterUserService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private Util util;

    @Autowired
    private OtpService otpService;

    @Override
    @Transactional
    public String registerUser(RegisterRequest registerRequest) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        if (usersDao.countByPhoneNumber(registerRequest.getPhone_number()) > 0)
            return Constant.Code.PHONE_DUPLICATE.getStatusCode();

        Map<String, String> message = new HashMap<>();
        message.put("request_id", registerRequest.getRequest_id());
        message.put("otp", registerRequest.getOtp());

        List<Otp> otps = otpService.validatePhoneAndOtp(registerRequest.getPhone_number(), registerRequest.getOtp());
        Otp otp = new Otp();

        if (otps.size() == 1) {
            for(Otp otp1 : otps){
                otp.setPhone(otp1.getPhone());
                otp.setOtp(otp1.getOtp());
                otp.setCreated_at(otp1.getCreated_at());
                otp.setStatus((otp1.getStatus()));
                otp.setId(otp1.getId());
            }

            if(new Date().getTime() - otp.getCreated_at().getTime() > 300000)
                return Constant.Code.OTP_EXPIRED.getStatusCode();

            if(otp.getStatus() == 0)
                return Constant.Code.OTP_ALREADY_IN_USED.getStatusCode();

            Users users = insertUser(registerRequest);
            inserUserType(users, registerRequest.getUser_type());

            //update status OTP
            otp.setStatus((byte) 0);
            otp.setAttempt(0);
            otpService.updateOtp(otp);

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.WRONG_OTP.getStatusCode();
        }
    }

    @Override
    public String requestOTPRegister(RegisterRequest registerRequest) throws ParseException {
        if (usersDao.countByPhoneNumber(registerRequest.getPhone_number()) > 0)
            return Constant.Code.PHONE_DUPLICATE.getStatusCode();

        if (usersDao.isEmailRegistered(registerRequest.getEmail()) > 0)
            return Constant.Code.EMAIL_DUPLICATE.getStatusCode();

        List<Otp> otps = otpService.validatePhone(registerRequest.getPhone_number());
        Otp otp = new Otp();

        //insert OTP (new user)
        if(otps.size() == 0){
            otp.setPhone(registerRequest.getPhone_number());
            otp.setOtp(util.generateOtp());
            otp.setCreated_at(new Date());
            otp.setStatus((byte) 1);
            otp.setAttempt(1);

            otp = otpService.insertOtp(otp);
        }

        //update OTP (resend OTP)
        if(otps.size() == 1){
            for (Otp otp1: otps) {
                otp.setPhone(otp1.getPhone());
                otp.setOtp(otp1.getOtp());
                otp.setCreated_at(otp1.getCreated_at());
                otp.setStatus((otp1.getStatus()));
                otp.setId(otp1.getId());
                otp.setAttempt(otp1.getAttempt());
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            if(new Date().getTime() - otp.getCreated_at().getTime() < 300000 && otp.getStatus() == 1){     //validate interval request OTP
                return Constant.Code.ALREADY_REQUESTED.getStatusCode();
            } else if(otp.getAttempt() == 5 && simpleDateFormat.parse(simpleDateFormat.format(otp.getCreated_at())).equals(simpleDateFormat.parse(simpleDateFormat.format(new Date())))) {     //validate attempt OTP
                return Constant.Code.ATTEMPT_OTP_ALREADY_REACH.getStatusCode();
            } else {    //create new OTP
                int count = otp.getAttempt();
                if(simpleDateFormat.format(otp.getCreated_at()).equals(simpleDateFormat.format(new Date())))
                    count = count + 1;
                else
                    count = 1;

                otp.setAttempt(count);
                otp.setOtp(util.generateOtp());
                otp.setCreated_at(new Date());
                otp.setStatus((byte) 1);
            }

            otp = otpService.updateOtp(otp);
        }

        //send OTP to SMS and email
        final String final_otp = otp.getOtp();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> message = new HashMap<>();
                message.put("to", registerRequest.getPhone_number());
                message.put("brand", "Mospaze");
                message.put("content", String.valueOf(Constant.configProperties.get(Constant.SMS_WORDING_OTP))
                        .replace("[otp]", final_otp)
                        .replace("[ttl]", "5 minute."));

                Map<String, String> email = new HashMap<>();
                email.put("subject", "OTP Registration");
                email.put("to", registerRequest.getEmail());
                email.put("content", String.valueOf(Constant.configProperties.get(Constant.SMS_WORDING_OTP))
                        .replace("[otp]", final_otp)
                        .replace("[ttl]", "5 minute."));

                util.sendSMS(message);

                util.sendEmail(email);
            }
        }).start();

        return Constant.Code.SUCCESS.getStatusCode();
    }

    private Users insertUser(RegisterRequest registerRequest) throws InvalidKeyException, NoSuchAlgorithmException {
        Users users = new Users();
        users.setFirst_name(registerRequest.getFirst_name());
        users.setLast_name(registerRequest.getLast_name());
        users.setEmail(registerRequest.getEmail());
        users.setPhone(registerRequest.getPhone_number());
        users.setPassword(util.encryptPassword(registerRequest.getPassword()));
        users.setUsername(registerRequest.getEmail());
        users.setConfirmed(1);
        users.setStatus(1);
        users.setVerified_phone(1);
        users.setConfirmation_code(String.valueOf(99999));
        users.setCreated_at(new Date());
        users.setFlag_change_password(new Date());

        return usersDao.save(users);
    }

    private ModelHasRole inserUserType(Users users, int user_type) {
        ModelHasRole modelHasRole = new ModelHasRole();
        modelHasRole.setModel_id(users.getId());
        modelHasRole.setRole_id(user_type);
        modelHasRole.setModel_type("Mospaze\\Models\\User\\User");
        return modelHasRoleDao.save(modelHasRole);
    }

}
