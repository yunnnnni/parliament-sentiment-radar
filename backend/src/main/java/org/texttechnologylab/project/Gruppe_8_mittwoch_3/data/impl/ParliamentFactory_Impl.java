package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.javatuples.Pair;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import org.dom4j.Element;

import java.io.File;
import java.util.*;

/**
 * class for parliament factory
 * implements interface ParliamentFactory
 */
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

    /**
     *
     * @param protocolDirectory
     */
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

    /**
     * link to the collections in mongodb
     * @param handler the object of class MongoDBConnectionHandler
     */
    @Override
    public void initFromMongoDB(MongoDBConnectionHandler handler) {
        this.handler = handler;
        initProtocolsFromMongoDB();
        initParliamentMembersFromMongoDB();
        initOtherSpeakersFromMongoDB();
        initSpeechesFromMongoDB();
        initFractionsFromMongoDB();
    }

    /**
     * read protocols from the specified collection protocols in mongodb
     */
    private void initProtocolsFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("protocols").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addProtocol(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * read the members from the specified collection parliament_members in mongodb
     */
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

    /**
     * read the speakers from the specified collection other_speakers in mongodb
     */
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

    /**
     * read the speeches from the specified collection speeches in mongodb
     */
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

    /**
     * read the fractions from the specified collection fractions in mongodb
     */
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


    /**
     * get the protocols
     * @return the list of objects of the PlenaryProtocol class
     */
    @Override
    public List<PlenaryProtocol> getProtocols() {
        List<PlenaryProtocol> protocolList = new ArrayList<PlenaryProtocol>(this.protocolMap.values());
        if (protocolList.contains(null)) {
            protocolList.removeIf(Objects::isNull);
        }
        return protocolList;
    }

    /**
     * get the speeches
     * @return
     */
    @Override
    public List<Speech> getSpeeches() {
        List<Speech> speechList = new ArrayList<Speech>(this.speechMap.values());
        if (speechList.contains(null)) {
            speechList.removeIf(Objects::isNull);
        }
        return speechList;
    }

    /**
     *
     * @param speechIdList
     * @return
     */
    @Override
    public List<Speech> getSpeeches(Set<String> speechIdList) {
        if (speechIdList == null){
            return null;
        }
        List<Speech> speechList = new ArrayList<>();
        for (String id: speechIdList){
            try{
                speechList.add(this.getSpeech(id));
            }catch (Exception e){
                continue;
            }
        }
        if (speechList.size() == 0){return null;}

        if (speechList.contains(null)) {
            speechList.removeIf(Objects::isNull);
        }
        return speechList;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Speaker> getSpeakers() {
        List<Speaker> speakerList = new ArrayList<Speaker>(this.speakerMap.values());
        if (speakerList.contains(null)) {
            speakerList.removeIf(Objects::isNull);
        }
        return speakerList;
    }

    /**
     *
     * @param fractionName
     * @return
     */
    @Override
    public List<Speaker> getSpeakersOfFraction(String fractionName) {
        Fraction fraction = this.getFraction(fractionName);
        if (fraction == null){
            return null;
        }
        List<Speaker> speakerList = new ArrayList<>();
        for (String speakerId: fraction.getSpeakerIds()){
            Speaker speaker = this.getSpeaker(speakerId);
            if (speaker != null){
                speakerList.add(speaker);
            }
        }
        if (speakerList.size() == 0){
            return null;
        }
        return speakerList;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Speaker> getParliamentMembers() {
        List<Speaker> speakerList = new ArrayList<Speaker>(this.parliamentMemberMap.values());
        if (speakerList.contains(null)) {
            speakerList.removeIf(Objects::isNull);
        }
        return speakerList;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Speaker> getOtherSpeakers() {
        List<Speaker> speakerList = new ArrayList<Speaker>(this.otherSpeakerMap.values());
        if (speakerList.contains(null)) {
            speakerList.removeIf(Objects::isNull);
        }
        return speakerList;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Fraction> getFractions() {
        List<Fraction> fractionList = new ArrayList<Fraction>(this.fractionMap.values());
        if (fractionList.contains(null)) {
            fractionList.removeIf(Objects::isNull);
        }
        return fractionList;
    }

    /**
     * get the specified plenary protocol
     * @param session number of the pleary protocol
     * @param term wahlpriode
     * @return plenary protocol
     */
    @Override
    public PlenaryProtocol getProtocol(int session, int term) {
        try{
            return this.protocolMap.get(new Pair<>(session, term));
        } catch (Exception e){
            return null;
        }
    }

    /**
     * get the speech based on the id
     * @param id id of the speech
     * @return the speech
     */
    @Override
    public Speech getSpeech(String id) {
        try{
            return this.speechMap.get(id);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * get the speaker based on the id
     * @param id id of the speaker
     * @return speaker
     */
    @Override
    public Speaker getSpeaker(String id) {
        try{
            return this.speakerMap.get(id);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * get the fraction based on the fraction's name
     * @param name name of the fraction
     * @return fraction
     */
    @Override
    public Fraction getFraction(String name) {
        try{
            return this.fractionMap.get(name);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * add Protocol to protocolMap
     * @param protocol object of the PlenaryProtocol class
     * @return
     */
    @Override
    public PlenaryProtocol addProtocol(PlenaryProtocol protocol) {
        PlenaryProtocol existedProtocol = this.getProtocol(protocol.getSession(), protocol.getTerm());
        if (existedProtocol == null){
            this.protocolMap.put(new Pair<>(protocol.getSession(),protocol.getTerm()), protocol);
            return protocol;
        }
        return existedProtocol;
    }

    /**
     * add speech to speschMap and speaker's id to speakerMap
     * @param speech object of the Speech class
     * @return
     */
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

    /**
     * add speaker to speakerMap
     * @param speaker object of the Speaker class
     * @return
     */
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

    /**
     * add fraction to fractionMap
     * @param fraction object of the Fraction class
     * @return
     */
    @Override
    public Fraction addFraction(Fraction fraction) {
        Fraction existedFraction = this.getFraction(fraction.getName());
        if (existedFraction == null){
            this.fractionMap.put(fraction.getName(),fraction);
            return fraction;
        }
        return existedFraction;
    }

    /**
     * create a object of the PlenaryProtocol class from the xml file
     * @param xmlFile xml file
     * @return
     */
    @Override
    public PlenaryProtocol addProtocol(File xmlFile) {
        PlenaryProtocol protocol = new PlenaryProtocol_Impl(xmlFile, this);
        return this.addProtocol(protocol);
    }

    /**
     * create a object of the Speech class from the speech element
     * @param speechElement element for speech
     * @return
     */
    @Override
    public Speech addSpeech(Element speechElement) {
        Speech speech = new Speech_Impl(speechElement, this);
        return this.addSpeech(speech);
    }

    /**
     * create a object of the Speaker class from the speaker element
     * @param speakerElement element for speaker
     * @return
     */
    @Override
    public Speaker addSpeaker(Element speakerElement) {
        Speaker speaker = new Speaker_Impl(speakerElement, this);
        return this.addSpeaker(speaker);
    }

    /**
     * create a object of the Speaker class from the fraction's name
     * @param name the name of fraction
     * @return
     */
    @Override
    public Fraction addFraction(String name) {
        Fraction fraction = new Fraction_Impl(name, this);
        return this.addFraction(fraction);
    }

    /**
     * create the object of the PlenaryProtocol class from mongodb document
     * @param protocolDocument document in mongodb that holds the relevant data about plenary protocol
     * @return
     */
    @Override
    public PlenaryProtocol addProtocol(Document protocolDocument) {
        PlenaryProtocol protocol = new PlenaryProtocol_Impl(protocolDocument, this);
        return this.addProtocol(protocol);
    }

    /**
     * create the object of the Speech class from mongodb document
     * @param speechDocument document in mongodb that holds the relevant data about speech
     * @return
     */
    @Override
    public Speech addSpeech(Document speechDocument) {
        Speech speech = new Speech_Impl(speechDocument, this);
        return this.addSpeech(speech);
    }

    /**
     * create the object of the Speaker class from mongodb document
     * @param speakerDocument document in mongodb that holds the relevant data about speaker
     * @return
     */
    @Override
    public Speaker addSpeaker(Document speakerDocument) {
        Speaker speaker = new Speaker_Impl(speakerDocument, this);
        return this.addSpeaker(speaker);
    }

    /**
     * create the object of the Fraction class from mongodb document
     * @param fractionDocument document in mongodb that holds the relevant data about fraction
     * @return
     */
    @Override
    public Fraction addFraction(Document fractionDocument) {
        Fraction fraction = new Fraction_Impl(fractionDocument, this);
        return this.addFraction(fraction);
    }
}
