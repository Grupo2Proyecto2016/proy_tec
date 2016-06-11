package com.example.malladam.AppUsuarios.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class StartActivity extends AppCompatActivity {

    private ImageView imagen;
    private String urlgetCompany;
    private VolleyS volley;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imagen = (ImageView) findViewById(R.id.start_image);

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon);
        imagen.setImageBitmap(icon);


        //////////EMPRESA CUSTOM///////////
        empresa = empresa.getInstance();
        urlgetCompany = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/getCompany";
        volley = volley.getInstance(this);
        try {
            WSgetCompany();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //////////EMPRESA CUSTOM///////////

    }

    private void WSgetCompany() throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWS(Request.Method.GET,urlgetCompany, null,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        empresa.setNombre(response.getString("nombre"));
                        empresa.setRut(response.getDouble("rut"));
                        empresa.setTelefono(response.getDouble("telefono"));
                        empresa.setDireccion(response.getString("direccion"));
                        empresa.setLogo(response.getString("logo").getBytes());
                        JSONObject pais = response.getJSONObject("pais");
                        empresa.setPais(pais.getString("nombre"));
                        empresa.setRazonSocial(response.getString("razonSocial"));
                        empresa.setLogoS(response.getString("logo"));

                        Intent intent = new Intent(StartActivity.this, BusquedaActivity.class);
                        StartActivity.this.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(StartActivity.this, getResources().getString(R.string.error_conexion) , Toast.LENGTH_LONG).show();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
