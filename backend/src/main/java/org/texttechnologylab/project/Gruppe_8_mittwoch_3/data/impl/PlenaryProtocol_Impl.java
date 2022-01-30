package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PlenaryProtocol_Impl implements PlenaryProtocol {
    private int session = -1;  // Sitzungsnummer
    private int term = -1;  // Wahlperiode
    private String titel = "";
    private String date = "";
    private LocalTime startTime = null;
    private LocalTime endTime = null;
    private List<Speaker> speakerList = new ArrayList<>();
    private List<AgendaItem> agendaItems = new ArrayList<>(0);

    public PlenaryProtocol_Impl(File xmlFile){
        try {
            SAXReader saxReader = new SAXReader();
            org.dom4j.Document document = saxReader.read(xmlFile);

            Element root = document.getRootElement();
            Element sitzungsverlauf = root.element("sitzungsverlauf");

            // -------------------------- attributes -----------------------------
            this.date = root.attributeValue("sitzung-datum");
            this.term = Integer.parseInt(root.attributeValue("wahlperiode"));
            this.session = Integer.parseInt(root.attributeValue("sitzung-nr"));
            this.startTime = LocalTime.parse(root.attributeValue("sitzung-start-uhrzeit"));
            this.endTime = LocalTime.parse(root.attributeValue("sitzung-ende-uhrzeit"));
            this.titel = this.session + ". Sitzung";

            // -------------------------- build speaker list --------------------------------
            List<Element> rednerElements = root.element("rednerliste").elements("redner");
            for (Element rednerElement : rednerElements){
                speakerList.add(new Speaker_Impl(rednerElement));
            }

            // -------------------------- build agendaItem list --------------------------------
            List<Element> tagesordnungespunktElementList = sitzungsverlauf.elements("tagesordnungspunkt");
            for (Element tagesordnungspunktElement : tagesordnungespunktElementList){
                this.agendaItems.add(new AgendaItem_Impl(tagesordnungspunktElement));
            }

            System.out.format("Finish reading %s\n", xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public List<AgendaItem> getAgendaItems() {
        return null;
    }

    @Override
    public Document toDocument() {
        return null;
    }
}
