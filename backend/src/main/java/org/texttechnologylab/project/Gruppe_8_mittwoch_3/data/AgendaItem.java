package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;
import org.javatuples.Pair;

import java.util.List;
import java.util.Set;

public interface AgendaItem {
    /**
     * getter for attribute id
     * @return id of the tagesordnungspunkt
     */
    String getId();

    void setProtocolId(int session, int term);

    Pair<Integer, Integer> getProtocolId();

    /**
     * print all texts in the tagesordnungspunkt
     * no return value needed, because all of the texts are printed directly in console
     */
    void printTexts();

    /**
     * getter for attribute Reden
     * @return list of reden in this Tagesordnungspunkt
     */
    public Set<String> getSpeechIds();

    /**
     * convert class instance to BSON Document,
     * so that it can be written to MongoDB
     * @return class instance in BSON Document form
     */
    public abstract Document toDocument();
}
