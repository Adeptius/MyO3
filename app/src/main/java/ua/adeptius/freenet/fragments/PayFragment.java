package ua.adeptius.freenet.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.adeptius.freenet.R;
import ua.adeptius.freenet.dao.DbCache;
import ua.adeptius.freenet.utils.Settings;
import ua.adeptius.freenet.model.Person;
import ua.adeptius.freenet.utils.Utilits;

//TODO добавить карту терминалов
public class PayFragment extends BaseFragment {

    private Person person;

    @Override
    void setAllSettings() {
        titleText = "Поповнення рахунку";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_pay2;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {


    }

    @Override
    void doInBackground() throws Exception {
        person = DbCache.getPerson();
        prepareAll();
    }

    @Override
    void processIfOk() {
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void prepareAll() {
        final View iPayViewContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView iPayView = (ImageView) iPayViewContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(iPayView, R.drawable.i_pay);
        iPayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("iPayView");
            }
        });

        final View eCommerceConnectContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView eCommerceConnect = (ImageView) eCommerceConnectContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(eCommerceConnect, R.drawable.e_commerce);
        eCommerceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("eCommerceConnect");
            }
        });

        final View payMasterContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView payMaster = (ImageView) payMasterContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(payMaster,  R.drawable.pay_master3);
        payMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("payMaster");
            }
        });

        final View platonContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView platon = (ImageView) platonContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(platon,  R.drawable.platon);
        platon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("platon");
            }
        });

        final View iBoxContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView iBox = (ImageView) iBoxContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(iBox,  R.drawable.ibox);
        iBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.ibox.ua/?section=maps"));
                startActivity(i);
            }
        });

        final View easyPayContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView easyPay = (ImageView) easyPayContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(easyPay,  R.drawable.easy_pay);
        easyPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://map.easypay.ua:8465/"));
                startActivity(i);
            }
        });

        final View tymeContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView tyme = (ImageView) tymeContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(tyme,  R.drawable.tyme);
        tyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://tyme.ua/ru/clients/where/"));
                startActivity(i);
            }
        });

        final View privatBankContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView privatBank = (ImageView) privatBankContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(privatBank,  R.drawable.privat);
        privatBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://secure.privatbank.ua/freenet"));
                startActivity(i);
            }
        });

        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                mainLayout.addView(iPayViewContainer);
                mainLayout.addView(eCommerceConnectContainer);
                mainLayout.addView(payMasterContainer);
                mainLayout.addView(platonContainer);
                mainLayout.addView(iBoxContainer);
                mainLayout.addView(easyPayContainer);
                mainLayout.addView(tymeContainer);
                mainLayout.addView(privatBankContainer);
            }
        });
    }

    private void addImageToViewFromResources(ImageView view, int image) {
            final Bitmap loadedBitMap = BitmapFactory
                    .decodeResource(getResources(), image);

            double y = loadedBitMap.getHeight();
            double x = loadedBitMap.getWidth();

            int currentX = (int) (mainLayout.getWidth() * 0.93);
            double ratio = y / x;
            int needY = (int) (currentX * ratio);

            view.getLayoutParams().height = needY;
            view.setImageBitmap(loadedBitMap);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void askHowMuch(final String system) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView before = new TextView(context);
        before.setText("Поповнити рахунок на");
        before.setTypeface(null, Typeface.BOLD);
        before.setTextSize(18);

        TextView after = new TextView(context);
        after.setText("грн");
        after.setTypeface(null, Typeface.BOLD);
        after.setTextSize(18);

        final EditText text = new EditText(context);
        text.setWidth((int) Utilits.dpToPixel(50,context));
        text.setCursorVisible(true);
        text.hasFocus();

        layout.addView(before);
        layout.addView(text);
        layout.addView(after);
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setCancelable(true);
        builder.setView(layout);
        builder.setPositiveButton("Поповнити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                String url, key1,key2,value1,value2;
                if("iPayView".equals(system)){
                    url = "https://paygate.freenet.ua/iPay/sign.php";
                    key1 = "card";
                    key2 = "add";
                    value1 = Settings.getCurrentLogin();
                    value2 = text.getText().toString();
                }else if("eCommerceConnect".equals(system)){
                    url = "https://paygate.freenet.ua/ecommerce/sign.php";
                    key1 = "id2";
                    key2 = "add";
                    value1 = String.valueOf(person.getId());
                    value2 = text.getText().toString();
                }else if("payMaster".equals(system)){
                    url = "https://paygate.freenet.ua/webmoney/init.php";
                    key1 = "pid";
                    key2 = "add";
                    value1 = String.valueOf(person.getId());
                    value2 = text.getText().toString();
                }else {
                    url = "https://paygate.freenet.ua/platon/init.php";
                    key1 = "account";
                    key2 = "p_id";
                    String key3 = "amount";
                    value1 = Settings.getCurrentLogin();
                    value2 = String.valueOf(person.getId());
                    String value3 = text.getText().toString();

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String formedUrl = String.format("http://e404.ho.ua/myo3/redirect/platon" +
                            ".html?url=%s&%s=%s&%s=%s&%s=%s", url, key1, value1, key2, value2, key3, value3);
                    System.out.println(formedUrl);
                    i.setData(Uri.parse(formedUrl));
                    startActivity(i);
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                String formedUrl = String.format("http://e404.ho.ua/myo3/redirect/redirectToPost" +
                                      ".html?url=%s&%s=%s&%s=%s", url, key1, value1, key2, value2);
                i.setData(Uri.parse(formedUrl));
                startActivity(i);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }
}
