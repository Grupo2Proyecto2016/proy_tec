package com.example.malladam.AppUsuarios.models;

import java.math.BigDecimal;

/**
 * Created by malladam on 05/06/2016.
 */
public class Sucursal {

    private int id_sucursal;
    private String nombre;
    private String direccion;
    private int telefono;
    private String mail;
    private Double latitud;
    private Double longitud;
    private Boolean addTerminal;
    private Boolean hasTerminal;


    public Sucursal() {
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public Boolean getAddTerminal() {
        return addTerminal;
    }

    public void setAddTerminal(Boolean addTerminal) {
        this.addTerminal = addTerminal;
    }

    public Boolean getHasTerminal() {
        return hasTerminal;
    }

    public void setHasTerminal(Boolean hasTerminal) {
        this.hasTerminal = hasTerminal;
    }
}

