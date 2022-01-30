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
        File d = new File(protocolDirectory);
        File[] files = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        List<PlenaryProtocol> protocolList = new ArrayList<>();
        List<Speaker> speakerList = new ArrayList<>();
        for (File file : files) {
            PlenaryProtocol protocol = new PlenaryProtocol_Impl(file);
            protocolList.add(protocol);
            speakerList.addAll(protocol.getSpeakerList());
        }
        System.out.println();
    }
}
