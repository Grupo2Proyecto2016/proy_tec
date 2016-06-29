package com.example.malladam.AppUsuarios.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;
import com.example.malladam.AppUsuarios.models.Terminal;
import com.example.malladam.AppUsuarios.utils.MenuTintUtils;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class EncomiendasActivity extends AppCompatActivity {

    private Empresa empresa;
    Spinner spinnerOrigen;
    Spinner spinnerDestino;
    EditText mAlto, mLargo, mAncho, mPeso;
    TextView mPrecio, mTextPrecio;
    List listTerminales;
    ArrayAdapter arrayAdapter;
    private VolleyS volley;
    private String urlgetTerminales,urlCalcPackage;
    List<Terminal> terminales = new ArrayList<Terminal>();
    LatLng latLngOrigen, latLngDestino;
    Integer distanceEnc;
    float volumeEnc;
    String alto, largo, ancho, peso;
    View focusView = mPeso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomiendas);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ///////WS///////
        urlgetTerminales = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.getTerminals);
        urlCalcPackage = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+getResources().getString(R.string.calcPackage);
        volley = volley.getInstance(this);
        ///////WS///////


        empresa = empresa.getInstance();
        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_encomiendas);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));

        ///////////ACTIONBAR+NAVIGATION////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
        ///////////ACTIONBAR////////////////

        try {
            WSgetTerminales();
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
        getMenuInflater().inflate(R.menu.menu_encomiendas, menu);
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
            case R.id.action_calculador:
                showPopup(EncomiendasActivity.this);
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

    private void showPopup(final Activity context) {
        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_calculadora, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mAlto = (EditText) layout.findViewById(R.id.popupAlto);
        mAncho = (EditText) layout.findViewById(R.id.popupAncho);
        mLargo = (EditText) layout.findViewById(R.id.popupLargo);
        mPeso = (EditText) layout.findViewById(R.id.popupPeso);
        mPrecio = (TextView) layout.findViewById(R.id.popupPrecio);
        mTextPrecio = (TextView) layout.findViewById(R.id.textViewPrecio);

        mAlto.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mAncho.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mLargo.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mPeso.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        spinnerOrigen = (Spinner) layout.findViewById(R.id.spinnerOrigen);
        spinnerDestino = (Spinner) layout.findViewById(R.id.spinnerDestino);
        arrayAdapter = new ArrayAdapter(EncomiendasActivity.this, android.R.layout.simple_dropdown_item_1line, listTerminales);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(listTerminales != null){
            spinnerOrigen.setAdapter(arrayAdapter);
            spinnerDestino.setAdapter(arrayAdapter);
        }

        spinnerOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                latLngOrigen = obtenerLatLngByTerminal(String.valueOf(spinnerOrigen.getSelectedItem()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                latLngDestino = obtenerLatLngByTerminal(String.valueOf(spinnerDestino.getSelectedItem()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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

        Button calcular = (Button) layout.findViewById(R.id.calcular);
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alto = mAlto.getText().toString();
                ancho = mAncho.getText().toString();
                largo = mLargo.getText().toString();
                peso = mPeso.getText().toString();

                if (TextUtils.isEmpty(alto)) {
                    alto = "1";
                }
                if (TextUtils.isEmpty(ancho)) {
                    ancho = "1";
                }
                if (TextUtils.isEmpty(largo)) {
                    largo = "1";
                }
                if (TextUtils.isEmpty(peso)) {
                    peso = "1";
                }

/*
                boolean validacion = true;
                if (TextUtils.isEmpty(alto)) {
                    mAlto.setError(getString(R.string.error_field_required));
                    focusView = mAlto;
                    validacion = false;
                }
                if (TextUtils.isEmpty(ancho)) {
                    mAncho.setError(getString(R.string.error_field_required));
                    focusView = mAncho;
                    validacion = false;
                }
                if (TextUtils.isEmpty(largo)) {
                    mLargo.setError(getString(R.string.error_field_required));
                    focusView = mLargo;
                    validacion = false;
                }
                if (TextUtils.isEmpty(peso)) {
                    mPeso.setError(getString(R.string.error_field_required));
                    focusView = mPeso;
                    validacion = false;
                }
                if (validacion)*/
                    calcularPrecioEncomienda(Float.parseFloat(largo), Float.parseFloat(ancho),Float.parseFloat(alto), Float.parseFloat(peso));
            }
        });
    }

    private void WSgetTerminales() throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWSarray(Request.Method.GET,urlgetTerminales, null,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Terminal terminal = new Terminal();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            terminal.setReajuste(jsonObject.getInt("reajuste"));
                            terminal.setEs_terminal(jsonObject.getBoolean("es_terminal"));
                            terminal.setDireccion(jsonObject.getString("direccion"));
                            terminal.setId_parada(jsonObject.getInt("id_parada"));
                            terminal.setLatitud(jsonObject.getDouble("latitud"));
                            terminal.setLongitud(jsonObject.getDouble("longitud"));
                            terminal.setDescripcion(jsonObject.getString("descripcion"));
                            terminal.setSucursal(false);
                            terminal.setEs_peaje(false);
                            terminales.add(terminal);
                        }
                        if(terminales.isEmpty()){
                            Toast.makeText(EncomiendasActivity.this, "Error al obtener las terminales desponibles", Toast.LENGTH_LONG).show();
                        }else{
                            listTerminales=covertirTerminalesAlistaSpinner(terminales);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
              }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ",volleyError.toString());
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<String> covertirTerminalesAlistaSpinner(List<Terminal> terminales) {
        List<String> termStr = new ArrayList<>();
        for (Terminal term: terminales) {
            termStr.add(term.getDescripcion());
        }
        return termStr;
    }

    private float calcularPrecioEncomienda(float largo, float ancho, float alto, float peso){
        distanceEnc = null;
        while(distanceEnc == null){
            distanceEnc = GetDistance(latLngOrigen,latLngDestino);
        }

        volumeEnc = (largo*ancho*alto) / 1000000;

        JSONObject jsonBody  = new JSONObject();
        try {
            jsonBody.put("weigth",peso);
            jsonBody.put("volume",volumeEnc);
            jsonBody.put("distance",distanceEnc);

            volley = volley.getInstance(this);

            volley.llamarWString(Request.Method.POST,urlCalcPackage,jsonBody,new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    mPrecio.setTextColor(Color.GREEN);
                    mPrecio.setText(response);
                    mTextPrecio.setVisibility(View.VISIBLE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    Log.d("el ERROR es: ",volleyError.toString());
                }
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private LatLng obtenerLatLngByTerminal(String terminal){
        LatLng latLng = new LatLng(0,0);
        for (Terminal term: terminales) {
            if (term.getDescripcion().equals(terminal)) {
                latLng = new LatLng(term.getLatitud(), term.getLongitud());
            }
        }
        return latLng;
    }


    private int GetDistance(LatLng src, LatLng dest) {

        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps/api/directions/json?");
        urlString.append("origin=");//from
        urlString.append( Double.toString(src.latitude));
        urlString.append(",");
        urlString.append( Double.toString(src.longitude));
        urlString.append("&destination=");//to
        urlString.append( Double.toString(dest.latitude));
        urlString.append(",");
        urlString.append( Double.toString(dest.longitude));
        urlString.append("&mode=driving&sensor=true");
        Log.d("xxx","URL="+urlString.toString());

        // get the JSON And parse it to get the directions data.
        HttpURLConnection urlConnection= null;
        URL url = null;

        try {
            url = new URL(urlString.toString());

            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

            String temp, response = "";
            while((temp = bReader.readLine()) != null){
                //Parse data
                response += temp;
            }

            //Close the reader, stream & connection
            bReader.close();
            inStream.close();
            urlConnection.disconnect();

            //Sortout JSONresponse
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray array = object.getJSONArray("routes");
            //Log.d("JSON","array: "+array.toString());

            //Routes is a combination of objects and arrays
            JSONObject routes = array.getJSONObject(0);
            //Log.d("JSON","routes: "+routes.toString());

            //String summary = routes.getString("summary");
            //Log.d("JSON","summary: "+summary);

            JSONArray legs = routes.getJSONArray("legs");
            //Log.d("JSON","legs: "+legs.toString());

            JSONObject steps = legs.getJSONObject(0);
            //Log.d("JSON","steps: "+steps.toString());

            JSONObject distance = steps.getJSONObject("distance");
            //Log.d("JSON","distance: "+distance.toString());

            //String sDistance = distance.getString("text");
            int iDistance = distance.getInt("value")/1000;

            return iDistance;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
