package com.example.andrea.dietisy.fragment.diaryFrags;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.andrea.dietisy.fragment.secondaryFrags.InserisciMisureFragment;
import com.example.andrea.dietisy.fragment.secondaryFrags.MsgBoxFragment;
import com.example.andrea.dietisy.model.Misure;
import com.example.andrea.dietisy.model.DBAdapter;
import com.example.andrea.dietisy.utility.GeneralUtilities;

import java.util.List;

import static com.example.andrea.dietisy.utility.GeneralUtilities.getMeseStringa;
import static com.example.andrea.dietisy.utility.GeneralUtilities.stringa2Data;

public class DiarioMisureFragment extends Fragment {

    DBAdapter dbHelper;
    ListView lstDiario;
    View parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_diario_misure, container, false);
        dbHelper = DBAdapter.getIstance(getContext());
        initCtrls();
        return parent;
    }

    private void initCtrls(){
        lstDiario = (ListView) parent.findViewById(R.id.lstDiarioMisure);
        riempiTabella(lstDiario);
    }

    private void riempiTabella(ListView lv){
        List list = Misure.getMisure(dbHelper,"","");   //TODO filtrare per data
        DiarioMisureFragment.CustomMisureAdapter adapter = new DiarioMisureFragment.CustomMisureAdapter(getActivity(), R.layout.lst_diario_misure_row_item, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Misure m = (Misure) parent.getItemAtPosition(position);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                DialogFragment fragment = InserisciMisureFragment.newInstance(new InserisciMisureFragment.InserisciMisureListener() {
                    @Override
                    public void onYes(DialogFragment dialog, Misure m) {

                        m.setDBToken(dbHelper);
                        m.salva();
                        dialog.dismiss();
                        DiarioMisureFragment.this.initCtrls();

                        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "Misure Aggiornate", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                    @Override
                    public void onNo(DialogFragment dialog) {
                        dialog.dismiss();
                    }
                },m);

                fragment.show(ft, "InserisciMisure");
            }
        });

    }



    public class CustomMisureAdapter extends ArrayAdapter<Misure> {

        public CustomMisureAdapter(Context context, int textViewResourceId, List<Misure> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getViewOptimize(position, convertView, parent);
        }

        public View getViewOptimize(int position, View convertView, ViewGroup parent) {


            DiarioMisureFragment.CustomMisureAdapter.ViewHolder viewHolder = null;
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                convertView = inflater.inflate(R.layout.lst_diario_misure_row_item, null);

                viewHolder = new DiarioMisureFragment.CustomMisureAdapter.ViewHolder();
                viewHolder.collo = (TextView)convertView.findViewById(R.id.textViewCollo);
                viewHolder.vita = (TextView)convertView.findViewById(R.id.textViewVita);
                viewHolder.fianchi = (TextView)convertView.findViewById(R.id.textViewFianchi);
                viewHolder.data = (TextView)convertView.findViewById(R.id.textViewData);
                viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDeleteListItem);
                viewHolder.imgTrendingCollo = (ImageView) convertView.findViewById(R.id.imgTrendingCollo);
                viewHolder.imgTrendingFianchi = (ImageView) convertView.findViewById(R.id.imgTrendingFianchi);
                viewHolder.imgTrendingVita = (ImageView) convertView.findViewById(R.id.imgTrendingVita);
                viewHolder.differenzaCollo = (TextView)convertView.findViewById(R.id.txtDifferenzaCollo);
                viewHolder.differenzaVita = (TextView)convertView.findViewById(R.id.txtDifferenzaVita);
                viewHolder.differenzaFianchi = (TextView)convertView.findViewById(R.id.txtDifferenzaFianchi);

                convertView.setTag(viewHolder);

            Misure m = getItem(position);

            viewHolder.collo.setText(String.format("%.2f", m.getCollo()) + " cm");
            viewHolder.fianchi.setText(String.format("%.2f", m.getFianchi()) + " cm");
            viewHolder.vita.setText(String.format("%.2f", m.getVita()) + " cm");

            GeneralUtilities.MyData myData = stringa2Data(m.getDate());
            //si formatta la data in formato leggibile, es: 13 Gennaio 1998
            String formattedData = myData.giorno + " " + getMeseStringa(myData.mese - 1) + " " + myData.anno;
            viewHolder.data.setText(formattedData);

            viewHolder.btnDelete.setOnClickListener(new DiarioMisureFragment.CustomMisureAdapter.DeleteListener(m));


            //cambio imageview trending
            if(position == 0){
                //primo elemento
                viewHolder.imgTrendingCollo.setVisibility(View.INVISIBLE);
                viewHolder.differenzaCollo.setVisibility(View.INVISIBLE);

                viewHolder.imgTrendingVita.setVisibility(View.INVISIBLE);
                viewHolder.differenzaVita.setVisibility(View.INVISIBLE);

                viewHolder.imgTrendingFianchi.setVisibility(View.INVISIBLE);
                viewHolder.differenzaFianchi.setVisibility(View.INVISIBLE);
            }else{
                Misure precedente = getItem(position-1);

                float mCollo = m.getCollo();
                float pCollo = precedente.getCollo();

                float mVita = m.getVita();
                float pVita = precedente.getVita();

                float mFianchi = m.getFianchi();
                float pFianchi = precedente.getFianchi();



                //COLLo
                if(mCollo < pCollo){

                    float differenzaCollo = Math.abs(mCollo - pCollo);


                    viewHolder.differenzaCollo.setText("-" +  String.format("%.2f", differenzaCollo)  + " cm");

                    viewHolder.differenzaCollo.setTextColor(getResources().getColor(R.color.color_verde));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrendingCollo.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrendingCollo.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp));
                    }

                }else if(mCollo == pCollo){

                    viewHolder.differenzaCollo.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrendingCollo.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrendingCollo.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp));
                    }

                }
                else{
                    //mPeso > pPeso

                    float differenzaCollo = Math.abs(mCollo - pCollo);

                    viewHolder.differenzaCollo.setText("+" +  String.format("%.2f", differenzaCollo)  + " cm");

                    viewHolder.differenzaCollo.setTextColor(getResources().getColor(R.color.color_rosso));


                }




                //VITA
                if(mVita < pVita){

                    float differenzaVita = Math.abs(mVita - pVita);

                    viewHolder.differenzaVita.setText("-" +  String.format("%.2f", differenzaVita)  + " cm");

                    viewHolder.differenzaVita.setTextColor(getResources().getColor(R.color.color_verde));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrendingVita.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrendingVita.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp));
                    }

                }else if(mVita == pVita){

                    viewHolder.differenzaVita.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrendingVita.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrendingVita.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp));
                    }

                }
                else{
                    //mPeso > pPeso

                    float differenzaVita = Math.abs(mVita - pVita);

                    viewHolder.differenzaVita.setText("+" + String.format("%.2f", differenzaVita)  + " cm");

                    viewHolder.differenzaVita.setTextColor(getResources().getColor(R.color.color_rosso));


                }



                //FIANCHI

                if(mFianchi < pFianchi){

                    float differenzaFianchi = Math.abs(mFianchi - pFianchi);

                    viewHolder.differenzaFianchi.setText("-" + String.format("%.2f", differenzaFianchi)  + " cm");

                    viewHolder.differenzaFianchi.setTextColor(getResources().getColor(R.color.color_verde));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrendingFianchi.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrendingFianchi.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_red_24dp));
                    }

                }else if(mFianchi == pFianchi){

                    viewHolder.differenzaFianchi.setVisibility(View.INVISIBLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.imgTrendingFianchi.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp, getContext().getTheme()));
                    } else {
                        viewHolder.imgTrendingFianchi.setImageDrawable(getResources().getDrawable(R.drawable.ic_drag_handle_blue_24dp));
                    }

                }
                else{
                    //mPeso > pPeso

                    float differenzaFianchi = Math.abs(mFianchi - pFianchi);

                    viewHolder.differenzaFianchi.setText("+" + String.format("%.2f", differenzaFianchi)  + " cm");

                    viewHolder.differenzaFianchi.setTextColor(getResources().getColor(R.color.color_rosso));


                }
            }

            return convertView;
        }

        private class DeleteListener implements View.OnClickListener,MsgBoxFragment.MsgBoxListener {

            private Misure misure;

            public DeleteListener(Misure m){
                misure = m;
            }

            @Override
            public void onClick(View v) {


                DialogFragment fragment = MsgBoxFragment.newInstance(this,true,"Sicuro di voler cancellare questo elemento?");
                fragment.show(getFragmentManager(), "Attenzione!");


            }


            private void cancellaElemento(){
                misure.delete();
            }

            @Override
            public void onYes(DialogFragment dialog) {
                cancellaElemento();
                dialog.dismiss();

                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), getResources().getString(R.string.success_element_deleted), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                DiarioMisureFragment.this.initCtrls();
            }

            @Override
            public void onNo(DialogFragment dialog) {
                dialog.dismiss();
            }
        }

        private class ViewHolder {
            public TextView collo;
            public TextView vita;
            public TextView fianchi;
            public TextView data;
            public ImageButton btnDelete;
            public ImageView imgTrendingCollo;
            public ImageView imgTrendingVita;
            public ImageView imgTrendingFianchi;
            public TextView differenzaCollo;
            public TextView differenzaFianchi;
            public TextView differenzaVita;
        }


    }



}
