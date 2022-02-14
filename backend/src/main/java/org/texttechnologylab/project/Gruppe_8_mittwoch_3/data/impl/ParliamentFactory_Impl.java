package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.javatuples.Pair;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import org.dom4j.Element;

import java.io.File;
import java.util.*;

public class ParliamentFactory_Impl implements ParliamentFactory {
    // use TreeMap to ensure data uniqueness and retrieval performance
    private Map<Pair<Integer, Integer>, PlenaryProtocol> protocolMap = new TreeMap<>();
    private Map<String, Speech> speechMap = new TreeMap<>();
    private Map<String, Speaker> speakerMap = new TreeMap<>();
    private Map<String, Speaker> parliamentMemberMap = new TreeMap<>();
    private Map<String, Speaker> otherSpeakerMap = new TreeMap<>();
    private Map<String, Fraction> fractionMap = new TreeMap<>();
    private MongoDBConnectionHandler handler;

    public ParliamentFactory_Impl(){

    }

    @Override
    public void initFromDirectory(String protocolDirectory) {
        try{
            File d = new File(protocolDirectory);
            File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            for (File file: files){
                System.out.println("------------------------ " + file + "------------------------");
                this.addProtocol(file);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initFromMongoDB(MongoDBConnectionHandler handler) {
        this.handler = handler;
        initParliamentMembersFromMongoDB();
        initOtherSpeakersFromMongoDB();
        initSpeechesFromMongoDB();
        initFractionsFromMongoDB();
    }

    private void initParliamentMembersFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("parliament_members").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addSpeaker(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void initOtherSpeakersFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("other_speakers").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addSpeaker(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void initSpeechesFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("speeches").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addSpeech(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void initFractionsFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("fractions").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addFraction(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }


    @Override
    public List<PlenaryProtocol> getProtocols() {
        return new ArrayList<PlenaryProtocol>(this.protocolMap.values());
    }

    @Override
    public List<Speech> getSpeeches() {
        return new ArrayList<Speech>(this.speechMap.values());
    }

    @Override
    public List<Speaker> getSpeakers() {
        return new ArrayList<Speaker>(this.speakerMap.values());
    }

    @Override
    public List<Speaker> getParliamentMembers() {
        return new ArrayList<Speaker>(this.parliamentMemberMap.values());
    }

    @Override
    public List<Speaker> getOtherSpeakers() {
        return new ArrayList<Speaker>(this.otherSpeakerMap.values());
    }

    @Override
    public List<Fraction> getFractions() {
        return new ArrayList<Fraction>(this.fractionMap.values());
    }

    @Override
    public PlenaryProtocol getProtocol(int session, int term) {
        return this.protocolMap.get(new Pair<>(session, term));
    }

    @Override
    public Speech getSpeech(String id) {
        return this.speechMap.get(id);
    }

    @Override
    public Speaker getSpeaker(String id) {
        return this.speakerMap.get(id);
    }

    @Override
    public Fraction getFraction(String name) {
        return this.fractionMap.get(name);
    }

    @Override
    public PlenaryProtocol addProtocol(PlenaryProtocol protocol) {
        PlenaryProtocol existedProtocol = this.getProtocol(protocol.getSession(), protocol.getTerm());
        if (existedProtocol == null){
            this.protocolMap.put(new Pair<>(protocol.getSession(),protocol.getTerm()), protocol);
            return protocol;
        }
        return existedProtocol;
    }

    @Override
    public Speech addSpeech(Speech speech) {
        Speech existedSpeech = this.getSpeech(speech.getId());
        if (existedSpeech == null){
            this.speechMap.put(speech.getId(), speech);
            String speakerId = speech.getSpeakerId();
            if (this.speakerMap.get(speakerId)!= null){
                this.speakerMap.get(speakerId).addSpeech(speech.getId());  // assign speech to speaker
            }
            return speech;
        }
        return existedSpeech;
    }

    @Override
    public Speaker addSpeaker(Speaker speaker) {
        Speaker existedSpeaker = this.getSpeaker(speaker.getId());
        if (existedSpeaker == null){
            this.speakerMap.put(speaker.getId(), speaker);
            if (speaker.isParliamentMember()){
                this.parliamentMemberMap.put(speaker.getId(), speaker);
            } else{
                this.otherSpeakerMap.put(speaker.getId(), speaker);
            }
            return speaker;
        }
        return existedSpeaker;
    }

    @Override
    public Fraction addFraction(Fraction fraction) {
        Fraction existedFraction = this.getFraction(fraction.getName());
        if (existedFraction == null){
            this.fractionMap.put(fraction.getName(),fraction);
            return fraction;
        }
        return existedFraction;
    }

    @Override
    public PlenaryProtocol addProtocol(File xmlFile) {
        PlenaryProtocol protocol = new PlenaryProtocol_Impl(xmlFile, this);
        return this.addProtocol(protocol);
    }

    @Override
    public Speech addSpeech(Element speechElement) {
        Speech speech = new Speech_Impl(speechElement, this);
        return this.addSpeech(speech);
    }

    @Override
    public Speaker addSpeaker(Element speakerElement) {
        Speaker speaker = new Speaker_Impl(speakerElement, this);
        return this.addSpeaker(speaker);
    }

    @Override
    public Fraction addFraction(String name) {
        Fraction fraction = new Fraction_Impl(name, this);
        return this.addFraction(fraction);
    }

    @Override
    public PlenaryProtocol addProtocol(Document protocolDocument) {
        return null;
    }

    @Override
    public Speech addSpeech(Document speechDocument) {
        Speech speech = new Speech_Impl(speechDocument, this);
        return this.addSpeech(speech);
    }

    @Override
    public Speaker addSpeaker(Document speakerDocument) {
        Speaker speaker = new Speaker_Impl(speakerDocument, this);
        return this.addSpeaker(speaker);
    }

    @Override
    public Fraction addFraction(Document fractionDocument) {
        Fraction fraction = new Fraction_Impl(fractionDocument, this);
        return this.addFraction(fraction);
    }
}
