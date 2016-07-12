package com.example.malladam.AppGuarda.utils;

/**
 * Created by malladam on 12/07/2016.
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.malladam.AppGuarda.Activity.LoginActivity;
import com.example.malladam.AppGuarda.DataBaseManager;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
    private boolean status;

    private static UbicacionService sInstance;


    public static synchronized UbicacionService getInstance() {
        if (sInstance == null) {
            sInstance = new UbicacionService();
        }
        return sInstance;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d("Servicio creado...", "asd");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "Servicio iniciado...");
        dbManager = new DataBaseManager(getApplicationContext());

        final String id_viaje = String.valueOf(dbManager.getViajeActual().getId_viaje());
        final String urlUbic = intent.getStringExtra("urlUbic");
        final String urlToken = intent.getStringExtra("urlToken");
        final String token = dbManager.getTokenLogueado();

        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                locationActual = location;
                try {
                    WScomunicarLocationChanged(locationActual, id_viaje, urlUbic, token, urlToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
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
                10, // minDistance in meters
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

}