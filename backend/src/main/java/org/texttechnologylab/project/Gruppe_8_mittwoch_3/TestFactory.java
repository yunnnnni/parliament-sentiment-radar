package org.texttechnologylab.project.Gruppe_8_mittwoch_3;

import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.ParliamentFactory;
import org.texttechnologylab.project.Gruppe_8_mittwoch_3.data.impl.ParliamentFactory_Impl;

public class TestFactory {
    public static void main(String[] args) {
        String protocolDirectory = "C:\\Programmieren\\nini\\praktikum\\gruppe_8_mittwoch_3_parliamentsentimentradar\\backend\\Daten\\test";
        ParliamentFactory factory = new ParliamentFactory_Impl();
        factory.initFromDirectory(protocolDirectory);
        System.out.println();

    }
}
