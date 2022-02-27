package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.javatuples.Pair;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import org.dom4j.Element;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class for parliament factory
 * implements interface ParliamentFactory
 * this class is used to store speech, speaker, fraction and protocol uniquely
 * @author Yunni Lu
 */
public class ParliamentFactory_Impl implements ParliamentFactory {
    // use TreeMap to ensure data uniqueness and retrieval performance
    private Map<Pair<Integer, Integer>, PlenaryProtocol> protocolMap = new TreeMap<>();
    private Map<String, Speech> speechMap = new TreeMap<>();
    private Map<String, Speaker> speakerMap = new TreeMap<>();
    private Map<String, Speaker> parliamentMemberMap = new TreeMap<>();
    private Map<String, Speaker> otherSpeakerMap = new TreeMap<>();
    private Map<String, Fraction> fractionMap = new TreeMap<>();
    private MongoDBConnectionHandler handler;

    public ParliamentFactory_Impl(){

    }

    /**
     * read xml files online, parse and save information into factory
     * source urls are defined in side of this function, not configurable
     */
    public void initOnline(){
        // these 2 urls can be found on bundestag website, which contains protocol urls
        // https://www.bundestag.de/services/opendata
        String[] baseUrls = {
                "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410",
                "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354"
        };
        List<String> protocolUrls = Arrays.stream(baseUrls)
                .map(url -> this.parseProtocolUrls(url, 0))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        for (String protocolUrl: protocolUrls){
            System.out.println("Read protocol from: " + protocolUrl);
            PlenaryProtocol protocol = new PlenaryProtocol_Impl(protocolUrl, this);
            this.addProtocol(protocol);
        }
    }

    /**
     * read data from data table url
     * parse html content to get list of protocol urls
     * @param dataTableUrl url of the website which contains protocol xml file url
     * @param offset we can only get 10 protocol urls on one page, set offset to get more
     * @return list of protocol urls
     */
    private List<String> parseProtocolUrls(String dataTableUrl, int offset){
        org.jsoup.nodes.Document doc = Jsoup.parse(getHttpResponse(dataTableUrl + "?limit=10&offset=" + offset));
        org.jsoup.nodes.Element elem = doc.getElementsByClass("meta-slider").get(0);
        int totalCount = Integer.parseInt(elem.attr("data-hits"));  // total number of available protocols
        int nextOffset = Integer.parseInt(elem.attr("data-nextoffset"));  // next available offset
        Elements elements = doc.getElementsByClass("bt-link-dokument");
        List<String> protocolUrls = elements.stream()
                .map(e -> "https://www.bundestag.de" + e.attr("href"))
                .collect(Collectors.toList());
        // if totalCount > nextOffset, we have more protocols to read
        if (totalCount > nextOffset){
            // call this function recursively to read protocols start from the nextOffset
            // combine results together into procolUrls
            protocolUrls.addAll(parseProtocolUrls(dataTableUrl, nextOffset));
        }
        return protocolUrls;
    }

    /**
     * get http content of the given url
     * @param url target url
     * @return http content string
     */
    private String getHttpResponse(String url){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET() // GET is default
                    .build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            return response.body();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read xml files from directory, parse and save information into factory
     * @param protocolDirectory directory with xml files
     */
    @Override
    public void initFromDirectory(String protocolDirectory) {
        try{
            File d = new File(protocolDirectory);
            File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            for (File file: files){
                System.out.println("------------------------ " + file + "------------------------");
                this.addProtocol(file);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * read data from mongodb and init factory
     * used by RESTservices
     * @param handler MongoDBConnectionHandler instance
     */
    @Override
    public void initFromMongoDB(MongoDBConnectionHandler handler) {
        this.handler = handler;
        initProtocolsFromMongoDB();
        initParliamentMembersFromMongoDB();
        initOtherSpeakersFromMongoDB();
        initSpeechesFromMongoDB();
        initFractionsFromMongoDB();
    }

    /**
     * sub function to init protocols from mongoDB
     */
    private void initProtocolsFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("protocols").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addProtocol(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * sub function to init parliament members from mongoDB
     */
    private void initParliamentMembersFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("parliament_members").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addSpeaker(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * sub function to init other speakers from mongoDB
     */
    private void initOtherSpeakersFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("other_speakers").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addSpeaker(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * sub function to init speeches from mongoDB
     */
    private void initSpeechesFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("speeches").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addSpeech(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * sub function to init fractions from mongoDB
     */
    private void initFractionsFromMongoDB(){
        FindIterable<Document> iterDoc = this.handler.getCollection("fractions").find();
        Iterator it = iterDoc.iterator();
        try {
            while (it.hasNext()) {
                Document docu = (Document) it.next();
                this.addFraction(docu);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }


    /**
     * get all available protocols from factory
     * @return list of protocols
     */
    @Override
    public List<PlenaryProtocol> getProtocols() {
        List<PlenaryProtocol> protocolList = new ArrayList<PlenaryProtocol>(this.protocolMap.values());
        if (protocolList.contains(null)) {
            protocolList.removeIf(Objects::isNull);
        }
        return protocolList;
    }

    /**
     * get all available speeches from factory
     * @return list of speeches
     */
    @Override
    public List<Speech> getSpeeches() {
        List<Speech> speechList = new ArrayList<Speech>(this.speechMap.values());
        if (speechList.contains(null)) {
            speechList.removeIf(Objects::isNull);
        }
        return speechList;
    }

    /**
     * filter speeches with a list of speechIds
     * @param speechIdList list of speech ids
     * @return list of speeches that meet the filter conditions
     */
    @Override
    public List<Speech> getSpeeches(Set<String> speechIdList) {
        if (speechIdList == null){
            return null;
        }
        List<Speech> speechList = new ArrayList<>();
        for (String id: speechIdList){
            try{
                speechList.add(this.getSpeech(id));
            }catch (Exception e){
                continue;
            }
        }
        if (speechList.size() == 0){return null;}

        if (speechList.contains(null)) {
            speechList.removeIf(Objects::isNull);
        }
        return speechList;
    }

    /**
     * get all available speakers
     * @return list of speakers
     */
    @Override
    public List<Speaker> getSpeakers() {
        List<Speaker> speakerList = new ArrayList<Speaker>(this.speakerMap.values());
        if (speakerList.contains(null)) {
            speakerList.removeIf(Objects::isNull);
        }
        return speakerList;
    }

    /**
     * filter speakers by fractionName
     * @param fractionName name of the fraction
     * @return list of speakers belong to this fraction
     */
    @Override
    public List<Speaker> getSpeakersOfFraction(String fractionName) {
        Fraction fraction = this.getFraction(fractionName);
        if (fraction == null){
            return null;
        }
        List<Speaker> speakerList = new ArrayList<>();
        for (String speakerId: fraction.getSpeakerIds()){
            Speaker speaker = this.getSpeaker(speakerId);
            if (speaker != null){
                speakerList.add(speaker);
            }
        }
        if (speakerList.size() == 0){
            return null;
        }
        return speakerList;
    }

    /**
     * get all available parliament members
     * @return list of parliament members
     */
    @Override
    public List<Speaker> getParliamentMembers() {
        List<Speaker> speakerList = new ArrayList<Speaker>(this.parliamentMemberMap.values());
        if (speakerList.contains(null)) {
            speakerList.removeIf(Objects::isNull);
        }
        return speakerList;
    }

    /**
     * get all available other speakers
     * @return list of other speakers
     */
    @Override
    public List<Speaker> getOtherSpeakers() {
        List<Speaker> speakerList = new ArrayList<Speaker>(this.otherSpeakerMap.values());
        if (speakerList.contains(null)) {
            speakerList.removeIf(Objects::isNull);
        }
        return speakerList;
    }

    /**
     * get all available fractions
     * @return list of fractions
     */
    @Override
    public List<Fraction> getFractions() {
        List<Fraction> fractionList = new ArrayList<Fraction>(this.fractionMap.values());
        if (fractionList.contains(null)) {
            fractionList.removeIf(Objects::isNull);
        }
        return fractionList;
    }

    /**
     * get the specified plenary protocol
     * @param session save the protocol number in mongodb
     * @param term wahlpriode of the sitzung
     * @return target plenary protocol
     */
    @Override
    public PlenaryProtocol getProtocol(int session, int term) {
        try{
            return this.protocolMap.get(new Pair<>(session, term));
        } catch (Exception e){
            return null;
        }
    }

    /**
     * get single speech by speechId
     * @param id id of the target speech
     * @return target speech
     */
    @Override
    public Speech getSpeech(String id) {
        try{
            return this.speechMap.get(id);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * get single speaker by speakerId
     * @param id id of the target speaker
     * @return target speaker
     */
    @Override
    public Speaker getSpeaker(String id) {
        try{
            return this.speakerMap.get(id);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * get single fraction by fractionName
     * @param name name of the fraction
     * @return target fraction
     */
    @Override
    public Fraction getFraction(String name) {
        try{
            return this.fractionMap.get(name);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * add Protocol to factory
     * @param protocol protocol to add
     * @return if protocol to add not exists in factory, return this new protocol
     *          if protocol already exists in factory, return the existed protocol
     */
    @Override
    public PlenaryProtocol addProtocol(PlenaryProtocol protocol) {
        PlenaryProtocol existedProtocol = this.getProtocol(protocol.getSession(), protocol.getTerm());
        if (existedProtocol == null){
            this.protocolMap.put(new Pair<>(protocol.getSession(),protocol.getTerm()), protocol);
            return protocol;
        }
        return existedProtocol;
    }

    /**
     * add speech to factory
     * @param speech speech to add
     * @return if speech to add not exists in factory, return this new speech
     *          if speech already exists in factory, return the existed speech
     */
    @Override
    public Speech addSpeech(Speech speech) {
        Speech existedSpeech = this.getSpeech(speech.getId());
        if (existedSpeech == null){
            this.speechMap.put(speech.getId(), speech);
            String speakerId = speech.getSpeakerId();
            if (this.speakerMap.get(speakerId)!= null){
                this.speakerMap.get(speakerId).addSpeech(speech.getId());  // assign speech to speaker
            }
            return speech;
        }
        return existedSpeech;
    }

    /**
     * add speaker to factory
     * @param speaker speaker to add
     * @return if speaker to add not exists in factory, return this new speaker
     *          if speaker already exists in factory, return the existed speaker
     */
    @Override
    public Speaker addSpeaker(Speaker speaker) {
        Speaker existedSpeaker = this.getSpeaker(speaker.getId());
        if (existedSpeaker == null){
            this.speakerMap.put(speaker.getId(), speaker);
            if (speaker.isParliamentMember()){
                this.parliamentMemberMap.put(speaker.getId(), speaker);
            } else{
                this.otherSpeakerMap.put(speaker.getId(), speaker);
            }
            return speaker;
        }
        return existedSpeaker;
    }

    /**
     * add fraction to factory
     * @param fraction fraction to add
     * @return if fraction to add not exists in factory, return this new fraction
     *          if fraction already exists in factory, return the existed fraction
     */
    @Override
    public Fraction addFraction(Fraction fraction) {
        Fraction existedFraction = this.getFraction(fraction.getName());
        if (existedFraction == null){
            this.fractionMap.put(fraction.getName(),fraction);
            return fraction;
        }
        return existedFraction;
    }

    /**
     * add protocol to factory
     * @param xmlFile xml file of this protocol
     * @return if protocol to add not exists in factory, return this new protocol
     *          if protocol already exists in factory, return the existed protocol
     */
    @Override
    public PlenaryProtocol addProtocol(File xmlFile) {
        PlenaryProtocol protocol = new PlenaryProtocol_Impl(xmlFile, this);
        return this.addProtocol(protocol);
    }

    /**
     * add speech to factory
     * @param speechElement element for this speech
     * @return if speech to add not exists in factory, return this new speech
     *          if speech already exists in factory, return the existed speech
     */
    @Override
    public Speech addSpeech(Element speechElement) {
        Speech speech = new Speech_Impl(speechElement, this);
        return this.addSpeech(speech);
    }

    /**
     * add speaker into factory
     * @param speakerElement element for speaker
     * @return if speaker to add not exists in factory, return this new speaker
     *          if speaker already exists in factory, return the existed speaker
     */
    @Override
    public Speaker addSpeaker(Element speakerElement) {
        Speaker speaker = new Speaker_Impl(speakerElement, this);
        return this.addSpeaker(speaker);
    }

    /**
     * add fraction into factory
     * @param name the name of fraction
     * @return if fraction to add not exists in factory, return this new fraction
     *          if fraction already exists in factory, return the existed fraction
     */
    @Override
    public Fraction addFraction(String name) {
        Fraction fraction = new Fraction_Impl(name, this);
        return this.addFraction(fraction);
    }

    /**
     * add protocol into factory
     * @param protocolDocument document in mongodb that holds the relevant data about plenary protocol
     * @return if protocol to add not exists in factory, return this new protocol
     *          if protocol already exists in factory, return the existed protocol
     */
    @Override
    public PlenaryProtocol addProtocol(Document protocolDocument) {
        PlenaryProtocol protocol = new PlenaryProtocol_Impl(protocolDocument, this);
        return this.addProtocol(protocol);
    }

    /**
     * add speech into factory
     * @param speechDocument document in mongodb that holds the relevant data about speech
     * @return if speech to add not exists in factory, return this new speech
     *          if speech already exists in factory, return the existed speech
     */
    @Override
    public Speech addSpeech(Document speechDocument) {
        Speech speech = new Speech_Impl(speechDocument, this);
        return this.addSpeech(speech);
    }

    /**
     * add speaker into factory
     * @param speakerDocument document in mongodb that holds the relevant data about speaker
     * @return if speaker to add not exists in factory, return this new speaker
     *          if speaker already exists in factory, return the existed speaker
     */
    @Override
    public Speaker addSpeaker(Document speakerDocument) {
        Speaker speaker = new Speaker_Impl(speakerDocument, this);
        return this.addSpeaker(speaker);
    }

    /**
     * add fraction into factory
     * @param fractionDocument document in mongodb that holds the relevant data about fraction
     * @return if fraction to add not exists in factory, return this new fraction
     *          if fraction already exists in factory, return the existed fraction
     */
    @Override
    public Fraction addFraction(Document fractionDocument) {
        Fraction fraction = new Fraction_Impl(fractionDocument, this);
        return this.addFraction(fraction);
    }
}
