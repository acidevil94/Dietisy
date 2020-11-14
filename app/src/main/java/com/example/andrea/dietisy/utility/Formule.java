package com.example.andrea.dietisy.utility;

import com.example.andrea.dietisy.model.Misure;
import com.example.andrea.dietisy.model.Peso;
import com.example.andrea.dietisy.model.DBAdapter;

import java.util.LinkedList;

/**
 * Created by andrea on 11/12/2016.
 *
 */

public class Formule {



    /*

    Tipo Morfologico
    TIPO MORFOLOGICO 	UOMO 	DONNA
    Longilineo 	Più di 10,4 	Più di 10,9
    Normolineo 	9,6-10,4 	    9,9-10,9
    Brevilineo 	Meno di 9,6 	Meno di 9,9
     */

    public enum Morfologia{
        Brevilineo,
        Normolineo,
        Longilineo
    }



   public enum Scopo{
        DIMAGRIMENTO,
        MANTENIMENTO,
        TONIFICAMENTO
    }






    public enum LivelloAttività{


        SEDENTARIO,
        LEGGERMENTE_ATTIVO,
        MODERATAMENTE_ATTIVO,
        MOLTO_ATTIVO,
        ESTREMAMENTE_ATTIVO


    }

    public static String formattaLivelloAttività(Formule.LivelloAttività l){
        switch(l){
            case SEDENTARIO:
                return "Sedentario";
            case LEGGERMENTE_ATTIVO:
                return "Leggermente Attivo";
            case MODERATAMENTE_ATTIVO:
                return "Moderatamente Attivo";
            case MOLTO_ATTIVO:
                return "Molto Attivo";

            default:
                return "";
        }
    }




    // fonte http://www.my-personaltrainer.it/circonferenza-polso.html

    public static Morfologia getMorfologia(float polso,float h,boolean male){
        float morfologia =  h / polso;
        if(male){
            if(morfologia < 9.6f)
                return Morfologia.Brevilineo;
            else if(morfologia <= 10.4f)
                return Morfologia.Normolineo;
            else
                return Morfologia.Longilineo;
        }else{
            if(morfologia < 9.9f)
                return Morfologia.Brevilineo;
            else if(morfologia <= 10.9f)
                return Morfologia.Normolineo;
            else
                return Morfologia.Longilineo;
        }
    }

    //NUOVO BMI: 1,3 x peso (Kg) / (Altezza (m)) ^ 2,5

    /*

    < 16.5 	GRAVE MAGREZZA
    16-18,49 SOTTOPESO
    18.5-24,99 NORMOPESO
    25-29,99 SOVRAPPESO
    30-34,99 	OBESITÀ CLASSE I (lieve)
    35-39,99 OBESITÀ CLASSE II (media)
    > 40 OBESITÀCLASSE III (grave)

     */
    public static float calcoloNuovoBMI(PreferencesHandler pH, DBAdapter db){
        float h = pH.getAltezza();
        Peso p = Peso.getLastPeso(db);
        if(p.getID() == 0 || h == 0) {
            //prima apertura app oppure ancora non insierisce altezza
            db.svuotaElencoEccezioni();
            return -1;
        }
        float lastPeso = p.getPeso();
        double den = Math.pow(h/100,2.5);
        double ris = 1.3 * lastPeso / den;
        return (float)ris;
    }



    public static float calcoloVecchioBMI(PreferencesHandler pH, DBAdapter db){
        float h = pH.getAltezza();
        Peso p = Peso.getLastPeso(db);
        float lastPeso = p.getPeso();
        double den = Math.pow(h/100,2);
        double ris = lastPeso / den;
        return (float)ris;
    }




    /*
    FORMULA DI KEYS - consigliata
    Peso ideale Uomini = (altezza in m)^2 x 22,1
Peso ideale Donne = (altezza in m)^2 x 20,6
Tratto da http://www.my-personaltrainer.it/peso-teorico.html


er scoprire a quale tipo morfologico appartieni misura la circonferenza del tuo polso destro (in centimetri)
 alla base della mano, applica la seguente formula e confrontala con i dati riportati in tabella

 morfologia = statura / polso


Tipo Morfologico
TIPO MORFOLOGICO 	UOMO 	DONNE
Longilineo 	Più di 10,4 	Più di 10,9
Normolineo 	9,6-10,4 	    9,9-10,9
Brevilineo 	Meno di 9,6 	Meno di 9,9

Se rientrate nella categoria dei longilinei il peso forma precedentemente calcolato
è troppo alto (ridurlo del 10%), se invece rientrate nella categoria dei brevilinei il
 il peso forma ideale è superiore a quello indicato dal nostro calcolatore automatico (aumentarlo del 10%).
     */
    //TODO: per adesso viene tolto il polso e quindi non influirà più sulle formule. Adattare quando cambierà
    public static float calcoloPesoIdeale(PreferencesHandler prefHandler){
        float ris = -1;

        float h = prefHandler.getAltezza();
        float polso = prefHandler.getCirconferenzaPolso();
        boolean isMaschio = prefHandler.getSex();
        Morfologia morfologia = getMorfologia(polso,h,isMaschio);

        if(h == 0 || polso == 0)
            return ris;

        //calcolo base
        ris = (float) Math.pow((h / 100) , 2);

        if(isMaschio){
            //maschio
            ris = ris * 22.1f;
        }else{
            //femmina
            ris = ris * 20.6f;
        }

        switch (morfologia){
            case Brevilineo:
                ris = ris * 1.1f;
                break;
            case Longilineo:
                ris = ris * 0.9f;
                break;
            default:
                // NORMOLINEO -> va bene il risultato precedente
                break;
        }
        return ris;
    }



    /*Buffon, Roher e Bardeen, successivamente confermata da Quételet e Martin

    Peso ideale Uomini = (1,40 x (altezza in dm) ^ 3)/100

    Peso ideale Donna = (1,35 x  (altezza in dm) ^ 3)/100


Tratto da http://www.my-personaltrainer.it/peso-teorico.html


    public static float calcoloPesoIdeale2(PreferencesHandler prefHandler){
        float h = prefHandler.getAltezza();
        h = h /10;
        float h3 = h * h* h;

        if(prefHandler.getSex()){
            //m
            return (1.4f * h3) / 100 ;
        }else{
            //f
            return (1.35f * h3 )/ 100;
        }
    }

*/
    /*
    Calcolo percentuale massa grassa negli uomini (%) :
495 / {1.0324 – 0.19077 [log(vita-collo)] + 0.15456 [log(statura)]-450

Calcola percentuale massa grassa nelle donne (%) :
495 / {1.29579 – 0.35004 [log(vita+fianchi-collo)] + 0.22100 [log(statura)]} – 450



Valutazione                         	Uomini	    Donne
Peso minimo, pericolo per la salute	    2% - 4%	    10% - 12%
Forma atletica	                        6% - 13%	14% - 20%
Buono stato di fitness	                14% - 17%	21% - 24%
Al di sopra della media	                18% - 25%	25% - 31%
Obesità	                                ≥ di 26%	≥ di 32%

Tratto da http://www.my-personaltrainer.it/massa-grassa.html
     */

    public static float calcoloMassaGrassa(PreferencesHandler pH,DBAdapter db){
        float h = pH.getAltezza();
        Misure m = Misure.getLastMisure(db);
        if(m.getID() == 0 || h == 0) {
            //prima apertura app oppure ancora non insierisce altezza
            db.svuotaElencoEccezioni();
            return -1;
        }
        float lastVita = m.getVita();
        float lastCollo = m.getCollo();
        float lastFianchi = m.getFianchi();
        float massaGrassa;
        float den ;

        if(pH.getSex()){
            den = lastVita-lastCollo;
            if(den <0)
                return -1;
            den = ((float)Math.log10(den));
            den = (1.0324f - 0.19077f * den + 0.15456f * ((float)Math.log10(h)));

            //maschio
            massaGrassa = 495 / den - 450;

        }else{

            den = lastVita+lastFianchi-lastCollo;

            if(den <0)
                return -1;
            //femmina
            massaGrassa = 495 / (1.29579f - 0.35004f * ((float)Math.log10(den)) + 0.22100f * ((float)Math.log10(h))) -450;

        }

        return massaGrassa;
    }



    /*
    calcolo metabolismo basale,
    fonte http://www.shapefit.com/health/basal-metabolic-rate.html
http://www.my-personaltrainer.it/calcolatore-calorico-giornaliero.htm

Calculate Basal Metabolic Rate :=>

Women: 655.095 + (9.5634 x weight ) + (1.849 x height) – (4.6756 x age in years)

Men: 66.4730 + (13.7156 x weight) + (5.033 x height ) – (6.775 x age in years)

     */


	 
    public static float calcolaMetabolismoBasale(PreferencesHandler pH,DBAdapter db){
        float h = pH.getAltezza();
        String dataNascita = pH.getDataNascita();
        Peso p = Peso.getLastPeso(db);
        if(p.getID() == 0 || h == 0 || dataNascita.equals("")) {
            //prima apertura app oppure ancora non insierisce altezza, peso e nascita
            db.svuotaElencoEccezioni();
            return -1;
        }
        float massaGrassa = calcoloMassaGrassa(pH,db);
        float w = p.getPeso();

        float massaMagra = w - (w * massaGrassa / 100);

        boolean male = pH.getSex();
        int a = GeneralUtilities.calcolaDifferenzaDate(dataNascita,GeneralUtilities.getCurrentDate());

        float ris = 0;

        if(male){
            //maschio
            ris = 66.4730f + (13.7156f * massaMagra) + (5.033f * h) - (6.775f * a);
        }else{
            //femmina
            ris = 655.095f + (9.5634f * massaMagra) + (1.849f * h) - (4.6756f * a);
        }


        return ris;
    }

    /*

    Sedentary (little or no exercise): BMR x 1.2
    Lightly active (easy exercise/sports 1-3 days/week): BMR x 1.375
    Moderately active (moderate exercise/sports 3-5 days/week): BMR x 1.55
    Very active (hard exercise/sports 6-7 days a week): BMR x 1.725
    Extremely active (very hard exercise/sports and physical job): BMR x 1.9
     */

    public static float calcolaMetabolismoTotale(PreferencesHandler pH,DBAdapter dbHelper){
        float metabolismoBasale = calcolaMetabolismoBasale(pH,dbHelper);
        LivelloAttività la = pH.getLivelloAttività();
        float ris;
        switch(la){
            case SEDENTARIO:
                ris =  metabolismoBasale * 1.2f;
                break;
            case LEGGERMENTE_ATTIVO:
                ris = metabolismoBasale * 1.375f;
                break;
            case MODERATAMENTE_ATTIVO:
                ris = metabolismoBasale * 1.55f;
                break;
            case MOLTO_ATTIVO:
                ris = metabolismoBasale * 1.725f;
                break;
            case ESTREMAMENTE_ATTIVO:
                ris = metabolismoBasale * 1.9f;
                break;
            default:
                ris = metabolismoBasale;
        }
        return ris;
    }


    public static float getMediaPesi(DBAdapter dbHelper, int qty) {
        LinkedList<Peso> pesi = (LinkedList<Peso>) Peso.getLastPesi(dbHelper,qty);
        int l = pesi.size();
        if(l == 0)
            return 0;
        float sum = 0;
        for(Peso p : pesi){
            sum += p.getPeso();
        }
        return sum / l;
    }
/*

rapporto VITA / ALTEZZA
http://www.nutrizionesport.com/predittori.html
sul sito i valori limite

 */

    public static float getWHR(DBAdapter db, PreferencesHandler pH){
        float h = pH.getAltezza();
        Misure m = Misure.getLastMisure(db);
        if(m.getID() == 0 || h == 0) {
            //prima apertura app oppure ancora non insierisce altezza
            db.svuotaElencoEccezioni();
            return -1;
        }
        float lastVita = m.getVita();
        return lastVita /  h;
    }





}
