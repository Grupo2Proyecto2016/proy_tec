package com.example.malladam.AppUsuarios.models;

/**
 * Created by malladam on 07/06/2016.
 */
public class Empresa {

    private String nombre;
    private double rut;
    private double telefono;
    private String direccion;
    private String logo;
    private String pais;
    private String razonSocial;
    private String mensaje = "Somos la primer empresa de transporte colectivo de pasajeros del País, fundada el 20 de octubre de 1930, gracias a la visión, coraje e ingenio de un grupo de transportistas que decidieron unirse y formar una empresa sobre ruedas.\n" +"\n" +"    Hace 85 años nuestros ómnibus atravesaban médanos y bosques, abriendo caminos hacia balnearios y zonas rurales, donde sólo unos pocos habitantes residían -en ese momento- en forma permanente. En un principio la Compañía atendía un espacio geográfico con centro en la ciudad de Pando en un radio de 60 kilómetros.";
    private static Empresa mEmpresa = null;

    private Empresa() {
    }

    public static Empresa getInstance() {
        if (mEmpresa == null) {
            mEmpresa = new Empresa();
        }
        return mEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getRut() {
        return rut;
    }

    public void setRut(double rut) {
        this.rut = rut;
    }

    public double getTelefono() {
        return telefono;
    }

    public void setTelefono(double telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public static Empresa getmEmpresa() {
        return mEmpresa;
    }

    public static void setmEmpresa(Empresa mEmpresa) {
        Empresa.mEmpresa = mEmpresa;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
