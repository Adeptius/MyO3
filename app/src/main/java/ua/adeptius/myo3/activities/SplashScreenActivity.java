package ua.adeptius.myo3.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.InetAddress;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.utils.Settings;
import ua.adeptius.myo3.utils.Utilits;

import static ua.adeptius.myo3.utils.Utilits.EXECUTOR;
import static ua.adeptius.myo3.utils.Utilits.HANDLER;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView statusTextView;
    private TextView titleText;
    private TextView comentText;
    private ProgressBar progressBar;
    public static String currentSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        statusTextView = (TextView) findViewById(R.id.status_text_view);
        titleText = (TextView) findViewById(R.id.text_title);
        comentText = (TextView) findViewById(R.id.text_coment);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInSplashScreen);
        startAnimation();

        //Preparing Shared Preferences
        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

        try {
            currentSessionId = Web.getSessionId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO сообщить что нет инета

        try {
            if (true){ //TODO условие, если залогинен
                goToMain();
            }else {

                goToLogin();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilits.networkLog("Не могу проверить сессию, что бы залогинится");
        }


        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    checkAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkAll() throws InterruptedException {
        Thread.sleep(1500);
        setStatusTextView("Перевірка наявності інтернету");
        if (!isInternetOk()){
            setStatusTextView("Інтернет відсутній");
            stopProgressBar();
        }else {
            setStatusTextView("Авторизація");
            if (isItFirstEnter()){
                setStatusTextView("Будь-ласка увійдіть");
                stopProgressBar();
                Thread.sleep(1000);
                goToMain();
            }else{

            }
        }
    }

    private boolean isAuthorizationOk(){
        return false;
    }

    private boolean isItFirstEnter(){
        boolean logIsEmpty = Settings.getCurrentLogin().equals("");
        boolean passIsEmpty = Settings.getCurrentPassword().equals("");
        return logIsEmpty && passIsEmpty;
    }

    private void stopProgressBar(){
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setStatusTextView(final String s){
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                statusTextView.setText(s);
            }
        });
    }

    private boolean isInternetOk(){
        try {
            InetAddress address = InetAddress.getByName("www.o3.ua");
            address.getHostAddress();
            return true;
        }catch (Exception e){
            return false;
        }
    }


    private void startAnimation() {
        final ImageView imageView = (ImageView) findViewById(R.id.logoView);
        Animation anim = AnimationUtils.loadAnimation(SplashScreenActivity.this,
                R.anim.splash_screen_scale);

        imageView.startAnimation(anim);

        titleText.setVisibility(View.INVISIBLE);
        comentText.setVisibility(View.INVISIBLE);


        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        titleText.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(SplashScreenActivity.this,
                                R.anim.splash_screen_alpha);
                        titleText.startAnimation(anim);
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        comentText.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(SplashScreenActivity.this,
                                R.anim.splash_screen_alpha);
                        comentText.startAnimation(anim);
                    }
                });
            }
        });
    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }


    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        this.finish();
        startActivity(intent);
    }

    public boolean isCurrentDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
