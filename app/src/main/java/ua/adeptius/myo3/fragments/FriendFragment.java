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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.dao.SendInfo;
import ua.adeptius.myo3.model.FriendInvite;

import static ua.adeptius.myo3.utils.Utilits.doTwoSymb;

public class FriendFragment  extends BaseFragment  {

    Button newFriendButton;
    List<FriendInvite> invites;

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
        TextView titleView = new TextView(context);
        titleView.setText("Хто ваш друг?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);

        final View layout = LayoutInflater.from(context).inflate(R.layout.item_friend_new, null);
        final EditText editSurname = (EditText) layout.findViewById(R.id.friend_familia);
        final EditText editName = (EditText) layout.findViewById(R.id.friend_name);
        final EditText editFatherName = (EditText) layout.findViewById(R.id.friend_surname);
        final EditText editPhone = (EditText) layout.findViewById(R.id.friend_phone);
        final EditText editAdress = (EditText) layout.findViewById(R.id.friend_adress);
        editSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editSurname.getText().toString().equals("Прізвище")){
                    editSurname.setText("");
                }else if (!hasFocus &&  editSurname.getText().toString().equals("")){
                    editSurname.setText("Прізвище");
                }
            }
        });
        editFatherName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editFatherName.getText().toString().equals("По-батькові")){
                    editFatherName.setText("");
                }else if (!hasFocus &&  editFatherName.getText().toString().equals("")){
                    editFatherName.setText("По-батькові");
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

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(titleView)
                .setView(layout)
                .setPositiveButton("Привести", null) //Set to null. We override the onclick
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String surname = editSurname.getText().toString();
                        String name = editName.getText().toString();
                        String fatherName = editFatherName.getText().toString();
                        String phone = editPhone.getText().toString();
                        String adress = editAdress.getText().toString();

                        if (phone.length() == 10){
                            phone = "+38"+phone;
                        }else if (phone.length() == 11){
                            phone = "+3"+phone;
                        }else if (phone.length() == 12){
                            phone = "+"+phone;
                        }

                        if ("".equals(surname) || "Прізвище".equals(surname) ||
                                "".equals(name) || "Ім'я".equals(name) ||
                                "".equals(fatherName) || "По-батькові".equals(fatherName) ||
                                "".equals(phone) || "Телефон".equals(phone) ||
                                "".equals(adress)  || "Адреса".equals(adress)){
                            makeSimpleSnackBar("Необхідно заповнити всі поля", layout);
                        }else if (phone.length() != 13){
                            makeSimpleSnackBar("Неправильний номер телефону", layout);
                        }else {

                            try{
                                surname = URLEncoder.encode(surname, "UTF-8");
                                name = URLEncoder.encode(name, "UTF-8");
                                fatherName = URLEncoder.encode(fatherName, "UTF-8");
                                adress = URLEncoder.encode(adress, "UTF-8");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            final HashMap<String, String> map = new HashMap<>();
                            map.put("surname", surname);
                            map.put("name", name);
                            map.put("fathername",fatherName);
                            map.put("phone", phone);
                            map.put("address", adress);
                            map.put("email", "");
                            EXECUTOR.submit(new Runnable() {
                                @Override
                                public void run() {
                                    String result = SendInfo.bringNewFriend(map);

                                    if ("Заявка створена!".equals(result)) {
                                        HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                        makeSimpleSnackBar("Заявка створена!", mainLayout);
                                        try{Thread.sleep(2500);}catch (Exception ignored){}
                                        reloadFragment();
                                    }else if ("Заявка по цьому номеру вже існує.".equals(result)){
                                        HANDLER.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        });
                                        makeSimpleSnackBar("Заявка по цьому номеру вже існує", layout);
                                    }else {
                                        makeSimpleSnackBar(result, layout);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    void doInBackground() throws Exception {
        invites = GetInfo.getFriendInvites();
        for (FriendInvite invite : invites) {
            System.out.println(invite);
        }
    }

    private void draw() {
        LinearLayout layout_for_invites = getLayout(R.id.layout_for_invites);
        for (FriendInvite invite : invites) {
            final View layout = LayoutInflater.from(context).inflate(R.layout.item_friend_invites, null);
            layout.setVisibility(View.VISIBLE);
            TextView date = (TextView) layout.findViewById(R.id.text_date);
            TextView status = (TextView) layout.findViewById(R.id.text_status);
            TextView name = (TextView) layout.findViewById(R.id.text_name);
            TextView adress = (TextView) layout.findViewById(R.id.text_address);
            TextView phone = (TextView) layout.findViewById(R.id.text_phone);

            int color = Color.parseColor("#2E7D32");
            if ("У розгляді".equals(invite.getStatus())){
                color = Color.parseColor("#0277BD");
            }else if ("Cкасовано".equals(invite.getStatus())){
                color = Color.parseColor("#9E9E9E");
            }
            date.setText(invite.getDate());
            date.setTextColor(color);
            status.setText(invite.getStatus());
            status.setTextColor(color);
            name.setText(invite.getFio());
            name.setTextColor(color);
            adress.setText(invite.getAdress());
            adress.setTextColor(color);
            phone.setText(invite.getPhone());
            phone.setTextColor(color);
            layout_for_invites.addView(layout);

        }

        

    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    @Override
    public void onClick(View v) {


    }


}
