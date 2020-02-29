package com.mospaze.werehouse.config;

import com.google.gson.Gson;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.repository.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Iman Khaeruddin
 * @since 27/11/2019
 */
@Configuration
public class CustomFilter {

    @Component
    public class DataTracker extends OncePerRequestFilter implements Filter {

        private Logger logger = LoggerFactory.getLogger(DataTracker.class);

        @Autowired
        private DataTrackerDao dataTrackerDao;

        @Autowired
        private MsCitiesDao msCitiesDao;

        @Autowired
        private MsDistrictsDao msDistrictsDao;

        @Autowired
        private MsWarehouseFeatureDao msWarehouseFeatureDao;

        @Autowired
        private MsWarehouseServiceDao msWarehouseServiceDao;

        @Autowired
        private MsWarehouseCategoryDao msWarehouseCategoryDao;

        @Autowired
        private MsWarehouseAutomationDao msWarehouseAutomationDao;

        @Autowired
        private MsWarehouseTypeDao msWarehouseTypeDao;

        @Autowired
        private MsWarehouseTemperatureDao msWarehouseTemperatureDao;

        @Autowired
        private MsWarehouseSpaceDao msWarehouseSpaceDao;

        @Autowired
        private MsWarehouseSpaceServiceDao msWarehouseSpaceServiceDao;

        @Autowired
        private Gson gson;

        @Autowired
        private Util util;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            ResettableStreamHttpServletRequest wrappedRequest = null;
            HttpServletResponseCopier responseCopier = null;

            try{

                com.mospaze.werehouse.entity.DataTracker dataTracker = new com.mospaze.werehouse.entity.DataTracker();

                //get request param
                Map<String, String> paramsMap =
                        (Map<String, String>)
                                Collections.list(request.getParameterNames())
                                        .stream()
                                        .collect(Collectors.toMap(
                                                name -> name,
                                                request::getParameter));
                String request_param = gson.toJson(paramsMap);

                //get request header
                Map<String, String> headersMap =
                        (Map<String, String>)
                                Collections.list(request.getHeaderNames())
                                        .stream()
                                        .collect(Collectors.toMap(
                                                name -> name,
                                                request::getHeader));
                String request_header = gson.toJson(headersMap);

                //wrapper request
                wrappedRequest = new ResettableStreamHttpServletRequest((HttpServletRequest) request);

                String request_body = IOUtils.toString(wrappedRequest.getReader());

                logger.info("request_uri : {}", request.getRequestURI());
                logger.info("request_method : {}", request.getMethod());
                logger.info("request_header : {}", request_header);
                logger.info("request_param : {}", request_param);
                logger.info("request_body : {}", request_body);

                wrappedRequest.resetInputStream();

                if (request.getRequestURI().equals(RestConstant.INDEX_WAREHOUSE.concat(RestConstant.GET_ALL_BY_FILTER)) &&
                        !request.getMethod().equals("OPTIONS")) {
                    dataTracker.setEnd_point(request.getRequestURI());
                    dataTracker.setMethod_name(request.getMethod());
                    dataTracker.setRequest_param(request_param);

                    if (request_body != null && !request_body.isEmpty()) {
                        dataTracker.setRequest_body(request_body);
                    }

                    dataTracker.setRequest_header(request_header);
                    dataTracker.setUser_agent(headersMap.get("user-agent"));
                    dataTracker.setBrowser(headersMap.get("browser"));
                    dataTracker.setOs(headersMap.get("os"));
                    dataTracker.setIp_address(headersMap.get("ip-address"));
                    dataTracker.setRequest_time(new Date());

                    if(headersMap.get("session") != null)
                        dataTracker.setUsername(util.parseJWTToken(headersMap.get("session")).getUsername());

                    Enumeration<String> parameterNames = request.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String str = parameterNames.nextElement();

                        if (str.equals("size_min"))
                            dataTracker.setSize_min(request.getParameter(str));

                        if (str.equals("size_max"))
                            dataTracker.setSize_max(request.getParameter(str));

                        if (str.equals("unit"))
                            dataTracker.setUnit(request.getParameter(str));

                        if (str.equals("is_racking"))
                            dataTracker.setIs_racking(Integer.parseInt(request.getParameter(str)));

                        if (str.equals("cities")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsCities> msCities = msCitiesDao.getCitiesNameFromId(Arrays.asList(arr));

                            if(msCities.size() > 0){
                                String city = "";
                                for(MsCities msCities1 : msCities){
                                    city = city.concat(msCities1.getName()).concat(",");
                                }

                                dataTracker.setCity(city.substring(0, city.length()-1).toUpperCase());
                            }
                        }

                        if (str.equals("district")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsDisctrict> msDisctricts = msDistrictsDao.getDistrictNameFromId(Arrays.asList(arr));

                            if(msDisctricts.size() > 0){
                                String district = "";
                                for(MsDisctrict msDisctrict : msDisctricts){
                                    district = district.concat(msDisctrict.getName()).concat(",");
                                }

                                dataTracker.setDistrict(district.substring(0, district.length()-1));
                            }
                        }

                        if (str.equals("feature")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseFeature> msWarehouseFeatures = msWarehouseFeatureDao.getWarehouseFeatureById(Arrays.asList(arr));

                            if(msWarehouseFeatures.size() > 0){
                                String feature = "";
                                for(MsWarehouseFeature msWarehouseFeature : msWarehouseFeatures){
                                    feature = feature.concat(msWarehouseFeature.getFeatures_name()).concat(",");
                                }

                                dataTracker.setFeature(feature.substring(0, feature.length()-1));
                            }
                        }

                        if (str.equals("category")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseCategory> msWarehouseCategories = msWarehouseCategoryDao.getWarehouseCategoryById(Arrays.asList(arr));

                            if(msWarehouseCategories.size() > 0){
                                String category = "";
                                for(MsWarehouseCategory msWarehouseCategory : msWarehouseCategories){
                                    category = category.concat(msWarehouseCategory.getCategory_name()).concat(",");
                                }

                                dataTracker.setCategory(category.substring(0, category.length()-1));
                            }
                        }

                        if (str.equals("space")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseSpace> msWarehouseSpaces = msWarehouseSpaceDao.getWarehouseSpaceById(Arrays.asList(arr));

                            if(msWarehouseSpaces.size() > 0){
                                String space = "";
                                for(MsWarehouseSpace msWarehouseSpace : msWarehouseSpaces){
                                    space = space.concat(msWarehouseSpace.getSpace_name()).concat(",");
                                }

                                dataTracker.setSpace(space.substring(0, space.length()-1));
                            }
                        }

                        if (str.equals("type")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseType> msWarehouseTypes = msWarehouseTypeDao.getWarehouseTypeById(Arrays.asList(arr));

                            if(msWarehouseTypes.size() > 0){
                                String type = "";
                                for(MsWarehouseType msWarehouseType : msWarehouseTypes){
                                    type = type.concat(msWarehouseType.getType_name()).concat(",");
                                }

                                dataTracker.setType(type.substring(0, type.length()-1));
                            }
                        }

                        if (str.equals("service")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseService> msWarehouseServices = msWarehouseServiceDao.getWarehouseServiceById(Arrays.asList(arr));

                            if(msWarehouseServices.size() > 0){
                                String service = "";
                                for(MsWarehouseService msWarehouseService : msWarehouseServices){
                                    service = service.concat(msWarehouseService.getServices_name()).concat(",");
                                }

                                dataTracker.setService(service.substring(0, service.length()-1));
                            }
                        }

                        if (str.equals("automation")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseAutomation> msWarehouseAutomations = msWarehouseAutomationDao.getWarehouseAutomationById(Arrays.asList(arr));

                            if(msWarehouseAutomations.size() > 0){
                                String automation = "";
                                for(MsWarehouseAutomation msWarehouseAutomation : msWarehouseAutomations){
                                    automation = automation.concat(msWarehouseAutomation.getAutomation_name()).concat(",");
                                }

                                dataTracker.setAutomation(automation.substring(0, automation.length()-1));
                            }
                        }

                        if (str.equals("temperature")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseTemperature> msWarehouseTemperatures = msWarehouseTemperatureDao.getWarehouseTemperatureById(Arrays.asList(arr));

                            if(msWarehouseTemperatures.size() > 0){
                                String temperature = "";
                                for(MsWarehouseTemperature msWarehouseTemperature : msWarehouseTemperatures){
                                    temperature = temperature.concat(msWarehouseTemperature.getTemperature_name()).concat(",");
                                }

                                dataTracker.setTemperature(temperature.substring(0, temperature.length()-1));
                            }
                        }

                        if (str.equals("space_service")){
                            String [] a = request.getParameter(str).split(",");
                            Integer arr [] = Arrays.stream(Arrays.asList(a).stream().mapToInt(Integer::parseInt).toArray()).boxed().toArray( Integer[]::new );

                            List<MsWarehouseSpaceService> msWarehouseSpaceServices = msWarehouseSpaceServiceDao.getWarehouseSpaceServiceById(Arrays.asList(arr));

                            if(msWarehouseSpaceServices.size() > 0){
                                String space_service = "";
                                for(MsWarehouseSpaceService msWarehouseSpaceService : msWarehouseSpaceServices){
                                    space_service = space_service.concat(msWarehouseSpaceService.getSpace_service_name()).concat(",");
                                }

                                dataTracker.setSpace_service(space_service.substring(0, space_service.length()-1));
                            }
                        }
                    }

                    dataTracker = dataTrackerDao.save(dataTracker);
                }

                //wrapper response
                responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

                filterChain.doFilter(wrappedRequest, responseCopier);

                responseCopier.flushBuffer();

                byte[] copy = responseCopier.getCopy();

                //get response header
                Map<String, String> response_header_map = new HashMap<>();
                /*Map<String, String> response_header_map =
                        (Map<String, String>)
                                responseCopier.getHeaderNames()
                                        .stream()
                                        .collect(Collectors.toMap(
                                                name -> name,
                                                responseCopier::getHeader));*/

                for(String key : responseCopier.getHeaderNames()){
                        response_header_map.put(key, responseCopier.getHeader(key));
                }

                String response_header = gson.toJson(response_header_map);
                String response_body = new String(copy, response.getCharacterEncoding());

                if (request.getRequestURI().equals(RestConstant.INDEX_WAREHOUSE.concat(RestConstant.GET_ALL_BY_FILTER)) &&
                        !request.getMethod().equals("OPTIONS")) {
                    dataTracker.setResponse_header(response_header);
                    dataTracker.setResponse_body(response_body);
                    dataTracker.setResponse_time(new Date());
                    dataTrackerDao.save(dataTracker);
                }

                logger.info("response_header : {}", response_header);
                logger.info("response_body : {}", response_body);
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                filterChain.doFilter(request, response);
            }

        }

    }

    private static class ResettableStreamHttpServletRequest extends
            HttpServletRequestWrapper {

        private byte[] rawData;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }


        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return new BufferedReader(new InputStreamReader(servletStream));
        }


        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        }
    }

    private class HttpServletResponseCopier extends HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter writer;
        private ServletOutputStreamCopier copier;

        public HttpServletResponseCopier(HttpServletResponse response) throws IOException {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("getWriter() has already been called on this response.");
            }

            if (outputStream == null) {
                outputStream = getResponse().getOutputStream();
                copier = new ServletOutputStreamCopier(outputStream);
            }

            return copier;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (outputStream != null) {
                throw new IllegalStateException("getOutputStream() has already been called on this response.");
            }

            if (writer == null) {
                copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
            }

            return writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            } else if (outputStream != null) {
                copier.flush();
            }
        }

        public byte[] getCopy() {
            if (copier != null) {
                return copier.getCopy();
            } else {
                return new byte[0];
            }
        }

        private class ServletOutputStreamCopier extends ServletOutputStream {

            private OutputStream outputStream;
            private ByteArrayOutputStream copy;

            public ServletOutputStreamCopier(OutputStream outputStream) {
                this.outputStream = outputStream;
                this.copy = new ByteArrayOutputStream(1024);
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
                copy.write(b);
            }

            public byte[] getCopy() {
                return copy.toByteArray();
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }
        }

    }


}
