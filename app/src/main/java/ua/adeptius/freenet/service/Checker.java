package ua.adeptius.freenet.service;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.adeptius.freenet.dao.DbCache;
import ua.adeptius.freenet.model.MegogoPts;
import ua.adeptius.freenet.model.Servise;
import ua.adeptius.freenet.utils.Settings;

import static ua.adeptius.freenet.utils.Utilits.*;

public class Checker extends Thread {

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}

            if (isLogged()){
                try{
                    isMoneyAnothForNextMonth();

                }catch (Exception e){
                    e.printStackTrace();
                }


            }else {
                log("Не залогинено");
            }
        }
    }









    private boolean isLogged(){
        boolean logNotNull = !Settings.getCurrentLogin().equals("");
        boolean passNotNull = !Settings.getCurrentPassword().equals("");
        return logNotNull && passNotNull;
    }


    private boolean isMoneyAnothForNextMonth() throws Exception{
        double amonth = Double.parseDouble(DbCache.getMountlyFeefromLK());
        double haveMoney = DbCache.getPerson().getCurrent();
//        log("денег " + haveMoney + " абонплата " + amonth);

        List<Servise> servises = DbCache.getServises();

        StringBuilder sb = new StringBuilder();
        int summaryCost = 0;

        for (Servise servise : servises) {
            if (isThatDateIsNextMonth(servise.getDateWillChange())){ // если услуга меняется
                if (servise.getNewName().contains("Удалить")){// если её удалят
                    System.out.println("В следующем месяце услуга " + servise.getMyTypeName() + " отключится");
                }else { // если она меняется на другую
                    summaryCost += getPrice(servise.getNewName());
                    System.out.println("В следующем месяце тариф изменится с "
                            + servise.getMyTypeName() + " с абонплатой " + servise.getCostForCustomer()
                            + " на " + servise.getNewName() + " c абонплатой " + getPrice(servise.getNewName()) + " грн");
                }
            }else if (servise.isHaveDiscount()){ // если есть скидка
                if (isThatDateIsNextMonth(servise.getDiscountTo())){// и она кончается в след месяце
                    summaryCost+= servise.getServiceCost();
                    System.out.println("В следующем месяце заканчивается скидка "
                            + servise.getDiscount() + "% на " + servise.getMyTypeName()
                    + " и теперь будет стоить " + servise.getServiceCost());
                }else { // и она пока не кончается
                    summaryCost += servise.getCostForCustomer();
                }
            }else { // если ничего с тарифом не произойдёт
                summaryCost += servise.getCostForCustomer();
            }


        }
        System.out.println(summaryCost);
        return false;
    }




    public static boolean isThatDateIsNextMonth(String date){
        if (date.contains(" ")){
            date = date.substring(0, date.indexOf(" "));
        }
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) +1;
//        int year = 2017;
//        int month = 2;
        month++;
        if (month == 13){
            month=1;
            year++;
        }
        String monthText = month<10 ? "0"+month : ""+month;
        String result = year + "-" + monthText + "-01";
        return result.equals(date);
    }

    public static int getPrice(String name) throws Exception {
        if (name.contains("MEGOGO")) {
            List<MegogoPts> megogoPts = DbCache.getMegogoPts();
            for (MegogoPts megogoPt : megogoPts) {
                if (megogoPt.getOriginalName().equals(name)) {
                    return Integer.parseInt(megogoPt.getMonth());
                }
            }
        } else {
            try {
                Matcher regexMatcher = Pattern.compile("\\d{1,4}[ ]?грн").matcher(name);
                if (regexMatcher.find()) {
                    String s = regexMatcher.group();
                    if (s.contains(" ")) {
                        s = s.substring(0, s.indexOf(" ")).trim();
                    }
                    return Integer.parseInt(s.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -9999999;
    }

}
