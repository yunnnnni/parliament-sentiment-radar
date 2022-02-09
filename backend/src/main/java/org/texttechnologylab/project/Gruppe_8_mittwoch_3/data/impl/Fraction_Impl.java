package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Fraction;

public class Fraction_Impl implements Fraction {
    private String name;

    public Fraction_Impl(String name){
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
    public Document toDocument() {
        Document document = new Document("name", this.name);
        return document;
    }
}
