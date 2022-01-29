package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

public interface Speaker {
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
    String getFraktion();

    Boolean isParliamentMember();

    Document toDocument();
}
