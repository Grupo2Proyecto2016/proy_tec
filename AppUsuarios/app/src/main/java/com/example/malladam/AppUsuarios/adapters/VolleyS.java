package com.example.malladam.AppUsuarios.adapters;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by malladam on 17/05/2016.
 */
public class VolleyS {
    private static VolleyS mVolleyS = null;
    private RequestQueue mRequestQueue;


    private VolleyS(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyS getInstance(Context context) {
        if (mVolleyS == null) {
            mVolleyS = new VolleyS(context);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void llamarWS(int metodo, String urlCreateUser, JSONObject jsonBody,  Response.Listener<JSONObject> response, Response.ErrorListener errorListener) throws TimeoutException, ExecutionException, InterruptedException {
        JsonObjectRequest request = new JsonObjectRequest(metodo,urlCreateUser,jsonBody, response, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        addToQueue(request);
    }

    public void llamarWSstring(int metodo, String url, Response.Listener<String> responseListener,Response.ErrorListener errorListener) {
        StringRequest strReq = new StringRequest(metodo, url, responseListener, errorListener)
        {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("llamarWSstringPARSE", response.toString());
                return super.parseNetworkResponse(response);
            }
        };
        // Adding request to request queue
        addToQueue(strReq);
    }



    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (mRequestQueue == null)
                mRequestQueue = this.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(request);
        }
    }
}


/*
JSONObject json = new JSONObject();
json.put("sun","yellow");

JSONArray veg = new JSONArray();
JSONObject vegData = new JSONObject();
vegData.put("apple","red");
vegData.put("banana","yellow");
vegData.put("melon","orange");

veg.put(vegData);

json.put("vegetables",veg);
{
    "sun":"yellow",
    "vegetables":[{
        "apple":"red",
        "banana":"yellow",
        "melon":"orange"
    }]
}
 */