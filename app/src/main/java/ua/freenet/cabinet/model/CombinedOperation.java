package ua.freenet.cabinet.model;


import java.util.ArrayList;
import java.util.List;

public class CombinedOperation {

    private String date;
    private double money;
    private String note;
    private double saldo;
    private List<Operation> operations = new ArrayList<>();


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMoney() {
        double summaryMoney = 0;
        for (Operation operation : operations) {
            summaryMoney+=operation.getMoney();
        }
        return summaryMoney;
    }

    public String getTextSaldo() {
        String result = String.valueOf(saldo);
        if (result.contains(".")) {
            int dot = result.indexOf(".");
            result = result.substring(0, dot + 2);
        }
        return result;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation operation){
        operations.add(operation);
    }

    public String getTextMoney() {
        String result = String.valueOf(getMoney());
        try{
            int dot = result.indexOf(".");
            if(result.length()>(dot+2)){
                result = result.substring(0, dot+3);
            }
        }catch (Exception ignored){}

        if (result.contains(".0")) {
            result = result.substring(0, result.indexOf(".0"));
        }
        if (result.contains(".")){
            int a = result.indexOf(".");
            if (result.length()>a+2){
                result = result.substring(0, a+2);
            }
        }
        if (result.equals("-0")){
            result = "0";
        }
        return result;
    }

    @Override
    public String toString() {
        return "CombinedOperation{" +
                "date='" + date + '\'' +
                ", money=" + money +
                ", note='" + note + '\'' +
                ", saldo=" + saldo +
                ", operations=" + operations +
                '}';
    }
}
