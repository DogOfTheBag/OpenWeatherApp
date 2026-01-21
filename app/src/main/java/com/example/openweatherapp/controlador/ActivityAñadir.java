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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Añadir ciudad");
        }

        vm = new ViewModelProvider(this).get(CiudadViewModel.class);

        buscarCiudad = findViewById(R.id.buscarCiudad);
        progreso = findViewById(R.id.progreso);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtTemp = findViewById(R.id.txtTemp);
        txtDesc = findViewById(R.id.txtDesc);
        imgEstado = findViewById(R.id.imgEstado);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> {
            if (ciudadActual == null) return;
            vm.insertar(ciudadActual);
            Toast.makeText(this, "Ciudad añadida", Toast.LENGTH_SHORT).show();
            finish();
        });


        buscarCiudad.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        buscarCiudad.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String ciudad = query.trim();
                if (ciudad.isEmpty()) return false;
                buscarCiudadEnApi(ciudad);
                buscarCiudad.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void buscarCiudadEnApi(String ciudad) {
        btnAdd.setEnabled(false);
        ciudadActual = null;
        progreso.setVisibility(ProgressBar.VISIBLE);

        OpenWeatherService api = OpenWeatherClient.getService();


        api.getByCiudad(ciudad, API_KEY, "metric", "es").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                progreso.setVisibility(ProgressBar.GONE);

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ActivityAñadir.this, "No encontrada o error (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                WeatherResponse wr = response.body();

                String nombre = wr.name;
                double temp = wr.main.temp;

                int weatherId = (wr.weather != null && !wr.weather.isEmpty()) ? wr.weather.get(0).id : 0;
                String desc = (wr.weather != null && !wr.weather.isEmpty()) ? wr.weather.get(0).description : "Desconocido";

                txtCiudad.setText(nombre);
                txtTemp.setText("Temperatura: " + temp + "°C");
                txtDesc.setText(desc);

                imgEstado.setImageResource(iconoPorWeatherId(weatherId));

                ciudadActual = new Ciudad(nombre, temp, weatherId, desc);

                btnAdd.setEnabled(true);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                progreso.setVisibility(ProgressBar.GONE);
                Toast.makeText(ActivityAñadir.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int iconoPorWeatherId(int id) {
        if (id >= 200 && id < 600) return R.drawable.ic_lluvia;
        if (id >= 600 && id < 700) return R.drawable.ic_nieve;
        if (id == 800 && id == 801) return R.drawable.ic_sol; //voy a contar sol completo y pocas nubes como sol
        if (id >= 802 && id <= 804) return R.drawable.ic_nube; //nubes dispersas y nubes cuenta como nubes
        return R.drawable.ic_desconocido;
    }
}
