package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

/**
 * interface for texts in plenary protocols
 */
public interface Text {
    /**
     * getter for attribute this.text
     * @return text content of the TextObject
     */
    String getText();

    /**
     * getter for attribute this.label
     * @return label of the TextObject
     */
    String getLabel();

    /**
     * convert class instance to bson document
     * @return bson document
     */
    Document toDocument();
}
