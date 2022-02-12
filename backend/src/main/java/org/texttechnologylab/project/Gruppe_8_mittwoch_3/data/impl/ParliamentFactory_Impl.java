package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import edu.washington.cs.knowitall.logic.Expression;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParliamentFactory_Impl implements ParliamentFactory {
    private List<PlenaryProtocol> protocolList = new ArrayList<>();
    private List<Speech> speechList = new ArrayList<>();
    private List<Speaker> speakerList = new ArrayList<>();
    private List<Fraction> fractionList = new ArrayList<>();
    private MongoDBConnectionHandler handler;

    @Override
    public void initFromDirectory(String protocolDirectory) {
        try{
            File d = new File(protocolDirectory);
            File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            List<PlenaryProtocol> protocolList = new ArrayList<>();
            for (File file: files){
                System.out.println("------------------------ " + file + "------------------------");
                protocolList.add(new PlenaryProtocol_Impl(file));
            }
//            return protocolList;
        } catch (Exception e){
            e.printStackTrace();
//            return null;
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
            if (speech.getId() == id){
                return speech;
            }
        }
        return null;
    }

    @Override
    public Speaker getSpeaker(String id) {
        for (Speaker speaker: this.speakerList){
            if (speaker.getId() == id){
                return speaker;
            }
        }
        return null;
    }

    @Override
    public Fraction getFraction(String name) {
        for (Fraction fraction: this.fractionList){
            if (fraction.getName() == name){
                return fraction;
            }
        }
        return null;
    }

//    @Override
//    public void setProtocols(List<PlenaryProtocol> protocolList) {
//
//    }
//
//    @Override
//    public void setSpeeches(List<Speech> speechList) {
//
//    }
//
//    @Override
//    public void setSpeakers(List<Speaker> speakerList) {
//
//    }
//
//    @Override
//    public void setFractions(List<Fraction> fractionList) {
//
//    }

    @Override
    public boolean addProtocol(PlenaryProtocol protocol) {
        if (this.getProtocol(protocol.getSession(), protocol.getTerm()) != null){
            this.protocolList.add(protocol);
            return true;
        }
        return false;
    }

    @Override
    public boolean addSpeech(Speech speech) {
        if (this.getSpeech(speech.getId()) != null){
            this.speechList.add(speech);
            return true;
        }
        return false;
    }

    @Override
    public boolean addSpeaker(Speaker speaker) {
        if (this.getSpeaker(speaker.getId()) != null){
            this.speakerList.add(speaker);
            return true;
        }
        return false;
    }

    @Override
    public boolean addFraction(Fraction fraction) {
        if (this.getFraction(fraction.getName()) != null){
            this.fractionList.add(fraction);
            return true;
        }
        return false;
    }

//    @Override
//    public boolean addProtocol(Element protocolElement) {
//
//    }
//
//    @Override
//    public boolean addSpeech(Element speechElement) {
//        Speech speech = new Speech_Impl(speechElement);
//        return this.addSpeech()
//    }
//
//    @Override
//    public boolean addSpeaker(Element speakerElement) {
//        Speaker speaker = new Speaker_Impl(speakerElement);
//        return this.addSpeaker(speaker);
//    }
//
//    @Override
//    public boolean addFraction(String name) {
//        Fraction fraction = new Fraction_Impl(name);
//        return this.addFraction(fraction);
//    }
}
