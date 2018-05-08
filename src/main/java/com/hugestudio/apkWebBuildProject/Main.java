/* hay que tener MAVEN Instalado (https://www.mkyong.com/maven/how-to-install-maven-in-windows/) 
para recompilar los cambios en el proyecto: mvn install o mvn clean install 
para correrlo en el localhost:5000 (teniendo heroku CLI instalado por supuesto)
heroku local:start
Nota: si le cambiamos el nombre al proyecto, o a la carpeta que contiene nuestro Main.java 
(src-main-java-com-miNombre-miNombreproyecto-Main.java) debemos cambiar el package en nuestro Main.java en donde se encuentra nuestra clase 
(package com.hugestudio.apkWebBuildProject) recompilarlo con mvn install y irnos al archivo Procfile y asignarle el nuevo nombre del nuevo jar ubicado
en la carpeta target que debera ejecutar para que corra el servidor
*/


package com.hugestudio.apkWebBuildProject;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.Map;


@Controller
@SpringBootApplication
public class Main {


  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }



}

