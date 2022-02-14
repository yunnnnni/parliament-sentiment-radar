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

public class Speech_Impl implements Speech {

    private String speechID;
    private Speaker speaker;
    private String speakerID;
    private String speakerId;
    private List<Text> textList = new ArrayList<>();
    private List<String> speechComment = new ArrayList<>();
    private JCas jcas;
    private Document document = new Document();
    private Map<String, Object> annotations = new HashMap<>();
    private ParliamentFactory factory = null;
//    private List<String> speechComment = new ArrayList<>();

    public Speech_Impl(Element speechElement, ParliamentFactory factory){
        this.factory = factory;
        this.init(speechElement);
    }

    public Speech_Impl(Element speechElement){
        this.init(speechElement);
    }

    private void init(Element speechElement){
        this.speechID = speechElement.attributeValue("id");
        List<Element> speechElements = speechElement.elements();
        for (Element elementS : speechElements) {
            if (elementS.getName().equals("p")) {
                if (elementS.attributes().isEmpty()) {
                    continue;
                }
                if (elementS.attributeValue("klasse").equals("redner")) {
                    this.speaker = new Speaker_Impl(elementS);
                } else {
                    this.textList.add(new Text_Impl(elementS));
                }
            } else if (elementS.getName().equals("kommentar")) {
//        speechComment.add(elementS.getText());
                this.textList.add((new Text_Impl(elementS.getText(), "comment")));
            } else if (elementS.getName().equals("name")) {
                this.textList.add((new Text_Impl(elementS.getText(), "name")));
            } else if (elementS.getName().equals("a")) {
                continue;
            }
        }
    }

    public Speech_Impl(Document speechDocument) {
        if (speechDocument.containsKey("speechId")) {
            this.speechID = speechDocument.getString("speechId");
        }
        if (speechDocument.containsKey("speakerID")) {
            this.speakerID = speechDocument.getString("speakerID");
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
            if (annotationsDocument.containsKey("person")) {
                this.annotations.put("person", annotationsDocument.getList("person", String.class));
            }
            if (annotationsDocument.containsKey("location")) {
                this.annotations.put("location", annotationsDocument.getList("location", String.class));
            }
            if (annotationsDocument.containsKey("organisation")) {
                this.annotations
                        .put("organisation", annotationsDocument.getList("organisation", String.class));
            }
            if (annotationsDocument.containsKey("token")) {
                this.annotations.put("token", annotationsDocument.getList("token", String.class));
            }
            if (annotationsDocument.containsKey("sentences")) {
                this.annotations.put("sentences", annotationsDocument.getList("sentences", String.class));
            }
            if (annotationsDocument.containsKey("pos")) {
                this.annotations.put("pos", annotationsDocument.getList("pos", String.class));
            }
            if (annotationsDocument.containsKey("dependency")) {
                this.annotations.put("dependency", annotationsDocument.getList("dependency", String.class));
            }
            if (annotationsDocument.containsKey("sentiment")) {
                this.annotations.put("sentiment", annotationsDocument.getList("sentiment", Double.class));
            }
        }
    }

    @Override
    public String getId() {
        return this.speechID;
    }

    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    @Override
    public List<Text> getTexts() {
        return this.textList;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("speechID", this.speechID);
        document.append("speakerID", this.speaker.getId());

        return document;
    }

    public Document toDocumentWithNLP() throws UIMAException {
        this.toCAS();
        this.setAnnotations();
        Document document = new Document();
        Document annotationsList = new Document();
        document.append("speechId", this.speechID);
        document.append("speakerID", this.speaker.getId());
        List<Document> texts = new ArrayList<>();
        for (Text t : this.textList) {
            texts.add(t.toDocument());
        }
        document.append("texts", texts);
//    document.append("person", toStringList(JCasUtil.select(this.jcas, NamedEntity.class).stream()
//        .filter(f -> f.getValue().equals("PER")).collect(
//            Collectors.toList())));
//    document.append("location", toStringList(JCasUtil.select(this.jcas, NamedEntity.class).stream()
//        .filter(f -> f.getValue().equals("LOC")).collect(
//            Collectors.toList())));
//    document.append("organisation", toStringList(
//        JCasUtil.select(this.jcas, NamedEntity.class).stream()
//            .filter(f -> f.getValue().equals("ORG")).collect(
//            Collectors.toList())));
//    document.append("token", toStringList(JCasUtil.select(this.jcas, Token.class).stream().collect(
//        Collectors.toList())));
//    document.append("sentences",
//        toStringList(JCasUtil.select(this.jcas, Sentence.class).stream().collect(
//            Collectors.toList())));
//    document.append("pos", toStringList(JCasUtil.select(this.jcas, POS.class).stream().collect(
//        Collectors.toList())));
//    document.append("dependency",
//        toStringList(JCasUtil.select(this.jcas, Dependency.class).stream().collect(
//            Collectors.toList())));
//    document.append("sentiment", getSentiments());

        annotationsList.append("person", annotations.get("person"));
        annotationsList.append("location", annotations.get("location"));
        annotationsList.append("organisation", annotations.get("organisation"));
        annotationsList.append("token", annotations.get("token"));
        annotationsList.append("sentences", annotations.get("sentences"));
        annotationsList.append("pos", annotations.get("pos"));
        annotationsList.append("dependency", annotations.get("dependency"));
        annotationsList.append("sentiment", annotations.get("sentiment"));
        document.append("annotations", annotationsList);

        this.document = document;
        return this.document;
    }

    public void setAnnotations() {
        annotations.put("person", toStringList(JCasUtil.select(this.jcas, NamedEntity.class).stream()
                .filter(f -> f.getValue().equals("PER")).collect(
                        Collectors.toList())));
        annotations.put("location", toStringList(JCasUtil.select(this.jcas, NamedEntity.class).stream()
                .filter(f -> f.getValue().equals("LOC")).collect(
                        Collectors.toList())));
        annotations.put("organisation", toStringList(
                JCasUtil.select(this.jcas, NamedEntity.class).stream()
                        .filter(f -> f.getValue().equals("ORG")).collect(
                                Collectors.toList())));
        annotations.put("token", toStringList(JCasUtil.select(this.jcas, Token.class).stream().collect(
                Collectors.toList())));
        annotations.put("sentences",
                toStringList(JCasUtil.select(this.jcas, Sentence.class).stream().collect(
                        Collectors.toList())));
        annotations.put("pos", toStringList(JCasUtil.select(this.jcas, POS.class).stream().collect(
                Collectors.toList())));
        annotations.put("dependency",
                toStringList(JCasUtil.select(this.jcas, Dependency.class).stream().collect(
                        Collectors.toList())));
        annotations.put("sentiment", getSentiments());

    }


    @Override
    public JCas toCAS() throws UIMAException {
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

        File file = new File("./backend/config/am_posmap.txt");
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

        return this.jcas;
    }

    @Override
    public JCas getCAS() {
        return this.jcas;
    }

    @Override
    public Map<String, Object> getAnnotations() {
        return null;
    }

    @Override
    public void clearJcas() {
        this.jcas = null;
    }

    @Override
    public void setSitzungsNr(String nr) {

    }

    public List<Sentence> getSentence() {
        return JCasUtil.select(this.jcas, Sentence.class).stream().collect(
                Collectors.toList());
    }

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
