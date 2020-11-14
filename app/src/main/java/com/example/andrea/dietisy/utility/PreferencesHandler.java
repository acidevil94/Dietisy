package com.example.andrea.dietisy.utility;

import android.content.Context;

import com.example.andrea.dietisy.R;


/**
 * Created by andrea on 04/12/2016.
 */

public  class PreferencesHandler {


     private static PreferencesHandler mSingleton = null;
     private final Context mContext;


    private PreferencesHandler(Context c){
        this.mContext = c;
        setupPreferenze();
    }

    public static PreferencesHandler getIstance(Context c){
        if(mSingleton == null)
            mSingleton = new PreferencesHandler(c);
        return mSingleton;
    }

    private boolean mPrimaApertura = true;


    private String mNome;
   // private String mCognome;

    private float mAltezza ;
    private boolean mSex = false;       //true:male, false:female
    private String mDataNascita ;


    private float mPesoObiettivo;
    private float mPesoIniziale;
  //  private Formule.Scopo mScopo;


    private Formule.LivelloAttività mLivelloAttività;

    private float mCirconferenzaPolso ;

    public boolean isPrimaApertura() {
        return mPrimaApertura;
    }

    public void setPrimaApertura(boolean mPrimaApertura) {
        this.mPrimaApertura = mPrimaApertura;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_prima_apertura),mPrimaApertura);
    }



        public float getAltezza(){
            return mAltezza;
        }

        public void setAltezza(float v){
            mAltezza = v;
            StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_tall),mAltezza);
        }

    public float getCirconferenzaPolso(){
        return mCirconferenzaPolso;
    }

    public void setCirconferenzaPolso(float v){
        mCirconferenzaPolso = v;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_polso),mCirconferenzaPolso);
    }

    public boolean getSex(){
        return mSex;
    }

    public void setSex(boolean v){
        mSex = v;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_sex),mSex);
    }

    public String getDataNascita(){
        return mDataNascita;
    }

    public void setDataNascita(String v){
        mDataNascita = v;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_data_nascita),mDataNascita);
    }

    public String getNome(){
        return mNome;
    }

    public void setNome(String v){
        mNome = v;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_nome),mNome);
    }

  /*  public String getCognome(){
        return mCognome;
    }

    public void setCognome(String v){
        mCognome = v;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_cognome),mCognome);
    }*/

    public float getPesoObiettivo() {
        return mPesoObiettivo;
    }

    public void setPesoObiettivo(float mPesoObiettivo) {
        this.mPesoObiettivo = mPesoObiettivo;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_peso_obiettivo),mPesoObiettivo);
    }

    public float getPesoIniziale() {
        return mPesoIniziale;
    }

    public void setPesoIniziale(float mPeso) {
        this.mPesoIniziale = mPeso;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_peso_iniziale),mPesoObiettivo);
    }

    public Formule.LivelloAttività getLivelloAttività() {
        return mLivelloAttività;
    }

    public void setLivelloAttività(Formule.LivelloAttività mLivelloAttività) {
        this.mLivelloAttività = mLivelloAttività;
        StorageUtilities.scriviPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_livello_attivita),mLivelloAttività.name());
    }


    public void setupPreferenze(){
        mNome = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_nome),"");
       // mCognome = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_cognome),"");
        mAltezza = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_tall),0.0f);
        mSex = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_sex),false);
        mPrimaApertura = StorageUtilities.getPreferenze(mContext,mContext.getResources().getString(R.string.prefkey_prima_apertura),true);
        mDataNascita = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_data_nascita),"");
        mCirconferenzaPolso = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_polso),0.0f);

        mPesoObiettivo = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_peso_obiettivo),0.0f);
        mPesoIniziale = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_peso_iniziale),0.0f);

        String livelloAttivita;
        livelloAttivita = StorageUtilities.getPreferenze(mContext, mContext.getResources().getString(R.string.prefkey_livello_attivita),"");

        switch(livelloAttivita){
            case "SEDENTARIO":
                mLivelloAttività = Formule.LivelloAttività.SEDENTARIO;
                break;
            case "LEGGERMENTE_ATTIVO":
                mLivelloAttività = Formule.LivelloAttività.LEGGERMENTE_ATTIVO;
                break;
            case "MODERATAMENTE_ATTIVO":
                mLivelloAttività = Formule.LivelloAttività.MODERATAMENTE_ATTIVO;
                break;
            case "MOLTO_ATTIVO":
                mLivelloAttività = Formule.LivelloAttività.MOLTO_ATTIVO;
                break;
            case "ESTREMAMENTE_ATTIVO":
                mLivelloAttività = Formule.LivelloAttività.ESTREMAMENTE_ATTIVO;
                break;
            default:
                mLivelloAttività = Formule.LivelloAttività.SEDENTARIO;
                break;
        }

    }



}
