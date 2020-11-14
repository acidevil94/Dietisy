package com.example.andrea.dietisy.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;
/**
 * Created by andrea on 06/12/2016.
 */


public class Peso {
    //metodi & proprietà statiche
    public static final String TABLE_NAME = "Pesi";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_DATE = "Data";
    public static final String COLUMN_NAME_PESO = "Peso";

    public static String getCreateSQL() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DATE + DBAdapter.TEXT_TYPE + "," +
                COLUMN_NAME_PESO + DBAdapter.FLOAT_TYPE + " );";
    }

    public static Peso getLastPeso(DBAdapter dbHelper){
        Peso ris = new Peso(dbHelper);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_PESO
        };
        String sortOrder = COLUMN_NAME_ID + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        cursor.moveToFirst();
            try {
                ris.setID(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)));
                int i = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
                ris.setDate(cursor.getString(i));
                ris.setPeso(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_PESO)));
            } catch (Exception e) {
                dbHelper.AddEccezione(TABLE_NAME + ": Lettura ultimo non riuscita!");
            }

        return ris;
    }

    public static Peso getFirstPeso(DBAdapter dbHelper){
        Peso ris = new Peso(dbHelper);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_PESO
        };
        String sortOrder = COLUMN_NAME_ID ;

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        cursor.moveToFirst();
        try {
            ris.setID(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)));
            int i = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
            ris.setDate(cursor.getString(i));
            ris.setPeso(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_PESO)));
        } catch (Exception e) {
            dbHelper.AddEccezione(TABLE_NAME + ": Lettura ultimo non riuscita!");
        }

        return ris;
    }

    public static List<Peso> getLastPesi(DBAdapter dbHelper, int quantity) {
        LinkedList<Peso> ris = new LinkedList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_PESO
        };
        // String sortOrder = COLUMN_NAME_ID ;

        String sortOrder = COLUMN_NAME_DATE + " DESC" ;

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if(cursor.getCount()<1)
            return ris;
        cursor.moveToFirst();
        Peso r ;
        int l = 0;
        try {
            do {
                if(l == quantity)
                    break;
                r = new Peso(dbHelper);
                r.setID(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)));
                int i = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
                r.setDate(cursor.getString(i));
                r.setPeso(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_PESO)));
                ris.add(r);
                l++;
            }while(cursor.moveToNext());
        } catch (Exception e) {
            dbHelper.AddEccezione(TABLE_NAME + ": Lettura all non riuscita!");
        }
        return ris;
    }

    public static List<Peso> getPesi(DBAdapter dbHelper, String dataDa, String dataA) {
        LinkedList<Peso> ris = new LinkedList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_PESO
        };
       // String sortOrder = COLUMN_NAME_ID ;

        String sortOrder = COLUMN_NAME_DATE ;

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if(cursor.getCount()<1)
            return ris;
        cursor.moveToFirst();
        Peso r ;
        try {
            do {
                r = new Peso(dbHelper);
                r.setID(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)));
                int i = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
                r.setDate(cursor.getString(i));
                r.setPeso(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_PESO)));
                ris.add(r);
            }while(cursor.moveToNext());
        } catch (Exception e) {
            dbHelper.AddEccezione(TABLE_NAME + ": Lettura all non riuscita!");
        }
        /*String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_PESO
        };
        String sortOrder = COLUMN_NAME_ID ;
        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        cursor.moveToFirst();
        Peso p;
        while(!cursor.isLast()) {

            try {
                p = new Peso(dbHelper);
                p.setID(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)));
                int i = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
                p.setDate(cursor.getString(i));
                p.setPeso(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_PESO)));
                ris.add(p);
            } catch (Exception e) {
                dbHelper.AddEccezione(TABLE_NAME + ": query all non riuscita!");
            }

            cursor.moveToNext();
        }*/
        return ris;
    }


    //Proprietà
    private DBAdapter mDBToken;

    private long mID;
    private String mDate;
    private float mPeso;

    //Costruttori

    //costruttore fake

    public Peso(){
    }

    public Peso(float peso,String data){
        this.mPeso = peso;
        this.mDate = data;
    }


    public Peso(DBAdapter dbHelper) {
        this.mDBToken = dbHelper;
        mID = 0;
    }

    public Peso(DBAdapter mDBHelper, int ID) throws RuntimeException{
        this.mDBToken = mDBHelper;
        mID = 0;
        if(!initValori(ID))
            throw new RuntimeException();
    }

    //Setter


    public void setDBToken(DBAdapter db){
        mDBToken = db;
    }

    public void setDate(String v) {
        mDate = v;
    }

    public void setPeso(float v) {
        mPeso = v;
    }

    public void setID(long id){mID = id;}

    //Getter
    public long getID(){
        return this.mID;
    }
    public String getDate() {
        return this.mDate;
    }

    public float getPeso() {
        return this.mPeso;
    }

    //metodi pubblici
    public boolean salva() {
        if (mID != 0)
            return update();
        else
            return insert();
    }

    public boolean delete(){
        if(mID == 0)
            return true;
        else
        {
            SQLiteDatabase db = mDBToken.getWritableDatabase();

            // Define 'where' part of query.
            String selection = COLUMN_NAME_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { String.valueOf(mID)};
            // Issue SQL statement.
            int rowAfferted = db.delete(TABLE_NAME, selection, selectionArgs);
            if(rowAfferted > 0)
                return true;
            else{
                mDBToken.AddEccezione(TABLE_NAME + ": Cancellazione non riuscita! ID:" + mID);
                return false;
            }

        }
    }

    //metodi privati
    private boolean initValori(int ID) {
        SQLiteDatabase db = mDBToken.getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_PESO
        };
        String selection =  COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(ID) };
        String sortOrder = COLUMN_NAME_ID + " DESC";
        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        cursor.moveToFirst();
        try {
            mID = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID));
            mDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE));
            mPeso = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_PESO));
        }catch(Exception e){}
        if(mID != 0)
            return true;
        else{
            mDBToken.AddEccezione(TABLE_NAME + ": Lettura non riuscita! ID:" + mID);
            return false;
        }
    }

    private boolean update() {
        SQLiteDatabase db = mDBToken.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PESO, mPeso);
        values.put(COLUMN_NAME_DATE, mDate);
        String selection = COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mID)};
        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if (count == 0) {
            mDBToken.AddEccezione(TABLE_NAME + ": Aggiornamento non riuscito! ID:" + mID);
            return false;
        } else
            return true;
    }

    private boolean insert(){
        SQLiteDatabase db = mDBToken.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DATE, mDate);
        values.put(COLUMN_NAME_PESO, mPeso);
        long newID = 0;
        try{
           newID =  db.insertOrThrow(TABLE_NAME,null,values);
        }catch(SQLException e){}
        if(newID != 0){
            mID = newID;
            return true;
        }else{
            mDBToken.AddEccezione(TABLE_NAME + ": Inserimento non riuscito! ID:" + mID);
            return false;
        }


    }



}
