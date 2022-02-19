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
    MongoDBConnectionHandler handler = null;
    ParliamentFactory factory = new ParliamentFactory_Impl();
    final static Logger logger = Logger.getLogger(RESTServices.class);

    public RESTServices() {
        this.handler = new MongoDBConnectionHandler("config/config.json");
        this.factory.initFromMongoDB(this.handler);
    }

    public void startServices() {
        port(5678);
        before("/*", (req, res) -> this.logger.info(req.ip() + ": " + req.uri() + "?" + req.queryString()));
        after((Filter) (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET");
        });

        get("/update-factory", (req, res) -> {
            res.type("application/json");
            this.factory = new ParliamentFactory_Impl();
            this.factory.initFromMongoDB(this.handler);
            return "updated";
        });
        get("/speech", (req, res) -> {
            res.type("application/json");
            return this.speechService(req).toJson();
        });
        get("/speeches", (req, res) -> {
            res.type("application/json");
            return this.speechesService(req).toJson();
        });
        get("/speakers", (req, res) -> {
            res.type("application/json");
            return this.speakerService(req).toJson();
        });
        get("/fractions", (req, res) -> {
            res.type("application/json");
            return this.fractionsService().toJson();
        });
        get("/fraction", (req, res) -> {
            res.type("application/json");
            return this.fractionService(req.queryParams("name")).toJson();
        });
        get("/statistic", (req, res) -> {
            res.type("application/json");
            return this.statisticService().toJson();
        });
        get("/sentiment", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "sentiments", "sentiment").toJson();
        });
        get("/pos", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "POS", "pos").toJson();
        });
        get("/tokens", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "tokens", "token").toJson();
        });
        get("/dependencies", (req, res) -> {
            res.type("application/json");
            return this.annotationService(req, "dependencies", "dependency").toJson();
        });
        get("/namedEntities", (req, res) -> {
            res.type("application/json");
            return this.namedEntitiesService(req).toJson();
        });
    }

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
     * query speech list
     * @param req: client request
     * @return list of speech ids to avoid overflow
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

    private Document fractionService(String fractionName){
        Fraction fraction= this.factory.getFraction(fractionName);
        Document fractionDocument = this.fractionToDocument(fraction);
        if (fractionDocument == null){
            return new Document();
        }
        return fractionDocument;
    }

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

    private List<Speaker> filterSpeakers(Request req){
        List<Speaker> speakerList = new ArrayList<>();
        // filter by speakerId
        String speakerId = req.queryParams("user");
        if (speakerId == null || speakerId == ""){  // if speakerId is not defined, add all speakers into list
            speakerList.addAll(this.factory.getSpeakers());
        }else{
            Speaker speakerFound = this.factory.getSpeaker(speakerId);
            if (speakerFound != null){
                speakerList.add(speakerFound);
            }
        }
        // filter by fraction
        String fractionName = req.queryParams("fraction");
        if (speakerList.size() > 0 && fractionName != null && fractionName != ""){
            speakerList = speakerList.stream()
                    .filter(speaker -> speaker.getFraction().equals(fractionName))
                    .collect(Collectors.toList());
        }
        return speakerList;
    }

    private List<Speech> filterSpeeches(Request req){
        List<Speaker> speakerList = this.filterSpeakers(req);
        Set<String> speechIdSet = new TreeSet<>();
        for (Speaker speaker: speakerList){
            speechIdSet.addAll(speaker.getSpeeches());
        }
        List<Speech> speechList = this.factory.getSpeeches(speechIdSet);
        // filter speakers by time
        // TODO: consider how to improve this function
        try{
            String timerange = req.queryParams("time");
            if (timerange != null){
                List<Speech> speechListFilteredByTime = new ArrayList<>();
                Document doc = Document.parse(timerange);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date gte = df.parse(doc.getString("gte"));
                Date lte = df.parse(doc.getString("lte"));
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
        }catch (ParseException e){
            logger.info(e);
        }catch (Exception e){
            logger.info(e);
        }
        return speechList;
    }

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
                if (text.getLabel() != "comment" && text.getLabel() != "name"){
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

    private Document namedEntitiesService(Request req){
        Document personDocu = annotationService(req, "persons", "element");
        Document organisationsDocu = annotationService(req, "organisations", "element");
        Document locationsDocu = annotationService(req, "locations", "element");
        List<Document> resultDocuments = new ArrayList<>();
        resultDocuments.add(new Document("persons", personDocu.get("result")));
        resultDocuments.add(new Document("organisations", organisationsDocu.get("result")));
        resultDocuments.add(new Document("locations", locationsDocu.get("result")));
        return packPayload(resultDocuments);
    }

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
	    if (req.queryParams("minimum") != null && req.queryParams("minimum") != ""){
            int minimum = Integer.parseInt(req.queryParams("minimum"));
            frequency = frequency.entrySet().stream()
                    .filter(x -> x.getValue() > minimum)
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
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
