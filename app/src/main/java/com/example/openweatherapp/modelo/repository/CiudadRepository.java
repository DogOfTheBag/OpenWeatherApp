package com.example.openweatherapp.modelo.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.openweatherapp.modelo.AppDB;
import com.example.openweatherapp.modelo.Ciudad;
import com.example.openweatherapp.modelo.CiudadDAO;

import java.util.List;
import java.util.concurrent.Executors;

/*En vez de hacer la actividad gestione el DAO directamente, podemos hacer una clase Repository que maneje las operaciones del dao
* puesto que solo queremos hacer insert, pues hacemos un getter y un insertar. Asi queda mejor.*/
public class CiudadRepository {
    private CiudadDAO dao;
    private LiveData<List<Ciudad>> ciudades;

    /*Le pasamos la app ya que dentro de la clase de la base de datos, getInstance usa el contexto de la app para crear la
    * base. Room te pide el contexto para poder hacer la instancia de la base de datos.*/
    public CiudadRepository(Application app){
        AppDB db = AppDB.getInstance(app);
        dao = db.ciudadDAO();
        ciudades = dao.getCiudades();

    }

    public LiveData<List<Ciudad>> getCiudades() {
        return ciudades;
    }

    //hacemos la insercion con hilo para no que no se pare la app ni nada durante la operacion
    public void insertar (Ciudad c){
        Executors.newSingleThreadExecutor().execute(() -> dao.insertar(c));
    }
}
