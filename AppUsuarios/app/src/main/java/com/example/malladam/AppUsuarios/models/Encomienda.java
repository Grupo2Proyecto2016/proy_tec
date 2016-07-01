package com.example.malladam.AppUsuarios.models;

import java.util.Date;

/**
 * Created by malladam on 29/06/2016.
 */
public class Encomienda {

    private String idEncomeinda;
    private String Origen;
    private String Destino;
    private long Fecha;
    private String CiEmisor;
    private String CiReceptor;
    private float Precio;
    private String Estado;

    public Encomienda() {
    }

    public Encomienda(String id, String origen, String destino, long fecha, String ciEmisor, String ciReceptor, float precio, String estado) {
        idEncomeinda = id;
        Origen = origen;
        Destino = destino;
        Fecha = fecha;
        CiEmisor = ciEmisor;
        CiReceptor = ciReceptor;
        Precio = precio;
        Estado = estado;
    }

    public String getIdEncomeinda() {
        return idEncomeinda;
    }

    public void setIdEncomeinda(String idEncomeinda) {
        this.idEncomeinda = idEncomeinda;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public long getFecha() {
        return Fecha;
    }

    public void setFecha(long fecha) {
        Fecha = fecha;
    }

    public String getCiEmisor() {
        return CiEmisor;
    }

    public void setCiEmisor(String ciEmisor) {
        CiEmisor = ciEmisor;
    }

    public String getCiReceptor() {
        return CiReceptor;
    }

    public void setCiReceptor(String ciReceptor) {
        CiReceptor = ciReceptor;
    }

    public float getPrecio() {
        return Precio;
    }

    public void setPrecio(float precio) {
        Precio = precio;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
