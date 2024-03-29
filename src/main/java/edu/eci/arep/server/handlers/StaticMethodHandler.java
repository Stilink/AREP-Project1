package edu.eci.arep.server.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.eci.arep.server.Handler;

/**
 * StaticMethodHandler
 */
public class StaticMethodHandler implements Handler {
    public Method method;

    public StaticMethodHandler(Method method) {
        this.method = method;
    }

    public String procesar() {
        try {
            return (String)method.invoke(method, null);
        } catch (Exception e) {
            return "El metodo ha fallado";
        } 
    }
    
    public String procesar(Object[] arg) {
        try {
            return (String)method.invoke(method, arg);
        } catch (Exception e) {
            return "El metodo ha fallado";
        } 
    }
}