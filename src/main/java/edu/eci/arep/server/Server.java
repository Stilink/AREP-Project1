package edu.eci.arep.server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import edu.eci.arep.server.handlers.StaticMethodHandler;

/**
 * Server
 */
public class Server {
    private Map<String, Handler> ListaURLHandler;

    public Server() {
        ListaURLHandler = new HashMap<String, Handler>();
    }

    public void escuchar() throws Exception {
        while (true) {
            ServerSocket serverSocket = new ServerSocket(Server.getPort());
            Socket cliente = serverSocket.accept();
            while (!cliente.isClosed()) {
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(cliente.getOutputStream(), StandardCharsets.UTF_8), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains("GET")) {
                        if (line.contains("/apps/")) {
                            String recurso = line.split(" ")[1];
                            if(!recurso.contains("?")){
                                if (ListaURLHandler.containsKey(recurso)) {
                                    out.println("HTTP/1.1 200 OK\r");
                                    out.println("Content-Type: text/html\r");
                                    out.println("\r\n");
                                    out.println(ListaURLHandler.get(recurso).procesar()+"\r");
                                } else {
                                    procesarLocal(out, recurso, cliente);
                                }
                            }else{
                                String recursoLocacion = recurso.substring(recurso.indexOf("/apps/"),recurso.indexOf("?"));
                                if (ListaURLHandler.containsKey(recursoLocacion)) {
                                    out.println("HTTP/1.1 200 OK+\r");
                                    out.println("Content-Type: text/html\r");
                                    out.println("\r\n");
                                    out.println(ListaURLHandler.get(recursoLocacion).procesar(new Object[]{recurso.substring(recurso.indexOf("?")+1)})+"\r");
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
            serverSocket.close();
        }
    }

    public void inicializar() {
        Reflections reflections = new Reflections("edu.eci.arep.apps", new SubTypesScanner(false));
        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for (Class clase : allClasses) {
            for (Method method : clase.getMethods()) {
                if (method.isAnnotationPresent(Web.class)) {
                    Web web = method.getAnnotation(Web.class);
                    Handler handler = new StaticMethodHandler(method);
                    ListaURLHandler.put("/apps/" + web.value(), handler);
                }
            }
        }

    }

    private void procesarLocal(PrintWriter out, String recurso, Socket cliente) throws Exception {
        BufferedReader br = null;
        String path;
        if(recurso.equals("/")){
            path = System.getProperty("user.dir")+"/index.html";
        }else{
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
            out.write(temp+"\r");
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



    static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 4567; // returns default port if heroku-port isn't set (i.e. on localhost)
	}
}