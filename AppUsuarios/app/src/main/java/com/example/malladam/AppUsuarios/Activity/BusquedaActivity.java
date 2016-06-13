package com.example.malladam.AppUsuarios.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class BusquedaActivity extends AppCompatActivity {

    private DataBaseManager dbManager;
    private String currentDate;
    final Context context = this;
    Intent intent;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final int DATE_DIALOG_ID = 1;
    private int year,month,day;
    private EditText mFechaIda;
    private EditText mDestino;
    private EditText mOrigen;
    private Button mButtonBuscar;
    private String urlgetCompany;
    private VolleyS volley;
    private Empresa empresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        mFechaIda = (EditText) findViewById(R.id.fechaIda);
        mOrigen = (EditText) findViewById(R.id.origenBusqueda);
        mDestino = (EditText) findViewById(R.id.destinoBusqueda);
        mButtonBuscar = (Button) findViewById(R.id.buscarButton);
        empresa = empresa.getInstance();
        mOrigen.setTextColor(Color.parseColor(empresa.getColorText()));
        mDestino.setTextColor(Color.parseColor(empresa.getColorText()));
        mFechaIda.setTextColor(Color.parseColor(empresa.getColorText()));
        mButtonBuscar.setTextColor(Color.parseColor(empresa.getColorText()));

        dbManager = new DataBaseManager(this);

        View.OnClickListener listenerDate = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                showDialog(DATE_DIALOG_ID);
            }
        };
        mFechaIda.setOnClickListener(listenerDate);


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
            navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor(empresa.getColorText())));
        }

        setupNavigationDrawerContent(navigationView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
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

        // Set the drawer toggle as the DrawerListener
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ///////////ACTIONBAR+NAVIGATION////////////
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
        return super.onOptionsItemSelected(item);
    }
    ///////////ACTIONBAR+NAVIGATION////////////////


    ///////////DATEPICKER////////////////
    private void updateDisplay() {
        currentDate = new StringBuilder().append(day).append(".").append(month + 1).append(".").append(year).toString();
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {
            year = i;
            month = j;
            day = k;
            updateDisplay();
            mFechaIda.setText(currentDate);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dpd = new DatePickerDialog(this, myDateSetListener, year, month, day);
                dpd.getDatePicker().setMinDate(new Date().getTime());
                return dpd;
        }
        return null;
    }
    ///////////DATEPICKER////////////////


}