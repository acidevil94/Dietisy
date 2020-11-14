package com.example.andrea.dietisy.fragment.secondaryFrags;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.utility.ConfigValues;
import com.example.andrea.dietisy.utility.GeneralUtilities;
import com.example.andrea.dietisy.utility.PreferencesHandler;



public class WHRDescriptionFragment extends DialogFragment {

    public interface WHRDescriptionListener {
        void onYes(DialogFragment dialog);
    }

    private WHRDescriptionFragment.WHRDescriptionListener mListener;

    private float mWHR;

    private TextView txtWHR;
    private PreferencesHandler myPrefHandler;
    private ConfigValues configValues;


    private Button btnOk;


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static WHRDescriptionFragment newInstance(WHRDescriptionFragment.WHRDescriptionListener listener, float WHR, ConfigValues c) {
        WHRDescriptionFragment fragment = new WHRDescriptionFragment();
        fragment.mListener = listener;
        fragment.mWHR = WHR;
        fragment.configValues = c;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_whrdescription, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        myPrefHandler = PreferencesHandler.getIstance(getContext());

        impostaCtrls(v);

        return v;
    }


    private void impostaCtrls(View v) {
        btnOk = (Button) v.findViewById(R.id.btnOkWHR);
        txtWHR = (TextView) v.findViewById(R.id.txtWHRFrag);


        txtWHR.setText("Rapporto vita-altezza: " + String.format("%.2f", mWHR) );


        boolean isMaschio = myPrefHandler.getSex();


        LinearLayout[] pannelli = new LinearLayout[] {
                (LinearLayout) v.findViewById(R.id.pnlWHRMagroGrave),
                (LinearLayout) v.findViewById(R.id.pnlWHRMagro),
                (LinearLayout) v.findViewById(R.id.pnlWHRSalute),
                (LinearLayout) v.findViewById(R.id.pnlWHRSovrappeso),
                (LinearLayout) v.findViewById(R.id.pnlWHRSerioSovrappeso),
                (LinearLayout) v.findViewById(R.id.pnlWHRObesita)
        };

        LinearLayout pnlWHRAcceso = GeneralUtilities.getCorrectObjectFromValues(pannelli,mWHR * 100,configValues.limitiWHMaschio,
                configValues.limitiWHFemmina,isMaschio);

        pnlWHRAcceso.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        TextView[] txtWH = new TextView[] {
                (TextView) v.findViewById(R.id.txtWHRMagroGrave),
                (TextView) v.findViewById(R.id.txtWHRMagro),
                (TextView) v.findViewById(R.id.txtWHRSalute),
                (TextView) v.findViewById(R.id.txtWHRSovrappeso),
                (TextView) v.findViewById(R.id.txtWHRSerioSovrappeso),
                (TextView) v.findViewById(R.id.txtWHRObesita)
        };

        String[] descrizioni = configValues.getDescrizioniWH(isMaschio);


        for(int i = 0; i< descrizioni.length ; i++){
            TextView txtTarget = txtWH[i];
            txtTarget.setText(descrizioni[i]);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onYes(WHRDescriptionFragment.this);

            }
        });
    }


}
