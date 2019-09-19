package edu.eci.arep.server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * ClienteThread
 */
public class ClienteThread extends Thread {

    private Map<String, Handler> ListaURLHandler;
    private Socket cliente;

    public ClienteThread(Socket cliente, Map<String,Handler> ListaURLHandler){
        this.cliente = cliente;
        this.ListaURLHandler = ListaURLHandler;
    }

    @Override
    public void run() {
        try {
            procesar();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void procesar() throws Exception{
        while (!cliente.isClosed()) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream(), StandardCharsets.UTF_8),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("GET")) {
                    if (line.contains("/apps/")) {
                        String recurso = line.split(" ")[1];
                        if (!recurso.contains("?")) {
                            if (ListaURLHandler.containsKey(recurso)) {
                                out.println("HTTP/1.1 200 OK\r");
                                out.println("Content-Type: text/html\r");
                                out.println("\r\n");
                                out.println(ListaURLHandler.get(recurso).procesar() + "\r");
                            } else {
                                procesarLocal(out, recurso, cliente);
                            }
                        } else {
                            String recursoLocacion = recurso.substring(recurso.indexOf("/apps/"), recurso.indexOf("?"));
                            if (ListaURLHandler.containsKey(recursoLocacion)) {
                                out.println("HTTP/1.1 200 OK+\r");
                                out.println("Content-Type: text/html\r");
                                out.println("\r\n");
                                out.println(ListaURLHandler.get(recursoLocacion).procesar(
                                        new Object[] { recurso.substring(recurso.indexOf("?") + 1).split("=")[1] })
                                        + "\r");
                            } else {
                                procesarLocal(out, recurso, cliente);
                            }
                        }
                    } else {
                        String recurso = line.split(" ")[1];
                        procesarLocal(out, recurso, cliente);
                    }
                    out.close();
                }
                if (!in.ready())
                    break;
            }
            in.close();
        }
        cliente.close();
    }



    private void procesarLocal(PrintWriter out, String recurso, Socket cliente) throws Exception {
        BufferedReader br = null;
        String path;
        if (recurso.equals("/")) {
            path = System.getProperty("user.dir") + "/principal.html";
        } else {
            path = System.getProperty("user.dir") + "/resources" + recurso;
        }

        try {
            br = new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            out.println("HTTP/1.1 404 Not Found\r");
            out.println("Content-Type: text/html\r");
            System.out.println("Not found");
        }
        try {
            if (path.contains(".html")) {
                html(out, br);
            } else if (path.contains(".png")) {
                png(out, cliente, recurso);
            }
        } catch (Exception e) {
            out.println("HTTP/1.1 404 Not Found\r");
            out.println("Content-Type: text/html\r");
            System.out.println("Not found");
        }

    }

    private void html(PrintWriter out, BufferedReader br) throws Exception {
        out.write("HTTP/1.1 200 OK\r");
        out.println("Content-Type: text/html\r");
        out.println("\r");
        String temp = br.readLine();
        while (temp != null) {
            out.write(temp + "\r");
            temp = br.readLine();
        }
    }

    private void png(PrintWriter out, Socket clientSocket, String recurso) throws Exception {
        out.write("HTTP/1.1 200 OK\r");
        out.println("Content-Type: image/png\r");
        out.println("\r");
        BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + "/resources" + recurso));
        ImageIO.write(image, "PNG", clientSocket.getOutputStream());
    }
}