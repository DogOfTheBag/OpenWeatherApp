package com.example.openweatherapp.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**/

@Entity(tableName = "ciudades")
public class Ciudad {
    @PrimaryKey (autoGenerate = true)
    private Integer id;
    private String nombre;

    private Double tempMedia;

    //usaremos esta variable para mapear los gifs que le pongamos a las ciudades del tiempo
    private Integer idTiempo;

    private String descripcion;


    public Ciudad(String nombre, Double tempMedia, Integer idTiempo, String descripcion) {
        this.nombre = nombre;
        this.tempMedia = tempMedia;
        this.idTiempo = idTiempo;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getTempMedia() {
        return tempMedia;
    }

    public Integer getIdTiempo() {
        return idTiempo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
