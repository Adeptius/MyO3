package ua.freenet.cabinet.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.dao.Web;
import ua.freenet.cabinet.model.Testing;
import ua.freenet.cabinet.model.TestingUser;
import ua.freenet.cabinet.service.BackgroundService;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.EXECUTOR;
import static ua.freenet.cabinet.utils.Utilits.HANDLER;

public class LoginActivity extends AppCompatActivity {

    ImageView logoImageView;
    private LinearLayout loginLayout;
    private EditText textLogin;
    private EditText textPassword;
    private Button buttonLogin;
    private TextView rememberPassButton;
    private LinearLayout splashLayout;

    private TextView statusTextView;
    private TextView titleText;
//    private TextView comentText;
    private ProgressBar progressBar;

    private TextView enterText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!isMyServiceRunning(BackgroundService.class))
            startService(new Intent(LoginActivity.this, BackgroundService.class));

        logoImageView = (ImageView) findViewById(R.id.logoView);
        textLogin = (EditText) findViewById(R.id.input_login);
        textPassword = (EditText) findViewById(R.id.input_password);
        buttonLogin = (Button) findViewById(R.id.button_login);
        rememberPassButton = (TextView) findViewById(R.id.remember_password);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        loginLayout.setVisibility(View.GONE);

        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        statusTextView = (TextView) findViewById(R.id.status_text_view);
        titleText = (TextView) findViewById(R.id.text_title);
//        comentText = (TextView) findViewById(R.id.text_coment);
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
        enterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textPassword.getText().toString().equals("4593")){
                    showChoise();
                }
            }
        });

        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startAnimation();
                try {
                    Thread.sleep(1000);
                    checkAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkAll() throws InterruptedException {

        startProgressBar();
        setStatusTextView("Перевірка наявності інтернету");
        Utilits.check();
        Thread.sleep(400);
        if (!isInternetOk()) {
            setStatusTextView("Інтернет відсутній");
            stopProgressBar();
        } else {
            setStatusTextView("Авторизація");
            Thread.sleep(400);
            if (isItFirstEnter()) {
                setStatusTextView("Будь-ласка увійдіть");
                stopProgressBar();
                Thread.sleep(2000);
                showLogin();
            } else {
                try {
                    if (Web.checkLoginAndSaveSessionIfTrue()) {
                        setStatusTextView("Завантаження данних..");
                        Thread.sleep(400);
                        DbCache.getPerson();
                        DbCache.getIps();
                        DbCache.getMountlyFeefromLK();
                        stopProgressBar();
                        setStatusTextView("Вхід виконано");
                        Thread.sleep(1000);
                        goToMain();
                    } else {
                        setStatusTextView("Будь-ласка увійдіть");
                        stopProgressBar();
                        Thread.sleep(2000);
                        showLogin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    setStatusTextView("Помилка зв'язку");
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
        try {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    titleText.setVisibility(View.INVISIBLE);
                    titleText.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(LoginActivity.this,
                            R.anim.splash_screen_scale);
                    titleText.startAnimation(anim);
                }
            });
            Thread.sleep(300);
        } catch (InterruptedException ignore) {}
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
                    if (Web.checkLoginAndSaveSessionIfTrue()) {
                        DbCache.getPerson();
                        DbCache.getIps();
                        DbCache.getMountlyFeefromLK();
                        goToMain();
                    } else {
                        Settings.clearAllData();
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(loginLayout,
                                        "Неправильний логін або пароль",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Settings.clearAllData();
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

        Intent intentIn = getIntent();
        String message = intentIn.getStringExtra("notAnothMessage");
        if (!"".equals(message)){
            intent.putExtra("notAnothMessage", message);
        }

        this.finish();
        startActivity(intent);
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * TESTING
     */
    private void showChoise() {
        final List<TestingUser> users = Testing.users;
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
}