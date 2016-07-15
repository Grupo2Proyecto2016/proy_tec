


package com.example.malladam.AppGuarda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malladam on 01/05/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper sInstance;

    //private static final String DB_NAME = "AppGuarda"+R.string.app_name+".sqlite";
    private static final int DB_SCHEME_VERSION = 7;

    public DataBaseHelper(Context context,String DB_NAME){
        super(context, DB_NAME, null, DB_SCHEME_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_TABLE_PASAJES);
        db.execSQL(DataBaseManager.CREATE_TABLE_SESION);
        db.execSQL(DataBaseManager.CREATE_TABLE_VIAJE);
        db.execSQL(DataBaseManager.CREATE_TABLE_ASIENTOS);
        db.execSQL(DataBaseManager.CREATE_TABLE_PARADAS);
        db.execSQL(DataBaseManager.CREATE_TABLE_UBICACION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseManager.ALTER_TABLE_ASIENTOS);
        
    }

    public static synchronized DataBaseHelper getInstance(Context context,String DB_NAME) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context, DB_NAME);
        }
        return sInstance;
    }

}
