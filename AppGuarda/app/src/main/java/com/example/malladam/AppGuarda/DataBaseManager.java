package com.example.malladam.AppGuarda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.malladam.AppGuarda.models.AsientoActivo;
import com.example.malladam.AppGuarda.models.Parada;
import com.example.malladam.AppGuarda.models.Pasaje;
import com.example.malladam.AppGuarda.models.ViajeActual;
import com.google.android.gms.maps.model.LatLng;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import static com.example.malladam.AppGuarda.DataBaseManager.TABLE_NAME_ASIENTOS;

/**
 * Created by malladam on 01/05/2016.
 */
public class DataBaseManager {

    public static final String TABLE_NAME_PASAJES = "pasajes";
    public static final String TABLE_NAME_USUARIOS = "usuarios";
    public static final String TABLE_NAME_SESION = "sesion";
    public static final String TABLE_NAME_VIAJE = "viaje";
    public static final String TABLE_NAME_ASIENTOS = "asientos";
    public static final String TABLE_NAME_PARADAS = "paradas";
    public static final String TABLE_NAME_UBICACION = "ubicacion";

    public static final String CN_ID_PASAJE = "id";
    public static final String CN_ID_VIAJE = "viaje";
    public static final String CN_ID_CLIENTE = "cliente";
    public static final String CN_ID_ORIGEN = "origen";
    public static final String CN_ID_DESTINO = "desntino";
    public static final String CN_FECHA = "fecha";
    public static final String CN_ASIENTO = "asiento";
    public static final String CN_VALOR = "valor";
    public static final String CN_TIPO_VENTA = "tipoVenta";
    public static final String CN_MEDIO_PAGO = "medioPago";
    public static final String CN_ESTADO = "estado";

    public static final String CN_ID_USUARIO = "id";
    public static final String CN_USUARIO = "usuario";
    public static final String CN_PASS = "pass";
    public static final String CN_NOM_USUARIO = "nombre";
    public static final String CN_EMAIL = "email";

    public static final String CN_LAT_UBI = "latitud";
    public static final String CN_LON_UBI = "longitud";

    public static final String CN_TOKEN_SESION = "token";
    public static final String CN_USUARIO_SESION = "usuario";
    public static final String CN_PASS_SESION = "password";

    public static final String CN_IDPASAJE_ASIENTO = "id_pasaje";
    public static final String CN_NROPASAJE_ASIENTO = "nro_pasaje";
    public static final String CN_COSTO_ASIENTO = "costo";
    public static final String CN_USERNAMEUSUARIO_ASIENTO = "username_usuario";
    public static final String CN_NOMBREUSUARIO_ASIENTO = "nombre_usuario";
    public static final String CN_APELLIDOUSUARIO_ASIENTO = "apellido_usuario";
    public static final String CN_IDVIAJE_ASIENTO = "id_viaje";
    public static final String CN_IDASIENTO_ASIENTO = "id_asiento";
    public static final String CN_NUMEROASIENTO_ASIENTO = "numero_asiento";
    public static final String CN_IDPARADASUBE_ASIENTO = "id_paradaSube";
    public static final String CN_IDPARADABAJA_ASIENTO = "id_paradaBaja";

    public static final String CN_IDPARADA_PARADAS = "id_parada";
    public static final String CN_DESCRIPCION_PARADAS = "descripcion";
    public static final String CN_DIRECCION_PARADAS = "direccion";
    public static final String CN_LATITUD_PARADAS = "latitud";
    public static final String CN_LONGITUD_PARADAS = "longitud";
    public static final String CN_ESTERMINAL_PARADAS = "es_terminal";


    public static final String CREATE_TABLE_ASIENTOS = "create table " +TABLE_NAME_ASIENTOS+ " ("
            + CN_IDPASAJE_ASIENTO + " integer primary key ,"
            + CN_NROPASAJE_ASIENTO + " text not null ,"
            + CN_COSTO_ASIENTO + " integer not null,"
            + CN_USERNAMEUSUARIO_ASIENTO + " text not null,"
            + CN_NOMBREUSUARIO_ASIENTO + " text not null,"
            + CN_APELLIDOUSUARIO_ASIENTO + " text not null,"
            + CN_IDVIAJE_ASIENTO + " integer not null,"
            + CN_IDASIENTO_ASIENTO + " integer not null,"
            + CN_NUMEROASIENTO_ASIENTO + " integer not null,"
            + CN_IDPARADASUBE_ASIENTO + " integer not null,"
            + CN_IDPARADABAJA_ASIENTO + " integer not null);";

    public static final String ALTER_TABLE_ASIENTOS = "ALTER TABLE "+TABLE_NAME_ASIENTOS+ " ADD COLUMN "+CN_NROPASAJE_ASIENTO+ " text;";


    public static final String CREATE_TABLE_UBICACION = "create table " +TABLE_NAME_UBICACION+ " ("
            + CN_LAT_UBI + " real,"
            + CN_LON_UBI + " real);";



    public static final String CREATE_TABLE_PASAJES = "create table " +TABLE_NAME_PASAJES+ " ("
            + CN_ID_PASAJE + " integer primary key autoincrement,"
            + CN_ID_VIAJE + " text not null,"
            + CN_ID_CLIENTE + " text not null,"
            + CN_ID_ORIGEN + " text not null,"
            + CN_ID_DESTINO + " text not null,"
            + CN_FECHA + " text not null,"
            + CN_ASIENTO + " integer not null,"
            + CN_VALOR + " double not null,"
            + CN_TIPO_VENTA + " text not null,"
            + CN_MEDIO_PAGO + " text not null,"
            + CN_ESTADO + " text not null);";

    public static final String CREATE_TABLE_USUARIOS = "create table " +TABLE_NAME_USUARIOS+ " ("
            + CN_ID_USUARIO + " integer primary key autoincrement,"
            + CN_USUARIO + " text not null,"
            + CN_PASS + " text not null,"
            + CN_NOM_USUARIO + " text not null,"
            + CN_EMAIL + " text not null);";


    public static final String CN_INICIO = "inicio";
    public static final String CN_FIN = "fin";
    public static final String CN_ES_DIRECTO = "es_directo";
    public static final String CN_ES_TERMINADO = "es_terminado";
    public static final String CN_ID_LINEA = "id_linea";
    public static final String CN_NUMERO_LINEA = "numero_linea";
    public static final String CN_ORIGEN_LINEA = "origen_linea";
    public static final String CN_DESTINO_LINEA = "destino_linea";
    public static final String CN_ID_VEHICULO = "id_vehiculo";
    public static final String CN_MARCA_VEHICULO = "marca_vehiculo";
    public static final String CN_MODELO_VEHICULO = "modelo_vehiculo";
    public static final String CN_MATRICULA_VEHICULO = "matricula_vehiculo";
    public static final String CN_CANTASIENTOS_VEHICULO = "cantAsientos_vehiculo";
    public static final String CN_CANTPARADOS_VEHICULO = "cantParados_vehiculo";


    public static final String CREATE_TABLE_SESION = "create table " +TABLE_NAME_SESION+ " (" + CN_TOKEN_SESION + " text not null," + CN_USUARIO_SESION + " text not null," +CN_PASS_SESION+ " text not null);";
    public static final String ALTER_TABLE_SESION = "ALTER TABLE "+TABLE_NAME_SESION+ " ADD COLUMN "+CN_TOKEN_SESION+ " text;";

    public static final String ALTER_TABLE_VIAJE1 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_INICIO+ " integer;";
    public static final String ALTER_TABLE_VIAJE2 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_FIN+ " integer;";
    public static final String ALTER_TABLE_VIAJE3 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_ES_DIRECTO+ " integer;";
    public static final String ALTER_TABLE_VIAJE4 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_ES_TERMINADO+ " integer;";
    public static final String ALTER_TABLE_VIAJE5 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_ID_LINEA+ " integer;";
    public static final String ALTER_TABLE_VIAJE6 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_NUMERO_LINEA+ " integer;";
    public static final String ALTER_TABLE_VIAJE7 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_ORIGEN_LINEA+ " text;";
    public static final String ALTER_TABLE_VIAJE8 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_DESTINO_LINEA+ " text;";
    public static final String ALTER_TABLE_VIAJE9 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_ID_VEHICULO+ " integer;";
    public static final String ALTER_TABLE_VIAJE10 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_MARCA_VEHICULO+ " text;";
    public static final String ALTER_TABLE_VIAJE11 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_MODELO_VEHICULO+ " text;";
    public static final String ALTER_TABLE_VIAJE12 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_MATRICULA_VEHICULO+ " text;";
    public static final String ALTER_TABLE_VIAJE13 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_CANTASIENTOS_VEHICULO+ " integer;";
    public static final String ALTER_TABLE_VIAJE14 = "ALTER TABLE "+TABLE_NAME_VIAJE+ " ADD COLUMN "+CN_CANTPARADOS_VEHICULO+ " integer;";

    public static final String CREATE_TABLE_VIAJE = "create table " +TABLE_NAME_VIAJE+ " ("
            + CN_ID_VIAJE + " text not null,"
            + CN_INICIO + " integer not null,"
            + CN_FIN + " integer not null,"
            + CN_ES_DIRECTO + " integer not null,"
            + CN_ES_TERMINADO + " integer not null,"
            + CN_ID_LINEA + " integer not null,"
            + CN_NUMERO_LINEA + " integer not null,"
            + CN_ORIGEN_LINEA + " text not null,"
            + CN_DESTINO_LINEA + " text not null,"
            + CN_ID_VEHICULO + " integer not null,"
            + CN_MARCA_VEHICULO + " text not null,"
            + CN_MODELO_VEHICULO + " text not null,"
            + CN_MATRICULA_VEHICULO + " text not null,"
            + CN_CANTASIENTOS_VEHICULO + " integer not null,"
            + CN_CANTPARADOS_VEHICULO + " integer not null);";


    public static final String CREATE_TABLE_PARADAS = "create table " +TABLE_NAME_PARADAS+ " ("
            + CN_IDPARADA_PARADAS + " integer not null,"
            + CN_DESCRIPCION_PARADAS + " text not null,"
            + CN_DIRECCION_PARADAS + " text not null,"
            + CN_LATITUD_PARADAS + " real not null,"
            + CN_LONGITUD_PARADAS + " real not null,"
            + CN_ESTERMINAL_PARADAS + " integer not null);";


    private SQLiteDatabase db;

    public DataBaseManager(Context context) {
        DataBaseHelper helper = DataBaseHelper.getInstance(context,"AppGuarda"+context.getString(R.string.app_name));
        db = helper.getWritableDatabase();
    }


    public void insertarPasaje(Pasaje pasaje){
        ContentValues valores = new ContentValues();
        valores.put(CN_ID_VIAJE,"0001");
        valores.put(CN_ID_CLIENTE,"0000");
        valores.put(CN_ID_ORIGEN,"0023");
        valores.put(CN_ID_DESTINO,"0022");
        valores.put(CN_FECHA,"2016-02-24 12:13:14"); //yyyy-MM-dd HH:mm:ss
        valores.put(CN_ASIENTO,pasaje.asiento);
        valores.put(CN_VALOR,"200");
        valores.put(CN_TIPO_VENTA,"Mostrador");
        valores.put(CN_MEDIO_PAGO,"Efectivo");
        valores.put(CN_ESTADO,"CN_ESTADO");

        db.insert(TABLE_NAME_PASAJES,null,valores);
    }

    /*public void insertarUsuarios(){
        ContentValues valores1 = new ContentValues();
        valores1.put(CN_USUARIO,"mmallada");
        valores1.put(CN_PASS,"mm123");
        valores1.put(CN_NOM_USUARIO,"Misael Mallada");
        valores1.put(CN_EMAIL,"misaelmallada@gmail.com");

        ContentValues valores2 = new ContentValues();
        valores2.put(CN_USUARIO,"mmiranda");
        valores2.put(CN_PASS,"mm456");
        valores2.put(CN_NOM_USUARIO,"Misael Miranda");
        valores2.put(CN_EMAIL,"misaelmiranda@gmail.com");

        db.insert(TABLE_NAME_USUARIOS,null,valores1);
        db.insert(TABLE_NAME_USUARIOS,null,valores2);
    }*/

    public Boolean validarUsuario(String usuario, String pass){
        String [] columnas = new String[]{CN_ID_USUARIO};
        Cursor resultado = db.query(TABLE_NAME_USUARIOS, columnas, CN_USUARIO+" = ? AND "+CN_PASS+" = ?",
                new String[]{usuario,pass}, null, null, null);
        if(resultado !=null && resultado.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public String getUserLogueado (){
        String logueado = null;
        String [] columnas = new String[]{CN_USUARIO_SESION};
        Cursor resultado = db.query(TABLE_NAME_SESION, columnas, null,null, null, null, null);
        resultado.moveToFirst();
        if(!resultado.isAfterLast()) {
            logueado = resultado.getString(resultado.getColumnIndex(CN_USUARIO_SESION));
        }
        return logueado;
    }


    public String getTokenLogueado() {
        String token = null;
        String user = getUserLogueado();
        String [] columnas = new String[]{CN_TOKEN_SESION};
        Cursor resultado = db.query(TABLE_NAME_SESION, columnas, CN_USUARIO_SESION+" = ?",
                new String[]{user}, null, null, null);
        resultado.moveToFirst();
        if(!resultado.isAfterLast()) {
            token = resultado.getString(resultado.getColumnIndex(CN_TOKEN_SESION));
        }else{
            token = "Sin Determinar";
        }
        resultado.close();
        return token;
    }


    public String getPassLogueado() {
        String pass = null;
        String user = getUserLogueado();
        String [] columnas = new String[]{CN_PASS_SESION};
        Cursor resultado = db.query(TABLE_NAME_SESION, columnas, CN_USUARIO_SESION+" = ?",
                new String[]{user}, null, null, null);
        resultado.moveToFirst();
        if(!resultado.isAfterLast()) {
            pass = resultado.getString(resultado.getColumnIndex(CN_PASS_SESION));
        }else{
            pass = "Sin Determinar";
        }
        resultado.close();
        return pass;
    }

    public void registrarLogin(String token, String usuario, String pass){
        eliminarLogin();
        ContentValues valores = new ContentValues();
        valores.put(CN_USUARIO_SESION,usuario);
        valores.put(CN_PASS_SESION,pass);
        valores.put(CN_TOKEN_SESION,token);

        db.insert(TABLE_NAME_SESION,null,valores);
    }

    public void eliminarLogin(){
        db.delete(TABLE_NAME_SESION,null,null);
    }



    public ViajeActual getViajeActual (){
        ViajeActual viaje = new ViajeActual();
        String [] columnas = new String[]{CN_ID_VIAJE, CN_INICIO ,CN_FIN ,CN_ES_DIRECTO ,
                CN_ES_TERMINADO ,CN_ID_LINEA ,CN_NUMERO_LINEA ,CN_ORIGEN_LINEA ,CN_DESTINO_LINEA ,
                CN_ID_VEHICULO ,CN_MARCA_VEHICULO ,CN_MODELO_VEHICULO ,CN_MATRICULA_VEHICULO ,
                CN_CANTASIENTOS_VEHICULO ,CN_CANTPARADOS_VEHICULO};
        Cursor resultado = db.query(TABLE_NAME_VIAJE, columnas, null,null, null, null, null);
        resultado.moveToFirst();
        if(!resultado.isAfterLast()) {
            viaje.setId_viaje(resultado.getInt(resultado.getColumnIndex(CN_ID_VIAJE)));
            viaje.setInicio(resultado.getLong(resultado.getColumnIndex(CN_INICIO)));
            viaje.setFin(resultado.getLong(resultado.getColumnIndex(CN_FIN)));
            if(resultado.getInt(resultado.getColumnIndex(CN_ES_DIRECTO))==0){
                viaje.setEs_directo(false);
            }else{
                viaje.setEs_directo(true);
            }
            if(resultado.getInt(resultado.getColumnIndex(CN_ES_TERMINADO))==0){
                viaje.setTerminado(false);
            }else{
                viaje.setTerminado(true);
            }
            viaje.setId_linea(resultado.getInt(resultado.getColumnIndex(CN_ID_LINEA)));
            viaje.setNumero_linea(resultado.getInt(resultado.getColumnIndex(CN_NUMERO_LINEA)));
            viaje.setOrigen_linea(resultado.getString(resultado.getColumnIndex(CN_ORIGEN_LINEA)));
            viaje.setDestino_linea(resultado.getString(resultado.getColumnIndex(CN_DESTINO_LINEA)));
            viaje.setId_vehiculo(resultado.getInt(resultado.getColumnIndex(CN_ID_VEHICULO)));
            viaje.setMarca_vehiculo(resultado.getString(resultado.getColumnIndex(CN_MARCA_VEHICULO)));
            viaje.setModelo_vehiculo(resultado.getString(resultado.getColumnIndex(CN_MODELO_VEHICULO)));
            viaje.setMatricula_vehiculo(resultado.getString(resultado.getColumnIndex(CN_MATRICULA_VEHICULO)));
            viaje.setCantAsientos_vehiculo(resultado.getInt(resultado.getColumnIndex(CN_CANTASIENTOS_VEHICULO)));
            viaje.setCantParados_vehiculo(resultado.getInt(resultado.getColumnIndex(CN_CANTPARADOS_VEHICULO)));
        }
        return viaje;
    }

    public void eliminarViaje(){
        db.delete(TABLE_NAME_VIAJE,null,null);
    }

    public void guardarViajeActual(ViajeActual viajeActual) {

        ContentValues valores = new ContentValues();
        valores.put(CN_ID_VIAJE,viajeActual.getId_viaje());
        valores.put(CN_INICIO,viajeActual.getInicio());
        valores.put(CN_FIN,viajeActual.getFin());
        valores.put(CN_ES_DIRECTO,viajeActual.getEs_directo());
        valores.put(CN_ES_TERMINADO,viajeActual.getTerminado());
        valores.put(CN_ID_LINEA,viajeActual.getId_linea());
        valores.put(CN_NUMERO_LINEA,viajeActual.getNumero_linea());
        valores.put(CN_ORIGEN_LINEA,viajeActual.getOrigen_linea());
        valores.put(CN_DESTINO_LINEA,viajeActual.getDestino_linea());
        valores.put(CN_ID_VEHICULO,viajeActual.getId_vehiculo());
        valores.put(CN_MARCA_VEHICULO,viajeActual.getMarca_vehiculo());
        valores.put(CN_MODELO_VEHICULO,viajeActual.getModelo_vehiculo());
        valores.put(CN_MATRICULA_VEHICULO,viajeActual.getMatricula_vehiculo());
        valores.put(CN_CANTASIENTOS_VEHICULO,viajeActual.getCantAsientos_vehiculo());
        valores.put(CN_CANTPARADOS_VEHICULO,viajeActual.getCantParados_vehiculo());

        db.insert(TABLE_NAME_VIAJE,null,valores);
    }


    public void insertarAsientoActivo(AsientoActivo asiento){
        ContentValues valores = new ContentValues();
        valores.put(CN_IDPASAJE_ASIENTO,asiento.getId_pasaje());
        valores.put(CN_NROPASAJE_ASIENTO,asiento.getNumero_pasaje());
        valores.put(CN_COSTO_ASIENTO,asiento.getCosto());
        valores.put(CN_USERNAMEUSUARIO_ASIENTO,asiento.getUsername_usuario());
        valores.put(CN_NOMBREUSUARIO_ASIENTO,asiento.getNombre_usuario());
        valores.put(CN_APELLIDOUSUARIO_ASIENTO,asiento.getApellido_usuario());
        valores.put(CN_IDVIAJE_ASIENTO,asiento.getId_viaje());
        valores.put(CN_IDASIENTO_ASIENTO,asiento.getId_asiento());
        valores.put(CN_NUMEROASIENTO_ASIENTO,asiento.getNumero_asiento());
        valores.put(CN_IDPARADASUBE_ASIENTO,asiento.getId_paradaSube());
        valores.put(CN_IDPARADABAJA_ASIENTO,asiento.getId_paradaBaja());

        db.insert(TABLE_NAME_ASIENTOS,null,valores);
    }


    public AsientoActivo getAsiento(int id_pasaje){

        String whereClause = CN_IDPASAJE_ASIENTO+" = ? ";
        String[] whereArgs = new String[] { String.valueOf(id_pasaje) };
        Cursor resultado = db.query(TABLE_NAME_ASIENTOS, null, whereClause, whereArgs, null, null, null);

        resultado.close();
        return null;
    }


    public List<AsientoActivo> getAsientosActivos(){
        Cursor resultado = db.query(TABLE_NAME_ASIENTOS, null, null, null, null, null, null);
        List<AsientoActivo> asientos = new ArrayList<AsientoActivo>();

        if (resultado.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                AsientoActivo asiento = new AsientoActivo();
                asiento.setApellido_usuario(resultado.getString(resultado.getColumnIndex(CN_APELLIDOUSUARIO_ASIENTO)));
                asiento.setCosto(Float.parseFloat(resultado.getString(resultado.getColumnIndex(CN_COSTO_ASIENTO))));
                asiento.setId_asiento(resultado.getInt(resultado.getColumnIndex(CN_IDASIENTO_ASIENTO)));
                asiento.setId_paradaSube(resultado.getInt(resultado.getColumnIndex(CN_IDPARADABAJA_ASIENTO)));
                asiento.setId_paradaBaja(resultado.getInt(resultado.getColumnIndex(CN_IDPARADASUBE_ASIENTO)));
                asiento.setId_pasaje(resultado.getInt(resultado.getColumnIndex(CN_IDPASAJE_ASIENTO)));
                asiento.setNumero_pasaje(resultado.getString(resultado.getColumnIndex(CN_NROPASAJE_ASIENTO)));
                asiento.setId_viaje(resultado.getInt(resultado.getColumnIndex(CN_IDVIAJE_ASIENTO)));
                asiento.setNombre_usuario(resultado.getString(resultado.getColumnIndex(CN_NOMBREUSUARIO_ASIENTO)));
                asiento.setNumero_asiento(resultado.getInt(resultado.getColumnIndex(CN_NUMEROASIENTO_ASIENTO)));
                asiento.setUsername_usuario(resultado.getString(resultado.getColumnIndex(CN_USERNAMEUSUARIO_ASIENTO)));
                asientos.add(asiento);
            } while(resultado.moveToNext());
        }
        resultado.close();

        return asientos;
    }


    public void eliminarAsiento (int id_pasaje){
        db.delete(TABLE_NAME_ASIENTOS, CN_IDPASAJE_ASIENTO+"="+String.valueOf(id_pasaje), null);
    }

    public void eliminarTodosAsientos(){
        db.delete(TABLE_NAME_ASIENTOS,null,null);
    }




    public void insertarParadasDelViaje(List<Parada> paradas){

        for (Parada parada: paradas ) {

            ContentValues valores = new ContentValues();
            valores.put(CN_DESCRIPCION_PARADAS, parada.getDescripcion());
            valores.put(CN_DIRECCION_PARADAS, parada.getDireccion());
            valores.put(CN_ESTERMINAL_PARADAS, parada.getEs_terminal());
            valores.put(CN_IDPARADA_PARADAS, parada.getId_parada());
            valores.put(CN_LATITUD_PARADAS, parada.getLatitud());
            valores.put(CN_LONGITUD_PARADAS, parada.getLongitud());

            db.insert(TABLE_NAME_PARADAS, null, valores);
        }
    }


    public List<Parada> getParadasDelViaje(){
        Cursor resultado = db.query(TABLE_NAME_PARADAS, null, null, null, null, null, null);
        List<Parada> paradas = new ArrayList<Parada>();

        if (resultado.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Parada parada = new Parada();
                parada.setDescripcion(resultado.getString(resultado.getColumnIndex(CN_DESCRIPCION_PARADAS)));
                parada.setDireccion(resultado.getString(resultado.getColumnIndex(CN_DIRECCION_PARADAS)));
                if(resultado.getInt(resultado.getColumnIndex(CN_ESTERMINAL_PARADAS))==0){
                    parada.setEs_terminal(false);
                }else{
                    parada.setEs_terminal(true);
                }
                parada.setId_parada(resultado.getInt(resultado.getColumnIndex(CN_IDPARADA_PARADAS)));
                parada.setLatitud(resultado.getDouble(resultado.getColumnIndex(CN_LATITUD_PARADAS)));
                parada.setLongitud(resultado.getDouble(resultado.getColumnIndex(CN_LONGITUD_PARADAS)));

                paradas.add(parada);
            } while(resultado.moveToNext());
        }
        resultado.close();

        return paradas;
    }


    public void eliminarParadasDelViaje(){
        db.delete(TABLE_NAME_PARADAS,null,null);
    }


    public void insertarUbicacion(LatLng ubicacion){

        eliminarUbicacion();

        ContentValues valores = new ContentValues();
        valores.put(CN_LAT_UBI, ubicacion.latitude);
        valores.put(CN_LON_UBI, ubicacion.longitude);

        db.insert(TABLE_NAME_UBICACION, null, valores);

    }


    public double getLatitud(){
        Cursor resultado = db.query(TABLE_NAME_UBICACION, null, null, null, null, null, null);
        if (resultado.moveToFirst()){
            return resultado.getDouble(resultado.getColumnIndex(CN_LAT_UBI));
        }else return 0;

    }

    public double getLongitud(){

        Cursor resultado = db.query(TABLE_NAME_UBICACION, null, null, null, null, null, null);
        if (resultado.moveToFirst()){
            return resultado.getDouble(resultado.getColumnIndex(CN_LON_UBI));
        }else return 0;
    }


    public void eliminarUbicacion(){
        db.delete(TABLE_NAME_UBICACION,null,null);
    }
}
