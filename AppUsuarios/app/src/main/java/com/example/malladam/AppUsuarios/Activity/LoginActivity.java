package com.example.malladam.AppUsuarios.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.malladam.AppUsuarios.DataBaseManager;
import com.example.malladam.AppUsuarios.R;
import com.example.malladam.AppUsuarios.adapters.VolleyS;
import com.example.malladam.AppUsuarios.models.Empresa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private Intent intent;
    private Boolean credencailesValidas = false;
    private String nuevoToken = "";
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mRegistroView;
    private DataBaseManager dbManager;
    private JSONObject jsonBody;
    private VolleyS volley;
    private String urlToken;
    boolean cancel = true;
    View focusView = mUserView;
    String user;
    String password;
    String urlUserExist;
    Handler mHandler;
    Boolean userExist;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userExist = false;
        intent = new Intent();
        dbManager = new DataBaseManager(this);
        empresa = empresa.getInstance();

        urlUserExist = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/userExists";
        mUserView = (AutoCompleteTextView) findViewById(R.id.user);
        mUserView.setTextColor(Color.parseColor(empresa.getColorText()));

        mHandler = new Handler();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setTextColor(Color.parseColor(empresa.getColorText()));

        LinearLayout mealLayout = (LinearLayout) findViewById(R.id.linear_login);
        mealLayout.setBackgroundColor(Color.parseColor(empresa.getColorBack()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(empresa.getColorHeader()));
        toolbar.setTitleTextColor(Color.parseColor(empresa.getColorTextHeader()));
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor(empresa.getColorTextHeader()), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mUserView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    try {
                        WSuserExist(mUserView.getText().toString());
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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setTextColor(Color.parseColor(empresa.getColorBack()));
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegistroView = (TextView) findViewById(R.id.registro);
        mRegistroView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registroIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registroIntent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        /////////////WS/////////////
        urlToken = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/auth";
        volley = volley.getInstance(this);
        /////////////WS/////////////

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(empresa.getNombre());
    }


    private void attemptLogin() {
        mUserView.setError(null);
        mPasswordView.setError(null);

        user = mUserView.getText().toString();
        password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(user)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else {
            try {
                WSconsultarValidezCredenciales(user, password);
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (TimeoutException e1) {
                e1.printStackTrace();
            }
            verificarLogin(false);
        }

    }


    private void WSconsultarValidezCredenciales(final String user, final String pass) throws JSONException, TimeoutException, ExecutionException{

            jsonBody  = new JSONObject();
            jsonBody.put("username",user);
            jsonBody.put("password",pass);

        try {
            volley.llamarWS(Request.Method.POST,urlToken, jsonBody,new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        credencailesValidas = true;
                        nuevoToken = (response.getString("token"));
                        verificarLogin(true);
                        //dbManager.registrarLogin(response.getString("token"),user, pass);/////guardo el usuario logueado en la base
                        Log.d("-el token es : ", response.getString("token"));
                        } catch (JSONException e) {
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
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), new String[]{},

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    public void verificarLogin(Boolean ws){

        if ((!credencailesValidas)) {
            mUserView.setError(getString(R.string.error_invalid_user));
            focusView = mUserView;
            cancel = true;
        }else {
            cancel = false;
        }
        if (cancel) {
            if(ws){
                focusView.requestFocus();
            }
        } else {
            dbManager.registrarLogin(nuevoToken,user, password);
            //dbManager.registrarLogin(user, password);/////guardo el usuario logueado en la base
            showProgress(true);
            intent = new Intent(LoginActivity.this, BusquedaActivity.class);
            startActivity(intent);
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
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.error_network_timeout),Toast.LENGTH_LONG).show();
                    userExist = false;
                    mUserView.setError(getResources().getString(R.string.error_network_timeout));
                    focusView = mUserView;
                } else{
                    userExist = false;
                    mUserView.setError("No existe el usuario");
                    focusView = mUserView;
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
                        Drawable okIcon = getResources().getDrawable(R.drawable.ok);
                        okIcon.setBounds(new Rect(0, 0, okIcon.getIntrinsicWidth(), okIcon.getIntrinsicHeight()));
                        mUserView.setError("Ok",okIcon);
                        focusView = mUserView;
                        userExist = true;
                    }
                });
                return super.parseNetworkResponse(response);
            }
        };
        // Adding request to request queue
        volley.addToQueue(strReq);
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

}

