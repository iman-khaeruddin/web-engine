package com.mospaze.werehouse.common;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public class RestConstant {

    public static final String INDEX_WAREHOUSE = "/warehouse";
    public static final String INDEX_CALCULATOR = "/calculator";
    public static final String INDEX_BOOKING = "/booking";
    public static final String INDEX_REGISTER = "/register";
    public static final String INDEX_LOGIN = "/login";
    public static final String INDEX_LOGOUT = "/logout";
    public static final String INDEX_FORGOT_PASSWORD = "/forgot";
    public static final String INDEX_USER_MANAGEMENT = "/user";
    public static final String INDEX_MASTER_DATA = "/master_data";
    public static final String INDEX_UPLOAD = "/upload";
    public static final String INDEX_OTP = "/otp";
    public static final String INDEX_NOTIFICATION = "/notification";
    public static final String INDEX_WMS_DASHBOARD = "/wms";
    public static final String INDEX_USER_SETTING = "/user_setting";
    public static final String INDEX_DATA_TRACKER = "/data_tracker";

    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String SAVE = "/insert";
    public static final String DOWNLOAD = "/download";

    public static final String GET_ALL = "/get/all";
    public static final String GET_ALL_FIRST_CLASS_WAREHOUSE = GET_ALL + "/first_class";
    public static final String GET_ALL_BY_ID = "/get/{id}";
    public static final String GET_ALL_BY_FILTER = "/get/all_by_filter";
    public static final String GET_ALL_BY_STATUS = "/get/by_status";
    public static final String GET_DETAIL = "/get/detail/{id}";
    public static final String GET_USER_BY_ROLE = "/get/by_role";
    public static final String GET_USER_SELLER_BY_NAME = "/get/seller/{name}";
    public static final String GET_USER_BUYER_BY_NAME = "/get/buyer/{name}";
    public static final String GET_NOTIFICATION = "/get/count/notification";
    public static final String GET_WAREHOUSE_RENTED_BY_USER = INDEX_WAREHOUSE + "/rent";

    public static final String GET_MASTER_AUTOMATION = "/get/automation";
    public static final String GET_MASTER_CATEGORY = "/get/category";
    public static final String GET_MASTER_FEATURE = "/get/feature";
    public static final String GET_MASTER_SERVICE = "/get/service";
    public static final String GET_MASTER_SPACE = "/get/space";
    public static final String GET_MASTER_SPACE_SERVICE = "/get/space_service";
    public static final String GET_MASTER_TYPE = "/get/type";
    public static final String GET_MASTER_UNIT = "/get/unit";
    public static final String GET_MASTER_CITIES = "/get/cities";
    public static final String GET_MASTER_DISTRICT = "/get/districts";
    public static final String GET_MASTER_TEMPERATURE = "/get/temperature";
    public static final String GET_MASTER_OPERATING_HOUR = "/get/operating_hour";
    public static final String GET_MASTER_DISTRICT_SEARCH_WAREHOUSE = "/get/districts_search_warehouse";

    public static final String GET_MASTER_FEATURE_BY_ID = GET_MASTER_FEATURE+"/{id}";
    public static final String GET_MASTER_FEATURE_ALL = GET_MASTER_FEATURE+"/all";

    public static final String GET_MASTER_SERVICE_BY_ID = GET_MASTER_SERVICE+"/{id}";
    public static final String GET_MASTER_SERVICE_ALL = GET_MASTER_SERVICE+"/all";

    public static final String GET_MASTER_AUTOMATION_BY_ID = GET_MASTER_AUTOMATION+"/{id}";
    public static final String GET_MASTER_AUTOMATION_ALL = GET_MASTER_AUTOMATION+"/all";

    public static final String GET_MASTER_TYPE_BY_ID = GET_MASTER_TYPE+"/{id}";
    public static final String GET_MASTER_TYPE_ALL = GET_MASTER_TYPE+"/all";

    public static final String GET_MASTER_CATEGORY_BY_ID = GET_MASTER_CATEGORY+"/{id}";
    public static final String GET_MASTER_CATEGORY_ALL = GET_MASTER_CATEGORY+"/all";

    public static final String GET_MASTER_OPERATING_HOUR_BY_ID = GET_MASTER_OPERATING_HOUR+"/{id}";
    public static final String GET_MASTER_OPERATING_HOUR_ALL = GET_MASTER_OPERATING_HOUR+"/all";

    public static final String MASTER_FEATURE_UPDATE = "/feature"+UPDATE;
    public static final String MASTER_FEATURE_INSERT = "/feature"+SAVE;
    public static final String MASTER_FEATURE_DELETE = "/feature"+DELETE;

    public static final String MASTER_SERVICE_UPDATE = "/service"+UPDATE;
    public static final String MASTER_SERVICE_INSERT = "/service"+SAVE;
    public static final String MASTER_SERVICE_DELETE = "/service"+DELETE;

    public static final String MASTER_AUTOMATION_UPDATE = "/automation"+UPDATE;
    public static final String MASTER_AUTOMATION_INSERT = "/automation"+SAVE;
    public static final String MASTER_AUTOMATION_DELETE = "/automation"+DELETE;

    public static final String MASTER_TYPE_UPDATE = "/type"+UPDATE;
    public static final String MASTER_TYPE_INSERT = "/type"+SAVE;
    public static final String MASTER_TYPE_DELETE = "/type"+DELETE;

    public static final String MASTER_CATEGORY_UPDATE = "/category"+UPDATE;
    public static final String MASTER_CATEGORY_INSERT = "/category"+SAVE;
    public static final String MASTER_CATEGORY_DELETE = "/category"+DELETE;

    public static final String MASTER_OPERATING_HOUR_UPDATE = "/operating_hour"+UPDATE;
    public static final String MASTER_OPERATING_HOUR_INSERT = "/operating_hour"+SAVE;
    public static final String MASTER_OPERATING_HOUR_DELETE = "/operating_hour"+DELETE;

    public static final String GET_MAX_SIZE = "/get/max_size";
    public static final String CALCULATE_AREA = "/calculate/area";

    public static final String VERIFY_EMAIL = "/password/verify_email";
    public static final String VERIFY_TOKEN = "/password/verify_token";

    public static final String CHANGE_PASSWORD = "/change/password";
    public static final String CHANGE_STATUS = "/change/status";
    public static final String FORCE_CHANGE_PASSWORD = "/force/change_password";

    public static final String REQUEST_OTP = "/request_otp";
    public static final String RESEND_OTP = "/resend/otp";
    public static final String VALIDATE_OTP = "/validate";

    public static final String UPLOAD_IMAGE = "/{type}/image";

    public static final String REPORT = "/report";
    public static final String QUOTATION = "/quotation";

    public static final String TREND_OCCUPANCY = REPORT + "/trend_occupancy";
    public static final String SUMMARY_WH_STATUS = REPORT + "/summary_wh_status";
    public static final String OCCUPANCY_PRODUCT_FAMILY = REPORT + "/occupancy_product_family";
    public static final String JOB_LIST = REPORT + "/job_list";
    public static final String STOCK_STATUS = REPORT + "/stock_status";
    public static final String TREND_OCCUPANCY_ = "/trend_occupancy";
    public static final String SUMMARY_WH_STATUS_ = "/summary_wh_status";
    public static final String STOCK_ON_HAND = "/stock_on_hand";
    public static final String JOB_LIST_ = "/job_list";

    public static final String GET_DATA_TRACKER = "get/{type}";

    public static final String UPDATE_PARTNER_WAREHOUSE = UPDATE + "/partner";
    public static final String UPDATE_FIRST_CLASS_WAREHOUSE = UPDATE + "/first_class";
}
