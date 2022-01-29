package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;

import java.io.File;
import java.util.List;

public class PlenaryProtocol_Impl implements PlenaryProtocol {

    public PlenaryProtocol_Impl(File file){

    }

    @Override
    public List<Speaker> getSpeakerList() {
        return null;
    }

    @Override
    public List<Speech> getSpeechList() {
        return null;
    }

    @Override
    public String getPlenaryNr() {
        return null;
    }

    @Override
    public AgendaItem getAgendaItem(String numberIndex) {
        return null;
    }

    @Override
    public Document toDocument() {
        return null;
    }
}
