package com.example.rafa.srevento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListaAcontecimientosActivity extends AppCompatActivity {

    private static final String ACTIVITY = "ListaAcontecimientosActivity";
    private ArrayList<AcontecimientoItem> items;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(ACTIVITY, "On create...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_acontecimientos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botón flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaAcontecimientosActivity.this, BuscarAcontecimientoActivity.class);
                startActivity(intent);
            }
        });

        myContext = this;

        listaAcontecimientos();
    }

    /**
     * Método que creará un elemento RecyclerView con los acontecimientos que recoga nuestra aplicación
     * después los mostrará en pantalla.
     */

    public void listaAcontecimientos(){
        TextView tv_Error = (TextView) findViewById(R.id.textViewError);
        items = new ArrayList<>();
        int id = -1;
        String nombre;
        String inicio;
        String fin;

        //Abrimos la base de datos en modo lectura
        // Recibe el contexto y la ruta de la base de datos
        AcontecimientoSQLiteHelper usdbh =
                new AcontecimientoSQLiteHelper(myContext, Environment.getExternalStorageDirectory()+"/srevento.db", null, 1);

        // creamos la variable de la base de datos
        SQLiteDatabase db = usdbh.getReadableDatabase();

        // la sentencia SQL
        String selectSQL = "SELECT * FROM acontecimiento ORDER BY inicio DESC";
        Cursor c = db.rawQuery(selectSQL, null);

        if(c.getCount() == 0){
            tv_Error.setText("No existen datos en la BBDD");
        } else {

            // Buscamos el layout principal para eliminar el textView
            LinearLayout layoutPrincipal = (LinearLayout) findViewById(R.id.content_lista_acontecimientos);
            layoutPrincipal.removeView(tv_Error);

            while (c.moveToNext()) {
                id = c.getInt(c.getColumnIndex("id"));
                nombre = c.getString(c.getColumnIndex("nombre")); //recogemos los datos de la columna 'nombre' de la base de datos
                inicio = c.getString(c.getColumnIndex("inicio"));
                fin = c.getString(c.getColumnIndex("fin"));

                try {
                    // creamos el formatero de como lo recoge en la base de datos
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                    Date fechaInicio = dateFormat.parse(inicio);
                    Date fechaFin = dateFormat.parse(fin);

                    // creamos el formato en el que lo va a mostrar
                    SimpleDateFormat formatPrint = new SimpleDateFormat("d/M/y hh:mm");
                    String inicioFormat = formatPrint.format(fechaInicio);
                    String finFormat = formatPrint.format(fechaFin);

                    // Creamos el objeto
                    AcontecimientoItem acontecimiento = new AcontecimientoItem(String.valueOf(id), nombre, inicioFormat, finFormat);
                    items.add(acontecimiento);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Se inicializa el RecyclerView
            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            // Se crea el Adaptador con los datos
            AcontecimientoAdapter adaptador = new AcontecimientoAdapter(items);

            // Se asocia el elemento con una acción al pulsar el elemento
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildAdapterPosition(v);
                    // creamos el archivo sharedPreferences
                    SharedPreferences prefs = myContext.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("id", items.get(position).getId());
                    editor.commit();

                    Intent intent = new Intent(myContext, MostrarAcontecimientoScrollActivity.class);
                    myContext.startActivity(intent);
                }
            });

            // Se asocia el Adaptador al RecyclerView
            recyclerView.setAdapter(adaptador);

            // Se muestra el RecyclerView en vertical
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onStart() {
        MyLog.d(ACTIVITY, "On Start...");
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyLog.d(ACTIVITY, "On resume...");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MyLog.d(ACTIVITY, "On pause...");
        super.onPause();
    }

    @Override
    protected void onStop() {
        MyLog.d(ACTIVITY, "On stop...");
        super.onStop();

    }

    @Override
    protected void onRestart() {
        MyLog.d(ACTIVITY, "On restart...");
        super.onRestart();

        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onDestroy() {
        MyLog.d(ACTIVITY, "On destroy...");
        super.onDestroy();
    }

    /**
     * Método para que nos aparezca el menu_main en nuestra aplicación
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Este método sirve para cada vez que cliquemos en un elemento del menú,
     * nos lleve a la actividad correspondiente que hayamos pulsado
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // action_aboutus es la id del elemento en el menu_main.xml
        if (id == R.id.action_acercade) {
            startActivity(new Intent(this, AcercaDeActivity.class));
            return true;
        }

        // action_settings es la id del elemento en el menu_main.xml
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        }

        if (id == R.id.content_lista_acontecimientos) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
