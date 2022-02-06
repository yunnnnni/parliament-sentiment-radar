package org.texttechnologylab.project.Gruppe_8_mittwoch_3.helper;

import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.PlenaryProtocol;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.Speaker;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.PlenaryProtocol_Impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProtocolFileReader {

    public static void main(String[] args) {
        String protocolDirectory = "./Daten/test/";
        readProtocols(protocolDirectory);
    }

    /**
     * read plenary protocol xml files from directory
     * @param protocolDirectory path of the directory
     * @return list of plenary protocols
     */
    public static List<PlenaryProtocol> readProtocols(String protocolDirectory){
        try{
            File d = new File(protocolDirectory);
            File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            List<PlenaryProtocol> protocolList = new ArrayList<>();
            for (File file: files){
                System.out.println("------------------------ " + file + "------------------------");
                protocolList.add(new PlenaryProtocol_Impl(file));
            }
            return protocolList;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
