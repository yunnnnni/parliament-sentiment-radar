package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.ParliamentFactory_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class ProtocolMongoDBWriter {
    public static void main(String[] args) {
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler("config/config.json");
        String protocolDirectory = "Daten/test";
        ParliamentFactory factory = new ParliamentFactory_Impl();
        factory.initFromDirectory(protocolDirectory);
        writeProtocols(factory.getProtocols(), handler);
//        writeSpeakers(factory.getParliamentMembers(), handler);
//        writeSpeechs(factory.getSpeeches(), handler);
//        writeFractions(factory.getFractions(), handler);
    }

    /**
     * write plenary protocols into mongodb
     * @param protocolList list of protocols
     * @param handler mongodb handler
     */
    public static void writeProtocols(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
        int bufferSize = 5;
        List<Document> protocolDocuments = new ArrayList<>();
        for (int i=0; i<protocolList.size(); i++){
            PlenaryProtocol protocol = protocolList.get(i);
            // check if document exists
            Bson filter;
            filter = and(eq("session", protocol.getSession()),
                         eq("term", protocol.getTerm()));
            Document rDocument = handler.getCollection("protocols").
                    find(filter).first();
            // if not, build document, add to buffer
            if (rDocument == null){
                System.out.println(i + "/" + protocolList.size() + " Add protocol " + protocol.getSession() + " in buffer.");
                Document protocolDocument = protocol.toDocument();
                protocolDocuments.add(protocolDocument);
            }
            // if buffer full, write documents in buffer to mongodb
            if (protocolDocuments.size() >= bufferSize){
                System.out.println("Write buffer into mongodb");
                handler.writeDocuments("protocols", protocolDocuments);
                protocolDocuments.clear();  // clear buffer after writing
            }
        }
        // write rest documents in buffer
        if (protocolDocuments.size() > 0){
            handler.writeDocuments("protocols", protocolDocuments);
        }
//        System.out.println();
    }
    /**
     * write speakers into mongodb
     * @param speakerList list of speakers
     */
    public static void writeSpeakers(List<Speaker> speakerList, MongoDBConnectionHandler handler){
        int bufferSize = 20;
        List<Document> speakerDocuments = new ArrayList<>();
        Random r = new Random();
        for (int i=0; i<speakerList.size(); i++){
            Speaker speaker = speakerList.get(i);
            // check if document exists
            Document rDocument = handler.getCollection("parliament_members").
                    find(eq("id", speaker.getId())).first();
            // if not, build document, add to buffer
            if (rDocument == null){
                int randomTimeout = r.nextInt(1000) + 500;
                System.out.println(i + "/" + speakerList.size() + " Add speaker " + speaker.getId() + " in buffer. Random timeout: " + randomTimeout);
                Document speakerDocument = speaker.toDocument();
                speakerDocuments.add(speakerDocument);
                try{
                    Thread.sleep(randomTimeout);
                }catch (Exception e){}
            }
            // if buffer full, write documents in buffer to mongodb
            if (speakerDocuments.size() >= bufferSize){
                System.out.println("Write buffer into mongodb");
                handler.writeDocuments("parliament_members", speakerDocuments);
                speakerDocuments.clear();  // clear buffer after writing
            }
        }
        // write rest documents in buffer
        if (speakerDocuments.size() > 0){
            handler.writeDocuments("parliament_members", speakerDocuments);
        }
//        System.out.println();
    }

    public static void writeSpeechs(List<Speech> speechList, MongoDBConnectionHandler handler){
        int bufferSize = 5;
        List<Document> speechDocuments = new ArrayList<>();
        Random r = new Random();
        for (int i=0; i<speechList.size(); i++){
            Speech speech = speechList.get(i);
            // check if document exists
            Document rDocument = handler.getCollection("speeches").
                    find(eq("speechId", speech.getId())).first();
            // if not, build document, add to buffer
            if (rDocument == null){
                System.out.println(i + "/" + speechList.size() + " Add speech " + speech.getId() + " in buffer.");
                Document speechDocument = speech.toDocument();
                speechDocuments.add(speechDocument);
            }
            // if buffer full, write documents in buffer to mongodb
            if (speechDocuments.size() >= bufferSize){
                System.out.println("Write buffer into mongodb");
                handler.writeDocuments("speeches", speechDocuments);
                speechDocuments.clear();  // clear buffer after writing
            }
        }
        // write rest documents in buffer
        if (speechDocuments.size() > 0){
            handler.writeDocuments("speeches", speechDocuments);
        }
//        System.out.println();
    }

    public static void writeFractions(List<Fraction> fractionList, MongoDBConnectionHandler handler){
        int bufferSize = 5;
        List<Document> fractionDocuments = new ArrayList<>();
        Random r = new Random();
        for (int i=0; i<fractionList.size(); i++){
            Fraction fraction = fractionList.get(i);
            // check if document exists
            Document rDocument = handler.getCollection("fractions").
                    find(eq("name", fraction.getName())).first();
            // if not, build document, add to buffer
            if (rDocument == null){
                System.out.println(i + "/" + fractionList.size() + " Add fraction " + fraction.getName() + " in buffer.");
                Document fractionDocument = fraction.toDocument();
                fractionDocuments.add(fractionDocument);
            }
            // if buffer full, write documents in buffer to mongodb
            if (fractionDocuments.size() >= bufferSize){
                System.out.println("Write buffer into mongodb");
                handler.writeDocuments("fractions", fractionDocuments);
                fractionDocuments.clear();  // clear buffer after writing
            }
        }
        // write rest documents in buffer
        if (fractionDocuments.size() > 0){
            handler.writeDocuments("fractions", fractionDocuments);
        }
//        System.out.println();
    }

    /**
     * write speeches into mongodb
     * @param protocolList list of protocols
     * @param handler mongodb handler
     */
    public static void writeSpeeches(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
//        for(PlenaryProtocol protocol: protocolList) {
//            for (AgendaItem agendaItem: protocol.getAgendaItems()){
//                for (Speech speech: agendaItem.getSpeeches()){
//                    handler.writeDocument("speech", speech.toDocument());
//                }
//            }
//        }
    }
}
