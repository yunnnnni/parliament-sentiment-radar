package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.Image_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.PlenaryProtocol_Impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProtocolFileReader {

    /**
     * Main function
     * get protocol directory
     * @param args arg unused
     */
    public static void main(String[] args) {
        String protocolDirectory = "backend/Daten/test/";
        List<PlenaryProtocol> protocolList = readProtocols(protocolDirectory);
        // Test for write speaker into mongodb
//        PlenaryProtocol protocol = protocolList.get(0);
//        List<Speaker> speakerList = protocol.getSpeakerList();
//        MongoCredential credential = MongoCredential.createCredential("PRG_WiSe21_002", "PRG_WiSe21_002", "VPyHfa39".toCharArray());
//        MongoClient mongoClient = new MongoClient(new ServerAddress("prg2021.texttechnologylab.org", 27020),credential, MongoClientOptions.builder().build());
//        MongoDatabase database = mongoClient.getDatabase("PRG_WiSe21_002");
//        MongoCollection<Document> collection = database.getCollection("test");
//
//        for (Speaker speaker: speakerList){
//            try {
//                ImageFinder finder = new ImageFinder(speaker.getFirstName(), speaker.getLastName());
//                speaker.setImage(new Image_Impl(finder.getImgUrl(), finder.getDescription()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            collection.insertOne(speaker.toDocument());
//        }
    }

    /**
     * read plenary protocol xml files from directory
     * @param protocolDirectory path of the directory
     * @return list of plenary protocols
     */
    public static List<PlenaryProtocol> readProtocols(String protocolDirectory){
        try{
            File d = new File(protocolDirectory);
            File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            List<PlenaryProtocol> protocolList = new ArrayList<>();
            for (File file: files){
                System.out.println("------------------------ " + file + "------------------------");
                protocolList.add(new PlenaryProtocol_Impl(file));
            }
            return protocolList;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
