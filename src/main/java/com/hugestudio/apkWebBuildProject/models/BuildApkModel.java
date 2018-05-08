/* Modelo Connections para MongoDB
*/


package com.hugestudio.apkWebBuildProject.models;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;


@Document(collection = "webapk") // crea este modelo en la base de datos si no existe

public class BuildApkModel {

  //atributos o propiedades
  @Id
  public String id; //automatico el id
  public String appName; //almacena el nombre de la app
  public Boolean showImageLoading;
  public String loadingImagePosition;
  public String siteUrl;
  public String splashBGColor;
  public String exitFooter;
  public String exitFooterBgColor;
  public String exitTag;
  public String exitTagColor;
  
  //constructores

      public BuildApkModel() {
    }

    public BuildApkModel(String appName, Boolean showImageLoading, String loadingImagePosition, String siteUrl, String splashBGColor, String exitFooter, String exitFooterBgColor, String exitTag, String exitTagColor) { //para darle valores al modelo (para el save method)
        this.appName = appName;
        this.showImageLoading = showImageLoading;
        this.loadingImagePosition = loadingImagePosition;
        this.siteUrl = siteUrl;
        this.splashBGColor = splashBGColor;
        this.exitFooter = exitFooter;
        this.exitFooterBgColor = exitFooterBgColor;
        this.exitTag = exitTag;
        this.exitTagColor = exitTagColor;
    }

}

