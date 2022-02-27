package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Fraction;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;

import java.util.*;

/**
 * class for fraction
 * implements interface Fraction
 * @author Yunni Lu
 */
public class Fraction_Impl implements Fraction {
    private String name;
    private Set<String> speakerIdSet = new TreeSet<>();
    private ParliamentFactory factory = null;

    /**
     * constructor
     * @param fractionName fraction name
     */
    public Fraction_Impl(String fractionName){
        this.setName(fractionName);
    }

    /**
     * constructor
     * @param name fraction name
     * @param factory the object of class ParliamentFactory
     */
    public Fraction_Impl(String name, ParliamentFactory factory){
        this.factory = factory;
        this.setName(name);
    }

    /**
     * constructor
     * read the data from mongodb document
     * save the data about fraction, name and all speaker id
     * @param fractionDocument document in mongodb that holds the relevant data about fraction
     * @param factory the object of class ParliamentFactory
     */
    public Fraction_Impl(Document fractionDocument, ParliamentFactory factory){
        this.factory = factory;
        if (fractionDocument.containsKey("name")){
            this.name = fractionDocument.getString("name");
        }
        if (fractionDocument.containsKey("speakerIds")){
            this.speakerIdSet.addAll(fractionDocument.getList("speakerIds", String.class));
        }
    }

    /**
     * get the fraction name
     * @return fraction name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * store the fraction names in the privat attribute name
     * @param fractionName fraction name
     */
    @Override
    public void setName(String fractionName) {
        fractionName = fractionName.replace("\u00a0"," ").trim();
        if (fractionName.equals("CDU/ CSU")){fractionName="CDU/CSU";}
        else if (fractionName.equals("fraktionslos")){fractionName="Fraktionslos";}
        else if (fractionName.replaceAll("\\s+","").toLowerCase(Locale.ROOT).equals("bündnis90/diegrünen")){
            fractionName="BÜNDNIS 90/DIE GRÜNEN";}
        else if ("bündnis90/diegrünen".contains(fractionName.replaceAll("\\s+","").toLowerCase(Locale.ROOT))){
            fractionName="BÜNDNIS 90/DIE GRÜNEN";}
        else if (fractionName.equals("SPD: Ja.")){fractionName="SPD";}
        else if (fractionName.equals("Erklärung nach § 30 GO")){fractionName="AfD";}
        this.name = fractionName;
    }

    /**
     * store the speaker id to speakerIdSet list
     * @param speaker the object of class Speaker
     */
    @Override
    public void addSpeaker(Speaker speaker) {
        // TODO: check if null
        this.speakerIdSet.add(speaker.getId());
    }

    /**
     * get the speaker id
     * @return the list about speaker id
     */
    @Override
    public List<String> getSpeakerIds() {
        return new ArrayList<>(this.speakerIdSet);
    }

    /**
     * save the data of the relevant data about fraction as document type
     * @return the document that stores the data about fraction
     */
    @Override
    public Document toDocument() {
        Document document = new Document("name", this.name);
        document.append("speakerIds", this.speakerIdSet);
        return document;
    }
}
