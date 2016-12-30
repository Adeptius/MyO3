package ua.adeptius.myo3.activities.fragments;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.persons.Person;

public class FreeDayFragment extends BaseFragment{

    private TextView firstText = getTextView(R.id.first_text);
    private Person person;
    private int numberOfFreeDays;
    private int availableFreeDays;

    @Override
    void init() {
        titleText = "Вільний день";
        descriptionText = "Безкоштовна послуга збільшення швидкості до 100мбіт";
        mainLayout = (LinearLayout) baseView.findViewById(R.id.scroll_view_news);
    }

    @Override
    void doInBackground() throws Exception {
        person = GetInfo.getPersonInfo();
        Map<String, Integer> map = GetInfo.getFreeDayInfo();
        numberOfFreeDays = map.get("daysTotal");
        availableFreeDays = map.get("daysLeft");
        drawScreen();
    }

    private void drawScreen(){
        int abonAge = person.getAge();
        int numberOfFreeDays = abonAge % 12;
        if (numberOfFreeDays > 5) numberOfFreeDays = 5;

        StringBuilder sb = new StringBuilder();
        sb.append("Ви з нами ");
        if (availableFreeDays ==0){
            sb.append("менше року, тому поки що послуга вам не доступна.");
        }else if (availableFreeDays ==1){
            sb.append("один рік, вам д.");
        }else if (availableFreeDays ==2){
            sb.append("два роки, тому поки послуга вам не доступна.");
        }else if (availableFreeDays ==3){
            sb.append("три роки, тому поки послуга вам не доступна.");
        }else if (availableFreeDays ==4){
            sb.append("чотири роки, тому поки послуга вам не доступна.");
        }else if (availableFreeDays ==5){
            sb.append("більше п'яти років, тому поки послуга вам не доступна.");
        }





    }




    @Override
    void processIfOk() {

    }

    @Override
    void processIfFail() {

    }

    @Override
    int setFragmentId() {
        return R.id.main_for_free_day;
    }

    @Override
    int setLayoutId() {
        return R.layout.fragment_free_day;
    }

    @Override
    public void onClick(View v) {

    }
}
