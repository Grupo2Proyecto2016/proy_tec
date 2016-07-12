package com.example.malladam.AppGuarda.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppGuarda.DataBaseManager;
import com.example.malladam.AppGuarda.FragmentIniciarViaje;
import com.example.malladam.AppGuarda.FragmentMain;
import com.example.malladam.AppGuarda.ManejadorInicio;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.Pasaje;
import com.example.malladam.AppGuarda.utils.UbicacionService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private DataBaseManager dbManager;
    final Context context = this;
    public ArrayList<Pasaje> pasajes;
    private VolleyS volley;
    Intent intent;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Empresa empresa;
    private TextView mPrecio, mTextPrecio, mOrigen;
    private Spinner spinnerDestino;
    private ArrayAdapter arrayAdapter;
    private View mProgressView;
    private List listParadas;
    private LatLng latLngOrigen, latLngDestino;
    private String urlGetSigPradas;
    public Intent intentUbicacion ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentUbicacion =new Intent(MainActivity.this, UbicacionService.class);
        volley = volley.getInstance(this);
        dbManager = new DataBaseManager(this);
        empresa = empresa.getInstance();
        urlGetSigPradas = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)
                +getResources().getString(R.string.findNextStationsByOrigin);


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

        iniciarSigFragment();
    }


    private void iniciarSigFragment() {

        if (dbManager.getViajeActual().getId_viaje() == null) {
            FragmentIniciarViaje fragmentIniciarViaje = new FragmentIniciarViaje();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameMain, fragmentIniciarViaje);
            fragmentTransaction.commit();
        } else {
            FragmentMain fragmentMain = new FragmentMain();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameMain, fragmentMain);
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
        return super.onCreateOptionsMenu(menu);
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
        else{
            int id = item.getItemId();
            switch (id) {
                case R.id.action_newpasaje: {
                    Intent intent = new Intent(MainActivity.this, QrActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.action_venderpasaje: {
                    showPopup(MainActivity.this, "PARADA ACTUAL");
                    return true;
                }

                case R.id.action_finish_viaje: {
                    dbManager.eliminarViaje();

                    //UbicacionService ubicacionService = UbicacionService.getInstance();
                    //ubicacionService.onDestroy();

                    stopService(intentUbicacion);

                    iniciarSigFragment();
                    return true;
                }
            }
                return true;
        }
    }
    ///////////ACTIONBAR+NAVIGATION////////////////



    private void showPopup(final Activity context, String paradaActual) {
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_vender_pasaje, viewGroup);

        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPrecio = (TextView) layout.findViewById(R.id.popupPrecio);
        mTextPrecio = (TextView) layout.findViewById(R.id.textViewPrecio);


        mOrigen = (TextView) layout.findViewById(R.id.textViewOrigen);
        spinnerDestino = (Spinner) layout.findViewById(R.id.spinnerDestino);
        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line, listParadas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(listParadas != null){
            spinnerDestino.setAdapter(arrayAdapter);
        }

        mOrigen.setText(paradaActual);

        spinnerDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //latLngDestino = obtenerLatLngByParada(String.valueOf(spinnerDestino.getSelectedItem()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        Button calcular = (Button) layout.findViewById(R.id.vender);
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //venderPasaje(Float.parseFloat(largo), Float.parseFloat(ancho),Float.parseFloat(alto), Float.parseFloat(peso));
            }
        });
    }


    /*private LatLng obtenerLatLngByParada(String parada){
        LatLng latLng = new LatLng(0,0);
        for (Parada term: paradasPosiblesDestino) {
            if (term.getDescripcion().equals(parada)) {
                latLng = new LatLng(term.getLatitud(), term.getLongitud());
            }
        }
        return latLng;
    }*/


/*
    private void WSgetSigParadasParadasByOrigen() throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET,urlGetSigPradas, null,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Terminal terminal = new Terminal();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            terminal.setReajuste(jsonObject.getInt("reajuste"));
                            terminal.setEs_terminal(jsonObject.getBoolean("es_terminal"));
                            terminal.setDireccion(jsonObject.getString("direccion"));
                            terminal.setId_parada(jsonObject.getInt("id_parada"));
                            terminal.setLatitud(jsonObject.getDouble("latitud"));
                            terminal.setLongitud(jsonObject.getDouble("longitud"));
                            terminal.setDescripcion(jsonObject.getString("descripcion"));
                            terminal.setSucursal(false);
                            terminal.setEs_peaje(false);
                            terminales.add(terminal);
                        }
                        if(terminales.isEmpty()){
                            Toast.makeText(EncomiendasActivity.this, "Error al obtener las terminales desponibles", Toast.LENGTH_LONG).show();
                        }else{
                            listTerminales=covertirTerminalesAlistaSpinner(terminales);
                            mProgressView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ",volleyError.toString());
                }
            }, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
*/
    /*
    private List<String> covertirParadasAlistaSpinner(List<Parada> paradas) {
        List<String> paradaStr = new ArrayList<>();
        for (Parada par: paradas) {
            paradaStr.add(par.getDescripcion());
        }
        return paradaStr;
    }*/




}