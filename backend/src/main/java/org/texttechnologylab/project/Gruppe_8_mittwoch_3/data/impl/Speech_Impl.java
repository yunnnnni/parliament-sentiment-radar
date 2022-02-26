package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.print.Doc;

import jnr.ffi.annotations.In;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.dom4j.Element;
import org.javatuples.Pair;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.type.Sentiment;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;

import java.util.List;
import java.util.Map;
import scala.xml.Elem;

/**
 * class for speech
 * implements interface Speech
 */
public class Speech_Impl implements Speech {
    private String speechId;
    private Speaker speaker;
    private String speakerId;
    private List<Text> textList = new ArrayList<>();
    private JCas jcas;
    private Map<String, Object> annotations = null;
    private Pair<Integer, Integer> protocolId = null;
    private ParliamentFactory factory = null;

    /**
     * constructor
     * @param speechDocument document in mongodb that holds the relevant data about speech
     * @param factory the object of class ParliamentFactory
     */
    public Speech_Impl(Document speechDocument, ParliamentFactory factory){
        this.factory = factory;
        this.init(speechDocument);
    }

    /**
     * constructor
     * @param speechElement element for speech
     * @param factory the object of class ParliamentFactory
     */
    public Speech_Impl(Element speechElement, ParliamentFactory factory){
        this.factory = factory;
        this.init(speechElement);
    }

    /**
     * read the data about speech from protocol xml file
     * through this method can get the data about speech
     * @param speechElement element about speech
     */
    private void init(Element speechElement){
        this.speechId = speechElement.attributeValue("id");
        List<Element> speechElements = speechElement.elements();
        for (Element elementS : speechElements) {
            if (elementS.getName().equals("p")) {
                if (elementS.attributes().isEmpty()) {
                    continue;
                }
                if (elementS.attributeValue("klasse").equals("redner")) {
//                    this.speaker = new Speaker_Impl(elementS);
                    this.speaker = this.factory.addSpeaker(elementS.element("redner"));
                    this.speakerId = this.speaker.getId();
                } else {
                    this.textList.add(new Text_Impl(elementS));
                }
            } else if (elementS.getName().equals("kommentar")) {
                this.textList.add((new Text_Impl(elementS.getText(), "comment")));
            } else if (elementS.getName().equals("name")) {
                this.textList.add((new Text_Impl(elementS.getText(), "name")));
            } else if (elementS.getName().equals("a")) {
                continue;
            }
        }
    }

    /**
     * read the data about speech from mongodb document
     * through this method can get the data about speech and nlp analysis results
     * @param speechDocument document in mongodb that holds the relevant data about speech
     */
    private void init(Document speechDocument) {
        if (speechDocument.containsKey("speechId")) {
            this.speechId = speechDocument.getString("speechId");
        }
        if (speechDocument.containsKey("speakerId")) {
            this.speakerId = speechDocument.getString("speakerId");
        }
        if (speechDocument.containsKey("protocolId")) {
            List<Integer> protocolId = speechDocument.get("protocolId", ArrayList.class);
            this.protocolId = new Pair<Integer, Integer>(protocolId.get(0), protocolId.get(1));
        }
        if (speechDocument.containsKey("texts")) {
            List<Document> texts = speechDocument.getList("texts", Document.class);
            for (Document textDocument : texts) {
                Text text = new Text_Impl(textDocument);
                this.textList.add(text);
            }
        }
        if (speechDocument.containsKey("annotations")) {
            this.annotations = new HashMap<>();
            Document annotationsDocument = speechDocument.get("annotations", Document.class);
            if (annotationsDocument.containsKey("persons")) {
                this.annotations.put("persons", annotationsDocument.getList("persons", String.class));
            }
            if (annotationsDocument.containsKey("locations")) {
                this.annotations.put("locations", annotationsDocument.getList("locations", String.class));
            }
            if (annotationsDocument.containsKey("organisations")) {
                this.annotations
                        .put("organisations", annotationsDocument.getList("organisations", String.class));
            }
            if (annotationsDocument.containsKey("tokens")) {
                this.annotations.put("tokens", annotationsDocument.getList("tokens", String.class));
            }
            if (annotationsDocument.containsKey("sentences")) {
                this.annotations.put("sentences", annotationsDocument.getList("sentences", String.class));
            }
            if (annotationsDocument.containsKey("POS")) {
                this.annotations.put("POS", annotationsDocument.getList("POS", String.class));
            }
            if (annotationsDocument.containsKey("dependencies")) {
                this.annotations.put("dependencies", annotationsDocument.getList("dependencies", String.class));
            }
            if (annotationsDocument.containsKey("sentiments")) {
                this.annotations.put("sentiments", annotationsDocument.getList("sentiments", Double.class));
            }
        }
    }

    /**
     * get speech id
     * @return speech id
     */
    @Override
    public String getId() {
        return this.speechId;
    }

    /**
     * get the speaker for the speech
     * @return speaker for the speech
     */
    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    /**
     * get the speaker's id for the speech
     * @return speaker' id for the speech
     */
    @Override
    public String getSpeakerId() {
        return this.speakerId;
    }

    /**
     * set the protocol id, session and wahlpriode
     * @param session save the protocol number in mongodb
     * @param term wahlpriode of the sitzung
     */
    @Override
    public void setProtocolId(int session, int term) {
        this.protocolId = new Pair<>(session, term);
    }

    /**
     * get the protocol id, the protocol number in mongodb and wahlpriode of the sitzung
     * @return the protocol number in mongodb and wahlpriode of the sitzung
     */
    @Override
    public Pair<Integer, Integer> getProtocolId() {
        return this.protocolId;
    }

    /**
     * get the list about object of class Text
     * @return the list about object of class Text
     */
    @Override
    public List<Text> getTexts() {
        return this.textList;
    }

    /**
     * save the data of the relevant data about speech as document type
     * @return the document that stores the data about speech
     */
    @Override
    public Document toDocument(){
        Document document = new Document();
        document.append("speechId", this.speechId);
        document.append("speakerId", this.speakerId);
        document.append("protocolId", this.protocolId);
        List<Document> texts = new ArrayList<>();
        for (Text t : this.textList) {
            texts.add(t.toDocument());
        }
        document.append("texts", texts);

        if (this.annotations == null){
            this.setAnnotations();
        }
        Document annotationsList = new Document();
        annotationsList.append("persons", this.annotations.get("persons"));
        annotationsList.append("locations", this.annotations.get("locations"));
        annotationsList.append("organisations", this.annotations.get("organisations"));
        annotationsList.append("tokens", this.annotations.get("tokens"));
        annotationsList.append("sentences", this.annotations.get("sentences"));
        annotationsList.append("POS", this.annotations.get("POS"));
        annotationsList.append("dependencies", this.annotations.get("dependencies"));
        annotationsList.append("sentiments", this.annotations.get("sentiments"));
        document.append("annotations", annotationsList);

        return document;
    }

    /**
     * save the NLP analysis results in Map
     */
    @Override
    public void setAnnotations() {
        this.toCAS();
        this.annotations = new HashMap<>();
        this.annotations.put("persons", toStringList(JCasUtil.select(this.jcas, NamedEntity.class)
                .stream()
                .filter(f -> f.getValue().equals("PER"))
                .collect(Collectors.toList())));
        this.annotations.put("locations", toStringList(JCasUtil.select(this.jcas, NamedEntity.class)
                .stream()
                .filter(f -> f.getValue().equals("LOC"))
                .collect(Collectors.toList())));
        this.annotations.put("organisations", toStringList(JCasUtil.select(this.jcas, NamedEntity.class)
                .stream()
                .filter(f -> f.getValue().equals("ORG"))
                .collect(Collectors.toList())));
        this.annotations.put("tokens", toStringList(JCasUtil.select(this.jcas, Token.class)
                .stream().collect(Collectors.toList())));
        this.annotations.put("sentences", toStringList(JCasUtil.select(this.jcas, Sentence.class)
                .stream().collect(Collectors.toList())));
        this.annotations.put("POS", toStringList(JCasUtil.select(this.jcas, POS.class)
                .stream().collect(Collectors.toList())));
        this.annotations.put("dependencies", toStringList(JCasUtil.select(this.jcas, Dependency.class)
                .stream().collect(Collectors.toList())));
        this.annotations.put("sentiments", getSentiments());

        this.clearJcas();
    }

    /**
     * create CAS object and NLP pipeline
     * then perform NLP analysis
     * @return the JCas after NLP analysis
     */
    @Override
    public JCas toCAS(){
        try{
            List<String> theTextList = this.textList.stream()
                    .filter(t -> (t.getLabel() != "comment") && (t.getLabel() != "name")).map(t -> t.getText())
                    .collect(
                            Collectors.toList());
            String speechText = String.join(" ", theTextList);
            JCas jcas = JCasFactory.createText(speechText, "de");

            AggregateBuilder pipeline = new AggregateBuilder();
            pipeline.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"

            ));

            File file = new File("config/am_posmap.txt");
            String sPOSMapFile = file.getAbsolutePath();

            pipeline.add(createEngineDescription(LabelAnnotatorDocker.class,
                    LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                    LabelAnnotatorDocker.PARAM_CUTOFF, false,
                    LabelAnnotatorDocker.PARAM_SELECTION, "text",
                    LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                    LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                    LabelAnnotatorDocker.PARAM_ADD_POS, true,
                    LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, sPOSMapFile,
                    LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                    LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                    LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.prg2021.texttechnologylab.org"
            ));

            pipeline.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION,
                    "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));
            AnalysisEngine pAE = pipeline.createAggregate();
            SimplePipeline.runPipeline(jcas, pAE);
            this.jcas = jcas;

        } catch (Exception e){
            e.printStackTrace();
        }
        return this.jcas;
    }

    /**
     * get the JCas after NLP analysis
     * @return analyzed JCas
     */
    @Override
    public JCas getCAS() {
        return this.jcas;
    }

    /**
     * get the NLP analysis results
     * @return NLP analysis results in Map
     */
    @Override
    public Map<String, Object> getAnnotations() {
        return this.annotations;
    }

    /**
     * empty jcas and reduce memory
     */
    @Override
    public void clearJcas() {
        this.jcas = null;
    }

    @Override
    public void setSitzungsNr(String nr) {
    }

    /**
     * get all sentence in NLP analysis results
     * @return sentence list
     */
    public List<Sentence> getSentence() {
        return JCasUtil.select(this.jcas, Sentence.class).stream().collect(
                Collectors.toList());
    }

    /**
     * get the sentiment of the NLP analysis results
     * @return sentiment list
     */
    public List<Double> getSentiments() {
        List<Sentiment> sentiments = new ArrayList<>();
        List<Sentence> sentences = this.getSentence();
        for (Sentence sentence : sentences) {
            for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                sentiments.add(sentiment);
            }
        }

        return sentiments.stream().map(s -> s.getSentiment()).collect(Collectors.toList());
    }

    /**
     * converts annotation list to a string list
     * facilitate subsequent extraction of relevant information.
     * @param annotationCollection list of annotation
     * @return string list
     */
    public List<String> toStringList(List<Annotation> annotationCollection) {
        HashSet<String> list = new HashSet<String>();

        for (Annotation annotation : annotationCollection) {
            if (annotation instanceof POS) {
                list.add(((POS) annotation).getPosValue());
            } else {
                list.add(annotation.getCoveredText());
            }
        }
        List<String> stringList = new ArrayList<String>();
        stringList.addAll(list);
        return stringList;
    }

}
