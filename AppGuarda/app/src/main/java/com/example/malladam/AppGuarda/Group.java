package com.example.malladam.AppGuarda;

import java.util.ArrayList;

/**
 * Created by malladam on 01/05/2016.
 */
public class Group {

    private String Numero;
    private String Origen;
    private String Destino;
    private int Icono;
    private ArrayList<Child> Items;

    public String getNumero() {
        return Numero;
    }
    public void setNumero(String numero) {
        this.Numero = numero;
    }

    public String getOrigen() {
        return Origen;
    }
    public void setOrigen(String origen) {
        this.Origen = origen;
    }

    public String getDestino() {
        return Destino;
    }
    public void setDestino(String destino) {
        this.Destino = destino;
    }

    public int getIcono() {
        return Icono;
    }
    public void setIcono(int icono) {
        this.Icono = icono;
    }

    public ArrayList<Child> getItems() {
        return Items;
    }
    public void setItems(ArrayList<Child> Items) {
        this.Items = Items;
    }

}
