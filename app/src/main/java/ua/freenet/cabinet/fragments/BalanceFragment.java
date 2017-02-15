package ua.freenet.cabinet.fragments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.model.CombinedOperation;
import ua.freenet.cabinet.model.Operation;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.utils.Utilits;

public class BalanceFragment extends BaseFragment {

//    private List<Operation> operations;

    @Override
    void setAllSettings() {
        titleText = "Баланс";
        descriptionText = "Кожного першого числа місяця знімається сумма у розмірі однієї абонентської плати";
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
        titleImage = R.drawable.background_balance2;
    }

    @Override
    void init() {

    }

    @Override
    void doInBackground() throws Exception {
        Person person = DbCache.getPerson();
        String mountlyFee = DbCache.getMountlyFeefromLK();
        List<Operation> operations = DbCache.getWildraws();
        sortByDate(operations);
        List<CombinedOperation> comboList = convertToComboList(operations);
        configureSaldo(person, comboList);
        String balance = String.valueOf(person.getCurrent());
        balance = balance.substring(0, balance.indexOf("."));
        titleText = "Баланс: " + balance + " грн";
        descriptionText = "Кожного першого числа місяця знімається сумма у розмірі однієї абонентської плати (" + mountlyFee + " грн)";
        updateTitle();
        prepareAllOperations(comboList);
    }


    @Override
    void processIfOk() {
        hideAllViewsInMainScreen();
        animateScreen();
    }

    @Override
    public void onClick(View v) {

    }

    private void prepareAllOperations(List<CombinedOperation> operations) {
        final List<View> viewsToAdd = new ArrayList<>();
        for (CombinedOperation operation : operations) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_balance_operation, null);
            TextView textOperationDate = (TextView) itemView.findViewById(R.id.operation_date);
            TextView textOperationComent = (TextView) itemView.findViewById(R.id.operation_coment);
            TextView textOperationMoney = (TextView) itemView.findViewById(R.id.operation_money);
            TextView textOperationSaldo = (TextView) itemView.findViewById(R.id.operation_saldo);

            String date = operation.getDate();
            try {
                String mon = date.substring(5, 7);
                String day = date.substring(8, 10);
                if (date.contains("00:00:00") && day.equals("01")) {
                    int month = Integer.parseInt(mon) - 1;
                    date = "Абонплата за " + Utilits.getStrMonthFirst(month);
                }
            } catch (Exception e) {
                date = operation.getDate();
            }
            textOperationDate.setText(date);

            String money;
            if (operation.getMoney() > 0) {
                money = "Поповнення на " + operation.getTextMoney() + " грн";
                textOperationMoney.setTextColor(COLOR_GREEN);
            } else {
                money = "Cписання: " + operation.getTextMoney() + " грн";
                textOperationMoney.setTextColor(Color.RED);
            }
            textOperationMoney.setText(money);

            String note = "";
            for (int i = 0; i < operation.getOperations().size(); i++) {
                Operation op = operation.getOperations().get(i);
                note = note + op.getNote();
                if (i != (operation.getOperations().size() - 1)) {
                    note += "\n";
                }
            }
            textOperationComent.setText(note);

            String saldo;
            if (operation.getTextSaldo().startsWith("-")) {
                saldo = operation.getTextSaldo();
                textOperationSaldo.setTextColor(Color.RED);
            } else {
                saldo = " " + operation.getTextSaldo();
                textOperationSaldo.setTextColor(COLOR_GREEN);
            }
            textOperationSaldo.setText(saldo);

            if (!note.equals("Нема проплат та списань")) {
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

    private void configureSaldo(Person person, List<CombinedOperation> combo) {
        double current = person.getCurrent();
        for (CombinedOperation operation : combo) {
            operation.setSaldo(current);
            if (operation.getMoney() > 0) {
                current = current - operation.getMoney();
            } else {
                current = current + Math.abs(operation.getMoney());
            }
        }
    }

    private List<CombinedOperation> convertToComboList(List<Operation> operations) {
        List<CombinedOperation> comboList = new ArrayList<>();
        for (Operation operation : operations) {
            if (operation.getNote().contains("Аренда приставок")) {
                continue;
            }
            CombinedOperation combinedOperation = null;

            for (CombinedOperation combi : comboList) { // тут ищем существующий комбо по дате операции
                if (combi.getDate().equals(operation.getDate())) {
                    combinedOperation = combi;// и если найден - сохраняем в переменную
                }
            }

            if (combinedOperation == null) { // если комбо с такой датой не существует
                combinedOperation = new CombinedOperation();
                combinedOperation.setDate(operation.getDate());
                combinedOperation.addOperation(operation);
                comboList.add(combinedOperation);
            } else { // если комбо с такой датой уже есть
                combinedOperation.addOperation(operation);
            }
        }
        return comboList;
    }
}
