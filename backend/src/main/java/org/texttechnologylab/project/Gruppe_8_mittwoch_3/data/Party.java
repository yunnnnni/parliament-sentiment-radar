package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

public interface Party {
    /**
     * getter for attribute this.name
     * @return name of the fraktion
     */
    String getName();

    /**
     * setter for attribute this.name
     * @param name name of the fraktion
     */
    void setName(String name);

    Object toDocument();
}
