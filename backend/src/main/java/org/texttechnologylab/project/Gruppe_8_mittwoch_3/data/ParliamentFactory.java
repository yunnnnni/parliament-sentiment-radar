package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.util.List;

import org.dom4j.Element;

public interface ParliamentFactory {
    void initFromDirectory(String protocolDirectory);
    void initFromMongoDB(MongoDBConnectionHandler handler);
    List<PlenaryProtocol> getProtocols();
    List<Speech> getSpeeches();
    List<Speaker> getSpeakers();
    List<Fraction> getFractions();

    PlenaryProtocol getProtocol(int session, int term);
    Speech getSpeech(String id);
    Speaker getSpeaker(String id);
    Fraction getFraction(String name);

//    void setProtocols(List<PlenaryProtocol> protocolList);
//    void setSpeeches(List<Speech> speechList);
//    void setSpeakers(List<Speaker> speakerList);
//    void setFractions(List<Fraction> fractionList);

    boolean addProtocol(PlenaryProtocol protocol);
    boolean addSpeech(Speech speech);
    boolean addSpeaker(Speaker speaker);
    boolean addFraction(Fraction fraction);

//    boolean addProtocol(Element protocolElement);
//    boolean addSpeech(Element speechElement);
//    boolean addSpeaker(Element speakerElement);
//    boolean addFraction(String name);
}
