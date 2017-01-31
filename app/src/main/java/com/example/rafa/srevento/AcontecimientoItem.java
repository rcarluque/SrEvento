package com.example.rafa.srevento;

/**
 * Created by Rafa on 18/10/2016.
 */

public class AcontecimientoItem {

    private String id;
    private String nombre;
    private String inicio;
    private String fin;

    public AcontecimientoItem(String id, String nombre, String inicio, String fin) {
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
}
