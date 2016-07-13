package com.example.malladam.AppUsuarios.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.PasajeArrayAdapter;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Asiento;
import com.example.malladam.AppUsuarios.models.GroupPasajeDT;
import com.example.malladam.AppUsuarios.models.Parada;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SelectAsientosActivity extends AppCompatActivity {

    String urlPreBuyTickets;
    String urlAppConfirmTickets;

    private static PayPalConfiguration config = new PayPalConfiguration()
        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId("AcYJ0yuY-V2vxt6XETFzitK6qBADBO9_XEiXZov3iCv-4S4RnCAywVBIAcPfRLsMghjPDz-bZASB_Efz")
    ;
    List<Integer> reservedTickets = new ArrayList<>();
    private VolleyS volley;
    private DataBaseManager dbManager;
    ListView lista;
    PasajeArrayAdapter<GroupPasajeDT> seatsArray;
    static TextView infoAsiento;

    int idViaje;
    int IdLinea;
    int origin;
    int destination;
    int idVehiculo;
    double valor;

    List<Asiento> seats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_asientos);
        dbManager = new DataBaseManager(this);
        urlPreBuyTickets = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.preBuyTickets);
        urlAppConfirmTickets = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.appConfirmTickets);

        Button btn_buy = (Button)findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(dbManager.getUserLogueado() == null)
                {
                    Toast.makeText(SelectAsientosActivity.this, "Debe loguearse para poder comprar pasajes.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    onBuyPressed(v);
                }

            }
        });

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        volley = volley.getInstance(this);
        ;

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
        } catch (ExecutionException e) {
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
        seatsArray = new PasajeArrayAdapter<GroupPasajeDT>(this,pasajesAgrupados, valor, totalView);
        LayoutInflater inflater = this.getLayoutInflater();
        View header = inflater.inflate(R.layout.list_header_row, lista, false);
        lista.addHeaderView(header, null, false);

        lista.setAdapter(seatsArray);
        List<Integer> seats = seatsArray.selectedSeats;
        double totalValue = seatsArray.totalValue;

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
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

    public void onBuyPressed(View pressed)
    {
        try
        {
            ReservTickets();
            PayPalPayment payment = new PayPalPayment(new BigDecimal(seatsArray.totalValue / 30), "USD", "Compra de pasajes", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, 0);
        }
        catch (Exception e)
        {
            Toast.makeText(SelectAsientosActivity.this, "Ha ocurrido un error al adquirir el pasaje. Intenta de nuevo en unos instantes", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null)
            {
                String paymentId = confirm.getProofOfPayment().getPaymentId();
                ConfirmTickets(paymentId);
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(SelectAsientosActivity.this, "Usted ha cancelado la compra de pasajes", Toast.LENGTH_LONG).show();
            //TODO: cancelar reservas
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(SelectAsientosActivity.this, "Ha ocurrido un error inesperado. intente mas tarde.", Toast.LENGTH_LONG).show();
            //TODO: cancelar reservas
        }
    }

    void ReservTickets() throws JSONException, TimeoutException, ExecutionException
    {
        JSONObject jsonBody = new JSONObject();
        try
        {
            JSONArray seatsJSON = getSeatsJSON(seatsArray.selectedSeats);
            jsonBody.put("id_viaje", idViaje);
            jsonBody.put("origen", origin);
            jsonBody.put("destino",destination);
            jsonBody.put("valor", seatsArray.totalValue);
            jsonBody.put("seleccionados", seatsJSON);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        try
        {
            volley.llamarWSCustomArray(Request.Method.POST, urlPreBuyTickets, jsonBody, new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++)
                    {
                        Integer ticketId = null;
                        try
                        {
                            JSONObject ticket = (JSONObject)response.get(i);
                            ticketId = ticket.getInt("id_pasaje");
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        reservedTickets.add(ticketId);
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ",volleyError.toString());
                }
            }, dbManager.getTokenLogueado());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void WSbuscarViajes(String dateFrom, String dateTo, List<Parada> origins, List<Parada> destinations) throws JSONException, TimeoutException, ExecutionException {


    }

    void ConfirmTickets(String paymentId)
    {
        String token = dbManager.getTokenLogueado();
        JSONArray ticketJSON = getSeatsJSON(reservedTickets);
        JSONObject jsonBody = new JSONObject();
        try
        {
            jsonBody.put("id_Pago", paymentId);
            jsonBody.put("tickets", ticketJSON);

            volley.llamarWS(Request.Method.POST, urlAppConfirmTickets, jsonBody, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError)
                {
                    if( volleyError instanceof ParseError)
                    {
                        Intent intent = new Intent(SelectAsientosActivity.this, PasajesActivity.class);
                        Toast.makeText(SelectAsientosActivity.this, "Los pasajes han sido acreditados a su cuenta", Toast.LENGTH_LONG).show();
                        SelectAsientosActivity.this.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SelectAsientosActivity.this, "Lo sentimos. Hubo un problema concretando la compra. Pongase en contacto con la sucursal mas cercana", Toast.LENGTH_LONG).show();
                        Log.d("el ERROR ",volleyError.toString());
                    }
                }
            }, token);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private JSONArray getSeatsJSON(List<Integer> seats)
    {
        JSONArray jsonArray = new JSONArray();
        for (Integer seat: seats)
        {
            jsonArray.put(seat);
        }
        return jsonArray;
    }
}
