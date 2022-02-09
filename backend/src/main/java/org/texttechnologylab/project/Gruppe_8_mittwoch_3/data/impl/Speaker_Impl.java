package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Fraction;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Party;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;

public class Speaker_Impl implements Speaker {
    private String id = "";
    private String titel = "";
    private String firstName = "";
    private String lastName = "";
    private Fraction fraction = new Fraction_Impl("");
//    private Party party = new Party_Impl("");
    private String role = "";

    public Speaker_Impl(Element speakerElement){
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
                            String fractionName = ele.getText().replace("\u00a0"," ");
                            // TODO: check abgeordnete without fraktion
                            //process special cases
                            if (fractionName.equals("CDU/ CSU")){fractionName="CDU/CSU";}
                            else if (fractionName.equals("fraktionslos")){fractionName="Fraktionslos";}
                            this.fraction.setName(fractionName);
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
//            if (this.id.startsWith("1100")){
//                this.initAbgeordnete(redner);
//            }
//            else{
//                this.initOtherRedner(redner);
//            }
        } catch (Exception e){
            e.printStackTrace();
        }
        // add picture

    }

    private void setPicture(){

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
        return getLastName();
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
        return this.fraction.getName();
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
        document.append("fraction", this.fraction.toDocument());
//        document.append("party", this.party.toDocument());
        document.append("role", this.role);
        return document;
    }
}
