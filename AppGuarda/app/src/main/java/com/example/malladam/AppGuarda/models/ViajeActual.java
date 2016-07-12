package com.example.malladam.AppGuarda.models;

/**
 * Created by malladam on 08/07/2016.
 */
public class ViajeActual {

    private Integer id_viaje;
    private long inicio;
    private long fin;
    private Boolean es_directo;
    private Boolean terminado;
    private int id_linea;
    private int numero_linea;
    private String origen_linea;
    private String destino_linea;
    private int id_vehiculo;
    private String marca_vehiculo;
    private String modelo_vehiculo;
    private String matricula_vehiculo;
    private int cantAsientos_vehiculo;
    private int cantParados_vehiculo;


    public ViajeActual() {
    }

    public Integer getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(Integer id_viaje) {
        this.id_viaje = id_viaje;
    }

    public long getInicio() {
        return inicio;
    }

    public void setInicio(long inicio) {
        this.inicio = inicio;
    }

    public long getFin() {
        return fin;
    }

    public void setFin(long fin) {
        this.fin = fin;
    }

    public Boolean getEs_directo() {
        return es_directo;
    }

    public void setEs_directo(Boolean es_directo) {
        this.es_directo = es_directo;
    }

    public Boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getNumero_linea() {
        return numero_linea;
    }

    public void setNumero_linea(int numero_linea) {
        this.numero_linea = numero_linea;
    }

    public String getOrigen_linea() {
        return origen_linea;
    }

    public void setOrigen_linea(String origen_linea) {
        this.origen_linea = origen_linea;
    }

    public String getDestino_linea() {
        return destino_linea;
    }

    public void setDestino_linea(String destino_linea) {
        this.destino_linea = destino_linea;
    }


    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getMarca_vehiculo() {
        return marca_vehiculo;
    }

    public void setMarca_vehiculo(String marca_vehiculo) {
        this.marca_vehiculo = marca_vehiculo;
    }

    public String getMatricula_vehiculo() {
        return matricula_vehiculo;
    }

    public void setMatricula_vehiculo(String matricula_vehiculo) {
        this.matricula_vehiculo = matricula_vehiculo;
    }

    public int getCantAsientos_vehiculo() {
        return cantAsientos_vehiculo;
    }

    public void setCantAsientos_vehiculo(int canAsientos_vehiculo) {
        this.cantAsientos_vehiculo = canAsientos_vehiculo;
    }

    public String getModelo_vehiculo() {
        return modelo_vehiculo;
    }

    public void setModelo_vehiculo(String modelo_vehiculo) {
        this.modelo_vehiculo = modelo_vehiculo;
    }

    public int getCantParados_vehiculo() {
        return cantParados_vehiculo;
    }

    public void setCantParados_vehiculo(int cantParados_vehiculo) {
        this.cantParados_vehiculo = cantParados_vehiculo;
    }
}
