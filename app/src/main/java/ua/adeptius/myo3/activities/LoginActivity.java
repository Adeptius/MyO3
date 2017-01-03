package ua.adeptius.myo3.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import ua.adeptius.myo3.R;

// TODO сделать логин
public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        loginView = (EditText) findViewById(R.id.login_view);
//        passwordView = (EditText) findViewById(R.id.password_view);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Button enterButton = (Button) findViewById(R.id.email_sign_in_button);

        goToMain();
    }

    public void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.finish();
        startActivity(intent);
    }
}
