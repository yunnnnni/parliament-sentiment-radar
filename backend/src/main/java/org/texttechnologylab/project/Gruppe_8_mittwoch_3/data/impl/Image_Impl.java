package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import org.bson.types.Binary;

public class Image_Impl {
    private byte [] imageBin;
    private String imageUrl;
    private String imageDescription;
    private String takenOn;
    private String pictureNr;
    private String photographer;

    /**
     * constructor
     * @param imgUrl save the url about image
     * @param description save the description about image
     */
    public Image_Impl(String imgUrl, String description){
        this.imageUrl = imgUrl;
        this.imageDescription = description;
//        this.imageBin = getImageFromUrl(imgUrl);
    }

    /**
     * read the data about image from mongodb document
     * @param imageDocument document in mongodb that holds the relevant data about image
     */
    public Image_Impl(Document imageDocument){
        if (imageDocument.containsKey("imageBin")){
            this.imageBin = imageDocument.get("imageBin", Binary.class).getData();
        }
        if (imageDocument.containsKey("url")){
            this.imageUrl = imageDocument.getString("url");
        }
        if (imageDocument.containsKey("description")){
            this.imageDescription = imageDocument.getString("description");
        }
    }

    /**
     * save the data of the relevant data about image as document type
     * @return the document that stores the data about image
     */
    public Document toDocument(){
        Document document = new Document();
//        document.append("imageBin", new Binary(this.imageBin));
        document.append("url", this.imageUrl);
        document.append("description", this.imageDescription);
        return document;
    }

    /**
     *
     * @param filePath path of the file
     */
    public void toFile(String filePath){
        try{
            File outputFile = new File(filePath);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(this.imageBin);
            }
        } catch (Exception e){

        }
    }

    /**
     * get image from the url link
     * @param urlStr url
     * @return
     */
    public byte [] getImageFromUrl(String urlStr){
        try{
            URL url = new URL(urlStr);
            BufferedImage image = ImageIO.read(url);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", bos);
            return bos.toByteArray();
        } catch (IOException e){
            return null;
        }
    }
}
