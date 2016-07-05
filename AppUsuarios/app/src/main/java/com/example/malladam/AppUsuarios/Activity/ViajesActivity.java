package com.example.malladam.AppUsuarios.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.models.Empresa;
import com.example.malladam.AppUsuarios.models.Viaje;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ViajesActivity extends AppCompatActivity {

    private TextView mLineaVia,mOrigenVia,mDestinoVia, mSalidaVia;
    private TextView mLineaViaAUX,mOrigenViaAUX,mDestinoViaAUX, mSalidaViaAUX, mTiempoViaAUX;
    private ImageView mTiempoVia;
    private TableLayout mTableLayout;
    private TableRow mTableRow;
    private Empresa empresa;
    private List<Viaje> viajesEncontrados = new ArrayList<Viaje>();
    private JSONArray viajesEncontradosJsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);

        mTableLayout = (TableLayout) findViewById(R.id.tableViaj);
        mTableLayout.setStretchAllColumns(true);
        mLineaVia = (TextView) findViewById(R.id.tvLineaViaj);
        mOrigenVia = (TextView) findViewById(R.id.tvOrigenViaj);
        mDestinoVia = (TextView) findViewById(R.id.tvDestinoViaj);
        mSalidaVia = (TextView) findViewById(R.id.tvSalidaViaj);
        mTiempoVia = (ImageView) findViewById(R.id.tvTiempoViaj);

        empresa = empresa.getInstance();

        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_viajes);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));

        mLineaVia.setTextColor(Color.parseColor(empresa.getColorText()));
        mOrigenVia.setTextColor(Color.parseColor(empresa.getColorText()));
        mDestinoVia.setTextColor(Color.parseColor(empresa.getColorText()));
        mSalidaVia.setTextColor(Color.parseColor(empresa.getColorText()));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            int color = Color.parseColor(empresa.getColorText());
            mTiempoVia.setColorFilter(color);
        }


        ///////////ACTIONBAR+NAVIGATION////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        ///////////ACTIONBAR////////////////




        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("viajesJsonArray");

        try {
            viajesEncontradosJsonArray = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        viajesEncontrados.clear();
        viajesEncontrados = armarListaByJSONArray(viajesEncontradosJsonArray);

        cargarGrillaViajes(viajesEncontrados);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivityAfterCleanup(BusquedaActivity.class);
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


    private void cargarGrillaViajes(List<Viaje> viajes){

        for (Viaje item: viajes) {
            mTableRow = new TableRow(this);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mTiempoViaAUX = new TextView(this);

                mTiempoViaAUX.setText("02:15");
                mTiempoViaAUX.setTextColor(Color.parseColor(empresa.getColorText()));
                mTiempoViaAUX.setGravity(Gravity.CENTER);
            }

            mLineaViaAUX = new TextView(this);
            mOrigenViaAUX = new TextView(this);
            mDestinoViaAUX = new TextView(this);
            mSalidaViaAUX = new TextView(this);

            mLineaViaAUX.setText(String.valueOf(item.getNumero()));
            mOrigenViaAUX.setText(item.getOrigen_description());
            mDestinoViaAUX.setText(item.getDestino_description());
            mSalidaViaAUX.setText(convertirTimestampADateLocal(item.getInicio()));

            mLineaViaAUX.setGravity(Gravity.CENTER);
            mOrigenViaAUX.setGravity(Gravity.CENTER);
            mDestinoViaAUX.setGravity(Gravity.CENTER);
            mSalidaViaAUX.setGravity(Gravity.RIGHT);

            mLineaViaAUX.setTextColor(Color.parseColor(empresa.getColorText()));
            mOrigenViaAUX.setTextColor(Color.parseColor(empresa.getColorText()));
            mDestinoViaAUX.setTextColor(Color.parseColor(empresa.getColorText()));
            mSalidaViaAUX.setTextColor(Color.parseColor(empresa.getColorText()));


            mTableRow.addView(mLineaViaAUX);
            mTableRow.addView(mOrigenViaAUX);
            mTableRow.addView(mDestinoViaAUX);
            mTableRow.addView(mSalidaViaAUX);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mTableRow.addView(mTiempoViaAUX);
            }

            mTableLayout.addView(mTableRow);
        }
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

    private List<Viaje> armarListaByJSONArray(JSONArray jsonArray) {
        List<Viaje> viajes = new ArrayList<Viaje>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Viaje viaje = new Viaje();
                JSONObject jsonObject = null;

                jsonObject = (JSONObject) jsonArray.get(i);
                viaje.setDestino(jsonObject.getInt("destino"));
                viaje.setDestino_description(jsonObject.getString("destino_description"));
                viaje.setId_viaje(jsonObject.getInt("id_viaje"));
                viaje.setInicio(Long.parseLong(jsonObject.getString("inicio")));
                viaje.setLinea_id_linea(jsonObject.getInt("linea_id_linea"));
                viaje.setLugares(jsonObject.getInt("lugares"));
                viaje.setNumero(jsonObject.getInt("numero"));
                viaje.setOrigen(jsonObject.getInt("origen"));
                viaje.setOrigen_description(jsonObject.getString("origen_description"));
                viajes.add(viaje);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viajes;
    }
}
