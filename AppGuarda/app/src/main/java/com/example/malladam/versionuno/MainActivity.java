package com.example.malladam.versionuno;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.android.IntentIntegrator;
import com.example.malladam.versionuno.models.Pasaje;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataBaseManager dbManager;
    final Context context = this;
    public ArrayList<Pasaje> pasajes;
    Intent intent;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DataBaseManager(this);

        ////////////NAVIGATION VIEW////////////////
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            View headerNavigation = navigationView.getHeaderView(0);
            TextView headerNombre = (TextView) headerNavigation.findViewById(R.id.nombreLogin);
            headerNombre.setText( dbManager.getUserLogueado() );

            setupNavigationDrawerContent(navigationView);
        }
        ////////////NAVIGATION VIEW////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentMain fragmentMain = new FragmentMain();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameMain,fragmentMain);
        fragmentTransaction.commit();
/*
        //Traer Pasajes
        pasajes = dbManager.obtenerPasajesActivos("VIAJE");*/

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asientos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_newpasaje) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}