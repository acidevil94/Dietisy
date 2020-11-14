package com.example.andrea.dietisy.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.fragment.startupFrags.StartupMisureFragment;
import com.example.andrea.dietisy.fragment.startupFrags.StartupObiettivoFragment;
import com.example.andrea.dietisy.fragment.startupFrags.StartupPesoFragment;
import com.example.andrea.dietisy.utility.PreferencesHandler;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartupActivity extends AppCompatActivity implements
                                        StartupPesoFragment.OnFragmentInteractionListener, StartupMisureFragment.OnFragmentInteractionListener,
                                        StartupObiettivoFragment.OnFragmentInteractionListener{


    Handler mHandler;

    private enum Frammenti{
        NESSUNO,
        STARTUPPESO,
        STARTUPMISURE,
        STARTUPOBIETTIVO
    }

//indica a che frammento è arrivato l'utente
    private Frammenti mFrammento = Frammenti.NESSUNO;

    private static  String CURRENT_TAG = Frammenti.NESSUNO.name() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startup);

        mHandler = new Handler();

        loadFragment();

    }

    private void startActivityPrincipale(){
        Intent startPrincipale;
        startPrincipale = new Intent(StartupActivity.this, PrincipaleActivity.class);
        startActivity(startPrincipale);
        StartupActivity.this.finish();
    }


    private void loadFragment() {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                if(mFrammento == Frammenti.STARTUPOBIETTIVO) {
                    PreferencesHandler pH =  PreferencesHandler.getIstance(getApplicationContext());
                    pH.setPrimaApertura(false);
                    startActivityPrincipale();
                    return;
                }
                Fragment fragment = getNextFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_startup, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        invalidateOptionsMenu();
    }

    private Fragment getNextFragment() {
        switch(mFrammento){


            case STARTUPPESO:
                StartupMisureFragment misureFragment = new StartupMisureFragment();
                CURRENT_TAG = Frammenti.STARTUPMISURE.name();
                mFrammento = Frammenti.STARTUPMISURE;
                return misureFragment;

            case STARTUPMISURE:
                StartupObiettivoFragment obiettivoFragment = new StartupObiettivoFragment();
                CURRENT_TAG = Frammenti.STARTUPOBIETTIVO.name();
                mFrammento = Frammenti.STARTUPOBIETTIVO;
                return obiettivoFragment;

            default:
                StartupPesoFragment inizialeFrag = new StartupPesoFragment();
                CURRENT_TAG = Frammenti.STARTUPPESO.name();
                mFrammento = Frammenti.STARTUPPESO;
                return inizialeFrag;
        }
    }



    @Override
    public void onAvantiPressed() {
        loadFragment();
    }







}
