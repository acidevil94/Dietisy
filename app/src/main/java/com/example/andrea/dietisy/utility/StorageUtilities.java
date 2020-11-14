package com.example.andrea.dietisy.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.andrea.dietisy.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by andrea on 02/12/2016.
 */

public class StorageUtilities {

    private static final String LOG_TAG = "STORAGE_UTILITIES";

    // usare per leggere le preferenze di un'applicazione
    public static int getPreferenze(Context context,String chiave,int defaultV) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int value = sharedPref.getInt(chiave, defaultV);
        return value;
    }
    public static String getPreferenze(Context context,String chiave,String defaultV) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String value = sharedPref.getString(chiave, defaultV);
        return value;
    }
    public static float getPreferenze(Context context,String chiave,float defaultV) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        float value = sharedPref.getFloat(chiave, defaultV);
        return value;
    }


    public static void scriviPreferenze(Context context,String chiave,String valore){
        SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(chiave, valore);
        editor.commit();
    }

    public static void scriviPreferenze(Context context,String chiave,int valore){
        SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(chiave, valore);
        editor.commit();
    }

    public static void scriviPreferenze(Context context,String chiave,float valore){
        SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(chiave, valore);
        editor.commit();
    }






    public static void scriviFileInterno(Context context,String text){
        File f = new File(context.getFilesDir(),"ciao.txt");
        FileOutputStream output;
        try {
            output = new FileOutputStream(f,false);
            byte[] b = text.getBytes();
            output.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String apriFileInterno(Context context){
        File f = new File(context.getFilesDir(),"ciao.txt");
        FileInputStream input;
        String ris = "";
        try {
            input = new FileInputStream(f);
           int r = input.read();
            while(r > 0) {
                char c = (char) r;
                ris += c;
                r = input.read();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ris;

    }

    public static boolean getPreferenze(Context context, String chiave, boolean defaultV) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean value = sharedPref.getBoolean(chiave, defaultV);
        return value;
    }

    public static void scriviPreferenze(Context context, String chiave, boolean valore) {
        SharedPreferences sharedPref = context.getSharedPreferences( context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(chiave, valore);
        editor.commit();
    }



    /*
    //alla fine scrivono sullo stesso file, perciò conviene usare il file interno.
    public static void scriviFileEsterno(Context context,String s){
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)) //controlla se sd è presente
            return;
        File file = new File(context.getExternalFilesDir(   //prende una directory sulla SD privata, la cartella documents
                Environment.DIRECTORY_DOCUMENTS),"ciao.txt");
        FileOutputStream output;
        try {
            output = new FileOutputStream(file);
            byte[] b = s.getBytes();
            output.write(b);

        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG,  e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static String apriFileEsterno(Context context){
        String state = Environment.getExternalStorageState();
        if(!Environment.MEDIA_MOUNTED.equals(state)) //controlla se sd è presente
            return "";
        File f = new File(context.getExternalFilesDir(   //prende una directory sulla SD privata, la cartella documents
                Environment.DIRECTORY_DOCUMENTS),"ciao.txt");
        FileInputStream input;
        String ris = "";
        try {
            input = new FileInputStream(f);
            int r = input.read();
            while(r > 0) {
                char c = (char) r;
                ris += c;
                r = input.read();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ris;

    }
*/

}
