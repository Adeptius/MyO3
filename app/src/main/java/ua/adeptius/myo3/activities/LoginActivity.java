package ua.adeptius.myo3.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.DbCache;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.Person;
import ua.adeptius.myo3.model.Testing2;
import ua.adeptius.myo3.model.TestingUser;
import ua.adeptius.myo3.utils.Settings;

import static ua.adeptius.myo3.utils.Utilits.EXECUTOR;
import static ua.adeptius.myo3.utils.Utilits.HANDLER;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


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





    private TextView enterText;
    private TextInputLayout logLay;
    private TextInputLayout passLay;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textLogin = (EditText) findViewById(R.id.input_login);
        textLogin.setOnClickListener(this);
        textPassword = (EditText) findViewById(R.id.input_password);
        textPassword.setOnClickListener(this);
        buttonLogin = (Button) findViewById(R.id.button_login);
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
        enterText = (TextView) findViewById(R.id.enter_text);
        enterText.setOnClickListener(this);
        logLay = (TextInputLayout) findViewById(R.id.text_input_layout);
        passLay = (TextInputLayout) findViewById(R.id.pass_lay);

        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

//        Settings.setCurrentLogin("413100711");
//        Settings.setCurrentPassword("0464023");

//        Settings.setCurrentLogin("6584");
//        Settings.setCurrentPassword("6874");

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
        Thread.sleep(1500);
        startProgressBar();
        setStatusTextView("Перевірка наявності інтернету");
        if (!isInternetOk()) {
            setStatusTextView("Інтернет відсутній");
            stopProgressBar();
        } else {
            setStatusTextView("Авторизація");
            if (isItFirstEnter()) {
                setStatusTextView("Будь-ласка увійдіть");
                stopProgressBar();
                Thread.sleep(2000);
                showLogin();
            } else {
                try {
                    Person person = DbCache.getPerson();
                    if (person.getCard() == null) {
                        DbCache.markPersonOld();
                        setStatusTextView("Будь-ласка увійдіть");
                        stopProgressBar();
                        Thread.sleep(2000);
                        showLogin();
                    } else {
                        setStatusTextView("Вхід виконан");
                        stopProgressBar();
                        Thread.sleep(1000);
                        goToMain();
                    }
                } catch (Exception e) {
                    showLogin();
                }
            }
        }
    }


    private void showLogin() throws InterruptedException {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(LoginActivity.this,
                        R.anim.login_screen_trans_out);
                splashLayout.startAnimation(anim);
                splashLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                Animation anim2 = AnimationUtils.loadAnimation(LoginActivity.this,
                        R.anim.login_screen_trans_in);
                loginLayout.startAnimation(anim2);
            }
        });
    }

    private void rememberPassword() {
        if (textLogin.getText().toString().equals("")) {
            showSnackBar("Будь-ласка, введіть логін (номер угоди)");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Зачекайте будь-ласка...");
            progressDialog.show();
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    String result = SendInfo.rememberPassword(textLogin.getText().toString());
                    showSnackBar(result);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    private void showSnackBar(final String message) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(loginLayout,
                        message,
                        Snackbar.LENGTH_LONG).show();
            }
        });
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
        Web.sessionId = null;

        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textPassword.getWindowToken(), 0);

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    DbCache.markPersonOld();
                    Person person = DbCache.getPerson();
                    if (person.getCard() == null) {
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(loginLayout,
                                        "Неправильний логін або пароль",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        goToMain();
                    }
                } catch (Exception e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(loginLayout,
                                    "Помилка. Можливо нема зв'язку",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }

    public boolean isCurrentDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean isItFirstEnter() {
        boolean logIsEmpty = Settings.getCurrentLogin().equals("");
        boolean passIsEmpty = Settings.getCurrentPassword().equals("");
        return logIsEmpty && passIsEmpty;
    }

    private void stopProgressBar() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startProgressBar() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setStatusTextView(final String s) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                statusTextView.setText(s);
            }
        });
    }

    private boolean isInternetOk() {
        try {
            InetAddress address = InetAddress.getByName("www.o3.ua");
            address.getHostAddress();
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    /**
     * TESTING
     */

    private static boolean one;
    private static boolean two;
    private static boolean three;

    private void secret(int i){
        if (i==1 && !two && !three){
            one = true;
        }else if(one && i==2 && !three){
            two = true;
        }else if (one && two && i==3){
            three = true;
            showChoise();
        }else {
            one=two=three=false;
        }
    }

    private void showChoise() {
        final List<TestingUser> users = Testing2.users;
        String[] cloneForUsers = new String[users.size()];
        for (int i = 0; i < cloneForUsers.length; i++) {
            cloneForUsers[i] = users.get(i).desc;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(cloneForUsers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                TestingUser user = users.get(item);
                textLogin.setText(user.log);
                textPassword.setText(user.pass);
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(textLogin)){
            secret(2);
        }else if (v.equals(textPassword)){
            secret(1);
        }else if (v.equals(enterText)){
            secret(3);
        }
    }
}
