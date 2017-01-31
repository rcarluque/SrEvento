package com.example.rafa.srevento;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rafa on 08/11/2016.
 */

public class AcontecimientoSQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Acontecimiento
    String sqlCreateAcontecimiento = "CREATE TABLE acontecimiento (id int(11) PRIMARY KEY, nombre varchar(256) NOT NULL, organizador varchar(256)," +
            " descripcion varchar(1024) NOT NULL, tipo int(11) NOT NULL, portada varchar(256), inicio datetime NOT NULL, fin datetime NOT NULL," +
            " direccion varchar(256), localidad varchar(256), cod_postal int(5), provincia varchar(256), longitud float, latitud float, " +
            " telefono int(9), email varchar(256), web varchar(256), facebook varchar(256), twitter varchar(256), instagram varchar(256) );";

    String sqlCreateEvento = " CREATE TABLE evento (id int(11) PRIMARY KEY, id_acontecimiento int(11), nombre varchar(256) NOT NULL," +
            "  descripcion varchar(2014) NOT NULL, inicio datetime NOT NULL, fin datetime NOT NULL, direccion varchar(256)," +
            "  localidad varchar(256), cod_postal int(5), provincia varchar(256), longitud float, latitud float," +
            "  FOREIGN KEY (id_acontecimiento) REFERENCES acontecimiento(id) );";

    public AcontecimientoSQLiteHelper(Context contexto, String nombre,
                                      SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreateAcontecimiento);
        db.execSQL(sqlCreateEvento);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Acontecimiento");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreateAcontecimiento);
        db.execSQL(sqlCreateEvento);
    }
}
