package com.felipe.vanhackchallenge.core.WaferList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.felipe.vanhackchallenge.core.Adapter.WaferListAdapter;
import com.felipe.vanhackchallenge.core.Domain.Wafer;
import com.felipe.vanhackchallenge.core.Helper.RecyclerItemTouchHelper;
import com.felipe.vanhackchallenge.core.Rest.WaferRestService;
import com.felipe.vanhackchallenge.R;

import java.util.ArrayList;
import java.util.List;

public class WaferListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    public List<Wafer> waferList;
    private WaferListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    public static final String ENDPOINT = "https://restcountries.eu/rest/v2/all";
    WaferRestService restService;
    RequestQueue requestQueue;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wafer_list);

        Log.d("WaferListActivity", "onCreate(): Defining and setting Toolbar title and configurations");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.wafer_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (ProgressBar) findViewById(R.id.progressBar);

        Log.d("WaferListActivity", "onCreate(): Referencing recyclerView and coordinatorLayout by their xml layout id's");
        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        Log.d("WaferListActivity", "onCreate(): Instanciating an Array list of the java POJO Wafer");
        waferList = new ArrayList<>();

        Log.d("WaferListActivity", "onCreate(): Instanciating WaferListAdapter which will iterate the waferList through UI");
        mAdapter = new WaferListAdapter(this, waferList);

        Log.d("WaferListActivity", "onCreate(): Instanciating RecyclerView and adding the decorators");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        //Where a new instance RecyclerItemTouchHelper is created, as the WaferListActivity implements the interface RecyclerItemTouchHelperListener, it can be passed as a parameter
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        //Making a request Queue, where all HTTP call requests have to be inserted in order to be processed
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        // making http call and fetching menu json
        Log.i("WaferListActivity", "onCreate(): Calling prepareWafer, to do the REST HTTP request ");
        prepareWafer();

        //REST SOLUTION 2 - Calling the AsyncTask to do the HTTP request
//        new HandleHttpRequest(this).execute();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

    }

    private void prepareWafer() {
        restService = new WaferRestService(requestQueue, this);
        restService.fetchPosts();
    }

    public void restCallback(List<Wafer> wafers){
        spinner.setVisibility(View.GONE);
        if (wafers.size() == 0) {
            Log.d("WaferListActivity", "restCallback(): The request wasn't successful, calling toast to alert user");
            Toast.makeText(getApplicationContext(), "Couldn't fetch the menu! Pleas try again.", Toast.LENGTH_LONG).show();
            return;
        }
        waferList.clear();

        Log.i("WaferListActivity", "restCallback(): Adding request body to List<Wafer> and notifying Adapter that new data was inserted");
        waferList.addAll(wafers);
        mAdapter.notifyDataSetChanged();
    }

    //As WaferListActivity implements RecyclerItemTouchHelperListener, the method onSwiped is being Overrided
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof WaferListAdapter.MyViewHolder) {
            // get the removed wafer name(Country name) to display it in snack bar
            String name = waferList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed wafer for undo purpose
            final Wafer deletedWafer = waferList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the wafer from recycler view
            mAdapter.removeWafer(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from wafer list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreWafer(deletedWafer, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds waferList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //REST SOLUTION 2 - THIS IS ANOTHER WAY TO DO THE REQUEST, DEFINING THE AsyncTask CLASS BODY
/*    private class HandleHttpRequest extends AsyncTask<Void, Void, Void> {

        WaferListActivity activity;

        public HandleHttpRequest(WaferListActivity activity) {
            super();
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            Log.d("HandleHttpRequest Class", "onPreExecute(): Starting Async task to parse HTTP request");
        }


        @Override
        protected Void doInBackground(Void... params) {
            Log.d("HandleHttpRequest Class", "doInBackground(): Calling requestContent to perform HTTP GET request in REST URL" + ENDPOINT);
            restService = new WaferRestService(requestQueue, activity);
            restService.restHttpRequest(ENDPOINT);
            return null;
        }


    }*/
}
