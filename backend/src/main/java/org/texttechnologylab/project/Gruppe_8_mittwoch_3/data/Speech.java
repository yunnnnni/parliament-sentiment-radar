package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.javatuples.Pair;
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

    /**
     * get the speaker's id for the speech
     * @return speaker' id for the speech
     */
    String getSpeakerId();

    /**
     * set the protocol id, session and wahlpriode
     * @param session save the protocol number in mongodb
     * @param term wahlpriode of the sitzung
     */
    void setProtocolId(int session, int term);

    /**
     * get the protocol id, the protocol number in mongodb and wahlpriode of the sitzung
     * @return the protocol number in mongodb and wahlpriode of the sitzung
     */
    Pair<Integer, Integer> getProtocolId();

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

    /**
     * get the JCas after NLP analysis
     * @return analyzed JCas
     */
    JCas getCAS();

    /**
     * save the NLP analysis results in Map
     */
    void setAnnotations();

    /**
     * get the NLP analysis results
     * @return NLP analysis results in Map
     */
    Map<String, Object> getAnnotations();

    /**
     * empty jcas and reduce memory
     */
    void clearJcas();

    void setSitzungsNr(String nr);
}
