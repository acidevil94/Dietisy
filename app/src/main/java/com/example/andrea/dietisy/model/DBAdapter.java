package com.example.andrea.dietisy.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by andrea on 03/12/2016.
 */

public class DBAdapter extends SQLiteOpenHelper {

    public static final String DBName = "MyDieta.db";
    public static final int DBVersion = 3;


    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INTEGER";
    public static final String FLOAT_TYPE = " FLOAT";
    public static final String DATE_TYPE = " DATE";

/*
    public static String getCreateStatement() {
        return  Peso.getCreateSQL() + ", " + Misure.getCreateSQL();
    }

*/
    public static String getDeleteStatement(){
        return    "DROP TABLE IF EXISTS " + Peso.TABLE_NAME + ";" +
                  "DROP TABLE IF EXISTS " + Misure.TABLE_NAME;
    }

    private static DBAdapter mySingleton = null;
    private List<String> elencoEccezioni;

    private DBAdapter(Context context) {
        super(context, DBName, null, DBVersion);
        this.elencoEccezioni = new LinkedList<String>();
    }

    public static DBAdapter getIstance(Context c){
        if(mySingleton == null)
            mySingleton = new DBAdapter(c);
        return mySingleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( Peso.getCreateSQL());
        db.execSQL( Misure.getCreateSQL());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(getDeleteStatement());
        onCreate(db);
    }

   /* @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( getCreateStatement());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( getDeleteStatement());
        onCreate(db);

    }

*/
    public void AddEccezione(String mess){
        this.elencoEccezioni.add((mess));
    }

    public void svuotaElencoEccezioni(){
        this.elencoEccezioni.clear();
    }

    public List<String> getElencoEccezioni(){
        return this.elencoEccezioni;
    }

}


