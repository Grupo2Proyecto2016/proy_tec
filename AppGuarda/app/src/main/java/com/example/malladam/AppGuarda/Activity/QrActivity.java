package com.example.malladam.AppGuarda.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.IntentIntegrator;
import com.android.IntentResult;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.malladam.AppGuarda.utils.DataBaseManager;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.AsientoActivo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class QrActivity extends Activity {

    private DataBaseManager dbManager;
    private String urlCobrarPasajeLeido, urlToken;
    private VolleyS volley;
    int intentosLogin = 0;
    private String scanContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        volley = volley.getInstance(this);
        dbManager = new DataBaseManager(this);
        urlCobrarPasajeLeido = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.collectTicket);
        urlToken = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/auth";

        IntentIntegrator scanIntegrator;
        scanIntegrator = new IntentIntegrator(QrActivity.this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null ) {
            scanContent = scanningResult.getContents();
            if (scanContent != null) {
                try {
                    WScobrarPasajeLeido(scanContent, String.valueOf(dbManager.getViajeActual().getId_viaje()));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sin lectura de pasaje", Toast.LENGTH_LONG).show();
                Intent intentError = new Intent(QrActivity.this, MainActivity.class);
                QrActivity.this.startActivity(intentError);
            }
        }
    }

    private void WScobrarPasajeLeido(String ticketNumber, String travelId) throws JSONException, TimeoutException, ExecutionException {

        final JSONObject jsonBody  = new JSONObject();
        jsonBody.put("ticketNumber",ticketNumber);
        jsonBody.put("travelId",travelId);

        String token = dbManager.getTokenLogueado();

        try {
            volley.llamarWS(Request.Method.POST,urlCobrarPasajeLeido, jsonBody,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean exito = response.getBoolean("success");
                        String msg = response.getString("msg");
                        Toast.makeText(QrActivity.this, msg, Toast.LENGTH_LONG).show();

                        if(exito){//guardo local el pasaje cobrado
                            AsientoActivo asientoActivo = new AsientoActivo();
                            JSONObject jsonObject = response.getJSONObject("ticket");
                            asientoActivo.setId_pasaje(jsonObject.getInt("id_pasaje"));
                            asientoActivo.setNumero_pasaje(jsonObject.getString("numero"));
                            asientoActivo.setCosto(Float.parseFloat(jsonObject.getString("costo")));

                            try{
                                JSONObject user_cmpra = jsonObject.getJSONObject("user_compra");
                                asientoActivo.setUsername_usuario(user_cmpra.getString("usrname"));
                                asientoActivo.setNombre_usuario(user_cmpra.getString("nombre"));
                                asientoActivo.setApellido_usuario(user_cmpra.getString("apellido"));
                            }
                            catch (JSONException e){
                                asientoActivo.setUsername_usuario("usuarioGenerico");
                                asientoActivo.setNombre_usuario("usuario");
                                asientoActivo.setApellido_usuario("generico");
                            }
                            JSONObject viaje = jsonObject.getJSONObject("viaje");
                            asientoActivo.setId_viaje(viaje.getInt("id_viaje"));
                            JSONObject asiento = jsonObject.getJSONObject("asiento");
                            asientoActivo.setId_asiento(asiento.getInt("id_asiento"));
                            asientoActivo.setNumero_asiento(asiento.getInt("numero"));
                            JSONObject paradaSube = jsonObject.getJSONObject("parada_sube");
                            asientoActivo.setId_paradaSube(paradaSube.getInt("id_parada"));
                            JSONObject paradaBaja = jsonObject.getJSONObject("parada_baja");
                            asientoActivo.setId_paradaBaja(paradaBaja.getInt("id_parada"));

                            dbManager.insertarAsientoActivo(asientoActivo);
                        }

                        Intent intent = new Intent(QrActivity.this, MainActivity.class);
                        QrActivity.this.startActivity(intent);
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
                    }
                    Log.d("el ERROR ",volleyError.toString());
                }
            }, token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                    WScobrarPasajeLeido(scanContent, String.valueOf(dbManager.getViajeActual().getId_viaje()));
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
                    Intent intent = new Intent(QrActivity.this, LoginActivity.class);
                    Toast.makeText(QrActivity.this, getResources().getString(R.string.tokenInvalido), Toast.LENGTH_LONG).show();
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivityAfterCleanup(MainActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
