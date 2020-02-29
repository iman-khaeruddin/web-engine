package com.mospaze.werehouse.common;

import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.entity.projector.WarehouseFeature;
import com.mospaze.werehouse.model.AdditionalService;
import com.mospaze.werehouse.model.DataQuotation;
import com.mospaze.werehouse.model.HandlingService;
import com.mospaze.werehouse.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

/**
 * @author Iman Khaeruddin
 * @since 26/11/2019
 */
@Component
public class GeneratePDFFile {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String UTF_8 = "UTF-8";

    @Autowired
    private MsWarehouseFeatureDao msWarehouseFeatureDao;

    @Autowired
    private BookingRequestDao bookingRequestDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private FulfillmentSpaceDao fulfillmentSpaceDao;

    @Autowired
    private LeasingSpaceDao leasingSpaceDao;

    @Autowired
    private S3Client s3Client;

    public String generateQuotation(int id) throws Exception {

        Optional<BookingRequest> bookingRequests = bookingRequestDao.findById(id);

        if(!bookingRequests.isPresent()) {
            throw new Exception(Constant.getEnMessage(Constant.Code.DATA_NOT_FOUND.getStatusCode()));
        }

        BookingRequest bookingRequest = bookingRequests.get();

        if(bookingRequest.getStatus() != Constant.StatusBooking.QUOTATION_REQUESTED.getStatus())
            throw new Exception(Constant.getEnMessage(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode()));

        //download the images from s3
        s3Client.downloadImageWarehouse(bookingRequest.getWarehouse_id());

        // The data in our Thymeleaf templates is not hard-coded. Instead,
        // we use placeholders in our templates. We fill these placeholders
        // with actual data by passing in an object. In this example, we will
        // write a letter to "John Doe".
        //
        // Note that we could also read this data from a JSON file, a database
        // a web service or whatever.
        DataQuotation data = new DataQuotation();
        if(bookingRequest.getIs_fulfillment_space() == 1)
            data = getDataQuotation(bookingRequest);
        else
            data = getDataQuotation(bookingRequest);

        /*data = getDataQuotation();*/

        String file_name = "Quotation_"+data.getBooking_no().replace("/","_")+".pdf";

        // We set-up a Thymeleaf rendering engine. All Thymeleaf templates
        // are HTML-based files located under "src/test/resources". Beside
        // of the main HTML file, we also have partials like a footer or
        // a header. We can re-use those partials in different documents.
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("data", data);

        // Flying Saucer needs XHTML - not just normal HTML. To make our life
        // easy, we use JTidy to convert the rendered Thymeleaf template to
        // XHTML. Note that this might not work for very complicated HTML. But
        // it's good enough for a simple letter.
        String renderedHtmlContent = "";
        if(bookingRequest.getIs_fulfillment_space() == 1)
            renderedHtmlContent = templateEngine.process("template_fulfillment", context);
        else
            renderedHtmlContent = templateEngine.process("template_leasing_space", context);

        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();

        // FlyingSaucer has a working directory. If you run this test, the working directory
        // will be the root folder of your project. However, all files (HTML, CSS, etc.) are
        // located under "/src/test/resources". So we want to use this folder as the working
        // directory.
        String baseUrl = FileSystems
                .getDefault()
                .getPath("src", "main", "resources")
                .toUri()
                .toURL()
                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();

        // And finally, we create the PDF:
        OutputStream outputStream = new FileOutputStream("/var/www/core/"+file_name);
        renderer.createPDF(outputStream);
        outputStream.close();

        return file_name;
    }

    private DataQuotation getDataQuotation(BookingRequest bookingRequest) throws ParseException {
        DataQuotation data = new DataQuotation();

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        logger.info("date : " +  bookingRequest.getQuotation_date().toString());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Constant.SIMPLE_DATE_FORMAT_4.parse(bookingRequest.getQuotation_date().toString()));
        calendar.add(Calendar.DATE, 1);

        logger.info("date : " + calendar.getTime());

        data.setDate(Constant.SIMPLE_DATE_FORMAT_3.format(calendar.getTime()));
        data.setBooking_no(bookingRequest.getQuotation_no());

        //buyer
        Optional<Users> users = usersDao.findById(bookingRequest.getBuyer_id());
        data.setBuyer_company_name(users.get().getCompany());
        data.setBuyer_pic_email(users.get().getEmail());
        data.setBuyer_pic_name(users.get().getFirst_name().concat(" ").concat(users.get().getLast_name()));
        data.setBuyer_pic_phone(users.get().getPhone());

        //admin
        users = usersDao.findById(bookingRequest.getAdmin_id());
        data.setAdmin_name(users.get().getFirst_name().concat(" ").concat(users.get().getLast_name()));
        data.setAdmin_position(users.get().getJob_tittle());

        //warehouse
        Optional<Warehouse> warehouse = warehouseDao.findById(bookingRequest.getWarehouse_id());
        data.setWarehouse_id(String.valueOf(warehouse.get().getName()));
        data.setWarehouse_location_area(warehouse.get().getDistrict() + ", " +warehouse.get().getCity() + ", " + warehouse.get().getState() + ", " + warehouse.get().getCountry());
        data.setWarehouse_service_type(bookingRequest.getIs_fulfillment_space() == 1 ? "Space & Handling" : "Space Only");
        data.setWarehouse_available_space(bookingRequest.getUnit() == 1 ?
                String.valueOf(warehouse.get().getTotal_rack_available()) :
                String.valueOf(warehouse.get().getTotal_stack_available()));

        data.setWarehouse_slug(warehouse.get().getSlug());

        calendar = Calendar.getInstance();
        calendar.setTime(Constant.SIMPLE_DATE_FORMAT_4.parse(bookingRequest.getStart_date().toString()));
        calendar.add(Calendar.DATE, 1);
        data.setStart_date(Constant.SIMPLE_DATE_FORMAT_3.format(calendar.getTime()));

        calendar = Calendar.getInstance();
        calendar.setTime(Constant.SIMPLE_DATE_FORMAT_4.parse(bookingRequest.getEnd_date().toString()));
        calendar.add(Calendar.DATE, 1);
        data.setEnd_date(Constant.SIMPLE_DATE_FORMAT_3.format(calendar.getTime()));

        //storage service
        data.setWarehouse_capacity(String.valueOf(bookingRequest.getCapacity()));
        data.setWarehouse_uom(bookingRequest.getUnit()==1 ? "Pallet" : "Sqm");
        data.setWarehouse_storage_service(data.getWarehouse_uom().equalsIgnoreCase("Pallet") ? "Racking storage" : "Floor stack");
        data.setWarehouse_price(kursIndonesia.format(bookingRequest.getMospaze_price()));
        data.setWarehouse_remark("");

        AdditionalService additionalService = new AdditionalService();
        List<AdditionalService> additionalServices = new ArrayList<>();

        if(bookingRequest.getIs_fulfillment_space() == 1){          //fulfillment
            HandlingService handlingService = new HandlingService();
            List <HandlingService> handlingServices = new ArrayList<>();

            Optional<FulfillmentSpace> fulfillmentSpace = fulfillmentSpaceDao.findByBookingRequestId(bookingRequest.getId());
            handlingService.setService_type("inbound");
            handlingService.setProduct_type(bookingRequest.getCategory());
            handlingService.setQty(String.valueOf(fulfillmentSpace.get().getInbound_service_value()));
            handlingService.setUom(fulfillmentSpace.get().getUnit_inbound());
            handlingService.setPrice(kursIndonesia.format(2000));
            handlingServices.add(handlingService);

            handlingService = new HandlingService();
            handlingService.setService_type("outbound");
            handlingService.setProduct_type(bookingRequest.getCategory());
            handlingService.setQty(String.valueOf(fulfillmentSpace.get().getOutbound_service_value()));
            handlingService.setUom(fulfillmentSpace.get().getUnit_outbound());
            handlingService.setPrice(kursIndonesia.format(2000));
            handlingServices.add(handlingService);

            data.setHandling_service(handlingServices);

            if(fulfillmentSpace.get().getRe_packing() == 1){
                additionalService.setService_name("Re-Packing");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getCustom_labeling() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Custom Labeling");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getProduct_kiting() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Product Kitting");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getReturn_logistics() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Return Logistics");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getShrink_wrapping() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Shrink Wrapping");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getCross_docking() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Cross Docking");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getMerger_in_transit() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Merger in Transit");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(fulfillmentSpace.get().getOther() != null && !fulfillmentSpace.get().getOther().isEmpty()){
                additionalService = new AdditionalService();
                additionalService.setService_name(fulfillmentSpace.get().getOther());
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }
        } else {            //leasing space
            Optional<LeasingSpace> leasingSpaces = leasingSpaceDao.findByBookingRequestId(bookingRequest.getId());

            if(leasingSpaces.get().getForklift_rent() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Forklift Rent");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(leasingSpaces.get().getPallet_rent() == 1){
                additionalService = new AdditionalService();
                additionalService.setService_name("Pallet Rent");
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }

            if(leasingSpaces.get().getOther() != null && !leasingSpaces.get().getOther().isEmpty()){
                additionalService = new AdditionalService();
                additionalService.setService_name(leasingSpaces.get().getOther());
                additionalService.setPrice(kursIndonesia.format(2000));
                additionalService.setUom("Kg");
                additionalServices.add(additionalService);
            }
        }

        data.setAdditional_service(additionalServices);

        List<WarehouseFeature> warehouseFeatures = msWarehouseFeatureDao.getMsWarehouseFeaturesById(bookingRequest.getWarehouse_id());
        data.setWarehouse_facilities(warehouseFeatures);

        return data;
    }

    private static DataQuotation getDataQuotation() {
        DataQuotation data = new DataQuotation();

        data.setDate("12/12/2019");
        data.setBooking_no("MC/MISA/0001/SH/12/2019");

        data.setBuyer_company_name("PT. Jarvis");
        data.setBuyer_pic_email("test@jarvis.com");
        data.setBuyer_pic_name("Tony Stark");
        data.setBuyer_pic_phone("081234567890");

        data.setAdmin_name("Jimmy Napoleon");
        data.setAdmin_position("Sales");

        data.setWarehouse_id("123");
        data.setWarehouse_location_area("Jakarta, Indonesia");
        data.setWarehouse_service_type("Space & Handling");
        data.setWarehouse_available_space("19000");

        data.setStart_date("12/12/2019");
        data.setEnd_date("30/12/2019");

        data.setWarehouse_capacity("123");
        data.setWarehouse_uom("Kg");
        data.setWarehouse_price("3000");
        data.setWarehouse_remark("");

        HandlingService handlingService = new HandlingService();
        List <HandlingService> handlingServices = new ArrayList<>();

        handlingService.setService_type("inbound");
        handlingService.setProduct_type("FMCG");
        handlingService.setQty("123");
        handlingService.setUom("Kg");
        handlingService.setPrice("2000");
        handlingServices.add(handlingService);

        handlingService = new HandlingService();
        handlingService.setService_type("outbound");
        handlingService.setProduct_type("FMCG");
        handlingService.setQty("123");
        handlingService.setUom("Kg");
        handlingService.setPrice("6000");
        handlingServices.add(handlingService);

        handlingService = new HandlingService();
        handlingService.setService_type("outbound");
        handlingService.setProduct_type("Electronic");
        handlingService.setQty("123");
        handlingService.setUom("Kg");
        handlingService.setPrice("6000");
        handlingServices.add(handlingService);

        handlingService = new HandlingService();
        handlingService.setService_type("outbound");
        handlingService.setProduct_type("Electronic");
        handlingService.setQty("123");
        handlingService.setUom("Kg");
        handlingService.setPrice("6000");
        handlingServices.add(handlingService);

        data.setHandling_service(handlingServices);

        AdditionalService additionalService = new AdditionalService();
        List<AdditionalService> additionalServices = new ArrayList<>();

        additionalService.setService_name("Cross docking");
        additionalService.setPrice("2000");
        additionalService.setUom("Kg");
        additionalServices.add(additionalService);

        additionalService = new AdditionalService();
        additionalService.setService_name("Repacking");
        additionalService.setPrice("2000");
        additionalService.setUom("Kg");
        additionalServices.add(additionalService);

        data.setAdditional_service(additionalServices);

        List<WarehouseFeature> warehouseFeatures = new ArrayList<>();
        WarehouseFeature warehouseFeature = new WarehouseFeature() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public String getFeatures_name() {
                return "Security Guard";
            }

            @Override
            public String getIcon() {
                return null;
            }
        };
        warehouseFeatures.add(warehouseFeature);

        warehouseFeature = new WarehouseFeature() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public String getFeatures_name() {
                return "CCTV";
            }

            @Override
            public String getIcon() {
                return null;
            }
        };
        warehouseFeatures.add(warehouseFeature);

        warehouseFeature = new WarehouseFeature() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public String getFeatures_name() {
                return "Temperature";
            }

            @Override
            public String getIcon() {
                return null;
            }
        };
        warehouseFeatures.add(warehouseFeature);

        data.setWarehouse_facilities(warehouseFeatures);

        System.out.println(data.toString());

        return data;
    }


    private static String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }
}
