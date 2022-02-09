package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.Image_Impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * find image from bundestag database
 */
public class ImageFinder {
    public ImageFinder(String firstName, String lastName){
        String filterName = "filterQuery[name][]=" + lastName + ",+" + firstName;
        String filterOthers = "filterQuery[ereignis][]=Plenarsitzung,+Redner&sortVal=2#group-1";
        String url = filterName + "&" + filterOthers;
        try{
            String urlEncodes = encodeValue(url);
        } catch (Exception e){

        }
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
