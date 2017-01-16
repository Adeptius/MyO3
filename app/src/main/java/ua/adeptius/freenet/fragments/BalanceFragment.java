package ua.adeptius.freenet.fragments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ua.adeptius.freenet.R;
import ua.adeptius.freenet.dao.DbCache;
import ua.adeptius.freenet.model.Operation;
import ua.adeptius.freenet.model.Person;

public class BalanceFragment extends BaseFragment {

    private List<Operation> operations;

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
        Person person = DbCache.getPerson();
        String mountlyFee = DbCache.getMountlyFeefromLK();
        sortByDate(operations);
        configureSaldo(person);
        String balance = String.valueOf(person.getCurrent());
        balance = balance.substring(0, balance.indexOf("."));
        titleText = "Баланс: " + balance + " грн";
        descriptionText = "Кожного першого числа знімається абонентська " +
                "плата наперед у розмірі " + mountlyFee + " грн";
        updateTitle();
        prepareAllOperations(operations);
    }

    @Override
    void processIfOk() {
        hideAllViewsInMainScreen();
        animateScreen();
    }

    @Override
    public void onClick(View v) {

    }

    private void prepareAllOperations(List<Operation> operations) {
        final List<View> viewsToAdd = new ArrayList<>();
        for (Operation operation : operations) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_balance_operation, null);

            TextView textOperationDate = (TextView) itemView.findViewById(R.id.operation_date);
            TextView textOperationComent = (TextView) itemView.findViewById(R.id.operation_coment);
            TextView textOperationMoney = (TextView) itemView.findViewById(R.id.operation_money);
            TextView textOperationSaldo = (TextView) itemView.findViewById(R.id.operation_saldo);

            String date = operation.getDate().replaceAll("00:00:00", "");
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
            String note = operation.getMyNote();

            textOperationDate.setText(date);
            textOperationComent.setText(note);
            textOperationMoney.setText(money);
            textOperationSaldo.setText(saldo);

            if (!note.equals("Нема проплат та списань")){
                viewsToAdd.add(itemView);
            }
        }
        addViewToMainLayout(viewsToAdd);
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
