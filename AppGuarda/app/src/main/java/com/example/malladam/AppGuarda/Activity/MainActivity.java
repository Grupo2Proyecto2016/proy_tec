package com.example.malladam.AppGuarda.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.IntentIntegrator;
import com.example.malladam.AppGuarda.DataBaseManager;
import com.example.malladam.AppGuarda.FragmentIniciarViaje;
import com.example.malladam.AppGuarda.FragmentMain;
import com.example.malladam.AppGuarda.ManejadorInicio;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.Pasaje;
import com.example.malladam.AppGuarda.utils.MenuTintUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataBaseManager dbManager;
    final Context context = this;
    public ArrayList<Pasaje> pasajes;
    Intent intent;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Empresa empresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DataBaseManager(this);
        empresa = empresa.getInstance();


        ///////////ACTIONBAR+NAVIGATION////////////////
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            View headerNavigation = navigationView.getHeaderView(0);
            TextView headerNombre = (TextView) headerNavigation.findViewById(R.id.nombreLogin);
            headerNombre.setText(dbManager.getUserLogueado());
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer);
        }

        setupNavigationDrawerContent(navigationView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorTextHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorHeader()));
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ///////////ACTIONBAR+NAVIGATION////////////




        if(dbManager.getViajeActual() == null){
            FragmentIniciarViaje fragmentIniciarViaje = new FragmentIniciarViaje();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameMain,fragmentIniciarViaje);
            fragmentTransaction.commit();
        }else{
            FragmentMain fragmentMain = new FragmentMain();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameMain,fragmentMain);
            fragmentTransaction.commit();
        }

    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.cerrarSesion:
                                menuItem.setChecked(true);
                                dbManager.eliminarLogin();
                                intent = new Intent(getApplicationContext(), ManejadorInicio.class);
                                startActivity(intent);
                                finish();
                                return true;
                        }
                        return true;
                    }
                });
    }


    ///////////ACTIONBAR+NAVIGATION////////////////
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(dbManager.getViajeActual() != null){
            getMenuInflater().inflate(R.menu.menu_asientos, menu);
            MenuTintUtils menuTintUtils = new MenuTintUtils();
            menuTintUtils.tintAllIcons(menu,Color.parseColor(empresa.getColorHeader()));
        }

        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_newpasaje) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            return true;
        }
        else if (id == R.id.action_finish_viaje) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.action_finish_viaje)+" "+dbManager.getViajeActual())
                    .setMessage("Desea finalizar el viaje actual ?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //WScomunicarFinDelViaje
                            dbManager.eliminarViaje();

                            Toast.makeText(MainActivity.this, "El viaje ha sido finalizado con éxito", Toast.LENGTH_LONG).show();

                            FragmentIniciarViaje fragmentIniciarViaje = new FragmentIniciarViaje();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frameMain,fragmentIniciarViaje);
                            fragmentTransaction.commit();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whitch){
                            //NOT
                        }
                    })

                    .setIcon(R.drawable.bus_icon)
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////ACTIONBAR+NAVIGATION////////////////


}