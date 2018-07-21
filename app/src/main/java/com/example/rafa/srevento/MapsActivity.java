package com.example.rafa.srevento;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String ACTIVITY = "MapsActivity";
    private GoogleMap mMap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.d(ACTIVITY, "On Create...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MyLog.d(ACTIVITY, "On Map Ready...");
        mMap = googleMap;

        posicionarAcontecimiento(mMap);
        posicionarEventos(mMap);
    }

    public void posicionarAcontecimiento(GoogleMap mMap){
        String idAcontecimiento = Methods.getInstance().getSharedPrefs(context).getString("id", "no hay id");

        // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
        String[] args = new String[]{idAcontecimiento};
        // la sentencia SQL
        String selectSQL = "SELECT * FROM acontecimiento WHERE id=?";
        // Creamos las variables que queremos utilizar
        String nombre = null;
        String latitud = null;
        String longitud = null;
        // creamos un cursor con la ejecución del select en la base de datos
        Cursor c = Methods.getInstance().getReadBBDD(context).rawQuery(selectSQL, args);
        if (c.moveToFirst()) {
            nombre = c.getString(c.getColumnIndex("nombre"));
            latitud = c.getString(c.getColumnIndex("latitud"));
            longitud = c.getString(c.getColumnIndex("longitud"));
        }

        // Añadimos marcador dónde este el acontecimiento
        LatLng acontecimientoPos = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        mMap.addMarker(new MarkerOptions().position(acontecimientoPos).title(nombre));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(acontecimientoPos, 12));
    }

    public void posicionarEventos(GoogleMap mMap){
        String idAcontecimiento = Methods.getInstance().getSharedPrefs(context).getString("id", "no hay id");

        // creamos un array de strings, en el cual introduciremos el campo que queremos recoger del where
        String[] args = new String[]{idAcontecimiento};
        // la sentencia SQL
        String selectSQL = "SELECT * FROM evento WHERE id_acontecimiento=?";
        // Creamos las variables que queremos utilizar
        String nombre = null;
        String latitud = null;
        String longitud = null;
        // creamos un cursor con la ejecución del select en la base de datos
        Cursor c = Methods.getInstance().getReadBBDD(context).rawQuery(selectSQL, args);
        
        while(c.moveToNext()) {
            nombre = c.getString(c.getColumnIndex("nombre"));
            latitud = c.getString(c.getColumnIndex("latitud"));
            longitud = c.getString(c.getColumnIndex("longitud"));

            if(latitud.isEmpty() || longitud.isEmpty()){
                MyLog.e(ACTIVITY, "Evento sin lagitud ni longitud");
            } else {
                LatLng eventoPos = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                mMap.addMarker(new MarkerOptions().position(eventoPos).title(nombre).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            }
        }
    }

}
