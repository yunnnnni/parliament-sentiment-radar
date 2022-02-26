package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * find image from bundestag database
 */
public class ImageFinder {
    private String html;
    private String srcUrl;
    private String imgUrl;
    private String description;

    /**
     * constructor
     * @param firstName firstname of the person
     * @param lastName lastname of the person
     * @throws IOException
     * @throws InterruptedException
     */
    public ImageFinder(String firstName, String lastName) throws IOException, InterruptedException {
        String fullName = firstName + " " + lastName;
        fullName = fullName.replace(" ", "+");
        String urlEncoded = "https://bilddatenbank.bundestag.de/search/picture-result?"
                            + "query=" + fullName
//                            + "filterQuery%5Bname%5D%5B%5D=" + lastName + "%2C+" + firstName
                            + "&filterQuery%5Bereignis%5D%5B%5D=Plenarsitzung%2C+Redner&sortVal=2";
        this.srcUrl = urlEncoded;
        this.html = this.getHttpResponse(this.srcUrl);
        this.parseHtml(this.html);
    }

    /**
     * get the url of the image
     * @return url string
     */
    public String getImgUrl(){
        return this.imgUrl;
    }

    /**
     * get the description of the image
     * @return description string
     */
    public String getDescription(){
        return this.description;
    }

    /**
     *
     * @param html
     */
    private void parseHtml(String html){
        Document doc = Jsoup.parse(html);
        Elements elems = doc.getElementsByAttribute("data-fancybox");
        if (elems.size() > 0){
            try{
                Element elem = elems.get(0);
                Element imgElement = elem.getElementsByTag("img").get(0);
                String urlPart = imgElement.attr("src");
                this.description = imgElement.attr("alt");
                this.imgUrl = "https://bilddatenbank.bundestag.de" + urlPart;
            }catch (Exception e){
            }
        }
    }

    /**
     *
     * @param url
     * @return
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
