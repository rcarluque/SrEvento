package com.example.rafa.srevento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MostrarAcontecimientoActivity extends AppCompatActivity {
    private LinearLayout layoutPrincipal;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_acontecimiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myContext = this;

       crearLayout();

        crearBotonFlotante();

    }

    /**
     * Método que recoge de la base de datos local en el movil los datos.
     * Posteriomente creamos un layout linear por cada elemento recogido.
     */

    protected void crearLayout(){
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

        //Abrimos la base de datos en modo lectura
        // Recibe el contexto y la ruta de la base de datos
        AcontecimientoSQLiteHelper usdbh =
                new AcontecimientoSQLiteHelper(myContext, Environment.getExternalStorageDirectory()+"/srevento.db", null, 1);

        // creamos la variable de la base de datos
        SQLiteDatabase db = usdbh.getReadableDatabase();

        // Recogemos la id de nuestro archivo shared preferences
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

        String idAcontecimiento = prefs.getString("id", "no hay id");

        // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
        String[] args = new String[] {idAcontecimiento};
        // la sentencia SQL
        String selectSQL = "SELECT * FROM acontecimiento WHERE id=?";
        // creamos un cursor con la ejecución del select en la base de datos

        Cursor c = db.rawQuery(selectSQL, args);
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
        layoutPrincipal = (LinearLayout) findViewById(R.id.content_mostrar_acontecimiento);

        layoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        // Asignamos el scrollbar al layout
        layoutPrincipal.scrollBy(0, 5);

        //Añadimos el TextView de nombre del acontecimiento. Y posteriormente hacemos lo mismo con todas las variables posteriores
        createTextView(R.drawable.ic_nombre, nombre, layoutPrincipal);
        createTextView(R.drawable.organizador, organizador, layoutPrincipal);
        createTextView(R.drawable.ic_descripcion, descripcion, layoutPrincipal);
        createTextView(R.drawable.ic_tipo, tipo,layoutPrincipal);
        createTextView(R.drawable.ic_inicio, inicio, layoutPrincipal);
        createTextView(R.drawable.ic_fin, fin, layoutPrincipal);
        createTextView(R.drawable.localidad, direccion, layoutPrincipal);
        createTextView(R.drawable.localidad, localidad, layoutPrincipal);
        createTextView(R.drawable.ic_email, cod_postal, layoutPrincipal);
        createTextView(R.drawable.ic_localidad, provincia, layoutPrincipal);
        createTextView(R.drawable.lngylat, latitud, layoutPrincipal);
        createTextView(R.drawable.lngylat, longitud, layoutPrincipal);
        createTextView(R.drawable.ic_telf, telefono, layoutPrincipal);
        createTextView(R.drawable.ic_email, email, layoutPrincipal);
        createTextView(R.drawable.ic_web, web, layoutPrincipal);
        createTextView(R.drawable.ic_facebook, facebook, layoutPrincipal);
        createTextView(R.drawable.ic_twitter, twitter, layoutPrincipal);
        createTextView(R.drawable.ic_instagram, instagram, layoutPrincipal);

    }

    /**
     * Creamos un método que recibirá el nombre del TV, el valor del TV, los params del alyout y el tipo de layout.
     * @param imgCod
     * @param valorTV
     * @param layoutPrincipal
     */
    protected void createTextView(int imgCod, String valorTV, LinearLayout layoutPrincipal) {
        if (!valorTV.isEmpty()) {
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

    /**
     * Método para crear un botón flotante. En caso de que no haya eventos aparecerá un mensaje.
     */
    private void crearBotonFlotante() {
        final Cursor c;
        // Conectamos con la base de datos
        AcontecimientoSQLiteHelper usdbh =
                new AcontecimientoSQLiteHelper(myContext, Environment.getExternalStorageDirectory()+"/srevento.db", null, 1);
        SQLiteDatabase db = usdbh.getReadableDatabase();

        // Recogemos la id de nuestro archivo shared preferences
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

        String idAcontecimiento = prefs.getString("id", "no hay id");

        // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
        String[] args = new String[] {idAcontecimiento};
        String selectSQL = "SELECT nombre FROM evento WHERE id_acontecimiento=?";
        c = db.rawQuery(selectSQL, args);

        // Botón flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEvento);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
               if(c.getCount() != 0){
                   Intent intent = new Intent(MostrarAcontecimientoActivity.this, EventosActivity.class);
                   startActivity(intent);
               } else {
                   Toast.makeText(myContext, "Este acontecimiento no tiene eventos",
                           Toast.LENGTH_SHORT).show();
               }
                }
            });

    }

}
