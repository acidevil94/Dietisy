package com.example.andrea.dietisy.utility;

import android.content.Context;

import com.example.andrea.dietisy.R;

/**
 * Created by Coluzzi Andrea on 10/02/2018.
 */

public class ConfigValues {

    private Context mContext;

    public Integer[] coloriBMI, coloriMG, coloriWH;

    public Float[] limitiBMI,
                     limitiMGMaschio,
                    limitiMGFemmina,
                    limitiWHMaschio,
                    limitiWHFemmina;
    public String[] descrizioniMGMaschio,
                        descrizioniMGFemmina,
                        descrizioniWHMaschio,
                        descrizioniWHFemmina;


    public ConfigValues(Context c){
        this.mContext = c;
        this.coloriBMI = new Integer[] {
                this.mContext.getResources().getColor(R.color.color_BMI_sottopesoGrave),
                this.mContext.getResources().getColor(R.color.color_BMI_sottopesoLieve),
                this.mContext.getResources().getColor(R.color.color_BMI_normopeso),
                this.mContext.getResources().getColor(R.color.color_BMI_sovrappeso),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse1),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse2),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse3)
        };
        this.coloriMG = new Integer[] {
                this.mContext.getResources().getColor(R.color.color_BMI_sottopesoGrave),
                this.mContext.getResources().getColor(R.color.color_BMI_sottopesoLieve),
                this.mContext.getResources().getColor(R.color.color_BMI_normopeso),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse1),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse2)
        };
        this.coloriWH = new Integer[] {
                this.mContext.getResources().getColor(R.color.color_BMI_sottopesoGrave),
                this.mContext.getResources().getColor(R.color.color_BMI_sottopesoLieve),
                this.mContext.getResources().getColor(R.color.color_BMI_normopeso),
                this.mContext.getResources().getColor(R.color.color_BMI_sovrappeso),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse1),
                this.mContext.getResources().getColor(R.color.color_BMI_obesoClasse2)
        };


        this.limitiBMI = new Float[]{
                16.49f,         //meno di 16.5 sottopeso grave
                18.49f,            //meno di 18.5 sottopeso lieve
                24.99f,             //meno di 25 normopeso
                29.99f,             //meno di 30 sovrappeso
                34.99f,             //meno di 35 obeso 1
                39.99f              //meno di 40 obeso 2
                                    //più di 40 obeso 3
        };

        this.limitiMGMaschio = new Float[]{
                5.99f,              //meno di 6 grasso minimo, pericolo
                13.99f,             //meno di 14 forma atletica
                17.99f,                //buono stato di salute
                25.99f                  //sopra media
                                        // > 26 obesita
        };

        this.limitiMGFemmina= new Float[]{
                13.99f,
                20.99f,
                24.99f,
                31.99f
        };

        //vita altezza su http://www.nutrizionesport.com/predittori.html

        this.limitiWHMaschio = new Float[]{
                34.99f,         // magrezza grave
                42.99f,            //Esageratamente magro
                52.99f,             //in salute
                57.99f,              //sovrappeso
                62.99f,             //seriamente sovrappeso
                                    //obeso grave
        };

        this.limitiWHFemmina= new Float[]{
                34.99f,         // magrezza grave
                41.99f,            //Esageratamente magro
                48.99f,             //in salute
                53.99f,              //sovrappeso
                57.99f,             //seriamente sovrappeso
                //obeso grave
        };

        this.descrizioniMGMaschio = new String[]{
                "< 6%",
                "6% - 14%",
                "14% - 18%",
                "18% - 26%",
                "> 26%"
        };

        this.descrizioniMGFemmina = new String[]{
                "< 14%",
                "14% - 21%",
                "21% - 25%",
                "25% - 32%",
                "> 32%"
        };

        this.descrizioniWHMaschio = new String[]{
                "< 0.35",
                "0.35 - 0.43",
                "0.43 - 0.53",
                "0.53 - 0.58",
                "0.58 - 0.63",
                "> 0.63"
        };

        this.descrizioniWHFemmina = new String[]{
                "< 0.35",
                "0.35 - 0.42",
                "0.42 - 0.49",
                "0.49 - 0.54",
                "0.54 - 0.58",
                "> 0.58"
        };
    }


    public String[] getDescrizioniMG(boolean maschio){
        if(maschio)
            return this.descrizioniMGMaschio;
        else
            return this.descrizioniMGFemmina;
    }

    public String[] getDescrizioniWH(boolean maschio){
        if(maschio)
            return this.descrizioniWHMaschio;
        else
            return this.descrizioniWHFemmina;
    }




}
