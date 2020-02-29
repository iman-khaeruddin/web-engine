package com.mospaze.werehouse.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mospaze.werehouse.entity.Config;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Token;
import com.mospaze.werehouse.entity.projector.DetailCities;
import com.mospaze.werehouse.model.*;
import com.mospaze.werehouse.repository.*;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.client.verify.CheckResponse;
import com.nexmo.client.verify.VerifyResponse;
import com.sendgrid.*;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@Component
public class Util {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private CustomTokenDao customTokenDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private MsCitiesDao msCitiesDao;

    @Autowired
    private CustomBookingRequestDao customBookingRequestDao;

    @Autowired
    private RedisTemplate<String, JWT> redisTemplate;

    @Value("${jwt_token_key}")
    private String JWT_TOKEN_KEY;

    @Value("${server.port}")
    private int port;

    private static final CsvMapper mapper = new CsvMapper();

    public Util() {
    }

    public String insertSession(LoginResponse loginResponse) throws UnsupportedEncodingException {
        JWT jwt = JWT.builder().name(loginResponse.getName())
                .email(loginResponse.getEmail())
                .user_id(loginResponse.getUser_id())
                .user_type(loginResponse.getUser_type())
                .username(loginResponse.getUsername())
                .build();

        String jwt_token = Jwts.builder()
                .setSubject("mospaze-core")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(addDays(new Date(),2))
                .claim("name", loginResponse.getName())
                .claim("email", loginResponse.getEmail())
                .claim("user_id", loginResponse.getUser_id())
                .claim("user_type", loginResponse.getUser_type())
                .claim("username", loginResponse.getUsername())
                .claim("role_id", loginResponse.getRole_id())
                .signWith(SignatureAlgorithm.HS256, JWT_TOKEN_KEY.getBytes("UTF-8"))
                .compact();

        redisTemplate.opsForHash().put("JWT", loginResponse.getUser_id(), jwt_token);
        redisTemplate.expire("JWT", 2, TimeUnit.DAYS);

        return jwt_token;
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    private String generateSession(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public String generateOtp(){
        int min = 1000;
        int max = 9999;
        return String.valueOf((int)(Math.random()*((max-min)+1))+min);
    }

    public Token insertTokenForgotPassword(ForgotPasswordRequest forgotPasswordRequest){
        Token token = new Token();
        String TOKEN = generateSession() + generateSession();
        if(customTokenDao.isAlreadyExist(TOKEN).compareTo(BigInteger.valueOf(0)) == 0){
            token.setToken(TOKEN);
            token.setFlag_active(1);
            token.setTimestamp(new Timestamp(new Date().getTime()));
            token.setType(Constant.TokenType.FORGOT_PASSWORD.getTokenType());
            token.setUsername(usersDao.getUserByEmail(forgotPasswordRequest.getEmail()).getPhone());

            tokenDao.save(token);
        } else {
            return insertTokenForgotPassword(forgotPasswordRequest);
        }
        return token;
    }

    public boolean checkTokenForgotPassword(String token, String username, String token_type){
        return (customTokenDao.isTimeOut(token, username, token_type).compareTo(BigInteger.valueOf(172800)) == -1);
    }

    public void killToken(String token, String username){
        tokenDao.updateToken(0, token, username);
    }

    public String encryptPassword(String password) throws InvalidKeyException, NoSuchAlgorithmException {
        String key = System.getenv("KEY");
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String encrypted_text = Hex.encodeHexString(sha256_HMAC.doFinal(password.getBytes(StandardCharsets.UTF_8)));

        logger.info("ENCRYPTED TEXT : {}", encrypted_text);

        return encrypted_text;
    }

    public void sendEmail(Map<String, String> email){

        try {
            Email from = new Email("Support@mospaze.com");
            String subject = email.get("subject");
            Email to = new Email(email.get("to"));
            Content content = new Content("text/plain", email.get("content"));
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid("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");

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            request.setBody(mail.build());

            Response response = sg.api(request);

            logger.info("Send Email : " + email);
            logger.info("response code : " + response.getStatusCode());
            logger.info("response body : " + response.getBody());
            logger.info("response header : " + response.getHeaders());
        } catch (IOException e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendEmailBookingStatus(Map<String, String> email){

        try {
            Email from = new Email("Support@mospaze.com");
            String subject = email.get("subject");
            Email to = new Email(email.get("to"));
            Content content = new Content("text/plain", email.get("content"));
            Mail mail = new Mail(from, subject, to, content);

            MailSettings mailSettings = new MailSettings();

            //bcc to log@mospaze.com
            BccSettings bccSettings = new BccSettings();
            bccSettings.setEmail(String.valueOf(Constant.configProperties.get(Constant.BCC_EMAIL_BOOKING_WAREHOUSE)));
            bccSettings.setEnable(true);
            mailSettings.setBccSettings(bccSettings);
            mail.setMailSettings(mailSettings);

            SendGrid sg = new SendGrid("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");

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            request.setBody(mail.build());

            Response response = sg.api(request);

            logger.info("Send Email : " + email);
            logger.info("response code : " + response.getStatusCode());
            logger.info("response body : " + response.getBody());
            logger.info("response header : " + response.getHeaders());
        } catch (IOException e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public SmsSubmissionResponse sendSMS(Map<String, String> message) {

        NexmoClient client = NexmoClient.builder().apiKey(String.valueOf(Constant.configProperties.get(Constant.NEXMO_API_KEY))).apiSecret(String.valueOf(Constant.configProperties.get(Constant.NEXMO_API_SECRET))).build();

        TextMessage textMessage = new TextMessage(message.get("brand"),
                message.get("to"),
                message.get("content")
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);

        logger.info("Send SMS : {}", message);
        logger.info("Status : {}", response.getMessages().get(0).getStatus());
        logger.info("ErrorText : {}", response.getMessages().get(0).getErrorText());

        return response;
    }

    public VerifyResponse sendOTP(Map<String, String> message) {

        NexmoClient client = NexmoClient.builder().apiKey(String.valueOf(Constant.configProperties.get(Constant.NEXMO_API_KEY))).apiSecret(String.valueOf(Constant.configProperties.get(Constant.NEXMO_API_SECRET))).build();

        //SEND OTP (Verify)
        VerifyResponse verifyResponse = client.getVerifyClient().verify(message.get("to"), message.get("from"));

        logger.info("RequestID: {}", verifyResponse.getRequestId());
        logger.info("Status : {}", verifyResponse.getStatus());
        logger.info("ErrorText : {}", verifyResponse.getErrorText());

        return verifyResponse;
    }

    public CheckResponse validateOTP(Map<String, String> message) throws IOException {

        NexmoClient client = NexmoClient.builder().apiKey(String.valueOf(Constant.configProperties.get(Constant.NEXMO_API_KEY))).apiSecret(String.valueOf(Constant.configProperties.get(Constant.NEXMO_API_SECRET))).build();

        //Check verification code
        CheckResponse checkResponse = client.getVerifyClient().check(message.get("request_id"), message.get("otp"));

        logger.info("RequestID: {}", checkResponse.getRequestId());
        logger.info("Status : {}", checkResponse.getStatus());
        logger.info("ErrorText : {}", checkResponse.getErrorText());
        logger.info("Price : {}", checkResponse.getPrice());
        logger.info("Currency : {}", checkResponse.getCurrency());

        return checkResponse;
    }

    public String validatePermissionDeleteUser(Optional<ModelHasRole> role_login_user, Optional<ModelHasRole> role_deleted_user){
        if(role_deleted_user.isPresent()){
            //is user SUPER ADMIN, ADMIN or SELLER
            if(Arrays.asList(Constant.Role.BUYER.getRole(), Constant.Role.SUB_SELLER.getRole()).contains(role_login_user.get().getRole_id()))
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is ADMIN deleted SUPER ADMIN
            if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_deleted_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is SELLER not deleted SUB SELLER
            if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() && role_deleted_user.get().getRole_id() != Constant.Role.SUB_SELLER.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is SUB SELLER deleted by SELLER, ADMIN or SUPER ADMIN
            if((role_deleted_user.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && role_deleted_user.get().getSuper_user() != role_login_user.get().getModel_id())){
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
            }

            //is user deleted him self
            if(role_login_user.get().getRole_id() == role_deleted_user.get().getRole_id())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            return Constant.Code.SUCCESS.getStatusCode();
        }
        return Constant.Code.USER_NOT_FOUND.getStatusCode();
    }

    public String validatePermissionGetUserByRole(Optional<ModelHasRole> role_login_user, Integer role){
        //is user SUPER ADMIN, ADMIN or SELLER
        if(Arrays.asList(Constant.Role.BUYER.getRole(), Constant.Role.SUB_SELLER.getRole()).contains(role_login_user.get().getRole_id()))
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SELLER get SUB SELLER
        if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() && role != null && (role != Constant.Role.SUB_SELLER.getRole()))
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is ADMIN get SUPER ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role != null && role == Constant.Role.SUPER_ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is ADMIN get ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role != null && role == Constant.Role.ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SUPER ADMIN get SUPER ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole() && role != null && role == Constant.Role.SUPER_ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        return Constant.Code.SUCCESS.getStatusCode();
    }

    public String validatePermissionSaveUser(Optional<ModelHasRole> role_login_user, UsersManagementRequest usersManagementRequest){
        //is user SUPER ADMIN, ADMIN or SELLER
        if(Arrays.asList(Constant.Role.BUYER.getRole(), Constant.Role.SUB_SELLER.getRole()).contains(role_login_user.get().getRole_id()))
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SELLER insert SUB SELLER
        if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() && usersManagementRequest.getRole() != Constant.Role.SUB_SELLER.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is ADMIN insert SUPER ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && usersManagementRequest.getRole() == Constant.Role.SUPER_ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is ADMIN insert ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && usersManagementRequest.getRole() == Constant.Role.ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SUPER ADMIN insert SUPER ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole() && usersManagementRequest.getRole() == Constant.Role.SUPER_ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is id_super_seller null if user ADMIN or SUPER ADMIN
        if((role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && usersManagementRequest.getRole() == Constant.Role.SUB_SELLER.getRole() && usersManagementRequest.getId_super_seller() == null) ||
                (role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole() && usersManagementRequest.getRole() == Constant.Role.SUB_SELLER.getRole() && usersManagementRequest.getId_super_seller() == null))
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        return Constant.Code.SUCCESS.getStatusCode();
    }

    public String validatePermissionGetDetailUser(Optional<ModelHasRole> role_login_user, Optional<ModelHasRole> role_get_user){
        if(role_get_user.isPresent()){
            //is user BUYER or SUB SELLER get him self
            if(Arrays.asList(Constant.Role.BUYER.getRole(), Constant.Role.SUB_SELLER.getRole()).contains(role_login_user.get().getRole_id()))
                if(role_login_user.get().getModel_id() != role_get_user.get().getModel_id())
                    return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is SELLER get user SUB SELLER or SELLER (him self)
            if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole()){
                if(role_get_user.get().getRole_id() == Constant.Role.SUB_SELLER.getRole()){
                    if(role_get_user.get().getSuper_user() != role_login_user.get().getModel_id())
                        return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
                }else if(role_get_user.get().getRole_id() == Constant.Role.SELLER.getRole()){
                    if(role_login_user.get().getModel_id() != role_get_user.get().getModel_id())
                        return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
                } else {
                    return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
                }
            }

            //is ADMIN get user SUPER ADMIN
            if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_get_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is ADMIN get user ANOTHER ADMIN
            if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_get_user.get().getRole_id() == Constant.Role.ADMIN.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            return Constant.Code.SUCCESS.getStatusCode();
        }
        return Constant.Code.USER_NOT_FOUND.getStatusCode();
    }

    public String validatePermissionChangeStatusUser(Optional<ModelHasRole> role_login_user, Optional<ModelHasRole> role_changed_user){
        if(role_changed_user.isPresent()){
            //is user SUPER ADMIN, ADMIN or SELLER
            if(Arrays.asList(Constant.Role.BUYER.getRole(), Constant.Role.SUB_SELLER.getRole()).contains(role_login_user.get().getRole_id()))
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is SUB SELLER changed by SELLER, ADMIN, or SUPER ADMIN
            if(role_changed_user.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && role_changed_user.get().getSuper_user() != role_login_user.get().getModel_id() ||
                    (role_changed_user.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && (role_login_user.get().getRole_id() != Constant.Role.SUPER_ADMIN.getRole() || role_login_user.get().getRole_id() != Constant.Role.ADMIN.getRole())))
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is ADMIN changed user SUPER ADMIN
            if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_changed_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is ADMIN changed user ADMIN
            if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_changed_user.get().getRole_id() == Constant.Role.ADMIN.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is SUPER ADMIN changed user SUPER ADMIN
            if(role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole() && role_changed_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            //is SELLER changed THEIR user SUB SELLER
            if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() && role_changed_user.get().getRole_id() != Constant.Role.SUB_SELLER.getRole())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            return Constant.Code.SUCCESS.getStatusCode();
        }
        return Constant.Code.USER_NOT_FOUND.getStatusCode();
    }

    public String validatePermissionUpdateUser(Optional<ModelHasRole> role_login_user, Optional<ModelHasRole> role_updated_user){
        //is user SUPER ADMIN, ADMIN or SELLER
        if(Arrays.asList(Constant.Role.BUYER.getRole(), Constant.Role.SUB_SELLER.getRole()).contains(role_login_user.get().getRole_id()))
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SELLER update user SUB SELLER
        if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() && role_updated_user.get().getRole_id() != Constant.Role.SUB_SELLER.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SUB SELLER updated by SELLER, ADMIN, or SUPER ADMIN
        if(role_updated_user.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && role_updated_user.get().getSuper_user() != role_login_user.get().getModel_id() ||
                (role_updated_user.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && (role_login_user.get().getRole_id() != Constant.Role.SUPER_ADMIN.getRole() || role_login_user.get().getRole_id() != Constant.Role.ADMIN.getRole())))
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is ADMIN update user SUPER ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_updated_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is ADMIN update user ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() && role_updated_user.get().getRole_id() == Constant.Role.ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        //is SUPER ADMIN update user SUPER ADMIN
        if(role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole() && role_updated_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        return Constant.Code.SUCCESS.getStatusCode();
    }

    @PostConstruct
    private void getAllConfig() throws JsonProcessingException {
        Iterable<Config> configs = configDao.findAll();
        Map<String, String> configProperties = new HashMap<>();
        Constant.SIMPLE_DATE_FORMAT_1.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String date = Constant.SIMPLE_DATE_FORMAT_1.format(new Date());
        logger.info("=== STORED TO LOCAL CACHE START ===");
        for(Config config : configs){
            configProperties.put(config.getKey(), config.getValue());

            logger.info("{} : {}", config.getKey(), config.getValue());
        }

        //SET STARTUP DATE
        configProperties.put(Constant.STARTUP_DATE, date);
        logger.info("{} : {}", Constant.STARTUP_DATE, date);

        //SET DETAIL DISTRICTS
        ObjectMapper objectMapper = new ObjectMapper();
        List<DetailCities> detailDistricts = msCitiesDao.getAllDistrictsEntities();
        String district = objectMapper.writeValueAsString(detailDistricts);
        configProperties.put(Constant.DETAIL_DISTRICTS, district);
        logger.info("{} : {}", Constant.DETAIL_DISTRICTS, district);

        Constant.setConfigProperties(configProperties);
        logger.info("=== STORED TO LOCAL CACHE END ===");
    }

    @PreDestroy
    public void preDestroy() throws UnknownHostException {
        Map<String, String> message = new HashMap<>();

        String ip = InetAddress.getLocalHost().getHostAddress();
        String host_name = InetAddress.getLocalHost().getHostName();
        String phone_number [] = String.valueOf(Constant.configProperties.get(Constant.IT_SUPPORT_PHONE_NUMBER)).split(",");
        Constant.SIMPLE_DATE_FORMAT_1.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String date = Constant.SIMPLE_DATE_FORMAT_1.format(new Date());
        String phone;

        for (int i = 0; i < phone_number.length; i++){
            phone = phone_number[i].trim();

            message.put("to", phone.startsWith("0") ? phone.replaceFirst("0", "62") : phone);
            message.put("brand", "Mospaze");
            message.put("content", "SERVER " + ip + ":" + port + " (" + host_name + ") is DOWN at " + date + ". PLEASE FIX ASAP!!");

            sendSMS(message);
        }
    }

    public JWT parseJWTToken(String jwt_token) throws UnsupportedEncodingException {

        Claims claims = Jwts.parser()
                .requireSubject("mospaze-core")
                .setSigningKey(JWT_TOKEN_KEY.getBytes("UTF-8"))
                .parseClaimsJws(jwt_token).getBody();

        String token = (String) redisTemplate.opsForHash().get("JWT", claims.get("user_id"));

        if(token != null){
            return JWT.builder().name(claims.get("name").toString())
                    .email(claims.get("email").toString())
                    .user_id((Integer) claims.get("user_id"))
                    .user_type(claims.get("user_type").toString())
                    .username(claims.get("username").toString())
                    .role_id((Integer) claims.get("role_id"))
                    .build();
        }
        else {
            logger.info("NOT FOUND session in REDIS, key : {}", claims.get("user_id"));
            return null;
        }
    }

    public String generateQuotationNumber(String code){
        BigInteger count = customBookingRequestDao.getCountBookingThisMonth(code).add(BigInteger.valueOf(1));

        BigInteger base = BigInteger.valueOf(1111);

        String padded = String.format("%04d" , base.add(count));

        String quotation_number = "MC/MISA/"+padded+"/"+code+"/"+Constant.SIMPLE_DATE_FORMAT_2.format(new Date());

        return quotation_number;
    }

    public void deleteImage(){
        for(int i = 1; i < 5; i++){
            File file = new File("/var/www/core/img/square"+i+".jpg");
            file.delete();

            logger.info(file.getAbsolutePath() + " IS DELETED");
        }

    }

    public static <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
        CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(true).withColumnSeparator(';');
        ObjectReader reader = mapper.readerFor(clazz).with(schema);
        return reader.<T>readValues(stream).readAll();
    }
}
