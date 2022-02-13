package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.BSON;
import org.bson.Document;
import org.neo4j.cypher.internal.frontend.v3_2.phases.Do;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.io.File;
import java.util.List;

import org.dom4j.Element;

public interface ParliamentFactory {
    void initFromDirectory(String protocolDirectory);
    void initFromMongoDB(MongoDBConnectionHandler handler);
    List<PlenaryProtocol> getProtocols();
    List<Speech> getSpeeches();
    List<Speaker> getSpeakers();
    List<Speaker> getParliamentMembers();
    List<Speaker> getOtherSpeakers();
    List<Fraction> getFractions();

    PlenaryProtocol getProtocol(int session, int term);
    Speech getSpeech(String id);
    Speaker getSpeaker(String id);
    Fraction getFraction(String name);

    PlenaryProtocol addProtocol(PlenaryProtocol protocol);
    Speech addSpeech(Speech speech);
    Speaker addSpeaker(Speaker speaker);
    Fraction addFraction(Fraction fraction);

    PlenaryProtocol addProtocol(File xmlFile);
    Speech addSpeech(Element speechElement);
    Speaker addSpeaker(Element speakerElement);
    Fraction addFraction(String name);

    PlenaryProtocol addProtocol(Document protocolDocument);
    Speech addSpeech(Document speechDocument);
    Speaker addSpeaker(Document speakerDocument);
    Fraction addFraction(Document fractionDocument);
}
