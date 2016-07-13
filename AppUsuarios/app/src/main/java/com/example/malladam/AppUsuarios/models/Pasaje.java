package com.example.malladam.AppUsuarios.models;

/**
 * Created by malladam on 01/05/2016.
 */
public class Pasaje {

    private String idPasaje;
    private String Origen;
    private String Destino;
    private long Fecha;
    private String Asiento;
    private float Precio;
    private String Estado;
    private String Numero;

    public Pasaje() {
    }

    public Pasaje(String idPasaje, String origen, String destino, long fecha, String asiento, float precio, String estado) {
        this.idPasaje = idPasaje;
        Origen = origen;
        Destino = destino;
        Fecha = fecha;
        Asiento = asiento;
        Precio = precio;
        Estado = estado;
    }

    public String getIdPasaje() {
        return idPasaje;
    }

    public void setIdPasaje(String idPasaje) {
        this.idPasaje = idPasaje;
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

    public String getAsiento() {
        return Asiento;
    }

    public void setAsiento(String asiento) {
        Asiento = asiento;
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

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) { Numero = numero; }
}
