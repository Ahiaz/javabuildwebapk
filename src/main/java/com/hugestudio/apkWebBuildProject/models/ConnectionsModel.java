/* Modelo Connections para MongoDB
*/


package com.hugestudio.apkWebBuildProject.models;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;


@Document(collection = "connections") // crea este modelo en la base de datos si no existe

public class ConnectionsModel {

  //atributos o propiedades
  @Id
  public String id; //automatico el id
  public int numConnections; //almacena el numero de conexiones actuales
  public int totalConnections; //almacena el numero de conexiones totales
  //constructores

      public ConnectionsModel() {
    }

    public ConnectionsModel(int numConnections, int totalConnections) { //para darle valores al modelo (para el save method)
        this.numConnections = numConnections;
        this.totalConnections = totalConnections;
    }

}