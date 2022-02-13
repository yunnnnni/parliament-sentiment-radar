package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

import java.util.List;
import java.util.Set;

public interface PlenaryProtocol {
    /**
     * getter for attribute this.rednerList
     * @return redner list in the protocol
     */
    Set<String> getSpeakerIdSet();

    int getSession();

    int getTerm();

    /**
     * getter for a target tagesordnungspunkt in this protocol
     * @param numberIndex index of the tagesordnungspunkt
     * @return target tagesordnungspunkt
     */
    AgendaItem getAgendaItem(String numberIndex);

    List<AgendaItem> getAgendaItems();

    Document toDocument();
}
