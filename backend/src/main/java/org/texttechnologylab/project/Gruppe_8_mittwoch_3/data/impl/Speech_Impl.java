package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.dom4j.Element;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;

import java.util.List;
import java.util.Map;
import scala.xml.Elem;

public class Speech_Impl implements Speech {

  private String speechID;
  private Speaker speaker;
  private List<Text> textList = new ArrayList<>();
  private List<String> speechComment = new ArrayList<>();
  private JCas jcas;

  public Speech_Impl(Element speechElement) {
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
        speechComment.add(elementS.getText());
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
  public Document toDocument() { //ab-4
    return null;
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
    String sPOSMapFile = getClass().getClassLoader().getResource("am_posmap.txt").getPath();

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

  }

  @Override
  public void setSitzungsNr(String nr) {

  }
}
