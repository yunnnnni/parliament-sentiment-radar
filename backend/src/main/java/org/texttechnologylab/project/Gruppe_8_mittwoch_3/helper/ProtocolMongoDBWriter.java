package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.PlenaryProtocol_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProtocolMongoDBWriter {

    public static void main(String[] args) {
        String protocolDirectory = "./Daten/test/";
        List<PlenaryProtocol> protocolList = readProtocols(protocolDirectory);
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler("Daten/config.json");
//        writeProtocols(protocolList, handler);
    }

    public static List<PlenaryProtocol> readProtocols(String protocolDirectory){
        // String protocolDirectory = "./Daten/Bundestag_19/";
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

    public static void writeProtocols(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
        for(PlenaryProtocol protocol: protocolList) {
            Document protocolDocument = protocol.toDocument();
            handler.writeDocument("protocol", protocolDocument);
        }
    }

    public static void writePlenaryMemebers(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
        for(PlenaryProtocol protocol: protocolList) {
            for (Speaker plenaryMember: protocol.getSpeakerList()){
                handler.writeDocument("speaker", plenaryMember.toDocument());
            }
        }
    }

    public static void writeSpeeches(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
        for(PlenaryProtocol protocol: protocolList) {
            for (AgendaItem agendaItem: protocol.getAgendaItems()){
                for (Speech speech: agendaItem.getSpeeches()){
                    handler.writeDocument("speech", speech.toDocument());
                }
            }
        }
    }
}
