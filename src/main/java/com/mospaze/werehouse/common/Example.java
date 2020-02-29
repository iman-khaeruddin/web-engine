package com.mospaze.werehouse.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class Example {

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

    private static final String water_mark = "MOSPAZE.COM";

    public static void main(String[] args) throws IOException {

        /*File input = new File("/Users/imankhaeruddin/Desktop/Screen Shot 2019-10-31 at 14.29.19.png");
        BufferedImage image = ImageIO.read(input);

        File output = new File("/Users/imankhaeruddin/Desktop/Screen Shot 2019-10-31 at 14.29.19.png");
        OutputStream out = new FileOutputStream(output);

        ImageWriter writer =  ImageIO.getImageWritersByFormatName("png").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()){
            System.out.println("masuk");
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.0000000000005f);
        }

        writer.write(null, new IIOImage(image, null, null), param);

        out.close();
        ios.close();
        writer.dispose();*/

        resizeImage();

    }

    public static void resizeImage() throws IOException {
        BufferedImage image_water_mark = ImageIO.read(new File("/Users/imankhaeruddin/Desktop/logo.png"));

        BufferedImage originalImage = ImageIO.read(new File("/Users/imankhaeruddin/Desktop/ocean.jpeg"));
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        image_water_mark = resize(image_water_mark, 100, 200);
        BufferedImage resizeImagePng = resizeLarge(originalImage, type, image_water_mark);
        ImageIO.write(resizeImagePng, "jpg", new File("/Users/imankhaeruddin/Desktop/large.jpg"));

        resizeImagePng = resizeMedium(originalImage, type, image_water_mark);
        ImageIO.write(resizeImagePng, "jpg", new File("/Users/imankhaeruddin/Desktop/medium.jpg"));

        resizeImagePng = resizeSquare(originalImage, type, image_water_mark);
        ImageIO.write(resizeImagePng, "jpg", new File("/Users/imankhaeruddin/Desktop/square.jpg"));

        resizeImagePng = resizeLandscape(originalImage, type, image_water_mark);
        ImageIO.write(resizeImagePng, "jpg", new File("/Users/imankhaeruddin/Desktop/landscape.jpg"));

        resizeImagePng = resizeThumbnail(originalImage, type, image_water_mark);
        ImageIO.write(resizeImagePng, "jpg", new File("/Users/imankhaeruddin/Desktop/thumbnail.jpg"));
    }

    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }


    public static BufferedImage resizeMedium(BufferedImage originalImage, int type, BufferedImage image_water_mark){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, null);

        // calculate center of the image
        int centerX = 300;
        int centerY = 250;

        System.err.println("LARGE WIDHT : " + centerX);
        System.err.println("LARGE HEIGHT : " + centerY);

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    public static BufferedImage resizeLarge(BufferedImage originalImage, int type, BufferedImage image_water_mark){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_LARGE, IMG_HEIGHT_LARGE, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_LARGE, IMG_HEIGHT_LARGE, null);

        // calculate center of the image
        int centerX = 364;
        int centerY = 300;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    public static BufferedImage resizeSquare(BufferedImage originalImage, int type, BufferedImage image_water_mark){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_SQUARE, IMG_HEIGHT_SQUARE, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_SQUARE, IMG_HEIGHT_SQUARE, null);

        // calculate center of the image
        int centerX = 200;
        int centerY = 250;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    public static BufferedImage resizeLandscape(BufferedImage originalImage, int type, BufferedImage image_water_mark){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH_LANDSCAPE, IMG_HEIGHT_LANDSCAPE, type);
        Graphics2D g = (Graphics2D) resizedImage.getGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH_LANDSCAPE, IMG_HEIGHT_LANDSCAPE, null);

        // calculate center of the image
        int centerX = 270;
        int centerY = 150;

        g.drawImage(image_water_mark, centerX, centerY, null);

        g.dispose();

        return resizedImage;
    }

    public static BufferedImage resizeThumbnail(BufferedImage originalImage, int type, BufferedImage image_water_mark){
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

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
