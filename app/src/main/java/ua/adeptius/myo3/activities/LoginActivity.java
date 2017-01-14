package ua.adeptius.myo3.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.InetAddress;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.Person;
import ua.adeptius.myo3.utils.Settings;

import static ua.adeptius.myo3.utils.Utilits.EXECUTOR;
import static ua.adeptius.myo3.utils.Utilits.HANDLER;

public class LoginActivity extends AppCompatActivity {


    private LinearLayout loginLayout;
    private EditText textLogin;
    private EditText textPassword;
    private Button buttonLogin;
    private TextView rememberPassButton;

    private LinearLayout splashLayout;
    private TextView statusTextView;
    private TextView titleText;
    private TextView comentText;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textLogin = (EditText) findViewById(R.id.input_login) ;
        textPassword = (EditText) findViewById(R.id.input_password) ;
        buttonLogin = (Button) findViewById(R.id.button_login) ;
        rememberPassButton = (TextView) findViewById(R.id.remember_password);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        loginLayout.setVisibility(View.GONE);

        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        statusTextView = (TextView) findViewById(R.id.status_text_view);
        titleText = (TextView) findViewById(R.id.text_title);
        comentText = (TextView) findViewById(R.id.text_coment);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInSplashScreen);
        progressBar.setVisibility(View.INVISIBLE);
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        rememberPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rememberPassword();
            }
        });

        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

        Settings.setCurrentLogin("234");
        Settings.setCurrentPassword("2354");

        startAnimation();

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
        startProgressBar();
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
                Thread.sleep(2000);
                showLogin();
            }else{
                try {
                    Person person = GetInfo.getPersonInfo();
                    if (person.getCard() == null) {
                        setStatusTextView("Будь-ласка увійдіть");
                        stopProgressBar();
                        Thread.sleep(2000);
                        showLogin();
                    }else {
                        MainActivity.person = person;
                        setStatusTextView("Вхід виконан");
                        Thread.sleep(1000);
                        goToMain();
                    }

                } catch (Exception e) {
                    showLogin();
                }
            }
        }
    }


    private void showLogin(){
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                splashLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void rememberPassword() {

    }

    private void startAnimation() {
        final ImageView imageView = (ImageView) findViewById(R.id.logoView);
        Animation anim = AnimationUtils.loadAnimation(LoginActivity.this,
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
                        Animation anim = AnimationUtils.loadAnimation(LoginActivity.this,
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
                        Animation anim = AnimationUtils.loadAnimation(LoginActivity.this,
                                R.anim.splash_screen_alpha);
                        comentText.startAnimation(anim);
                    }
                });
            }
        });
    }

    public void login() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Авторизація...");
        progressDialog.show();

        String login = textLogin.getText().toString();
        String password = textPassword.getText().toString();
        Settings.setCurrentLogin(login);
        Settings.setCurrentPassword(password);

        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textPassword.getWindowToken(), 0);

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    Person person = GetInfo.getPersonInfo();
                    if (person.getCard() == null) {
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(loginLayout,
                                        "Неправильний логін або пароль",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        MainActivity.person = person;
                        goToMain();
                    }
                }catch (Exception e){
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(loginLayout,
                                    "Помилка. Можливо нема зв'язку",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
                }finally {
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }

    public boolean isCurrentDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
    private void startProgressBar(){
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
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
}
