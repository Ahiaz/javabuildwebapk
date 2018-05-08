/* Repositorio para crear consultas en el modelo Connections MongoDB

extiende de una interfaz creada desde cero para poder implementar consultas propias a la base de datos
evitando utilizar las que trae por defecto el import org.springframework.data.mongodb.repository.MongoRepository;
porque es muy limitado en consultas complejas
// si queremos nuestras propias consultas a mongo https://docs.spring.io/spring-data/data-document/docs/current/reference/html/#repositories.custom-implementations

*/


package com.hugestudio.apkWebBuildProject.repository.Connections;

import com.hugestudio.apkWebBuildProject.models.ConnectionsModel; //de esta forma importamos el modelo que necesita el repositorio

import org.springframework.data.mongodb.repository.MongoRepository; 

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

import java.util.List;


@Repository

public interface ConnectionsRepository extends MongoRepository <ConnectionsModel, String>, ConnectionsRepositoryCustom{

// los metodos ya han sido definidos en la interfaz custom (connectionsRepositoryCustom) pueden ser utilizados junto con los
//que proporciona por defecto MongoRepository https://docs.spring.io/spring-data/data-document/docs/current/reference/html/#repositories.custom-implementations

// ACA se declaran solo los que trae Mongorepository por defecto


} 