package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.Otp;
import com.mospaze.werehouse.entity.Users;
import com.mospaze.werehouse.model.OtpPageable;
import com.mospaze.werehouse.model.ValidateOTPRequest;
import com.mospaze.werehouse.repository.OtpDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.OtpService
 */
@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpDao otpDao;

    @Autowired
    private Util util;

    @Autowired
    private UsersDao usersDao;

    @Override
    public List<Otp> validatePhone(String phone) {
        return otpDao.findByPhone(phone);
    }

    @Override
    public List<Otp> validatePhoneAndOtp(String phone, String otp) {
        return otpDao.findByPhoneAndOtp(phone, otp);
    }

    @Override
    public Otp insertOtp(Otp otp) {
        return otpDao.save(otp);
    }

    @Override
    public Otp updateOtp(Otp otp) {
        return otpDao.save(otp);
    }

    @Override
    public OtpPageable getListOtp(Integer page, Integer size) {
        OtpPageable otpPageable = new OtpPageable();

        Pageable pageable = PageRequest.of(page, size);

        Page<Otp> otps = otpDao.findAllPageable(pageable);

        otpPageable.setDetail_otp(otps.getContent());
        otpPageable.setTotal_page(BigInteger.valueOf(otps.getTotalPages()));
        otpPageable.setTotal_element(BigInteger.valueOf(otps.getTotalElements()));

        return otpPageable;
    }

    @Override
    public String resendOtp(String phone) {
        Map<String, String> message = new HashMap<>();
        Otp otp = new Otp();

        List<Otp> otps = validatePhone(phone);
        if(otps.size() != 1)
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();

        for (Otp otp1: otps) {
            otp.setPhone(otp1.getPhone());
            otp.setOtp(otp1.getOtp());
            otp.setCreated_at(otp1.getCreated_at());
            otp.setStatus((otp1.getStatus()));
            otp.setId(otp1.getId());
            otp.setAttempt(otp1.getAttempt());
        }

        if(otp.getStatus() == 0)
            return Constant.Code.OTP_ALREADY_IN_USED.getStatusCode();

        otp.setOtp(util.generateOtp());
        otp.setCreated_at(new Date());
        otp.setStatus((byte) 1);

        otp = otpDao.save(otp);

        message.put("to", phone);
        message.put("brand", "Mospaze");
        message.put("content", String.valueOf(Constant.configProperties.get(Constant.SMS_WORDING_OTP))
                .replace("[otp]", otp.getOtp())
                .replace("[ttl]", "5 minute."));

        util.sendSMS(message);

        return Constant.Code.SUCCESS.getStatusCode();
    }

    @Override
    @Transactional
    public String requestOTPForceChangePassword(String phone) throws ParseException {
        Map<String, String> message = new HashMap<>();
        Otp otp = new Otp();

        List<Otp> otps = validatePhone(phone);
        Optional<Users> users = usersDao.getUserByPhone(phone);

        if(users.isPresent()){
            if(users.get().getFlag_change_password() != null)
                return Constant.Code.FORCE_CHANGE_PASSWORD_NOT_ALLOWED.getStatusCode();
        } else {
            return Constant.Code.PHONE_NUMBER_NOT_FOUND.getStatusCode();
        }

        //insert OTP (new user)
        if(otps.size() == 0){
            otp.setPhone(phone);
            otp.setOtp(util.generateOtp());
            otp.setCreated_at(new Date());
            otp.setStatus((byte) 1);
            otp.setAttempt(1);

            otp = insertOtp(otp);
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

            otp = updateOtp(otp);
        }

        message.put("to", phone);
        message.put("brand", "Mospaze");
        message.put("content", String.valueOf(Constant.configProperties.get(Constant.SMS_WORDING_OTP))
                .replace("[otp]", otp.getOtp())
                .replace("[ttl]", "5 minute."));

        util.sendSMS(message);

        return Constant.Code.SUCCESS.getStatusCode();
    }

    @Override
    @Transactional
    public String validateOtp(ValidateOTPRequest validateOTPRequest) {

        List<Otp> otps = validatePhoneAndOtp(validateOTPRequest.getPhone(), validateOTPRequest.getOtp());
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

            //update status OTP
            otp.setStatus((byte) 0);
            otp.setAttempt(0);
            updateOtp(otp);

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.WRONG_OTP.getStatusCode();
        }
    }
}
