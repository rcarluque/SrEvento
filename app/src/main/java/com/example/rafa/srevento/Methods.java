package com.example.rafa.srevento;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by crafa on 15/03/2018.
 */

class Methods {
    private static final Methods ourInstance = new Methods();

    static Methods getInstance() {
        return ourInstance;
    }

    private Methods() {
    }

    public SQLiteDatabase getReadBBDD(Context context){
        SQLiteDatabase db;

        //Abrimos la base de datos en modo lectura
        // Recibe el contexto y la ruta de la base de datos
        AcontecimientoSQLiteHelper usdbh =
                new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory() + "/srevento.db", null, 1);

        // creamos la variable de la base de datos
        db = usdbh.getReadableDatabase();
        return db;
    }

    public SharedPreferences getSharedPrefs(Context context){
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        return prefs;
    }

}
