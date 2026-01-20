package com.example.openweatherapp.modelo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;


/*Para crear nuestra base de datos usaremos el patrón de diseño singleton
 * este patron basicamente se resume en solo crear una sola (single) instancia de una clase en la app
 * puesto que solo queremos una base de datos en nuestra app, lo usamos para crearla
 * de esta forma optimizamos el que solo se cree una db y evitamos errores de duplicidad*/
@Database(entities = {Ciudad.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract CiudadDAO ciudadDAO();

    public static volatile AppDB INSTANCE;
    //voy a explicar esto
    public static AppDB getInstance(Context context){
        //se comprueba si hay una instancia creada de la base de datos
        if(INSTANCE == null){
            /*si no la hay, usamos synchronized para que un único hilo pueda crear la base de datos
             * y no haya problemas*/
            synchronized (AppDB.class){
                //otra comprobacion de si existe por seguridad
                if(INSTANCE == null) {
                    /*si no existe, creamos la instancia de la base de datos usando databaseBuilder
                     * le pasamos el concepto de la clase, el .class de la clase actual, el nombre de la base de datos
                     * y el callback para cuando se cree la bd, y hacemos el build.*/
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "Tiempo_db").build();
                }
            }
        }
        return INSTANCE;
    }

}
