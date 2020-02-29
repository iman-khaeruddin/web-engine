package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.*;
import com.mospaze.werehouse.repository.*;
import com.mospaze.werehouse.service.BookingService;
import com.mospaze.werehouse.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.BookingService
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRequestDao bookingRequestDao;

    @Autowired
    private CustomBookingRequestDao customBookingRequestDao;

    @Autowired
    private LeasingSpaceDao leasingSpaceDao;

    @Autowired
    private FulfillmentSpaceDao fulfillmentSpaceDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private MsUnitDao msUnitDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Util util;

    @Autowired
    private ConfigDao configDao;

    @Value("${url.booking}")
    private String url_booking;

    @Override
    @Transactional
    public String bookingProcess(BookingRequestModel bookingRequestModel, String username) {

        //validate start date and end date (start date < end date)
        long date_booking_diff = bookingRequestModel.getEnd_date().getTime() - bookingRequestModel.getStart_date().getTime();
        if(date_booking_diff < 1)
            return Constant.Code.DATE_BOOKING_INVALID.getStatusCode();

        Optional<Users> users = usersDao.getUserByPhone(username);

        if (modelHasRoleDao.findRoleByUserId(users.get().getId()) != Constant.Role.BUYER.getRole())
            return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

        Optional<Warehouse> warehouses = warehouseDao.findById((int)bookingRequestModel.getWarehouse_id());
        if(!warehouses.isPresent()){
            return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
        }

        //insert booking
        bookingRequestModel.setName(users.get().getFirst_name().concat(" ").concat(users.get().getLast_name()));
        bookingRequestModel.setEmail(users.get().getEmail());
        bookingRequestModel.setPhone_number(users.get().getPhone());
        BookingRequest bookingRequest = insertBooking(bookingRequestModel, username);

        //update status available warehouse
        /*Optional<Warehouse> warehouses = warehouseDao.findById(bookingRequestModel.getWarehouse_id());
        if (warehouses.isPresent()) {
            Warehouse warehouse = warehouses.get();
            warehouse.setStatus_available(Constant.StatusAvailableWarehouse.BOOKED.getStatus());
            warehouseDao.save(warehouse);
        }*/

        //insert leasing space
        if (bookingRequestModel.getLeasing_space() != null)
            insertLeasingSpace(bookingRequestModel, bookingRequest);

        //insert fulfillment space
        if (bookingRequestModel.getFulfillment_space() != null)
            insertFulfillmentSpace(bookingRequestModel, bookingRequest);

        //insert notification
        Notification notification = new Notification();
        notification.setUser_id(0);
        notification.setType(Constant.NotificationType.BOOKING.getType());
        notification.setModel_id(bookingRequest.getId());
        notification.setId_message("Booking " + bookingRequest.getKode_booking() + " menunggu diproses");
        notification.setEn_message("Booking " + bookingRequest.getKode_booking() + " waiting processed");
        notification.setCreated_at(new Date());
        notificationService.insertNotification(notification);

        return Constant.Code.SUCCESS.getStatusCode();
    }

    @Override
    public BookingRequestPageable getBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole role_login_user, Integer page, Integer size) {
        BookingRequestPageable bookingRequestPageable = new BookingRequestPageable();

        bookingRequestPageable.setDetailBookingRequest(customBookingRequestDao.getBookingRequestByStatus(status, warehouse_name, role_login_user, page, size));
        bookingRequestPageable.setTotal_page(customBookingRequestDao.getTotalPageBookingRequestByStatus(status, warehouse_name, role_login_user, page, size));
        bookingRequestPageable.setTotal_element(customBookingRequestDao.getTotalElementBookingRequestByStatus(status, warehouse_name, role_login_user, page, size));

        if(bookingRequestPageable.getDetailBookingRequest().size() > 0){
            bookingRequestPageable.setFrom((page * size) + 1);
            bookingRequestPageable.setTo((page * bookingRequestPageable.getDetailBookingRequest().size()) + bookingRequestPageable.getDetailBookingRequest().size());

            if(bookingRequestPageable.getDetailBookingRequest().size() != size)
                bookingRequestPageable.setTo(bookingRequestPageable.getFrom() + bookingRequestPageable.getDetailBookingRequest().size() - 1);
        }

        return bookingRequestPageable;
    }

    @Override
    public GeneralResponse getDetailBookingRequestById(int id, ModelHasRole role_login_user) {
        GeneralResponse generalResponse = new GeneralResponse();
        OrderRequestModel orderRequestModel = new OrderRequestModel();

        Optional<BookingRequest> bookingRequest = bookingRequestDao.findById(id);

        if (bookingRequest.isPresent()) {
            orderRequestModel.setId(bookingRequest.get().getId());
            orderRequestModel.setPic_name(bookingRequest.get().getPic_name());
            orderRequestModel.setPic_email(bookingRequest.get().getEmail());
            orderRequestModel.setPic_phone(bookingRequest.get().getPhone_number());
            orderRequestModel.setKode_booking(bookingRequest.get().getKode_booking());
            orderRequestModel.setStart_date(bookingRequest.get().getStart_date());
            orderRequestModel.setEnd_date(bookingRequest.get().getEnd_date());
            orderRequestModel.setStorage_quantity(bookingRequest.get().getCapacity());
            orderRequestModel.setWarehouse_type(bookingRequest.get().getCategory());
            orderRequestModel.setWarehouse_service_type(bookingRequest.get().getIs_fulfillment_space() == 1 ? "Fulfillment Space" : "Leasing Space");
            orderRequestModel.setStatus(bookingRequest.get().getStatus());
            orderRequestModel.setMospaze_price(bookingRequest.get().getMospaze_price());
            orderRequestModel.setSeller_price(bookingRequest.get().getSeller_price());
            orderRequestModel.setReason_rejected(bookingRequest.get().getReason_rejected());

            orderRequestModel.setUnit(msUnitDao.findById(bookingRequest.get().getUnit()).orElse(null));
            orderRequestModel.setWarehouse(warehouseDao.findById(bookingRequest.get().getWarehouse_id()).orElse(null));
            orderRequestModel.setBuyer(usersDao.findById(bookingRequest.get().getBuyer_id()).orElse(null));

            if(bookingRequest.get().getIs_fulfillment_space() == 1){
                orderRequestModel.setLeasing_space(null);

                orderRequestModel.setFulfillment_space(fulfillmentSpaceDao.findByBookingRequestId(bookingRequest.get().getId()).get());
            }

            if(bookingRequest.get().getIs_leasing_space() == 1){
                orderRequestModel.setFulfillment_space(null);

                orderRequestModel.setLeasing_space(leasingSpaceDao.findByBookingRequestId(bookingRequest.get().getId()).get());
            }

            //is buyer ?
            if(role_login_user.getRole_id() == Constant.Role.BUYER.getRole()){
                if(orderRequestModel.getBuyer().getId() != role_login_user.getModel_id()){
                    generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                    generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                    generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                    return generalResponse;
                }
            }

            WarehouseOwner warehouseOwner = new WarehouseOwner();
            Optional<Users> owner = usersDao.findById(orderRequestModel.getWarehouse().getUser_id());
            if (owner.isPresent()) {
                warehouseOwner.setId(owner.get().getId());
                warehouseOwner.setName(owner.get().getFirst_name() + " " + owner.get().getLast_name());

                orderRequestModel.setWarehouse_owner(warehouseOwner);
            }

            //is seller ?
            if(role_login_user.getRole_id() == Constant.Role.SELLER.getRole()){
                if(orderRequestModel.getWarehouse_owner().getId() != role_login_user.getModel_id()){
                    generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                    generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                    generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                    return generalResponse;
                }
            }

            //is sub seller ?
            if(role_login_user.getRole_id() == Constant.Role.SUB_SELLER.getRole()){
                if(orderRequestModel.getWarehouse_owner().getId() != role_login_user.getSuper_user()){
                    generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                    generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                    generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                    return generalResponse;
                }
            }

            generalResponse.setData(orderRequestModel);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        } else {
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return generalResponse;
    }

    @Override
    @Deprecated
    public NotificationResponse getNotificationBooking(ModelHasRole role_login_user) {
        NotificationResponse notificationResponse = new NotificationResponse();

        if (role_login_user.getRole_id() == Constant.Role.ADMIN.getRole() ||
                role_login_user.getRole_id() == Constant.Role.SUPER_ADMIN.getRole()) {
//            notificationResponse.setNotifications(notificationService.getNotification(0, Constant.NotificationType.BOOKING.getType()));
            notificationResponse.setTotal_notification(bookingRequestDao.getCountBookingRequestByStatusAdmin(Constant.StatusBooking.OPEN.getStatus()));
        }

        if (role_login_user.getRole_id() == Constant.Role.SELLER.getRole()) {
//            notificationResponse.setNotifications(notificationService.getNotification(role_login_user.getModel_id(), Constant.NotificationType.BOOKING.getType()));
            notificationResponse.setTotal_notification(bookingRequestDao.getCountBookingRequestByStatusSeller(Constant.StatusBooking.SELLER_REVIEW.getStatus(),
                    role_login_user.getModel_id()));
        }

        if (role_login_user.getRole_id() == Constant.Role.SUB_SELLER.getRole()) {
//            notificationResponse.setNotifications(notificationService.getNotification(role_login_user.getSuper_user(), Constant.NotificationType.BOOKING.getType()));
            notificationResponse.setTotal_notification(bookingRequestDao.getCountBookingRequestByStatusSeller(Constant.StatusBooking.SELLER_REVIEW.getStatus(),
                    role_login_user.getSuper_user()));
        }

        if (role_login_user.getRole_id() == Constant.Role.BUYER.getRole()) {
//            notificationResponse.setNotifications(notificationService.getNotification(role_login_user.getModel_id(), Constant.NotificationType.BOOKING.getType()));
            notificationResponse.setTotal_notification(bookingRequestDao.getCountBookingRequestByStatusBuyer(role_login_user.getModel_id()));
        }

        return notificationResponse;
    }

    @Override
    @Transactional
    public String updateStatus(OrderRequestModel orderRequestModel, ModelHasRole role_login_user, int action) {
        if (!Constant.getActionChangeStatusBooking().contains(action))
            return Constant.Code.ACTION_INVALID.getStatusCode();

        Optional<BookingRequest> isBookingRequest = bookingRequestDao.findById(orderRequestModel.getId());

        if (!isBookingRequest.isPresent())
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();

        BookingRequest bookingRequest = isBookingRequest.get();
        Map<String, String> email = new HashMap<>();
        Map<String, String> message = new HashMap<>();
        Notification notification = new Notification();

        if (role_login_user.getRole_id() == Constant.Role.SUPER_ADMIN.getRole() ||
                role_login_user.getRole_id() == Constant.Role.ADMIN.getRole()) {

            if (action == Constant.StatusBooking.ADMIN_APPROVED_REVIEW.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.OPEN.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.SELLER_REVIEW.getStatus());
                bookingRequest.setAdmin_id(role_login_user.getModel_id());

                Optional<Users> owner = usersDao.findById(warehouseDao.findById(bookingRequest.getWarehouse_id()).get().getUser_id());
                String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_APPROVED_ADMIN);
                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "New Booking Warehouse");
                email.put("to", owner.get().getEmail());
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", owner.get().getPhone());
                message.put("content", wordingMessage.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                notification.setUser_id(owner.get().getId());
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Booking " + bookingRequest.getKode_booking() + " menunggu diproses");
                notification.setEn_message("Booking " + bookingRequest.getKode_booking() + " waiting processed");
                notification.setCreated_at(new Date());

            } else if (action == Constant.StatusBooking.ADMIN_REJECTED.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.OPEN.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.ADMIN_REJECTED.getStatus());
                bookingRequest.setReason_rejected(orderRequestModel.getReason_rejected());
                bookingRequest.setAdmin_id(role_login_user.getModel_id());

                String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_REJECTED_ADMIN);
                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "Booking Warehouse Rejected");
                email.put("to", bookingRequest.getEmail());
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", bookingRequest.getPhone_number());
                message.put("content", wordingMessage.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                notification.setUser_id(bookingRequest.getBuyer_id());
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Kode booking Anda " + bookingRequest.getKode_booking() + " telah ditolak");
                notification.setEn_message("Your booking code " + bookingRequest.getKode_booking() + " has been rejected");
                notification.setCreated_at(new Date());
            } else if (action == Constant.StatusBooking.ADMIN_APPROVED.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.SELLER_APPROVED.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.ADMIN_APPROVED.getStatus());
                bookingRequest.setMospaze_price(orderRequestModel.getMospaze_price());
                bookingRequest.setAdmin_id(role_login_user.getModel_id());

                String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_APPROVED_SELLER);
                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "Your Booking Already Approved");
                email.put("to", bookingRequest.getEmail());
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", bookingRequest.getPhone_number());
                message.put("content", wordingMessage.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                notification.setUser_id(bookingRequest.getBuyer_id());
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Kode booking Anda " + bookingRequest.getKode_booking() + " telah disetujui");
                notification.setEn_message("Your booking code " + bookingRequest.getKode_booking() + " has been approved");
                notification.setCreated_at(new Date());
            } else if (action == Constant.StatusBooking.ADMIN_REJECTED.getStatus() &&
                    (bookingRequest.getStatus() == Constant.StatusBooking.SELLER_APPROVED.getStatus() ||
                            bookingRequest.getStatus() == Constant.StatusBooking.SELLER_REJECTED.getStatus())) {
                bookingRequest.setStatus(Constant.StatusBooking.ADMIN_REJECTED.getStatus());
                bookingRequest.setReason_rejected(orderRequestModel.getReason_rejected());
                bookingRequest.setAdmin_id(role_login_user.getModel_id());

                String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_REJECTED_ADMIN);
                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "Booking Warehouse Rejected");
                email.put("to", bookingRequest.getEmail());
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", bookingRequest.getPhone_number());
                message.put("content", wordingMessage.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                notification.setUser_id(bookingRequest.getBuyer_id());
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Kode booking Anda " + bookingRequest.getKode_booking() + " telah ditolak");
                notification.setEn_message("Your booking code " + bookingRequest.getKode_booking() + " has been rejected");
                notification.setCreated_at(new Date());
            } else {
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
            }

        }

        if (role_login_user.getRole_id() == Constant.Role.SELLER.getRole() ||
                role_login_user.getRole_id() == Constant.Role.SUB_SELLER.getRole()) {

            if (action == Constant.StatusBooking.SELLER_APPROVED.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.SELLER_REVIEW.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.SELLER_APPROVED.getStatus());
                bookingRequest.setSeller_price(orderRequestModel.getSeller_price());

                /*String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_APPROVED_SELLER);
                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "Your Booking Already Approved");
                email.put("to", bookingRequest.getEmail());
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", bookingRequest.getPhone_number());
                message.put("content", wordingMessage.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));*/

                notification.setUser_id(0);
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Kode booking " + bookingRequest.getKode_booking() + " telah disetujui seller");
                notification.setEn_message("Booking code " + bookingRequest.getKode_booking() + " has been approved by seller");
                notification.setCreated_at(new Date());
            } else if (action == Constant.StatusBooking.SELLER_REJECTED.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.SELLER_REVIEW.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.SELLER_REJECTED.getStatus());
                bookingRequest.setReason_rejected(orderRequestModel.getReason_rejected());

                /*String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_REJECTED_SELLER);
                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "Booking Warehouse Rejected");
                email.put("to", bookingRequest.getEmail());
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", bookingRequest.getPhone_number());
                message.put("content", wordingMessage.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));*/

                notification.setUser_id(0);
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Kode booking " + bookingRequest.getKode_booking() + " telah ditolak seller");
                notification.setEn_message("Booking code " + bookingRequest.getKode_booking() + " has been rejected by seller");
                notification.setCreated_at(new Date());
            } else {
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
            }

        }

        if (role_login_user.getRole_id() == Constant.Role.BUYER.getRole()) {

            if (action == Constant.StatusBooking.QUOTATION_REQUESTED.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.ADMIN_APPROVED.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.QUOTATION_REQUESTED.getStatus());
                bookingRequest.setQuotation_date(new Date());

                if(bookingRequest.getIs_fulfillment_space() == 1)
                    bookingRequest.setQuotation_no(util.generateQuotationNumber(Constant.QuotationCode.SH.getCode()));
                else
                    bookingRequest.setQuotation_no(util.generateQuotationNumber(Constant.QuotationCode.SO.getCode()));

//                Optional<Users> owner = usersDao.findById(warehouseDao.findById(bookingRequest.getWarehouse_id()).get().getUser_id());
                String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_QUOTATION_CREATE_BUYER);
//                String wordingMessage = Constant.configProperties.get(Constant.SMS_WORDING_BOOKING_WAREHOUSE);

                email.put("subject", "Booking Warehouse Quotation Requested");
                email.put("to", String.valueOf(Constant.configProperties.get(Constant.EMAIL_ENQUIRY_MOSPAZE)));
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", String.valueOf(Constant.configProperties.get(Constant.PHONE_ENQUIRY_MOSPAZE)).startsWith("0") ? String.valueOf(Constant.configProperties.get(Constant.PHONE_ENQUIRY_MOSPAZE)).replaceFirst("0", "62") : (String) Constant.configProperties.get(Constant.PHONE_ENQUIRY_MOSPAZE));
                message.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                notification.setUser_id(0);
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Buyer request quotation untuk kode booking " + bookingRequest.getKode_booking());
                notification.setEn_message("Buyer request quotation for booking code " + bookingRequest.getKode_booking());
                notification.setCreated_at(new Date());

            } else if (action == Constant.StatusBooking.BUYER_REJECTED.getStatus() &&
                    bookingRequest.getStatus() == Constant.StatusBooking.ADMIN_APPROVED.getStatus()) {
                bookingRequest.setStatus(Constant.StatusBooking.BUYER_REJECTED.getStatus());
                bookingRequest.setReason_rejected(orderRequestModel.getReason_rejected());

                String wordingEmail = Constant.configProperties.get(Constant.EMAIL_WORDING_REJECTED_BUYER);

                email.put("subject", "Booking Warehouse Rejected");
                email.put("to", String.valueOf(Constant.configProperties.get(Constant.EMAIL_ENQUIRY_MOSPAZE)));
                email.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                message.put("brand", "Mospaze");
                message.put("to", String.valueOf(Constant.configProperties.get(Constant.PHONE_ENQUIRY_MOSPAZE)).startsWith("0") ? String.valueOf(Constant.configProperties.get(Constant.PHONE_ENQUIRY_MOSPAZE)).replaceFirst("0", "62") : (String) Constant.configProperties.get(Constant.PHONE_ENQUIRY_MOSPAZE));
                message.put("content", wordingEmail.replace("[url]", url_booking + "/" + isBookingRequest.get().getId()));

                notification.setUser_id(0);
                notification.setType(Constant.NotificationType.BOOKING.getType());
                notification.setModel_id(bookingRequest.getId());
                notification.setId_message("Buyer menolak penawaran untuk kode booking " + bookingRequest.getKode_booking());
                notification.setEn_message("Buyer was rejected the offer for booking code " + bookingRequest.getKode_booking());
                notification.setCreated_at(new Date());
            } else {
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
            }

        }

        bookingRequestDao.save(bookingRequest);

        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("SEND EMAIL : {}", email);
                util.sendEmailBookingStatus(email);

                logger.info("SEND SMS : {}", message);
                util.sendSMS(message);

                notificationService.insertNotification(notification);
            }
        }).start();

        return Constant.Code.SUCCESS.getStatusCode();
    }

    private com.mospaze.werehouse.entity.BookingRequest insertBooking(BookingRequestModel bookingRequestModel, String username) {
        com.mospaze.werehouse.entity.BookingRequest bookingRequest = new com.mospaze.werehouse.entity.BookingRequest();
        bookingRequest.setKode_booking(bookingRequestModel.getKode_booking());
        Optional<Users> users = usersDao.getUserByPhone(username);
        bookingRequest.setBuyer_id(users.get().getId());
        bookingRequest.setWarehouse_id(bookingRequestModel.getWarehouse_id());
        bookingRequest.setPic_name(bookingRequestModel.getName());
        bookingRequest.setEmail(bookingRequestModel.getEmail());
        bookingRequest.setPhone_number(bookingRequestModel.getPhone_number());
        bookingRequest.setCapacity(bookingRequestModel.getCapacity());
        bookingRequest.setUnit(bookingRequestModel.getUnit());
        bookingRequest.setCategory(bookingRequestModel.getCategory().stream().map(String::valueOf).collect(Collectors.joining(",")));
        bookingRequest.setStatus(Constant.StatusBooking.OPEN.getStatus());
        bookingRequest.setCreated_at(new Date());
        bookingRequest.setStart_date(bookingRequestModel.getStart_date());
        bookingRequest.setEnd_date(bookingRequestModel.getEnd_date());

        if (bookingRequestModel.getFulfillment_space() != null)
            bookingRequest.setIs_fulfillment_space(1);
        if (bookingRequestModel.getLeasing_space() != null)
            bookingRequest.setIs_leasing_space(1);

        return bookingRequestDao.save(bookingRequest);
    }

    private LeasingSpace insertLeasingSpace(BookingRequestModel bookingRequestModel, com.mospaze.werehouse.entity.BookingRequest bookingRequest) {
        LeasingSpace leasingSpace = new LeasingSpace();
        leasingSpace.setBooking_request_id(bookingRequest.getId());
        leasingSpace.setForklift_rent(bookingRequestModel.getLeasing_space().getForklift_rent());
        leasingSpace.setPallet_rent(bookingRequestModel.getLeasing_space().getPallet_rent());
        leasingSpace.setOther(bookingRequestModel.getLeasing_space().getOther());

        return leasingSpaceDao.save(leasingSpace);
    }

    private FulfillmentSpace insertFulfillmentSpace(BookingRequestModel bookingRequestModel, com.mospaze.werehouse.entity.BookingRequest bookingRequest) {
        FulfillmentSpace fulfillmentSpace = new FulfillmentSpace();
        fulfillmentSpace.setBooking_request_id(bookingRequest.getId());
        fulfillmentSpace.setCross_docking(bookingRequestModel.getFulfillment_space().getCross_docking());
        fulfillmentSpace.setCustom_labeling(bookingRequestModel.getFulfillment_space().getCustom_labeling());
        fulfillmentSpace.setMerger_in_transit(bookingRequestModel.getFulfillment_space().getMerger_in_transit());
        fulfillmentSpace.setOther(bookingRequestModel.getFulfillment_space().getOther());
        fulfillmentSpace.setProduct_kiting(bookingRequestModel.getFulfillment_space().getProduct_kiting());
        fulfillmentSpace.setRe_packing(bookingRequestModel.getFulfillment_space().getRe_packing());
        fulfillmentSpace.setReturn_logistics(bookingRequestModel.getFulfillment_space().getReturn_logistics());
        fulfillmentSpace.setShrink_wrapping(bookingRequestModel.getFulfillment_space().getShrink_wrapping());

        fulfillmentSpace.setInbound_service(bookingRequestModel.getFulfillment_space().getInbound_service());
        fulfillmentSpace.setOutbound_service(bookingRequestModel.getFulfillment_space().getOutbound_service());

        fulfillmentSpace.setInbound_service_value(bookingRequestModel.getFulfillment_space().getInbound_service_value());
        fulfillmentSpace.setOutbound_service_value(bookingRequestModel.getFulfillment_space().getOutbound_service_value());

        fulfillmentSpace.setUnit_inbound(bookingRequestModel.getFulfillment_space().getUnit_inbound());
        fulfillmentSpace.setUnit_outbound(bookingRequestModel.getFulfillment_space().getUnit_outbound());

        /*fulfillmentSpace.setWeight_inbound(bookingRequestModel.getFulfillment_space().getWeight_inbound());
        fulfillmentSpace.setWeight_outbound(bookingRequestModel.getFulfillment_space().getWeight_outbound());*/

        return fulfillmentSpaceDao.save(fulfillmentSpace);
    }
}
