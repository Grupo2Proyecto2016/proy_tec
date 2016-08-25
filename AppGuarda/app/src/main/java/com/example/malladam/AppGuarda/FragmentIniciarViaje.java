package com.example.malladam.AppGuarda;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.example.malladam.AppGuarda.models.Parada;
import com.example.malladam.AppGuarda.models.ViajeActual;
import com.example.malladam.AppGuarda.utils.DataBaseManager;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private List<Parada> paradasDelViaje= new ArrayList<>();
    private String urlGetSigViaje, urlToken,urlStartTravel;
    private ViajeActual viajeActual;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_iniciarviaje, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                        viajeActual.setCantParados_vehiculo(0);//viajeActual.setCantParados_vehiculo(vehiculo.getInt("cantParados"));
                        viajeActual.setMarca_vehiculo(vehiculo.getString("marca"));
                        viajeActual.setModelo_vehiculo(vehiculo.getString("modelo"));
                        JSONArray paradas = linea.getJSONArray("paradas");
                        for (int p = 0; p < paradas.length(); p++) {
                            Parada parada = new Parada();
                            JSONObject jsonObject = (JSONObject) paradas.get(p);
                            parada.setDescripcion(jsonObject.getString("descripcion"));
                            parada.setEs_terminal(jsonObject.getBoolean("es_terminal"));
                            parada.setDireccion(jsonObject.getString("direccion"));
                            parada.setId_parada(jsonObject.getInt("id_parada"));
                            parada.setLatitud(jsonObject.getDouble("latitud"));
                            parada.setLongitud(jsonObject.getDouble("longitud"));
                            paradasDelViaje.add(parada);
                        }

                        if (viajeActual.getId_viaje() != null) {
                            mostrarDialogo(viajeActual, paradasDelViaje);
                        } else {
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

    private void mostrarDialogo(final ViajeActual viajeActual, final List<Parada> paradasDelViaje) {

        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.iniciarViaje)+" "+viajeActual.getId_viaje())
                .setMessage("Desea iniciar el viaje correspondiente a la linea "+viajeActual.getNumero_linea()+
                        " desde "+viajeActual.getOrigen_linea()+" hacia "+viajeActual.getDestino_linea()+
                        " previsto para la fecha "+convertirTimestampADateLocal(viajeActual.getInicio())+" ?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

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

                        dbManager.guardarViajeActual(viajeActual);
                        dbManager.insertarParadasDelViaje(paradasDelViaje);
                        dbManager.insertarLogicaParadas(paradasDelViaje.get(0).getId_parada(), paradasDelViaje.get(1).getId_parada(),
                                GetDistance(new LatLng(paradasDelViaje.get(0).getLatitud(), paradasDelViaje.get(0).getLongitud()),
                                        new LatLng(paradasDelViaje.get(1).getLatitud(), paradasDelViaje.get(1).getLongitud())));

                        //debug
                        int ult = dbManager.getIdUltParada();
                        int prox = dbManager.getIdProxParada();
                        double dist= dbManager.getDistProxParada();
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
                } else{
                    Toast.makeText(getActivity(), "Error al iniciar el viaje",Toast.LENGTH_LONG).show();
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
        menu.clear();
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    private int GetDistance(LatLng src, LatLng dest) {

        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps/api/directions/json?");
        urlString.append("origin=");//from
        urlString.append( Double.toString(src.latitude));
        urlString.append(",");
        urlString.append( Double.toString(src.longitude));
        urlString.append("&destination=");//to
        urlString.append( Double.toString(dest.latitude));
        urlString.append(",");
        urlString.append( Double.toString(dest.longitude));
        urlString.append("&mode=driving&sensor=true");
        Log.d("xxx","URL="+urlString.toString());

        // get the JSON And parse it to get the directions data.
        HttpURLConnection urlConnection= null;
        URL url = null;

        try {
            url = new URL(urlString.toString());

            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

            String temp, response = "";
            while((temp = bReader.readLine()) != null){
                //Parse data
                response += temp;
            }

            //Close the reader, stream & connection
            bReader.close();
            inStream.close();
            urlConnection.disconnect();

            //Sortout JSONresponse
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray array = object.getJSONArray("routes");
            //Log.d("JSON","array: "+array.toString());

            //Routes is a combination of objects and arrays
            JSONObject routes = array.getJSONObject(0);
            //Log.d("JSON","routes: "+routes.toString());

            //String summary = routes.getString("summary");
            //Log.d("JSON","summary: "+summary);

            JSONArray legs = routes.getJSONArray("legs");
            //Log.d("JSON","legs: "+legs.toString());

            JSONObject steps = legs.getJSONObject(0);
            //Log.d("JSON","steps: "+steps.toString());

            JSONObject distance = steps.getJSONObject("distance");
            //Log.d("JSON","distance: "+distance.toString());

            //String sDistance = distance.getString("text");
            int iDistance = distance.getInt("value");

            return iDistance;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
