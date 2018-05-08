/* Modelo para respuesta de peticion
*/

package com.hugestudio.apkWebBuildProject.models;


public class ResponseStatus {

  public Boolean ok; //si esta ok (true or false)

  //constructores

      public ResponseStatus() {
    }

    public ResponseStatus(Boolean ok) { //para darle valores al modelo
        this.ok = ok;
    }

}