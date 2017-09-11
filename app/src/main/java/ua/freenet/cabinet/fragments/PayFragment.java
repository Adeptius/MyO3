package ua.freenet.cabinet.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

public class PayFragment extends HelperFragment {

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
        prepareViews();
    }

    @Override
    void processIfOk() {
        showAndAnimatePreparedViews();
    }

    private void prepareViews() {
        final View iPayViewContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView iPayView = (ImageView) iPayViewContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(iPayView, R.drawable.i_pay);
        iPayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("iPayView");
            }
        });
        preparedViews.add(iPayViewContainer);


        final View eCommerceConnectContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView eCommerceConnect = (ImageView) eCommerceConnectContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(eCommerceConnect, R.drawable.e_commerce);
        eCommerceConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("eCommerceConnect");
            }
        });
        preparedViews.add(eCommerceConnectContainer);


        final View payMasterContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView payMaster = (ImageView) payMasterContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(payMaster, R.drawable.pay_master3);
        payMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("payMaster");
            }
        });
        preparedViews.add(payMasterContainer);


        final View tachCardContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView tachCard = (ImageView) tachCardContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(tachCard, R.drawable.tachcard);
        tachCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("tachCard");
            }
        });
        preparedViews.add(tachCardContainer);


        final View platonContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView platon = (ImageView) platonContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(platon, R.drawable.platon);
        platon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askHowMuch("platon");
            }
        });
        preparedViews.add(platonContainer);


        final View iBoxContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView iBox = (ImageView) iBoxContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(iBox, R.drawable.ibox);
        iBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser("https://www.ibox.ua/?section=maps");
            }
        });
        preparedViews.add(iBoxContainer);


        final View easyPayContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView easyPay = (ImageView) easyPayContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(easyPay, R.drawable.easy_pay);
        easyPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser("https://map.easypay.ua:8465/");
            }
        });
        preparedViews.add(easyPayContainer);


        final View tymeContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView tyme = (ImageView) tymeContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(tyme, R.drawable.tyme);
        tyme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser("https://tyme.ua/ru/clients/where/");
            }
        });
        preparedViews.add(tymeContainer);


        final View privatBankContainer = LayoutInflater.from(context).inflate(R.layout.item_pay, null);
        ImageView privatBank = (ImageView) privatBankContainer.findViewById(R.id.pay_image_view);
        addImageToViewFromResources(privatBank, R.drawable.privat);
        privatBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser("https://secure.privatbank.ua/freenet");
            }
        });
        preparedViews.add(privatBankContainer);
    }

    private void addImageToViewFromResources(ImageView view, int image) {
        final Bitmap loadedBitMap = BitmapFactory
                .decodeResource(getResources(), image);

        double y = loadedBitMap.getHeight();
        double x = loadedBitMap.getWidth();

        int currentX = (int) (mainLayout.getWidth() * 0.93);
        double ratio = y / x;

        view.getLayoutParams().height = (int) (currentX * ratio);
        view.setImageBitmap(loadedBitMap);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void askHowMuch(final String system) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView before = new TextView(context);
        before.setText(" Поповнити рахунок на");
        before.setTypeface(null, Typeface.BOLD);
        before.setTextSize(18);

        final EditText text = new EditText(context);
        text.setWidth((int) Utilits.dpToPixel(50, context));
        text.setCursorVisible(true);
        text.hasFocus();
        text.setInputType(InputType.TYPE_CLASS_NUMBER);

        TextView after = new TextView(context);
        after.setText("грн");
        after.setTypeface(null, Typeface.BOLD);
        after.setTextSize(18);

        layout.addView(before);
        layout.addView(text);
        layout.addView(after);
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        new MyAlertDialogBuilder(context)
                .setView(layout)
                .setPositiveButtonWithRunnableForExecutor("Поповнити", new Runnable() {
                    @Override
                    public void run() {
                        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                        String add = text.getText().toString();
                        if (add.equals("")){
                            add = "0";
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("https://o3.ua/content/files/redirecttopost(1).html?url=");

                        if ("iPayView".equals(system)) {
                            sb.append("https://paygate.freenet.ua/iPay/sign.php");
                            sb.append("&card=").append(person.getCard());
                            sb.append("&add=").append(add);
                        } else if ("eCommerceConnect".equals(system)) {
                            sb.append("https://paygate.freenet.ua/ecommerce/sign.php");
                            sb.append("&id2=").append(String.valueOf(person.getId()));
                            sb.append("&add=").append(add);
                        } else if ("payMaster".equals(system)) {
                            sb.append("https://paygate.freenet.ua/webmoney/init.php");
                            sb.append("&pid=").append(String.valueOf(person.getId()));
                            sb.append("&add=").append(add);
                        } else if ("tachCard".equals(system)){
                            openInBrowser("https://user.tachcard.com/en/pay-freenet?account="+person.getCard()+"&amount=" + add);
                            return;
                        }else {
                            sb.append("https://paygate.freenet.ua/platon/init.php");
                            sb.append("&account=").append(Settings.getCurrentLogin());
                            sb.append("&p_id=").append(String.valueOf(person.getId()));
                            double cost = Double.parseDouble(add);
                            double commision = (cost * 1.01) / 100;
                            cost = cost + commision;
                            add = getMoneyText(cost);
                            sb.append("&amount=").append(add);
                        }
                        openInBrowser(sb.toString());
                    }
                }).createAndShow();
    }

    public String getMoneyText(double in) {
        String many = String.valueOf(in);
        int pos = many.indexOf(".") + 3;
        boolean cont = many.contains(".");
        int len = many.length();

        if (cont && len >= pos) {
            many = many.substring(0, many.indexOf(".") + 3);
        } else if (cont && len >= pos - 1) {
            many = many + "0";
        }
        return many;
    }
}
