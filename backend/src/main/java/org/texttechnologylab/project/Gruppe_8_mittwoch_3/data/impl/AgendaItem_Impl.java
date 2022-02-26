package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import java.util.*;

import org.apache.uima.ruta.type.html.P;
import org.bson.Document;
import org.dom4j.Element;

import org.javatuples.Pair;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;

/**
 * class for each agenda item
 * implements interface AgendaItem
 */
public class AgendaItem_Impl implements AgendaItem {
    private String topId;
    private List<String> agendaText = new ArrayList<>();
//    private List<Speech> speechList = new ArrayList<>();
    private Set<String> speechIdSet = new TreeSet<>();
    private Set<String> speakerIdSet = new TreeSet<>();
    private List<String> agendaItemComment = new ArrayList<>();
    private Pair<Integer, Integer> protocolId = null;
    private ParliamentFactory factory = null;

    /**
     * constructor
     * @param agendaElement element for agenda item
     * @param protocolId wahlpriode and sitzungsnummer
     * @param factory the object of class ParliamentFactory
     */
    public AgendaItem_Impl(Element agendaElement, Pair<Integer, Integer> protocolId, ParliamentFactory factory) {
        this.factory = factory;
        this.protocolId = protocolId;
        this.init(agendaElement);
    }

    /**
     * constructor
     * @param agendaDocument document in mongodb that holds the relevant data about agenda item
     * @param factory the object of class ParliamentFactory
     */
    public AgendaItem_Impl(Document agendaDocument, ParliamentFactory factory){
        this.factory = factory;
        this.init(agendaDocument);
    }

    /**
     * read the data about agenda item from protocol xml file
     * through this method can get the data about agenda item
     * top-id, speech element, comment and text from agenda item
     * @param agendaElement element about agenda items
     */
    private void init(Element agendaElement){
        List<String> agendaTextLabel = Arrays
                .asList("J", "J_1", "O", "A_TOP", "T_Beratung", "T_Drs", "T_E_Drs", "T_E_E_Drs", "T_E_fett",
                        "uF020 T_NaS", "T_NaS_NaS", "T_ZP_NaS", "T_ZP_NaS_NaS", "T_ZP_NaS_NaS_Strich",
                        "T_Ueberweisung", "T_fett", "T_ohne_NaS");

        this.topId = agendaElement.attributeValue("top-id");
        List<Element> agendaItemElements = agendaElement.elements();
        for (Element elementsA : agendaItemElements) {
            if (elementsA.getName().equals("rede")) {
                Speech speech = this.factory.addSpeech(elementsA);
                speech.setProtocolId(this.protocolId.getValue0(), this.protocolId.getValue1());
                this.speechIdSet.add(speech.getId());
            } else if (elementsA.getName().equals("kommentar")) {
                agendaItemComment.add(elementsA.getText());
            } else if (elementsA.getName().equals("p") && agendaTextLabel
                    .contains(elementsA.attributeValue("klasse"))){
                agendaText.add(elementsA.getText());
            }
        }
    }

    /**
     * rea the data from mongodb document
     * through this method can get the data about agenda item
     * topId, protocolId and speechIds
     * @param agendaDocument document in mongodb that holds the relevant data about agenda item
     */
    private void init(Document agendaDocument){
        if (agendaDocument.containsKey("topId")){
            this.topId = agendaDocument.getString("topId");
        }
        if (agendaDocument.containsKey("protocolId")) {
            List<Integer> protocolId = agendaDocument.get("protocolId", ArrayList.class);
            this.protocolId = new Pair<Integer, Integer>(protocolId.get(0), protocolId.get(1));
        }
        if (agendaDocument.containsKey("speechIds")){
            this.speechIdSet.addAll(agendaDocument.getList("speechIds", String.class));
        }
    }

    /**
     * get tagesordnungspunkt id
     * @return id of the tagesordnungspunkt
     */
    @Override
    public String getId() {
                              return this.topId;
                                                }

    /**
     * set protocol id
     * @param session wahlpriode of the sitzung
     * @param term save the sitzungsnummer
     */
    @Override
    public void setProtocolId(int session, int term) {
        this.protocolId = new Pair<>(session, term);
    }

    /**
     * get protocol id
     * @return sitzungsnummer and wahlpriode
     */
    @Override
    public Pair<Integer, Integer> getProtocolId() {
        return this.protocolId;
    }

    /**
     * out put the text from agenda item
     */
    @Override
    public void printTexts() {
        for(String text : agendaText){
            System.out.println(text);
        }
    }

    @Override
    public Set<String> getSpeechIds() {
        return this.speechIdSet;
    }

//    @Override
//    public List<Speech> getSpeeches() {
//        return this.speechList;
//    }

    /**
     * save the data of the relevant data about agenda item as document type
     * @return the document that stores the data about agenda item
     */
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("topId", this.topId);
        document.append("protocolId", this.protocolId);
        document.append("speechIds", this.speechIdSet);
        return document;
    }
}
