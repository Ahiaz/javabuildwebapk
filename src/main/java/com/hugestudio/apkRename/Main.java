/* hay que tener MAVEN Instalado (https://www.mkyong.com/maven/how-to-install-maven-in-windows/) 
para recompilar los cambios en el proyecto: mvn install o mvn clean install 
para correrlo en el localhost:5000 (teniendo heroku CLI instalado por supuesto)
heroku local:start
Nota: si le cambiamos el nombre al proyecto, o a la carpeta que contiene nuestro Main.java 
(src-main-java-com-miNombre-miNombreproyecto-Main.java) debemos cambiar el package en nuestro Main.java en donde se encuentra nuestra clase 
(package com.hugestudio.apkRename) recompilarlo con mvn install y irnos al archivo Procfile y asignarle el nuevo nombre del nuevo jar ubicado
en la carpeta target que debera ejecutar para que corra el servidor
*/


package com.hugestudio.apkRename;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@Controller
@SpringBootApplication
public class Main {



   public static void setUpStreamGobbler(final InputStream is, final PrintStream ps) { //nos permite ver lo que imprime la consola cuando ejecutamos un comando externo
      final InputStreamReader streamReader = new InputStreamReader(is);
      new Thread(new Runnable() {
         public void run() {
            BufferedReader br = new BufferedReader(streamReader);
            String line = null;
            try {
               while ((line = br.readLine()) != null) {
                  ps.println("en proceso: " + line);
               }


            } catch (IOException e) {
               e.printStackTrace();
            } finally {
               try {
                  br.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
         }
      }).start();


   
}


public static void signAPK(String appname){//firma el apk


String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/signer/sign-master/dist/sign.jar", "src/main/resources/public/makingAPK/"+appname+"/"+appname+".apk", "--override"}; 


new Thread(new Runnable() {
         public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                  System.in));
            String command = null;
            try {
               while ((command = reader.readLine()) != null) {
                  System.out.println("Command Received:" + command);
               }
            } catch (Exception ex) {
               ex.printStackTrace();
               // failed to listening command
            }

         }
      }).start();

      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);

         System.out.println("esperando por el resultado");
         process.waitFor();

         System.out.println("finished signed");


      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }




}





public static void compileAPK(String appname){//descompila el apk en el servidor

String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/apktool/apktool.jar", "b", "src/main/resources/public/makingAPK/"+appname, "-o", "src/main/resources/public/makingAPK/"+appname+"/"+appname+".apk"}; 


new Thread(new Runnable() {
         public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                  System.in));
            String command = null;
            try {
               while ((command = reader.readLine()) != null) {
                  System.out.println("Command Received:" + command);
               }
            } catch (Exception ex) {
               ex.printStackTrace();
               // failed to listening command
            }

         }
      }).start();

      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);

         System.out.println("esperando por el resultado");
         process.waitFor();

         System.out.println("finished compiled");

         signAPK(appname);


      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }




}

public static void decompileAPK(String appname){//descompila el apk en el servidor

String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/apktool/apktool.jar", "d", "src/main/resources/public/apktool/webapp.apk", "-o", "src/main/resources/public/makingAPK/"+appname}; 

new Thread(new Runnable() {
         public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                  System.in));
            String command = null;
            try {
               while ((command = reader.readLine()) != null) {
                  System.out.println("Command Received:" + command);
               }
            } catch (Exception ex) {
               ex.printStackTrace();
               // failed to listening command
            }

         }
      }).start();

      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);

         System.out.println("esperando por el resultado");
         process.waitFor();
        System.out.println("finished decompile");

        compileAPK(appname);

      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }




}



  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

    @RequestMapping("/rename_apk") //example http://localhost:5000/rename_apk?name=redBull

  String rename(@RequestParam(/*defaultValue="fordefault",*/ required=true) String name) {


try{



  decompileAPK("servicioshospitalarios");
  //********************************************** descompilar el apk del servidor******************************************************




//*********************************cambiar todas las ocurrencias  del package name en androidManifest.xml **************************************

      /*
        String packageName = "com.hugestudio.";

        String webpageName = name;

         File inputFile = new File("src/main/resources/public/apktool/webapp/AndroidManifest.xml"); //ubicacion de nuestro archivo
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
         Document doc = docBuilder.parse(inputFile);
         Node androidmanifest = doc.getFirstChild();
         Node manifestTag = doc.getElementsByTagName("manifest").item(0);
         
         // update attribute manifest package
         NamedNodeMap attr = manifestTag.getAttributes();
         Node nodeAttr = attr.getNamedItem("package");
         nodeAttr.setTextContent(packageName+webpageName); //set package name

         //update attribute activity android:name
         Node MainActivityTag = doc.getElementsByTagName("activity").item(0);
         NamedNodeMap attr1 = MainActivityTag.getAttributes();
         Node nodeAttr1 = attr1.getNamedItem("android:name");
         nodeAttr1.setTextContent(packageName+webpageName+".MainActivity"); //set package name

         //update attribute provider android:authorities
         Node PicassoTag = doc.getElementsByTagName("provider").item(0);
         NamedNodeMap attr2 = PicassoTag.getAttributes();
         Node nodeAttr2 = attr2.getNamedItem("android:authorities");
         nodeAttr2.setTextContent(packageName+webpageName+".com.squareup.picasso"); //set package name


         //set in console (for view)
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         System.out.println("-----------Modified File-----------");
         StreamResult consoleResult = new StreamResult(System.out);
         transformer.transform(source, consoleResult);

        // set the new xml in the file

         StreamResult output = new StreamResult(inputFile); // xml is a object of File i.e. File xml = new File(filePath);
         transformer.transform(source, output);

 //*************************************************************************************************************************************        
*/
}

catch(Exception e){e.printStackTrace();}



/*

 String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/apktool/apktool.jar", "b", "src/main/resources/public/apktool/webapp" }; 

 //recompila la carpeta que por default habiamos puesto descompilada mediante APKTOOL (java -jar apktool.jar d myaapp.apk // to decompile)

new Thread(new Runnable() {
         public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                  System.in));
            String command = null;
            try {
               while ((command = reader.readLine()) != null) {
                  System.out.println("Command Received:" + command);
               }
            } catch (Exception ex) {
               ex.printStackTrace();
               // failed to listening command
            }

         }
      }).start();

      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);

         System.out.println("esperando por el resultado");
         process.waitFor();
      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }

*/

    return "index";

      }
  
  

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}

