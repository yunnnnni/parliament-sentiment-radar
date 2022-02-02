package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

import java.util.List;

public interface PlenaryProtocol {
    /**
     * getter for attribute this.rednerList
     * @return redner list in the protocol
     */
    public abstract List<Speaker> getSpeakerList();

    public abstract List<Speech> getSpeechList();

    /**
     * getter for attribute sitzungsNr
     * @return sitzungsNr of this protocol
     */
    public abstract String getPlenaryNr();

    /**
     * getter for a target tagesordnungspunkt in this protocol
     * @param numberIndex index of the tagesordnungspunkt
     * @return target tagesordnungspunkt
     */
    public abstract AgendaItem getAgendaItem(String numberIndex);

    public abstract List<AgendaItem> getAgendaItems();

    public abstract Document toDocument();
}
