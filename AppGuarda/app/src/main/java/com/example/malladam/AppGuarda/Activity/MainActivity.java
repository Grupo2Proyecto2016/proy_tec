package com.example.malladam.AppGuarda.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.malladam.AppGuarda.DataBaseManager;
import com.example.malladam.AppGuarda.FragmentIniciarViaje;
import com.example.malladam.AppGuarda.FragmentMain;
import com.example.malladam.AppGuarda.ManejadorInicio;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.Parada;
import com.example.malladam.AppGuarda.models.Pasaje;
import com.example.malladam.AppGuarda.utils.UbicacionService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private Parada paradaOrigen;
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
    private LatLng latLngOrigen, latLngDestino;
    private String urlGetSigPradas, urlFinViaje, urlToken;
    public Intent intentUbicacion;
    int intentosLogin = 0;
    private List<Parada> posiblesDestinos;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentUbicacion = new Intent(MainActivity.this, UbicacionService.class);
        volley = volley.getInstance(this);
        dbManager = new DataBaseManager(this);
        empresa = empresa.getInstance();
        urlGetSigPradas = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name)
                + getResources().getString(R.string.findNextStationsByOrigin);
        urlFinViaje = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name)
                + getResources().getString(R.string.finishTravel);
        urlToken = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) +
                getResources().getString(R.string.auth);




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
        menu.clear();
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
        } else {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_newpasaje: {
                    Intent intent = new Intent(MainActivity.this, QrActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.action_venderpasaje: {
                    Double lat = dbManager.getLatitud();
                    Double lon = dbManager.getLongitud();
                    if(lat!= 0 && lon!=0){
                        LatLng actual = new LatLng(lat,lon );

                        paradaOrigen = paradaProximaDelListado(actual, dbManager.getParadasDelViaje());
                        try {
                            WSgetSigParadasParadasByOrigen(paradaOrigen.getId_parada(), dbManager.getViajeActual().getId_linea());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        Toast.makeText(MainActivity.this, "Error al obtener la ubicacion actual", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }

                case R.id.action_finish_viaje: {
                    finalizarViajeActual();
                    return true;
                }
            }
            return true;
        }
    }

    ///////////ACTIONBAR+NAVIGATION////////////////


    private void finalizarViajeActual() {

        try {
            WScomunicarFinViaje(dbManager.getViajeActual().getId_viaje(), dbManager.getTokenLogueado());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void showPopup(final Activity context) {

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_vender_pasaje, viewGroup);

        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPrecio = (TextView) layout.findViewById(R.id.popupPrecio);
        mTextPrecio = (TextView) layout.findViewById(R.id.textViewPrecio);
        mOrigen = (TextView) layout.findViewById(R.id.textViewOrigen);
        spinnerDestino = (Spinner) layout.findViewById(R.id.spinnerDestino);

        mOrigen.setText(paradaOrigen.getDireccion());

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line, posiblesDestinos);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!posiblesDestinos.isEmpty()) {
            spinnerDestino.setAdapter(arrayAdapter);
        }


        spinnerDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //latLngDestino = obtenerLatLngByParada(String.valueOf(spinnerDestino.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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


    private void WScomunicarFinViaje(final Integer idViaje, final String token) throws JSONException, TimeoutException, ExecutionException {

        StringRequest strReq = new StringRequest(Request.Method.GET, urlFinViaje + "?travelId=" + String.valueOf(idViaje),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WSInicioDeViaje OK", response.toString());

                        dbManager.eliminarViaje();
                        stopService(intentUbicacion);
                        Toast.makeText(MainActivity.this, "Viaje finalizado con éxito", Toast.LENGTH_LONG).show();
                        iniciarSigFragment();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("WSInicioDeViaje ERROR ", volleyError.toString());
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error al finalizar el viaje", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                if (token != null) {
                    headers.put("Authorization", token);
                }
                return headers;
            }
        };
        // Adding request to request queue
        volley.addToQueue(strReq);

    }


    private void refrescarLoginFinaizarViaje(final String user, final String pass) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", user);
        jsonBody.put("password", pass);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlToken, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    intentosLogin = 0;
                    dbManager.registrarLogin(response.getString("token"), user, pass);
                    WScomunicarFinViaje(dbManager.getViajeActual().getId_viaje(), response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("el ERROR del token es ", volleyError.toString());
                if (intentosLogin >= 2) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.tokenInvalido), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else intentosLogin++;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        volley.addToQueue(request);
    }



    private Parada paradaProximaDelListado(LatLng actual, List<Parada> paradas) {

        Parada cercana = paradas.get(0);
        float[] primerResult = new float[1];
        Location.distanceBetween(actual.latitude, actual.longitude,
                paradas.get(0).getLatitud(), paradas.get(0).getLongitud(),
                primerResult);

        float distanciaMinima = primerResult[0];

        for (Parada parada : paradas) {
            float[] results = new float[1];
            Location.distanceBetween(actual.latitude, actual.longitude,
                    parada.getLatitud(), parada.getLongitud(),
                    results);
            if (results[0] < distanciaMinima) {
                cercana = parada;
            }
        }
        return cercana;
    }


    private void WSgetSigParadasParadasByOrigen(int id_parada_origen, int id_linea) throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET, urlGetSigPradas+ "?origin=" + String.valueOf(id_parada_origen)
                    + "?line=" + String.valueOf(id_linea), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    posiblesDestinos.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Parada parada = new Parada();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            parada.setDescripcion(jsonObject.getString("descripcion"));
                            parada.setEs_terminal(jsonObject.getBoolean("es_terminal"));
                            parada.setDireccion(jsonObject.getString("direccion"));
                            parada.setId_parada(jsonObject.getInt("id_parada"));
                            parada.setLatitud(jsonObject.getDouble("latitud"));
                            parada.setLongitud(jsonObject.getDouble("longitud"));
                            posiblesDestinos.add(parada);
                        }
                        if (posiblesDestinos.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Error al obtener las próximas paradas", Toast.LENGTH_LONG).show();
                        } else {
                            showPopup(MainActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ", volleyError.toString());
                    Toast.makeText(MainActivity.this, "Error al obtener las próximas paradas", Toast.LENGTH_LONG).show();
                }
            }, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
    /*
    private List<String> covertirParadasAlistaSpinner(List<Parada> paradas) {
        List<String> paradaStr = new ArrayList<>();
        for (Parada par: paradas) {
            paradaStr.add(par.getDescripcion());
        }
        return paradaStr;
    }*/