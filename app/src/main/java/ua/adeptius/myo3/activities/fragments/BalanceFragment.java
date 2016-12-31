package ua.adeptius.myo3.activities.fragments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.persons.Operation;
import ua.adeptius.myo3.model.persons.Person;

public class BalanceFragment extends BaseFragment {

    private List<Operation> operations;
    private Person person;
    private String abonPlata;

    @Override
    void init() {
        titleText = "Баланс";
        descriptionText = "Кожного першого числа знімається абонентська плата наперед на цілий місяць";
    }

    @Override
    void doInBackground() throws Exception {
        operations = getOperations(5);
        person = GetInfo.getPersonInfo();
        abonPlata = GetInfo.getMountlyFee();
    }

    @Override
    void processIfOk() {
        sortByDate(operations);
        configureSaldo(person);
        String balance = String.valueOf(person.getCurrent());
        balance = balance.substring(0, balance.indexOf("."));
        titleText = "Баланс: " + balance + " грн";
        descriptionText = "Кожного першого числа знімається абонентська " +
                "плата наперед у розмірі " + abonPlata + " грн";
        setTitle(titleText, descriptionText);
        prepareAllOperations(operations);
        hideAllViewsInMainScreen();
        animateScreen();
    }

    @Override
    void processIfFail() {

    }


    @Override
    public void onClick(View v) {

    }

    private void prepareAllOperations(List<Operation> operations) {
        for (Operation operation : operations) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_balance_item_operation, null);
            mainLayout.addView(itemView);

            TextView textOperationDate = (TextView) itemView.findViewById(R.id.operation_date);
            TextView textOperationComent = (TextView) itemView.findViewById(R.id.operation_coment);
            TextView textOperationMoney = (TextView) itemView.findViewById(R.id.operation_money);
            TextView textOperationSaldo = (TextView) itemView.findViewById(R.id.operation_saldo);

            String date = operation.getDate().replaceAll("00:00:00", "");

            String note = operation.getMyNote();

            String money;
            if (operation.getMoney() > 0) {
                money = "Поповнення на " + operation.getTextMoney() + " грн";
            } else {
                money = "Cписання: " + operation.getTextMoney() + " грн";
            }
            if (operation.getMoney() > 0) {
                textOperationMoney.setTextColor(COLOR_GREEN);
            } else {
                textOperationMoney.setTextColor(Color.RED);
            }

            String saldo;
            if (operation.getTextSaldo().startsWith("-")) {
                saldo = operation.getTextSaldo();
                textOperationSaldo.setTextColor(Color.RED);
            } else {
                saldo = " " + operation.getTextSaldo();
                textOperationSaldo.setTextColor(COLOR_GREEN);
            }

            textOperationDate.setText(date);
            textOperationComent.setText(note);
            textOperationMoney.setText(money);
            textOperationSaldo.setText(saldo);
        }
    }

    private void sortByDate(List<Operation> operations) {
        Collections.sort(operations, new Comparator<Operation>() {
            @Override
            public int compare(Operation o1, Operation o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    private void configureSaldo(Person person) {
        double current = person.getCurrent();
        for (Operation operation : operations) {
            operation.setSaldo(current);
            if (operation.getMoney() > 0) {
                current = current - operation.getMoney();
            } else {
                current = current + Math.abs(operation.getMoney());
            }
        }
    }

    private List<Operation> getOperations(int count) throws Exception {
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "-" + month;
        List<Operation> operationOneMonth = GetInfo.getWildraws(date);
        for (int i = 0; i < count; i++) {
            date = oneMounthAgo(date);
            List<Operation> next = GetInfo.getWildraws(date);
            for (Operation operation : next) {
                operationOneMonth.add(operation);
            }
        }
        return operationOneMonth;
    }

    private static String oneMounthAgo(String currentDate) {
        int year = Integer.parseInt(currentDate.substring(0, 4));
        int mouth = Integer.parseInt(currentDate.substring(5, 7));
        if (mouth > 1) {
            mouth--;
        } else {
            mouth = 12;
            year--;
        }
        String m = mouth < 10 ? "0" + mouth : mouth + "";
        return "" + year + "-" + m;
    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_base_scrolling;
    }

    @Override
    int setLayoutId() {
        return R.id.base_scroll_view;
    }
}
