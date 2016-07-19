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
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.IntentIntegrator;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.models.Empresa;
import com.example.malladam.AppUsuarios.utils.MenuTintUtils;

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
    Intent intent;
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
        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_nosotros);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));


        ///////////ACTIONBAR+NAVIGATION////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor(empresa.getColorTextHeader()), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        ///////////ACTIONBAR////////////////
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nosotros, menu);
        MenuTintUtils menuTintUtils = new MenuTintUtils();
        menuTintUtils.tintAllIcons(menu,Color.parseColor(empresa.getColorTextHeader()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivityAfterCleanup(BusquedaActivity.class);
                return true;
            case R.id.action_sucursales:
                intent = new Intent(getApplicationContext(), SucursalesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_info:
                showPopup(NosotrosActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
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

}
