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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.activity.PrincipaleActivity;
import com.example.andrea.dietisy.model.Misure;
import com.example.andrea.dietisy.utility.GeneralUtilities;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;

import static com.example.andrea.dietisy.utility.GeneralUtilities.getMeseStringa;
import static com.example.andrea.dietisy.utility.GeneralUtilities.stringa2Data;

/**
 * Created by andrea on 30/01/2017.
 */

public class InserisciMisureFragment extends DialogFragment {


    public interface InserisciMisureListener {
        public void onYes(DialogFragment dialog, Misure m);

        public void onNo(DialogFragment dialog);
    }

    private InserisciMisureFragment.InserisciMisureListener mListener;


    private EditText txtColloValore;
    private EditText txtFianchiValore;
    private EditText txtVitaValore;
    private TextView txtData;

    private  DatePickerDialog datePickerDialog;
    private ImageView btnData;
    private Misure mPrecedente;

    private String dataSel;


    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static InserisciMisureFragment newInstance(InserisciMisureListener listener,Misure m) {
        InserisciMisureFragment fragment = new InserisciMisureFragment();
        fragment.mListener = listener;
        fragment.mPrecedente = m;
        return fragment;
    }

    public static InserisciMisureFragment newInstance(InserisciMisureListener listener) {
        InserisciMisureFragment fragment = new InserisciMisureFragment();
        fragment.mListener = listener;
        fragment.mPrecedente = new Misure();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inserisci_misure, container, true);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        impostaCtrls(v);
        return v;
    }



    private void impostaCtrls(View v) {
        txtColloValore = (EditText) v.findViewById(R.id.txtColloValore);
        txtVitaValore = (EditText) v.findViewById(R.id.txtVitaValore);
        txtFianchiValore = (EditText) v.findViewById(R.id.txtFianchiValore);
        txtData = (TextView) v.findViewById(R.id.txtDataMisure);
        btnData = (ImageView) v.findViewById(R.id.btnInserisciDataMisure);

        if(mPrecedente.getID() == 0){
            dataSel = GeneralUtilities.getCurrentDate();
        }
        else{
            dataSel =  mPrecedente.getDate();
            txtColloValore.setText(String.valueOf(mPrecedente.getCollo()));
            txtVitaValore.setText(String.valueOf(mPrecedente.getVita()));
            txtFianchiValore.setText(String.valueOf(mPrecedente.getFianchi()));
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


        Button btnSalva = (Button) v.findViewById(R.id.btnInserisciMisureSalva);
        Button btnClose = (Button) v.findViewById(R.id.btnInserisciMisureAnnulla);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNo(InserisciMisureFragment.this);
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

        String vitaInserita = txtVitaValore.getText().toString();
        String colloInserito = txtColloValore.getText().toString();
        String fianchiInseriti = txtFianchiValore.getText().toString();
        if (!vitaInserita.equals("") && !colloInserito.equals("") && !fianchiInseriti.equals("") && ! dataSel.equals("")) {
            if (mListener != null) {
                mPrecedente.setFianchi(Float.parseFloat(fianchiInseriti));
                mPrecedente.setCollo(Float.parseFloat(colloInserito));
                mPrecedente.setVita(Float.parseFloat(vitaInserita));
                mPrecedente.setDate(dataSel);
                mListener.onYes(InserisciMisureFragment.this, mPrecedente);
            }
        }else {
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