package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.ParliamentFactory_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;

public class RESTServices {
    MongoDBConnectionHandler handler = null;
    ParliamentFactory factory = new ParliamentFactory_Impl();

    public RESTServices(){
        this.handler = new MongoDBConnectionHandler("config/config.json");
        this.factory.initFromMongoDB(this.handler);
    }

    public void startServices(){
        get("/speakers", (req, res) -> {
            res.type("application/json");
            return this.speakerService();
        });
    }

    private String speakerService(){
        Document document = new Document();
        List<Document> speakerDocuments = new ArrayList<>();
        try{
            for (Speaker speaker: this.factory.getParliamentMembers()){
                Document speakerDocument = speaker.toDocument();
                speakerDocument.remove("_id");
                speakerDocuments.add(speakerDocument);
            }
        } catch (Exception e){
            List<Document> emptyDocumentList = new ArrayList<>();
            document.append("result", emptyDocumentList);
            document.append("success", false);
            return document.toJson();
        }
        document.append("result", speakerDocuments);
        document.append("success", true);
        return document.toJson();
    }
}
