package com.example.rafa.srevento;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MostrarAcontecimientoScrollActivity extends AppCompatActivity {
    final private int REQUEST_CODE_INTERNET = 10;
    private Context myContext;

    private String idAcontecimiento;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_acontecimiento_scroll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CambiarIdioma.cambiar(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myContext = this;

        idAcontecimiento = Methods.getInstance().getSharedPrefs(myContext).getString("id", "no hay id");

        crearLayout();
        crearBotonFlotante(idAcontecimiento);
    }

    /**
     * Método con el que definimos la acción que queremos realizar al pulsar el icono de flecha hacia atrás.
     * En este caso hemos tenido que volver a iniciar la actividad ListaAcontecimientos porque
     * si iniciamos directamente la aplicación mostrando el último acontecimiento visitado, se cerrará
     * la aplicación.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que recoge de la base de datos local en el movil los datos.
     * Posteriomente creamos un layout linear por cada elemento recogido.
     */

    protected void crearLayout() {
        String nombre = null;
        String organizador = null;
        String descripcion = null;
        String tipo = null;
        String inicio = null;
        String fin = null;
        String direccion = null;
        String localidad = null;
        String cod_postal = null;
        String provincia = null;
        String latitud = null;
        String longitud = null;
        String telefono = null;
        String email = null;
        String web = null;
        String facebook = null;
        String twitter = null;
        String instagram = null;

        // Creamos el boton de mapas fuera por si saltase alguna excepción no fuese nulo
        Button map_button;

        // String idAcontecimiento = DatosPrefs.getPrefs(myContext).getString("id", "no hay id");
        //String idAcontecimiento = Methods.getInstance().getSharedPrefs(myContext).getString("id", "no hay id");

        // Comprobamos que exista la ID, si devuelve que no hay id retrocederá a ListaAcontecimientos
        if (idAcontecimiento.equals("no hay id")) {
            onBackPressed();
        } else {
            // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
            String[] args = new String[]{idAcontecimiento};
            // la sentencia SQL
            String selectSQL = "SELECT * FROM acontecimiento WHERE id=?";
            // creamos un cursor con la ejecución del select en la base de datos

            // Cursor c = DatosPrefs.getBBDD(myContext).rawQuery(selectSQL, args);
            Cursor c = Methods.getInstance().getReadBBDD(myContext).rawQuery(selectSQL, args);

            // Controlamos que haya datos en la BBBDD (porque puede ser que hayamos borrado la BBDD y las
            // preferencias siguen almacenadas en el móvil y puede que tengamos activado el mostrar
            // último acontecimiento visitado al iniciar la app.
            if(c.getCount() == 0){
                onBackPressed();
            } else {
                if (c.moveToFirst()) {
                    nombre = c.getString(c.getColumnIndex("nombre")); //recogemos los datos de la columna 'nombre' de la base de datos
                    organizador = c.getString(c.getColumnIndex("organizador")); //recogemos los datos de la columna 'organizador' de la base de datos
                    descripcion = c.getString(c.getColumnIndex("descripcion"));
                    tipo = c.getString(c.getColumnIndex("tipo"));
                    inicio = c.getString(c.getColumnIndex("inicio"));
                    fin = c.getString(c.getColumnIndex("fin"));
                    direccion = c.getString(c.getColumnIndex("direccion"));
                    localidad = c.getString(c.getColumnIndex("localidad"));
                    cod_postal = c.getString(c.getColumnIndex("cod_postal"));
                    provincia = c.getString(c.getColumnIndex("provincia"));
                    latitud = c.getString(c.getColumnIndex("latitud"));
                    longitud = c.getString(c.getColumnIndex("longitud"));
                    telefono = c.getString(c.getColumnIndex("telefono"));
                    email = c.getString(c.getColumnIndex("email"));
                    web = c.getString(c.getColumnIndex("web"));
                    facebook = c.getString(c.getColumnIndex("facebook"));
                    twitter = c.getString(c.getColumnIndex("twitter"));
                    instagram = c.getString(c.getColumnIndex("instagram"));
                }
                //Añadimos el LinearLayout
                LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.layout_mostrar_acontecimiento_scroll);

                // Asignamos el scrollbar al layout
                layoutPrincipal.scrollBy(0, 5);

                try {
                    // creamos el formatero de como lo recoge en la base de datos
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                    Date fechaInicio = dateFormat.parse(inicio);
                    Date fechaFin = dateFormat.parse(fin);

                    // creamos el formato en el que lo va a mostrar
                    SimpleDateFormat formatPrint = new SimpleDateFormat("d/M/y hh:mm");
                    String inicioFormat = formatPrint.format(fechaInicio);
                    String finFormat = formatPrint.format(fechaFin);

                    //Añadimos el TextView de nombre del acontecimiento. Y posteriormente hacemos lo mismo con todas las variables posteriores
                    createTextView(R.drawable.ic_nombre, nombre, layoutPrincipal);
                    createTextView(R.drawable.organizador, organizador, layoutPrincipal);
                    createTextView(R.drawable.ic_descripcion, descripcion, layoutPrincipal);
                    createTextView(R.drawable.ic_tipo, tipo, layoutPrincipal);
                    createTextView(R.drawable.ic_inicio, inicioFormat, layoutPrincipal);
                    createTextView(R.drawable.ic_fin, finFormat, layoutPrincipal);
                    createTextView(R.drawable.localidad, direccion, layoutPrincipal);
                    createTextView(R.drawable.localidad, localidad, layoutPrincipal);
                    createTextView(R.drawable.ic_email, cod_postal, layoutPrincipal);
                    createTextView(R.drawable.ic_localidad, provincia, layoutPrincipal);
                    createTextView(R.drawable.ic_telf, telefono, layoutPrincipal);
                    createTextView(R.drawable.ic_email, email, layoutPrincipal);
                    createTextView(R.drawable.ic_web, web, layoutPrincipal);

                    botonNavegador(R.drawable.ic_facebook, facebook, "https://m.facebook.com/"+facebook, layoutPrincipal);
                    botonNavegador(R.drawable.ic_twitter, twitter, "https://mobile.twitter.com/"+twitter, layoutPrincipal);
                    botonNavegador(R.drawable.ic_instagram, instagram, "https://www.instagram.com/", layoutPrincipal);

                    map_button = new Button(this);
                    map_button.setText(R.string.boton_mapa);
                    abrirMapa(latitud, longitud, map_button);
                    layoutPrincipal.addView(map_button);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creamos un método que recibirá el nombre del TV, el valor del TV, los params del alyout y el tipo de layout.
     *
     * @param imgCod
     * @param valorTV
     * @param layoutPrincipal
     */
    protected void createTextView(int imgCod, String valorTV, LinearLayout layoutPrincipal) {
        if (!valorTV.isEmpty()) {
            // Obtenemos los params del layout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            // creamos otro linear layout al cual le asignamos un estilo, después los parametros y la orientación
            LinearLayout layoutSecundario = new LinearLayout(new ContextThemeWrapper(this, R.style.AppTheme));
            layoutSecundario.setLayoutParams(params);
            layoutSecundario.setOrientation(LinearLayout.HORIZONTAL);

            // creamos el text view con el nombre que reciba
            TextView newTV = new TextView(new ContextThemeWrapper(this, R.style.AppTheme));
            // le asignamos el valor que reciva al tv
            newTV.setText(valorTV.toString());
            // le asignamos los parametros al tv
            newTV.setLayoutParams(params);

            newTV.setGravity(Gravity.CENTER);

            // creamos la imagen view, que será el icono
            ImageView iv = new ImageView(this);
            // asignamos el codigo de imagen a la imagen (esto hará que encuentre la imagen y la muestre)
            iv.setImageResource(imgCod);

            layoutSecundario.addView(iv);
            layoutSecundario.addView(newTV);

            layoutPrincipal.addView(layoutSecundario);
        }
    }

    private void botonNavegador(int imgCod, String text, final String url, LinearLayout layoutPrincipal){
        Button boton = new Button(this);
        boton.setText(text);
        Drawable icon = this.getResources(). getDrawable(imgCod);
        boton.setCompoundDrawablesWithIntrinsicBounds( icon, null, null, null );
        layoutPrincipal.addView(boton);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, NavegadorActivity.class);
                intent.putExtra("URL", url);
                myContext.startActivity(intent);
            }
        });
    }

    /**
     * Método para crear un botón flotante. En caso de que no haya eventos aparecerá un mensaje.
     */
    private void crearBotonFlotante(String id) {
        final Cursor c;

        // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
        String[] args = new String[]{id};
        String selectSQL = "SELECT nombre FROM evento WHERE id_acontecimiento=?";

        //c = DatosPrefs.getBBDD(myContext).rawQuery(selectSQL, args);
        c = Methods.getInstance().getReadBBDD(myContext).rawQuery(selectSQL, args);

        // Botón flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEvento);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c.getCount() != 0) {
                    Intent intent = new Intent(MostrarAcontecimientoScrollActivity.this, EventosActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(myContext, R.string.no_eventos_en_acontecimiento,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void abrirMapa(final String latitud, final String longitud, Button boton) {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(latitud.isEmpty() || longitud.isEmpty()) {
                    Toast.makeText(myContext, R.string.acontecimiento_sin_mapa,
                            Toast.LENGTH_SHORT).show();
                } else{
                    if (isOnline()) {

                        int permissionCheck = ContextCompat.checkSelfPermission(myContext, android.Manifest.permission.INTERNET);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                            Intent intent = new Intent(myContext, MapsActivity.class);
                            myContext.startActivity(intent);
                        } else {
                            if (Build.VERSION.SDK_INT >= 23) {
                                // Explicar permiso
                                if (ActivityCompat.shouldShowRequestPermissionRationale(MostrarAcontecimientoScrollActivity.this, android.Manifest.permission.INTERNET)) {
                                    Toast.makeText(myContext, R.string.internet_permission,
                                            Toast.LENGTH_SHORT).show();
                                }
                                // Solicitar el permiso
                                ActivityCompat.requestPermissions(MostrarAcontecimientoScrollActivity.this, new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_INTERNET);
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
    
}
