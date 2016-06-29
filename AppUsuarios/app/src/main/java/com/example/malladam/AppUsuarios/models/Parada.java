package com.example.malladam.AppUsuarios.models;

/**
 * Created by malladam on 05/06/2016.
 */
public class Parada {

    private int id_parada;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Boolean esTerminal;
    private Boolean esPeaje;
    private float reajuste;
    private Boolean sucursal;


    public Parada() {
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

    public Boolean getEsTerminal() {
        return esTerminal;
    }

    public void setEsTerminal(Boolean esTerminal) {
        this.esTerminal = esTerminal;
    }

    public Boolean getEsPeaje() {
        return esPeaje;
    }

    public void setEsPeaje(Boolean esPeaje) {
        this.esPeaje = esPeaje;
    }

    public float getReajuste() {
        return reajuste;
    }

    public void setReajuste(float reajuste) {
        this.reajuste = reajuste;
    }

    public Boolean getSucursal() {
        return sucursal;
    }

    public void setSucursal(Boolean sucursal) {
        this.sucursal = sucursal;
    }
}

