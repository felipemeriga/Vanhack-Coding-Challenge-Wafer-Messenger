package com.felipe.vanhackchallenge.core.Rest;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.felipe.vanhackchallenge.core.Domain.Wafer;
import com.felipe.vanhackchallenge.core.Domain.WaferOriginal;
import com.felipe.vanhackchallenge.core.WaferList.WaferListActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WaferRestService {

    public static final String ENDPOINT = "https://restcountries.eu/rest/v2/all";
    RequestQueue requestQueue;
    private Gson gson;
    public WaferListActivity activity;
    List<Wafer> wafers;
    List<WaferOriginal> waferOriginalList;

    public WaferRestService(RequestQueue requestQueue, WaferListActivity activity) {
        this.requestQueue = requestQueue;
        this.activity = activity;

        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
        wafers = new ArrayList<>();
    }

    public void fetchPosts() {
        Log.i("WaferRestService Class", "fetchPosts() Doing the HTTP GET request on ENDPOINT: " + ENDPOINT);
        this.requestQueue.add(new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError));
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.i("WaferRestService Class", "onPostsLoaded() Success: The HTTP GET request was processed successfully!");
            waferOriginalList = Arrays.asList(gson.fromJson(response, WaferOriginal[].class));

            Log.d("WaferRestService Class", "onPostsLoaded() Calling fetchIntoWafer to insert correct data into wafer, requested from coding challenge");
            fetchIntoWafer(waferOriginalList);
            Log.i("WaferRestService", "onPostsLoaded() " + activity.waferList.size() + " wafers loaded.");
            activity.restCallback(wafers);

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("WaferRestService Class", "onPostsError() Error:" + error.toString());
            activity.restCallback(wafers);

        }
    };

    private final void fetchIntoWafer(List<WaferOriginal> waferOriginalList){

        for(WaferOriginal waferOriginal: waferOriginalList){
            wafers.add(new Wafer(waferOriginal.getName(),((LinkedTreeMap) waferOriginal.getLanguages().get(0)).get("name").toString(),((LinkedTreeMap) waferOriginal.getCurrencies().get(0)).get("name").toString()));
        }

    }


    //REST SOLUTION 2 - THIS IS ANOTHER WAY TO DO THE REQUEST, THIS METHOD WILL BE CALLED FROM AN AsyncTask
/*    public  String restHttpRequest(String url){

        try{
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("WaferRestService Class", "restHttpRequest(): The HTTP request was done successfully");
                            onRequestFinished(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("WaferRestService Class", "restHttpRequest(): There was an error in the HTTP request");
                    Log.d("WaferRestService Class", "restHttpRequest(): Error: " + error.getMessage());


                }
            });

            this.requestQueue.add(stringRequest);

        }catch (Exception e){
            Log.d("WaferRestService Class", "restHttpRequest(): Error: " + e.getMessage());
            throw e;
        }

        return null;
    }

    public void onRequestFinished(String response){
        Log.i("WaferRestService Class", "onRequestFinished() Success: The HTTP GET request was processed successfully!");
        waferOriginalList = Arrays.asList(gson.fromJson(response, WaferOriginal[].class));

        Log.d("WaferRestService Class", "onRequestFinished() Calling fetchIntoWafer to insert correct data into wafer, requested from coding challenge");
        fetchIntoWafer(waferOriginalList);
        Log.i("WaferRestService", "onRequestFinished() " + activity.waferList.size() + " wafers loaded.");
        activity.restCallback(wafers);

    }*/

}
