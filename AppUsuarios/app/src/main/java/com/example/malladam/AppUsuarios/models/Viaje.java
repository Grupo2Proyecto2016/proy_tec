package com.example.malladam.AppUsuarios.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by malladam on 05/06/2016.
 */
public class Viaje {

    private int id_viaje;
    private int lugares;
    private long inicio;
    private int numero;
    private int linea_id_linea;
    private int origen;
    private String origen_description;
    private int destino;
    private String destino_description;


    public Viaje() {
    }

    public Viaje(int id_viaje, int lugares, long inicio, int numero, int linea_id_linea, int origen, String origen_description, int destino, String destino_description) {
        this.id_viaje = id_viaje;
        this.lugares = lugares;
        this.inicio = inicio;
        this.numero = numero;
        this.linea_id_linea = linea_id_linea;
        this.origen = origen;
        this.origen_description = origen_description;
        this.destino = destino;
        this.destino_description = destino_description;
    }

    public int getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(int id_viaje) {
        this.id_viaje = id_viaje;
    }

    public int getLugares() {
        return lugares;
    }

    public void setLugares(int lugares) {
        this.lugares = lugares;
    }

    public long getInicio() {
        return inicio;
    }

    public void setInicio(long inicio) {
        this.inicio = inicio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getLinea_id_linea() {
        return linea_id_linea;
    }

    public void setLinea_id_linea(int linea_id_linea) {
        this.linea_id_linea = linea_id_linea;
    }

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public String getOrigen_description() {
        return origen_description;
    }

    public void setOrigen_description(String origen_description) {
        this.origen_description = origen_description;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public String getDestino_description() {
        return destino_description;
    }

    public void setDestino_description(String destino_description) {
        this.destino_description = destino_description;
    }
}

