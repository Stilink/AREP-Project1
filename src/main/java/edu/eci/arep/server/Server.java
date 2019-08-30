package edu.eci.arep.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import edu.eci.arep.server.handlers.StaticMethodHandler;

/**
 * Server
 */
public class Server {
    private Map<String, Handler> ListaURLHandler;

    public Server(){
        ListaURLHandler = new HashMap<String,Handler>();
    }

    public void escuchar() {
        System.out.println(ListaURLHandler.get("apps/cuadrado").procesar());
    }

    public void inicializar() {
        Reflections reflections = new Reflections("edu.eci.arep.apps",new SubTypesScanner(false));
        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for(Class clase : allClasses){
            for(Method method : clase.getMethods()){
                if(method.isAnnotationPresent(Web.class)){
                    Web web = method.getAnnotation(Web.class);
                    Handler handler = new StaticMethodHandler(method);
                    ListaURLHandler.put("apps/"+web.value(), handler);
                }
            }
        }
        /*Class clase;
        try {
            clase = Class.forName("edu.eci.arep.apps.WebServiceHello");
            Method method = clase.getMethod("square", null);
            System.out.println(method.invoke(null, null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
    }
}