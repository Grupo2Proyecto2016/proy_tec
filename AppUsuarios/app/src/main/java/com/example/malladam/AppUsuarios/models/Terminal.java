package com.example.malladam.AppUsuarios.models;

/**
 * Created by malladam on 23/06/2016.
 */
public class Terminal {

    private int id_parada;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Boolean es_terminal;
    private Boolean es_peaje;
    private int reajuste;
    private Boolean sucursal;

    public Terminal() {
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

    public Boolean getEs_peaje() {
        return es_peaje;
    }

    public void setEs_peaje(Boolean es_peaje) {
        this.es_peaje = es_peaje;
    }

    public int getReajuste() {
        return reajuste;
    }

    public void setReajuste(int reajuste) {
        this.reajuste = reajuste;
    }

    public Boolean getSucursal() {
        return sucursal;
    }

    public void setSucursal(Boolean sucursal) {
        this.sucursal = sucursal;
    }
}
