package com.example.malladam.AppGuarda.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppGuarda.ManejadorInicio;
import com.example.malladam.AppGuarda.R;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.Empresa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class StartActivity extends AppCompatActivity {

    private ImageView imagen;
    private String urlgetCompany;
    private VolleyS volley;
    private Empresa empresa;
    private View mProgressView;
    private TextView name;
    private ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mProgressView = findViewById(R.id.start_progress);
        imagen = (ImageView) findViewById(R.id.start_image);
        name = (TextView) findViewById(R.id.start_name);
        refresh = (ImageButton) findViewById(R.id.start_refresh);
        refresh.setVisibility(View.GONE);

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icon);
        imagen.setImageBitmap(icon);

        showProgress(true);


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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setVisibility(View.GONE);
                showProgress(true);
                try {
                    WSgetCompany();
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
                        empresa.setColorBack(response.getString("colorBack"));
                        empresa.setColorText(response.getString("colorText"));
                        empresa.setColorHeader(response.getString("colorHeader"));
                        empresa.setColorTextHeader(response.getString("colorTextHeader"));

                        Intent intent = new Intent(StartActivity.this, ManejadorInicio.class);
                        StartActivity.this.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showProgress(false);
                    Toast.makeText(StartActivity.this, getResources().getString(R.string.error_conexion) , Toast.LENGTH_LONG).show();
                    refresh.setVisibility(View.VISIBLE);
                }
            }, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
