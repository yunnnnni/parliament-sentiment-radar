package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;
import org.dom4j.Element;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Fraction;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Image;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;

import java.util.Set;
import java.util.TreeSet;

/**
 * class for speaker
 * implements interface Speaker
 * @author Yunni Lu
 */
public class Speaker_Impl implements Speaker {
    private String id = "";
    private String titel = "";
    private String firstName = "";
    private String lastName = "";
    private String fractionName = "";
    private String role = "";
    private Image img = null;
    private Set<String> speechIdSet = new TreeSet<>();
//    private Set<Speech> speechSet = new TreeSet<>();
    private ParliamentFactory factory = null;

    /**
     * constructor
     * @param speakerDocument document in mongodb that holds the relevant data about speaker
     * @param factory the object of class ParliamentFactory
     */
    public Speaker_Impl(Document speakerDocument, ParliamentFactory factory){
        this.factory = factory;
        this.init(speakerDocument);
    }

    /**
     * constructor
     * @param speakerElement element for speaker
     * @param factory the object of class ParliamentFactory
     */
    public Speaker_Impl(Element speakerElement, ParliamentFactory factory){
        this.factory = factory;
        this.init(speakerElement);
    }

    /**
     * constructor
     * @param speakerElement element for speaker
     */
    public Speaker_Impl(Element speakerElement){
        this.init(speakerElement);
    }

    /**
     * read the data from protocol xml file
     * through this method can get the all data about speaker
     * id, vorname, nachname, titel, fraktion, rolle, ortzusatz and namenszusatz
     * @param speakerElement
     */
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
                            if (this.firstName.equals("Alterspräsident Dr. Hermann")){
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

    /**
     *  rea the data from mongodb document
     *  through this method can get the data about speaker
     *  id, titel, firstname, name, fraction, role, image and speeches
     * @param speakerDocument
     */
    private void init(Document speakerDocument){
        this.id = speakerDocument.getString("id");
        this.titel = speakerDocument.getString("titel");
        this.firstName = speakerDocument.getString("firstname");
        this.lastName = speakerDocument.getString("name");
        this.fractionName = speakerDocument.getString("fraction");
        this.role = speakerDocument.getString("role");
        this.img = new Image_Impl(speakerDocument.get("image", Document.class));
        if (speakerDocument.containsKey("speechIds")){
            this.speechIdSet.addAll(speakerDocument.getList("speechIds", String.class));
        }
    }

    /**
     * get the speaker id
     * @return speaker id
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * get the speaker firstname
     * @return
     */
    @Override
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * get the speaker lastname
     * @return
     */
    @Override
    public String getLastName() {
        return this.lastName;
    }

    /**
     * get the speaker name
     * @return speaker name
     */
    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * get the speaker titel
     * @return speaker titel
     */
    @Override
    public String getTitel() {
        return this.titel;
    }

    /**
     * get the speaker fraction
     * @return speaker fraction
     */
    @Override
    public String getFraction() {
        return this.fractionName;
    }

    /**
     * check if the speaker is a parliament member
     * if the id starts with 1100, it is the parliament member
     * @return judgment Results, if true, the speaker is parliament member, if false, the speaker is not parliament member
     */
    @Override
    public Boolean isParliamentMember() {
        if (this.id.startsWith("1100")){
            return true;
        }
        return false;
    }

    /**
     * save the data of the relevant data about speaker as document type
     * then can save to mongodb
     * @return the document that stores the data about speaker
     */
    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("firstname", this.firstName);
        document.append("name", this.lastName);
        document.append("id", this.id);
        document.append("titel", this.titel);
        document.append("fraction", this.fractionName);
        document.append("role", this.role);
        // find image when writing to mongodb
        // this can help reduce debugging time
        if (this.img == null){
            this.img = new Image_Impl(this.firstName, this.lastName);
        }
        document.append("image", this.img.toDocument());
        document.append("speechIds", this.speechIdSet);
        return document;
    }

    /**
     * add the speaker's speech id to speechIdSet collection
     * @param speechId speech id
     */
    @Override
    public void addSpeech(String speechId) {
        this.speechIdSet.add(speechId);
    }

    /**
     * get the speaker's speech id
     * @return all the speaker's speech id
     */
    @Override
    public Set<String> getSpeeches() {
        return this.speechIdSet;
    }
}
