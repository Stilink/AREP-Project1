package edu.eci.arep.controlador;

import java.lang.reflect.Method;

import edu.eci.arep.server.Server;

/**
 * Controlador
 */
public class Controlador {

    public static void main(String[] args) throws Exception{
        Server service = new Server(10);
        service.inicializar();
        Thread serverThread = new Thread(service);
        serverThread.start();     
    }
}