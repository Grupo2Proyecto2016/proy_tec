package com.example.malladam.AppGuarda.utils;

/**
 * Created by malladam on 12/07/2016.
 */

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.malladam.AppGuarda.Activity.LoginActivity;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.Parada;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class UbicacionService extends Service {

    private LocationManager locMgr;
    private LocationListener locListener;
    private VolleyS volley;
    private int intentosLogin = 0;
    private DataBaseManager dbManager;
    private Location locationActual;
    private List<Parada> paradasDelViaje = new ArrayList<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d("Servicio creado...", "asd");
    }

/*
    @Override public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Servicio ", "iniciado...");
        dbManager = new DataBaseManager(getApplicationContext());
        paradasDelViaje = dbManager.getParadasDelViaje();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final String id_viaje = String.valueOf(dbManager.getViajeActual().getId_viaje());
        final String urlUbic = intent.getStringExtra("urlUbic");
        final String urlToken = intent.getStringExtra("urlToken");
        final String token = dbManager.getTokenLogueado();

        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                if(dbManager.getViajeActual()!=null){
                    locationActual = location;
                    dbManager.insertarUbicacion(new LatLng(location.getLatitude(), location.getLongitude()));
                    try {
                        WScomunicarLocationChanged(locationActual, id_viaje, urlUbic, token, urlToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    logicaDeParadas(location);
                }
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_REDELIVER_INTENT;
        }
        locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, // minTime in ms
                1, // minDistance in meters
                locListener);

        return START_REDELIVER_INTENT;
    }


    private void WScomunicarLocationChanged(Location location, String id_viaje, final String urlUbic, final String token, final String urlToken) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody  = new JSONObject();
        jsonBody.put("travelId",id_viaje);
        jsonBody.put("lat",location.getLatitude());
        jsonBody.put("lng",location.getLongitude());

        volley = volley.getInstance(this);


        try {
            volley.llamarWS(Request.Method.POST, urlUbic, jsonBody,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("la RESPUESTA ",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ",volleyError.toString());
                    if( volleyError instanceof AuthFailureError) {
                        try {
                            refrescarLogin(dbManager.getUserLogueado(), dbManager.getPassLogueado(), urlToken, urlUbic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        Log.d("Servicio destruido...", "zxc");
        super.onDestroy();
    }

    private void refrescarLogin(final String user, final String pass, final String urlToken, final String urlUbic)throws JSONException, TimeoutException, ExecutionException {

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
                    WScomunicarLocationChanged(locationActual, String.valueOf(dbManager.getViajeActual().getId_viaje()),
                            urlUbic, response.getString("token"), urlToken);
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
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.tokenInvalido), Toast.LENGTH_LONG).show();
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

    private void logicaDeParadas(Location ubicacionActual) {
        int id_proxParada = dbManager.getIdProxParada();
        Parada proximaParada = getParadaById(paradasDelViaje, id_proxParada);
        if(proximaParada != null){
            LatLng latlngProxParada = new LatLng(proximaParada.getLatitud(), proximaParada.getLongitud());
            int distanciaActual_ProxParada = GetDistance(new LatLng(ubicacionActual.getLatitude(), ubicacionActual.getLongitude()),
                    latlngProxParada);
            if(distanciaActual_ProxParada <100 && distanciaActual_ProxParada > 0){
                if(dbManager.liberanAsientosByParadaDestino(id_proxParada) )
                    Toast.makeText(UbicacionService.this, "Descienden pasajeros en "+distanciaActual_ProxParada +" mts", Toast.LENGTH_SHORT).show();
            }
            if(distanciaActual_ProxParada <= dbManager.getDistProxParada() ){ //me sigo acercando a prox parada
                dbManager.setDistProxParada(distanciaActual_ProxParada);
            }
            else if(distanciaActual_ProxParada > dbManager.getDistProxParada()*1.5){
                dbManager.setIdUltParada(id_proxParada);
                proximaParada = getSiguienteParadaById(paradasDelViaje, id_proxParada);
                latlngProxParada = new LatLng(proximaParada.getLatitud(), proximaParada.getLongitud());
                distanciaActual_ProxParada = GetDistance(new LatLng(ubicacionActual.getLatitude(), ubicacionActual.getLongitude()),
                        latlngProxParada);

                dbManager.setIdProxParada(proximaParada.getId_parada());
                dbManager.setDistProxParada(distanciaActual_ProxParada);
                dbManager.eliminarAsientosByParadaDestino(id_proxParada);
            }
        }

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
            int iDistance = distance.getInt("value");// /1000

            return iDistance;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Parada getParadaById(List<Parada> paradas, int id){
        for (Parada item: paradas) {
            if(id == item.getId_parada())
                return  item;
        }
        return null;
    }


    private Parada getSiguienteParadaById(List<Parada> paradas, int id){
        for (int item = 0; item<=paradas.size()-1;item++ ) {
            if(id == paradas.get(item).getId_parada()){
                if(item == paradas.size()-1){
                    return  paradas.get(item);
                }
                return  paradas.get(item+1);
            }
        }
        return paradas.get(paradas.size()-1);//ultima
    }
}