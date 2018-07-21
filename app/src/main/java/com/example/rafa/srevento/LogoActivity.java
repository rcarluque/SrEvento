package com.example.rafa.srevento;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LogoActivity extends AppCompatActivity {
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CambiarIdioma.cambiar(this);

        setContentView(R.layout.activity_logo);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ListaAcontecimientosActivity.class);
                startActivity(intent);
                finish();

                if (ultAcontecimiento()) {
                    Intent intentMostrar = new Intent(getApplicationContext(), MostrarAcontecimientoScrollActivity.class);
                    startActivity(intentMostrar);
                    finish();
                }
            }
        },3000);

    }

    /**
     * Método con el que comprobaremos el booleano de las PreferenceActivity
     * @return el valor que hayamos marcado en las preferencias
     */
    public boolean ultAcontecimiento(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getBoolean("ultAcontecimiento_switch", false);
    }

}
