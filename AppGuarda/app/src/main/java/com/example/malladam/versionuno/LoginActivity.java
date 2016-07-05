package com.example.malladam.versionuno;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.malladam.versionuno.adapters.VolleyS;
import com.example.malladam.versionuno.models.Asientos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private Intent intent;
    private Boolean credencailesValidas = false;
    private String nuevoToken = "";
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private DataBaseManager dbManager;
    private JSONObject jsonBody;
    private VolleyS volley;
    private String urlToken;
    boolean cancel = true;
    View focusView = mUserView;
    String user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(this, MainActivity.class);
        dbManager = new DataBaseManager(this);

        mUserView = (AutoCompleteTextView) findViewById(R.id.user);

        mPasswordView = (EditText) findViewById(R.id.password);
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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        ///////WS/////
        urlToken = getResources().getString(R.string.WSServer)+getResources().getString(R.string.app_name)+"/auth";
        volley = volley.getInstance(this);
        //////WS/////////////7
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
    private Boolean isCredencialesValidas(String user, String pass) {
        return dbManager.validarUsuario(user, pass);
    }



    private void WSconsultarValidezCredenciales(final String user, final String pass) throws JSONException, TimeoutException, ExecutionException{

            jsonBody  = new JSONObject();
            jsonBody.put("username",user);
            jsonBody.put("password",pass);

        try {
            volley.getToken(urlToken, jsonBody,new Response.Listener<JSONObject>(){
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
                    });
            Thread.sleep(13000);
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
            startActivity(intent);
        }
    }
}

