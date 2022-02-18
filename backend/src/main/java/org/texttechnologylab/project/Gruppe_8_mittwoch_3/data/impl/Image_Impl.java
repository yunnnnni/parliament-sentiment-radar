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

    public Image_Impl(String imgUrl, String description){
        this.imageUrl = imgUrl;
        this.imageDescription = description;
//        this.imageBin = getImageFromUrl(imgUrl);
    }

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

    public Document toDocument(){
        Document document = new Document();
//        document.append("imageBin", new Binary(this.imageBin));
        document.append("url", this.imageUrl);
        document.append("description", this.imageDescription);
        return document;
    }

    public void toFile(String filePath){
        try{
            File outputFile = new File(filePath);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(this.imageBin);
            }
        } catch (Exception e){

        }
    }

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
