package com.example.malladam.versionuno.models;

/**
 * Created by malladam on 01/05/2016.
 */
public class Pasaje {

    public String asiento;
    public String idViaje;
    public String idCliente;
    public String idOrigen;
    public String idDestino;
    public String fecha;
    public String valor;
    public String tipoVenta;
    public String medioPago;
    public String estado;

    public Pasaje(String asiento, String idViaje, String idCliente, String idOrigen, String idDestino, String fecha, String valor, String tipoVenta, String medioPago) {
        this.asiento = asiento;
        this.idViaje = idViaje;
        this.idCliente = idCliente;
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.fecha = fecha;
        this.valor = valor;
        this.tipoVenta = tipoVenta;
        this.medioPago = medioPago;
        this.estado = "A"; //Activo ; Inactivo
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

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
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

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
