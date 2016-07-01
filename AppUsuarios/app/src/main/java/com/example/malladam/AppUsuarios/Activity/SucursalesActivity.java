package com.example.malladam.AppUsuarios.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import com.example.malladam.AppUsuarios.adapters.PopupAdapter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;
import com.example.malladam.AppUsuarios.models.Sucursal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SucursalesActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    Intent intent;
    private GoogleMap mMap;
    private VolleyS volley;
    private String urlgetSucursales;
    List<Sucursal> sucursales = new ArrayList<Sucursal>();
    private ActionBarDrawerToggle mDrawerToggle;
    private DataBaseManager dbManager;
    private Empresa empresa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        dbManager = new DataBaseManager(this);
        empresa = empresa.getInstance();
        ///////WS/////
        urlgetSucursales = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/getBranches";
        volley = volley.getInstance(this);
        //////WS/////////////

        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_sucursales);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));

        try {
            WSgetSucursales();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


///////////ACTIONBAR////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        setSupportActionBar(toolbar);

        // Set the drawer toggle as the DrawerListener
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        ///////////ACTIONBAR////////////////
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (final Sucursal item : sucursales) {
            LatLng suc = new LatLng(item.getLatitud(), item.getLongitud());
            mMap.addMarker(new MarkerOptions()
                    .position(suc)
                    .title(item.getNombre())
                    .snippet(item.getDireccion()+":"+item.getTelefono()+":"+item.getMail()));
            mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sucursales.get(0).getLatitud(),sucursales.get(0).getLongitud()), 10.0f));
        mMap.setOnInfoWindowClickListener(this);
    }

    private void WSgetSucursales() throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET,urlgetSucursales, null,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Sucursal sucursal = new Sucursal();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            sucursal.setAddTerminal(jsonObject.getBoolean("addTerminal"));
                            sucursal.setHasTerminal(jsonObject.getBoolean("hasTerminal"));
                            sucursal.setDireccion(jsonObject.getString("direccion"));
                            sucursal.setId_sucursal(jsonObject.getInt("id_sucursal"));
                            sucursal.setLatitud(jsonObject.getDouble("latitud"));
                            sucursal.setLongitud(jsonObject.getDouble("longitud"));
                            sucursal.setMail(jsonObject.getString("mail"));
                            sucursal.setTelefono(jsonObject.getInt("telefono"));
                            sucursal.setNombre(jsonObject.getString("nombre"));
                            sucursales.add(sucursal);
                        }
                    if(!sucursales.isEmpty()){
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(SucursalesActivity.this);
                    }else{
                        new AlertDialog.Builder(SucursalesActivity.this)
                                .setTitle("Ops!")
                                .setMessage("No pudimos mostrar nuestras sucursales")
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

    ///////////ACTIONBAR////////////////
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivityAfterCleanup(NosotrosActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }
    ///////////ACTIONBAR////////////////

}
