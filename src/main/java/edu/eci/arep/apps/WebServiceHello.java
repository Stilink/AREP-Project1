package edu.eci.arep.apps;

import edu.eci.arep.server.Web;

/**
 * WebServiceHello
 */
public class WebServiceHello {
    @Web("cuadrado")
    public static String square() {
        return "<!DOCTYPE html> <html> <head> <title> Titulo </title>  </head> <body> <h1>Hello world! </h1> </body> </html>";
    }

    @Web("cuadradoConNombre")
    public static String square(String text) {
        return "<!DOCTYPE html> <html> <head> <title> Titulo </title>  </head> <body> <h1>Test! </h1>"+text+" </body> </html>";
    }

    @Web("a")
    public static String metodoDestinadoACrearException() {
        String[] strings = new String[4];
        for(String s : strings){
            s = "a";
        }
        return strings[5];
    }
}