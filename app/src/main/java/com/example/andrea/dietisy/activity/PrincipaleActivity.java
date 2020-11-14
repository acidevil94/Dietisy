


package com.example.andrea.dietisy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.andrea.dietisy.R;
import com.example.andrea.dietisy.fragment.DiaryFragment;
import com.example.andrea.dietisy.fragment.MainFragment;
import com.example.andrea.dietisy.fragment.SettingFragment;
import com.example.andrea.dietisy.utility.ConfigValues;
import com.example.andrea.dietisy.utility.PreferencesHandler;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class PrincipaleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private final String TAG = "PRINCIPALEACTIVITY";

    Handler mHandler;
    DrawerLayout drawer;

    // index to identify current nav menu item
    public static int navItemIndex = 0;



    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_DIARY = "diary";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_SHARE = "share";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    private GoogleApiClient client;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //se prima apertura delll'app allora mostra l'activity per i suggerimenti
         //ckPrimaApertura();
        PreferencesHandler pH = PreferencesHandler.getIstance(getApplicationContext());
        boolean isPrimaApertura = pH.isPrimaApertura();
        if(isPrimaApertura){
            Log.e(TAG,"IS PRIMA APERTURA!");
            Intent startupActivity = new Intent(this,StartupActivity.class);
            startActivity(startupActivity);
            finish();
            return;
        }else {
            Log.e(TAG,"IS NOT PRIMA APERTURA!");
        }

        setContentView(R.layout.activity_principale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.app_name);

        initControl();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHandler = new Handler();

        loadHomeFragment();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void initControl() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                break;
            case R.id.nav_diary:
                navItemIndex = 1;
                CURRENT_TAG = TAG_DIARY;
                break;
            case R.id.nav_slideshow:
                navItemIndex = 2;
                CURRENT_TAG = TAG_MOVIES;
                break;
            case R.id.nav_manage:
                navItemIndex = 3;
                CURRENT_TAG = TAG_SETTINGS;
                break;
            case R.id.nav_share:
                navItemIndex = 4;
                CURRENT_TAG = TAG_SHARE;
                break;
            case R.id.nav_send:
                //TODO chiamare un activity per inviare un messaggio al programmatore
                // launch new intent instead of loading fragment
            /*    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                drawer.closeDrawers();*/



                return true;
            default:
                navItemIndex = 0;
        }
        loadHomeFragment();
        return true;
    }




    private void loadHomeFragment() {
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_principale, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
            //    HomeFragment homeFragment = new HomeFragment();

                MainFragment homeFragment = new MainFragment();
                return homeFragment;
            case 1:
                DiaryFragment diarioFragment = new DiaryFragment();
              // DiarioPesiFragment diarioFragment = new DiarioPesiFragment();
                return diarioFragment;

            case 3:
                // setting fragment
                SettingFragment setFragment = new SettingFragment();
                return setFragment;
            default:
                return new MainFragment();
        }
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Principale Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
