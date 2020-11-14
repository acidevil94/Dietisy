package com.example.andrea.dietisy.fragment;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.fragment.secondaryFrags.BMIDescriptionFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.DietFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.InserisciMisureFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.InserisciObiettivoFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.InserisciPesoFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.MGDescriptionFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.WHRDescriptionFragment;
import com.example.andrea.dietisy.model.Misure;
import com.example.andrea.dietisy.model.Peso;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.ConfigValues;
import com.example.andrea.dietisy.utility.Formule;
import com.example.andrea.dietisy.utility.GeneralUtilities;
import com.example.andrea.dietisy.utility.PreferencesHandler;
import com.github.lzyzsd.circleprogress.ArcProgress;


import static com.example.andrea.dietisy.utility.Formule.calcoloMassaGrassa;
import static com.example.andrea.dietisy.utility.Formule.getWHR;


public class MainFragment extends Fragment {

    private DBAdapter dbHelper;
    private PreferencesHandler pH;
    private TextView  txtPesoAttuale,txtPesoObiettivo, txtPesoIniziale,
                        txtMassaGrassa, txtColloAttuale, txtFianchiAttuale, txtVitaAttuale,
                        txtMetabolismoTotale,txtBMI, txtWHR;
    private ImageView bollinoBMI,bollinoMG,bollinoWHR;
    private LinearLayout pnlDieta, pnlMG,pnlBMI;
    private Animation animFabOpen, animFabClose, animFabRClockwise, animFabRAnticlockwise;
    private ArcProgress arcObiettivo;
    private FloatingActionButton fabPlus, fabPeso, fabMetro;
    private boolean fabPlusIsOpen = false;
    private Handler mHandler;
    private ConfigValues configVals;

    private ImageView btnObiettivo, btnWHR;


    private float BMI, MG, WHR;

    private ScrollView scrllMain;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Home");
        setHasOptionsMenu(true);

        mHandler = new Handler();

        pH = PreferencesHandler.getIstance(getContext());
        dbHelper = DBAdapter.getIstance(getContext());
        initCtrls(v);

        caricaAnimazioni(v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                auditDati();
            }
        };
            if (mRunnable != null) {
                mHandler.post(mRunnable);
            }
        }

        setListeners();

        //classe che raccoglie i limiti e i colori degli indicatori
        configVals = new ConfigValues(getContext());

        return v;
    }







    private void initCtrls(View v) {

        txtBMI = (TextView) v.findViewById(R.id.txtBMIMain);
        arcObiettivo = (ArcProgress) v.findViewById(R.id.arcObiettivoMain);
        txtPesoAttuale = (TextView) v.findViewById(R.id.txtPesoAttualeMain);
        txtPesoObiettivo = (TextView) v.findViewById(R.id.txtPesoObiettivoMain);
        txtPesoIniziale = (TextView) v.findViewById(R.id.txtPesoIniziale);
        fabMetro = (FloatingActionButton) v.findViewById(R.id.fab_ruler);
        fabPeso = (FloatingActionButton) v.findViewById(R.id.fab_peso);
        fabPlus = (FloatingActionButton) v.findViewById(R.id.fab_plus);
        txtMassaGrassa =(TextView) v.findViewById(R.id.txtMassaGrassa);
        txtMetabolismoTotale = (TextView) v.findViewById(R.id.txtMetabolismoTotale);
        txtColloAttuale = (TextView) v.findViewById(R.id.txtColloAttuale);
        txtFianchiAttuale = (TextView) v.findViewById(R.id.txtFianchiAttuale);
        txtVitaAttuale = (TextView) v.findViewById(R.id.txtVitaAttuale);
        txtWHR = (TextView) v.findViewById(R.id.txtWHRMain);
        scrllMain = (ScrollView) v.findViewById(R.id.scrllMain);
        pnlBMI = (LinearLayout) v.findViewById(R.id.pnlBMI);
        pnlDieta = (LinearLayout) v.findViewById(R.id.pnlDieta);
        pnlMG = (LinearLayout) v.findViewById(R.id.pnlMG);
        btnObiettivo = (ImageView) v.findViewById(R.id.btnObiettivoMain);
        btnWHR = (ImageView) v.findViewById(R.id.btnWHRMain);


        bollinoBMI =(ImageView) v.findViewById(R.id.bollinoBMI);
        bollinoMG =(ImageView) v.findViewById(R.id.bollinoMG);
        bollinoWHR =(ImageView) v.findViewById(R.id.bollinoWHR);

    }

    private void auditDati() {

        BMI = Formule.calcoloVecchioBMI(pH, dbHelper);
        txtBMI.setText(String.format("%.1f", BMI));

        int coloreBMI = GeneralUtilities.getCorrectObjectFromValues(configVals.coloriBMI,BMI,configVals.limitiBMI);
        createCircle(bollinoBMI,coloreBMI);

        float pesoObiettivo = pH.getPesoObiettivo();
        Peso lastPeso = Peso.getLastPeso(dbHelper);
        float primoPeso = Peso.getFirstPeso(dbHelper).getPeso();
        txtPesoIniziale.setText(primoPeso + " Kg");         //TODO: il peso iniziale si intende nell'arco del conseguimento dell'obiettivo, quando si inserisce l'obiettivo bisogna segnarsi il valore del peso in quel momento ed usarlo come peso iniziale.


        float diffIniziale = primoPeso - pesoObiettivo;
        float diffAttuale = lastPeso.getPeso() - pesoObiettivo;
        if(lastPeso.getID() != 0){
            txtPesoAttuale.setText(String.format("%.2f", lastPeso.getPeso()) + " Kg");
        }

        float percentuale ;
        if(diffAttuale <= 0 || diffIniziale < 0 )
            percentuale = 100;
        else
            percentuale= (diffIniziale - diffAttuale) * 100 / diffIniziale;
        if(percentuale < 0)
            percentuale = 0;

        arcObiettivo.setProgress((int)percentuale);
        float kgPersi = diffIniziale - diffAttuale;

        //se sono negativi è inutile
        if(kgPersi > 0)
            arcObiettivo.setBottomText(String.format("%.1f", kgPersi) + " Kg " + getResources().getString(R.string.word_lost));
        else
            arcObiettivo.setBottomText("0 Kg persi");

        if(pesoObiettivo != -1){
            txtPesoObiettivo.setText(String.format("%.1f", pesoObiettivo)+ " Kg");
        }

        txtMetabolismoTotale.setText((int)Formule.calcolaMetabolismoTotale(pH,dbHelper) + " Kcal");


        MG = calcoloMassaGrassa(pH,dbHelper);
        txtMassaGrassa.setText(String.format("%.1f", MG) + " %");
        int coloreMG = GeneralUtilities.getCorrectObjectFromValues(
                        configVals.coloriMG,MG,configVals.limitiMGMaschio,
                            configVals.limitiMGFemmina,pH.getSex());
        createCircle(bollinoMG,coloreMG);

        Misure m = Misure.getLastMisure(dbHelper);
		txtColloAttuale.setText(m.getCollo() + " cm");
        txtFianchiAttuale.setText(m.getFianchi() + " cm");
        txtVitaAttuale.setText(m.getVita() + " cm");


        WHR = getWHR(dbHelper,pH);
        txtWHR.setText(String.format("%.2f", WHR) +"");
        int coloreWHR = GeneralUtilities.getCorrectObjectFromValues(
                configVals.coloriWH,WHR * 100,configVals.limitiWHMaschio,
                configVals.limitiWHFemmina,pH.getSex());
        createCircle(bollinoWHR,coloreWHR);

    }

    private void createCircle(ImageView view, int color) {
        // Create a mutable bitmap
        Bitmap bitMap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        bitMap = bitMap.copy(bitMap.getConfig(), true);
        // Construct a canvas with the specified bitmap to draw into
        Canvas canvas = new Canvas(bitMap);
        // Create a new paint with default settings.
        Paint paint = new Paint();
        // smooths out the edges of what is being drawn
        paint.setAntiAlias(true);
        // set color
        paint.setColor(color);
        // set style
        paint.setStyle(Paint.Style.FILL);
        // set stroke
        paint.setStrokeWidth(4.5f);
        // draw circle with radius 30
        canvas.drawCircle(50, 50, 30, paint);
        // set on ImageView or any other view
        view.setImageBitmap(bitMap);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setListeners() {
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apriChiudiFABPlus(false);
            }
        });

        fabMetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apriChiudiFABPlus(false);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                DialogFragment fragment = InserisciMisureFragment.newInstance(new InserisciMisureFragment.InserisciMisureListener() {
                    @Override
                    public void onYes(DialogFragment dialog, Misure m) {
                        salvaMisure(m);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNo(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                });
                fragment.show(ft, "InserisciMisure");
            }
        });


        fabPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apriChiudiFABPlus(false);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogFragment fragment = InserisciPesoFragment.newInstance(new InserisciPesoFragment.InserisciPesoListener() {
                    @Override
                    public void onYes(DialogFragment dialog, Peso pesoInserito) {
                        salvaPeso(pesoInserito);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNo(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                });
                fragment.show(ft, "InserisciPeso");
            }
        });

        btnObiettivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogFragment fragment = InserisciObiettivoFragment.newInstance(new InserisciObiettivoFragment.InserisciObiettivoListener() {
                    @Override
                    public void onYes(DialogFragment dialog, float m) {
                        salvaObiettivo(m);
                        dialog.dismiss();
                    }
                    @Override
                    public void onNo(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                }, pH);
                fragment.show(ft, "InserisciObiettivo");
            }
        });

        //nasconde pulsante + se si scrolla verso il basso
        scrllMain.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    apriChiudiFABPlus(true);
                    fabPlus.hide();
                } else {
                    fabPlus.show();
                }
            }

        });



        pnlBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogFragment fragment = BMIDescriptionFragment.newInstance(new BMIDescriptionFragment.BMIDescriptionListener() {
                    @Override
                    public void onYes(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },BMI,configVals);
                fragment.show(ft, "BMISpiegazione");
            }
        });


        pnlMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogFragment fragment = MGDescriptionFragment.newInstance(new MGDescriptionFragment.MGDescriptionListener() {
                    @Override
                    public void onYes(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },MG,configVals);
                fragment.show(ft, "MGSpiegazione");
            }
        });

        pnlMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogFragment fragment = MGDescriptionFragment.newInstance(new MGDescriptionFragment.MGDescriptionListener() {
                    @Override
                    public void onYes(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },MG,configVals);
                fragment.show(ft, "MGSpiegazione");
            }
        });

        btnWHR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogFragment fragment = WHRDescriptionFragment.newInstance(new WHRDescriptionFragment.WHRDescriptionListener() {
                    @Override
                    public void onYes(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },WHR,configVals);
                fragment.show(ft, "WHRSpiegazione");
            }
        });

        pnlDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DietFragment fragment = DietFragment.newInstance(new DietFragment.DietFragmentListener() {
                    @Override
                    public void onYes(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },pH,dbHelper, txtMetabolismoTotale);
                fragment.show(ft, "DietSpieg");
            }
        });

    }

    private void caricaAnimazioni(View v) {
        animFabOpen = AnimationUtils.loadAnimation(v.getContext(), R.anim.fab_open);
        animFabClose = AnimationUtils.loadAnimation(v.getContext(), R.anim.fab_close);
        animFabRClockwise = AnimationUtils.loadAnimation(v.getContext(), R.anim.rotate_clockwise);
        animFabRAnticlockwise = AnimationUtils.loadAnimation(v.getContext(), R.anim.rotate_aniclockwise);
    }


    private void apriChiudiFABPlus(boolean forcedClose){

        if (fabPlusIsOpen) {
            fabPeso.startAnimation(animFabClose);
            fabMetro.startAnimation(animFabClose);
            fabPlus.startAnimation(animFabRAnticlockwise);
            fabMetro.setClickable(false);
            fabPeso.setClickable(false);
            fabPlusIsOpen = false;

        } else {
            if(!forcedClose) {
                fabPeso.startAnimation(animFabOpen);
                fabMetro.startAnimation(animFabOpen);
                fabPlus.startAnimation(animFabRClockwise);
                fabMetro.setClickable(true);
                fabPeso.setClickable(true);
                fabPlusIsOpen = true;
            }
        }
    }


    private void salvaPeso(Peso p) {
        p.setDBToken(dbHelper);
        p.salva();
        if (dbHelper.getElencoEccezioni().size() > 0)
            ;
        else {
            Snackbar.make(getActivity().findViewById(R.id.pnlFABS), getResources().getString(R.string.success_element_saved), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        auditDati();
                    }
                };
                if (mRunnable != null) {
                    mHandler.post(mRunnable);
                }
            }
        }
    }

    private void salvaMisure(Misure m) {
        m.setDBToken(dbHelper);
        m.salva();
        if (dbHelper.getElencoEccezioni().size() > 0)
            ;
        else {
            Snackbar.make(getActivity().findViewById(R.id.pnlFABS),  getResources().getString(R.string.success_element_saved), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        auditDati();
                    }
                };
                if (mRunnable != null) {
                    mHandler.post(mRunnable);
                }

            }
        }

    }

    private void salvaObiettivo(float m) {

        pH.setPesoObiettivo(m);

            Snackbar.make(getActivity().findViewById(R.id.pnlFABS),  getResources().getString(R.string.success_element_saved), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        auditDati();
                    }
                };
                if (mRunnable != null) {
                    mHandler.post(mRunnable);
                }

            }
    }


    @Override
    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    auditDati();
                }
            };
            if (mRunnable != null) {
                mHandler.post(mRunnable);
            }

        }
    }


}
