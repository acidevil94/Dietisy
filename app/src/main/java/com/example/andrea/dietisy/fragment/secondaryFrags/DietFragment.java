package com.example.andrea.dietisy.fragment.secondaryFrags;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.Formule;
import com.example.andrea.dietisy.utility.PreferencesHandler;

import org.w3c.dom.Text;

import java.util.HashMap;



public class DietFragment extends DialogFragment {

    public interface DietFragmentListener {
        void onYes(DialogFragment dialog);
    }

    private DietFragment.DietFragmentListener mListener;
    private PreferencesHandler myPrefHandler;
    private DBAdapter myDB;


    private TextView txtLivelloAttivita, txtMetabolismoBasale, txtMetabolismoTotale, txtMainFragMetabolismo;
    private ImageButton btnSedentario,btnLeggAttivo,btnAttivo,btnMoltoAttivo,btnExtAttivo;
    private Button btnOK;

    private HashMap iconeTag;
    private boolean[] iconeAccese;  // in questo ordine: sedentario,leggermente attivo,attivo,molto attivo, ext att


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static DietFragment newInstance(DietFragment.DietFragmentListener listener,
                                            PreferencesHandler myPrefHandler, DBAdapter db,TextView txtMainFragMetabolismo) {
        DietFragment fragment = new DietFragment();
        fragment.mListener = listener;
        fragment.myPrefHandler = myPrefHandler;
        fragment.myDB = db;
        fragment.txtMainFragMetabolismo = txtMainFragMetabolismo;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diet, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        initCtrl(v);
        initMap();
        initIcone();

        refreshCtrl();
        return v;
    }

    private void initMap(){
        iconeTag = new HashMap();
        iconeTag.put("sedentario",0);
        iconeTag.put("leggermente_attivo",1);
        iconeTag.put("attivo",2);
        iconeTag.put("molto_attivo",3);
        iconeTag.put("ext_attivo",4);
    }

    private void selectLvlAtt(boolean[] iconeAccese,TextView txtLvlAtt) {
        Formule.LivelloAttività la = myPrefHandler.getLivelloAttività();
        switch(la){
            case SEDENTARIO:
                iconeAccese[0] = true;
                btnSedentario.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_sedentario_filled));
                txtLvlAtt.setText("Sedentario");
                break;
            case LEGGERMENTE_ATTIVO:
                iconeAccese[1] = true;
                btnLeggAttivo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_leggermente_attivo_filled));
                txtLvlAtt.setText("Leggermente Attivo");
                break;
            case MODERATAMENTE_ATTIVO:
                iconeAccese[2] = true;
                btnAttivo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_attivo_filled));
                txtLvlAtt.setText("Moderatamente Attivo");
                break;
            case MOLTO_ATTIVO:
                iconeAccese[3] = true;
                btnMoltoAttivo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_molto_attivo_filled));
                txtLvlAtt.setText("Molto Attivo");
                break;
            case ESTREMAMENTE_ATTIVO:
                iconeAccese[4] = true;
                btnExtAttivo.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_ext_attivo_filled));
                txtLvlAtt.setText("Estremamente Attivo");
                break;
            default:
                return;
        }
    }

    private void initIcone() {
        iconeAccese = new boolean[5];

        selectLvlAtt(iconeAccese,txtLivelloAttivita);

        IconListener icListener = new IconListener();


        btnSedentario.setTag("sedentario");
        btnSedentario.setOnClickListener(icListener);

        btnLeggAttivo.setTag("leggermente_attivo");
        btnLeggAttivo.setOnClickListener(icListener);

        btnAttivo.setTag("attivo");
        btnAttivo.setOnClickListener(icListener);

        btnMoltoAttivo.setTag("molto_attivo");
        btnMoltoAttivo.setOnClickListener(icListener);

        btnExtAttivo.setTag("ext_attivo");
        btnExtAttivo.setOnClickListener(icListener);
    }

    private void initCtrl(View v){
        btnOK = (Button) v.findViewById(R.id.btnDietFragOK);
        txtLivelloAttivita = (TextView) v.findViewById(R.id.txtLvlAttivitàDiet);
        txtMetabolismoBasale = (TextView) v.findViewById(R.id.txtMetabolismoBasaleDiet);
        txtMetabolismoTotale = (TextView) v.findViewById(R.id.txtMetabolismoTotaleDiet);

        btnSedentario = (ImageButton) v.findViewById(R.id.btnSedentarioDiet);
        btnLeggAttivo = (ImageButton) v.findViewById(R.id.btnLeggermenteAttivoDiet);
        btnAttivo = (ImageButton) v.findViewById(R.id.btnAttivoDiet);
        btnMoltoAttivo = (ImageButton) v.findViewById(R.id.btnMoltoAttivoDiet);
        btnExtAttivo = (ImageButton) v.findViewById(R.id.btnExtAttivoDiet);


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onYes(DietFragment.this);
                }
            }
        });

    }


    private void refreshCtrl() {

        float metabolismoBasale = Formule.calcolaMetabolismoBasale(myPrefHandler,myDB);
        float metabolismoTotale = Formule.calcolaMetabolismoTotale(myPrefHandler,myDB);

        String strMetabolismoTotale = (int)metabolismoTotale + " Kcal";

        txtMetabolismoBasale.setText((int)metabolismoBasale + " Kcal");
        txtMetabolismoTotale.setText(strMetabolismoTotale);

        txtMainFragMetabolismo.setText(strMetabolismoTotale);

    }


    private class IconListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            ImageButton btn = (ImageButton) view;
            String name = (String) btn.getTag();
            Context c = getContext();
            Formule.LivelloAttività la;
            Drawable d;
            int i = (int) iconeTag.get(name);
            if(iconeAccese[i]){
                return;
            }else{
                d  = c.getResources().getDrawable(c.getResources().getIdentifier("ic_" + name + "_filled", "drawable", c.getPackageName()));
            }
            btn.setImageDrawable(d);
            iconeAccese[i] = !iconeAccese[i];
            String strLvlAtt = "";
            if(iconeAccese[i]){
                switch(name){
                    case "sedentario":
                        la = Formule.LivelloAttività.SEDENTARIO;
                        if(iconeAccese[1]) {
                            btnLeggAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_leggermente_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[1] = false;
                        }
                        if(iconeAccese[2]) {
                            btnAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[2] = false;
                        }
                        if(iconeAccese[3]) {
                            btnMoltoAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_molto_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[3] = false;
                        }
                        if(iconeAccese[4]) {
                            btnExtAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_ext_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[4] = false;
                        }
                        strLvlAtt = "Sedentario";
                        break;
                    case "leggermente_attivo":
                        la = Formule.LivelloAttività.LEGGERMENTE_ATTIVO;
                        if(iconeAccese[0]) {
                            btnSedentario.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_sedentario_nofill", "drawable", c.getPackageName())));
                            iconeAccese[0] = false;
                        }
                        if(iconeAccese[2]) {
                            btnAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[2] = false;
                        }
                        if(iconeAccese[3]) {
                            btnMoltoAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_molto_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[3] = false;
                        }
                        if(iconeAccese[4]) {
                            btnExtAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_ext_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[4] = false;
                        }
                        strLvlAtt = "Leggermente Attivo";
                        break;
                    case "attivo":
                        la = Formule.LivelloAttività.MODERATAMENTE_ATTIVO;
                        if(iconeAccese[0]) {
                            btnSedentario.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_sedentario_nofill", "drawable", c.getPackageName())));
                            iconeAccese[0] = false;
                        }
                        if(iconeAccese[1]) {
                            btnLeggAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_leggermente_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[1] = false;
                        }
                        if(iconeAccese[3]) {
                            btnMoltoAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_molto_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[3] = false;
                        }
                        if(iconeAccese[4]) {
                            btnExtAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_ext_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[4] = false;
                        }
                        strLvlAtt = "Moderatamente Attivo";
                        break;
                    case "molto_attivo":
                        la = Formule.LivelloAttività.MOLTO_ATTIVO;
                        if(iconeAccese[0]) {
                            btnSedentario.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_sedentario_nofill", "drawable", c.getPackageName())));
                            iconeAccese[0] = false;
                        }
                        if(iconeAccese[1]) {
                            btnLeggAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_leggermente_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[1] = false;
                        }
                        if(iconeAccese[2]) {
                            btnAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[2] = false;
                        }
                        if(iconeAccese[4]) {
                            btnExtAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_ext_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[4] = false;
                        }
                        strLvlAtt = "Molto Attivo";
                        break;
                    case "ext_attivo":
                        la = Formule.LivelloAttività.ESTREMAMENTE_ATTIVO;
                        if(iconeAccese[0]) {
                            btnSedentario.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_sedentario_nofill", "drawable", c.getPackageName())));
                            iconeAccese[0] = false;
                        }
                        if(iconeAccese[1]) {
                            btnLeggAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_leggermente_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[1] = false;
                        }
                        if(iconeAccese[2]) {
                            btnAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[2] = false;
                        }
                        if(iconeAccese[3]) {
                            btnMoltoAttivo.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_molto_attivo_nofill", "drawable", c.getPackageName())));
                            iconeAccese[3] = false;
                        }
                        strLvlAtt = "Estremamente Attivo";
                        break;
                    default:
                        la = Formule.LivelloAttività.SEDENTARIO;
                        break;
                }

                txtLivelloAttivita.setText(strLvlAtt);

                myPrefHandler.setLivelloAttività(la);

                refreshCtrl();


            }


        }
    }


}


