package org.texttechnologylab.project.Gruppe_8_mittwoch_3;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Binary;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.Image_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper.ImageFinder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        MongoCredential credential = MongoCredential.createCredential("PRG_WiSe21_002", "PRG_WiSe21_002", "VPyHfa39".toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("prg2021.texttechnologylab.org", 27020),credential, MongoClientOptions.builder().build());
        MongoDatabase database = mongoClient.getDatabase("PRG_WiSe21_002");
        MongoCollection<Document> collection = database.getCollection("test");

//        // write image
//        Image_Impl testImg = new Image_Impl("https://bilddatenbank.bundestag.de/fotos/file7c4hdt1pwkw7haed3bj.jpg","","","");
//        Document docuSend = testImg.toDocument();
//        collection.insertOne(docuSend);
//
//        // get image
//        Document docuGet = collection.find().first();
//        Image_Impl imgGet = new Image_Impl(docuGet);
//        imgGet.toFile("imgGet.jpg");

        // test ImageFinder
//        try{
//            ImageFinder finder = new ImageFinder("Anja", "Karliczek");
//            String imgUrl = finder.getImgUrl();
//            String description = finder.getDescription();
//            System.out.println();
//        }catch (Exception e){
//
//        }


        // TODO: test write speaker list into mongodb
    }
}
