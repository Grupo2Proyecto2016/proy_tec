package com.example.malladam.versionuno.models;

/**
 * Created by malladam on 01/05/2016.
 */
public class PasajeDataType {

    public String asiento;
    public String idViaje;
    public int icon;
    public String nomCliente;
    public String nomOrigen;
    public String nomDestino;
    public String fecha;
    public String valor;
    public String estado;

    public PasajeDataType(String asiento, String idViaje, int icon, String nomCliente, String nomOrigen, String nomDestino, String fecha, String valor, String estado) {
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
