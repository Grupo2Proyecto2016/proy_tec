package com.example.malladam.AppUsuarios.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;
import com.example.malladam.AppUsuarios.models.Parada;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class BusquedaActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

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
    private String urlgetStations,urlGetFilteredStations, urlSearchTravels;
    private VolleyS volley;
    private Empresa empresa;
    private GoogleMap mMap;
    private PopupWindow pw;
    private List<Parada> paradas = new ArrayList<Parada>();
    private List<Parada> paradasDestino = new ArrayList<Parada>();
    private List<Parada> paradasOrigen = new ArrayList<Parada>();
    private List<Parada> paradasByDestino = new ArrayList<Parada>();
    private Circle circle;
    double radiusInMeters = 1000;
    LatLng puntoSeleccionado;
    Boolean origenPress=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        mFechaIda = (EditText) findViewById(R.id.fechaIda);
        mOrigen = (Button) findViewById(R.id.origenBusqueda);
        mDestino = (Button) findViewById(R.id.destinoBusqueda);
        mButtonBuscar = (Button) findViewById(R.id.buscarButton);
        empresa = empresa.getInstance();
        mOrigen.setTextColor(Color.parseColor(empresa.getColorBack()));
        mDestino.setTextColor(Color.parseColor(empresa.getColorBack()));
        mOrigen.setBackgroundColor(Color.parseColor(empresa.getColorText()));
        mDestino.setBackgroundColor(Color.parseColor(empresa.getColorText()));
        mFechaIda.setTextColor(Color.parseColor(empresa.getColorText()));

        urlSearchTravels = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.searchTravels);
        urlgetStations = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.getStations);
        urlGetFilteredStations = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.getFilteredStations);
        volley = volley.getInstance(this);

        DrawerLayout mealLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));

        mOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origenPress=true;
                if(paradasDestino.isEmpty()){
                    Toast.makeText(BusquedaActivity.this, getResources().getString(R.string.origenSinDestino), Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        WSgetParadasByDestino(paradasDestino);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origenPress=false;
                try {
                    WSgetAllParadas();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        mButtonBuscar.setTextColor(Color.parseColor(empresa.getColorBack()));
        mButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentDate == null)
                    Toast.makeText(BusquedaActivity.this, "Ingrese la fecha de viaje", Toast.LENGTH_LONG).show();
                else if(paradasDestino == null || paradasDestino.isEmpty())
                    Toast.makeText(BusquedaActivity.this, "Ingrese el destino del viaje", Toast.LENGTH_LONG).show();
                else if(paradasOrigen == null || paradasOrigen.isEmpty())
                    Toast.makeText(BusquedaActivity.this, "Ingrese el origen del viaje", Toast.LENGTH_LONG).show();
                else {
                    try {
                        WSbuscarViajes(currentDate, currentDate, paradasOrigen, paradasDestino);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



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

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor(empresa.getColorTextHeader()), PorterDuff.Mode.SRC_ATOP);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        toolbar.setNavigationIcon(upArrow);
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
    }
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
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

                            case R.id.misEncomiendas:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), EncomiendasActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.misViajes:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), PasajesActivity.class);
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
        currentDate = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).toString();
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

    public Calendar getCalendar(int day, int month, int year) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month+1);
        date.set(Calendar.DAY_OF_MONTH, day);

        return date;
    }


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

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(origenPress) {
            for (Parada item : paradasByDestino) {//bydestino
                LatLng suc = new LatLng(item.getLatitud(), item.getLongitud());
                if (item.getEsTerminal()) {
                    mMap.addMarker(new MarkerOptions()
                            .position(suc)
                            .title(item.getDireccion())
                            .snippet(item.getDescripcion())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                } else {
                    mMap.addMarker(new MarkerOptions()
                            .position(suc)
                            .title(item.getDireccion())
                            .snippet(item.getDescripcion())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }
        }
        else{
            for (Parada item : paradas) {
                LatLng suc = new LatLng(item.getLatitud(), item.getLongitud());
                if (item.getEsTerminal()) {
                    mMap.addMarker(new MarkerOptions()
                            .position(suc)
                            .title(item.getDireccion())
                            .snippet(item.getDescripcion())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                } else {
                    mMap.addMarker(new MarkerOptions()
                            .position(suc)
                            .title(item.getDireccion())
                            .snippet(item.getDescripcion())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }
        }

        LatLng centroMapa = new LatLng(-34.83346,-56.16735);//montevideo
        if(origenPress) {
            if(!paradasByDestino.isEmpty()) {
                centroMapa = new LatLng(paradasByDestino.get(0).getLatitud(), paradasByDestino.get(0).getLongitud());
            }
        }else{
            if(!paradas.isEmpty()) {
                centroMapa = new LatLng(paradas.get(0).getLatitud(), paradas.get(0).getLongitud());
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centroMapa, 13.0f));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(circle != null) {
                    circle.remove();
                }
                CircleOptions circleOptions = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(0x550000ff).strokeColor(Color.BLUE).strokeWidth(2);
                circle = mMap.addCircle(circleOptions);

                puntoSeleccionado = latLng;//actualizo ubicacion de global puntoSeleccionado
            }
        });
    }

    private void initiatePopupWindow(Boolean origen){
        LayoutInflater inflater = (LayoutInflater) BusquedaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_maps, (ViewGroup) findViewById(R.id.popupMaps));
        Display display = getWindowManager().getDefaultDisplay();

        //layout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));
        TextView mMapTitle=(TextView)layout.findViewById(R.id.mapTitle);
        mMapTitle.setTextColor(Color.parseColor(empresa.getColorText()));
        if(origen){
            mMapTitle.setText(getResources().getString(R.string.dondesubes));
        }else {
            mMapTitle.setText(getResources().getString(R.string.dondequieresir));
        }

        pw = new PopupWindow(layout, display.getWidth() - 120, display.getHeight() - 120);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPopup)).getMapAsync(this);

        Button buttonOk=(Button)layout.findViewById(R.id.maps_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(puntoSeleccionado!=null){
                    if(origenPress){
                        paradasOrigen = obtenerParadasCercanasApunto(radiusInMeters, puntoSeleccionado, paradasByDestino);
                    }else{
                        paradasDestino = obtenerParadasCercanasApunto(radiusInMeters, puntoSeleccionado, paradas);
                    }
                }
                SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPopup);
                if (f != null)
                    getSupportFragmentManager().beginTransaction().remove(f).commit();
                pw.dismiss();
            }
        });
    }

    private void WSgetAllParadas() throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET,urlgetStations, null,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        paradas.clear();
                        for (int i = 0; i < response.length(); i++) {
                            Parada parada = new Parada();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            parada.setDescripcion(jsonObject.getString("descripcion"));
                            parada.setDireccion(jsonObject.getString("direccion"));
                            parada.setEsPeaje(false);//hardcode jsonObject.getBoolean("es_peaje")
                            parada.setEsTerminal(false);//hardcode jsonObject.getBoolean("es_terminal")
                            parada.setLatitud(jsonObject.getDouble("latitud"));
                            parada.setLongitud(jsonObject.getDouble("longitud"));
                            parada.setId_parada(jsonObject.getInt("id_parada"));
                            parada.setReajuste(jsonObject.getInt("reajuste"));
                            parada.setSucursal(false);//hardcode  jsonObject.getBoolean("sucursal")
                            paradas.add(parada);
                        }
                        if(!paradas.isEmpty()){
                            initiatePopupWindow(false);
                        }else{
                            new AlertDialog.Builder(BusquedaActivity.this)
                                    .setTitle("Ops!")
                                    .setMessage("No pudimos mostrar las paradas")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
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


    private void WSgetParadasByDestino(List<Parada> destinos) throws JSONException, TimeoutException, ExecutionException {

        JSONArray jsonArray = getIdByParadas(destinos);
        try {
            volley.llamarWSarray(Request.Method.POST,urlGetFilteredStations, jsonArray,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        paradasByDestino.clear();
                        for (int i = 0; i < response.length(); i++) {
                            Parada parada = new Parada();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            parada.setDescripcion(jsonObject.getString("descripcion"));
                            parada.setDireccion(jsonObject.getString("direccion"));
                            parada.setEsPeaje(false);//hardcode jsonObject.getBoolean("es_peaje")
                            parada.setEsTerminal(false);//hardcode jsonObject.getBoolean("es_terminal")
                            parada.setLatitud(jsonObject.getDouble("latitud"));
                            parada.setLongitud(jsonObject.getDouble("longitud"));
                            parada.setId_parada(jsonObject.getInt("id_parada"));
                            parada.setReajuste(jsonObject.getInt("reajuste"));
                            parada.setSucursal(false);//hardcode  jsonObject.getBoolean("sucursal")
                            paradasByDestino.add(parada);
                        }
                        if(!paradasByDestino.isEmpty()){
                            initiatePopupWindow(true);
                        }else{
                            new AlertDialog.Builder(BusquedaActivity.this)
                                    .setTitle("Ops!")
                                    .setMessage("No pudimos obtener las paradas")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
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


    private void WSbuscarViajes(String dateFrom, String dateTo, List<Parada> origins, List<Parada> destinations) throws JSONException, TimeoutException, ExecutionException {

        JSONArray jsonArrayOrigenes = getIdByParadas(origins);
        JSONArray jsonArrayDestinos = getIdByParadas(destinations);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("dateFrom",dateFrom);
        jsonBody.put("dateTo",dateTo);
        jsonBody.put("origins",jsonArrayOrigenes);
        jsonBody.put("destinations",jsonArrayDestinos);

        try {
            volley.llamarWSCustomArray(Request.Method.POST,urlSearchTravels, jsonBody,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                if(response.length()>0){
                    Intent intent = new Intent(BusquedaActivity.this, ViajesActivity.class);
                    intent.putExtra("viajesJsonArray", response.toString());
                    startActivity(intent);

                } else {
                    new AlertDialog.Builder(BusquedaActivity.this)
                            .setTitle("Ops!")
                            .setMessage("No disponemos de viajes con estos parámetros")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
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


    private List<Parada> obtenerParadasCercanasApunto(double radio, LatLng centro, List<Parada> paradas){
        List<Parada> paradasCercanas = new ArrayList<Parada>();

        for (Parada item: paradas) {
            float[] results = new float[1];
            Location.distanceBetween(centro.latitude, centro.longitude,
                    item.getLatitud(), item.getLongitud(),
                    results);
            if(results[0]<=radio){
                paradasCercanas.add(item);
            }
        }
        return paradasCercanas;
    }


    private JSONArray getIdByParadas (List<Parada> paradas) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Parada item: paradas) {
            jsonArray.put(item.getId_parada());
        }
        return jsonArray;
    }
}