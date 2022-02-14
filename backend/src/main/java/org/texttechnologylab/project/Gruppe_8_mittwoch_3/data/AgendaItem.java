package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

import java.util.List;

public interface AgendaItem {
    /**
     * getter for attribute id
     * @return id of the tagesordnungspunkt
     */
    public abstract String getId();

    /**
     * print all texts in the tagesordnungspunkt
     * no return value needed, because all of the texts are printed directly in console
     */
    void printTexts();

    /**
     * getter for attribute Reden
     * @return list of reden in this Tagesordnungspunkt
     */
//    public abstract List<Speech> getSpeeches();

    /**
     * convert class instance to BSON Document,
     * so that it can be written to MongoDB
     * @return class instance in BSON Document form
     */
    public abstract Document toDocument();
}
