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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.Map;
import java.io.*;
import com.hugestudio.apkWebBuildProject.models.BuildApkModel; //de esta forma importamos el modelo que necesita el repositorio
import com.hugestudio.apkWebBuildProject.repository.BuildApk.BuildApkRepository; //de esta forma importamos el repositorio que necesitamos
import java.text.NumberFormat;
import org.springframework.http.ResponseEntity;
import com.hugestudio.apkWebBuildProject.models.ResponseStatus; //de esta forma importamos el modelo que creamos para dar una respuesta a una peticion
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.apache.commons.io.IOUtils;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Controller
@RequestMapping("buildapk")
public class BuildApkCtrl {

@Autowired
private static BuildApkRepository webApkRepository; // mediante este repositorio accedemos a todos los metodos del model webapk de MongoDB
private static ResponseStatus status = null; //instancia del objeto para dar una respuesta 



   public static void setUpStreamGobbler(final InputStream is, final PrintStream ps) { //nos permite ver lo que imprime la consola cuando ejecutamos un comando externo JAR (consume mucho RAM)
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



public static void getMemoryUsage(){


Runtime runtime = Runtime.getRuntime();

NumberFormat format = NumberFormat.getInstance();

StringBuilder sb = new StringBuilder();
long maxMemory = runtime.maxMemory();
long allocatedMemory = runtime.totalMemory();
long freeMemory = runtime.freeMemory();

sb.append("free memory: " + format.format(freeMemory / 1024) + "<br/>");
sb.append("allocated memory: " + format.format(allocatedMemory / 1024) + "<br/>");
sb.append("max memory: " + format.format(maxMemory / 1024) + "<br/>");
sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");

System.out.println(sb);

}


public static void signAPK(String appname){//firma el apk


String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/signer/sign-master/dist/sign.jar", "src/main/resources/public/makingAPK/"+appname+"/"+appname+".apk", "--override"}; 

/*
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
      }).start(); */

      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();

         /*
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);

         */

         System.out.println("waiting for signed");
         process.waitFor();

         System.out.println("finished signed");
         process.destroy();

      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }




}





public static void compileAPK(String appname){//descompila el apk en el servidor

String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/apktool/apktool.jar", "b", "src/main/resources/public/makingAPK/"+appname, "-o", "src/main/resources/public/makingAPK/"+appname+"/"+appname+".apk"}; 

/*
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
      }).start(); */

      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();

         /*
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);
*/
         System.out.println("waiting compiled");
         process.waitFor();

         System.out.println("finished compiled");
         process.destroy();


      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }




}

public static void decompileAPK(String appname){//descompila el apk en el servidor

String[] CMD_ARRAY = { "java", "-jar", "src/main/resources/public/apktool/apktool.jar", "d", "src/main/resources/public/apktool/webapp.apk", "-o", "src/main/resources/public/makingAPK/"+appname}; 

/*
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
*/
      Process process = null;
      try {
         ProcessBuilder processBuilder = new ProcessBuilder(CMD_ARRAY);
         process = processBuilder.start();
         /*
         InputStream inputStream = process.getInputStream();
         setUpStreamGobbler(inputStream, System.out);

         InputStream errorStream = process.getErrorStream();
         setUpStreamGobbler(errorStream, System.err);
*/
         System.out.println("waiting decompiled");
         process.waitFor();
        System.out.println("finished decompile");
        process.destroy();

      } catch (IOException e) {
         throw new RuntimeException(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }




}


//@RequestMapping("/rename_apk") //example http://localhost:5000/builapk/rename_apk recibiendo un JSON

@GetMapping(
  value = "/rename_apk",
  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE //devolvera un archivo como respuesta (el apk)
)
 public @ResponseBody byte[] getFile(/*@RequestBody BuildApkModel webJson*/) throws IOException{ //recibira un JSON

      String appname = "servicioshospitalarios";

      InputStream fileApk = null; //para retornar el APK

try{


  //********************************************** descompilar el apk del servidor******************************************************


  decompileAPK(appname);


//*********************************cambiar todas las ocurrencias  del package name en androidManifest.xml **************************************

        String packageName = "com.hugestudio.";

        String webpageName = appname;

         File inputFile = new File("src/main/resources/public/makingAPK/"+appname+"/AndroidManifest.xml"); //ubicacion de nuestro archivo
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

         /*
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         System.out.println("-----------Modified File-----------");
         StreamResult consoleResult = new StreamResult(System.out);
         transformer.transform(source, consoleResult);
         */

        // set the new xml in the file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult output = new StreamResult(inputFile); // xml is a object of File i.e. File xml = new File(filePath);
         transformer.transform(source, output);

        //*********************************Compilamos el archivo APK*******************************
         compileAPK(appname);

         //*******************************Firmamos el archivo APK**********************************
         signAPK(appname);

         //*********************************Codificamos en bytes el archivo para devolverlo*******************************
     fileApk = getClass().getResourceAsStream("src/main/resources/public/makingAPK/"+appname+"/"+appname+".apk"); //codifica el archivo APK para enviarlo al cliente

     fileApk.close();
}

catch(Exception e){e.printStackTrace();}


return IOUtils.toByteArray(fileApk);

      }
  

}

