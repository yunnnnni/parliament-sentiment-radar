package org.texttechnologylab.project.Gruppe_8_mittwoch_3.REST;

import java.io.IOException;

/**
 * test for image
 * @author Yunni Lu
 */
public class RESTStarter {
    public static void main(String[] args) throws IOException {
        RESTServices rest = new RESTServices();
        rest.startServices();
    }
}
