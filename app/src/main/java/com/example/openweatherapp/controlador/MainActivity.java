package com.example.openweatherapp.controlador;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweatherapp.R;
import com.example.openweatherapp.controlador.ActivityAñadir;
import com.example.openweatherapp.controlador.viewmodel.CiudadViewModel;
import com.example.openweatherapp.modelo.AppDB;
import com.example.openweatherapp.modelo.Ciudad;
import com.example.openweatherapp.modelo.CiudadDAO;
import com.example.openweatherapp.vista.CiudadAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executors;

/*La primera parte de este ejercicio (la actividad) tendrá un foco en crear una base de datos
 * usando AndroidRoom (sqlite). Lo bueno de usar esto, es que es prácticamente como usar JPA, pero en android
 * además de que tenemos la base de datos insertada en la app.
 * en la segunda parte sustituiré la db con la llamada de la api REST que ya nos dará directamente los datos
 * y no hará falta almacenarlos.*/

public class MainActivity extends AppCompatActivity {

    private CiudadAdapter adapter;
    private CiudadViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //el ejercicio nos pide el action bar, asi que le ponemos el titulo de la app arriba
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("OPEN WEATHER APP");
        }

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CiudadAdapter();
        recycler.setAdapter(adapter);

        /*Como nos pide el ejercicio, usaremos un ViewModel para que que directamente la app se ponga a observar
        * le pasamos el viewmodel usando viewModelProvider, que coge la clase que hemos hecho, y se pone a
        * observar cambios que haya en el listadso de ciudades*/
        vm = new ViewModelProvider(this).get(CiudadViewModel.class);
        /*lo mismo que en weather app, el viewmodel observa cambios en el listado, y si los hay, cogemos la lista
        * que tenemos y la ponemos en el adapter con el metodo reference (::)*/
        vm.getCiudades().observe(this, adapter::setLista);

        //el boton nos ejecutará el metodo para cambiarnos a la actividad de añadir ciudades
        FloatingActionButton boton = findViewById(R.id.botonFlotante);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ActivityAñadir.class);
                startActivity(i);
            }
        });
    }

}