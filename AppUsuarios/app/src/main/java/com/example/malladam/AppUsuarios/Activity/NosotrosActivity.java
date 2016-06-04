package com.example.malladam.AppUsuarios.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import com.android.IntentIntegrator;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NosotrosActivity extends AppCompatActivity {

    TextView mDescripcion;
    DataBaseManager dbManager;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    Intent intent;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nosotros);

        mDescripcion = (TextView) findViewById(R.id.detalles1nosotros);

        dbManager = new DataBaseManager(this);

        mDescripcion.setText("Somos la primer empresa de transporte colectivo de pasajeros del País, fundada el 20 de octubre de 1930, gracias a la visión, coraje e ingenio de un grupo de transportistas que decidieron unirse y formar una empresa sobre ruedas.\n" +
                "\n" +
                "    Hace 85 años nuestros ómnibus atravesaban médanos y bosques, abriendo caminos hacia balnearios y zonas rurales, donde sólo unos pocos habitantes residían -en ese momento- en forma permanente. En un principio la Compañía atendía un espacio geográfico con centro en la ciudad de Pando en un radio de 60 kilómetros.");


        ///////////ACTIONBAR+NAVIGATION////////////////
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            View headerNavigation = navigationView.getHeaderView(0);
            TextView headerNombre = (TextView) headerNavigation.findViewById(R.id.nombreLogin);
            headerNombre.setText(dbManager.getUserLogueado());

            navigationView.getMenu().clear(); //clear old inflated items.
            if (dbManager.getUserLogueado() == null) {
                navigationView.inflateMenu(R.menu.drawer_logout);
            } else {
                navigationView.inflateMenu(R.menu.drawer_login);
            }
        }

        setupNavigationDrawerContent(navigationView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ///////////ACTIONBAR+NAVIGATION////////////////

    }

    ///////////ACTIONBAR+NAVIGATION////////////////
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
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
                                intent = new Intent(getApplicationContext(), BusquedaActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.iniciarSesion:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.registrarse:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.nosotros:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), NosotrosActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_nosotros, menu);
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
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else if (id == R.id.action_sucursales) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ///////////ACTIONBAR+NAVIGATION////////////////

}
