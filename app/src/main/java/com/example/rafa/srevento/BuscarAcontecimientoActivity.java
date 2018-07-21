package com.example.rafa.srevento;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BuscarAcontecimientoActivity extends AppCompatActivity {

    private static final String ACTIVITY = "BuscarAcontecimientoActivity";
    public static Context myContext;
    final private int REQUEST_CODE_INTERNET = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_acontecimiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // le asignamos a la variable de myContext el valor this, que irá tomando el valor de
        // la clase anadirAcontecimiento
        myContext = this;

        abreLink();
    }

    /**
     * Con este método recogemos los datos del editText de la ID y los guardamos en una variable de tipo editText para
     * luego convertirla a String. Posteriormente comprobamos que cuando se pulse el botón de descargar
     * se abra la web de json con los datos del servidor. Sólo si el campo no está vacio
     */

    private void abreLink(){
        // con el siguiente código hacemos que al pulsar el botón de añadir, cambie a otra actividad
        Button button_buscar = (Button) findViewById(R.id.button_buscar_acontecimiento);
        button_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline() == true) {
                    // Obtenemos el texto de busqueda
                    EditText editText_nombre = (EditText) findViewById(R.id.et_texto_buscar_acontecimiento);
                    String textoBuscar = editText_nombre.getText().toString();

                    // Código con el cual ocultaremos el teclado al pulsar el botón
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText_nombre.getWindowToken(), 0);

                    if (textoBuscar.length() == 0) {
                        Snackbar.make(v, R.string.campo_vacio, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        int permissionCheck = ContextCompat.checkSelfPermission(myContext, Manifest.permission.INTERNET);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                            new BuscaAcontecimientoAsyncTask(textoBuscar, myContext).execute();
                        } else {
                            if (Build.VERSION.SDK_INT >= 23) {
                                // Explicar permiso
                                if (ActivityCompat.shouldShowRequestPermissionRationale(BuscarAcontecimientoActivity.this, Manifest.permission.INTERNET)) {
                                    Toast.makeText(myContext, "Se necesita permiso de conexión a internet",
                                            Toast.LENGTH_SHORT).show();
                                }
                                // Solicitar el permiso
                                ActivityCompat.requestPermissions(BuscarAcontecimientoActivity.this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_INTERNET);
                            } else {
                                Snackbar.make(v, "Mensaje por defecto", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Método para comprobar que la conexión a internet está activada
     * @return true en caso de estar activada. False en caso de que no, y muestra un toast
     */

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(myContext, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*
    * Metódos para observar el cambio de las actividades entre sí
    * El inicio,pausa,destrucción,reiniciado y volver a utilizar la aplicación
    * */
    @Override
    protected void onStart() {
        MyLog.d(ACTIVITY, "On Start...");
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyLog.d(ACTIVITY, "On Resume...");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyLog.d(ACTIVITY, "On Pause...");
        super.onPause();
    }

    @Override
    protected void onStop() {
        MyLog.d(ACTIVITY, "On Stop...");
        super.onStop();

    }

    @Override
    protected void onRestart() {
        MyLog.d(ACTIVITY, "On Restart...");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        MyLog.d(ACTIVITY, "On Destroy...");
        super.onDestroy();
    }

}
