package org.texttechnologylab.project.Gruppe_8_mittwoch_3;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;


import io.github.classgraph.ModuleRef;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.uima.UIMAException;
import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.AgendaItem_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.PlenaryProtocol_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.Speech_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper.ProtocolFileReader;

/**
 * test for NLP
 */
public class NLPTest{

    public static void main(String[] args) throws UIMAException {
        MongoCredential credential = MongoCredential.createCredential("PRG_WiSe21_Gruppe_8_3", "PRG_WiSe21_Gruppe_8_3", "cjJMGyHF".toCharArray());
        ServerAddress address = new ServerAddress("prg2021.texttechnologylab.org", 27020);
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(false).build();
        MongoClient mongoClient = new MongoClient(address, credential, options);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("PRG_WiSe21_Gruppe_8_3");
//    mongoDatabase.createCollection("agendaItemTest");
//    mongoDatabase.createCollection("speechTest");
        MongoCollection<Document> agendaItemCollection = mongoDatabase.getCollection("agendaItemTest");
        MongoCollection<Document> speechCollection = mongoDatabase.getCollection("speechTest");

        List<PlenaryProtocol_Impl> protocolList = new ArrayList<>();
        File d = new File("/Users/chufanzhang/ParliamentSentimentRadar/gruppe_8_mittwoch_3_parliamentsentimentradar/backend/Daten/test");
        File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        for (File file: files){
            System.out.println("------------------------ " + file + "------------------------");
            protocolList.add(new PlenaryProtocol_Impl(file));
        }
        List<AgendaItem> agendaItems = protocolList.get(0).getAgendaItems();
//    System.out.println(ParliamentSentimentRadar.getClassLoader().getResource("am_posmap.txt").getPath());
//        Document document = protocolList.get(0).getAgendaItems().get(0).getSpeeches().get(0).toDocumentWithNLP();
        Document document1 = protocolList.get(0).getAgendaItems().get(0).toDocument();
        agendaItemCollection.insertOne(document1);
//        speechCollection.insertOne(document);

//    for (AgendaItem agendaItem : agendaItems){
//      Document agendaItemDocument = agendaItem.toDocument();
//      agendaItemCollection.insertOne(agendaItemDocument);
//      List<Speech> speeches = agendaItem.getSpeeches();
//      for (Speech speech : speeches){
//        Document speechDocument = speech.toDocumentWithNLP();
//        speechCollection.insertOne(speechDocument);
//        speech.clearJcas();
//     }
//    }
    }
}
