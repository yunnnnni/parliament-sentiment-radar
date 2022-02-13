package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Fraction;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper.ImageFinder;

import java.io.IOException;

public class Speaker_Impl implements Speaker {
    private String id = "";
    private String titel = "";
    private String firstName = "";
    private String lastName = "";
    private String fractionName = "";
    private String role = "";
    private Image_Impl img = new Image_Impl("", "");
    private ParliamentFactory factory = null;

    public Speaker_Impl(Element speakerElement, ParliamentFactory factory){
        this.factory = factory;
        this.init(speakerElement);
    }

    public Speaker_Impl(Element speakerElement){
        this.init(speakerElement);
    }

    private void init(Element speakerElement){
        // init from element
        try{
            this.id = speakerElement.attributeValue("id");
            if (this.id.isEmpty()){
                System.out.format("");
            }
            // iterate over all elements
            for(Object obj:speakerElement.element("name").elements()){
                if(obj==null || !(obj instanceof Element)){  // if not element, continue
                    continue;
                }
                // process different attributes of name
                try{
                    Element ele=(Element)obj;
                    String name=ele.getName();
                    switch (name){
                        case "vorname":
                            this.firstName = ele.getText();
                            if (this.firstName.equals("Alterpräsident Dr. Hermann")){
                                this.titel = "Dr.";
                                this.firstName = "Hermann";
                            }
                            continue;
                        case "nachname":
                            this.lastName = ele.getText();
                            continue;
                        case "titel":
                            this.titel = ele.getText();
                            continue;
                        case "fraktion":
                            String fractionName = ele.getText();
                            // TODO: check abgeordnete without fraktion
                            Fraction fraction = this.factory.addFraction(fractionName);
                            fraction.addSpeaker(this);
                            this.fractionName = fraction.getName();

                            continue;
                        case "rolle":
                            try {
                                assert ele.element("rolle_kurz").getText() == ele.element("rolle_lang").getText();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            this.role = ele.elementText("rolle_lang");
                            continue;
                        case "ortszusatz":
                            // jump ortszusatz
                            continue;
                        case "namenszusatz":
                            // jump
                            continue;
                        default:
                            System.out.printf("Speaker.name_process case: %s\n", name);
                            continue;
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
//            ImageFinder finder = new ImageFinder(this.firstName, this.lastName);
//            this.img = new Image_Impl(finder.getImgUrl(), finder.getDescription());

        } catch (Exception e){
            e.printStackTrace();
        }
        // add picture

    }

    public void setImage(Image_Impl image){
        this.img = image;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getTitel() {
        return this.titel;
    }

    @Override
    public String getFraktion() {
        return this.fractionName;
    }

    @Override
    public Boolean isParliamentMember() {
        if (this.id.startsWith("1100")){
            return true;
        }
        return false;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("id", this.id);
        document.append("titel", this.titel);
        document.append("firstName", this.firstName);
        document.append("lastName", this.lastName);
        document.append("fraction", this.fractionName);
        document.append("role", this.role);
        try {
            ImageFinder finder = new ImageFinder(this.firstName, this.lastName);
            this.img = new Image_Impl(finder.getImgUrl(), finder.getDescription());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        document.append("image", this.img.toDocument());
        return document;
    }
}
