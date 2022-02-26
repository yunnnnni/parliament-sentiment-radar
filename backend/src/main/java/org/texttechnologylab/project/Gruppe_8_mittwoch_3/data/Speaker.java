package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.Image_Impl;

import java.util.Set;

public interface Speaker {

    /**
     * set the speaker image
     * @param image the object of class Image_Impl
     */
    void setImage(Image_Impl image);

    /**
     * return id of the redner
     * @return id String of the redner
     */
    String getId();

    /**
     * return first name of the redner
     * @return first name
     */
    String getFirstName();

    /**
     * return last name of the redner
     * @return last name
     */
    String getLastName();

    /**
     * get full name of the redner = first name + last name
     * @return full name of the redner
     */
    String getName();

    /**
     * get titel of the redner
     * @return titel of the redner
     */
    String getTitel();

    /**
     * return fraktion name of this abgeordnete
     * @return name of the fraktion of this abgeordnete
     */
    String getFraction();

    /**
     * check if the speaker is a parliament member
     * if the id starts with 1100, it is the parliament member
     * @return judgment Results, if true, the speaker is parliament member, if false, the speaker is not parliament member
     */
    Boolean isParliamentMember();

    /**
     * save the data of the relevant data about speaker as document type
     * then can save to mongodb
     * @return the document that stores the data about speaker
     */
    Document toDocument();

    /**
     * add the speaker's speech id to speechIdSet collection
     * @param speechId speech id
     */
    void addSpeech(String speechId);

    /**
     * get the speaker's speech id
     * @return all the speaker's speech id
     */
    Set<String> getSpeeches();
}
