package com.example.andrea.dietisy.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.utility.Formule;
import com.example.andrea.dietisy.utility.PreferencesHandler;

import java.util.Calendar;

public class SettingFragment extends Fragment {
    private PreferencesHandler myPrefHandler;


    private Button btnSalva;
    private RadioButton rdbtnMaschio;
    private RadioButton rdbtnFemmina;
    private EditText txtEtà;
    private EditText txtAltezza;
    private EditText txtCirconferenzaPolso;
    private EditText txtNome;


    private Spinner cmbLivelloAttivita;

    private DatePickerDialog datePickerDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        getActivity().setTitle("Impostazioni");
        setHasOptionsMenu(true);
        myPrefHandler = PreferencesHandler.getIstance(getContext());

        

        initCtrls(v);


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.impostazioni_fragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actSaveImpostazioni:
                salvaPreferenze();
                return true;
            default:
                return false;
        }
    }

    public void initCtrls(View v){
        txtAltezza = (EditText) v.findViewById(R.id.txtAltezzaSetting);
        txtCirconferenzaPolso = (EditText) v.findViewById(R.id.txtPolsoSetting);
        txtEtà = (EditText) v.findViewById(R.id.txtDataSetting);
        txtNome = (EditText) v.findViewById(R.id.txtNomeSetting);

        rdbtnMaschio = (RadioButton) v.findViewById(R.id.rdbtnMaschioSetting);
        rdbtnMaschio.setChecked(myPrefHandler.getSex());
        rdbtnFemmina = (RadioButton) v.findViewById(R.id.rdbtnFemminaSetting);
        rdbtnFemmina.setChecked(!myPrefHandler.getSex());
       /* btnSalva = (Button) v.findViewById(R.id.btnSave2);
        btnSalva.setOnClickListener(new ClickListener());*/

        String dataNascita = myPrefHandler.getDataNascita();
        if(!dataNascita.equals(""))
            txtEtà.setText(dataNascita);

        String nome = myPrefHandler.getNome();
        if(!nome.equals(""))
            txtNome.setText(nome);

        float altezza = myPrefHandler.getAltezza();
        if(altezza != 0)
            txtAltezza.setText(String.valueOf(altezza));

        float polso = myPrefHandler.getCirconferenzaPolso();
        if(polso != 0)
            txtCirconferenzaPolso.setText(String.valueOf(polso));

        txtEtà.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                txtEtà.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        cmbLivelloAttivita = (Spinner) v.findViewById(R.id.cmbLivelloAttivitaSetting);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.livelli_attivita, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        cmbLivelloAttivita.setAdapter(adapter);

        Formule.LivelloAttività la = myPrefHandler.getLivelloAttività();

        cmbLivelloAttivita.setSelection(la.ordinal());

    }


    private boolean ckSalvaPreferenze(){
        String dataNascita = txtEtà.getText().toString();
        if(dataNascita.equals("")){
            txtEtà.setError("Inserire una data valida");
            return false;
        }

        String altezza = txtAltezza.getText().toString();
        if(altezza.equals("")){
            txtAltezza.setError("Inserire un valore valido");
            return false;
        }

        String polso = txtCirconferenzaPolso.getText().toString();
        if(polso.equals("")){
            txtCirconferenzaPolso.setError("Inserire un valore valido");
            return false;
        }

        String nome = txtNome.getText().toString();
        if(nome.equals("")){
            txtNome.setError("Inserire un valore valido");
            return false;
        }

        return true;
    }


    private void salvaPreferenze(){
        if(!ckSalvaPreferenze())
            return;
        myPrefHandler.setAltezza(Float.parseFloat(txtAltezza.getText().toString()));
        myPrefHandler.setCirconferenzaPolso(Float.parseFloat(txtCirconferenzaPolso.getText().toString()));
        myPrefHandler.setDataNascita(txtEtà.getText().toString());
        myPrefHandler.setSex(rdbtnMaschio.isChecked());
        myPrefHandler.setNome(txtNome.getText().toString());

        int position = cmbLivelloAttivita.getSelectedItemPosition();

        Formule.LivelloAttività la = Formule.LivelloAttività.values()[position];

        myPrefHandler.setLivelloAttività(la);

        Snackbar.make( getActivity().findViewById(R.id.drawer_layout), "Impostazioni Salvate" , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }



    private class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            salvaPreferenze();
        }
    }





}
