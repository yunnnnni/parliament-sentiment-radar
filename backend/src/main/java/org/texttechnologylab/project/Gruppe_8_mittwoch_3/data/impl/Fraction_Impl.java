package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Fraction;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;

import java.util.Set;
import java.util.TreeSet;

public class Fraction_Impl implements Fraction {
    private String name;
    private Set<String> speakerIdSet = new TreeSet<>();
    private ParliamentFactory factory = null;

    public Fraction_Impl(String name){
        this.setName(name);
    }

    public Fraction_Impl(String name, ParliamentFactory factory){
        this.factory = factory;
        this.setName(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addSpeaker(Speaker speaker) {
        // TODO: check if null
        this.speakerIdSet.add(speaker.getId());
    }

    @Override
    public Document toDocument() {
        Document document = new Document("name", this.name);
        return document;
    }
}
