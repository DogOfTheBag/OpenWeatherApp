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

    /*Aqui pasa lo mismo que lo mencionado en el Repository. AndroidViewModel nos guarda una referencia del
    * contexto de la app, por lo que en el constructor que tiene la clase padre directamente lo pide
    * De esta forma el viewmodel tiene una forma de guardar el estado de la app y no perderlo
    * Tendremos ademas una referencia de los metodos que usa el DAO para que luego el viewmodel sepa como cambiar
    * la ui, sin necesidad de saber estrictamente lo que hace el dao, solo saber que puede hacer.*/
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
