package com.example.andrea.dietisy.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.fragment.diaryFrags.DiarioMisureFragment;
import com.example.andrea.dietisy.fragment.diaryFrags.DiarioPesiFragment;
import com.example.andrea.dietisy.model.DBAdapter;


public class DiaryFragment extends Fragment {


    private DBAdapter mDBHelper;

    private ViewPager mViewPager;
    public static TabLayout tabLayout;
    private FrammentiDiarioPagerAdapter mPagerAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        getActivity().setTitle("Diario");
        mDBHelper = DBAdapter.getIstance(getContext());

        initCtrls(v);

        inizializzaTabs();

        return v;
    }


    private void inizializzaTabs(){
        mPagerAdapter = new FrammentiDiarioPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mViewPager);
            }
        });
    }



    private void initCtrls(View v){
        mViewPager = (ViewPager) v.findViewById(R.id.pagerDiario);
        tabLayout = (TabLayout) v.findViewById(R.id.tabsDiario);
    }






    private class FrammentiDiarioPagerAdapter extends FragmentPagerAdapter{

        public FrammentiDiarioPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return new DiarioPesiFragment();
            else
                return new DiarioMisureFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }


        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Pesi";
                case 1 :
                    return "Misure";
                /*case 2 :
                    return "Updates";*/
            }
            return null;
        }

    }



  /*  private void riempiTab(View v){

        ScrollView sv = new ScrollView(v.getContext());

        TableLayout mTabella = new TableLayout(v.getContext());



        TableRow rowTitle = new TableRow(v.getContext());
        TableRow rowDetails = new TableRow(v.getContext());
        rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        rowDetails.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow colTitle = new TableRow(v.getContext());
        TextView title = new TextView(v.getContext());
        title.setText("DIARIO");
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorAccent));

        // TextView detail = new TextView(getApplicationContext());


        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 6;

        rowTitle.addView(title, params);
        //  rowDetails.addView(detail, params);



        // labels column
        TextView lblProductCode = new TextView(v.getContext());
        lblProductCode.setText("Data");
        lblProductCode.setTypeface(Typeface.DEFAULT_BOLD);
        lblProductCode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        lblProductCode.setTextColor(getResources().getColor(R.color.colorAccent));

        TextView lblDesc = new TextView(v.getContext());
        lblDesc.setText("Peso");
        lblDesc.setTypeface(Typeface.DEFAULT_BOLD);
        lblDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        lblDesc.setTextColor(getResources().getColor(R.color.colorAccent));



        colTitle.addView(lblProductCode);
        colTitle.addView(lblDesc);
        //     colTitle.addView(lblExpiration);

        mTabella.addView(rowTitle);
        mTabella.addView(rowDetails);
        mTabella.addView(colTitle);

        TableRow current;


        List<Peso> pesi = Peso.getPesi(mDBHelper,null,null);




        for(Peso p : pesi){
            current = new TableRow(v.getContext());
            TextView toAdd;

            toAdd = new TextView(v.getContext());
            toAdd.setText(p.getDate());
            toAdd.setTextSize(14);
            toAdd.setTextColor(getResources().getColor(R.color.colorText));
            current.addView(toAdd);

            toAdd = new TextView(v.getContext());
            toAdd.setText(String.valueOf(p.getPeso()));
            toAdd.setTextSize(14);
            toAdd.setTextColor(getResources().getColor(R.color.colorText));
            current.addView(toAdd);

            mTabella.addView(current);
        }
        mTabella.setVerticalScrollBarEnabled(true);
        mTabella.setStretchAllColumns(true);
        mTabella.setShrinkAllColumns(true);

        sv.addView(mTabella);

        RelativeLayout r = (RelativeLayout) v.findViewById(R.id.vista_diario);
      r.addView(sv);

    }*/




}
