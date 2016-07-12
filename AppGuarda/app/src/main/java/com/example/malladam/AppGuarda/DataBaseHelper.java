


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
    private static final int DB_SCHEME_VERSION = 3;

    public DataBaseHelper(Context context,String DB_NAME){
        super(context, DB_NAME, null, DB_SCHEME_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_TABLE_PASAJES);
        db.execSQL(DataBaseManager.CREATE_TABLE_SESION);
        db.execSQL(DataBaseManager.CREATE_TABLE_VIAJE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE1);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE2);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE3);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE4);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE5);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE6);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE7);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE8);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE9);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE10);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE11);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE12);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE13);
        db.execSQL(DataBaseManager.ALTER_TABLE_VIAJE14);
        
    }

    public static synchronized DataBaseHelper getInstance(Context context,String DB_NAME) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context, DB_NAME);
        }
        return sInstance;
    }

}
