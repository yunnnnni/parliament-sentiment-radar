package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.apache.uima.ruta.type.html.P;
import org.bson.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.elasticsearch.action.ingest.IngestActionForwarder;
import org.javatuples.Pair;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PlenaryProtocol_Impl implements PlenaryProtocol {
    private int session = -1;  // Sitzungsnummer
    private int term = -1;  // Wahlperiode
    private String titel = "";
    private String date = "";
    private Date startTime = null;
    private Date endTime = null;
    private Set<String> speakerIdSet = new TreeSet<>();
    private List<AgendaItem> agendaItems = new ArrayList<>(0);
    private ParliamentFactory factory = null;

    public PlenaryProtocol_Impl(Document protocolDocument, ParliamentFactory factory){
        this.factory = factory;
        this.init(protocolDocument);
    }

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
            this.startTime = this.formatTime(this.date, root.attributeValue("sitzung-start-uhrzeit"));
            this.endTime = this.formatTime(this.date, root.attributeValue("sitzung-ende-uhrzeit"));
            this.titel = this.session + ". Sitzung";

            // -------------------------- build speaker list --------------------------------
            List<Element> rednerElements = root.element("rednerliste").elements("redner");
            for (Element rednerElement : rednerElements){
                Speaker speaker = this.factory.addSpeaker(rednerElement);
                this.speakerIdSet.add(speaker.getId());
            }

            // -------------------------- build agendaItem list --------------------------------
            List<Element> tagesordnungespunktElementList = sitzungsverlauf.elements("tagesordnungspunkt");
            Pair<Integer, Integer> protocolId = new Pair<>(this.session, this.term);
            for (Element tagesordnungspunktElement : tagesordnungespunktElementList){
                AgendaItem item = new AgendaItem_Impl(tagesordnungspunktElement, protocolId, this.factory);
                this.agendaItems.add(item);
            }

            System.out.format("Finish reading %s\n", xmlFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Document protocolDocument){
        this.session = protocolDocument.getInteger("session");
        this.term = protocolDocument.getInteger("term");
        this.titel = protocolDocument.getString("titel");
        this.date = protocolDocument.getString("date");
        this.startTime = protocolDocument.get("startTime", Date.class);
        this.endTime = protocolDocument.get("endTime", Date.class);
        this.speakerIdSet.addAll(protocolDocument.getList("speakerIds", String.class));

        this.agendaItems = protocolDocument.getList("agendaItems", Document.class)
                .stream()
                .map(d -> new AgendaItem_Impl(d, this.factory))
                .collect(Collectors.toList());
    }

//    private Date formatDate(String dateStr){
//        SimpleDateFormat isoFormat = new SimpleDateFormat("dd.MM.yyyy");
////        isoFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
//        try {
//            Date date = isoFormat.parse(dateStr);
//            return date;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private LocalDate formatDate(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try{
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Date formatTime(String dateStr, String timeStr){
        timeStr = timeStr.replace(" Uhr", "");
        String dateTimeStr = dateStr + "-" + timeStr;
        SimpleDateFormat isoFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm");
        if (timeStr.contains(".")){
            isoFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm");
        }
        isoFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        try {
            Date dateTime = isoFormat.parse(dateTimeStr);
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date formatTime2(String timeStr){
        timeStr = timeStr.replace(" Uhr", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        if (timeStr.contains(".")){
            formatter = DateTimeFormatter.ofPattern("H.mm");
        }
        try{
            LocalTime time = LocalTime.parse(timeStr, formatter);
//            return Date.from(time.atDate(this.date).atZone(ZoneId.of("Europe/Berlin")).toInstant());
            return null;
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
    public String getDate() {
        return this.date;
    }

    @Override
    public Date getStartDateTime() {
        return this.startTime;
    }

    @Override
    public String getStartDateTimeStr() {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        return sdf.format(this.startTime);
    }

    @Override
    public Date getEndDateTime() {
        return this.endTime;
    }

    @Override
    public String getEndDateTimeStr() {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        return sdf.format(this.endTime);
    }

    @Override
    public Pair<Integer, Integer> getProtocolId() {
        return new Pair<>(this.session, this.term);
    }

    @Override
    public AgendaItem getAgendaItem(String agendaItemId) {
        for (AgendaItem item: this.agendaItems){
            if (item.getId().equals(agendaItemId)){
                return item;
            }
        }
        return null;
    }

    @Override
    public List<AgendaItem> getAgendaItems() {
        return this.agendaItems;
    }

    @Override
    public Document toDocument() {
        org.bson.Document document = new org.bson.Document();
        document.append("session", this.session);
        document.append("term", this.term);
        document.append("titel", this.titel);
        document.append("date", this.date);
        document.append("startTime", this.startTime);
        document.append("endTime", this.endTime);
        document.append("speakerIds", this.speakerIdSet);
        List<org.bson.Document> agendaItemDocuments = new ArrayList<>();
        for (AgendaItem item: this.agendaItems){
            agendaItemDocuments.add(item.toDocument());
        }
        document.append("agendaItems", agendaItemDocuments);
        return document;
    }
}
