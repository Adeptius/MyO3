package ua.freenet.cabinet.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ua.freenet.cabinet.R;

import static ua.freenet.cabinet.fragments.BaseFragment.COLOR_BLUE;
import static ua.freenet.cabinet.utils.Utilits.EXECUTOR;
import static ua.freenet.cabinet.utils.Utilits.HANDLER;


public class MyAlertDialogBuilder extends AlertDialog.Builder {

    private Context context;
    private AlertDialog savedDialog;

    public MyAlertDialogBuilder(Context context) {
        super(context);
        this.context = context;
        setCancelable(true);
    }


    public MyAlertDialogBuilder setView(View view) {
        super.setView(view);
        return this;
    }


    public MyAlertDialogBuilder setMessage(String message) {
        super.setMessage(message);
        return this;
    }

    public MyAlertDialogBuilder setMessageText(String message){
        View textLayout = LayoutInflater.from(context).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(message);
        super.setView(textLayout);
        return this;
    }


    public MyAlertDialogBuilder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
        super.setItems(items, listener);
        return this;
    }

    public MyAlertDialogBuilder setPositiveButtonForClose(String positiveButtonName) {
        super.setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return this;
    }

    public MyAlertDialogBuilder setPositiveButtonWithRunnableForExecutor(String positiveButtonName, final Runnable runnable) {
        super.setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EXECUTOR.submit(runnable);
            }
        });
        return this;
    }


    public MyAlertDialogBuilder setPositiveButtonWithRunnableForHandler(String positiveButtonName, final Runnable runnable) {
        super.setPositiveButton(positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HANDLER.post(runnable);
            }
        });
        return this; // сам закрывает диалог по нажатии на позитивную кнопку
    }


    public MyAlertDialogBuilder setNegativeButtonForClose(String negativeText) {
        super.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return this;
    }

    public MyAlertDialogBuilder setTitleText(String title) {
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        super.setCustomTitle(titleView);
        return this;
    }

    public MyAlertDialogBuilder setTitleTextWithWhiteBackground(String title) {
        TextView titleView = new TextView(context);
        titleView.setText(title);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(COLOR_BLUE);
        titleView.setBackgroundColor(Color.WHITE);
        super.setCustomTitle(titleView);
        return this;
    }

    @Override
    public MyAlertDialogBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        super.setPositiveButton(text, listener);
        return this;
    }

    @Override
    public AlertDialog create() {
        final AlertDialog dialog = super.create();
        savedDialog = dialog;
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(COLOR_BLUE);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(COLOR_BLUE);
            }
        });
        return dialog;
    }

    public void showDialog(){
        savedDialog.show();
    }


    public MyAlertDialogBuilder createAndShow(){
        create().show();
        return this;
    }

    public MyAlertDialogBuilder close(){
        savedDialog.dismiss();
        return this;
    }

    public MyAlertDialogBuilder closeWithHandler(){
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                close();
            }
        });
        return this;
    }

    public AlertDialog createShowAndSetPositiveForExecutor(String positiveButton, final Runnable runnable){
        return createShowAndSetPositiveFor(positiveButton, runnable, true);
    }

    public AlertDialog createShowAndSetPositiveForHandler(String positiveButton, final Runnable runnable){
        return createShowAndSetPositiveFor(positiveButton, runnable, false);
    }

    private AlertDialog createShowAndSetPositiveFor(String positiveButton, final Runnable runnable, final boolean inExecutor){
        super.setPositiveButton(positiveButton, null);
        final AlertDialog dialog = create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                Button button = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                button.setTextColor(COLOR_BLUE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(COLOR_BLUE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inExecutor){
                            EXECUTOR.submit(runnable);
                        }else {
                            HANDLER.post(runnable);
                        }
                    }
                });
            }
        });
        dialog.show();// не закрывает диалог
        return dialog;//выносим builder в переменную и вызываем .close в нужный момент
    }

    public MyAlertDialogBuilder setCanceledOnTouchOutside(boolean cancel){
        savedDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
}
