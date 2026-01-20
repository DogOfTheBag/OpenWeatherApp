package com.example.openweatherapp.controlador.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.openweatherapp.modelo.Ciudad;
import com.example.openweatherapp.modelo.repository.CiudadRepository;

import java.util.List;

public class CiudadViewModel extends AndroidViewModel {

    private CiudadRepository repo;

    public CiudadViewModel(@NonNull Application app){
        super(app);
        repo = new CiudadRepository(app);
    }


    public LiveData<List<Ciudad>> getCiudades() {
        return repo.getCiudades();
    }

    public void insertar(Ciudad c) {
        repo.insertar(c);
    }
}
