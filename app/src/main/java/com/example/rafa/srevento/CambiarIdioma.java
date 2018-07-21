package com.example.rafa.srevento;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Rafa on 09/02/2017.
 */

public class CambiarIdioma extends Activity{
    /**
     * Método con el que recogemos de las prefencias la parte de idioma_list y leemos
     * el idioma elegido por el usuario.
     * Lo declaramos estático para poder ser llamado desde cualquier clase
     */
    public static void cambiar(Activity activity){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        String cod_lenguaje = pref.getString("idioma_list", "");

        Resources res = activity.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(cod_lenguaje);
        res.updateConfiguration(conf, dm);;
    }
}
