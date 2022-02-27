package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

import java.util.List;

/**
 * interface for fraction in plenary protocols
 * @author Yunni Lu
 */
public interface Fraction {
    /**
     * getter for attribute this.name
     * @return name of the fraction
     */
    String getName();

    /**
     * setter for attribute this.name
     * @param name name of the fraktion
     */
    void setName(String name);

    /**
     * store the speaker id to speakerIdSet list
     * @param speaker the object of class Speaker
     */
    void addSpeaker(Speaker speaker);

    /**
     * get the speaker id
     * @return the list about speaker id
     */
    List<String> getSpeakerIds();

    /**
     * save the data of the relevant data about fraction as document type
     * @return the document that stores the data about fraction
     */
    Document toDocument();
}
