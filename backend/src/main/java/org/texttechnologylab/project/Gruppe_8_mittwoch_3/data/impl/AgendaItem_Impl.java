package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import java.util.*;
import org.bson.Document;
import org.dom4j.Element;

import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;

public class AgendaItem_Impl implements AgendaItem {
    private String topID;
    private List<String> agendaText = new ArrayList<>();
    private List<Speech> speechList = new ArrayList<>();
    private Set<String> speechIdSet = new TreeSet<>();
    private Set<String> speakerIDSet = new TreeSet<>();
    private List<String> agendaItemComment = new ArrayList<>();
    private ParliamentFactory factory = null;


    public AgendaItem_Impl(Element agendaElement, ParliamentFactory factory) {
        this.factory = factory;
        this.init(agendaElement);
    }

    public AgendaItem_Impl(Element agendaElement) {
        this.init(agendaElement);
    }

    public AgendaItem_Impl(Document agendaDocument){
        if (agendaDocument.containsKey("top-id")){
            this.topID = agendaDocument.getString("top-id");
        }
        if (agendaDocument.containsKey("speech")){
            List<Document> speeches = agendaDocument.getList("speech", Document.class);
            for (Document document : speeches){
                if (document.containsKey("speechID")){
                    this.speechIdSet.add(document.getString("speechID"));
                }
                if (document.containsKey("speakerID")){
                    this.speakerIDSet.add(document.getString("speaker"));
                }
            }
        }
    }

    private void init(Element agendaElement){
        List<String> agendaTextLabel = Arrays
                .asList("J", "J_1", "O", "A_TOP", "T_Beratung", "T_Drs", "T_E_Drs", "T_E_E_Drs", "T_E_fett",
                        "uF020 T_NaS", "T_NaS_NaS", "T_ZP_NaS", "T_ZP_NaS_NaS", "T_ZP_NaS_NaS_Strich",
                        "T_Ueberweisung", "T_fett", "T_ohne_NaS");

        this.topID = agendaElement.attributeValue("top-id");
        List<Element> agendaItemElements = agendaElement.elements();
        for (Element elementsA : agendaItemElements) {
            if (elementsA.getName().equals("rede")) {
                speechList.add(new Speech_Impl(elementsA));
            } else if (elementsA.getName().equals("kommentar")) {
                agendaItemComment.add(elementsA.getText());
            } else if (elementsA.getName().equals("p") && agendaTextLabel
                    .contains(elementsA.attributeValue("klasse"))){
                agendaText.add(elementsA.getText());
            }
        }

//        List<String> agendaTextLabel = Arrays
//              .asList("J", "J_1", "O", "A_TOP", "T_Beratung", "T_Drs", "T_E_Drs", "T_E_E_Drs", "T_E_fett",
//                      "uF020 T_NaS", "T_NaS_NaS", "T_ZP_NaS", "T_ZP_NaS_NaS", "T_ZP_NaS_NaS_Strich",
//                      "T_Ueberweisung", "T_fett", "T_ohne_NaS");
//
//        this.topID = agendaElement.attributeValue("top-id");
//        List<Element> agendaItemElements = agendaElement.elements();
//        for (Element elementsA : agendaItemElements) {
//            if (elementsA.getName().equals("rede")) {
//                if (this.factory != null){
//                    Speech speech = this.factory.addSpeech(elementsA);
//                    this.speechIdSet.add(speech.getId());
//                } else{
//                    speechList.add(new Speech_Impl(elementsA));
//                }
//            } else if (elementsA.getName().equals("kommentar")) {
//                agendaItemComment.add(elementsA.getText());
//            } else if (elementsA.getName().equals("p") && agendaTextLabel
//                    .contains(elementsA.attributeValue("klasse"))){
//                agendaText.add(elementsA.getText());
//            }
//        }
    }

    @Override
    public String getId() {
                              return this.topID;
                                                }

    @Override
    public void printTexts() {
        for(String text : agendaText){
            System.out.println(text);
        }
    }

    @Override
    public List<Speech> getSpeeches() {
                                          return this.speechList;
                                                                 }



    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("topID", this.topID);
        List<Document> speeches = new ArrayList<>();
        for (Speech s : this.speechList){
            speeches.add(s.toDocument());
        }
        document.append("speechIDs", speeches);
        return document;
    }
}
