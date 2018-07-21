package com.example.rafa.srevento;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by Rafa on 14/02/2017.
 * Clase con métodos estáticos los cuales podremos llamar desde la clase en la que los vayamos a necesitar.
 * El primer método devuelve un objeto de tipo SQLiteDatabase, db, que es la base de datos interna del móvil
 * El segundo método devuelve un objeto de tipo SharedPreferences, prefs, que es el archivo de Preferencias guardado en el teléfono
 */

public class DatosPrefs{

    public static SQLiteDatabase getBBDD(Context context){
        SQLiteDatabase db;

        //Abrimos la base de datos en modo lectura
        // Recibe el contexto y la ruta de la base de datos
        AcontecimientoSQLiteHelper usdbh =
                new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory() + "/srevento.db", null, 1);

        // creamos la variable de la base de datos
        db = usdbh.getReadableDatabase();
        return db;
    }

    public static SharedPreferences getPrefs(Context context){
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        return prefs;
    }


}
