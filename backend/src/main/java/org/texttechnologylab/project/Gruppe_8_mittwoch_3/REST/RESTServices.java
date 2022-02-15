package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.*;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.ParliamentFactory_Impl;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.database.MongoDBConnectionHandler;
import spark.Filter;
import spark.Request;

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
        before("/*", (req, res) -> this.logger.info(req.ip() + ": " + req.uri() + "?" + req.queryString()));
        after((Filter) (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET");
        });

        get("/update-factory", (req, res) -> {
            res.type("application/json");
            this.factory.initFromMongoDB(this.handler);
            return "updated";
        });
        get("/speech", (req, res) -> {
            res.type("application/json");
            return this.speechService(req);
        });
        get("/speeches", (req, res) -> {
            res.type("application/json");
            return this.speechesService(req);
        });
        get("/speakers", (req, res) -> {
            res.type("application/json");
            return this.speakerService(req);
        });
        get("/fractions", (req, res) -> {
            res.type("application/json");
            return this.fractionsService();
        });
        get("/fraction", (req, res) -> {
            res.type("application/json");
            return this.fractionService(req.queryParams("name"));
        });
        get("/statistic", (req, res) -> {
            res.type("application/json");
            return this.statisticService();
        });
        get("/sentiment", (req, res) -> {
            res.type("application/json");
            return this.sentimentService(req, "sentiments", "sentiment").toJson();
        });
        get("/pos", (req, res) -> {
            res.type("application/json");
            return this.sentimentService(req, "POS", "pos").toJson();
        });
        get("/tokens", (req, res) -> {
            res.type("application/json");
            return this.sentimentService(req, "tokens", "token").toJson();
        });
	get("/dependencies", (req, res) -> {
            res.type("application/json");
            return this.sentimentService(req, "dependencies", "dependency").toJson();
        });
        get("/namedEntities", (req, res) -> {
            res.type("application/json");
            return this.namedEntitiesService(req).toJson();
        });
    }

    private String speechService(Request req) {
        String speechId= req.queryParams("id");
        Speech speech = this.factory.getSpeech(speechId);
        if (speech == null){
            List<Document> emptyDocumentList = new ArrayList<>();
            Document document = new Document();
            document.append("result", emptyDocumentList);
            document.append("success", false);
            return document.toJson();
        }

        return speech.toDocument().toJson();
    }

    private String speechesService(Request req){

        // query speaker list
//        List<Speaker> speakerList = filterSpeakers(req);
//        Set<String> speechIdSet = new TreeSet<>();
//        for (Speaker speaker: speakerList){
//            speechIdSet.addAll(speaker.getSpeeches());
//        }
//        List<Speech> speechList = this.factory.getSpeeches(speechIdSet);
        List<Speech> speechList = filterSpeeches(req);

        Document document = new Document();
        List<Document> speechDocuments = new ArrayList<>();
        try{
            for (Speech speech: speechList){
                if (speech == null){continue;}
                Document speechDocument = speech.toDocument();
                speechDocument.remove("_id");
                speechDocuments.add(speechDocument);
            }
        } catch (Exception e){
            e.printStackTrace();
            List<Document> emptyDocumentList = new ArrayList<>();
            document.append("result", emptyDocumentList);
            document.append("success", false);
            return document.toJson();
        }
        document.append("result", speechDocuments);
        document.append("success", true);
        return document.toJson();
    }

    private String fractionsService(){
        Document document = new Document();
        List<Document> fractionDocuments = new ArrayList<>();
        try{
            for (Fraction fraction: this.factory.getFractions()){
		Document fractionDocument = new Document();
                fractionDocument.put("id", fraction.getName());
                fractionDocument.put("members", fraction.getSpeakerIds().size());
                fractionDocument.put("memberIds", fraction.getSpeakerIds());
                fractionDocuments.add(fractionDocument);
            }
        } catch (Exception e){
            List<Document> emptyDocumentList = new ArrayList<>();
            document.append("result", emptyDocumentList);
            document.append("success", false);
            return document.toJson();
        }
        document.append("result", fractionDocuments);
        document.append("success", true);
        return document.toJson();
    }

    private String fractionService(String fractionName){
        if (fractionName == null){
            return new Document().toJson();
        }
        Fraction fraction= this.factory.getFraction(fractionName);
        if (fraction == null){
            return new Document().toJson();
        }
        return fraction.toDocument().toJson();
    }

    private String speakerService(Request req){
        // query speaker list
        List<Speaker> speakerList = filterSpeakers(req);

        Document document = new Document();
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
            document.append("result", emptyDocumentList);
            document.append("success", false);
            return document.toJson();
        }
        document.append("result", speakerDocuments);
        document.append("success", true);
        return document.toJson();
    }

    private List<Speaker> filterSpeakers(Request req){
        String speakerId = req.queryParams("user");
        String fractionName = req.queryParams("fraction");
        List<Speaker> filterById = new ArrayList<>();
        List<Speaker> filterByFraction = new ArrayList<>();
        if (speakerId == null || speakerId == ""){
            filterById.addAll(this.factory.getSpeakers());
        }else{
            Speaker speakerFound = this.factory.getSpeaker(speakerId);
            if (speakerFound != null){
                filterById.add(speakerFound);
            }
        }
        if (fractionName == null || fractionName == ""){
            filterByFraction.addAll(this.factory.getSpeakers());
        }else{
            List<Speaker> listFilteredByFraction = this.factory.getSpeakersOfFraction(fractionName);
            if (listFilteredByFraction!= null){
                filterByFraction.addAll(listFilteredByFraction);
            }
        }

        filterById.retainAll(filterByFraction);

        if (filterById.contains(null)){
            filterById.removeIf(Objects::isNull);
        }
        return filterById;

    }

    private List<Speech> filterSpeeches(Request req){
        List<Speaker> speakerList = filterSpeakers(req);
        Set<String> speechIdSet = new TreeSet<>();
        for (Speaker speaker: speakerList){
            speechIdSet.addAll(speaker.getSpeeches());
        }
        List<Speech> speechList = this.factory.getSpeeches(speechIdSet);
        return speechList;
    }

    private String statisticService(){
        Document document = new Document();

        List<Speaker> speakerList = this.factory.getSpeakers();
        List<Document> speakerStatistics = new ArrayList<>();
        for (Speaker speaker: speakerList){
            Document docu = new Document();
            docu.append("count", speaker.getSpeeches().size());
            docu.append("id", speaker.getId());
            speakerStatistics.add(docu);
        }
        document.append("speakers", speakerStatistics);

        List<Speech> speechList = this.factory.getSpeeches();
        List<Document> speechStatistics = new ArrayList<>();
        for (Speech speech: speechList){
            List<Text> texts = speech.getTexts();
            List<String> textStrs = texts.stream()
                    .filter(t -> t.getLabel() != "comment" && t.getLabel() != "name")
                    .map(t -> t.getText())
                    .collect(Collectors.toList());
            String textStr = String.join(" ", textStrs);
            Document docu = new Document();
            docu.append("count", textStr.length());
            docu.append("id", speech.getId());
            speechStatistics.add(docu);
        }
        document.append("speeches", speechStatistics);

	Document docu = new Document();
        docu.append("result", document);
        docu.append("success", true);
        return docu.toJson();
    }

    private Document namedEntitiesService(Request req){
        Document personDocu = sentimentService(req, "persons", "element");
        Document organisationsDocu = sentimentService(req, "organisations", "element");
        Document locationsDocu = sentimentService(req, "locations", "element");
        List<Document> resultDocuments = new ArrayList<>();
        resultDocuments.add(new Document("persons", personDocu.get("result")));
        resultDocuments.add(new Document("organisations", organisationsDocu.get("result")));
        resultDocuments.add(new Document("locations", locationsDocu.get("result")));
        Document docu = new Document();
        docu.append("result", resultDocuments);
        docu.append("success", true);
        return docu;
    }

    private Document sentimentService(Request req, String annotationType, String outputAnnotationTag){
        List<Speech> speechList = filterSpeeches(req);
        Document document = new Document();
        Map<Object, Integer> frequency = new TreeMap<>();
        for (Speech speech: speechList){
            try{
                List<Object> sentiments = (List<Object>) speech.getAnnotations().get(annotationType);
                for (Object sentiment: sentiments){
                    Integer count = frequency.get(sentiment);
                    if (count != null){
                        frequency.put(sentiment, ++count);
                    }else{
                        frequency.put(sentiment, 1);
                    }
                }
            }catch (Exception e){
                logger.debug(e);
            }
        }
        List<Document> results = new ArrayList<>();
        for (Object sentiment: frequency.keySet()){
            Document docu = new Document();
            docu.append(outputAnnotationTag, sentiment);
            docu.append("count", frequency.get(sentiment));
            results.add(docu);
        }
        document.put("result", results);
        document.put("success", true);
        return document;
    }
}
