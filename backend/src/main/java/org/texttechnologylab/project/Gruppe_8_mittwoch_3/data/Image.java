package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;

/**
 * interface for image in plenary protocols
 * @author Yunni Lu
 */
public interface Image {
    /**
     * get url of this image
     * @return image url
     */
    String getUrl();

    /**
     * get description of this image
     * @return image descriptionS
     */
    String getDescription();

    /**
     * convert image to mongodb document
     * @return mongodb document
     */
    Document toDocument();
}
