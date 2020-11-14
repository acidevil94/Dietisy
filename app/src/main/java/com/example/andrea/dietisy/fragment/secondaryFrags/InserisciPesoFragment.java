package com.example.andrea.dietisy.fragment.secondaryFrags;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.model.Peso;
import com.example.andrea.dietisy.utility.GeneralUtilities;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;

import static com.example.andrea.dietisy.utility.GeneralUtilities.getMeseStringa;
import static com.example.andrea.dietisy.utility.GeneralUtilities.stringa2Data;

/**
 * Created by andrea on 27/01/2017.
 */

public class InserisciPesoFragment extends DialogFragment {


    public interface InserisciPesoListener {
        public void onYes(DialogFragment dialog, Peso p);

        public void onNo(DialogFragment dialog);
    }

    private InserisciPesoListener mListener;
    private Peso mPrecedente;


    private  DatePickerDialog datePickerDialog;

    private EditText txtInserisciPesoValore;
    private ImageView btnData;

    private TextView txtData;

    private String dataSel = "";

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static InserisciPesoFragment newInstance(InserisciPesoListener listener,Peso p) {
        InserisciPesoFragment fragment = new InserisciPesoFragment();
        fragment.mListener = listener;
        fragment.mPrecedente = p;
        return fragment;
    }

    public static InserisciPesoFragment newInstance(InserisciPesoListener listener) {
        InserisciPesoFragment fragment = new InserisciPesoFragment();
        fragment.mListener = listener;
        fragment.mPrecedente = new Peso();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inserisci_peso, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        impostaCtrls(v);

        return v;
    }


    private void impostaCtrls(View v) {
        txtInserisciPesoValore = (EditText) v.findViewById(R.id.txtPesoValore);
        txtData = (TextView) v.findViewById(R.id.txtDataPeso);
        btnData = (ImageView) v.findViewById(R.id.btnInserisciDataPeso);
        Button btnSalva = (Button) v.findViewById(R.id.btnInserisciPesoSalva);
        Button btnClose = (Button) v.findViewById(R.id.btnInserisciPesoAnnulla);

        if(mPrecedente.getID() == 0){
            dataSel = GeneralUtilities.getCurrentDate();
        }
        else{
            dataSel =  mPrecedente.getDate();
            txtInserisciPesoValore.setText(String.valueOf(mPrecedente.getPeso()));
        }

        //la data è in formato yyyy/mm/dd, viene convertita dal metodo e restituita come oggetto
        GeneralUtilities.MyData myData = stringa2Data(dataSel);
        //si formatta la data in formato leggibile, es: 13 Gennaio 1998
        String formattedData = myData.giorno + " " + getMeseStringa(myData.mese - 1) + " " + myData.anno;
        txtData.setText(formattedData);



        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtilities.MyData dataRecord = stringa2Data(dataSel);
                datePickerDialog = new DatePickerDialog( getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dataSel = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                String formattedData = dayOfMonth + " " + getMeseStringa(monthOfYear) + " " + year;
                                txtData.setText(formattedData);
                            }
                        }, dataRecord.anno, dataRecord.mese - 1, dataRecord.giorno);
                datePickerDialog.show();
            }
        });



        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNo(InserisciPesoFragment.this);
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

        String pesoInseritoS = txtInserisciPesoValore.getText().toString();

        if (!pesoInseritoS.equals("") && !dataSel.equals("")) {
            if (mListener != null) {
                mPrecedente.setPeso(Float.parseFloat(pesoInseritoS));
                mPrecedente.setDate(dataSel);
                mListener.onYes(InserisciPesoFragment.this, mPrecedente);
            }
        } else {
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


