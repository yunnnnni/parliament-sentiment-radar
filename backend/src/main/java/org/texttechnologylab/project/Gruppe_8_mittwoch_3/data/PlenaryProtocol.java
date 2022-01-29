package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

import java.util.List;

public interface PlenaryProtocol {
    /**
     * getter for attribute this.rednerList
     * @return redner list in the protocol
     */
    List<Speaker> getSpeakerList();

    List<Speech> getSpeechList();

    /**
     * getter for attribute sitzungsNr
     * @return sitzungsNr of this protocol
     */
    String getPlenaryNr();

    /**
     * getter for a target tagesordnungspunkt in this protocol
     * @param numberIndex index of the tagesordnungspunkt
     * @return target tagesordnungspunkt
     */
    AgendaItem getAgendaItem(String numberIndex);

    Document toDocument();
}
