package com.reshma.nishant.attendance.bus145;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userName;
    String userRFID;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getApplicationContext().getSharedPreferences(MyGlobalConstants.USER_INFO_SHARED_PREFERENCE, MODE_PRIVATE);
        editor = pref.edit();
        userName = pref.getString(MyGlobalConstants.USER_NAME, "");
        userRFID = pref.getString(MyGlobalConstants.USER_RFID, "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void userTapped (View view) {
        if(userName.equals("") || userRFID.equals("")) {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, CheckAttendanceActivity.class);
            startActivity(i);
        }
    }

    private void delay() {
         /* New Handler to start the Menu-Activity
            * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,Menu.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
