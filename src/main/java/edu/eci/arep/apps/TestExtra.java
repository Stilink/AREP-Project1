package edu.eci.arep.apps;

import edu.eci.arep.server.Web;

/**
 * WebServiceHello
 */
public class TestExtra {
    @Web("extra")
    public static String square() {
        return "<!DOCTYPE html> <html> <head> <title> Titulo </title>  </head> <body> <h1>Hello world! </h1> <p>Extra</p> </body> </html>";
    }
 }
