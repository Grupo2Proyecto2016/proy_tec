package com.example.malladam.AppUsuarios.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.PasajeArrayAdapter;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Asiento;
import com.example.malladam.AppUsuarios.models.GroupPasajeDT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SelectAsientosActivity extends AppCompatActivity {

    private VolleyS volley;
    private DataBaseManager dbManager;
    ListView lista;
    PasajeArrayAdapter<GroupPasajeDT> adaptador;
    int asientosBus = 44;
    static TextView infoAsiento;

    int idViaje;
    int IdLinea;
    int origin;
    int destination;
    int idVehiculo;
    double valor;

    List<Asiento> seats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_asientos);

        volley = volley.getInstance(this);
        dbManager = new DataBaseManager(this);

        idViaje = getIntent().getIntExtra("Id_viaje", 0);
        IdLinea = getIntent().getIntExtra("Id_linea", 0);
        origin = getIntent().getIntExtra("origin", 0);
        destination = getIntent().getIntExtra("destination", 0);
        idVehiculo = getIntent().getIntExtra("id_vehiculo", 0);
        valor = getIntent().getDoubleExtra("valor", 0);

        String url = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/getSeats";

        try
        {
            PedirFafa(url);
        } catch (JSONException e)
        {
            e.printStackTrace();
        } catch (TimeoutException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    private void DrawSeats()
    {
        List<GroupPasajeDT> pasajesAgrupados = new ArrayList<>();

        for (int item = 0; item < seats.size();)
        {
            GroupPasajeDT grupoPasajes = new GroupPasajeDT();
            for (int itemInterno = 0; itemInterno < 4; itemInterno++)
            {
                if(item < seats.size())
                {
                    switch (itemInterno) {
                        case 0:
                            grupoPasajes.setVentana1(seats.get(item));
                            break;
                        case 1:
                            grupoPasajes.setPasillo1(seats.get(item));
                            break;
                        case 2:
                            grupoPasajes.setPasillo2(seats.get(item));
                            break;
                        case 3:
                            grupoPasajes.setVentana2(seats.get(item));
                            break;
                    }
                }
                item++;
            }
            pasajesAgrupados.add(grupoPasajes);
        }


        TextView totalView = (TextView)findViewById(R.id.infoAsiento);
        lista = (ListView)findViewById(R.id.list_asientos);
        adaptador = new PasajeArrayAdapter<GroupPasajeDT>(this,pasajesAgrupados, valor, totalView);
        LayoutInflater inflater = this.getLayoutInflater();
        View header = inflater.inflate(R.layout.list_header_row, lista, false);
        lista.addHeaderView(header, null, false);

        lista.setAdapter(adaptador);
        List<Integer> seats = adaptador.selectedSeats;
        double totalValue = adaptador.totalValue;
    }

    private void PedirFafa(String url) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id_viaje", idViaje);
        jsonBody.put("id_linea",IdLinea);
        jsonBody.put("origen", origin);
        jsonBody.put("destino",destination);
        jsonBody.put("id_vehiculo",idVehiculo);

        try {
            volley.llamarWSCustomArray(Request.Method.POST, url, jsonBody,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response)
                {
                    try
                    {
                        for(int i = 0; i < response.length(); i++)
                        {
                            Asiento seat = new Asiento();
                            JSONObject seatJson = (JSONObject)response.get(i);

                            seat.id_asiento = seatJson.getLong("id_asiento");
                            seat.numero = seatJson.getInt("numero");
                            seat.es_accesible = seatJson.getBoolean("es_accesible");
                            seat.es_ventana = seatJson.getBoolean("es_ventana");
                            seat.habilitado = seatJson.getBoolean("habilitado");
                            seat.reservado = seatJson.getBoolean("reservado");

                            seats.add(seat);
                        }
                        DrawSeats();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    Log.d("el ERROR del token es ",volleyError.toString());
                }
            }, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void desplegarInfo(int asiento){

        //obtener los datos segun el asiento
        infoAsiento.setBackgroundResource(R.color.backMenu);
        infoAsiento.setText(String.valueOf(asiento));
    }

}
