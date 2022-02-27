package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data;

import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

/**
 * interface for parliament factory
 * protocol, speaker, speech, fraction are stored uniquely in factory
 * this can avoid duplicated data
 */
public interface ParliamentFactory {

    /**
     * read xml files online, parse and save information into factory
     * source urls are defined in side of this function, not configurable
     */
    void initOnline();

    /**
     * read xml files from directory, parse and save information into factory
     * @param protocolDirectory directory with xml files
     */
    void initFromDirectory(String protocolDirectory);

    /**
     * read data from mongodb and init factory
     * used by RESTservices
     * @param handler MongoDBConnectionHandler instance
     */
    void initFromMongoDB(MongoDBConnectionHandler handler);

    /**
     * get all available protocols from factory
     * @return list of protocols
     */
    List<PlenaryProtocol> getProtocols();

    /**
     * get all available speeches from factory
     * @return list of speeches
     */
    List<Speech> getSpeeches();

    /**
     * filter speeches with a list of speechIds
     * @param speechIdList list of speech ids
     * @return list of speeches that meet the filter conditions
     */
    List<Speech> getSpeeches(Set<String> speechIdList);

    /**
     * get all available speakers
     * @return list of speakers
     */
    List<Speaker> getSpeakers();

    /**
     * filter speakers by fractionName
     * @param fractionName name of the fraction
     * @return list of speakers belong to this fraction
     */
    List<Speaker> getSpeakersOfFraction(String fractionName);

    /**
     * get all available parliament members
     * @return list of parliament members
     */
    List<Speaker> getParliamentMembers();

    /**
     * get all available other speakers
     * @return list of other speakers
     */
    List<Speaker> getOtherSpeakers();

    /**
     * get all available fractions
     * @return list of fractions
     */
    List<Fraction> getFractions();

    /**
     * get the specified plenary protocol
     * @param session save the protocol number in mongodb
     * @param term wahlpriode of the sitzung
     * @return target plenary protocol
     */
    PlenaryProtocol getProtocol(int session, int term);

    /**
     * get single speech by speechId
     * @param id id of the target speech
     * @return target speech
     */
    Speech getSpeech(String id);

    /**
     * get single speaker by speakerId
     * @param id id of the target speaker
     * @return target speaker
     */
    Speaker getSpeaker(String id);

    /**
     * get single fraction by fractionName
     * @param name name of the fraction
     * @return target fraction
     */
    Fraction getFraction(String name);

    /**
     * add Protocol to factory
     * @param protocol protocol to add
     * @return if protocol to add not exists in factory, return this new protocol
     *          if protocol already exists in factory, return the existed protocol
     */
    PlenaryProtocol addProtocol(PlenaryProtocol protocol);

    /**
     * add speech to factory
     * @param speech speech to add
     * @return if speech to add not exists in factory, return this new speech
     *          if speech already exists in factory, return the existed speech
     */
    Speech addSpeech(Speech speech);

    /**
     * add speaker to factory
     * @param speaker speaker to add
     * @return if speaker to add not exists in factory, return this new speaker
     *          if speaker already exists in factory, return the existed speaker
     */
    Speaker addSpeaker(Speaker speaker);

    /**
     * add fraction to factory
     * @param fraction fraction to add
     * @return if fraction to add not exists in factory, return this new fraction
     *          if fraction already exists in factory, return the existed fraction
     */
    Fraction addFraction(Fraction fraction);

    /**
     * add protocol to factory
     * @param xmlFile xml file of this protocol
     * @return if protocol to add not exists in factory, return this new protocol
     *          if protocol already exists in factory, return the existed protocol
     */
    PlenaryProtocol addProtocol(File xmlFile);

    /**
     * add speech to factory
     * @param speechElement element for this speech
     * @return if speech to add not exists in factory, return this new speech
     *          if speech already exists in factory, return the existed speech
     */
    Speech addSpeech(Element speechElement);

    /**
     * add speaker into factory
     * @param speakerElement element for speaker
     * @return if speaker to add not exists in factory, return this new speaker
     *          if speaker already exists in factory, return the existed speaker
     */
    Speaker addSpeaker(Element speakerElement);

    /**
     * add fraction into factory
     * @param name the name of fraction
     * @return if fraction to add not exists in factory, return this new fraction
     *          if fraction already exists in factory, return the existed fraction
     */
    Fraction addFraction(String name);

    /**
     * add protocol into factory
     * @param protocolDocument document in mongodb that holds the relevant data about plenary protocol
     * @return if protocol to add not exists in factory, return this new protocol
     *          if protocol already exists in factory, return the existed protocol
     */
    PlenaryProtocol addProtocol(Document protocolDocument);

    /**
     * add speech into factory
     * @param speechDocument document in mongodb that holds the relevant data about speech
     * @return if speech to add not exists in factory, return this new speech
     *          if speech already exists in factory, return the existed speech
     */
    Speech addSpeech(Document speechDocument);

    /**
     * add speaker into factory
     * @param speakerDocument document in mongodb that holds the relevant data about speaker
     * @return if speaker to add not exists in factory, return this new speaker
     *          if speaker already exists in factory, return the existed speaker
     */
    Speaker addSpeaker(Document speakerDocument);

    /**
     * add fraction into factory
     * @param fractionDocument document in mongodb that holds the relevant data about fraction
     * @return if fraction to add not exists in factory, return this new fraction
     *          if fraction already exists in factory, return the existed fraction
     */
    Fraction addFraction(Document fractionDocument);
}
