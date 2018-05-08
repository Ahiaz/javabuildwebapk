/* interfaz que expone los metodos que posee el ConnectionsRepository a mongoDB
deberan ser implementados por otra clase que posea nombre+Impl para que tenga funcionalidad (ConnectionsRepositoryImpl)
*/


package com.hugestudio.apkWebBuildProject.repository.Connections;

import com.hugestudio.apkWebBuildProject.models.ConnectionsModel; //de esta forma importamos el modelo que necesita el repositorio

import org.springframework.data.mongodb.repository.MongoRepository; // trae metodos de mongo predefinidos pero para mayor control haremos los nuestros

import org.springframework.stereotype.Repository;

import java.util.List;


public interface ConnectionsRepositoryCustom{


Boolean CreateOrUpdateConnection(); //actualiza un registro especifico en el modelo Connections en la db de mongo
Boolean UpdateDisconnection(); //actualiza un registro especifico en el modelo Connections en la db de mongo
} 