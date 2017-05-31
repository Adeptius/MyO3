package ua.freenet.cabinet.activities;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.dao.Web;
import ua.freenet.cabinet.fragments.MainFragment;
import ua.freenet.cabinet.model.Operation;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.PreviouslyPerson;
import ua.freenet.cabinet.model.Testing;
import ua.freenet.cabinet.model.TestingUser;
import ua.freenet.cabinet.service.BackgroundService;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.EXECUTOR;
import static ua.freenet.cabinet.utils.Utilits.HANDLER;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout loginLayout;
    private EditText textLogin;
    private EditText textPassword;
    private LinearLayout splashLayout;
    private TextView statusTextView;
    private TextView titleText;
    private ProgressBar progressBar;

    private boolean fastLogin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!isMyServiceRunning(BackgroundService.class))
            startService(new Intent(LoginActivity.this, BackgroundService.class));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textLogin = (EditText) findViewById(R.id.input_login);
        textPassword = (EditText) findViewById(R.id.input_password);
        final Button buttonLogin = (Button) findViewById(R.id.button_login);
        TextView rememberPassButton = (TextView) findViewById(R.id.remember_password);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        loginLayout.setVisibility(View.GONE);

        splashLayout = (LinearLayout) findViewById(R.id.splash_layout);
        statusTextView = (TextView) findViewById(R.id.status_text_view);
        titleText = (TextView) findViewById(R.id.text_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInSplashScreen);
        progressBar.setVisibility(View.INVISIBLE);


        rememberPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rememberPassword();
            }
        });
        TextView enterText = (TextView) findViewById(R.id.enter_text);
        enterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textPassword.getText().toString().equals("4593")) {
                    showChoise();
                } else if (textPassword.getText().toString().equals("3954")) {
                    Testing.testAllUserEnter();
                }
            }
        });

        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!fastLogin)
                        Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startAnimation();
                try {
                    if (!fastLogin)
                        Thread.sleep(800);
                    checkAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


//      Ищем есть ли сохранённые логин\пасс
        final Set<PreviouslyPerson> previouslyPersons = Settings.getPreviouslyPersons();
        if (previouslyPersons.size() > 0) { // сохраненный логин и пасс есть
            buttonLogin.setText("Я входив раніше");
            textLogin.addTextChangedListener(new TextWatcher() { // если в поле логина будет вводится текст
                @Override                                        // то кнопка превращается в стандартную "увійти"
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    buttonLogin.setText("Увійти");
                    buttonLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            login(textLogin.getText().toString(), textPassword.getText().toString());
                        }
                    });
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final LinearLayout container = new LinearLayout(LoginActivity.this);
                    container.setOrientation(LinearLayout.VERTICAL);
                    final android.app.AlertDialog dialog = new MyAlertDialogBuilder(LoginActivity.this)
                            .setView(container)
                            .create();
                    dialog.show();
//                    наполняем контейнер итемами
                    for (final PreviouslyPerson person : previouslyPersons) {
                        final View layout = LayoutInflater.from(LoginActivity.this).inflate(R.layout.alert_item_person_choise, null);
                        TextView fioText = (TextView) layout.findViewById(R.id.text_fio);
                        TextView cardText = (TextView) layout.findViewById(R.id.text_card);
                        TextView addressText = (TextView) layout.findViewById(R.id.text_address);
                        ImageView remove = (ImageView) layout.findViewById(R.id.remove_button);
                        fioText.setText(person.getFio());
                        cardText.setText("Договір: " + person.getCard());
                        addressText.setText(person.getAddress());
                        remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (doubleBackToExitPressedOnce) {
                                    previouslyPersons.remove(person);
                                    Settings.setPreviouslyPersons(previouslyPersons);
                                    container.removeView(layout);
                                    if (previouslyPersons.size()==0){
                                        dialog.dismiss();
                                        buttonLogin.setText("Увійти");
                                        buttonLogin.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                login(textLogin.getText().toString(), textPassword.getText().toString());
                                            }
                                        });
                                    }
                                    return;
                                }

                                doubleBackToExitPressedOnce = true;
                                Snackbar.make(container, "Ще раз для видалення", Snackbar.LENGTH_SHORT).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        doubleBackToExitPressedOnce = false;
                                    }
                                }, 2000);

                            }
                        });

                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                login(person.getCard(), person.getPass());
                            }
                        });
                        container.addView(layout);
                    }
                }
            });
        } else { // сохраненного пароля нет.
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login(textLogin.getText().toString(), textPassword.getText().toString());
                }
            });
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    private void checkAll() throws InterruptedException {
        startProgressBar();
        setStatusTextView("Перевірка наявності інтернету");
        if (!fastLogin)
            Thread.sleep(100);
        if (!isInternetOk()) {
            setStatusTextView("Інтернет відсутній");
            stopProgressBar();
        } else {
            setStatusTextView("Авторизація");
            if (!fastLogin)
                Thread.sleep(100);
            if (isItFirstEnter()) {
                setStatusTextView("Будь-ласка, увійдіть");
                stopProgressBar();
                if (!fastLogin)
                    Thread.sleep(1000);
                showLogin();
            } else {
                try {
                    if (GetInfo.checkLoginAndSaveSessionIfTrue()) {
                        setStatusTextView("Завантаження данних..");
                        if (!fastLogin)
                            Thread.sleep(100);
                        DbCache.getPerson();
                        DbCache.getIps();
                        DbCache.getMountlyFeefromLK();
                        stopProgressBar();
                        setStatusTextView("Вхід виконано");
                        if (!fastLogin)
                            Thread.sleep(300);
                        goToMain();
                    } else {
                        setStatusTextView("Будь-ласка увійдіть");
                        stopProgressBar();
                        if (!fastLogin)
                            Thread.sleep(1000);
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
            new MyAlertDialogBuilder(this)
                    .setMessage("Будь-ласка, введіть логін (номер договору)")
                    .setNegativeButtonForClose("Ок")
                    .createAndShow();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Зачекайте будь-ласка...");
            progressDialog.show();
            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    final String result = SendInfo.rememberPassword(textLogin.getText().toString());
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            final InputMethodManager imm = (InputMethodManager) LoginActivity.this
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(textPassword.getWindowToken(), 0);
                            new MyAlertDialogBuilder(LoginActivity.this)
                                    .setMessage(result)
                                    .setNegativeButtonForClose("Ок")
                                    .createAndShow();
                        }
                    });
                }
            });
        }
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
        } catch (InterruptedException ignore) {
        }
    }

    public void login(String login, String password) {
        Settings.setCurrentLogin(login);
        Settings.setCurrentPassword(password);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Авторизація...");
        progressDialog.show();
        Web.sessionId = null;

        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textPassword.getWindowToken(), 0);

        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (GetInfo.checkLoginAndSaveSessionIfTrue()) {
                        Person person = DbCache.getPerson();
                        DbCache.getIps();
                        DbCache.getMountlyFeefromLK();
                        Set<PreviouslyPerson> previouslyPersons = Settings.getPreviouslyPersons();
                        String address = person.getAddress().getStrNameUa() + " " + person.getAddress().gethName();
                        previouslyPersons.add(new PreviouslyPerson(person.getFIO(), person.getCard(), Settings.getCurrentPassword(), address));
                        Settings.setPreviouslyPersons(previouslyPersons);
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
        if (!"".equals(message)) {
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
