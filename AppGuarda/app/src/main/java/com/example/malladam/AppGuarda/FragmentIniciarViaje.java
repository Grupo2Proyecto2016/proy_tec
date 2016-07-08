package com.example.malladam.AppGuarda;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.malladam.AppGuarda.Activity.MainActivity;
import com.example.malladam.AppGuarda.adapters.VolleyS;
import com.example.malladam.AppGuarda.models.Empresa;


/**
 * Created by malladam on 07/05/2016.
 */
public class FragmentIniciarViaje extends Fragment {

    Button button;
    private Empresa empresa;
    private DataBaseManager dbManager;
    private VolleyS volley;
    private String urlGetSigViaje;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_iniciarviaje, container, false);

        dbManager = new DataBaseManager(getActivity().getApplicationContext());
        empresa = empresa.getInstance();
        urlGetSigViaje = getResources().getString(R.string.WSServer) + getResources().getString(R.string.app_name) + getResources().getString(R.string.getSiguienteViaje);
        volley = volley.getInstance(getActivity().getApplicationContext());


        button = (Button) v.findViewById(R.id.button);

        button.setTextColor(Color.parseColor(empresa.getColorText()));
        button.setBackgroundColor(Color.parseColor(empresa.getColorBack()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fecha,origen, destino, viaje, linea;
                fecha= "12/23/12332 12:41";
                origen = "LA paz";
                destino = "nueva zelandida";
                viaje = "123a";
                linea = "468";

                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.iniciarViaje)+" "+viaje)
                        .setMessage("Desea iniciar el viaje correspondiente a la linea "+linea+" desde "+origen+" hacia "+destino+" previsto para la fecha "+fecha+" ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.registrarViaje(viaje);

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whitch){
                                //NOT
                            }
                        })

                        .setIcon(R.drawable.bus_icon)
                        .show();
                ///////////////////////////levantar dialog con sigueinte viaje sino dialog sin viaje
                /*try {
                    WSgetSiguienteViaje();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
            }
        });
/*
        FragmentManager manager=getActivity().getSupportFragmentManager();

        PageAdapter adapter=new PageAdapter(manager);

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);*/
        return v;
    }
}

/*
    private void WSgetSiguienteViaje() throws JSONException, TimeoutException, ExecutionException {
        try {
            volley.llamarWString(Request.Method.GET,urlGetSigViaje, null,new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Sucursal sucursal = new Sucursal();
                            JSONObject jsonObject = (JSONObject) response.get(i);
                            sucursal.setAddTerminal(jsonObject.getBoolean("addTerminal"));
                            sucursal.setHasTerminal(jsonObject.getBoolean("hasTerminal"));
                            sucursal.setDireccion(jsonObject.getString("direccion"));
                            sucursal.setId_sucursal(jsonObject.getInt("id_sucursal"));
                            sucursal.setLatitud(jsonObject.getDouble("latitud"));
                            sucursal.setLongitud(jsonObject.getDouble("longitud"));
                            sucursal.setMail(jsonObject.getString("mail"));
                            sucursal.setTelefono(jsonObject.getInt("telefono"));
                            sucursal.setNombre(jsonObject.getString("nombre"));
                            sucursales.add(sucursal);
                        }
                        if(!sucursales.isEmpty()){
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                            mapFragment.getMapAsync(SucursalesActivity.this);
                        }else{
                            new AlertDialog.Builder(SucursalesActivity.this)
                                    .setTitle("Ops!")
                                    .setMessage("No pudimos mostrar nuestras sucursales")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
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
            }, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
        */