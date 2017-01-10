package ua.adeptius.myo3.fragments;


import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.BonusServiceSpending;

public class BonusFragment  extends BaseFragment {

    private int bonuses;
    private String bonusSpendingId;

    private List<BonusServiceSpending> serviceSpendings;


    @Override
    void setAllSettings() {
        titleText = "Бонуси";
        descriptionText = "Система нарахування бонусів по програмі лояльності";
        fragmentId = R.layout.fragment_bonus;
        titleImage = R.drawable.background_main1;
        layoutId = R.id.main_bonus;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        bonuses = GetInfo.getBonuses();
        serviceSpendings = GetInfo.getBonusesSpending();

    }

    @Override
    void processIfOk() {
        animateScreen();
        draw();
    }

    private void draw() {
        TextView textBonuses = getTextView(R.id.text_bonuses_amonth);
        textBonuses.setText(String.valueOf(bonuses));

    }

    private void spendBonus(int amonth){
        HashMap<String, String> map = new HashMap<>();
        map.put("bonus", String.valueOf(amonth));
        map.put("s_id", bonusSpendingId);
        map.put("p_id","");




    }

    @Override
    public void onClick(View v) {

    }
}
