package com.example.andrea.dietisy.fragment.startupFrags;

import android.content.Context;
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
import com.example.andrea.dietisy.model.Peso;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.Formule;
import com.example.andrea.dietisy.utility.PreferencesHandler;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;


public class StartupObiettivoFragment extends Fragment {

    private StartupObiettivoFragment.OnFragmentInteractionListener mListener;
    private PreferencesHandler myPrefHandler;
    private DBAdapter dbHelper;
    private TextView txtPesoIdeale;
    private EditText txtPesoObiettivo;
    private ImageButton btnAvanti;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_startup_obiettivo, container, false);

        myPrefHandler = PreferencesHandler.getIstance(getContext());
        dbHelper = DBAdapter.getIstance(getContext());


        initCtrls(v);

        return v;
    }



    private void initCtrls(View v){


        btnAvanti = (ImageButton) v.findViewById(R.id.btnAvantiStartupObiettivo);
        txtPesoObiettivo = (EditText) v.findViewById(R.id.txtObiettivoStartup);
        txtPesoIdeale = (TextView) v.findViewById(R.id.txtPesoIdealeStartup);

        float pesoIdeale = Formule.calcoloPesoIdeale(myPrefHandler);
        if(pesoIdeale != -1){
            String toSetPesoForma = String.format("%.1f", pesoIdeale) + " Kg";
            txtPesoIdeale.setText(toSetPesoForma);
        }


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

        String peso = txtPesoObiettivo.getText().toString();
        if(peso.equals("")){
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

        myPrefHandler.setPesoObiettivo(Float.parseFloat(txtPesoObiettivo.getText().toString()));

        Peso p = Peso.getLastPeso(dbHelper);
        float peso = p.getPeso();
        myPrefHandler.setPesoIniziale(peso);


        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StartupObiettivoFragment.OnFragmentInteractionListener) {
            mListener = (StartupObiettivoFragment.OnFragmentInteractionListener) context;
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
