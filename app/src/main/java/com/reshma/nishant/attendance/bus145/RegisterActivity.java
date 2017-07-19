package com.reshma.nishant.attendance.bus145;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    EditText firstNameET;
    EditText lastNameET;
    EditText courseNumberET;

    String firstName = "";
    String lastName = "";
    String courseNumber = "";
    String userRFID = "";

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstNameET = (EditText) findViewById(R.id.firstNameEditText);
        lastNameET = (EditText) findViewById(R.id.lastNameEditText);
        courseNumberET = (EditText) findViewById(R.id.courseNumberEditText);
        courseNumberET.setText("Bus 145");

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(RegisterActivity.this);
    }

    public void registerButtonClicked (View view) {

        firstName = firstNameET.getText().toString();
        lastName = lastNameET.getText().toString();
        courseNumber = courseNumberET.getText().toString();

        if(firstName.equals("") || firstName == null) {
            Toast.makeText(this,"Please enter first name ...",Toast.LENGTH_LONG).show();
        } else if(lastName.equals("") || lastName == null) {
            Toast.makeText(this,"Please enter last name ...",Toast.LENGTH_LONG).show();
        } else if(courseNumber.equals("") || courseNumber == null) {
            Toast.makeText(this,"Please check course number ...",Toast.LENGTH_LONG).show();
        } else {
            sendPostToGetRFID();
        }
    }

    private void sendPostToGetRFID() {
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("courseNumber", "Bus 145");

        JsonObjectRequest request_json = new JsonObjectRequest(MyGlobalConstants.URL_POST_GET_RFID_BY_STUDENTNAME_HEROKU, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Process os success response
                        // response
                        Log.d("Response", response.toString());
                        saveUserDetailedOnDevice(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.d("Error.Response", "That didn't work");
            }
        });
        // add the request object to the queue to be executed
        queue.add(request_json);
    }

    private void saveUserDetailedOnDevice(JSONObject response) {
        try {
            userRFID = response.getString("rfid");
            pref = getApplicationContext().getSharedPreferences(MyGlobalConstants.USER_INFO_SHARED_PREFERENCE, MODE_PRIVATE);
            editor = pref.edit();
            editor.putString(MyGlobalConstants.USER_NAME, firstName + " " +lastName);
            editor.putString(MyGlobalConstants.USER_RFID, userRFID);
            editor.commit();
            Log.d("Saved Following :", "Name: " +firstName + " " +lastName +", Rfid: " +userRFID);
            goToCheckAttendance();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this,"Something went wrong...Try again",Toast.LENGTH_LONG).show();
        }
    }

    private void goToCheckAttendance(){
        Toast.makeText(RegisterActivity.this,"Great! All set to check attendance!",Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, CheckAttendanceActivity.class);
        startActivity(i);
    }

//
//    public void tempDebugClicked (View view) {
//        Intent i = new Intent(this, CheckAttendanceActivity.class);
//        startActivity(i);
//    }
//
//    public void getClicked (View view) {
//        String getEndpoint =MyGlobalConstants.URL_GET_GET_ATTENDANCE_BY_RFID_LOCAL_RESHMA;
//        String RFID = "148,26,50,170";
//        String urlGet = getEndpoint +"?rfid=" +RFID;
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGet,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                        mTextView.setText("Response is: "+ response.substring(0,500));
//                        Log.d("Response",response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//                Log.d("Error.Response", "That didn't work");
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
////                params.put("RFID", "1234");
////                params.put("OTP", "12345");
//
//                return params;
//            }
//        };
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
//
//    public void postClicked (View view) {
//        String postURL = MyGlobalConstants.URL_POST_GET_RFID_BY_STUDENTNAME_LOCAL_RESHMA;
//
//        // Post params to be sent to the server
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("firstName", "reshma");
//        params.put("lastName", "dokania");
//        params.put("courseNumber", "Bus 145");
//
//        JsonObjectRequest request_json = new JsonObjectRequest(URL_FOR_RFID, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //Process os success response
//                        // response
//                        try {
//                            Log.d("Response", response.getString("rfid"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//                Log.d("Error.Response", "That didn't work");
//            }
//        });
//        // add the request object to the queue to be executed
//        queue.add(request_json);
//    }

}
