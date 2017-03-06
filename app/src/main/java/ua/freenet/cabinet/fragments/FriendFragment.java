package ua.freenet.cabinet.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.SendInfo;
import ua.freenet.cabinet.model.FriendInvite;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

public class FriendFragment extends HelperFragment {

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

    @Override
    void doInBackground() throws Exception {
        invites = DbCache.getFriendInvites();
    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    private void showNewFriendForm() {
        final View layout = LayoutInflater.from(context).inflate(R.layout.alert_friend_new, null);
        final EditText editSurname = (EditText) layout.findViewById(R.id.friend_familia);
        final EditText editName = (EditText) layout.findViewById(R.id.friend_name);
        final EditText editFatherName = (EditText) layout.findViewById(R.id.friend_surname);
        final EditText editPhone = (EditText) layout.findViewById(R.id.friend_phone);
        final EditText editAdress = (EditText) layout.findViewById(R.id.friend_adress);
        editSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editSurname.getText().toString().equals("Прізвище")) {
                    editSurname.setText("");
                } else if (!hasFocus && editSurname.getText().toString().equals("")) {
                    editSurname.setText("Прізвище");
                }
            }
        });
        editFatherName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editFatherName.getText().toString().equals("По-батькові")) {
                    editFatherName.setText("");
                } else if (!hasFocus && editFatherName.getText().toString().equals("")) {
                    editFatherName.setText("По-батькові");
                }
            }
        });
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editName.getText().toString().equals("Ім'я")) {
                    editName.setText("");
                } else if (!hasFocus && editName.getText().toString().equals("")) {
                    editName.setText("Ім'я");
                }
            }
        });
        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editPhone.getText().toString().equals("Телефон")) {
                    editPhone.setText("");
                } else if (!hasFocus && editPhone.getText().toString().equals("")) {
                    editPhone.setText("Телефон");
                }
            }
        });
        editAdress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editAdress.getText().toString().equals("Адреса")) {
                    editAdress.setText("");
                } else if (!hasFocus && editAdress.getText().toString().equals("")) {
                    editAdress.setText("Адреса");
                }
            }
        });

        final MyAlertDialogBuilder builder = new MyAlertDialogBuilder(context);
        builder.setTitleText("Хто ваш друг?")
                .setView(layout)
                .createShowAndSetPositiveForExecutor("Привести", new Runnable() {
                    @Override
                    public void run() {
                        String surname = editSurname.getText().toString();
                        String name = editName.getText().toString();
                        String fatherName = editFatherName.getText().toString();
                        String phone = editPhone.getText().toString();
                        String adress = editAdress.getText().toString();

                        if (phone.length() == 10) {
                            phone = "+38" + phone;
                        } else if (phone.length() == 11) {
                            phone = "+3" + phone;
                        } else if (phone.length() == 12) {
                            phone = "+" + phone;
                        }

                        if ("".equals(surname) || "Прізвище".equals(surname) ||
                                "".equals(name) || "Ім'я".equals(name) ||
                                "".equals(fatherName) || "По-батькові".equals(fatherName) ||
                                "".equals(phone) || "Телефон".equals(phone) ||
                                "".equals(adress) || "Адреса".equals(adress)) {
                            makeSimpleSnackBar("Необхідно заповнити всі поля", layout);
                        } else if (phone.length() != 13) {
                            makeSimpleSnackBar("Неправильний номер телефону", layout);
                        } else {
                            try {
                                surname = URLEncoder.encode(surname, "UTF-8");
                                name = URLEncoder.encode(name, "UTF-8");
                                fatherName = URLEncoder.encode(fatherName, "UTF-8");
                                adress = URLEncoder.encode(adress, "UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            final HashMap<String, String> map = new HashMap<>();
                            map.put("surname", surname);
                            map.put("name", name);
                            map.put("fathername", fatherName);
                            map.put("phone", phone);
                            map.put("address", adress);
                            map.put("email", "");

                            progressDialogShow();

                            String result = SendInfo.bringNewFriend(map);
                            if ("Заявка створена!".equals(result)) {
                                DbCache.markFriendInvitesOld();
                                builder.closeWithHandler();
                                progressDialogWaitStopShowMessageReload("Заявка створена!", mainLayout);
                            } else if ("Заявка по цьому номеру вже існує.".equals(result)) {
                                builder.closeWithHandler();
                                progressDialogStopAndShowMessage("Заявка по цьому номеру вже існує", layout);
                            } else {
                                progressDialogStopAndShowMessage(result, layout);
                            }
                        }
                    }
                });
    }


    private void draw() {
        LinearLayout layout_for_invites = getLayout(R.id.layout_for_invites);
        for (FriendInvite invite : invites) {
            final View layout = LayoutInflater.from(context).inflate(R.layout.item_friend_invites, null);
            layout_for_invites.setVisibility(View.VISIBLE);
            TextView date = (TextView) layout.findViewById(R.id.text_date);
            TextView status = (TextView) layout.findViewById(R.id.text_status);
            TextView name = (TextView) layout.findViewById(R.id.text_name);
            TextView adress = (TextView) layout.findViewById(R.id.text_address);
            TextView phone = (TextView) layout.findViewById(R.id.text_phone);

            int color = Color.parseColor("#2E7D32");
            if ("У розгляді".equals(invite.getStatus())) {
                color = Color.parseColor("#0277BD");
            } else if ("Cкасовано".equals(invite.getStatus())) {
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
        if (invites.size() == 0) {
            layout_for_invites.setVisibility(View.GONE);
        }
    }
}
