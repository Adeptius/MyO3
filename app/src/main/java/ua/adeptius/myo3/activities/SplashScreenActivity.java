package ua.adeptius.myo3.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.utils.Utilits;

public class SplashScreenActivity extends AppCompatActivity {

    public static String currentSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Preparing Shared Preferences
        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

        currentSessionId = Settings.getSessionID();


        try {
            if (Web.isLoggedNow(currentSessionId)){
                goToMain();
            }else {
                goToLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilits.networkLog("Не могу проверить сессию, что бы залогинится");
        }
    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }

    public void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }

    public boolean isCurrentDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
