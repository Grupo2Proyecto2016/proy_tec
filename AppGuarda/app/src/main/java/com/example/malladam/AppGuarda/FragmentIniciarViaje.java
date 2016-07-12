package com.example.malladam.AppGuarda;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.malladam.AppGuarda.Activity.LoginActivity;
import com.example.malladam.AppGuarda.Activity.MainActivity;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.Empresa;
import com.example.malladam.AppGuarda.models.ViajeActual;
import com.example.malladam.AppGuarda.utils.UbicacionService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


/**
 * Created by malladam on 07/05/2016.
 */
public class FragmentIniciarViaje extends Fragment {

    Button button;
    private Empresa empresa;
    private DataBaseManager dbManager;
    int intentosLogin = 0;
    private VolleyS volley;
    private String urlGetSigViaje, urlToken,urlStartTravel;
    private ViajeActual viajeActual;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_iniciarviaje, container, false);

        setHasOptionsMenu(true);

        dbManager = new DataBaseManager(getActivity().getApplicationContext());
        empresa = empresa.getInstance();
        urlToken = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.auth);
        urlGetSigViaje = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + getResources().getString(R.string.getSiguienteViaje);
        urlStartTravel = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + getResources().getString(R.string.startTravel);
        volley = volley.getInstance(getActivity().getApplicationContext());


        button = (Button) v.findViewById(R.id.button);

        button.setTextColor(Color.parseColor(empresa.getColorText()));
        button.setBackgroundColor(Color.parseColor(empresa.getColorBack()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtener  sig viaje y pregnto sino aviso
                try {
                    WSgetSigViajeByGuarda(dbManager.getTokenLogueado());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                ///////////////////////////levantar dialog con sigueinte viaje sino dialog sin viaje
                /*try {
                    WSgetSiguienteViaje();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
            }
        });
/*
        FragmentManager manager=getActivity().getSupportFragmentManager();

        PageAdapter adapter=new PageAdapter(manager);

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);*/
        return v;
    }


    private void WSgetSigViajeByGuarda(String tokenUser) throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWS(Request.Method.GET,urlGetSigViaje, null,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                   viajeActual = new ViajeActual();
                    try {
                        viajeActual.setId_viaje(response.getInt("id_viaje"));
                        viajeActual.setInicio(response.getLong("inicio"));
                        viajeActual.setFin(response.getLong("inicio"));
                        viajeActual.setEs_directo(false);
                        viajeActual.setTerminado(response.getBoolean("terminado"));
                        JSONObject linea = response.getJSONObject("linea");
                        viajeActual.setId_linea(linea.getInt("id_linea"));
                        viajeActual.setNumero_linea(linea.getInt("numero"));
                        JSONObject origen_linea = linea.getJSONObject("origen");
                        viajeActual.setOrigen_linea(origen_linea.getString("descripcion"));
                        JSONObject destino_linea = linea.getJSONObject("destino");
                        viajeActual.setDestino_linea(destino_linea.getString("descripcion"));
                        JSONObject vehiculo = response.getJSONObject("vehiculo");
                        viajeActual.setId_vehiculo(vehiculo.getInt("id_vehiculo"));
                        viajeActual.setMatricula_vehiculo(vehiculo.getString("matricula"));
                        viajeActual.setCantAsientos_vehiculo(vehiculo.getInt("cantAsientos"));
                        viajeActual.setCantParados_vehiculo(vehiculo.getInt("cantParados"));
                        viajeActual.setMarca_vehiculo(vehiculo.getString("marca"));
                        viajeActual.setModelo_vehiculo(vehiculo.getString("modelo"));

                        if(viajeActual.getId_viaje()!= null){
                            mostrarDialogo(viajeActual);
                        }else{
                            Toast.makeText(getActivity(), "Error al obtener el sigueinte viaje ", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if( volleyError instanceof AuthFailureError) {
                        try {
                            refrescarLogin(dbManager.getUserLogueado(), dbManager.getPassLogueado());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Usted no tiene viajes asignados próximos a partir", Toast.LENGTH_LONG).show();
                    }
                    Log.d("el ERROR ",volleyError.toString());
                }
            }, tokenUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDialogo(final ViajeActual viajeActual) {

        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.iniciarViaje)+" "+viajeActual.getId_viaje())
                .setMessage("Desea iniciar el viaje correspondiente a la linea "+viajeActual.getNumero_linea()+
                        " desde "+viajeActual.getOrigen_linea()+" hacia "+viajeActual.getDestino_linea()+
                        " previsto para la fecha "+convertirTimestampADateLocal(viajeActual.getInicio())+" ?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.eliminarViaje();
                        dbManager.guardarViajeActual(viajeActual);

                        try {
                            WScomunicarInicioDeViaje(viajeActual.getId_viaje(), dbManager.getTokenLogueado());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


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

    private void refrescarLogin(final String user, final String pass)throws JSONException, TimeoutException, ExecutionException {

        dbManager.eliminarLogin();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", user);
        jsonBody.put("password", pass);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,urlToken,jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    intentosLogin=0;
                    dbManager.registrarLogin(response.getString("token"), user, pass);
                    WSgetSigViajeByGuarda(dbManager.getTokenLogueado());
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
                if(intentosLogin >= 2) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), getResources().getString(R.string.tokenInvalido), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                else intentosLogin++;
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

    private String convertirTimestampADateLocal(long timestamp){

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(tz);
        String localTime = sdf.format(new Date(timestamp ));
        /*CharSequence relTime = DateUtils.getRelativeTimeSpanString(
                timestamp ,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS); DIFERENCIA DE DIAS ENTRE HOY Y TIMESTAMP*/
        return  localTime;
    }


    private void WScomunicarInicioDeViaje(final Integer idViaje, final String token)throws JSONException, TimeoutException, ExecutionException {

        StringRequest strReq = new StringRequest(Request.Method.GET,urlStartTravel+"?travelId="+String.valueOf(idViaje),
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("WSInicioDeViaje OK", response.toString());

                        Toast.makeText(getActivity(), "Viaje iniciado con éxito", Toast.LENGTH_LONG).show();

                        //////////LANZO SERVICIO//////////////
                        MainActivity mainActivity  =(MainActivity) getActivity();
                        Intent mServiceIntent = mainActivity.intentUbicacion;
                        mServiceIntent.putExtra("urlToken", getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + "/auth");
                        mServiceIntent.putExtra("urlUbic", getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + getResources().getString(R.string.setTravelLocation));
                        getActivity().startService(mServiceIntent);
                        //////////LANZO SERVICIO//////////////

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("WSInicioDeViaje ERROR ",volleyError.toString());
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_timeout),Toast.LENGTH_LONG).show();
                    dbManager.eliminarViaje();
                } else{
                    Toast.makeText(getActivity(), "Error al iniciar el viaje",Toast.LENGTH_LONG).show();
                    dbManager.eliminarViaje();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                if (token != null){
                    headers.put("Authorization", token);
                }
                return headers;
            }
        };
        // Adding request to request queue
        volley.addToQueue(strReq);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

}
