package com.example.rafa.srevento;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by crafa on 07/05/2018.
 */

public class BuscaAcontecimientoAsyncTask  extends AsyncTask<String, String, String> {

    private String textoBuscar;
    private String mensaje;
    private boolean flag = true;
    private HttpURLConnection connectionUrl;
    private Context context;
    private ProgressBar pb;
    private TextView tvError;
    private RecyclerView recyclerView;
    private ArrayList<AcontecimientoItem> acontecimientos;

    public BuscaAcontecimientoAsyncTask(String texto, Context context){
        super();
        this.textoBuscar = texto;
        this.context = context;
        acontecimientos = new ArrayList<>();

        Activity activity = (Activity)context;
        this.pb = (ProgressBar) activity.findViewById(R.id.pb_buscar_acontecimiento);
        this.recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerAcontecimientos);
        this.tvError = (TextView) activity.findViewById(R.id.tv_error_buscar);
    }

    @Override
    protected void onPreExecute(){ pb.setVisibility(View.VISIBLE);}

    @Override
    protected String doInBackground(String... args) {
        StringBuilder total = new StringBuilder();

        try {
            URL url = new URL(Constantes.REST_URL_BUSQUEDA_NOMBRE + this.textoBuscar);
            connectionUrl = (HttpURLConnection) url.openConnection();

            InputStream input = new BufferedInputStream(connectionUrl.getInputStream());
            BufferedReader breader = new BufferedReader(new InputStreamReader(input));

            String linea;

            while((linea = breader.readLine()) != null){
                total.append(linea);
            }

            JSONObject jsontotal = new JSONObject(total.toString());
            if(jsontotal.has("acontecimientos")){
                JSONArray jsonListaAcont = new JSONArray(jsontotal.getString("acontecimientos"));

                for(int i=0; i< jsonListaAcont.length(); i++){
                    JSONObject jsonAcont = (JSONObject) jsonListaAcont.get(i);
                    String id = (jsonAcont.has("id") ? jsonAcont.getString("id") : "");
                    String nombre = (jsonAcont.has("nombre") ? jsonAcont.getString("nombre") : "");
                    String inicio = (jsonAcont.has("inicio") ? jsonAcont.getString("inicio") : "");
                    String fin = null;

                    // creamos el formatero de como lo recoge en la base de datos
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
                    Date fechaInicio = dateFormat.parse(inicio);

                    // creamos el formato en el que lo va a mostrar
                    SimpleDateFormat formatPrint = new SimpleDateFormat("d/M/y hh:mm");
                    String inicioFormat = formatPrint.format(fechaInicio);

                    AcontecimientoItem ac = new AcontecimientoItem(id, nombre, inicioFormat, fin);
                    acontecimientos.add(ac);
                }
            } else if(jsontotal.has("error")) {
                flag = false;
                mensaje = jsontotal.getString("message");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionUrl.disconnect();
        }

        return total.toString();
    }

    @Override
    protected void onPostExecute(String total){
        pb.setVisibility(View.INVISIBLE);

        if(flag){
            tvError.setText("");

            AcontecimientoAdapter adapter = new AcontecimientoAdapter(acontecimientos);

            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildAdapterPosition(v);

                    new GuardaAcontecimientoAsyncTask(context, acontecimientos.get(position).getId(), pb).execute();
                }
            });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }else{
            tvError.setText(mensaje);
        }
    }
}