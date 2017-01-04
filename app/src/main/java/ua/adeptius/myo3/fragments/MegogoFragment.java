package ua.adeptius.myo3.fragments;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.MegogoPts;

public class MegogoFragment extends BaseFragment {

    private List<MegogoPts> megogoPts;

    @Override
    void setAllSettings() {
        titleText = "";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        titleImage = R.drawable.background_megogo11;
        layoutId = R.id.base_scroll_view;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        megogoPts = GetInfo.getMegogoPts();
        for (MegogoPts megogoPt : megogoPts) {
            System.out.println(megogoPt);
        }
    }

    @Override
    void processIfOk() {
        draw();
        animateScreen();
    }

    private void draw() {
        StringBuilder sb = new StringBuilder();
        sb.append("Національні телеканали");
        sb.append("\nІнтерактивні канали MEGOGO");
        sb.append("\nВідключення всієї реклами на MEGOGO");

        Button lightButton = addMegogoLayoutAndreturnItsButton(
                "Підписка легка", sb.toString(), null, 37);
        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sb.append("\n+Колекція кращих фільмів та мультфільмів М");
        Button optimalButton = addMegogoLayoutAndreturnItsButton(
                "Підписка оптимальна", sb.toString(), "Безкоштовно перші 30 днів*", 77);
        optimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        sb.append("\n++Закордонні телеканали");
        Button maximumButton = addMegogoLayoutAndreturnItsButton(
                "Підписка максимальна", sb.toString(), null, 147);
        maximumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String viasat = "В пакет входять 2 телеканали TV1000 Premium HD и TV1000 Megahit HD с " +
                "гарячими хітами кінопрокату від найкрупніших голлівудських кіностудій.";
        Button viasatButton = addMegogoLayoutAndreturnItsButton(
                "Додатковий пакет Viasat Premium", viasat, null, 57);
        viasatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String filmBox = "Тематичні канали: кіно та серіали, мода, музика, бойові мистецтва та " +
                "познавальні передачі (7 каналів, з них 3 - в якості HD).";
        Button filmBoxButton = addMegogoLayoutAndreturnItsButton(
                "Додатковий пакет FilmBox", filmBox, null, 39);
        filmBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });








        addAdditionalText();
    }


    private void addAdditionalText(){
        TextView textView = new TextView(context);
        textView.setText(R.string.megogo_additional);
        textView.setPadding(40,30,40,40);
        textView.setVisibility(View.GONE);
        mainLayout.addView(textView);
    }

    private Button addMegogoLayoutAndreturnItsButton(String name, String list, String coment, int cost){
        View mainLayoutMegogo = LayoutInflater.from(context).inflate(R.layout.item_megogo_main, null);
        TextView nameField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_name);
        nameField.setText(name);
        TextView listField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_list);
        listField.setText(list);
        TextView comentField = (TextView) mainLayoutMegogo.findViewById(R.id.megogo_coment);
        if (coment != null){
            comentField.setText(coment);
            comentField.setVisibility(View.VISIBLE);
        }
        Button button = (Button) mainLayoutMegogo.findViewById(R.id.megogo_activate_button);
        button.setText("Активувати - " + cost + " грн/міс");
        mainLayoutMegogo.setVisibility(View.GONE);
        mainLayout.addView(mainLayoutMegogo);
        return button;
    }


    @Override
    public void onClick(View v) {

    }
}
