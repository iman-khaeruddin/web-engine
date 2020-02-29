package com.mospaze.werehouse.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.mospaze.werehouse.entity.Media;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.repository.MediaDao;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseDao;
import com.mospaze.werehouse.service.MediaService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@Service
public class S3Client {

    private Logger logger = LoggerFactory.getLogger(S3Client.class);

    private AmazonS3 s3client;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.region}")
    private String region;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private MediaService mediaService;

    private static final int IMG_WIDTH_LARGE = 936;
    private static final int IMG_HEIGHT_LARGE = 665;

    private static final int IMG_WIDTH_MEDIUM = 800;
    private static final int IMG_HEIGHT_MEDIUM = 568;

    private static final int IMG_WIDTH_SQUARE = 600;
    private static final int IMG_HEIGHT_SQUARE = 600;

    private static final int IMG_WIDTH_LANDSCAPE = 750;
    private static final int IMG_HEIGHT_LANDSCAPE = 390;

    private static final int IMG_WIDTH_THUMBNAIL = 300;
    private static final int IMG_HEIGHT_THUMBNAIL = 300;

    private static final String CONVERSIONS_LARGE = "/conversions/large.jpg";
    private static final String CONVERSIONS_MEDIUM = "/conversions/medium.jpg";
    private static final String CONVERSIONS_SQUARE = "/conversions/square.jpg";
    private static final String CONVERSIONS_LANDSCAPE = "/conversions/landscape.jpg";
    private static final String CONVERSIONS_THUMBNAIL = "/conversions/thumbnail.jpg";

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @Transactional
    public String uploadImageWarehouse(MultipartFile[] imageFile, int id, JWT jwt, String file_name) {
        try {
            Optional<ModelHasRole> userRole = modelHasRoleDao.findById(usersDao.getUserByPhone(jwt.getUsername()).get().getId());
            Optional<Warehouse> warehouse = warehouseDao.findById(id);

            if(warehouse.isPresent()) {
                if (userRole.get().getRole_id() == Constant.Role.BUYER.getRole() ||
                        (userRole.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && warehouse.get().getUser_id() != userRole.get().getSuper_user()) ||
                        (userRole.get().getRole_id() == Constant.Role.SELLER.getRole() && warehouse.get().getUser_id() != userRole.get().getModel_id()))
                    return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
            } else {
                return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
            }

            if(imageFile.length == 0)
                return Constant.Code.NO_MEDIA_SELECTED.getStatusCode();

            for(int i=0; i<imageFile.length; i++) {
                if(imageFile[i].getSize() > 5242880)
                    return Constant.Code.MAX_SIZE_FILE_EXCEEDED.getStatusCode();

                if (!Constant.IMAGE_EXTENTION_VALIDATION_REGEX.matcher(imageFile[i].getOriginalFilename()).matches())
                    return Constant.Code.FILE_FORMAT_INVALID.getStatusCode();
            }

            for(int i=0; i<imageFile.length; i++) {
                //original file
                File file = convertMultiPartToFile(imageFile[i]);
                File output = file;

                //compress the original image if size more than 1 MB
                if(file.length() > 1048576){
                    BufferedImage image = ImageIO.read(file);

                    OutputStream out = new FileOutputStream(output);

                    ImageWriter writer =  ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(file.getName())).next();
                    ImageOutputStream ios = ImageIO.createImageOutputStream(out);
                    writer.setOutput(ios);

                    ImageWriteParam param = writer.getDefaultWriteParam();
                    if (param.canWriteCompressed()){
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(0.5f);
                    }

                    writer.write(null, new IIOImage(image, null, null), param);

                    out.close();
                    ios.close();
                    writer.dispose();
                }

                //insert media table
                Media media = new Media();
                media.setModel_id(id);
                media.setModel_type("Mospaze\\Models\\Warehouse\\Warehouse");
                media.setCollection_name("warehouse");
                media.setFile_name(imageFile[i].getOriginalFilename());
                media.setName(imageFile[i].getOriginalFilename());
                media.setMime_type(URLConnection.guessContentTypeFromName(file.getName()));
                media.setDisk("S3");
                media.setSize((int) output.length());
                media.setManipulations("[]");
                media.setCustom_properties("[]");
                media.setOrder_column(0);

                if(file_name != null)
                    if(file_name.equals(imageFile[i].getOriginalFilename()))
                        media.setOrder_column(1);

                media.setCreated_at(new Date());
                media.setUpdated_at(new Date());
                media = mediaService.save(media);

                //upload to AWS S3
                String fileName = media.getId() + "/" + imageFile[i].getOriginalFilename();
                uploadFileTos3bucket(fileName, output);

                BufferedImage originalImage = ImageIO.read(file);
                int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                //resize and upload the image
                resizeAndUpload(originalImage, media.getId(), type, output);

                file.delete();
                output.delete();
            }

        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();
            return Constant.Code.GENERAL_ERROR.getStatusCode();
        }
        return Constant.Code.SUCCESS.getStatusCode();
    }

    private void resizeAndUpload(BufferedImage originalImage, int id, int type, File file) throws IOException {

        String base_directory = "/var/www/core/" + UUID.randomUUID() + id;

        BufferedImage image_water_mark = ImageIO.read(new File("/var/www/core/img/logo.png"));
        image_water_mark = resize(image_water_mark, 100, 200);

        //resize large
        BufferedImage resizeImageJpg = resizeImageLarge(ImageIO.read(file), type, image_water_mark);
        ImageIO.write(resizeImageJpg,
                FilenameUtils.getExtension(file.getName()),
                new File(base_directory + FilenameUtils.getExtension(file.getName())));

        String fileName = id + CONVERSIONS_LARGE;
        uploadFileTos3bucket(fileName, new File(base_directory + FilenameUtils.getExtension(file.getName())));

        //resize medium
        resizeImageJpg = resizeImageMedium(ImageIO.read(file), type, image_water_mark);
        ImageIO.write(resizeImageJpg,
                FilenameUtils.getExtension(file.getName()),
                new File(base_directory + FilenameUtils.getExtension(file.getName())));

        fileName = id + CONVERSIONS_MEDIUM;
        uploadFileTos3bucket(fileName, new File(base_directory + FilenameUtils.getExtension(file.getName())));

        //resize square
        resizeImageJpg = resizeImageSquare(ImageIO.read(file), type, image_water_mark);
        ImageIO.write(resizeImageJpg,
                FilenameUtils.getExtension(file.getName()),
                new File(base_directory + FilenameUtils.getExtension(file.getName())));

        fileName = id + CONVERSIONS_SQUARE;
        uploadFileTos3bucket(fileName, new File(base_directory + FilenameUtils.getExtension(file.getName())));

        //resize landscape
        resizeImageJpg = resizeImageLandscape(ImageIO.read(file), type, image_water_mark);
        ImageIO.write(resizeImageJpg,
                FilenameUtils.getExtension(file.getName()),
                new File(base_directory + FilenameUtils.getExtension(file.getName())));

        fileName = id + CONVERSIONS_LANDSCAPE;
        uploadFileTos3bucket(fileName, new File(base_directory + FilenameUtils.getExtension(file.getName())));

        //resize thumbnail
        resizeImageJpg = resizeImageThumbnail(ImageIO.read(file), type, image_water_mark);
        ImageIO.write(resizeImageJpg,
                FilenameUtils.getExtension(file.getName()),
                new File(base_directory + FilenameUtils.getExtension(file.getName())));

        fileName = id + CONVERSIONS_THUMBNAIL;
        uploadFileTos3bucket(fileName, new File(base_directory + FilenameUtils.getExtension(file.getName())));

        new File(base_directory + FilenameUtils.getExtension(file.getName())).delete();

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private static BufferedImage resizeImageLarge(BufferedImage originalImage, int type, BufferedImage image_water_mark){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_LARGE, IMG_HEIGHT_LARGE, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_LARGE, IMG_HEIGHT_LARGE, null);

        // calculate center of the watermark
        int centerX = 364;
        int centerY = 300;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    private BufferedImage resizeImageMedium(BufferedImage originalImage, int type, BufferedImage image_water_mark) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, null);

        // calculate center of the watermark
        int centerX = 300;
        int centerY = 250;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    private BufferedImage resizeImageThumbnail(BufferedImage originalImage, int type, BufferedImage image_water_mark) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_THUMBNAIL, IMG_HEIGHT_THUMBNAIL, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_THUMBNAIL, IMG_HEIGHT_THUMBNAIL, null);

        // calculate center of the image
        int centerX = 55;
        int centerY = 100;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    private BufferedImage resizeImageLandscape(BufferedImage originalImage, int type, BufferedImage image_water_mark) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_LANDSCAPE, IMG_HEIGHT_LANDSCAPE, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_LANDSCAPE, IMG_HEIGHT_LANDSCAPE, null);

        // calculate center of the watermark
        int centerX = 270;
        int centerY = 150;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    private BufferedImage resizeImageSquare(BufferedImage originalImage, int type, BufferedImage image_water_mark) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_SQUARE, IMG_HEIGHT_SQUARE, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_SQUARE, IMG_HEIGHT_SQUARE, null);

        // calculate center of the watermark
        int centerX = 200;
        int centerY = 250;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    @Transactional
    public String deleteImageWarehouse(int id, String username) {

        Optional<Media> media = mediaDao.findById(id);

        if(media.isPresent()){
            Optional<ModelHasRole> user_role_login = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());
            Optional<Warehouse> warehouse = warehouseDao.findById(media.get().getModel_id());

            if(warehouse.isPresent()) {
                if (user_role_login.get().getRole_id() == Constant.Role.BUYER.getRole() ||
                        (user_role_login.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && warehouse.get().getUser_id() != user_role_login.get().getSuper_user()) ||
                        (user_role_login.get().getRole_id() == Constant.Role.SELLER.getRole() && warehouse.get().getUser_id() != user_role_login.get().getModel_id()))
                    return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
            } else {
                return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
            }

            //delete original file
            s3client.deleteObject(new DeleteObjectRequest(bucketName, id+"/"+media.get().getFile_name()));

            //delete conversions large
            s3client.deleteObject(new DeleteObjectRequest(bucketName, id + CONVERSIONS_LARGE));

            //delete conversions medium
            s3client.deleteObject(new DeleteObjectRequest(bucketName, id + CONVERSIONS_MEDIUM));

            //delete conversions square
            s3client.deleteObject(new DeleteObjectRequest(bucketName, id + CONVERSIONS_SQUARE));

            //delete conversions landscape
            s3client.deleteObject(new DeleteObjectRequest(bucketName, id + CONVERSIONS_LANDSCAPE));

            //delete conversions thumbnail
            s3client.deleteObject(new DeleteObjectRequest(bucketName, id + CONVERSIONS_THUMBNAIL));

            //delete media table
            mediaDao.deleteById(id);

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();
        }
    }

    public void downloadImageWarehouse(int warehouse_id) throws IOException {
        List<Media> mediaList = mediaDao.getAllMediaWarehouseUseLimit(warehouse_id, 4);

        int counter = 1;

        for (Media media: mediaList) {
            S3Object s3object = s3client.getObject(bucketName, media.getId() + CONVERSIONS_SQUARE);
            S3ObjectInputStream inputStream = s3object.getObjectContent();
            FileUtils.copyInputStreamToFile(inputStream, new File("/var/www/core/img/square"+counter+".jpg"));

            counter++;
        }
    }

}