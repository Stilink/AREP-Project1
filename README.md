# AREP - PROJECT 1 - FRAMEWORK WEB

## Autor
- Jeymar Vega

## Introducción
En este repositorio se encuentra una implementación de framework web, que permite responder a solicitudes sobre imagenes png y archivos HTML.
Además permite al desarrollador añadir POJOs con metodos marcados como @Web, dentro de la anotación se debe colocar el nombre que se desea para que dicho metodo sea accesible de manera web.
Los POJOs deben ser colocados dentro de la carpeta /src/main/java/edu/eci/arep/apps para que sean visibles por el framework.
Las cuales podrán ser accesibles de la siguiente manera: "/apps/{nombre-metodo}", además se permite el envió de un "parametro" de la siguiente manera: "/apps/{nombre-metodo}?param=paramValue",
de modo que el metodo recibirá como parametro una cadena así "param=paramValue". La interpretación y manejo del parametro queda a cargo del desarrollador.

## Arquitectura de Sotfware
![alt text](https://github.com/Stilink/AREP-Project1/blob/master/img/modelo_clases.PNG)

## Arquitectura de despliegue
![alt text](https://github.com/Stilink/AREP-Project1/blob/master/img/componentes.PNG)

## Conclusiones

## Bibliografía
 - Github, Stilink(Jeymar Vega) - Github: https://github.com/Stilink/AREP_CYS
 
 ## Heroku
 https://arep-project-1.herokuapp.com/
