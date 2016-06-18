package com.example.malladam.AppUsuarios.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;
import java.util.Calendar;
import java.util.Date;

public class BusquedaActivity extends AppCompatActivity  implements NumberPicker.OnValueChangeListener{

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
    private Button mDestino;
    private Button mOrigen;
    private Button mButtonBuscar;
    private String urlgetCompany;
    private VolleyS volley;
    private Empresa empresa;
    private TextView mCantAsientos;
    static Dialog d ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        mFechaIda = (EditText) findViewById(R.id.fechaIda);
        mOrigen = (Button) findViewById(R.id.origenBusqueda);
        mDestino = (Button) findViewById(R.id.destinoBusqueda);
        mButtonBuscar = (Button) findViewById(R.id.buscarButton);
        mCantAsientos = (TextView) findViewById(R.id.cantidadAsientos);
        empresa = empresa.getInstance();
        mOrigen.setTextColor(Color.parseColor(empresa.getColorBack()));
        mDestino.setTextColor(Color.parseColor(empresa.getColorBack()));
        mOrigen.setBackgroundColor(Color.parseColor(empresa.getColorText()));
        mDestino.setBackgroundColor(Color.parseColor(empresa.getColorText()));
        mFechaIda.setTextColor(Color.parseColor(empresa.getColorText()));
        mCantAsientos.setTextColor(Color.parseColor(empresa.getColorText()));

        DrawerLayout mealLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));

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


        //numberPcker//
        mCantAsientos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);

    }

    public void show()
    {
        final Dialog d = new Dialog(BusquedaActivity.this);
        d.setTitle(getResources().getString(R.string.cantAsientos));
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(50);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mCantAsientos.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
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