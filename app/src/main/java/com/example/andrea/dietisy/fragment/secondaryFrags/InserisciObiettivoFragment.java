package com.example.andrea.dietisy.fragment.secondaryFrags;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.utility.Formule;
import com.example.andrea.dietisy.utility.PreferencesHandler;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;

public class InserisciObiettivoFragment extends DialogFragment {

    public interface InserisciObiettivoListener {
        public void onYes(DialogFragment dialog, float p);

        public void onNo(DialogFragment dialog);
    }

    private InserisciObiettivoFragment.InserisciObiettivoListener mListener;
    private float mPrecedente;
    private PreferencesHandler myPrefHandler;


    private EditText txtInserisciObiettivoValore;

    private TextView txtPesoIdeale;


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static InserisciObiettivoFragment newInstance(InserisciObiettivoFragment.InserisciObiettivoListener listener, float obiettivo, PreferencesHandler myPrefHandler) {
        InserisciObiettivoFragment fragment = new InserisciObiettivoFragment();
        fragment.mListener = listener;
        fragment.mPrecedente = obiettivo;
        fragment.myPrefHandler = myPrefHandler;
        return fragment;
    }

    public static InserisciObiettivoFragment newInstance(InserisciObiettivoFragment.InserisciObiettivoListener listener, PreferencesHandler myPrefHandler) {
        InserisciObiettivoFragment fragment = new InserisciObiettivoFragment();
        fragment.mListener = listener;
        fragment.mPrecedente = 0;
        fragment.myPrefHandler = myPrefHandler;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inserisci_obiettivo, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);


        impostaCtrls(v);

        return v;
    }


    private void impostaCtrls(View v) {
        txtInserisciObiettivoValore = (EditText) v.findViewById(R.id.txtObiettivoValore);
        Button btnSalva = (Button) v.findViewById(R.id.btnInserisciObiettivoSalva);
        Button btnClose = (Button) v.findViewById(R.id.btnInserisciObiettivoAnnulla);
        txtPesoIdeale = (TextView) v.findViewById(R.id.txtPesoIdealeObiettivo);

        if(mPrecedente != 0){
            txtInserisciObiettivoValore.setText(String.valueOf(mPrecedente));
        }

        float pesoIdeale = Formule.calcoloPesoIdeale(myPrefHandler);
        if(pesoIdeale != -1){
            String toSetPesoForma = String.format("%.2f", pesoIdeale) +  " Kg";
            txtPesoIdeale.setText(toSetPesoForma);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNo(InserisciObiettivoFragment.this);
                }
            }
        });


        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToParentFrag();

            }
        });
    }


    private void returnToParentFrag() {

        String pesoInseritoS = txtInserisciObiettivoValore.getText().toString();

        if (!pesoInseritoS.equals("")) {
            if (mListener != null) {
                mPrecedente = Float.parseFloat(txtInserisciObiettivoValore.getText().toString());
                mListener.onYes(InserisciObiettivoFragment.this, mPrecedente);
            }
        }
        else {
            Cue.init()
                    .with(getContext())
                    .setMessage(getResources().getString(R.string.error_msg_form_not_filled))
                    .setGravity(Gravity.CENTER | Gravity.BOTTOM)
                    .setType(Type.DANGER)
                    .setDuration(Duration.LONG)
                    .show();
        }


    }
}
