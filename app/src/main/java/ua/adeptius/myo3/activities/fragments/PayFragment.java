package ua.adeptius.myo3.activities.fragments;


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

import java.util.HashMap;
import java.util.Map;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.persons.Person;
import ua.adeptius.myo3.utils.Utilits;

public class PayFragment extends BaseFragment {

    HashMap<ImageView, Integer> viewsAndImgId = new HashMap<>();
    private Person person;

    @Override
    void init() {
        titleText = "Оплата послуг";
        descriptionText = "";
        mainLayout = (LinearLayout) baseView.findViewById(R.id.scroll_view_balance);
        drawAll();
    }

    @Override
    void doInBackground() throws Exception {
        person = GetInfo.getPersonInfo();
    }

    @Override
    void processIfOk() {
        drawIcons();
    }

    @Override
    void processIfFail() {

    }

    private void drawAll() {

        View iPayViewContainer = LayoutInflater.from(context).inflate(R.layout.fragment_pay_item, null);
        ImageView iPayView = (ImageView) iPayViewContainer.findViewById(R.id.pay_image_view);
        viewsAndImgId.put(iPayView, R.drawable.i_pay_3);
        iPayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("iPayView");
            }
        });
        mainLayout.addView(iPayViewContainer);

        View eCommerceConnectContainer = LayoutInflater.from(context).inflate(R.layout.fragment_pay_item, null);
        ImageView eCommerceConnect = (ImageView) eCommerceConnectContainer.findViewById(R.id.pay_image_view);
        viewsAndImgId.put(eCommerceConnect, R.drawable.e_commerce3);
        eCommerceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("eCommerceConnect");
            }
        });
        mainLayout.addView(eCommerceConnectContainer);


        View payMasterContainer = LayoutInflater.from(context).inflate(R.layout.fragment_pay_item, null);
        ImageView payMaster = (ImageView) payMasterContainer.findViewById(R.id.pay_image_view);
        viewsAndImgId.put(payMaster, R.drawable.pay_master2);
        payMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("payMaster");
            }
        });
        mainLayout.addView(payMasterContainer);


        View privatBankContainer = LayoutInflater.from(context).inflate(R.layout.fragment_pay_item, null);
        ImageView privatBank = (ImageView) privatBankContainer.findViewById(R.id.pay_image_view);
        viewsAndImgId.put(privatBank, R.drawable.privat_bank4);
        privatBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://secure.privatbank.ua/freenet"));
                startActivity(i);
            }
        });
        mainLayout.addView(privatBankContainer);


        View platonContainer = LayoutInflater.from(context).inflate(R.layout.fragment_pay_item, null);
        ImageView platon = (ImageView) platonContainer.findViewById(R.id.pay_image_view);
        viewsAndImgId.put(platon, R.drawable.platon2);
        platon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("platon");
            }
        });
        mainLayout.addView(platonContainer);


    }

    private void drawIcons() {
        for (Map.Entry<ImageView, Integer> entry : viewsAndImgId.entrySet()) {
            final ImageView view = entry.getKey();
            final Bitmap loadedBitMap = BitmapFactory
                    .decodeResource(getResources(), entry.getValue());

            double y = loadedBitMap.getHeight();
            double x = loadedBitMap.getWidth();

            int currentX = view.getWidth();
            double ratio = y / x;
            int needY = (int) (currentX * ratio);

            view.getLayoutParams().height = needY;
            view.setImageBitmap(loadedBitMap);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
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
    int setFragmentId() {
        return R.layout.fragment_base_scrolling;
    }

    @Override
    int setLayoutId() {
        return R.id.scroll_view_balance;
    }

    @Override
    public void onClick(View v) {

    }
}
