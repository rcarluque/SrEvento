package com.example.rafa.srevento;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class EventosActivity extends AppCompatActivity implements ListadoEventoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d("EventosActivity", "onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        // Comprueba si la actividad está utilizando la versión de diseño
        // contenedor del FrameLayout. Si es así, debemos añadir el primer fragmento
        if (findViewById(R.id.unique_fragment) != null) {

            // Sin embargo, si estamos siendo restaurados de un estado anterior,
            // entonces no necesitamos hacer nada y debemos regresar o bien
            // podríamos terminar con fragmentos superpuestos.
            if (savedInstanceState != null) {
                return;
            }

            // Creamos una instancia de ListadoEventoFragment
            ListadoEventoFragment listadoFrag = new ListadoEventoFragment();

            // En caso de iniciarse esta actividad con instrucciones especiales de un Intent,
            // pasa los extras de la Intención al fragmento como argumentos
            listadoFrag.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.unique_fragment, listadoFrag).commit();
        }
    }

    public void onFragmentInteraction(int position, EventoItem item){
        MyLog.d("EventosActivity", "onFragmentInteraction...");
        // Captura el fragmento de artículo del diseño de la actividad
        MostrarEventoFragment contenidoFrag = (MostrarEventoFragment)
                getSupportFragmentManager().findFragmentById(R.id.contenido_fragment);

        if (contenidoFrag != null) {
            // Si el fragmento del artículo está disponible, estaremos en un diseño de dos paneles ...

            // Llama a un método en el Contenido para actualizar su contenido
            contenidoFrag.updateView(position, item.getId());
        } else {

            // Crear fragmento y darle un argumento para el artículo seleccionado
            MostrarEventoFragment newcontenidoFrag = new MostrarEventoFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putString("id", item.getId());
            newcontenidoFrag.setArguments(args);

            // Reemplazamos lo que esté en la vista MostrarEventoFragment con este fragmento,
            // y agrega la transacción a la pila trasera para que el usuario pueda navegar de nuevo
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.unique_fragment, newcontenidoFrag);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}