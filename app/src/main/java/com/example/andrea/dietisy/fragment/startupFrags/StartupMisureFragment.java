package com.example.andrea.dietisy.fragment.startupFrags;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.model.Misure;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.Formule;
import com.example.andrea.dietisy.utility.PreferencesHandler;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;

import java.util.HashMap;

import static com.example.andrea.dietisy.utility.GeneralUtilities.getCurrentDate;

public class StartupMisureFragment extends Fragment {


    private StartupMisureFragment.OnFragmentInteractionListener mListener;
    private PreferencesHandler myPrefHandler;
    private DBAdapter dbHelper;
    private ImageButton btnAvanti;
    private EditText txtVita,txtCollo,txtFianchi, txtPolso;
    private TextView txtLvlAttivita;
    private ImageButton btnSedentario,btnLeggAttivo,btnAttivo,btnMoltoAttivo,btnExtAttivo;
    private HashMap iconeTag;
    private boolean[] iconeAccese;  // in questo ordine: sedentario,leggermente attivo,attivo,molto attivo, ext att



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_startup_misure, container, false);

        myPrefHandler = PreferencesHandler.getIstance(getContext());
        dbHelper = DBAdapter.getIstance(getContext());

        iconeAccese = new boolean[5];

        iconeAccese[2] = true;

        initCtrls(v);
        initMap();

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

    private void initCtrls(View v){
        StartupMisureFragment.IconListener icListener = new StartupMisureFragment.IconListener();

        btnSedentario = (ImageButton) v.findViewById(R.id.btnSedentario);
        btnSedentario.setTag("sedentario");
        btnSedentario.setOnClickListener(icListener);

        btnLeggAttivo = (ImageButton) v.findViewById(R.id.btnLeggermenteAttivo);
        btnLeggAttivo.setTag("leggermente_attivo");
        btnLeggAttivo.setOnClickListener(icListener);

        btnAttivo = (ImageButton) v.findViewById(R.id.btnAttivo);
        btnAttivo.setTag("attivo");
        btnAttivo.setOnClickListener(icListener);

        btnMoltoAttivo = (ImageButton) v.findViewById(R.id.btnMoltoAttivo);
        btnMoltoAttivo.setTag("molto_attivo");
        btnMoltoAttivo.setOnClickListener(icListener);

        btnExtAttivo = (ImageButton) v.findViewById(R.id.btnExtAttivo);
        btnExtAttivo.setTag("ext_attivo");
        btnExtAttivo.setOnClickListener(icListener);

        btnAvanti = (ImageButton) v.findViewById(R.id.btnAvantiStartupMisure);
        txtCollo = (EditText) v.findViewById(R.id.txtColloStartup);
        txtVita = (EditText) v.findViewById(R.id.txtVitaStartup);
        txtFianchi = (EditText) v.findViewById(R.id.txtFianchiStartup);
        txtLvlAttivita = (TextView)  v.findViewById(R.id.txtLvlAttivitàStartup);
        txtPolso = (EditText) v.findViewById(R.id.txtPolsoStartup);


        btnAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( salvaPreferenze())
                    if (mListener != null)
                        mListener.onAvantiPressed();
            }
        });
    }

    private boolean ckSalvaPreferenze(){

        boolean ris = true;
        int selectedIcon = 0;

        for(int i = 0;i<5;i++){
            if(iconeAccese[i])
                selectedIcon++;
        }
        if(selectedIcon != 1){
            ris = false;
        }

        String vita = txtVita.getText().toString();
        if(vita.equals("")){
            ris = false;
        }

        String collo = txtCollo.getText().toString();
        if(collo.equals("")){
            ris = false;
        }

        String fianchi = txtFianchi.getText().toString();
        if(fianchi.equals("")){
            ris = false;
        }

        String polso = txtPolso.getText().toString();
        if(polso.equals("")){
            ris = false;
        }


        if(!ris){
            Cue.init()
                    .with(getContext())
                    .setMessage(getResources().getString(R.string.error_msg_form_not_filled))
                    .setGravity(Gravity.CENTER | Gravity.BOTTOM)
                    .setType(Type.DANGER)
                    .setDuration(Duration.LONG)
                    .show();
        }

        return ris;
    }


    private boolean salvaPreferenze(){

        if(!ckSalvaPreferenze())
            return false;


        if(iconeAccese[0]){
            //sedentario
            myPrefHandler.setLivelloAttività(Formule.LivelloAttività.SEDENTARIO);
        }else if(iconeAccese[1]){
            //poco attivo
            myPrefHandler.setLivelloAttività(Formule.LivelloAttività.LEGGERMENTE_ATTIVO);
        }else if(iconeAccese[2]){
            //attivo
            myPrefHandler.setLivelloAttività(Formule.LivelloAttività.MODERATAMENTE_ATTIVO);
        }else if(iconeAccese[3]){
            //molto attivo
            myPrefHandler.setLivelloAttività(Formule.LivelloAttività.MOLTO_ATTIVO);
        }else if(iconeAccese[4]){
            //ext attivo
            myPrefHandler.setLivelloAttività(Formule.LivelloAttività.ESTREMAMENTE_ATTIVO);
        }

        String vitaInserita,colloInserito,fianchiInseriti, polsoInserito;
        vitaInserita = txtVita.getText().toString();
        colloInserito = txtCollo.getText().toString();
        fianchiInseriti = txtFianchi.getText().toString();
        polsoInserito = txtPolso.getText().toString();

        //salva misure
        Misure m = new Misure(dbHelper);
        m.setVita(Float.parseFloat(vitaInserita));
        m.setCollo(Float.parseFloat(colloInserito));
        m.setFianchi(Float.parseFloat(fianchiInseriti));
        m.setDate(getCurrentDate());
        m.salva();

        myPrefHandler.setCirconferenzaPolso(Float.parseFloat(polsoInserito));
        return true;
    }

    private class IconListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            ImageButton btn = (ImageButton) view;
            String name = (String) btn.getTag();
            Context c = getContext();
            Drawable d;
            int i = (int) iconeTag.get(name);
            if(iconeAccese[i]){
                d  = c.getResources().getDrawable(c.getResources().getIdentifier("ic_" + name + "_nofill", "drawable", c.getPackageName()));
            }else{
                d  = c.getResources().getDrawable(c.getResources().getIdentifier("ic_" + name + "_filled", "drawable", c.getPackageName()));
            }
            btn.setImageDrawable(d);
            iconeAccese[i] = !iconeAccese[i];
            String strLvlAtt = "";
            if(iconeAccese[i]){
                switch(name){
                    case "sedentario":
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
                        break;
                }

                txtLvlAttivita.setText(strLvlAtt);



            }


        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StartupMisureFragment.OnFragmentInteractionListener) {
            mListener = (StartupMisureFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onAvantiPressed();
    }




}