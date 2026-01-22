package com.example.openweatherapp.controlador;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import com.example.openweatherapp.R;
import com.example.openweatherapp.controlador.viewmodel.CiudadViewModel;
import com.example.openweatherapp.modelo.Ciudad;
import com.example.openweatherapp.red.OpenWeatherClient;
import com.example.openweatherapp.red.OpenWeatherService;
import com.example.openweatherapp.red.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAñadir extends AppCompatActivity {

    private SearchView buscarCiudad;
    //he puesto una barra de progeso mientras retorna la busqueda de la api para que quede mas pro
    private ProgressBar progreso;
    private TextView txtCiudad, txtTemp, txtDesc;
    private ImageView imgEstado;
    private Button btnAdd;

    private CiudadViewModel vm;
    private Ciudad ciudadActual;
    private static final String API_KEY = "854b91f7c166f570214ed98e66d4c256";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /*Esto es importante. Le pasamos a la actividad el ViewModel en vez del repository
        * para que maneje directamente la base de datos.
        * Esto se puede hacer debido a que el respository tiene el DAO, que a su vez tiene la instancia
        * de la base de datos, por lo que si operamos directamente con el viewModel, actualizamos la UI y la
        * DB a la vez, por lo que nos evitamos lios y problemas*/
        vm = new ViewModelProvider(this).get(CiudadViewModel.class);

        buscarCiudad = findViewById(R.id.buscarCiudad);
        progreso = findViewById(R.id.progreso);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtTemp = findViewById(R.id.txtTemp);
        txtDesc = findViewById(R.id.txtDesc);
        imgEstado = findViewById(R.id.imgEstado);
        btnAdd = findViewById(R.id.btnAdd);

        //si pulsamos el boton de añadir se guarda en la db y manda mensajito
        btnAdd.setOnClickListener(v -> {
            if (ciudadActual == null) return;
            vm.insertar(ciudadActual);
            Toast.makeText(this, "Ciudad añadida", Toast.LENGTH_SHORT).show();
            finish();
        });

        //esto es para que al pulsar enter la barra de busqueda busque la ciudad directamente
        buscarCiudad.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        /*Vamos a hacer un listener que actue cuando busquemos la query que deseemos
        * para ello usaremos dentro onQueryTextSubmit, que basicamente actua cuando se pasa un texto de
        * query. Tambien tenemos onQueryTextChanged que cambiaria cada vez que cambia el texto, pero creo
        * que este es más apropiado para este caso
        * aun asi nos obliga implementarlo si o si, pero lo dejo sin usar porque no me interesa*/
        buscarCiudad.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String ciudad = query.trim();
                if (ciudad.isEmpty()) return false;
                //IMPORTANTE SEPARAR ESTO DEL METODO DE ABAJO
                /*aqui usamos el metodo auxiliar para buscar la ciudad*/
                buscarCiudadEnApi(ciudad);
                //aqui quitamos el foco de la barra de busqueda para que se quite el teclado del movil cuando buscamos
                buscarCiudad.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //el metodo que hace toda la chicha, voy a ir explicando todo lo importante nuevo
    private void buscarCiudadEnApi(String ciudad) {
        //hacemos que el boton no pueda pulsarse a no ser que hayamos encontrado una ciudad valida
        btnAdd.setEnabled(false);
        ciudadActual = null;
        progreso.setVisibility(ProgressBar.VISIBLE);

        /*Haremos un servicio al que llamaremos api, ya que ahi es donde hemos puesto las querys que
        * usaremos para pedir los datos a la pagina. Para conseguirlo, usamos el getService del client
        * y se lo asignamos*/
        OpenWeatherService api = OpenWeatherClient.getService();

        /*Una vez hecho, usamos el metodo de la interfaz para buscar la ciudad y le pasamos la ciudad introducida
        * en la barra de busqueda con la clave, unidades e idioma, y la metemos en cola*/
        api.getByCiudad(ciudad, API_KEY, "metric", "es").enqueue(new Callback<WeatherResponse>() {
            @Override
            /*Al recibir la respuesta, quitamos la visibilidad de la barra de progreso primero de todo*/
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                progreso.setVisibility(ProgressBar.GONE);

                //si la respuesta ha sido fallida avisamos
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ActivityAñadir.this, "No encontrada o error (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*Si ha sido buena, cogemos el cuerpo de la respuesta y se lo asignamos a un objeto WeatherResponse
                * al que despues le asignaremos los parámetros que nos ha pasado la respuesta en el JSON*/
                WeatherResponse wr = response.body();

                String nombre = wr.name;
                double temp = wr.main.temp;

                /*Otra expresion lambda, la ? y : son un if else basicamente
                * comprobamos el id y la descripcion, si estan vacios o son null ponemos id y descripcion como desconocido para el logo
                * si no el que sea
                * (a partir de la ? es lo que hace el if si se cumple, si no hace a partir del :)*/
                int weatherId = (wr.weather != null && !wr.weather.isEmpty()) ? wr.weather.get(0).id : 0;
                String desc = (wr.weather != null && !wr.weather.isEmpty()) ? wr.weather.get(0).description : "Desconocido";

                /*cogemos los datos que nos han pasado y se lo ponemos a la vista*/
                txtCiudad.setText(nombre);
                txtTemp.setText("Temperatura: " + temp + "°C");
                txtDesc.setText(desc);

                imgEstado.setImageResource(iconoPorWeatherId(weatherId));

                //hacemos una ciudad auxiliar para guardarla si pulsamos el boton y lo activamos para que lo pueda guardar
                ciudadActual = new Ciudad(nombre, temp, weatherId, desc);

                btnAdd.setEnabled(true);
            }

            //si fallase la conexion a la api ponemos un mensaje con el error
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                progreso.setVisibility(ProgressBar.GONE);
                Toast.makeText(ActivityAñadir.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //para que se vean los drawables en esta actividad tambien
    private int iconoPorWeatherId(int id) {
        if (id >= 200 && id < 600) return R.drawable.ic_lluvia;
        if (id >= 600 && id < 700) return R.drawable.ic_nieve;
        if (id == 800 && id == 801) return R.drawable.ic_sol; //voy a contar sol completo y pocas nubes como sol
        if (id >= 802 && id <= 804) return R.drawable.ic_nube; //nubes dispersas y nubes cuenta como nubes
        return R.drawable.ic_desconocido;
    }
}
