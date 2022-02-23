package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;
import org.javatuples.Pair;

import java.util.Date;
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

    String getDate();
    Date getStartDateTime();
    String getStartDateTimeStr();
    Date getEndDateTime();
    String getEndDateTimeStr();

    Pair<Integer, Integer> getProtocolId();
    /**
     * getter for a target tagesordnungspunkt in this protocol
     * @param  agendaItemId index of the tagesordnungspunkt
     * @return target tagesordnungspunkt
     */
    AgendaItem getAgendaItem(String agendaItemId);

    List<AgendaItem> getAgendaItems();

    Document toDocument();
}
