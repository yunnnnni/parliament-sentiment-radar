package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import edu.washington.cs.knowitall.logic.Expression;
import org.apache.uima.ruta.type.html.P;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParliamentFactory_Impl implements ParliamentFactory {
    /**
     Reasons for using arraylist instead of hashset/treeset:
     1. reduce memory usage (hashset needs more much more memory than list)
        - REST Service can read all of the data into memory to prove efficiency
     2. efficient by iteration (it's faster to iterate over arraylist than hashset)
        - efficiency for searching & filtering is more important than creation
        - only during creation the list is indexed by id
            in this case hashset has O(1), arrayList has O(n)
        - when filtering, we have to manually go through each element to find the one that matches the filter conditions
            only one keyword (like id) can be set as key in hashmap, but we can filter speech by speaker, fraction ...
            in this case hashset has O(n), arrayList has O(n)
     3. easy to implement and understand (no need to overwrite hashcode(), compare(), equals() for each class)

     For the above reasons, we choose arraylist to store all the data
     the uniqueness of the data is guaranteed by the add function
    **/
    private List<PlenaryProtocol> protocolList = new ArrayList<>();
    private List<Speech> speechList = new ArrayList<>();
    private List<Speaker> speakerList = new ArrayList<>();
    private List<Speaker> parliamentMemberList = new ArrayList<>();
    private List<Speaker> otherSpeakerList = new ArrayList<>();
    private List<Fraction> fractionList = new ArrayList<>();
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

    }

    @Override
    public List<PlenaryProtocol> getProtocols() {
        return this.protocolList;
    }

    @Override
    public List<Speech> getSpeeches() {
        return this.speechList;
    }

    @Override
    public List<Speaker> getSpeakers() {
        return this.speakerList;
    }

    @Override
    public List<Fraction> getFractions() {
        return this.fractionList;
    }

    @Override
    public PlenaryProtocol getProtocol(int session, int term) {
        for (PlenaryProtocol protocol: this.protocolList){
            if (protocol.getSession() == session && protocol.getTerm() == term){
                return protocol;
            }
        }
        return null;
    }

    @Override
    public Speech getSpeech(String id) {
        for (Speech speech: this.speechList){
            if (speech.getId().equals(id)){
                return speech;
            }
        }
        return null;
    }

    @Override
    public Speaker getSpeaker(String id) {
        for (Speaker speaker: this.speakerList){
            if (speaker.getId().equals(id)){
                return speaker;
            }
        }
        return null;
    }

    @Override
    public Fraction getFraction(String name) {
        for (Fraction fraction: this.fractionList){
            if (fraction.getName().equals(name)){
                return fraction;
            }
        }
        return null;
    }

    @Override
    public PlenaryProtocol addProtocol(PlenaryProtocol protocol) {
        PlenaryProtocol existedProtocol = this.getProtocol(protocol.getSession(), protocol.getTerm());
        if (existedProtocol == null){
            this.protocolList.add(protocol);
            return protocol;
        }
        return existedProtocol;
    }

    @Override
    public Speech addSpeech(Speech speech) {
        Speech existedSpeech = this.getSpeech(speech.getId());
        if (existedSpeech == null){
            this.speechList.add(speech);
            return speech;
        }
        return existedSpeech;
    }

    @Override
    public Speaker addSpeaker(Speaker speaker) {
        Speaker existedSpeaker = this.getSpeaker(speaker.getId());
        if (existedSpeaker == null){
            this.speakerList.add(speaker);
            if (speaker.isParliamentMember()){
                this.parliamentMemberList.add(speaker);
            } else{
                this.otherSpeakerList.add(speaker);
            }
            return speaker;
        }
        return existedSpeaker;
    }

    @Override
    public Fraction addFraction(Fraction fraction) {
        Fraction existedFraction = this.getFraction(fraction.getName());
        if (existedFraction == null){
            this.fractionList.add(fraction);
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
        return addSpeech(speech);
    }

    @Override
    public Speaker addSpeaker(Element speakerElement) {
        Speaker speaker = new Speaker_Impl(speakerElement, this);
        return addSpeaker(speaker);
    }

    @Override
    public Fraction addFraction(String name) {
        Fraction fraction = new Fraction_Impl(name, this);
        return addFraction(fraction);
    }
}
