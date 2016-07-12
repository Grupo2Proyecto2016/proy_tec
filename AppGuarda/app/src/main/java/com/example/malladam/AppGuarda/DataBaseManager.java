package com.example.malladam.AppGuarda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.malladam.AppGuarda.models.Pasaje;
import com.example.malladam.AppGuarda.models.ViajeActual;

import java.util.ArrayList;

/**
 * Created by malladam on 01/05/2016.
 */
public class DataBaseManager {

    public static final String TABLE_NAME_PASAJES = "pasajes";
    public static final String TABLE_NAME_USUARIOS = "usuarios";
    public static final String TABLE_NAME_SESION = "sesion";
    public static final String TABLE_NAME_VIAJE = "viaje";

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

    public static final String CN_TOKEN_SESION = "token";
    public static final String CN_USUARIO_SESION = "usuario";
    public static final String CN_PASS_SESION = "password";



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

    public ArrayList<Pasaje> obtenerPasajesActivos(String idViaje){
        ArrayList<Pasaje> pasajesActivos = new ArrayList<Pasaje>();
        //CONSULTA SQL
        return  pasajesActivos;
    }

    public ArrayList<Pasaje> obtenerPasajesInactivos(String idViaje, String asiento){
        ArrayList<Pasaje> pasajesInactivos = new ArrayList<Pasaje>();
        //CONSULTA SQL
        return  pasajesInactivos;
    }

    public ArrayList<Pasaje> obtenerTodosLosPasajes(String idViaje){
        ArrayList<Pasaje> pasajes = new ArrayList<Pasaje>();
        //CONSULTA SQL
        return  pasajes;
    }

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
}




    /*public String[] getContacts(){
    Cursor cursor = getReadableDatabase().rawQuery("SELECT name FROM contacts", null);
    cursor.moveToFirst();
    ArrayList<String> names = new ArrayList<String>();
    while(!cursor.isAfterLast()) {
        names.add(cursor.getString(cursor.getColumnIndex("name")));
        cursor.moveToNext();
    }
    cursor.close();
    return names.toArray(new String[names.size()]);
}
*/
