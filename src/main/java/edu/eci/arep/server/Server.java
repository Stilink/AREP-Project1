package edu.eci.arep.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import edu.eci.arep.server.handlers.StaticMethodHandler;

/**
 * Server
 */
public class Server implements Runnable {
    private Map<String, Handler> ListaURLHandler;
    private ExecutorService pool;
    private ServerSocket serverSocket;

    public Server(int poolSize) {
        try {
            ListaURLHandler = new HashMap<String, Handler>();
            serverSocket = new ServerSocket(Server.getPort());
            pool = Executors.newFixedThreadPool(poolSize);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; // returns default port if heroku-port isn't set (i.e. on localhost)
    }

    @Override
    public void run() {
        try{
            while(true){
                Socket cliente = serverSocket.accept();
                ClienteThread clienteT = new ClienteThread(cliente, ListaURLHandler);
                pool.execute(clienteT);
            }
        }catch(Exception e){
            pool.shutdown();
        }

    }
}
