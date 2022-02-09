package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import org.bson.types.Binary;

public class Image_Impl {
    private byte [] imageBin;
    private String takenOn;
    private String pictureNr;
    private String photographer;

    public Image_Impl(String imgUrl, String takenOn, String pictureNr, String photographer){
//        this.imageBin = getImageFromUrl("https://bilddatenbank.bundestag.de/fotos/file7c4hdt1pwkw7haed3bj.jpg");
        this.imageBin = getImageFromUrl(imgUrl);
        this.takenOn = takenOn;
        this.pictureNr = pictureNr;
        this.photographer = photographer;
    }

    public Image_Impl(Document imageDocument){
        this.imageBin = imageDocument.get("imageBin", Binary.class).getData();
    }

    public Document toDocument(){
        Document document = new Document();
        document.append("imageBin", new Binary(this.imageBin));
        document.append("pictureNr", this.pictureNr);
        document.append("takenOn", this.takenOn);
        document.append("photographer", this.photographer);
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
//        URL url = new URL("https://bilddatenbank.bundestag.de/fotos/file7c4hdt1pwkw7haed3bj.jpg");
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