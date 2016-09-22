package com.example.malladam.AppGuarda.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.malladam.AppGuarda.adapters.PasajeArrayAdapterSelect;
import com.example.malladam.AppGuarda.models.GroupPasajeDT;
import com.example.malladam.AppGuarda.utils.DataBaseManager;
import com.example.malladam.AppGuarda.FragmentIniciarViaje;
import com.example.malladam.AppGuarda.FragmentMain;
import com.example.malladam.AppGuarda.ManejadorInicio;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.AsientoActivo;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.Parada;
import com.example.malladam.AppGuarda.models.ViajeActual;
import com.example.malladam.AppGuarda.utils.UbicacionService;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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
    private VolleyS volley;
    Intent intent;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Empresa empresa;
    private TextView mPrecio, mTextPrecio, mOrigen,mNroAsiento;
    private Button calcular;
    private Spinner spinnerDestino;
    private ArrayAdapter arrayAdapter;
    private View mProgressView;
    private String urlGetSigPradas, urlFinViaje, urlToken, urlGetTicketValue, urlBuyTicket, urlGetSeats;
    public Intent intentUbicacion;
    int intentosLogin = 0;
    private List<Parada> posiblesDestinos = new ArrayList<>();
    private Parada paradaDestino;
    private double valorPasaje;
    public List<AsientoActivo> seats = new ArrayList<>();
    public List<Integer> selectedSeats = new ArrayList<>();
    private PopupWindow popup = new PopupWindow();


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
        urlGetTicketValue = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) +
                getResources().getString(R.string.getTicketValue);
        urlBuyTicket = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) +
                getResources().getString(R.string.buyTicket);
        urlGetSeats=getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/getSeats";


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
            //////////LANZO SERVICIO//////////////
            if(!isMyServiceRunning(UbicacionService.class)){
                Intent mServiceIntent = MainActivity.this.intentUbicacion;
                mServiceIntent.putExtra("urlToken", getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + "/auth");
                mServiceIntent.putExtra("urlUbic", getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + getResources().getString(R.string.setTravelLocation));
                MainActivity.this.startService(mServiceIntent);
            }
            //////////LANZO SERVICIO//////////////

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
                                cerrarSesion(menuItem);
                                return true;
                        }
                        return true;
                    }
                });
    }


    private void cerrarSesion(MenuItem menuItem){
        Integer idViaje = dbManager.getViajeActual().getId_viaje();
        if (idViaje == null) {
            cerrar(menuItem);
        }else{
            mostrarDialogoCerrarYFinalizar(idViaje, menuItem);
        }

    }

    private void cerrar(MenuItem menuItem){
        menuItem.setChecked(true);
        dbManager.eliminarLogin();
        intent = new Intent(getApplicationContext(), ManejadorInicio.class);
        startActivity(intent);
        finish();
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
                            WSgetSigParadasParadasByOrigen(paradaOrigen.getId_parada(), dbManager.getViajeActual().getId_linea(), dbManager.getTokenLogueado());
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
                    mostrarDialogoFinalizar();
                    return true;
                }
            }
            return true;
        }
    }

    ///////////ACTIONBAR+NAVIGATION////////////////


    private void mostrarDialogoCerrarYFinalizar(final int idViajeActual, final MenuItem menuItem) {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.finViaje)+" "+idViajeActual)
                .setMessage("El viaje no ha finalizado, que desea hacer?")
                .setPositiveButton(R.string.cerrarsesionyviaje, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finalizarViajeActual(idViajeActual);
                        cerrar(menuItem);
                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whitch){
                        //NOT
                    }
                })
                .setNeutralButton(R.string.relevo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cerrar(menuItem);
                    }
                })
                .setIcon(R.drawable.bus_icon)
                .show();
    }

    private void mostrarDialogoFinalizar() {
        final int idViajeActual = dbManager.getViajeActual().getId_viaje();

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.finViaje)+" "+idViajeActual)
                .setMessage("Desea finalizar el viaje actual?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finalizarViajeActual(idViajeActual);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whitch){
                        //NOT
                    }
                })
                .setIcon(R.drawable.bus_icon)
                .show();
    }


    private void finalizarViajeActual(int idViajeActual) {

        try {
            WScomunicarFinViaje(idViajeActual, dbManager.getTokenLogueado());
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

        popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mProgressView = layout.findViewById(R.id.vender_progress);

        mPrecio = (TextView) layout.findViewById(R.id.popupPrecio);
        mTextPrecio = (TextView) layout.findViewById(R.id.textViewPrecio);
        mOrigen = (TextView) layout.findViewById(R.id.textViewOrigen);
        spinnerDestino = (Spinner) layout.findViewById(R.id.spinnerDestino);
        mNroAsiento = (TextView) layout.findViewById(R.id.textViewNroAsi);
        calcular = (Button) layout.findViewById(R.id.vender);

        mOrigen.setText(paradaOrigen.getDescripcion());
        mOrigen.setKeyListener(null);

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line, covertirParadasAlistaSpinner(posiblesDestinos));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!posiblesDestinos.isEmpty()) {
            spinnerDestino.setAdapter(arrayAdapter);
        }

        spinnerDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    paradaDestino = posiblesDestinos.get(position);
                    ViajeActual actual = dbManager.getViajeActual();
                    mProgressView.setVisibility(View.VISIBLE);
                    calcular.setVisibility(View.GONE);

                    try {
                        WSobtenerValorPasaje(paradaOrigen.getId_parada(), paradaDestino.getId_parada(),
                                actual.getId_linea(), actual.getId_viaje(), actual.getId_vehiculo());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
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

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViajeActual actual = dbManager.getViajeActual();
                try {
                    WSvenderPasaje(actual.getId_viaje(), paradaOrigen.getId_parada(), paradaDestino.getId_parada(),
                            actual.getId_linea(), actual.getId_vehiculo(), valorPasaje,
                            Integer.parseInt(mNroAsiento.getText().toString()),dbManager.getTokenLogueado() );
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void WScomunicarFinViaje(final Integer idViaje, final String token) throws JSONException, TimeoutException, ExecutionException {

        StringRequest strReq = new StringRequest(Request.Method.GET, urlFinViaje + "?travelId=" + String.valueOf(idViaje),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WSFinDeViaje OK", response.toString());

                        stopService(intentUbicacion);
                        dbManager.eliminarUbicacion();
                        dbManager.eliminarParadasDelViaje();
                        dbManager.eliminarTodosAsientos();
                        dbManager.eliminarViaje();

                        //GET INFO PASAJE FINALIZADO//////////////////////////////////////////
                        showPopupFinViaje(idViaje, MainActivity.this);
                        iniciarSigFragment();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    Log.d("WSInicioDeViaje ERROR ", volleyError.toString());
                    if( volleyError instanceof AuthFailureError) {
                        refrescarLoginFinaizarViaje(dbManager.getUserLogueado(), dbManager.getPassLogueado());
                    } else if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error al finalizar el viaje", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
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


    private void showPopupFinViaje(Integer id_viaje, Activity context) {

        if(id_viaje != null) {
            // Inflate the popup_layout.xml
            LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popupInfoPasajero);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.popup_fin_viaje, viewGroup);

            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            TextView origen = (TextView) layout.findViewById(R.id.popupFinViajeOrigen);
            TextView destino = (TextView) layout.findViewById(R.id.popupFinViajeDestino);
            TextView viaje = (TextView) layout.findViewById(R.id.popupFinViajeNroViaje);
            TextView vendidos = (TextView) layout.findViewById(R.id.popupFinViajePasajesVendidos);
            TextView cobrados = (TextView) layout.findViewById(R.id.popupFinViajePasajesCobrados);
            TextView recaudacion = (TextView) layout.findViewById(R.id.popupFinViajeRecaudacion);

            //origen.setText(dbManager.getParadaDelViajeById(pasaje.getId_paradaSube()).getDescripcion());
            //destino.setText(dbManager.getParadaDelViajeById(pasaje.getId_paradaBaja()).getDescripcion());
            //asiento.setText(String.valueOf(pasaje.getNumero_asiento()));
            //vendidos.setText(String.valueOf(pasaje.getNumero_asiento()));
            //cobrados.setText(String.valueOf(pasaje.getNumero_asiento()));
            //recaudacion.setText(String.valueOf(pasaje.getNumero_asiento()));

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
        }
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


    private void WSgetSigParadasParadasByOrigen(final int id_parada_origen,final int id_linea, String token) throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET, urlGetSigPradas+ "?origin=" + String.valueOf(id_parada_origen)
                    + "&line=" + String.valueOf(id_linea), null, new Response.Listener<JSONArray>() {
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
                            Toast.makeText(MainActivity.this, "Sin próximas paradas", Toast.LENGTH_LONG).show();
                        } else {
                            Parada seleccionar = new Parada();
                            seleccionar.setDescripcion("Seleccione destino");
                            posiblesDestinos.add(0,seleccionar);
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
                    if( volleyError instanceof AuthFailureError) {
                        try {
                            refrescarLoginSigPAradas(dbManager.getUserLogueado(), dbManager.getPassLogueado(), id_parada_origen, id_linea);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else{
                        Toast.makeText(MainActivity.this, "Error al obtener las próximas paradas", Toast.LENGTH_LONG).show();
                    }
                }
            }, token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void refrescarLoginSigPAradas(final String user, final String pass, final int id_parada_origen, final int id_linea) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", user);
        jsonBody.put("password", pass);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlToken, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    intentosLogin = 0;
                    dbManager.registrarLogin(response.getString("token"), user, pass);
                    WSgetSigParadasParadasByOrigen(id_parada_origen,id_linea,response.getString("token"));
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


    private List<String> covertirParadasAlistaSpinner(List<Parada> paradas) {
        List<String> paradaStr = new ArrayList<>();
        for (Parada par: paradas) {
            paradaStr.add(par.getDescripcion());
        }
        return paradaStr;
    }


    private void WSobtenerValorPasaje(final int id_parada_origen, final int id_parada_destino, final int id_linea, final int id_viaje, final int id_vehi) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id_viaje", id_viaje);
        jsonBody.put("origen", id_parada_origen);
        jsonBody.put("destino", id_parada_destino);
        jsonBody.put("id_linea", id_linea);
        jsonBody.put("id_vehiculo", id_vehi);

        try {
            volley.llamarWString(Request.Method.POST, urlGetTicketValue, jsonBody, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("el response es ", response.toString());
                    mProgressView.setVisibility(View.GONE);
                    calcular.setVisibility(View.VISIBLE);
                    mTextPrecio.setVisibility(View.VISIBLE);
                    mPrecio.setVisibility(View.VISIBLE);
                    //mPrecio.setText(response.toString());
                    valorPasaje=Double.parseDouble(response.toString());
                    try {
                        PedirFafa(urlGetSeats,id_viaje, id_linea, id_parada_origen, id_parada_destino,id_vehi);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR es ", volleyError.toString());
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void WSvenderPasaje(final int id_viaje,final int origen,final int destino,final int id_linea,
                                final int id_vehiculo,final double valor, final int nro_asiento,
                                final String token) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id_viaje", id_viaje);
        jsonBody.put("origen", origen);
        jsonBody.put("destino", destino);
        jsonBody.put("id_linea", id_linea);
        jsonBody.put("id_vehiculo", id_vehiculo);
        jsonBody.put("rDoc", "");
        jsonBody.put("rUser", "");
        jsonBody.put("valor", valor);

        selectedSeats.clear();
        selectedSeats.add(nro_asiento);
        JSONArray seatsJSON = getSeatsJSON(selectedSeats);

        jsonBody.put("seleccionados", seatsJSON);


        try {
            volley.llamarWSCustomArray(Request.Method.POST, urlBuyTicket, jsonBody, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    popup.dismiss();
                    for (int i = 0; i < response.length(); i++) {
                        AsientoActivo asientoActivo = new AsientoActivo();
                        try {
                            JSONObject jsonObject = (JSONObject)response.get(i);

                            asientoActivo.setId_pasaje(jsonObject.getInt("id_pasaje"));
                            asientoActivo.setNumero_pasaje(jsonObject.getString("numero"));
                            asientoActivo.setCosto(Float.parseFloat(jsonObject.getString("costo")));
                            asientoActivo.setUsername_usuario("generico");
                            asientoActivo.setNombre_usuario("Usuario");
                            asientoActivo.setApellido_usuario("Genérico");
                            JSONObject viaje = jsonObject.getJSONObject("viaje");
                            asientoActivo.setId_viaje(viaje.getInt("id_viaje"));
                            try{
                                JSONObject asiento = jsonObject.getJSONObject("asiento");
                                asientoActivo.setId_asiento(asiento.getInt("id_asiento"));
                                asientoActivo.setNumero_asiento(asiento.getInt("numero"));
                            } catch (JSONException e) {
                                asientoActivo.setId_asiento(0);
                                asientoActivo.setNumero_asiento(0);
                            }
                            JSONObject paradaSube = jsonObject.getJSONObject("parada_sube");
                            asientoActivo.setId_paradaSube(paradaSube.getInt("id_parada"));
                            JSONObject paradaBaja = jsonObject.getJSONObject("parada_baja");
                            asientoActivo.setId_paradaBaja(paradaBaja.getInt("id_parada"));

                            dbManager.insertarAsientoActivo(asientoActivo);
                            showPopupQr(MainActivity.this,asientoActivo.getNumero_pasaje());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(MainActivity.this, "Pasajes vendidos con éxito", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ", volleyError.toString());
                    if( volleyError instanceof AuthFailureError) {
                        try {
                            refrescarLoginVenderPasaje(dbManager.getUserLogueado(), dbManager.getPassLogueado(),
                                    id_viaje, origen, destino, id_linea, id_vehiculo, valor, nro_asiento);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else{
                        Toast.makeText(MainActivity.this, "Error al vender los pasajes", Toast.LENGTH_LONG).show();
                        popup.dismiss();
                    }
                }
            }, token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void refrescarLoginVenderPasaje(final String user, final String pass,final int id_viaje, final int origen,
                                            final int destino,final int id_linea, final int id_vehiculo,final double valor,
                                            final int nro_asiento) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", user);
        jsonBody.put("password", pass);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlToken, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    intentosLogin = 0;
                    dbManager.registrarLogin(response.getString("token"), user, pass);
                    WSvenderPasaje(id_viaje,origen,destino,id_linea,id_vehiculo,valor,nro_asiento,response.getString("token"));
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


    private JSONArray getSeatsJSON(List<Integer> seats)
    {
        JSONArray jsonArray = new JSONArray();
        for (Integer seat: seats)
        {
            jsonArray.put(seat);
        }
        return jsonArray;
    }



    private void showPopupQr(final Activity context, String numpasaje)
    {
        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_qr);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_qr, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ImageView imagen_qr = (ImageView) layout.findViewById(R.id.img_qr_pasaje);
        try {
            Bitmap bitmap = encodeAsBitmap(numpasaje);
            imagen_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Button btn_qr = (Button) layout.findViewById(R.id.close);
        btn_qr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                iniciarSigFragment();
            }
        });
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        int WHITE = 0xFFFFFFFF;
        int BLACK = 0xFF000000;
        int WIDTH = 400;
        int HEIGHT = 400;

        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void PedirFafa(String url, int idViaje, int IdLinea, int origin, int destination,
                           int idVehiculo) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id_viaje", idViaje);
        jsonBody.put("id_linea",IdLinea);
        jsonBody.put("origen", origin);
        jsonBody.put("destino",destination);
        jsonBody.put("id_vehiculo",idVehiculo);

        try {
            volley.llamarWSCustomArray(Request.Method.POST, url, jsonBody,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response)
                {
                    try {
                        seats.clear();
                        for(int i = 0; i < response.length(); i++)
                        {
                            AsientoActivo seat = new AsientoActivo();
                            JSONObject seatJson = (JSONObject)response.get(i);

                            seat.setId_asiento(seatJson.getInt("id_asiento"));
                            seat.setNumero_asiento(seatJson.getInt("numero"));
                            seat.setEsAccesible(seatJson.getBoolean("es_accesible"));
                            seat.setEsVentana(seatJson.getBoolean("es_ventana"));
                            seat.setHabilitado(seatJson.getBoolean("habilitado"));
                            seat.setReservado(seatJson.getBoolean("reservado"));

                            seats.add(seat);
                        }
                        if(!seats.isEmpty()){
                            mostrarPopupSelectAsientos();
                        }else{
                            Toast.makeText(MainActivity.this, "No hay lugares disponibles", Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR es ",volleyError.toString());
                }
            }, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void mostrarPopupSelectAsientos(){

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.popupSelectAsientos);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_select_asiento, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        Button btn_qr = (Button) layout.findViewById(R.id.btn_ok);
        btn_qr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);




        List<GroupPasajeDT> pasajesAgrupados = new ArrayList<>();

        for (int item = 0; item < seats.size();)
        {
            GroupPasajeDT grupoPasajes = new GroupPasajeDT();
            for (int itemInterno = 0; itemInterno < 4; itemInterno++)
            {
                if(item < seats.size())
                {
                    switch (itemInterno) {
                        case 0:
                            grupoPasajes.setPasaje1(seats.get(item));
                            break;
                        case 1:
                            grupoPasajes.setPasaje2(seats.get(item));
                            break;
                        case 2:
                            grupoPasajes.setPasaje3(seats.get(item));
                            break;
                        case 3:
                            grupoPasajes.setPasaje4(seats.get(item));
                            break;
                    }
                }
                item++;
            }
            pasajesAgrupados.add(grupoPasajes);
        }

        ListView lista = (ListView)findViewById(R.id.list_selectasientos);
        PasajeArrayAdapterSelect<GroupPasajeDT> seatsArray;
        seatsArray = new PasajeArrayAdapterSelect<GroupPasajeDT>(this,pasajesAgrupados, valorPasaje);
        LayoutInflater inflater = this.getLayoutInflater();
        View header = inflater.inflate(R.layout.list_header_row, lista, false);
        lista.addHeaderView(header, null, false);

        lista.setAdapter(seatsArray);
        List<Integer> seats = seatsArray.selectedSeats;
        double totalValue = seatsArray.totalValue;

    }

}