package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;

public class Text_Impl implements Text {
    private String text;
    private String label;

    public Text_Impl(Element textElement){
        this.text = textElement.getText().replace("\u00a0"," ");
        this.label = textElement.attributeValue("klasse");
    }

    public Text_Impl(Element textElement, String label){
        this.text = textElement.getText().replace("\u00a0"," ");
        this.label = label;
    }

    public Text_Impl(String text, String label){
        this.text = text.replace("\u00a0"," ");
        this.label = label;
    }

    public Text_Impl(Document textDocument){
        this.text = textDocument.getString("text").replace("\u00a0"," ");
        this.label = textDocument.getString("label");
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("text", this.text);
        document.append("label", this.label);
        return document;
    }

}
