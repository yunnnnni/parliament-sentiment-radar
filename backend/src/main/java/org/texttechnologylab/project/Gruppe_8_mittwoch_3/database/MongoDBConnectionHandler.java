package org.texttechnologylab.project.Gruppe_8_mittwoch_3.database;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private Map<String, MongoCollection<Document>> collections = new HashMap<>();

    public MongoDBConnectionHandler(String configPath) throws IOException {
        // Properties config = this.readConfig("Daten/config.json");
        this.config = this.readConfig(configPath);
        try {
            MongoCredential credential = MongoCredential.createCredential(this.config.getRemoteUser(), this.config.getRemoteDatabase(), this.config.getRemotePassword().toCharArray());
            MongoClient mongoClient = new MongoClient(new ServerAddress(this.config.getRemoteHost(), this.config.getRemotePort()),credential, MongoClientOptions.builder().build());
            this.database = mongoClient.getDatabase(this.config.getRemoteDatabase());
            for (String collectionName: this.config.getRemoteCollections()){
                this.collections.put(collectionName, this.database.getCollection(collectionName));
            }
            System.out.println("Connect to database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void writeDocument(String collectionName, Document document){
        try{
            MongoCollection<Document> collection = this.getCollection(collectionName);
            collection.insertOne(document);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Document getDocument(String collectionName, Bson query){
        try{
            MongoCollection<Document> collection = this.getCollection(collectionName);
            return collection.find(query).first();
        } catch (NullPointerException e){
            System.err.println("MongoDBConnectionHandler.getDocument: collection " + collectionName + " not found!");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updataDocument(String collectionName, Bson query, Document document){

    }

    public Boolean deleteDocument(String collectionName, Bson query){
        try {
            MongoCollection<Document> collection = this.getCollection(collectionName);
            DeleteResult result = collection.deleteOne(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            return false;
        }
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        try {
            return this.collections.get(collectionName);
        } catch (NullPointerException e) {
            System.err.println("MongoDBConnectionHandler.getCollection: collection " + collectionName + " not found!");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private MongoDBConfig readConfig(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(path), MongoDBConfig.class);
    }

}
