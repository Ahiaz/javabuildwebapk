/* hay que tener MAVEN Instalado (https://www.mkyong.com/maven/how-to-install-maven-in-windows/) 
para recompilar los cambios en el proyecto: mvn install o mvn clean install 
para correrlo en el localhost:5000 (teniendo heroku CLI instalado por supuesto)
heroku local:start
Nota: si le cambiamos el nombre al proyecto, o a la carpeta que contiene nuestro Main.java 
(src-main-java-com-miNombre-miNombreproyecto-Main.java) debemos cambiar el package en nuestro Main.java en donde se encuentra nuestra clase 
(package com.hugestudio.apkWebBuildProject) recompilarlo con mvn install y irnos al archivo Procfile y asignarle el nuevo nombre del nuevo jar ubicado
en la carpeta target que debera ejecutar para que corra el servidor
*/


package com.hugestudio.apkWebBuildProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.Map;
import java.io.*;
import com.hugestudio.apkWebBuildProject.models.ConnectionsModel; //de esta forma importamos el modelo que necesita el repositorio
import com.hugestudio.apkWebBuildProject.models.ResponseStatus; //de esta forma importamos el modelo que creamos para dar una respuesta a una peticion
import com.hugestudio.apkWebBuildProject.repository.Connections.ConnectionsRepository; //de esta forma importamos el repositorio que necesitamos
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("connection")
public class ConnectionCtrl {

@Autowired
private ConnectionsRepository connRepository; // mediante este repositorio accedemos a todos los metodos del model Connection de MongoDB

private ResponseStatus status = null; //instancia del objeto para dar una respuesta 

    @RequestMapping("/get_connected") //example http://localhost:5000/connection/get_connected
 public ResponseEntity<ResponseStatus> connected() { //obtiene y si existen menos de dos conexiones conecta con el servidor

try{

status = new ResponseStatus(connRepository.CreateOrUpdateConnection()); // ok = true or false

}

catch(Exception e){e.printStackTrace();}


return ResponseEntity.ok(status);

      }


    @RequestMapping("/set_disconnected") //example http://localhost:5000/connection/set_disconnected
 public ResponseEntity<ResponseStatus> disconnected() { //desconecta del servidor para dejar disponible un campo

try{

status = new ResponseStatus(connRepository.UpdateDisconnection()); // ok = true or false

}

catch(Exception e){e.printStackTrace();}


return ResponseEntity.ok(status);

      }      

}

