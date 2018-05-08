/* implementa los metodos expuestos en la interfaz ConnectionsRepositoryCustom (les da funcionalidad el Impl es obligatorio)
*/


package com.hugestudio.apkWebBuildProject.repository.Connections;

import com.hugestudio.apkWebBuildProject.models.ConnectionsModel; //de esta forma importamos el modelo que necesita el repositorio

import org.springframework.data.mongodb.repository.MongoRepository; // trae metodos de mongo predefinidos pero para mayor control haremos los nuestros

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;



public class ConnectionsRepositoryImpl implements ConnectionsRepositoryCustom{

// metodos de MongoDB para este repositorio

	 @Autowired
    MongoTemplate mongoOperation;


@Override
   public Boolean CreateOrUpdateConnection(){//actualiza un registro especifico en el modelo Connections en la db de mongo


try{


		Query query = new Query();

		int numConnections = 0;

		int totalConnections = 0; //para registro de totales de conexiones que ha tenido el programa 

		String id ="connections";


		// primero consultamos la cantidad de usuarios conectados, como el query esta vacio agarra el primer registro en el modelo de connections
		ConnectionsModel Connections = mongoOperation.findOne(query, ConnectionsModel.class); //query + modelo (numConnections, totalConnections)

		if(Connections!=null){ //si existe el modelo con datos, recolectamos la cantidad de usuarios conectados

		numConnections = Connections.numConnections;

        totalConnections = Connections.totalConnections;

        id = Connections.id;
		}

		numConnections = numConnections + 1; //nuevo usuario conectado

		totalConnections = totalConnections + 1; //para tener un conteo del total de usuarios conectados

		if(numConnections>2){ //se excedio la cantidad de usuarios conectados


		System.out.println("No more than 2 users connected");


		return false;

		}


		else{



		query.addCriteria(Criteria.where("_id").is(id)); //condicion para hacer el update, buscamos el id

		Update update = new Update();
		
		update.set("numConnections", numConnections);

		update.set("totalConnections", totalConnections);

		mongoOperation.upsert(query, update, ConnectionsModel.class); //si no existe lo crea...si existe lo actualiza


		System.out.println("Connected to the server");

		return true;

}

}catch(Exception e){System.out.println("en CreateOrUpdateConnection " + e); return false;}

   } 



@Override
   public Boolean UpdateDisconnection(){//actualiza un registro especifico en el modelo Connections en la db de mongo


try{


		Query query = new Query();

		int numConnections = 0;

		String id ="connections";


		// primero consultamos la cantidad de usuarios conectados, como el query esta vacio agarra el primer registro en el modelo de connections
		ConnectionsModel Connections = mongoOperation.findOne(query, ConnectionsModel.class); //query + modelo (numConnections, totalConnections)

		if(Connections!=null){ //si existe el modelo con datos, recolectamos la cantidad de usuarios conectados

		numConnections = Connections.numConnections;

        id = Connections.id;
		}


		if(numConnections>0){ //si hay por lo menos una conexion



		System.out.println("Users connected: "+numConnections);


		numConnections = numConnections -1; //le restamos el usuario desconectado

		query.addCriteria(Criteria.where("_id").is(id)); //condicion para hacer el update, buscamos el id

		Update update = new Update();
		
		update.set("numConnections", numConnections);

		mongoOperation.updateFirst(query, update, ConnectionsModel.class); //si no existe lo crea...si existe lo actualiza

		System.out.println("Disconnected successfully");


		return true;

		}


		else{


		System.out.println("This user was not connected");



		return false;

}

}catch(Exception e){System.out.println("en UpdateDisconnection " + e); return false;}

   } 


} 