package com.example.openweatherapp.vista;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweatherapp.R;
import com.example.openweatherapp.modelo.Ciudad;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
/*la clase estándar que vimos para hacer el recyclerView*/
public class CiudadAdapter extends RecyclerView.Adapter<CiudadAdapter.ciudadVH> {

    /*Creamos la lista de ciudades que tenemos en la app, y después usaremos la lista de ciudades del dao
     * para que cada vez que abramos la app se ponga la lista de ciudades guardadas.*/
    private final List<Ciudad> lista = new ArrayList<>();

    public void setLista(List<Ciudad> nueva){
        lista.clear();
        if(nueva != null)
            lista.addAll(nueva);
        //este metodo llama al recycler view para que pinte de nuevo la lista por los cambios que ha habido
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ciudadVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        /*En vez de crear el inflater donde los parametros hacemos ambas cosas en una sola linea, es lo mismo usando .from*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ciudad, parent, false);
        return new ciudadVH(v);
    }




    @Override
    public void onBindViewHolder(@NonNull ciudadVH v, int position){
        Ciudad c = lista.get(position);
        v.txtNombre.setText(c.getNombre());
        v.txtTemp.setText(c.getTempMedia() + "°C");
        v.txtEstado.setText(c.getDescripcion()); // o lo que uses

        int gif = gifPorWeatherId(c.getIdTiempo());

        Glide.with(v.imgTiempo.getContext())
                .asGif()
                .load(gif)
                .into(v.imgTiempo);
    }

    /*TENGO QUE BUSCAR LOS GIFS Y METERLOS EN LA CARPETA DE DRAWABLE*/
    private int gifPorWeatherId(Integer id) {
        if (id == null) return R.drawable.gif_desconocido;

        if (id >= 200 && id < 600) return R.drawable.gif_lluvia;
        if (id >= 600 && id < 700) return R.drawable.gif_nieve;
        if (id == 800) return R.drawable.gif_sol;
        if (id >= 801 && id <= 804) return R.drawable.gif_nublado;

        return R.drawable.gif_desconocido;
    }





    @Override
    public int getItemCount(){
        return lista.size();
    }

    /*el viewholder, la vista de la ciudad sera el icono, nombre, temperatura, y estado.*/
    static class ciudadVH extends RecyclerView.ViewHolder{
        TextView txtNombre, txtTemp, txtEstado;
        ImageView imgTiempo;

        public ciudadVH (@NonNull View itemView){
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTemp = itemView.findViewById(R.id.txtTemp);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgTiempo = itemView.findViewById(R.id.imgTiempo);
        }

    }

}
