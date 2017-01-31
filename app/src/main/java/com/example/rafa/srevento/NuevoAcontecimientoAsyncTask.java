package com.example.rafa.srevento;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Esta clase sirve para crear una tarea asíncrona al conectar
 * con nuestra web del servidor.
 * También comprobará los permisos de internet
 * Created by Rafa on 28/10/2016.
 */

public class NuevoAcontecimientoAsyncTask extends AsyncTask<String,String,String> {

    HttpURLConnection urlConnection;
    String id;
    Context context;
    ProgressDialog dialog;
    ProgressBar pb;
    boolean realizar;
    String mensaje;

    public NuevoAcontecimientoAsyncTask(String id, Context context, ProgressBar pb) {
        this.id = id;
        this.context = context;
        this.pb = pb;
    }

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public ProgressDialog getDialog() {
        return dialog;
    }

    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HttpURLConnection getUrlConnection() {
        return urlConnection;
    }

    public void setUrlConnection(HttpURLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    protected void onPreExecute(){
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... args) {

        StringBuilder resultado = new StringBuilder();

        try {
            URL url = new URL("http://srevento.esy.es/api/v1/acontecimiento/"+id);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String linea;
            while ((linea = reader.readLine()) != null) {
                resultado.append(linea);
            }

            //Abrimos la base de datos en modo escritura
            // creamos un nuevo objeto tipo AcontecimientoSQLH, al cual le pasamos el context, y la ruta dónde guardar la base de datos en nuestro telefono
            AcontecimientoSQLiteHelper usdbh =
                    new AcontecimientoSQLiteHelper(context, Environment.getExternalStorageDirectory()+"/srevento.db", null, 1);

            SQLiteDatabase db = usdbh.getWritableDatabase();

            //Si hemos abierto correctamente la base de datos
            if(db != null) {

                // Código para insertar datos en JSON desde array de resultado.
                JSONObject raizJson = new JSONObject(resultado.toString());
                if (raizJson.has("acontecimiento")) {
                    // Si encuentra en el Json el tag acontecimiento, igualaremos la funcion realizar a true pues si se puede realizar
                    realizar = true;
                    MyLog.e("tag", "contiene acontecimiento");

                    JSONObject objetoJsonAcontecimiento = new JSONObject(raizJson.getString("acontecimiento"));
                    String idAcontecimiento = (objetoJsonAcontecimiento.has("id") ? objetoJsonAcontecimiento.getString("id") : "");
                    String nombreAcontecimiento = (objetoJsonAcontecimiento.has("nombre") ? objetoJsonAcontecimiento.getString("nombre") : "");
                    String organizadorAcontecimiento = (objetoJsonAcontecimiento.has("organizador") ? objetoJsonAcontecimiento.getString("organizador") : "");
                    String descripcionAcontecimiento = (objetoJsonAcontecimiento.has("descripcion") ? objetoJsonAcontecimiento.getString("descripcion") : "");
                    String tipoAcontecimiento = (objetoJsonAcontecimiento.has("tipo") ? objetoJsonAcontecimiento.getString("tipo") : "");
                    String portadaAcontecimiento = (objetoJsonAcontecimiento.has("portada") ? objetoJsonAcontecimiento.getString("portada") : "");
                    String inicioAcontecimiento = (objetoJsonAcontecimiento.has("inicio") ? objetoJsonAcontecimiento.getString("inicio") : "");
                    String finAcontecimiento = (objetoJsonAcontecimiento.has("fin") ? objetoJsonAcontecimiento.getString("fin") : "");
                    String direccionAcontecimiento = (objetoJsonAcontecimiento.has("direccion") ? objetoJsonAcontecimiento.getString("direccion") : "");
                    String localidadAcontecimiento = (objetoJsonAcontecimiento.has("localidad") ? objetoJsonAcontecimiento.getString("localidad") : "");
                    String cod_postaldAcontecimiento = (objetoJsonAcontecimiento.has("cod_postal") ? objetoJsonAcontecimiento.getString("cod_postal") : "");
                    String provinciaAcontecimiento = (objetoJsonAcontecimiento.has("provincia") ? objetoJsonAcontecimiento.getString("provincia") : "");
                    String longitudAcontecimiento = (objetoJsonAcontecimiento.has("longitud") ? objetoJsonAcontecimiento.getString("longitud") : "");
                    String latitudAcontecimiento = (objetoJsonAcontecimiento.has("latitud") ? objetoJsonAcontecimiento.getString("latitud") : "");
                    String telefonoAcontecimiento = (objetoJsonAcontecimiento.has("telefono") ? objetoJsonAcontecimiento.getString("telefono") : "");
                    String emailAcontecimiento = (objetoJsonAcontecimiento.has("email") ? objetoJsonAcontecimiento.getString("email") : "");
                    String webAcontecimiento = (objetoJsonAcontecimiento.has("web") ? objetoJsonAcontecimiento.getString("web") : "");
                    String facebookAcontecimiento = (objetoJsonAcontecimiento.has("facebook") ? objetoJsonAcontecimiento.getString("facebook") : "");
                    String twitterAcontecimiento = (objetoJsonAcontecimiento.has("twitter") ? objetoJsonAcontecimiento.getString("twitter") : "");
                    String instagramAcontecimiento = (objetoJsonAcontecimiento.has("instagram") ? objetoJsonAcontecimiento.getString("instagram") : "");

                    db.delete("acontecimiento", "id="+idAcontecimiento, null);

                    //Creamos el registro a insertar como objeto ContentValues
                    ContentValues nuevoAcontecimiento = new ContentValues();
                    nuevoAcontecimiento.put("id", idAcontecimiento);
                    nuevoAcontecimiento.put("nombre",nombreAcontecimiento);
                    nuevoAcontecimiento.put("organizador", organizadorAcontecimiento);
                    nuevoAcontecimiento.put("descripcion", descripcionAcontecimiento);
                    nuevoAcontecimiento.put("tipo", tipoAcontecimiento);
                    nuevoAcontecimiento.put("portada",portadaAcontecimiento);
                    nuevoAcontecimiento.put("inicio",inicioAcontecimiento);
                    nuevoAcontecimiento.put("fin",finAcontecimiento);
                    nuevoAcontecimiento.put("direccion", direccionAcontecimiento);
                    nuevoAcontecimiento.put("localidad", localidadAcontecimiento);
                    nuevoAcontecimiento.put("cod_postal", cod_postaldAcontecimiento);
                    nuevoAcontecimiento.put("provincia", provinciaAcontecimiento);
                    nuevoAcontecimiento.put("longitud", longitudAcontecimiento);
                    nuevoAcontecimiento.put("latitud", latitudAcontecimiento);
                    nuevoAcontecimiento.put("telefono", telefonoAcontecimiento);
                    nuevoAcontecimiento.put("email", emailAcontecimiento);
                    nuevoAcontecimiento.put("web", webAcontecimiento);
                    nuevoAcontecimiento.put("facebook", facebookAcontecimiento);
                    nuevoAcontecimiento.put("twitter", twitterAcontecimiento);
                    nuevoAcontecimiento.put("instagram", instagramAcontecimiento);

                    //Insertamos el registro en la base de datos
                    db.insert("acontecimiento", null, nuevoAcontecimiento);

                    // comprobamos si el acontecimiento (la raiz) contiene eventos.
                    if(raizJson.has("eventos")) {
                        MyLog.e("tag", "contiene evento");
                        // Hacer esto para eventos
                        JSONArray listaEventos = new JSONArray(raizJson.getString("eventos"));
                        // borramos todos los registros existentes de la id correspondiente
                        db.delete("evento", "id_acontecimiento="+idAcontecimiento, null);

                        for (int i = 0; i < listaEventos.length(); i++) {
                            JSONObject objetoJsonEvento = (JSONObject) listaEventos.get(i);
                            String idEvento = (objetoJsonEvento.has("id") ? objetoJsonEvento.getString("id") : "");
                            String idAcontecimientoEvento = (objetoJsonEvento.has("id_acontecimiento") ? objetoJsonEvento.getString("id_acontecimiento") : "");
                            String nombreEvento = (objetoJsonEvento.has("nombre") ? objetoJsonEvento.getString("nombre") : "");
                            String descripcionEvento = (objetoJsonEvento.has("descripcion") ? objetoJsonEvento.getString("descripcion") : "");
                            String inicioEvento = (objetoJsonEvento.has("inicio") ? objetoJsonEvento.getString("inicio") : "");
                            String finEvento = (objetoJsonEvento.has("fin") ? objetoJsonEvento.getString("fin") : "");
                            String direccionEvento = (objetoJsonEvento.has("direccion") ? objetoJsonEvento.getString("direccion") : "");
                            String localidadEvento = (objetoJsonEvento.has("localidad") ? objetoJsonEvento.getString("localidad") : "");
                            String cod_postalEvento = (objetoJsonEvento.has("cod_postal") ? objetoJsonEvento.getString("cod_postal") : "");
                            String provinciaEvento = (objetoJsonEvento.has("provincia") ? objetoJsonEvento.getString("provincia") : "");
                            String longitudEvento = (objetoJsonEvento.has("longitud") ? objetoJsonEvento.getString("longitud") : "");
                            String latitudEvento = (objetoJsonEvento.has("latitud") ? objetoJsonEvento.getString("latitud") : "");

                            //Creamos el registro a insertar como objeto ContentValues
                            ContentValues nuevoEvento = new ContentValues();
                            nuevoEvento.put("id", idEvento);
                            nuevoEvento.put("id_acontecimiento", idAcontecimientoEvento);
                            nuevoEvento.put("nombre", nombreEvento);
                            nuevoEvento.put("descripcion", descripcionEvento);
                            nuevoEvento.put("inicio", inicioEvento);
                            nuevoEvento.put("fin", finEvento);
                            nuevoEvento.put("direccion", direccionEvento);
                            nuevoEvento.put("localidad", localidadEvento);
                            nuevoEvento.put("cod_postal", cod_postalEvento);
                            nuevoEvento.put("provincia", provinciaEvento);
                            nuevoEvento.put("longitud", longitudEvento);
                            nuevoEvento.put("latitud", latitudEvento);

                            //Insertamos el registro en la base de datos
                            db.insert("evento", null, nuevoEvento);
                        }
                    }
                    db.close();
                } else if(raizJson.has("error")){
                    mensaje = raizJson.getString("message");

                    // Si en la raiz obtenida del json no hay un acontecimiento igualamos a false la funcion realizar
                    realizar = false;
                }
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }


        return resultado.toString();
    }

    @Override
    protected void onPostExecute(String resultado) {
        MyLog.d("ONPOSTEXECUTE",resultado);

        //Volvemos a ocultar la barra de progreso
        pb.setVisibility(View.INVISIBLE);
        // Si se ha podido realizar creará un nuevo archivo
        if(realizar != false) {
            // creamos el archivo sharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("id", getId());
            editor.commit();

            Intent intent = new Intent(context, MostrarAcontecimientoActivity.class);
            context.startActivity(intent);
            // Si no se ha podido realizar mostrará un Totas con mensaje de error
        } else{
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        }
    }



}
