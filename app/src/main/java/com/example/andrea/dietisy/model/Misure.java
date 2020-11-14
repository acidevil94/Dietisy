package com.example.andrea.dietisy.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by andrea on 30/01/2017.
 */

public class Misure {
    //metodi & proprietà statiche
    public static final String TABLE_NAME = "Misure";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_DATE = "Data";
    public static final String COLUMN_NAME_VITA = "Vita";
    public static final String COLUMN_NAME_FIANCHI = "Fianchi";
    public static final String COLUMN_NAME_COLLO = "Collo";




    public static String getCreateSQL() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DATE + DBAdapter.TEXT_TYPE + "," +
                COLUMN_NAME_VITA + DBAdapter.FLOAT_TYPE + "," +
                COLUMN_NAME_FIANCHI + DBAdapter.FLOAT_TYPE + "," +
                COLUMN_NAME_COLLO + DBAdapter.FLOAT_TYPE +
                " );";
    }

    public static Misure getLastMisure(DBAdapter dbHelper){
        Misure ris = new Misure(dbHelper);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_VITA,
                COLUMN_NAME_FIANCHI,
                COLUMN_NAME_COLLO
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
            ris.setVita(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_VITA)));
            ris.setCollo(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_COLLO)));
            ris.setFianchi(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_FIANCHI)));
        } catch (Exception e) {
            dbHelper.AddEccezione(TABLE_NAME + ": Lettura ultimo non riuscita!");
        }

        return ris;
    }

    public static List<Misure> getMisure(DBAdapter dbHelper, String dataDa, String dataA) {
        LinkedList<Misure> ris = new LinkedList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_DATE,
                COLUMN_NAME_FIANCHI,
                COLUMN_NAME_COLLO,
                COLUMN_NAME_VITA
        };

        //String sortOrder = COLUMN_NAME_ID ;

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
        Misure r ;
        try {
            do {
                r = new Misure(dbHelper);
                r.setID(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)));
                int i = cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE);
                r.setDate(cursor.getString(i));
                r.setVita(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_VITA)));
                r.setCollo(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_COLLO)));
                r.setFianchi(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_FIANCHI)));
                ris.add(r);
            }while(cursor.moveToNext());
        } catch (Exception e) {
            dbHelper.AddEccezione(TABLE_NAME + ": Lettura all non riuscita!");
        }

        return ris;
    }

    //Proprietà
    private DBAdapter mDBToken;

    private long mID;
    private String mDate;
    private float mVita;
    private float mFianchi;
    private float mCollo;


    //Costruttori
    public Misure() {
        mID = 0;
    }

    public Misure(DBAdapter dbHelper) {
        this.mDBToken = dbHelper;
        mID = 0;
    }

    public Misure(DBAdapter mDBHelper, int ID) throws RuntimeException{
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


    public void setID(long id){mID = id;}

    public void setVita(float v){ mVita = v;}

    public void setFianchi(float v){ mFianchi = v;}

    public void setCollo(float v){ mCollo = v;}

    //Getter
    public long getID(){
        return this.mID;
    }
    public String getDate() {
        return this.mDate;
    }

    public float getVita(){return mVita;}
    public float getCollo(){return mCollo;}
    public float getFianchi(){return mFianchi;}

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
                COLUMN_NAME_VITA,
                COLUMN_NAME_COLLO,
                COLUMN_NAME_FIANCHI
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
            mCollo = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_COLLO));
            mVita = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_VITA));
            mFianchi = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_FIANCHI));

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
        values.put(COLUMN_NAME_COLLO, mCollo);
        values.put(COLUMN_NAME_FIANCHI, mFianchi);
        values.put(COLUMN_NAME_VITA, mVita);
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
        values.put(COLUMN_NAME_COLLO, mCollo);
        values.put(COLUMN_NAME_FIANCHI, mFianchi);
        values.put(COLUMN_NAME_VITA, mVita);
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
