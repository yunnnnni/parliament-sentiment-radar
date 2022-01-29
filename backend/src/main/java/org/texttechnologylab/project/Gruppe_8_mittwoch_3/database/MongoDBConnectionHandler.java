package org.texttechnologylab.project.Gruppe_8_mittwoch_3.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;


public class MongoDBConnectionHandler {
    private MongoDBConfig config;
    private MongoDatabase database;
    private MongoCollection<Document> protocolCollection;
    private MongoCollection<Document> redeCollection;

    public MongoDBConnectionHandler(String configPath) throws IOException {
        // Properties config = this.readConfig("Daten/config.json");
        this.config = this.readConfig(configPath);
        try {
            MongoCredential credential = MongoCredential.createCredential(this.config.getRemoteUser(), this.config.getRemoteDatabase(), this.config.getRemotePassword().toCharArray());
            MongoClient mongoClient = new MongoClient(new ServerAddress(this.config.getRemoteHost(), this.config.getRemotePort()),credential, MongoClientOptions.builder().build());
            this.database = mongoClient.getDatabase(this.config.getRemoteDatabase());
            this.protocolCollection = this.database.getCollection(this.config.getRemoteCollection());
            this.redeCollection = this.database.getCollection("rede");
            System.out.println("Connect to database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void writeDocument(Document document){
        // MongoCollection<Document> collection = this.database.getCollection(collectionName);
        this.protocolCollection.insertOne(document);
    }

    public Document getDocument(Bson query){
        try{
            return this.protocolCollection.find(query).first();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updataDocument(Bson query, Document document){
    }

    public Boolean deleteDocument(Bson query){
        try {
            DeleteResult result = this.protocolCollection.deleteOne(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            return false;
        }
    }

    private MongoDBConfig readConfig(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(path), MongoDBConfig.class);
    }

}
