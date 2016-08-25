package com.example.malladam.AppUsuarios.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;
import com.example.malladam.AppUsuarios.models.Encomienda;
import com.example.malladam.AppUsuarios.models.Parada;
import com.example.malladam.AppUsuarios.models.Pasaje;
import com.example.malladam.AppUsuarios.models.Terminal;
import com.example.malladam.AppUsuarios.utils.DirectionsJSONParser;
import com.example.malladam.AppUsuarios.utils.MenuTintUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class PasajesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Empresa empresa;
    private VolleyS volley;
    private String urlUserPasajes, urlToken, urlGetUbicacionViaje;
    private TextView mOrigenPas,mDestinoPas, mFechaPas, mAsientoPas, mPrecioPas, mEstadoPas;
    private TextView mOrigenPasAUX,mDestinoPasAUX, mFechaPasAUX, mAsientoPasAUX, mPrecioPasAUX, mEstadoPasAUX;
    private TableLayout mTableLayout;
    private TableRow mTableRow;
    private DataBaseManager dbManager;
    private View mProgressView;
    private Handler mHandler;
    private int intentosLogin = 0;
    private PopupWindow pw;
    private GoogleMap mMap;
    private Marker mActualMarker;
    private List<LatLng> ubicParadas = new ArrayList<>();
    private String idViajeActual;
    private LatLng ubicacion = new LatLng(0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajes);

        mTableLayout = (TableLayout) findViewById(R.id.tablePas);
        mTableLayout.setStretchAllColumns(true);
        mOrigenPas = (TextView) findViewById(R.id.tvOrigenPas);
        mDestinoPas = (TextView) findViewById(R.id.tvDestinoPas);
        mFechaPas = (TextView) findViewById(R.id.tvFechaPas);
        mAsientoPas = (TextView) findViewById(R.id.tvAsientoPas);
        mPrecioPas = (TextView) findViewById(R.id.tvPrecioPas);
        mEstadoPas = (TextView) findViewById(R.id.tvEstadoPas);
        mProgressView = findViewById(R.id.enco_progress);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ///////WS///////

        urlUserPasajes = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.userTickets);
        urlGetUbicacionViaje = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.getLastTravelLocation);
        urlToken = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.auth);
        volley = volley.getInstance(this);
        ///////WS///////

        empresa = empresa.getInstance();
        dbManager = new DataBaseManager(this);
        mHandler = new Handler();

        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_pasajes);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));


        mOrigenPas.setTextColor(Color.parseColor(empresa.getColorText()));
        mDestinoPas.setTextColor(Color.parseColor(empresa.getColorText()));
        mFechaPas.setTextColor(Color.parseColor(empresa.getColorText()));
        mEstadoPas.setTextColor(Color.parseColor(empresa.getColorText()));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mAsientoPas.setTextColor(Color.parseColor(empresa.getColorText()));
            mPrecioPas.setTextColor(Color.parseColor(empresa.getColorText()));
        }


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

        try {
            WSgetUserPasajes(dbManager.getTokenLogueado());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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



    private void WSgetUserPasajes(String tokenUser) throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET,urlUserPasajes, null,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    List<Pasaje> pasajes = new ArrayList<Pasaje>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Pasaje pasaje = new Pasaje();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            pasaje.setIdPasaje(jsonObject.getString("id_pasaje"));
                            pasaje.setEstado(jsonObject.getString("estado"));
                            pasaje.setPrecio(Float.parseFloat(jsonObject.getString("costo")));
                            JSONObject viaje = jsonObject.getJSONObject("viaje");
                            pasaje.setIdViaje(viaje.getString("id_viaje"));
                            pasaje.setFecha(viaje.getLong("inicio"));
                            JSONObject paradaSube = jsonObject.getJSONObject("parada_sube");
                            pasaje.setOrigen(paradaSube.getString("descripcion"));
                            JSONObject paradaBaja = jsonObject.getJSONObject("parada_baja");
                            pasaje.setDestino(paradaBaja.getString("descripcion"));
                            JSONObject asiento = jsonObject.getJSONObject("asiento");
                            pasaje.setAsiento(asiento.getString("numero"));
                            pasaje.setNumero(jsonObject.getString("numero"));
                            pasajes.add(pasaje);
                        }
                        mProgressView.setVisibility(View.GONE);
                        if(pasajes.isEmpty()){
                            Toast.makeText(PasajesActivity.this, "Usted no ha adquirido pasajes aún", Toast.LENGTH_LONG).show();
                        }else{
                            cargarGrillaPasajes(pasajes);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if( volleyError instanceof AuthFailureError) {
                        try {
                            refrescarLogin(dbManager.getUserLogueado(), dbManager.getPassLogueado());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("el ERROR ",volleyError.toString());
                }
            }, tokenUser);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void cargarGrillaPasajes(List<Pasaje> pasajes){

        for (Pasaje item: pasajes) {

            mTableRow = new TableRow(this);

            mOrigenPasAUX = new TextView(this);
            mDestinoPasAUX = new TextView(this);
            mFechaPasAUX = new TextView(this);
            mEstadoPasAUX = new TextView(this);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mAsientoPasAUX = new TextView(this);
                mPrecioPasAUX = new TextView(this);

                mAsientoPasAUX.setText(item.getAsiento());
                mPrecioPasAUX.setText(String.valueOf(item.getPrecio()));

                mAsientoPasAUX.setTextColor(Color.parseColor(empresa.getColorText()));
                mPrecioPasAUX.setTextColor(Color.parseColor(empresa.getColorText()));

                mAsientoPasAUX.setTextSize(10);
                mPrecioPasAUX.setTextSize(10);

                mAsientoPasAUX.setGravity(Gravity.CENTER);
                mPrecioPasAUX.setGravity(Gravity.RIGHT);

                mOrigenPasAUX.setText(reducir(item.getOrigen(), 30));
                mDestinoPasAUX.setText(reducir(item.getDestino(), 30));

            }else{
                mOrigenPasAUX.setText(reducir(item.getOrigen(), 20));
                mDestinoPasAUX.setText(reducir(item.getDestino(), 20));
            }

            mFechaPasAUX.setText(convertirTimestampADateLocal(item.getFecha()));

            switch (item.getEstado()){
                case "1":
                    mEstadoPasAUX.setText("Reservado");
                break;
                case "2":
                    mEstadoPasAUX.setText("Comprado");
                break;
                case "3":
                    mEstadoPasAUX.setText("En viaje");
                break;
                case "4":
                    mEstadoPasAUX.setText("Cobrado");
                    break;
                case "5":
                    mEstadoPasAUX.setText("Cancelado");
                    break;
                default:
                    mEstadoPasAUX.setText("");
                break;
            }

            mOrigenPasAUX.setTextSize(10);
            mDestinoPasAUX.setTextSize(10);
            mFechaPasAUX.setTextSize(10);
            mEstadoPasAUX.setTextSize(10);

            mOrigenPasAUX.setGravity(Gravity.LEFT);
            mDestinoPasAUX.setGravity(Gravity.LEFT);
            mFechaPasAUX.setGravity(Gravity.CENTER);
            mEstadoPasAUX.setGravity(Gravity.CENTER);

            mOrigenPasAUX.setTextColor(Color.parseColor(empresa.getColorText()));
            mDestinoPasAUX.setTextColor(Color.parseColor(empresa.getColorText()));
            mFechaPasAUX.setTextColor(Color.parseColor(empresa.getColorText()));
            mEstadoPasAUX.setTextColor(Color.parseColor(empresa.getColorText()));


            mTableRow.addView(mOrigenPasAUX);
            mTableRow.addView(mDestinoPasAUX);
            mTableRow.addView(mFechaPasAUX);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mTableRow.addView(mAsientoPasAUX);
                mTableRow.addView(mPrecioPasAUX);
            }

            if(item.getEstado()=="3") {
                mEstadoPasAUX.setTextColor(Color.BLUE);
                mEstadoPasAUX.setTag(item.getIdViaje());
                mEstadoPasAUX.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String idViaje = (String) v.getTag();
                        try {
                            idViajeActual = idViaje;
                            WSgetUbicacionDelViaje(idViaje, dbManager.getTokenLogueado(), true);
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

            mTableRow.addView(mEstadoPasAUX);

            mTableRow.setTag(item.getNumero());
            mTableRow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String numpasaje =  (String)v.getTag();
                    showPopup(PasajesActivity.this,numpasaje);
                }
            });

            mTableLayout.addView(mTableRow);
        }
    }


    private void refrescarLogin(final String user, final String pass)throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", user);
        jsonBody.put("password", pass);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,urlToken,jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dbManager.registrarLogin(response.getString("token"), user, pass);
                    WSgetUserPasajes(dbManager.getTokenLogueado());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("el ERROR del token es ", volleyError.toString());

                Intent intent = new Intent(PasajesActivity.this, LoginActivity.class);
                Toast.makeText(PasajesActivity.this, getResources().getString(R.string.tokenInvalido), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                mHandler.post(new Runnable() {
                    @Override
                    // this will run on the main thread.
                    public void run() {
                    }
                });
                return super.parseNetworkResponse(response);
            }
        };
        volley.addToQueue(request);
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


    private void showPopup(final Activity context, String numpasaje)
    {
        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_qr);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_qr, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ImageView imagen_qr = (ImageView) layout.findViewById(R.id.img_qr_pasaje);
        try {
            Bitmap bitmap = encodeAsBitmap(numpasaje);
            imagen_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Button btn_qr = (Button) layout.findViewById(R.id.close);
        btn_qr.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        int WHITE = 0xFFFFFFFF;
        int BLACK = 0xFF000000;
        int WIDTH = 400;
        int HEIGHT = 400;

        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }


    private String reducir(String cadena, int tamanio){
        String retorno=cadena;
        if (cadena.length() > tamanio){
            retorno = cadena.substring(0,tamanio-3).concat("...");
        }
        return retorno;
    }


    private void showPopupUbicacion(final Activity context, LatLng ubic, List<LatLng> ubicParadas)
    {
        /*
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popupMaps);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_maps, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        */

        LayoutInflater inflater = (LayoutInflater) PasajesActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_maps, (ViewGroup) findViewById(R.id.popupMaps));
        Display display = getWindowManager().getDefaultDisplay();

        pw = new PopupWindow(layout, display.getWidth() - 120, display.getHeight() - 120);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPopup)).getMapAsync(this);

        this.ubicParadas = ubicParadas;

        Button buttonOk=(Button)layout.findViewById(R.id.maps_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapPopup);
                if (f != null)
                    getSupportFragmentManager().beginTransaction().remove(f).commit();
                pw.dismiss();
            }
        });
        //////////////////
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }


    private void WSgetUbicacionDelViaje(final String idViaje, final String token, final Boolean todo) throws JSONException, TimeoutException, ExecutionException {

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, urlGetUbicacionViaje + "?travelId=" + String.valueOf(idViaje),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WSubicacionDelViaje OK", response.toString());
                        if(todo) {
                            List<LatLng> ubicParadas = new ArrayList<LatLng>();
                            try {
                                JSONObject viaje = response.getJSONObject("viaje");
                                JSONObject linea = viaje.getJSONObject("linea");
                                JSONArray paradas = linea.getJSONArray("paradas");

                                for (int i = 0; i < paradas.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) paradas.get(i);
                                    LatLng ubicParada = new LatLng(jsonObject.getDouble("latitud"), jsonObject.getDouble("longitud"));
                                    ubicParadas.add(ubicParada);
                                }

                                ubicacion = new LatLng(response.getDouble("latitud"), response.getDouble("longitud"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            showPopupUbicacion(PasajesActivity.this,ubicacion, ubicParadas);
                        }
                        else{
                            try {
                                ubicacion = new LatLng(response.getDouble("latitud"), response.getDouble("longitud"));
                                actualizarUbicacionMapa();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    Log.d("UbicacionViaje ERROR ", volleyError.toString());
                    if( volleyError instanceof AuthFailureError) {
                        refrescarLoginGetUbicacionDelViaje(dbManager.getUserLogueado(), dbManager.getPassLogueado(), idViaje, todo);
                    } else if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                        Toast.makeText(PasajesActivity.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PasajesActivity.this, "Error al obtener la ubicacion del bus", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                if (token != null) {
                    headers.put("Authorization", token);
                }
                return headers;
            }
        };
        // Adding request to request queue
        volley.addToQueue(jsonReq);
    }


    private void refrescarLoginGetUbicacionDelViaje(final String user, final String pass, final String idViaje, final Boolean todo) throws JSONException, TimeoutException, ExecutionException {

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", user);
        jsonBody.put("password", pass);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlToken, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    intentosLogin = 0;
                    dbManager.registrarLogin(response.getString("token"), user, pass);
                    WSgetUbicacionDelViaje(idViaje, response.getString("token"), todo);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("el ERROR del token es ", volleyError.toString());
                if (intentosLogin >= 2) {
                    Intent intent = new Intent(PasajesActivity.this, LoginActivity.class);
                    Toast.makeText(PasajesActivity.this, getResources().getString(R.string.tokenInvalido), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else intentosLogin++;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        volley.addToQueue(request);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mActualMarker != null){
            mActualMarker.remove();
        }
        mActualMarker = mMap.addMarker(new MarkerOptions()
                .position(ubicacion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.position_indicator)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 13.0f));

        if(ubicParadas.size() >= 2) {
            LatLng origin = ubicParadas.get(0);
            LatLng dest = ubicParadas.get(ubicParadas.size() - 1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

        ActualizarUbicacion actualizarUbicacion = new ActualizarUbicacion();
        actualizarUbicacion.execute();
    }

        //comienza auxiliares dibujar ruta
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=1;i<ubicParadas.size()-1;i++){
            LatLng point  = ubicParadas.get(i);
            if(i==1)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){

        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.parseColor("#05b1fb"));
                lineOptions.geodesic(true);
            }

            mMap.addPolyline(lineOptions);
        }
    }
    //fin auxiliares dibijar ruta


    private class ActualizarUbicacion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            try{
                while(true) {
                    WSgetUbicacionDelViaje(idViajeActual, dbManager.getTokenLogueado(), false);
                    Thread.sleep(5000);
                }
            }catch(Exception e){
            }
            return "";
        }
    }


    public void actualizarUbicacionMapa() {

        if(mActualMarker != null){
            mActualMarker.remove();
        }
        mActualMarker = mMap.addMarker(new MarkerOptions()
                .position(ubicacion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.position_indicator)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, mMap.getCameraPosition().zoom));
    }
}
