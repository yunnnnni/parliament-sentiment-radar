package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Binary;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST.RESTServices;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.Image_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper.ImageFinder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * test for image
 */
public class RESTStarter {
    public static void main(String[] args) throws IOException {
        RESTServices rest = new RESTServices();
        rest.startServices();
    }
}
