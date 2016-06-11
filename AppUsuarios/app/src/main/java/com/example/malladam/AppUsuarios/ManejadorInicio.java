package com.example.malladam.AppUsuarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.Activity.LoginActivity;
import com.example.malladam.AppUsuarios.Activity.MainActivity;
import com.example.malladam.AppUsuarios.adapters.VolleyS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ManejadorInicio extends AppCompatActivity {

    DataBaseManager dbManager;
    String urlgetCompany;
    private VolleyS volley;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        dbManager = new DataBaseManager(this);




        String logueado = dbManager.getUserLogueado();
        if (logueado != null) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
