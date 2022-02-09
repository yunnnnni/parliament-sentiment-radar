package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import java.util.ArrayList;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;

import java.util.List;
import java.util.Map;
import scala.xml.Elem;

public class Speech_Impl implements Speech {

    private String speechID;
    private Speaker speaker;
    private List<Text> textList = new ArrayList<>();
    private List<String> speechComment = new ArrayList<>();

    public Speech_Impl(Element speechElement){
        this.speechID = speechElement.attributeValue("id");
        List<Element> speechElements = speechElement.elements();
        for (Element elementS : speechElements){
            if (elementS.getName().equals("p")){
                if (elementS.attributes().isEmpty()){
                    continue;
                }
                if (elementS.attributeValue("klasse").equals("redner")){
                    this.speaker = new Speaker_Impl(elementS.element("redner"));
                } else{
                    this.textList.add(new Text_Impl(elementS));
                }
            } else if (elementS.getName().equals("kommentar")) {
                speechComment.add(elementS.getText());
            }
        }

    }

    @Override
    public String getId() {
        return this.speechID;
    }

    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    @Override
    public List<Text> getTexts() {
        return this.textList;
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
