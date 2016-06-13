package com.example.malladam.AppUsuarios.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.IntentIntegrator;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.models.Empresa;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NosotrosActivity extends AppCompatActivity {

    TextView mDescripcion;
    DataBaseManager dbManager;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    Intent intent;
    private ActionBarDrawerToggle mDrawerToggle;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nosotros);

        mDescripcion = (TextView) findViewById(R.id.detalles1nosotros);
        empresa = empresa.getInstance();
        dbManager = new DataBaseManager(this);

        mDescripcion.setText(empresa.getMensaje());
        mDescripcion.setTextColor(Color.parseColor(empresa.getColorText()));


        ///////////ACTIONBAR+NAVIGATION////////////////
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            View headerNavigation = navigationView.getHeaderView(0);
            TextView headerNombre = (TextView) headerNavigation.findViewById(R.id.nombreLogin);
            headerNombre.setText(dbManager.getUserLogueado());

            navigationView.getMenu().clear(); //clear old inflated items.
            if (dbManager.getUserLogueado() == null) {
                navigationView.inflateMenu(R.menu.drawer_logout);
            } else {
                navigationView.inflateMenu(R.menu.drawer_login);
            }
        }

        setupNavigationDrawerContent(navigationView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ///////////ACTIONBAR+NAVIGATION////////////////

    }

    ///////////ACTIONBAR+NAVIGATION////////////////
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.cerrarSesion:
                                menuItem.setChecked(true);
                                dbManager.eliminarLogin();
                                intent = new Intent(getApplicationContext(), BusquedaActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.iniciarSesion:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.registrarse:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                startActivity(intent);
                                finish();
                                return true;

                            case R.id.nosotros:
                                menuItem.setChecked(true);
                                intent = new Intent(getApplicationContext(), NosotrosActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nosotros, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else if (id == R.id.action_sucursales) {
            intent = new Intent(getApplicationContext(), SucursalesActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_info) {
            showPopup(NosotrosActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ///////////ACTIONBAR+NAVIGATION////////////////

    private void showPopup(final Activity context) {
        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        TextView nombre = (TextView) layout.findViewById(R.id.popupNombre);
        TextView razonSocial = (TextView) layout.findViewById(R.id.popupRazonSocial);
        TextView direccion = (TextView) layout.findViewById(R.id.popupDireccion);
        TextView rut = (TextView) layout.findViewById(R.id.popupRut);
        TextView telefono = (TextView) layout.findViewById(R.id.popupTelefono);
        TextView pais = (TextView) layout.findViewById(R.id.popupPais);
        ImageView logo = (ImageView) layout.findViewById(R.id.popupLogo);

        rut.setText(Double.toString(empresa.getRut()));
        nombre.setText(empresa.getNombre());
        razonSocial.setText(empresa.getRazonSocial());
        direccion.setText(empresa.getDireccion());
        telefono.setText(Double.toString(empresa.getTelefono()));
        pais.setText(empresa.getPais());

        String encodedDataString = empresa.getLogoS();

        encodedDataString = encodedDataString.replace("data:image/png;base64,","");

        byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
        logo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}
