package com.example.malladam.AppUsuarios.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity  {

    private EditText mNombreView;
    private EditText mApellidoView;
    private EditText mCorreoView;
    private EditText mDireccionView;
    private EditText mFechaNacView;
    private EditText mUsuarioView;
    private EditText mContraseñaView;
    private EditText mConfirmacionView;
    private EditText mTelefonoView;
    private Button mRegistroButton;
    View focusView;
    private int year, month, day;
    private String currentDate;
    private String nombre;
    private String apellido;
    private String correo;
    private String direccion;
    private String usuario;
    private String contraseña;
    private String confirmacion;
    private String telefono;
    private String fechaNac;
    Pattern pattern;
    private VolleyS volley;
    private String urlregisterUser,urlUserExist;
    private JSONObject jsonBody;
    private Boolean userDisponible = false;
    Handler mHandler;

    private static final int DATE_DIALOG_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String expressionEmail = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        pattern = Pattern.compile(expressionEmail, Pattern.CASE_INSENSITIVE);

        mNombreView = (EditText) findViewById(R.id.nombreRegistro);
        mApellidoView = (EditText) findViewById(R.id.apellidoRegistro);
        mCorreoView = (EditText) findViewById(R.id.correoRegistro);
        mDireccionView = (EditText) findViewById(R.id.direccionRegistro);
        mUsuarioView = (EditText) findViewById(R.id.usuarioRegistro);
        mContraseñaView = (EditText) findViewById(R.id.contraseñaRegistro);
        mConfirmacionView = (EditText) findViewById(R.id.confirmacionRegistro);
        mTelefonoView = (EditText) findViewById(R.id.telefonoRegistro);
        mRegistroButton = (Button) findViewById(R.id.buttonRegistro);
        mFechaNacView = (EditText) findViewById(R.id.fecNacRegistro);

        mHandler = new Handler();

        ///////WS/////
        urlregisterUser = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/registerUser";
        urlUserExist = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/userExists";
        volley = volley.getInstance(this);
        //////WS/////////////

        View.OnClickListener listenerDate = new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                showDialog(DATE_DIALOG_ID);
            }
        };
        mFechaNacView.setOnClickListener(listenerDate);

        mRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = mNombreView.getText().toString();
                apellido = mApellidoView.getText().toString();
                correo = mCorreoView.getText().toString();
                direccion = mDireccionView.getText().toString();
                usuario = mUsuarioView.getText().toString();
                contraseña = mContraseñaView.getText().toString();
                confirmacion = mConfirmacionView.getText().toString();
                telefono = mTelefonoView.getText().toString();
                fechaNac = mFechaNacView.getText().toString();

                if(validarDatosRegistro(nombre, apellido, correo, direccion, usuario, contraseña , confirmacion, telefono,fechaNac)){
                    try {
                        WSregistarUsuario(nombre, apellido, correo, direccion, usuario, contraseña , telefono,day, month, year);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mUsuarioView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if((!hasFocus) && (!mUsuarioView.getText().toString().isEmpty())) {
                    try {
                        WSuserExist(mUsuarioView.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateDisplay() {
        currentDate = new StringBuilder().append(day).append(".").append(month + 1).append(".").append(year).toString();
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {
            mFechaNacView.setError(null);

            year = i;
            month = j;
            day = k;
            updateDisplay();
            mFechaNacView.setText(currentDate);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dpd = new DatePickerDialog(this, myDateSetListener, year, month, day);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                return dpd;
        }
        return null;
    }

    private Boolean validarDatosRegistro(String nombre,String apellido,String correo,String direccion,String usuario,String contraseña ,String confirmacion,
                                         String telefono,String fechaNac){
        Boolean valido = true;
        Matcher matcher = pattern.matcher(correo);

        mUsuarioView.setError(null,null);

        if(TextUtils.isEmpty(nombre)){
            mNombreView.setError(getString(R.string.error_field_required));
            focusView = mNombreView;
            valido = false;
        }
        if(TextUtils.isEmpty(apellido)){
            mApellidoView.setError(getString(R.string.error_field_required));
            focusView = mApellidoView;
            valido = false;
        }
        if(TextUtils.isEmpty(correo)){
            mCorreoView.setError(getString(R.string.error_field_required));
            focusView = mCorreoView;
            valido = false;
        }else if(!matcher.matches()){
            mCorreoView.setError(getString(R.string.error_invalid_email));
            focusView = mCorreoView;
            valido = false;
        }
        if(TextUtils.isEmpty(direccion)){
            mDireccionView.setError(getString(R.string.error_field_required));
            focusView = mDireccionView;
            valido = false;
        }
        if(TextUtils.isEmpty(contraseña)){
            mContraseñaView.setError(getString(R.string.error_field_required));
            focusView = mContraseñaView;
            valido = false;
        }
        if(TextUtils.isEmpty(confirmacion)){
            mConfirmacionView.setError(getString(R.string.error_field_required));
            focusView = mConfirmacionView;
            valido = false;
        }

        if((!TextUtils.isEmpty(confirmacion))&&(!TextUtils.isEmpty(contraseña))){
            if(!confirmacion.equals(contraseña)){
                mConfirmacionView.setError(getString(R.string.error_invalid_confirmacion));
                focusView = mConfirmacionView;
                valido = false;
            }
        }
        if(TextUtils.isEmpty(usuario)){
            mUsuarioView.setError(getString(R.string.error_field_required));
            focusView = mUsuarioView;
            mContraseñaView.setText("");
            mConfirmacionView.setText("");
            valido = false;
        }else if(!userDisponible){
            mUsuarioView.setError(getString(R.string.error_user_not_disponible));
            focusView = mUsuarioView;
            mContraseñaView.setText("");
            mConfirmacionView.setText("");
            valido = false;
        }
        if(TextUtils.isEmpty(telefono)){
            mTelefonoView.setError(getString(R.string.error_field_required));
            focusView = mTelefonoView;
            valido = false;
        }
        if(TextUtils.isEmpty(fechaNac)){
            mFechaNacView.setError(getString(R.string.error_field_required));
            focusView = mFechaNacView;
            valido = false;
        }
        return valido;
    }

    private void WSregistarUsuario(final String nombre, final String apellido, final String correo, final String direccion, final String usuario,
                                   final String contraseña , final String telefono,final int fecNacDia,final int fecNacMes,final int fecNacAnio)
            throws JSONException, TimeoutException, ExecutionException {
        jsonBody  = new JSONObject();
        jsonBody.put("nombre",nombre);
        jsonBody.put("apellido",apellido);
        jsonBody.put("usrname",usuario);
        jsonBody.put("email",correo);
        jsonBody.put("telefono",telefono);
        jsonBody.put("direccion",direccion);
        jsonBody.put("fch_nacimiento", new SimpleDateFormat("yyyy-MM-dd").format(new Date(fecNacAnio-1900, fecNacMes, fecNacDia+1)));
        jsonBody.put("passwd",contraseña);

        try {
            volley.llamarWS(Request.Method.POST,urlregisterUser, jsonBody,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    //NUNCA ENTRA, OK ES ERROR PARSE
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("el ERROR ",volleyError.toString());
                    if (volleyError instanceof ParseError) {
                        Intent registroIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(registroIntent);
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.usuarioRegistro)+" "+usuario+" "+getResources().getString(R.string.creadoConExito), Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.creadoSinExito)+" "+getResources().getString(R.string.usuarioRegistro)+" "+usuario, Toast.LENGTH_LONG).show();
                        if((volleyError.getMessage() !=null) && !volleyError.getMessage().isEmpty()) {
                            Toast.makeText(RegisterActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void WSuserExist(final String usuario)throws JSONException, TimeoutException, ExecutionException {

        StringRequest strReq = new StringRequest(Request.Method.GET,urlUserExist+"?username="+usuario,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("llamarWSstringOK", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("WSuserExist ERROR ",volleyError.toString());
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                    userDisponible = false;
                    mUsuarioView.setError(getResources().getString(R.string.error_network_timeout));
                    focusView = mUsuarioView;
                } else{
                    userDisponible = true;
                    Drawable okIcon = getResources().getDrawable(R.drawable.ok);
                    okIcon.setBounds(new Rect(0, 0, okIcon.getIntrinsicWidth(), okIcon.getIntrinsicHeight()));
                    mUsuarioView.setError("Usuario disponible",okIcon);
                    focusView = mUsuarioView;
                }
            }
        })
        {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("llamarWSstringPARSE", response.toString());

                mHandler.post(new Runnable() {
                    @Override
                    // this will run on the main thread.
                    public void run() {
                        mUsuarioView.setError("Usuario no disponible");
                        focusView = mUsuarioView;
                        userDisponible = false;

                    }
                });
                return super.parseNetworkResponse(response);

            }
        };
    // Adding request to request queue
    volley.addToQueue(strReq);

    }
}