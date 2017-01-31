package com.example.rafa.srevento;

/**
 * Created by Rafa on 17/01/2017.
 */

public class EventoItem {
    private String id;
    private String nombre;

    public EventoItem(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}