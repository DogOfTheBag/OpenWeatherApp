package com.example.openweatherapp.modelo.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.openweatherapp.modelo.AppDB;
import com.example.openweatherapp.modelo.Ciudad;
import com.example.openweatherapp.modelo.CiudadDAO;

import java.util.List;
import java.util.concurrent.Executors;

public class CiudadRepository {
    private CiudadDAO dao;
    private LiveData<List<Ciudad>> ciudades;

    public CiudadRepository(Application app){
        AppDB db = AppDB.getInstance(app);
        dao = db.ciudadDAO();
        ciudades = dao.getCiudades();

    }

    public LiveData<List<Ciudad>> getCiudades() {
        return ciudades;
    }

    public void insertar (Ciudad c){
        Executors.newSingleThreadExecutor().execute(() -> dao.insertar(c));
    }
}
