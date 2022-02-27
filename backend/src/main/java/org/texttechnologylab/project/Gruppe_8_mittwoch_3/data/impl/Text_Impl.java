package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Text;

/**
 * class for text
 * implements interface Text
 * @author Chufan Zhang
 */
public class Text_Impl implements Text {
    private String text;
    private String label;

    /**
     * constructor
     * read the content of the text from protocol xml file
     * @param textElement element for text
     */
    public Text_Impl(Element textElement){
        this.text = textElement.getText().replace("\u00a0"," ");
        this.label = textElement.attributeValue("klasse");
    }

    /**
     * constructor
     * read the content of the text from protocol xml file
     * @param textElement element for text
     * @param label the label for text
     */
    public Text_Impl(Element textElement, String label){
        this.text = textElement.getText().replace("\u00a0"," ");
        this.label = label;
    }

    /**
     * constructor
     * @param text content of the text
     * @param label the label for text
     */
    public Text_Impl(String text, String label){
        this.text = text.replace("\u00a0"," ");
        this.label = label;
    }

    /**
     * constructor
     * read the content of the text from mongodb document
     * @param textDocument
     */
    public Text_Impl(Document textDocument){
        this.text = textDocument.getString("text").replace("\u00a0"," ");
        this.label = textDocument.getString("label");
    }

    /**
     * get the content of the text
     * @return text
     */
    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getLabel() {
        return null;
    }

    /**
     * save the data of the relevant content of the text as document type
     * @return the document that stores the content of the text
     */
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("text", this.text);
        document.append("label", this.label);
        return document;
    }

}
