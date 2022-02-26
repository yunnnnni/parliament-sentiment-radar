package org.texttechnologylab.project.Gruppe_8_mittwoch_3.database;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *  link mongodb and implement related operations
 */
public class MongoDBConnectionHandler {
    private MongoDBConfig config;
    private MongoDatabase database;
    private Map<String, MongoCollection<Document>> collections = new HashMap<>();

    /**
     * use the data stored in the config file and use it to link to mongodb
     * @param configPath path of the config file
     */
    public MongoDBConnectionHandler(String configPath){
        // Properties config = this.readConfig("Daten/config.json");
        try {
            this.config = this.readConfig(configPath);
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

    /**
     * write documents to the specified collection in mongodb
     * @param collectionName collection name
     * @param documents documents
     */
    public void writeDocuments(String collectionName, List<Document> documents){
        try{
            MongoCollection<Document> collection = this.getCollection(collectionName);
            collection.insertMany(documents);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * write a document to the specified collection in mongodb
     * @param collectionName the specified collection name, which collection to write to
     * @param document document
     */
    public void writeDocument(String collectionName, Document document){
        try{
            MongoCollection<Document> collection = this.getCollection(collectionName);
            collection.insertOne(document);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * by calling this method, you can get the document form the collection specified in mongodb
     * @param collectionName the specified collection name
     * @param query
     * @return document
     */
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

    /**
     * update the document stored in the specified collection in mongodb.
     * @param collectionName the specified collection name
     * @param query
     * @param newDocument the new document
     * @return result, does the update succeed
     */
    public boolean updataDocument(String collectionName, Bson query, Document newDocument){
        MongoCollection<Document> collection = this.getCollection(collectionName);
        try{
            UpdateResult result = collection.replaceOne(query, newDocument);
            if (result != null && result.getModifiedCount() == 0 && result.getMatchedCount() == 0){
                try {
                    this.getCollection(collectionName).insertOne(newDocument);
                    return true;
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * delete the specified document in the specified collection in mongodb
     * @param collectionName the specified collection name
     * @param query
     * @return result, does the delete succeed
     */
    public Boolean deleteDocument(String collectionName, Bson query){
        try {
            MongoCollection<Document> collection = this.getCollection(collectionName);
            DeleteResult result = collection.deleteOne(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
            return true;
        } catch (Exception e) {
//            System.err.println("Unable to delete due to an error: " + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * link to the specified collection in mongodb
     * @param collectionName the specified collection name
     * @return collection
     */
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

    /**
     * read the data stored in the config file
     * @param path path of the config file
     * @return MongoDBConfig instance
     * @throws IOException
     */
    private MongoDBConfig readConfig(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(path), MongoDBConfig.class);
    }

}
