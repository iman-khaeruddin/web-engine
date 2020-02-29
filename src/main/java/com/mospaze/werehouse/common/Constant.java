package com.mospaze.werehouse.common;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public class Constant {

    public static final String EMAIL_WORDING_APPROVED_ADMIN = "EMAIL_WORDING_APPROVED_ADMIN";
    public static final String EMAIL_WORDING_REJECTED_ADMIN = "EMAIL_WORDING_REJECTED_ADMIN";
    public static final String EMAIL_WORDING_APPROVED_SELLER = "EMAIL_WORDING_APPROVED_SELLER";
    public static final String EMAIL_WORDING_REJECTED_BUYER = "EMAIL_WORDING_REJECTED_BUYER";
    public static final String EMAIL_WORDING_QUOTATION_CREATE_BUYER = "EMAIL_WORDING_QUOTATION_CREATE_BUYER";
    public static final String SMS_WORDING_BOOKING_WAREHOUSE = "SMS_WORDING_BOOKING_WAREHOUSE";
    public static final String NEXMO_API_KEY = "NEXMO_API_KEY";
    public static final String SENDGRID_API_KEY = "SENDGRID_API_KEY";
    public static final String NEXMO_API_SECRET = "NEXMO_API_SECRET";
    public static final String SMS_WORDING_OTP = "SMS_WORDING_OTP";
    public static final String EMAIL_WORDING_RESET_PASSWORD = "EMAIL_WORDING_RESET_PASSWORD";
    public static final String URL_CONTACT_US = "URL_CONTACT_US";
    public static final String BCC_EMAIL_BOOKING_WAREHOUSE = "BCC_EMAIL_BOOKING_WAREHOUSE";
    public static final String IT_SUPPORT_PHONE_NUMBER = "IT_SUPPORT_PHONE_NUMBER";
    public static final String STARTUP_DATE = "STARTUP_DATE";
    public static final String EMAIL_WORDING_APPROVED_WAREHOUSE = "EMAIL_WORDING_APPROVED_WAREHOUSE";
    public static final String EMAIL_WORDING_REJECTED_WAREHOUSE = "EMAIL_WORDING_REJECTED_WAREHOUSE";
    public static final String EMAIL_ENQUIRY_MOSPAZE = "EMAIL_ENQUIRY_MOSPAZE";
    public static final String PHONE_ENQUIRY_MOSPAZE = "PHONE_ENQUIRY_MOSPAZE";
    public static final String DETAIL_CITIES = "DETAIL_CITIES";
    public static final String DETAIL_DISTRICTS = "DETAIL_DISTRICTS";

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_1 = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_2 = new SimpleDateFormat("MM/yyyy");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_3 = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_5 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_6 = new SimpleDateFormat("yyyy-MM");

    public static final Pattern PHONE_VALIDATION_REGEX = Pattern.compile("^(?:[0-9] ?){6,14}[0-9]$");
    public static final Pattern IMAGE_EXTENTION_VALIDATION_REGEX = Pattern.compile("([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.jpg|.jpeg|.png|.JPG|.JPEG|.PNG)$");

    public static Map<String, String> configProperties;

    public static Map<String, String> getConfigProperties() {
        return configProperties;
    }

    public static void setConfigProperties(Map<String, String> configProperties) {
        Constant.configProperties = configProperties;
    }

    public enum StatusBooking {
        OPEN(1),
        SELLER_REVIEW(2),
        SELLER_APPROVED(3),
        ADMIN_REJECTED(4),
        QUOTATION_REQUESTED(5),
        ADMIN_APPROVED(6),
        SELLER_REJECTED(7),
        ADMIN_APPROVED_REVIEW(8),
        BUYER_REJECTED(9);

        private int status;

        StatusBooking(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public enum Code {
        SUCCESS("00"),
        GENERAL_ERROR("01"),

        PHONE_DUPLICATE("12"),
        EMAIL_DUPLICATE("42"),
        FEATURE_DUPLICATE("55"),
        AUTOMATION_DUPLICATE("56"),
        CATEGORY_DUPLICATE("57"),
        SERVICE_DUPLICATE("58"),
        TYPE_DUPLICATE("60"),
        WAREHOUSE_NAME_DUPLICATE("59"),

        WRONG_USERNAME_PASSWORD("32"),
        USERNAME_NOT_REGISTERED("63"),
        EMAIL_NOT_REGISTERED("54"),
        ROLE_NOT_ALLOWED("85"),
        NOT_BUYER("10"),

        SESSION_TIMEOUT("89"),

        FORCE_CHANGE_PASSWORD("88"),
        FORCE_CHANGE_PASSWORD_NOT_ALLOWED("44"),

        USER_NOT_FOUND("78"),
        DATA_NOT_FOUND("72"),
        WAREHOUSE_NOT_FOUND("43"),
        PHONE_NUMBER_NOT_FOUND("65"),

        ALREADY_REQUESTED("53"),
        WRONG_PATH_VARIABLE("31"),

        NO_MEDIA_SELECTED("77"),
        MAX_SIZE_FILE_EXCEEDED("66"),

        OTP_EXPIRED("21"),
        OTP_ALREADY_IN_USED("17"),
        WRONG_OTP("95"),
        ATTEMPT_OTP_ALREADY_REACH("41"),

        ACTION_INVALID("19"),
        TOKEN_INVALID("75"),
        PHONE_NUMBER_INVALID("98"),
        FILE_FORMAT_INVALID("22"),
        DATE_BOOKING_INVALID("09"),
        ADDRESS_INVALID("05"),
        OLD_PASSWORD_INVALID("73"),

        WAREHOUSE_NOT_ACTIVE("14"),
        FIRST_CLASS_WAREHOUSE_ALREADY_REACH("15");

        private String statusCode;

        Code(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusCode() {
            return statusCode;
        }
    }

    public static String getEnMessage(String code) {
        Map<String, String> message = new HashMap<>();

        message.put("00", "Successfully");
        message.put("01", "Failed");
        message.put("12", "Phone already in used");
        message.put("42", "Email already in used");
        message.put("32", "Wrong password");
        message.put("63", "Username not registered");
        message.put("54", "Email not registered");
        message.put("75", "Token invalid");
        message.put("89", "Session timeout");
        message.put("88", "Please change your password first");
        message.put("85", "Account not allowed");
        message.put("59", "Warehouse name already in used");
        message.put("78", "User not found");
        message.put("95", "Invalid OTP");
        message.put("53", "Please wait a few minute");
        message.put("43", "Warehouse not found");
        message.put("72", "Data not found");
        message.put("31", "Wrong url");
        message.put("55", "Feature name already in used");
        message.put("56", "Automation name already in used");
        message.put("57", "Category name already in used");
        message.put("58", "Service name already in used");
        message.put("60", "Type name already in used");
        message.put("19", "Action invalid");
        message.put("77", "No image selected");
        message.put("21", "Otp expired");
        message.put("17", "Otp already in used");
        message.put("98", "Phone number invalid");
        message.put("41", "You've reached the maximum limit of OTP request");
        message.put("65", "Phone number not found");
        message.put("44", "You not allowed to force change password");
        message.put("09", "End date should greater than start date");
        message.put("66", "The file exceeds its maximum permitted size of 5242880 byte");
        message.put("22", "Format file not allowed");
        message.put("05", "Address invalid");
        message.put("10", "User is not buyer");
        message.put("14", "Warehouse not active");
        message.put("15", "First class Warehouse already reach, max 5 warehouse");
        message.put("73", "Old password invalid");

        return message.get(code);
    }

    public static String getInMessage(String code) {
        Map<String, String> message = new HashMap<>();

        message.put("00", "Sukses");
        message.put("01", "Gagal");
        message.put("12", "No Handphone sudah digunakan");
        message.put("42", "Email sudah digunakan");
        message.put("32", "Password salah");
        message.put("63", "Username belum terdaftar");
        message.put("54", "Email belum terdaftar");
        message.put("75", "Token invalid");
        message.put("89", "Session timeout");
        message.put("88", "Silakan ganti password anda");
        message.put("85", "Account anda tidak diizinkan");
        message.put("59", "Nama warehouse sudah digunakan");
        message.put("78", "User tidak ditemukan");
        message.put("95", "OTP salah");
        message.put("53", "Mohon tunggu beberapa saat");
        message.put("43", "Warehouse tidak ditemukan");
        message.put("72", "Data tidak ditemukan");
        message.put("31", "URL salah");
        message.put("55", "Nama feature sudah digunakan");
        message.put("56", "Nama automation sudah digunakan");
        message.put("57", "Nama category sudah digunakan");
        message.put("58", "Nama service sudah digunakan");
        message.put("60", "Nama type sudah digunakan");
        message.put("19", "Action invalid");
        message.put("77", "Gambar tidak terpilih");
        message.put("21", "Otp kadaluarsa");
        message.put("17", "Otp sudah digunakan");
        message.put("98", "No Handphone invalid");
        message.put("41", "Anda sudah mencapai batas maksimal request OTP");
        message.put("65", "No Handphone tidak ditemukan");
        message.put("44", "Anda tidak diizinkan untuk force change password");
        message.put("09", "Tanggal selesai harus lebih besar dari tanggal mulai");
        message.put("66", "File melebihi ukuran maksimum yang diizinkan yaitu 5242880 byte");
        message.put("22", "File format tidak diizinkan");
        message.put("05", "Alamat invalid");
        message.put("10", "User bukan buyer");
        message.put("14", "Warehouse belum aktif");
        message.put("15", "First class Warehouse sudah terpenuhi, max 5 warehouse");
        message.put("73", "Password lama invalid");

        return message.get(code);
    }

    public enum StatusAvailableWarehouse {
        UNAVAILABLE(0),
        AVAILABLE(1),
        BOOKED(2);

        private int status;

        StatusAvailableWarehouse(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public enum TokenType {
        FORGOT_PASSWORD("FORGOT_PASSWORD");

        private String tokenType;

        TokenType(String tokenType) { this.tokenType = tokenType; }

        public String getTokenType() { return tokenType; }
    }

    public enum Role {
        SUPER_ADMIN(1),
        ADMIN(2),
        SELLER(3),
        BUYER(4),
        SUB_SELLER(5);

        private int role;

        Role(int role) {
            this.role = role;
        }

        public int getRole() {
            return role;
        }
    }

    public enum ImageType {
        WAREHOUSE(1),
        USER(2);

        private int type;

        ImageType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum NotificationType {
        WAREHOUSE(1),
        USER(2),
        BOOKING(3);

        private int type;

        NotificationType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum StatusVerification {
        PENDING_VERIFICATION(1),
        INFORMATION_VERIFICATION(2),
        WAREHOUSE_INSPECTION(3),
        APPROVED(4),
        REJECTED(5);

        private int status;

        StatusVerification(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public static Collection<Integer> getStatusVerification() {
        return Arrays.asList(StatusVerification.PENDING_VERIFICATION.getStatus(),
                StatusVerification.INFORMATION_VERIFICATION.getStatus(),
                StatusVerification.WAREHOUSE_INSPECTION.getStatus(),
                StatusVerification.APPROVED.getStatus(),
                StatusVerification.REJECTED.getStatus());
    }

    public static Collection<Integer> getActionChangeStatusBooking() {
        return Arrays.asList(StatusBooking.SELLER_APPROVED.getStatus(),
                StatusBooking.SELLER_REJECTED.getStatus(),
                StatusBooking.ADMIN_APPROVED.getStatus(),
                StatusBooking.ADMIN_REJECTED.getStatus(),
                StatusBooking.QUOTATION_REQUESTED.getStatus(),
                StatusBooking.ADMIN_APPROVED_REVIEW.getStatus());
    }

    public enum QuotationCode {
        SH("SH"),
        SO("SO");

        private String code;

        QuotationCode(String status) {
            this.code = status;
        }

        public String getCode() {
            return code;
        }
    }

    public static List<String> StatusTransactionList(){
        List<String> status_transaction_list = new ArrayList<>();

        status_transaction_list.add("OPEN");
        status_transaction_list.add("PICK");
        status_transaction_list.add("PACK");
        status_transaction_list.add("READY FOR SHIP");
        status_transaction_list.add("CLOSED");

        return status_transaction_list;
    }

    public enum DataTrackerType {
        OS("os"),
        BROWSER("browser"),
        IP_ADDRESS("ip_address"),
        CITY("city"),
        DISTRICT("district");

        private String type;

        DataTrackerType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public static List<String> DataTrackerTypeList(){
        List<String> data_tracker_type_list = new ArrayList<>();

        data_tracker_type_list.add(DataTrackerType.OS.getType());
        data_tracker_type_list.add(DataTrackerType.BROWSER.getType());
        data_tracker_type_list.add(DataTrackerType.IP_ADDRESS.getType());
        data_tracker_type_list.add(DataTrackerType.CITY.getType());
        data_tracker_type_list.add(DataTrackerType.DISTRICT.getType());

        return data_tracker_type_list;
    }
}
