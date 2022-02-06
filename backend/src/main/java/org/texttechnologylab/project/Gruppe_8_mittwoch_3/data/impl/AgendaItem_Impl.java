package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import java.util.ArrayList;
import java.util.Arrays;
import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.AgendaItem;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speech;

import java.util.List;

public class AgendaItem_Impl implements AgendaItem {

  private String topID;
  private List<String> agendaText = new ArrayList<>();
  private List<Speech> speechList = new ArrayList<>();
  private List<String> agendaItemComment = new ArrayList<>();

  public AgendaItem_Impl(Element agendaElement) {
    List<String> agendaTextLabel = Arrays
        .asList("J", "J_1", "O", "A_TOP", "T_Beratung", "T_Drs", "T_E_Drs", "T_E_E_Drs", "T_E_fett",
            "uF020 T_NaS", "T_NaS_NaS", "T_ZP_NaS", "T_ZP_NaS_NaS", "T_ZP_NaS_NaS_Strich",
            "T_Ueberweisung", "T_fett", "T_ohne_NaS");

    this.topID = agendaElement.attributeValue("top-id");
    List<Element> agendaItemElements = agendaElement.elements();
    for (Element elementsA : agendaItemElements) {
      if (elementsA.getName().equals("rede")) {
        speechList.add(new Speech_Impl(elementsA));
      } else if (elementsA.getName().equals("kommentar")) {
        agendaItemComment.add(elementsA.getText());
      } else if (elementsA.getName().equals("p") && agendaTextLabel
          .contains(elementsA.attributeValue("klasse"))){
          agendaText.add(elementsA.getText());
      }
    }
  }

  @Override
  public String getId() {
    return this.topID;
  }

  @Override
  public void printTexts() {
      for(String text : agendaText){
          System.out.println(text);
      }
  }

  @Override
  public List<Speech> getSpeeches() {
    return this.speechList;
  }

  @Override
  public Document toDocument() {
    return null;
  }
}
