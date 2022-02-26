package org.texttechnologylab.project.Gruppe_8_mittwoch_3.database;

import java.util.List;

/**
 * get mongodb related data
 * host, database, user, password, port and collections
 */
public class MongoDBConfig{
    private String remoteHost;
    private String remoteDatabase;
    private String remoteUser;
    private String remotePassword;
    private int remotePort;
    private List<String> remoteCollections;

//    public Properties(String configJsonPath){
//        ObjectMapper objectMapper = new ObjectMapper();
//        ExampleClass example = objectMapper.readValue(new File("example.json"), ExampleClass.class);
//    }

    public String getRemoteHost(){ return this.remoteHost;}
    public String getRemoteDatabase(){ return this.remoteDatabase;}
    public String getRemoteUser(){ return this.remoteUser;}
    public String getRemotePassword(){ return this.remotePassword;}
    public int getRemotePort(){ return this.remotePort;}
    public List<String> getRemoteCollections(){ return this.remoteCollections;}
}
