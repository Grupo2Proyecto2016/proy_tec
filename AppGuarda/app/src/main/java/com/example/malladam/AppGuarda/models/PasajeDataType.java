package com.example.malladam.AppGuarda.models;

import com.android.IntentResult;

/**
 * Created by malladam on 01/05/2016.
 */
public class PasajeDataType {

    private Integer id_pasaje;
    private String asiento;
    private String idViaje;
    private int icon;
    private String nomCliente;
    private String nomOrigen;
    private String nomDestino;
    private String fecha;
    private String valor;
    private String estado;


    public PasajeDataType() {
    }

    public PasajeDataType(Integer id_pasaje, String asiento, String idViaje, int icon, String nomCliente, String nomOrigen, String nomDestino, String fecha, String valor, String estado) {
        this.id_pasaje = id_pasaje;
        this.asiento = asiento;
        this.idViaje = idViaje;
        this.icon = icon;
        this.nomCliente = nomCliente;
        this.nomOrigen = nomOrigen;
        this.nomDestino = nomDestino;
        this.fecha = fecha;
        this.valor = valor;
        this.estado = estado;
    }

    public Integer getId_pasaje() {
        return id_pasaje;
    }

    public void setId_pasaje(Integer id_pasaje) {
        this.id_pasaje = id_pasaje;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(String idViaje) {
        this.idViaje = idViaje;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getNomCliente() {
        return nomCliente;
    }

    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    public String getNomOrigen() {
        return nomOrigen;
    }

    public void setNomOrigen(String nomOrigen) {
        this.nomOrigen = nomOrigen;
    }

    public String getNomDestino() {
        return nomDestino;
    }

    public void setNomDestino(String nomDestino) {
        this.nomDestino = nomDestino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
