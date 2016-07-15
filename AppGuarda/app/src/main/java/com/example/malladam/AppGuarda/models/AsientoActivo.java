package com.example.malladam.AppGuarda.models;

/**
 * Created by malladam on 13/07/2016.
 */
public class AsientoActivo {

    private Integer id_pasaje;
    private String numero_pasaje;
    private float costo;
    private String username_usuario;
    private String nombre_usuario;
    private String apellido_usuario;
    private int id_viaje;
    private int id_asiento;
    private int numero_asiento;
    private int id_paradaSube;
    private int id_paradaBaja;


    public AsientoActivo() {
    }


    public AsientoActivo(Integer id_pasaje, String numero_pasaje, float costo, String username_usuario, String nombre_usuario,
                         String apellido_usuario, int id_viaje, int id_asiento, int numero_asiento,
                         int id_paradaSube, int id_paradaBaja) {
        this.id_pasaje = id_pasaje;
        this.numero_pasaje = numero_pasaje;
        this.costo = costo;
        this.username_usuario = username_usuario;
        this.nombre_usuario = nombre_usuario;
        this.apellido_usuario = apellido_usuario;
        this.id_viaje = id_viaje;
        this.id_asiento = id_asiento;
        this.numero_asiento = numero_asiento;
        this.id_paradaSube = id_paradaSube;
        this.id_paradaBaja = id_paradaBaja;
    }


    public Integer getId_pasaje() {
        return id_pasaje;
    }

    public void setId_pasaje(Integer id_pasaje) {
        this.id_pasaje = id_pasaje;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public String getUsername_usuario() {
        return username_usuario;
    }

    public void setUsername_usuario(String username_usuario) {
        this.username_usuario = username_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getApellido_usuario() {
        return apellido_usuario;
    }

    public void setApellido_usuario(String apellido_usuario) {
        this.apellido_usuario = apellido_usuario;
    }

    public int getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(int id_viaje) {
        this.id_viaje = id_viaje;
    }

    public int getId_asiento() {
        return id_asiento;
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getNumero_asiento() {
        return numero_asiento;
    }

    public void setNumero_asiento(int numero_asiento) {
        this.numero_asiento = numero_asiento;
    }

    public int getId_paradaSube() {
        return id_paradaSube;
    }

    public void setId_paradaSube(int id_paradaSube) {
        this.id_paradaSube = id_paradaSube;
    }

    public int getId_paradaBaja() {
        return id_paradaBaja;
    }

    public void setId_paradaBaja(int id_paradaBaja) {
        this.id_paradaBaja = id_paradaBaja;
    }

    public String getNumero_pasaje() {
        return numero_pasaje;
    }

    public void setNumero_pasaje(String numero_pasaje) {
        this.numero_pasaje = numero_pasaje;
    }
}
