package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.CasSerializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.SerializerInitializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.UnknownFactoryException;

import java.util.List;
import java.util.Map;

public interface Speech {
    /**
     * get id of this Rede
     * @return id
     */
    String getId();

    /**
     * get redner of this rede
     * @return Redner
     */
    Speaker getSpeaker();
    String getSpeakerId();

    /**
     * getter function for attribute this.texts
     * @return return all texts in the Rede
     */
    List<Text> getTexts();

    /**
     * convert class instance to BSON Document
     * jcas object is included
     * @return bson document
     * @throws SerializerInitializationException
     * @throws UnknownFactoryException
     * @throws CasSerializationException
     */
    // Document toDocument() throws SerializerInitializationException, UnknownFactoryException, CasSerializationException;
    Document toDocument();

    /**
     * convert text strings in the Rede to jcas object
     * process jcas object with defined pipeline
     * @return
     */
    JCas toCAS();

    JCas getCAS();

    void setAnnotations();

    Map<String, Object> getAnnotations();

    void clearJcas();

    void setSitzungsNr(String nr);
}
