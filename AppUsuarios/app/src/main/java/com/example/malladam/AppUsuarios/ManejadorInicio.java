package com.example.malladam.AppUsuarios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.malladam.AppUsuarios.Activity.LoginActivity;
import com.example.malladam.AppUsuarios.Activity.MainActivity;

public class ManejadorInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        DataBaseManager dbManager = new DataBaseManager(this);
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
