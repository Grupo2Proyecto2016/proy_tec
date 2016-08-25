package com.example.malladam.AppUsuarios.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.ListViewViajesAdapter;
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

    private Empresa empresa;
    private List<Viaje> viajesEncontrados = new ArrayList<Viaje>();
    private JSONArray viajesEncontradosJsonArray;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);

        empresa = empresa.getInstance();

        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_viajes);
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


        Intent intent = getIntent();
        String jsonArray = intent.getStringExtra("viajesJsonArray");

        try {
            viajesEncontradosJsonArray = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viajesEncontrados.clear();
        viajesEncontrados = armarListaByJSONArray(viajesEncontradosJsonArray);

        ListViewViajesAdapter adapter = new ListViewViajesAdapter(this, R.layout.listview_viajes_row, viajesEncontrados);
        listView = (ListView) findViewById(R.id.listViewViajes);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Viaje selectedTravel = viajesEncontrados.get(position);
                Intent intent = new Intent(ViajesActivity.this, SelectAsientosActivity.class);
                intent.putExtra("Id_viaje", selectedTravel.getId_viaje());
                intent.putExtra("Id_linea", selectedTravel.getLinea_id_linea());
                intent.putExtra("origin", selectedTravel.getOrigen());
                intent.putExtra("destination", selectedTravel.getDestino());
                intent.putExtra("id_vehiculo", selectedTravel.getVehiculo_id());
                intent.putExtra("valor", selectedTravel.getValor());
                ViajesActivity.this.startActivity(intent);
                }
            }
        );

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivityAfterCleanup(BusquedaActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private String convertirTimestampADateLocal(long timestamp){

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat soloHora = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(tz);
        String localTime = soloHora.format(new Date(timestamp ));
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
                viaje.setLugares(jsonObject.getString("lugares"));
                viaje.setNumero(jsonObject.getInt("numero"));
                viaje.setVehiculo_id(jsonObject.getInt("id_vehiculo"));
                viaje.setOrigen(jsonObject.getInt("origen"));
                viaje.setOrigen_description(jsonObject.getString("origen_description"));
                viaje.setValor(jsonObject.getDouble("valor"));
                viajes.add(viaje);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viajes;
    }
}
