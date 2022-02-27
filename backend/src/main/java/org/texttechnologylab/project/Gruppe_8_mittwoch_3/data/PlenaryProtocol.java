package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;
import org.javatuples.Pair;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * interface for plenary protocols
 * @author Chufan Zhang
 */
public interface PlenaryProtocol {
    /**
     * getter for attribute this.rednerList
     * @return redner list in the protocol
     */
    Set<String> getSpeakerIdSet();

    /**
     * get the protocol number in mongodb
     * @return the protocol number in mongodb
     */
    int getSession();

    /**
     * get wahlpriode of the sitzung
     * @return wahlpriode of the sitzung
     */
    int getTerm();

    /**
     * get the meeting date
     * @return date
     */
    String getDate();

    /**
     * get the meeting start time
     * @return meeting start time
     */
    Date getStartDateTime();

    /**
     * format the start time as string
     * @return meeting start time
     */
    String getStartDateTimeStr();

    /**
     * get the meeting end time
     * @return meeting end time
     */
    Date getEndDateTime();

    /**
     * format the end time as string
     * @return meeting end time
     */
    String getEndDateTimeStr();

    /**
     * get the protocol number in mongodb
     * @return the protocol number in mongodb and wahlpriode of the sitzung
     */
    Pair<Integer, Integer> getProtocolId();

    /**
     * getter for a target tagesordnungspunkt in this protocol
     * @param  agendaItemId index of the tagesordnungspunkt
     * @return target tagesordnungspunkt
     */
    AgendaItem getAgendaItem(String agendaItemId);

    /**
     * get all agenda items for the meeting
     * @return the object list of class AgendaItem
     */
    List<AgendaItem> getAgendaItems();

    /**
     * save the data of the relevant data about plenary protocol as document type
     * @return the document that stores the data about plenary protocol
     */
    Document toDocument();
}
