package ua.adeptius.myo3.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.SendInfo;

import static ua.adeptius.myo3.utils.Utilits.doTwoSymb;

public class FriendFragment  extends BaseFragment  {

    Button newFriendButton;

    @Override
    void setAllSettings() {
        titleText = "Приведи друга";
        descriptionText = "";
        fragmentId = R.layout.fragment_friend;
        titleImage = R.drawable.background_friend;
        layoutId = R.id.main_friend;
    }

    @Override
    void init() {
        newFriendButton = getButton(R.id.button_new_friend);
        newFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewFriendForm();
            }
        });
        hideAllViewsInMainScreen();
    }

    private void showNewFriendForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);

        TextView titleView = new TextView(context);
        titleView.setText("Хто ваш друг?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        builder.setCustomTitle(titleView);

        View layout = LayoutInflater.from(context).inflate(R.layout.item_friend_new, null);
        final EditText editFamilia = (EditText) layout.findViewById(R.id.friend_familia);
        final EditText editName = (EditText) layout.findViewById(R.id.friend_name);
        final EditText editSurname = (EditText) layout.findViewById(R.id.friend_surname);
        final EditText editPhone = (EditText) layout.findViewById(R.id.friend_phone);
        final EditText editAdress = (EditText) layout.findViewById(R.id.friend_adress);
        editFamilia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editFamilia.getText().toString().equals("Фамілія")){
                    editFamilia.setText("");
                }else if (!hasFocus &&  editFamilia.getText().toString().equals("")){
                    editFamilia.setText("Фамілія");
                }
            }
        });
        editSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editSurname.getText().toString().equals("По-батькові")){
                    editSurname.setText("");
                }else if (!hasFocus &&  editSurname.getText().toString().equals("")){
                    editSurname.setText("По-батькові");
                }
            }
        });
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editName.getText().toString().equals("Ім'я")){
                    editName.setText("");
                }else if (!hasFocus &&  editName.getText().toString().equals("")){
                    editName.setText("Ім'я");
                }
            }
        });
        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editPhone.getText().toString().equals("Телефон")){
                    editPhone.setText("");
                }else if (!hasFocus &&  editPhone.getText().toString().equals("")){
                    editPhone.setText("Телефон");
                }
            }
        });
        editAdress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editAdress.getText().toString().equals("Адреса")){
                    editAdress.setText("");
                }else if (!hasFocus &&  editAdress.getText().toString().equals("")){
                    editAdress.setText("Адреса");
                }
            }
        });

        builder.setView(layout);
        builder.setCustomTitle(titleView);
        builder.setPositiveButton("Привести", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String familia = editFamilia.getText().toString();
                String name = editFamilia.getText().toString();
                String surname = editFamilia.getText().toString();
                String phone = editFamilia.getText().toString();
                String adress = editFamilia.getText().toString();

                if ("".equals(familia)){
                    makeSimpleSnackBar("Введіть ім'я", mainLayout);
                }











            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    void doInBackground() throws Exception {
        draw();
        animateScreen();
    }

    private void draw() {

    }

    @Override
    void processIfOk() {

    }

    @Override
    public void onClick(View v) {


    }


}
