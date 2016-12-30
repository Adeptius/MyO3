package ua.adeptius.myo3.activities.fragments;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.persons.Person;

public class FreeDayFragment extends BaseFragment{

    private TextView firstText;
    private Person person;
    private int numberOfFreeDays;
    private int availableFreeDays;

    @Override
    void init() {
        titleText = "Вільний день";
        descriptionText = "Безкоштовна послуга збільшення швидкості до 100мбіт";
        mainLayout = (LinearLayout) baseView.findViewById(R.id.main_for_free_day);
        firstText = getTextView(R.id.first_text);
    }

    @Override
    void doInBackground() throws Exception {
        person = GetInfo.getPersonInfo();
//        Map<String, Integer> map = GetInfo.getFreeDayInfo();
//        numberOfFreeDays = map.get("daysTotal");
//        availableFreeDays = map.get("daysLeft");
        drawScreen();
    }

    private void drawScreen(){
        int abonAge = person.getAge();
        int numberOfFreeDays = abonAge % 12;
        if (numberOfFreeDays > 5) numberOfFreeDays = 5;

        StringBuilder sb = new StringBuilder();
        sb.append("Ви з нами ");
        if (availableFreeDays ==0){
            sb.append("менше року, нажаль, поки що, послуга вам не доступна.");
        }else if (availableFreeDays ==1){
            sb.append("один рік. Вам надається один день у місяць без обмежень");
        }else if (availableFreeDays ==2){
            sb.append("два роки. Вам надається два дні у місяць без обмежень");
        }else if (availableFreeDays ==3){
            sb.append("три роки. Вам надається три дня у місяць без обмежень");
        }else if (availableFreeDays ==4){
            sb.append("чотири роки. Вам надається чотири дні у місяць без обмежень");
        }else if (availableFreeDays ==5){
            sb.append("більше п'яти років. Вам надається п'ять днів у місяць без обмежень");
        }

        if (!(availableFreeDays == 0)){
            sb.append(" швидкості.");






        }

        firstText.setText(sb.toString());
    }




    @Override
    void processIfOk() {

    }

    @Override
    void processIfFail() {

    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_free_day;
    }

    @Override
    int setLayoutId() {
        return R.id.main_for_free_day;
    }

    @Override
    public void onClick(View v) {

    }
}
