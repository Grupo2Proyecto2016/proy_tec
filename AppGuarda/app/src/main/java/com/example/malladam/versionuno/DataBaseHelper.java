


package com.example.malladam.versionuno;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malladam on 01/05/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper sInstance;

    private static final String DB_NAME = "VersionUnoPuntoUno.sqlite";
    private static final int DB_SCHEME_VERSION = 4;

    public DataBaseHelper(Context context){
        super(context, DB_NAME, null, DB_SCHEME_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL(DataBaseManager.CREATE_TABLE_PASAJES);
        db.execSQL(DataBaseManager.CREATE_TABLE_USUARIOS);
        db.execSQL(DataBaseManager.CREATE_TABLE_SESION);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseManager.ALTER_TABLE_SESION);
    }

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context);
        }
        return sInstance;
    }

}
