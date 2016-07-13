package com.example.malladam.AppGuarda.models;

/**
 * Created by malladam on 13/07/2016.
 */
public class Parada {

    private int id_parada;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Boolean es_terminal;


    public Parada() {
    }

    public Parada(int id_parada, String descripcion, String direccion, Double latitud, Double longitud, Boolean es_terminal) {
        this.id_parada = id_parada;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.es_terminal = es_terminal;
    }

    public int getId_parada() {
        return id_parada;
    }

    public void setId_parada(int id_parada) {
        this.id_parada = id_parada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getEs_terminal() {
        return es_terminal;
    }

    public void setEs_terminal(Boolean es_terminal) {
        this.es_terminal = es_terminal;
    }
}
