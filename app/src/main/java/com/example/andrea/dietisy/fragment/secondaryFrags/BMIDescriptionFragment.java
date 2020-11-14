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


import static com.example.andrea.dietisy.utility.GeneralUtilities.isBetween;


public class BMIDescriptionFragment extends DialogFragment {

    public interface BMIDescriptionListener {
        void onYes(DialogFragment dialog);
    }

    private BMIDescriptionFragment.BMIDescriptionListener mListener;

    private float mBMI;

    private TextView txtBMI;
    private ConfigValues configValues;



    private Button btnOk;


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static BMIDescriptionFragment newInstance(BMIDescriptionFragment.BMIDescriptionListener listener, float BMI, ConfigValues configValues) {
        BMIDescriptionFragment fragment = new BMIDescriptionFragment();
        fragment.mListener = listener;
        fragment.mBMI = BMI;
        fragment.configValues = configValues;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bmidescription, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);



        impostaCtrls(v);

        return v;
    }


    private void impostaCtrls(View v) {
        btnOk = (Button) v.findViewById(R.id.btnOkBMI);
        txtBMI = (TextView) v.findViewById(R.id.txtBMIFrag);


        float roundedBMI = (float) (Math.round(mBMI*100.0)/100.0);
        txtBMI.setText("Il tuo BMI: " + roundedBMI);

        LinearLayout[] pannelli = new LinearLayout[] {
                (LinearLayout) v.findViewById(R.id.pnlBMISottopesoGrave),
                (LinearLayout) v.findViewById(R.id.pnlBMISottopesoLieve),
                (LinearLayout) v.findViewById(R.id.pnlBMINormopeso),
                (LinearLayout) v.findViewById(R.id.pnlBMISovrappeso),
                (LinearLayout) v.findViewById(R.id.pnlBMIObeso1),
                (LinearLayout) v.findViewById(R.id.pnlBMIObeso2),
                (LinearLayout) v.findViewById(R.id.pnlBMIObeso3)
        };

        LinearLayout pnlBMIAcceso = GeneralUtilities.getCorrectObjectFromValues(pannelli,roundedBMI,configValues.limitiBMI);


        pnlBMIAcceso.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onYes(BMIDescriptionFragment.this);

            }
        });
    }


}
