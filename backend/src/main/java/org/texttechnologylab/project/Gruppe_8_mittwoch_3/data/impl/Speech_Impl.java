package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;

import java.util.List;
import java.util.Map;

public class Speech_Impl implements Speech {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public Speaker getSpeaker() {
        return null;
    }

    @Override
    public List<Text> getTexts() {
        return null;
    }

    @Override
    public Document toDocument() {
        return null;
    }

    @Override
    public JCas toCAS() throws UIMAException {
        return null;
    }

    @Override
    public JCas getCAS() {
        return null;
    }

    @Override
    public Map<String, Object> getAnnotations() {
        return null;
    }

    @Override
    public void clearJcas() {

    }

    @Override
    public void setSitzungsNr(String nr) {

    }
}
