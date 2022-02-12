package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static spark.Spark.*;

public class HelloWorld {
    public static void main(String[] args) {
        MongoCredential credential = MongoCredential.createCredential("PRG_WiSe21_002", "PRG_WiSe21_002", "VPyHfa39".toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("prg2021.texttechnologylab.org", 27020),credential, MongoClientOptions.builder().build());
        MongoDatabase database = mongoClient.getDatabase("PRG_WiSe21_002");
        MongoCollection<Document> collection = database.getCollection("test");

        get("/hello", (req, res) -> "Hello World");
    }
}