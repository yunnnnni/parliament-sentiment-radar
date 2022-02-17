package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class HelloWorld {
    public static void main(String[] args) {
        MongoDBConnectionHandler handler = new MongoDBConnectionHandler("config/config.json");

        get("/hello", (req, res) -> "Hello World");
        get("/speakers", (req, res) -> {
            res.type("application/json");
            Document document = new Document();
            List<Document> retVals = new ArrayList<>();
            Document retDoc = handler.getDocument("parliament_members", new BsonDocument());
            retDoc.remove("_id");
            retVals.add(retDoc);
            document.append("result", retVals);
            document.append("success", true);
            return document.toJson();
        });
    }
}