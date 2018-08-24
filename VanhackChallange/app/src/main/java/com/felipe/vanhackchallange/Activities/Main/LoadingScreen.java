package com.felipe.vanhackchallange.Activities.Main;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.felipe.vanhackchallange.Activities.Domain.Wafer;
import com.felipe.vanhackchallange.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class LoadingScreen extends AppCompatActivity {

    public static final String ENDPOINT = "https://restcountries.eu/rest/v2/all";
    RequestQueue requestQueue;
    private Gson gson;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity Class", "onCreate(): Starting executing of the MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        fetchPosts();

        //REST SOLUTION 2 - Calling the AsyncTask to do the HTTP request
//        new HandleHttpRequest(this).execute();
    }

    private void fetchPosts() {
        Log.i("MainActivity Class", "fetchPosts() Doingthe HTTP GET request on ENDPOINT: " + ENDPOINT);
        this.requestQueue.add(new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError));
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("MainActivity Class", "onResponse() Success: The HTTP GET request was processed successfully!");
            List<Wafer> wafer = Arrays.asList(gson.fromJson(response, Wafer[].class));

            Log.i("MainActivity", "onResponse() " + wafer.size() + " posts loaded.");

        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity Class", "onPostsError() Error:" + error.toString());

        }
    };


//REST SOLUTION 2 - THIS IS ANOTHER WAY TO DO THE REQUEST AS A ASYNC CLASS THREAD
//    public  String restHttpRequest(String url){
//        RequestQueue queue = Volley.newRequestQueue(this);
//        try{
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d("MainActivity Class", "restHttpRequest(): The HTTP request was done successfully");
//                            onRequestFinished(response);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("MainActivity Class", "restHttpRequest(): There was an error in the HTTP request");
//                    Log.d("MainActivity Class", "restHttpRequest(): Error: " + error.getMessage());
//
//
//                }
//            });
//
//            queue.add(stringRequest);
//
//        }catch (Exception e){
//            Log.d("MainActivity Class", "restHttpRequest(): Error: " + e.getMessage());
//            throw e;
//        }
//
//        return null;
//    }
//
//    public void onRequestFinished(String response){
//        List<Wafer> wafer = Arrays.asList(gson.fromJson(response, Wafer[].class));
//
//        Log.i("MainActivity", "onResponse()" + wafer.size() + " wafer loaded.");
//
//    }
//
//    private class HandleHttpRequest extends AsyncTask<Void, Void, Void> {
//
//        MainActivity mainActivity;
//
//        public HandleHttpRequest(MainActivity mainActivity) {
//            super();
//            this.mainActivity = mainActivity;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Log.d("HandleHttpRequest Class", "onPreExecute(): Starting Async task to parse HTTP request");
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Log.d("HandleHttpRequest Class", "doInBackground(): Calling requestContent to perform HTTP GET request in REST URL" + ENDPOINT);
//            this.mainActivity.restHttpRequest(ENDPOINT);
//            return null;
//        }
//
//
//    }


}


