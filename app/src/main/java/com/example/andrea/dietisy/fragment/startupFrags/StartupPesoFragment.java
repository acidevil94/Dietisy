package com.example.andrea.dietisy.fragment.startupFrags;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.model.Peso;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.PreferencesHandler;
import com.fxn.cue.Cue;
import com.fxn.cue.enums.Duration;
import com.fxn.cue.enums.Type;

import java.util.Calendar;
import java.util.HashMap;

import static com.example.andrea.dietisy.utility.GeneralUtilities.getCurrentDate;
import static com.example.andrea.dietisy.utility.GeneralUtilities.getMeseStringa;

public class StartupPesoFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private  PreferencesHandler myPrefHandler;
    private DBAdapter dbHelper;
    private TextView txtEtà;
    private EditText txtAltezza,txtPeso;
    private ImageButton btnAvanti;
    private DatePickerDialog datePickerDialog;
    private ImageButton btnMan,btnWoman;
    private ImageView btnInserisciDataStartup;
    private HashMap iconeTag;
    private boolean[] iconeAccese;  // in questo ordine: uomo,donna,sedentario,leggermente attivo,attivo,molto attivo



    private String dataSel = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_startup_peso, container, false);

        myPrefHandler = PreferencesHandler.getIstance(getContext());
        dbHelper = DBAdapter.getIstance(getContext());

        iconeAccese = new boolean[6];

        initCtrls(v);
        initMap();

        return v;
    }

    private void initMap(){
        iconeTag = new HashMap();
        iconeTag.put("boy",0);
        iconeTag.put("girl",1);
    }

    private void initCtrls(View v){
        IconListener icListener = new IconListener();

        btnMan = (ImageButton) v.findViewById(R.id.btnMan);
        btnMan.setTag("boy");
        btnMan.setOnClickListener(icListener);

        btnWoman = (ImageButton) v.findViewById(R.id.btnWoman);
        btnWoman.setTag("girl");
        btnWoman.setOnClickListener(icListener);


        btnAvanti = (ImageButton) v.findViewById(R.id.btnAvantiStartupPeso);
        txtAltezza = (EditText) v.findViewById(R.id.txtAltezzaStartup);
        txtEtà = (TextView) v.findViewById(R.id.txtEtàStartup);
        txtPeso = (EditText) v.findViewById(R.id.txtPesoValoreStartup);
        btnInserisciDataStartup = (ImageView) v.findViewById(R.id.btnInserisciDataStartup);

        //listener che apre il dialog per le date
        btnInserisciDataStartup.setOnClickListener(new View.OnClickListener() {
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
                                String data = dayOfMonth + " " + getMeseStringa(monthOfYear) + " " + year;
                                dataSel = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                txtEtà.setText(data);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

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

        for(int i = 0;i<6;i++){
           if(iconeAccese[i])
               selectedIcon++;
        }
        if(selectedIcon != 1){
            ris = false;
        }

        String dataNascita = txtEtà.getText().toString();
        if(dataNascita.equals("")){
            ris = false;
        }

        String altezza = txtAltezza.getText().toString();
        if(altezza.equals("")){
            ris = false;
        }

        String peso = txtPeso.getText().toString();
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

        myPrefHandler.setAltezza(Float.parseFloat(txtAltezza.getText().toString()));
        myPrefHandler.setDataNascita(dataSel);
        myPrefHandler.setSex(iconeAccese[0]);           //maschio: true, femmina: false


        String pesoInseritoS;
        pesoInseritoS = txtPeso.getText().toString();
        //salva peso
        Peso p = new Peso(dbHelper);
        p.setPeso(Float.parseFloat(pesoInseritoS));
        p.setDate(getCurrentDate());
        p.salva();

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

            if(iconeAccese[i]){
                switch(name){
                    case "boy":
                        if(iconeAccese[1]) {
                            btnWoman.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_girl_nofill", "drawable", c.getPackageName())));
                            iconeAccese[1] = false;
                        }
                        break;
                    case "girl":
                        if(iconeAccese[0]) {
                            btnMan.setImageDrawable(c.getResources().getDrawable(c.getResources().getIdentifier("ic_boy_nofill", "drawable", c.getPackageName())));
                            iconeAccese[0] = false;
                        }
                        break;
                    default:
                        break;
                }
            }


        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
