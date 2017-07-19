package com.reshma.nishant.attendance.bus145;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckAttendanceActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CheckAttendanceActivity";
    private TextView nameTV;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    RequestQueue queue;

    String userName;
    String userRFID;

    JSONObject responseLocal;

    ArrayList myAttendance = new ArrayList<DataObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing for updates...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getAttendance();
            }
        });

        pref = getApplicationContext().getSharedPreferences(MyGlobalConstants.USER_INFO_SHARED_PREFERENCE, MODE_PRIVATE);
        editor = pref.edit();

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(CheckAttendanceActivity.this);

        nameTV = (TextView) findViewById(R.id.nameTV);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        getSavedUserInfo();
        nameTV.setText("Hello " +userName);

        getAttendance();

        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private void getSavedUserInfo() {
        userName = pref.getString(MyGlobalConstants.USER_NAME, "");
        userRFID = pref.getString(MyGlobalConstants.USER_RFID, "");
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 8; index++) {
            int weekNumber = index +1;
            DataObject obj = new DataObject("Week " + weekNumber,
                    "True / False");
            results.add(index, obj);
        }
        return results;
    }

    public void getAttendance () {
        String getEndpoint = MyGlobalConstants.URL_GET_GET_ATTENDANCE_BY_RFID_HEROKU;
        String RFID = userRFID;
        String urlGET = getEndpoint + "?rfid=" + RFID;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.d("Response ", "checkAttendace " +response);
                        convertResoponseToJsonAndSave(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.d("Error.Response", "That didn't work");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void sampleData() {
        String data="{'week1':'true','week2':'true','week3':'true','week4':'false','week5':'false','week6':'true','week7':'true','week8':'true'}";

    }

    private void convertResoponseToJsonAndSave(String data) {
        JSONParser parser = new JSONParser();
        try {
            responseLocal = (JSONObject) parser.parse(data);
            Log.d("Value ", String.valueOf(responseLocal.get("week1")));
            updateDataOnLocal();

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ERROR ", "Couldn't convert string to JSON");
        }
    }

    private void updateDataOnLocal() {

        DataObject obj1 = new DataObject("Week 1", String.valueOf(responseLocal.get("week1")));
        myAttendance.add(0, obj1);
        editor.putString(MyGlobalConstants.WEEK_1, String.valueOf(responseLocal.get("week1")));

        DataObject obj2 = new DataObject("Week 2", String.valueOf(responseLocal.get("week2")));
        myAttendance.add(1, obj2);
        editor.putString(MyGlobalConstants.WEEK_2, String.valueOf(responseLocal.get("week2")));

        DataObject obj3 = new DataObject("Week 3", String.valueOf(responseLocal.get("week3")));
        myAttendance.add(2, obj3);
        editor.putString(MyGlobalConstants.WEEK_3, String.valueOf(responseLocal.get("week3")));

        DataObject obj4 = new DataObject("Week 4", String.valueOf(responseLocal.get("week4")));
        myAttendance.add(3, obj4);
        editor.putString(MyGlobalConstants.WEEK_4, String.valueOf(responseLocal.get("week4")));

        DataObject obj5 = new DataObject("Week 5", String.valueOf(responseLocal.get("week5")));
        myAttendance.add(4, obj5);
        editor.putString(MyGlobalConstants.WEEK_5, String.valueOf(responseLocal.get("week5")));

        DataObject obj6 = new DataObject("Week 6", String.valueOf(responseLocal.get("week6")));
        myAttendance.add(5, obj6);
        editor.putString(MyGlobalConstants.WEEK_6, String.valueOf(responseLocal.get("week6")));

        DataObject obj7 = new DataObject("Week 7", String.valueOf(responseLocal.get("week7")));
        myAttendance.add(6, obj7);
        editor.putString(MyGlobalConstants.WEEK_7, String.valueOf(responseLocal.get("week7")));

        DataObject obj8 = new DataObject("Week 8", String.valueOf(responseLocal.get("week8")));
        myAttendance.add(7, obj8);
        editor.putString(MyGlobalConstants.WEEK_8, String.valueOf(responseLocal.get("week8")));

        editor.commit();

        setAttendanceInView();
    }

    private void setAttendanceInView() {
        mAdapter = new MyRecyclerViewAdapter(myAttendance);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void logoutButtonClicked(View view) {
        editor.clear();
        editor.commit();

        Toast.makeText(CheckAttendanceActivity.this,"Logged out",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);

    }

}
