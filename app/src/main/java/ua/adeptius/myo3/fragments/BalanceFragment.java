package ua.adeptius.myo3.fragments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.DbCache;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.Operation;
import ua.adeptius.myo3.model.Person;

public class BalanceFragment extends BaseFragment {

    private List<Operation> operations;
    private Person person;
    private String mountlyFee;

    @Override
    void setAllSettings() {
        titleText = "Баланс";
        descriptionText = "Кожного першого числа знімається абонентська плата наперед на цілий місяць";
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
        titleImage = R.drawable.background_balance2;
    }

    @Override
    void init() {

    }

    @Override
    void doInBackground() throws Exception {
        operations = DbCache.getWildraws();
        person = DbCache.getPerson();
        mountlyFee = DbCache.getMountlyFeefromLK();
        sortByDate(operations);
        configureSaldo(person);
    }

    @Override
    void processIfOk() {
        String balance = String.valueOf(person.getCurrent());
        balance = balance.substring(0, balance.indexOf("."));
        titleText = "Баланс: " + balance + " грн";
        descriptionText = "Кожного першого числа знімається абонентська " +
                "плата наперед у розмірі " + mountlyFee + " грн";
        updateTitle();
        prepareAllOperations(operations);
        hideAllViewsInMainScreen();
        animateScreen();
    }

    @Override
    public void onClick(View v) {

    }

    private void prepareAllOperations(List<Operation> operations) {
        for (Operation operation : operations) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_balance_operation, null);
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
}
