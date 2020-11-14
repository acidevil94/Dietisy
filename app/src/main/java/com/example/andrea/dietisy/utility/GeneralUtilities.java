package com.example.andrea.dietisy.utility;


import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.example.andrea.dietisy.fragment.secondaryFrags.MsgBoxFragment;
import com.example.andrea.dietisy.model.DBAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrea on 04/12/2016.
 */

public class GeneralUtilities {


    public static class MyData{
        public int giorno,mese,anno;

        public MyData(int g,int m,int a){
            giorno = g;
            mese=m;
            anno = a;
        }
    }



    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) +1) + "-" + c.get(Calendar.DAY_OF_MONTH);
    }


    public static MyData stringa2Data(String d){
        String[] array = d.split("-");
        int anno = Integer.parseInt(array[0]);
        int mese = Integer.parseInt(array[1]);
        int giorno = Integer.parseInt(array[2]);
        return new MyData(giorno,mese,anno);
    }

    public static boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }


    public static <T> T getCorrectObjectFromValues(T[] scelte, float valore, Float[] valoriLimiti) {
        float lowerBound = 0f;
        for (int i = 0; i< valoriLimiti.length ; i++) {
            float limite = valoriLimiti[i];
            if(isBetween(valore,lowerBound,limite))
                return scelte[i];
            lowerBound = limite;
        }
        return scelte[scelte.length-1];
    }

    public static <T> T getCorrectObjectFromValues(T[] scelte, float valore, Float[] valoriLimMaschio, Float[] valoriLimFemmina, boolean maschio){
        float lowerBound = 0f;
        Float[] valoriLimiti = maschio ? valoriLimMaschio : valoriLimFemmina;
        for (int i = 0; i< valoriLimiti.length ; i++) {
            float limite = valoriLimiti[i];
            if(isBetween(valore,lowerBound,limite))
                return scelte[i];
            lowerBound = limite;
        }
        return scelte[scelte.length-1];
    }


    public static String getDataStringa(String d){

        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        Date d1 = null;
        try {
            d1 = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return  "";
        }
        int m = d1.getMonth();
        return d1.getDate() + " " + getMeseStringa(m-1) + " " +  ( d1.getYear() +1900) ;
        //return d;
    }


    public static String getMeseStringa(int mese){
        if(mese <0 || mese > 11){
            return "";
        }
        String[] mesi = {"Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno","Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"};
        return mesi[mese];
    }




    public static boolean isNumber(String s){
        try
        {
            Double.parseDouble(s);
            return true;
        }
        catch(NumberFormatException e)
        {
            //not a double
            return false;
        }
    }

    public static void mostraErrori(DBAdapter dbHelper, MsgBoxFragment.MsgBoxListener l, Context c, FragmentManager spm){
        String s = "";
        for(String e : dbHelper.getElencoEccezioni()){
            s += e + " ";
        }
        DialogFragment fragment = MsgBoxFragment.newInstance(l,false,s);
        fragment.show(spm, "Attenzione!");
        dbHelper.svuotaElencoEccezioni();
    }

    public static int calcolaDifferenzaDate(String dataNascita, String currentDate) {

        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dataNascita);
            d2 = format.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

        long diff = d2.getTime() - d1.getTime();

        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return (int) days / 365;
    }

}
