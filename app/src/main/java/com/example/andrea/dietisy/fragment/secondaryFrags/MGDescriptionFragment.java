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



public class MGDescriptionFragment extends DialogFragment {

    public interface MGDescriptionListener {
        void onYes(DialogFragment dialog);
    }

    private MGDescriptionFragment.MGDescriptionListener mListener;

    private float mMG;

    private TextView txtMG;
    private PreferencesHandler myPrefHandler;
    private ConfigValues configValues;


    private Button btnOk;


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MGDescriptionFragment newInstance(MGDescriptionFragment.MGDescriptionListener listener, float MG, ConfigValues c) {
        MGDescriptionFragment fragment = new MGDescriptionFragment();
        fragment.mListener = listener;
        fragment.mMG = MG;
        fragment.configValues = c;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mgdescription, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        myPrefHandler = PreferencesHandler.getIstance(getContext());

        impostaCtrls(v);

        return v;
    }


    private void impostaCtrls(View v) {
        btnOk = (Button) v.findViewById(R.id.btnOkMG);
        txtMG = (TextView) v.findViewById(R.id.txtMGFrag);


        float roundedMG = (float) (Math.round(mMG*100.0)/100.0);
        txtMG.setText("Percentuale di Massa Grassa: " + roundedMG + "%");


        boolean isMaschio = myPrefHandler.getSex();


        LinearLayout[] pannelli = new LinearLayout[] {
                (LinearLayout) v.findViewById(R.id.pnlMGGrassoBasso),
                (LinearLayout) v.findViewById(R.id.pnlMGFormaAtletica),
                (LinearLayout) v.findViewById(R.id.pnlMGBuonoStato),
                (LinearLayout) v.findViewById(R.id.pnlMGSopraMedia),
                (LinearLayout) v.findViewById(R.id.pnlMGObesita)
        };

        LinearLayout pnlMGAcceso = GeneralUtilities.getCorrectObjectFromValues(pannelli,mMG,configValues.limitiMGMaschio,
                                    configValues.limitiMGFemmina,isMaschio);

        pnlMGAcceso.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        TextView[] txtMG = new TextView[] {
                (TextView) v.findViewById(R.id.txtMGGrassoBasso),
                (TextView) v.findViewById(R.id.txtMGFormaAtletica),
                (TextView) v.findViewById(R.id.txtMGBuonoStato),
                (TextView) v.findViewById(R.id.txtMGSopraMedia),
                (TextView) v.findViewById(R.id.txtMGObesita)
        };

        String[] descrizioni = configValues.getDescrizioniMG(isMaschio);


        for(int i = 0; i< descrizioni.length ; i++){
            TextView txtTarget = txtMG[i];
            txtTarget.setText(descrizioni[i]);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onYes(MGDescriptionFragment.this);

            }
        });
    }


}
