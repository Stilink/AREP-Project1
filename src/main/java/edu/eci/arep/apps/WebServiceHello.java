package edu.eci.arep.apps;

import edu.eci.arep.server.Web;

/**
 * WebServiceHello
 */
public class WebServiceHello {
    @Web("cuadrado")
    public static String square() {
        return "<html> <head> <tittle> Titulo </tittle>  </head> <body> <a> Hello! </body> </html>";
    }
}