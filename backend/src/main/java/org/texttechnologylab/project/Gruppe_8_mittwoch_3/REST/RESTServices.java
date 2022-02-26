package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.javatuples.Pair;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.ParliamentFactory_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import spark.Filter;
import spark.Request;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class RESTServices {
    MongoDBConnectionHandler handler;
    ParliamentFactory factory = new ParliamentFactory_Impl();
    final Logger logger = Logger.getLogger(RESTServices.class);

    /**
     * constructor for RESTServices
     * read data from nongodb, init parliament factory
     */
    public RESTServices() {
        this.handler = new MongoDBConnectionHandler("config/config.json");
        this.factory.initFromMongoDB(this.handler);
    }

    /**
     * set routes to start REST services
     */
    public void startServices() {
        port(5678);
        // debug output
        before("/*", (req, res) -> this.logger.info(req.ip() + ": " + req.uri() + "?" + req.queryString()));
        // allow cross origin request
        after((Filter) (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET");
        });
        // set service to force renew parliament factory
        // so that the frontend developers can have the newest data in mongodb
        // without manually restart the server
        get("/update-factory", (req, res) -> {
            res.type("application/json");
            this.factory = new ParliamentFactory_Impl();
            this.factory.initFromMongoDB(this.handler);
            return "updated";
        });
        // get certain protocol with term and session
        get("/protocol", (req, res) -> {
            res.type("application/json");
            return this.protocolService(req).toJson();
        });
        // get all available protocols
        get("/protocols", (req, res) -> {
            res.type("application/json");
            return this.protocolsService().toJson();
        });
        // get certain agendaItem with protocolId and agendaItemId
        get("/agendaitem", (req, res) -> {
            res.type("application/json");
            return this.agendaItemService(req).toJson();
        });
        // get agendaItems of a certain protocol
        get("/agendaitems", (req, res) -> {
            res.type("application/json");
            return this.agendaItemsService(req).toJson();
        });
        // get certain speech with speechId
        get("/speech", (req, res) -> {
            res.type("application/json");
            return this.speechService(req).toJson();
        });
        // get speeches with the given conditions
        get("/speeches", (req, res) -> {
            res.type("application/json");
            return this.speechesService(req).toJson();
        });
        // get speakers with the given conditions
        get("/speakers", (req, res) -> {
            res.type("application/json");
            return this.speakerService(req).toJson();
        });
        // get fractions with the given conditions
        get("/fractions", (req, res) -> {
            res.type("application/json");
            return this.fractionsService().toJson();
        });
        // get certain fraction with fraction name
        get("/fraction", (req, res) -> {
            res.type("application/json");
            return this.fractionService(req.queryParams("name")).toJson();
        });
        // get statstics of speakers and speeches
        get("/statistic", (req, res) -> {
            res.type("application/json");
            return this.statisticService().toJson();
        });
        // get sentiment with the given conditions
        get("/sentiment", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "sentiments", "sentiment").toJson();
        });
        // get pos with the given conditions
        get("/pos", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "POS", "pos").toJson();
        });
        // get tokens with the given conditions
        get("/tokens", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "tokens", "token").toJson();
        });
        // get dependencies with the given conditions
        get("/dependencies", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "dependencies", "dependency").toJson();
        });
        // get namedEntities with the given conditions
        get("/namedEntities", (req, res) -> {
            res.type("application/json");
            return this.namedEntitiesService(req).toJson();
        });
    }

    /**
     * service to get certain protocol
     * @param req request sent by the user
     * @return Document for the protocol
     */
    private Document protocolService(Request req){
        try{
            int session = Integer.parseInt(req.queryParams("session"));
            int term = Integer.parseInt(req.queryParams("term"));
            PlenaryProtocol protocol = this.factory.getProtocol(session, term);
            return protocolToDocument(protocol);
        } catch (Exception e){
            return new Document();
        }
    }

    /**
     * service to get list of available protocols
     * @return Document
     */
    private Document protocolsService(){
        try{
            List<PlenaryProtocol> protocolList = this.factory.getProtocols();
            List<Document> protocolDocumentList = new ArrayList<>();
            for (PlenaryProtocol protocol: protocolList){
                Document protocolDocument = protocolToDocument(protocol);
                if (!protocolDocument.isEmpty()){
                    protocolDocumentList.add(protocolDocument);
                }
            }
//            protocolDocumentList.removeAll(Collections.singleton(null));
            return this.packPayload(protocolDocumentList);
        } catch (Exception e){
            List<Document> emptyDocumentList = new ArrayList<>();
            return this.packPayload(emptyDocumentList);
        }
    }

    /**
     * service to get certain agendaItem
     * @param req client request
     * @return Document for this agendaItem
     */
    private Document agendaItemService(Request req){
        try{
            int session = Integer.parseInt(req.queryParams("session"));
            int term = Integer.parseInt(req.queryParams("term"));
            String agendaItemId = req.queryParams("id");
            PlenaryProtocol protocol = this.factory.getProtocol(session, term);
            AgendaItem agendaItem = protocol.getAgendaItem(agendaItemId);
            return agendaItemToDocument(agendaItem);
        } catch (Exception e){
            return new Document();
        }
    }

    /**
     * service to get agendaItems of a certain protocol
     * @param req client request
     * @return Document of these agendaItems
     */
    private Document agendaItemsService(Request req){
        try{
            int session = Integer.parseInt(req.queryParams("session"));
            int term = Integer.parseInt(req.queryParams("term"));
            PlenaryProtocol protocol = this.factory.getProtocol(session, term);
            List<Document> agendaItemDocumentList = new ArrayList<>();
            for (AgendaItem agendaItem: protocol.getAgendaItems()){
                Document agendaItemDocument = agendaItemToDocument(agendaItem);
                if (!agendaItemDocument.isEmpty()){
                    agendaItemDocumentList.add(agendaItemDocument);
                }
            }
            return this.packPayload(agendaItemDocumentList);
        } catch (Exception e){
            List<Document> emptyDocumentList = new ArrayList<>();
            return this.packPayload(emptyDocumentList);
        }
    }

    /**
     * convert protocol instance into document
     * @param protocol protocol instance
     * @return Document for this protocol
     */
    private Document protocolToDocument(PlenaryProtocol protocol){
        try{
            Document document = new Document();
            document.append("date", protocol.getDate());
            document.append("startTime", protocol.getStartDateTimeStr());
            document.append("endTime", protocol.getEndDateTimeStr());
//            document.append("protocolId", protocol.getProtocolId());
            document.append("session", protocol.getSession());
            document.append("term", protocol.getTerm());
            List<String> agendaItemIds = protocol.getAgendaItems().stream().map(AgendaItem::getId).collect(Collectors.toList());
            agendaItemIds.removeAll(Collections.singleton(null));
            document.append("agendaItemIds", agendaItemIds);
            return document;
        } catch (Exception e){
            return new Document();
        }

    }

    /**
     * convert agendaItem instance into document
     * @param agendaItem agendaItem instance
     * @return Document for this agendaItem
     */
    private Document agendaItemToDocument(AgendaItem agendaItem){
        try{
            Document document = new Document();
            document.append("id", agendaItem.getId());
            document.append("protocolId", agendaItem.getProtocolId());
            document.append("speechIds", agendaItem.getSpeechIds());
            return document;
        } catch (Exception e){
            return new Document();
        }
    }

    /**
     * service to get certain speech
     * @param req client request
     * @return Document for this speech
     */
    private Document speechService(Request req) {
        String speechId= req.queryParams("id");
        Speech speech = this.factory.getSpeech(speechId);
        if (speech == null){
            return new Document();
        }
        Document document = new Document();
        Document speechDocument = speech.toDocument();
        try{
            Pair<Integer, Integer> protocolId = speech.getProtocolId();
            PlenaryProtocol protocol = this.factory.getProtocol(protocolId.getValue0(), protocolId.getValue1());
            document.append("date", protocol.getDate());
        }catch (Exception e){
            logger.info("speechService - " + e);
        }
        document.append("protocolId", speech.getProtocolId());
        document.append("speaker", speech.getSpeakerId());
        document.append("id", speech.getId());
        document.append("texts", speechDocument.get("texts"));
        document.append("annotations", speechDocument.get("annotations"));

        return document;
    }

    /**
     * service to get speeches
     * @param req: client request
     * @return Document which contains speech information
     */
    private Document speechesService(Request req){
        // query speech list
        List<Speech> speechList = this.filterSpeeches(req);
        Set<String> speechIdSet = new TreeSet<>();
        for (Speech speech: speechList){
            speechIdSet.add(speech.getId());
        }
        Document docu = new Document();
        docu.append("result", speechIdSet);
        if (speechIdSet.size() == 0){
            docu.append("success", false);
        }else{
            docu.append("success", true);
        }
        return docu;
    }

    /**
     * service to get all fractions
     * no parameters are needed
     * @return Document which contains fraction information
     */
    private Document fractionsService(){
        try{
            List<Document> fractionDocuments = new ArrayList<>();
            for (Fraction fraction: this.factory.getFractions()){
                Document fractionDocument = this.fractionToDocument(fraction);
                if (fractionDocument != null){
                    fractionDocuments.add(fractionDocument);
                }
            }
            return this.packPayload(fractionDocuments);
        } catch (Exception e){
            List<Document> emptyDocumentList = new ArrayList<>();
            return this.packPayload(emptyDocumentList);
        }
    }

    /**
     * service to get certain fraction
     * @param fractionName name of this fraction
     * @return Document for this fraction
     */
    private Document fractionService(String fractionName){
        Fraction fraction= this.factory.getFraction(fractionName);
        Document fractionDocument = this.fractionToDocument(fraction);
        if (fractionDocument == null){
            return new Document();
        }
        return fractionDocument;
    }

    /**
     * convert fraction instance into document
     * @param fraction fraction instance
     * @return Document of this fraction
     */
    private Document fractionToDocument(Fraction fraction){
        try{
            Document document = new Document();
            document.append("id", fraction.getName());
            document.append("members", fraction.getSpeakerIds().size());
            document.append("memberIds", fraction.getSpeakerIds());
            return document;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * service to get certain speaker
     * @param req request sent by the user
     * @return Document for this speaker
     */
    private Document speakerService(Request req){
        // query speaker
        List<Speaker> speakerList = this.filterSpeakers(req);

        List<Document> speakerDocuments = new ArrayList<>();
        try{
            for (Speaker speaker: speakerList){
                if (speaker == null){continue;}
                Document speakerDocument = speaker.toDocument();
                speakerDocument.remove("_id");
                speakerDocuments.add(speakerDocument);
            }
        } catch (Exception e){
            List<Document> emptyDocumentList = new ArrayList<>();
            return this.packPayload(emptyDocumentList);
        }
        return this.packPayload(speakerDocuments);
    }

    /**
     * get and filter speakers with the given query conditions
     * @param req request sent by the user
     * @return List of speakers
     */
    private List<Speaker> filterSpeakers(Request req){
        List<Speaker> speakerList = new ArrayList<>();
        // filter by speakerId
        String speakerId = req.queryParams("user");
        // if speakerId is not defined, add all speakers into list
        if (speakerId == null || speakerId.equals("")){
            speakerList.addAll(this.factory.getSpeakers());
        // if speakerId is defined, find this speaker and add it into list
        }else{
            Speaker speakerFound = this.factory.getSpeaker(speakerId);
            if (speakerFound != null){
                speakerList.add(speakerFound);
            }
        }
        // filter speakers by fraction
        String fractionName = req.queryParams("fraction");
        if (speakerList.size() > 0 && fractionName != null && !fractionName.equals("")){
            speakerList = speakerList.stream()
                    .filter(speaker -> speaker.getFraction().equals(fractionName))
                    .collect(Collectors.toList());
        }
        return speakerList;
    }

    /**
     * get and filter speeches with the given query conditions
     * @param req request sent by the user
     * @return List of speeches
     */
    private List<Speech> filterSpeeches(Request req){
        List<Speaker> speakerList = this.filterSpeakers(req);
        Set<String> speechIdSet = new TreeSet<>();
        for (Speaker speaker: speakerList){
            speechIdSet.addAll(speaker.getSpeeches());
        }
        List<Speech> speechList = this.factory.getSpeeches(speechIdSet);
        // filter speakers by time
        try{
            String timeStart = req.queryParams("time[gte]");
            String timeEnd = req.queryParams("time[lte]");
            if ((timeStart != null && !timeStart.equals("")) || (timeEnd != null && !timeEnd.equals(""))){
                List<Speech> speechListFilteredByTime = new ArrayList<>();
                Date gte = new Date(0); // Default date and time
                Date lte = new Date(); // Today's date and current time
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                if (timeStart != null && !timeStart.equals("")){gte = df.parse(timeStart);}
                if (timeEnd != null && !timeEnd.equals("")){lte = df.parse(timeEnd);}
                logger.info("QueryParams: gte: " + gte + ", lte: " + lte);
                for (Speech speech: speechList){
                    Pair<Integer, Integer> protocolId = speech.getProtocolId();
                    PlenaryProtocol protocol = this.factory.getProtocol(protocolId.getValue0(), protocolId.getValue1());
                    Date protocolStartTime = protocol.getStartDateTime();
                    Date protocolEndDateTime = protocol.getEndDateTime();
                    if (protocolStartTime.after(gte) && protocolEndDateTime.before(lte)){
                        speechListFilteredByTime.add(speech);
                    }
                }
                return speechListFilteredByTime;
            }
        // error handling
        }catch (ParseException e){
            logger.info(e);
        }
        return speechList;
    }

    /**
     * service to get statistics for speeches and speakers
     * no query conditions are allowed
     * @return Document for the statistics
     */
    private Document statisticService(){
        Document document = new Document();
        // get statistic of speakers
        List<Document> speakerStatistics = new ArrayList<>();
        for (Speaker speaker: this.factory.getSpeakers()){
            Document docu = new Document();
            docu.append("count", speaker.getSpeeches().size());
            docu.append("id", speaker.getId());
            speakerStatistics.add(docu);
        }
        document.append("speakers", speakerStatistics);
        // get statistic of speeches
        List<Document> speechStatistics = new ArrayList<>();
        for (Speech speech: this.factory.getSpeeches()){
            int length = 0;
            for (Text text: speech.getTexts()){
                if (!text.getLabel().equals("comment") && !text.getLabel().equals("name")){
                    length += text.getText().length();
                }
            }
            Document docu = new Document();
            docu.append("length", length);
            docu.append("id", speech.getId());
            speechStatistics.add(docu);
        }
        document.append("speeches", speechStatistics);

        return this.packPayload(document);
    }

    /**
     * service to get namedEntities
     * @param req request sent by the user
     * @return Document for the namedEntities
     */
    private Document namedEntitiesService(Request req){
        // use annotationService to get persons, organisations, locations
        Document personDocu = annotationService(req, "persons", "element");
        Document organisationsDocu = annotationService(req, "organisations", "element");
        Document locationsDocu = annotationService(req, "locations", "element");
        // pack them together
        List<Document> resultDocuments = new ArrayList<>();
        resultDocuments.add(new Document("persons", personDocu.get("result")));
        resultDocuments.add(new Document("organisations", organisationsDocu.get("result")));
        resultDocuments.add(new Document("locations", locationsDocu.get("result")));
        return packPayload(resultDocuments);
    }

    /**
     * service to get annotations
     * @param req request with query conditions
     * @param annotationType type of the annotation to return
     * @param outputAnnotationType name of the annotation in output can be defined separately with this param
     * @return Document with desired annotation data
     */
    private Document annotationService(Request req, String annotationType, String outputAnnotationType){
        // filter speeches by query
        List<Speech> speechList = this.filterSpeeches(req);
        // get frequency of each tag
        Map<Object, Integer> frequency = new TreeMap<>();
        for (Speech speech: speechList){
            try{
                List<Object> annotations = (List<Object>) speech.getAnnotations().get(annotationType);
                for (Object annotation: annotations){
                    Integer count = frequency.get(annotation);
                    if (count != null){
                        frequency.put(annotation, ++count);
                    }else{
                        frequency.put(annotation, 1);
                    }
                }
            }catch (Exception e){
                logger.debug(e);
            }
        }
        // filter annotations by minimum frequency
	    if (req.queryParams("minimum") != null && !req.queryParams("minimum").equals("")){
            int minimum = Integer.parseInt(req.queryParams("minimum"));
            frequency = frequency.entrySet().stream()
                    .filter(x -> x.getValue() > minimum)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        // create payload, map to document list
        List<Document> results = new ArrayList<>();
        for (Map.Entry<Object, Integer> entry: frequency.entrySet()){
            Document docu = new Document();
            docu.append(outputAnnotationType, entry.getKey());
            docu.append("count", entry.getValue());
            results.add(docu);
        }
        return this.packPayload(results);
    }

    /**
     * pack payload to make sure that return values of each service have consistent format
     * @param payload Document which contains desired data
     * @return Document with data and process status
     */
    private Document packPayload(List<Document> payload){
        Document docu = new Document();
        docu.append("result", payload);
        if (payload == null || payload.size() == 0){
            docu.append("success", false);
        }else{
            docu.append("success", true);
        }
        return docu;
    }

    /**
     * pack payload to make sure that return values of each service have consistent format
     * @param payload Document which contains desired data
     * @return Document with data and process status
     */
    private Document packPayload(Document payload){
        Document docu = new Document();
        docu.append("result", payload);
        if (payload == null){
            docu.append("success", false);
        }else{
            docu.append("success", true);
        }
        return docu;
    }
}
