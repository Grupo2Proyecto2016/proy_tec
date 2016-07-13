package com.example.malladam.AppUsuarios.models;

/**
 * Created by Pablo on 7/13/2016.
 */
public class GroupPasajeDT
{
    private Asiento ventana1;
    private Asiento pasillo1;
    private Asiento pasillo2;
    private Asiento ventana2;

    public GroupPasajeDT() {
    }

    public Asiento getVentana1() {
        return ventana1;
    }

    public void setVentana1(Asiento ventana1) {
        this.ventana1 = ventana1;
    }

    public Asiento getPasillo1() {
        return pasillo1;
    }

    public void setPasillo1(Asiento pasillo1) {
        this.pasillo1 = pasillo1;
    }

    public Asiento getPasillo2() {
        return pasillo2;
    }

    public void setPasillo2(Asiento pasillo2) {
        this.pasillo2 = pasillo2;
    }

    public Asiento getVentana2() {
        return ventana2;
    }

    public void setVentana2(Asiento ventana2) {
        this.ventana2 = ventana2;
    }
}
