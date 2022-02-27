package org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl;

import org.bson.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Image;

/**
 * class to save image of speakers
 */
public class Image_Impl implements Image {
    private String imageUrl;
    private String imageDescription;

    public Image_Impl(String firstName, String lastName){
        String fullName = firstName + " " + lastName;
        fullName = fullName.replace(" ", "+");
        String urlEncoded = "https://bilddatenbank.bundestag.de/search/picture-result?"
                + "query=" + fullName
//                            + "filterQuery%5Bname%5D%5B%5D=" + lastName + "%2C+" + firstName
                + "&filterQuery%5Bereignis%5D%5B%5D=Plenarsitzung%2C+Redner&sortVal=2";
        try{
            String html = this.getHttpResponse(urlEncoded);
            this.parseHtml(html);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * read the data about image from mongodb document
     * @param imageDocument document in mongodb that holds the relevant data about image
     */
    public Image_Impl(Document imageDocument){
        if (imageDocument.containsKey("url")){
            this.imageUrl = imageDocument.getString("url");
        }
        if (imageDocument.containsKey("description")){
            this.imageDescription = imageDocument.getString("description");
        }
    }


    /**
     * get url of this image
     * @return image url
     */
    @Override
    public String getUrl() {
        return this.imageUrl;
    }

    /**
     * get description of this image
     * @return image description
     */
    @Override
    public String getDescription() {
        return this.imageDescription;
    }

    /**
     * save the data of the relevant data about image as document type
     * @return the document that stores the data about image
     */
    public Document toDocument(){
        Document document = new Document();
//        document.append("imageBin", new Binary(this.imageBin));
        document.append("url", this.imageUrl);
        document.append("description", this.imageDescription);
        return document;
    }

    /**
     * parse image information on the given html
     * @param html html content which contains speaker image
     */
    private void parseHtml(String html){
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Elements elems = doc.getElementsByAttribute("data-fancybox");
        if (elems.size() > 0){
            try{
                Element elem = elems.get(0);
                Element imgElement = elem.getElementsByTag("img").get(0);
                String urlPart = imgElement.attr("src");
                this.imageDescription = imgElement.attr("alt");
                this.imageUrl = "https://bilddatenbank.bundestag.de" + urlPart;
            }catch (Exception e){
            }
        }
    }

    /**
     * get http content of the given url
     * @param url target url
     * @return http content string
     */
    private String getHttpResponse(String url){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET() // GET is default
                    .build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            return response.body();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
