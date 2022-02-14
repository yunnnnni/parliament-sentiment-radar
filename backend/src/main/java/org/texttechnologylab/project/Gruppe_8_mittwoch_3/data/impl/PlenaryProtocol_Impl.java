package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlenaryProtocol_Impl implements PlenaryProtocol {
    private int session = -1;  // Sitzungsnummer
    private int term = -1;  // Wahlperiode
    private String titel = "";
    private String date = "";
    private LocalTime startTime = null;
    private LocalTime endTime = null;
    private Set<String> speakerIdSet = new TreeSet<>();
    private List<AgendaItem> agendaItems = new ArrayList<>(0);
    private ParliamentFactory factory = null;

    public PlenaryProtocol_Impl(File xmlFile, ParliamentFactory factory){
        this.factory = factory;
        this.init(xmlFile);
    }

    public PlenaryProtocol_Impl(File xmlFile){
        this.init(xmlFile);
    }

    private void init(File xmlFile){
        try {
            SAXReader saxReader = new SAXReader();
            org.dom4j.Document document = saxReader.read(xmlFile);

            Element root = document.getRootElement();
            Element sitzungsverlauf = root.element("sitzungsverlauf");

            // -------------------------- attributes -----------------------------
            this.date = root.attributeValue("sitzung-datum");
            this.term = Integer.parseInt(root.attributeValue("wahlperiode"));
            this.session = Integer.parseInt(root.attributeValue("sitzung-nr"));
            try{
                this.startTime = this.formatTime(root.attributeValue("sitzung-start-uhrzeit"));
                this.endTime = this.formatTime(root.attributeValue("sitzung-ende-uhrzeit"));
            } catch (Exception e){
                e.printStackTrace();
            }
            this.titel = this.session + ". Sitzung";

            // -------------------------- build speaker list --------------------------------
            List<Element> rednerElements = root.element("rednerliste").elements("redner");
            for (Element rednerElement : rednerElements){
                Speaker speaker = this.factory.addSpeaker(rednerElement);
                this.speakerIdSet.add(speaker.getId());
            }

            // -------------------------- build agendaItem list --------------------------------
            List<Element> tagesordnungespunktElementList = sitzungsverlauf.elements("tagesordnungspunkt");
            for (Element tagesordnungspunktElement : tagesordnungespunktElementList){
                this.agendaItems.add(new AgendaItem_Impl(tagesordnungspunktElement, this.factory));
            }

            System.out.format("Finish reading %s\n", xmlFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocalTime formatTime(String timeStr){
        timeStr = timeStr.replace(" Uhr", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        if (timeStr.contains(".")){
            formatter = DateTimeFormatter.ofPattern("H.mm");
        }
        try{
            LocalTime time = LocalTime.parse(timeStr, formatter);
            return time;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Set<String> getSpeakerIdSet() {
        return this.speakerIdSet;
    }

    @Override
    public int getSession() {
        return this.session;
    }

    @Override
    public int getTerm() {
        return this.term;
    }

    @Override
    public AgendaItem getAgendaItem(String numberIndex) {
        return null;
    }

    @Override
    public List<AgendaItem> getAgendaItems() {
        return this.agendaItems;
    }

    @Override
    public Document toDocument() {
        return null;
    }
}
