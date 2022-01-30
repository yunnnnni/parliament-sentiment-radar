package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.util.List;

public class ProtocolMongoDBWriter {

    public static void main(String[] args) {
        String protocolDirectory = "./Daten/test/";
        List<PlenaryProtocol> protocolList = ProtocolFileReader.readProtocols(protocolDirectory);
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler("config/config.json");
        writeProtocols(protocolList, handler);
    }

    /**
     * write plenary protocols into mongodb
     * @param protocolList list of protocols
     * @param handler mongodb handler
     */
    public static void writeProtocols(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
        for(PlenaryProtocol protocol: protocolList) {
            Document protocolDocument = protocol.toDocument();
            handler.writeDocument("protocol", protocolDocument);
        }
    }

    /**
     * write plenary members into mongodb
     * @param protocolList list of protocols
     * @param handler mongodb handler
     */
    public static void writePlenaryMemebers(List<PlenaryProtocol> protocolList, MongoDBConnectionHandler handler){
        for(PlenaryProtocol protocol: protocolList) {
            for (Speaker plenaryMember: protocol.getSpeakerList()){
                handler.writeDocument("speaker", plenaryMember.toDocument());
            }
        }
    }

    /**
     * write speeches into mongodb
     * @param protocolList list of protocols
     * @param handler mongodb handler
     */
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
