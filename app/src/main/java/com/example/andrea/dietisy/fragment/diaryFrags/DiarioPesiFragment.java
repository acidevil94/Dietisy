package com.example.andrea.dietisy.fragment.diaryFrags;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.fragment.secondaryFrags.InserisciPesoFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.MsgBoxFragment;
import com.example.andrea.dietisy.model.Peso;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.GeneralUtilities;


import java.util.List;

import static com.example.andrea.dietisy.utility.GeneralUtilities.getMeseStringa;
import static com.example.andrea.dietisy.utility.GeneralUtilities.stringa2Data;

public class DiarioPesiFragment extends Fragment {


    private static final String TAG = "DiarioPesiFragment";


    DBAdapter dbHelper;


    ListView lstDiario;

    View parent;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_diario_pesi, container, false);


        dbHelper = DBAdapter.getIstance(getContext());

        initCtrls();

        return parent;
    }


    private void initCtrls(){
        lstDiario = (ListView) parent.findViewById(R.id.lstDiarioPesi);

        riempiTabella(lstDiario);
    }

    private void riempiTabella(ListView lv){
        List list = Peso.getPesi(dbHelper,"","");   //TODO filtrare per data
        CustomPesiAdapter adapter = new CustomPesiAdapter(getActivity(), R.layout.lst_diario_pesi_row_item, list);

        lv.setAdapter(adapter);

        //al click mostrare dialog per modificare il peso
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Peso p = (Peso) parent.getItemAtPosition(position);


                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                DialogFragment fragment = InserisciPesoFragment.newInstance(new InserisciPesoFragment.InserisciPesoListener() {
                    @Override
                    public void onYes(DialogFragment dialog, Peso p) {


                        p.setDBToken(dbHelper);
                        p.salva();



                        dialog.dismiss();


                        DiarioPesiFragment.this.initCtrls();

                        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "Peso Aggiornato", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();


                    }

                    @Override
                    public void onNo(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },p);

                fragment.show(ft, "InserisciPeso");
            }
        });

    }



    public class CustomPesiAdapter extends ArrayAdapter<Peso>{



        public CustomPesiAdapter(Context context, int textViewResourceId,
                                 List<Peso> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getViewOptimize(position, convertView, parent);

        }

        public View getViewOptimize(int position, View convertView, ViewGroup parent) {


            //Log.e(TAG,convertView + " ");

            ViewHolder viewHolder = null;
          //  if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lst_diario_pesi_row_item, null);
                viewHolder = new ViewHolder();
                viewHolder.peso = (TextView)convertView.findViewById(R.id.textViewName);
                viewHolder.data = (TextView)convertView.findViewById(R.id.textViewNumber);
                viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDeleteListItem);
                viewHolder.imgTrending = (ImageView) convertView.findViewById(R.id.imgTrending);
                viewHolder.differenza = (TextView)convertView.findViewById(R.id.txtDifferenzaPesi);

                convertView.setTag(viewHolder);
            Peso p = getItem(position);
            viewHolder.peso.setText(String.format("%.2f", p.getPeso())+ " Kg");

            GeneralUtilities.MyData myData = stringa2Data(p.getDate());
            //si formatta la data in formato leggibile, es: 13 Gennaio 1998
            String formattedData = myData.giorno + " " + getMeseStringa(myData.mese - 1) + " " + myData.anno;

            viewHolder.data.setText(formattedData);
            viewHolder.btnDelete.setOnClickListener(new DeleteListener(p));


            //cambio imageview trending
            if(position == 0){
                //primo elemento
                viewHolder.imgTrending.setVisibility(View.INVISIBLE);
                viewHolder.differenza.setVisibility(View.INVISIBLE);

            }else{
                Peso precedente = getItem(position-1);
                float mPeso = p.getPeso();
                float pPeso = precedente.getPeso();






                if(mPeso < pPeso){

                    float differenzaPesi = Math.abs(mPeso - pPeso);

                    viewHolder.differenza.setText("-" + String.format("%.2f", differenzaPesi)  + " Kg");

                    viewHolder.differenza.setTextColor(getResources().getColor(R.color.color_verde));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrending.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrending.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp));
                    }

                    Log.e(TAG,"Non prima posizione, mio valore:" +mPeso + " , valore precedente:" + pPeso + " ho messo freccia in basso");

                }else if(mPeso == pPeso){

                    viewHolder.differenza.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrending.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrending.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp));
                    }


          //          Log.e(TAG,"Non prima posizione, mio valore:" +mPeso + " , valore precedente:" + pPeso + "  ho messo uguale");

                }
                else{
                    //mPeso > pPeso

                    float differenzaPesi = Math.abs(mPeso - pPeso);

                    viewHolder.differenza.setText("+" + String.format("%.2f", differenzaPesi)  + " Kg");

                    viewHolder.differenza.setTextColor(getResources().getColor(R.color.color_rosso));


              //      Log.e(TAG,"Non prima posizione, mio valore:" +mPeso + " , valore precedente:" + pPeso + " ho messo freccia in su");


                }
            }

            return convertView;
        }

        private class DeleteListener implements View.OnClickListener,MsgBoxFragment.MsgBoxListener{

            private Peso peso;

            public DeleteListener(Peso p){
                peso = p;
            }

            @Override
            public void onClick(View v) {
                DialogFragment fragment = MsgBoxFragment.newInstance(this,true,"Sicuro di voler cancellare questo elemento?");
                fragment.show(getFragmentManager(), "Attenzione!");
            }

            private void cancellaElemento(){
                peso.delete();
            }

            @Override
            public void onYes(DialogFragment dialog) {
                cancellaElemento();
                dialog.dismiss();

                DiarioPesiFragment.this.initCtrls();

                Snackbar.make(getActivity().findViewById(R.id.drawer_layout),  getResources().getString(R.string.success_element_deleted), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onNo(DialogFragment dialog) {
                dialog.dismiss();
            }
        }

        private class ViewHolder {
            public TextView peso;
            public TextView data;
            public ImageButton btnDelete;
            public ImageView imgTrending;
            public TextView differenza;
        }


    }



}
