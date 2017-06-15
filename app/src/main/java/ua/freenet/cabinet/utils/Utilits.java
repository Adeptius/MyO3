package ua.freenet.cabinet.utils;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilits {

    public static final Handler HANDLER = new Handler(Looper.getMainLooper());
    public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private static boolean networkLogEnable = true;
    private static boolean miscLogEnable = true;
    private static boolean logToFileEnable = false;
    public static String savedFeedBackText = "";


    public static void networkLog(String message){
        if (networkLogEnable) Log.d("---O3 Logger---", message);
        if (logToFileEnable) writeToFile(message);
    }

    public static int calculateDaysCostLeft(int costOfFullMonth){
        Calendar calendar = new GregorianCalendar();
        int current = calendar.get(Calendar.DAY_OF_MONTH);
        int left = 30 - current;
        return (costOfFullMonth /30) * left;
    }

    public static void log(String message){
        if (miscLogEnable) Log.d("---O3 Logger---", message);
        if (logToFileEnable) writeToFile(message);
    }

    private static void writeToFile(String message){
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format1.format(calendar.getTime());

        String filePath = "/storage/emulated/0/log.txt";
        String text = time + ": " + message + "\n";

        try {
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(text);
            bufferWriter.close();
        }
        catch (IOException e) {
//            System.out.println(e);
            System.out.println("Ошибка записи в файл");
        }
    }

    public static String parseUkrTime(int year, int month, int day) {
        return day + " " + getStrMonth(month) + " " + year + "р.";
    }

    public static String getUkrDateNow(){
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + " " + getStrMonth(month) + " " + year + "р.";
    }

    public static String getStrMonthFirst(int a){
        String[] month = new String[]{"Січень","Лютий","Березень","Квітень","Травень","Червень","Липень",
                "Серпень","Вересень","Жовтень","Листопад","Грудень"};
        return month[a].toLowerCase();
    }

    public static String getStrMonth(int a){
        String[] month = new String[]{"Січня","Лютого","Березня","Квітня","Травня","Червня","Липня",
                "Серпня","Вересня","Жовтня","Листопада","Грудня"};
        return month[a].toLowerCase();
    }

    public static List<String> getPhonesFromString(String s){
        String regex = "[(]?\\d{1,5}[)]?[ ]{0,4}\\d{1,4}[-| ]\\d{1,4}[-| ]\\d{1,4}[-\\d\\d]+";
        Matcher regexMatcher = Pattern.compile(regex).matcher(s);
        List<String> list = new ArrayList<>();
        while (regexMatcher.find()){
            list.add(regexMatcher.group());
        }
        return list;
    }

    public static String[] splitJson(String json){
        String[] splittedJson;
        if (json.contains("},{")){
            splittedJson = json.split("\\},\\{");
            for (int i = 0; i < splittedJson.length; i++) {
                if (i==0) splittedJson[i] += "}";
                if (i==splittedJson.length-1) splittedJson[i] = "{" + splittedJson[i];
                if (i!=0 && i!=splittedJson.length-1) splittedJson[i] = "{" + splittedJson[i] + "}";
            }
        }else {
            splittedJson = new String[1];
            splittedJson[0] = json;
        }
        return splittedJson;
    }

    public static String[] splitJsonAltAlhoritm(String json){
        ArrayList<String> list = new ArrayList<>();
        int counter = 0;
        int position = 0;
        for (int i = 0; i < json.length(); i++) {
            String s = "" + json.charAt(i);
            if (s.equals("{")) counter++;
            if (s.equals("}")) counter--;
            if (counter==0){
                String s1 = json.substring(position, i+1);
                if (s1.startsWith(",")) s1 = s1.replaceFirst(",","");
                if (!s1.equals(",") && !s1.equals("")){
                    list.add(s1);
                    position = i+1;
                }
            }
        }

        String[] cutted = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cutted[i] = list.get(i);
        }
        return cutted;
    }

    public static float dpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String doTwoSymb(int i) {
        String s = String.valueOf(i);
        if (s.length() == 1) s = "0" + s;
        return s;
    }

    public static boolean isDateIsCurrentMonth(String sDate) {
        int month = Integer.parseInt(sDate.substring(3,5));
        int currentMonth = new GregorianCalendar().get(Calendar.MONTH)+1;
        return month==currentMonth;
    }

    public static Date getDate(String date){
        // string in format yyyy-mm-dd hh-mm-ss
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        int hour = Integer.parseInt(date.substring(11,13));
        int minute = Integer.parseInt(date.substring(14,16));
        int seconds = Integer.parseInt(date.substring(17,19));
        @SuppressWarnings("deprecation")
        Date date2 = new Date(year-1900,month-1,day,hour,minute,seconds);
        return date2;
    }

    public static String oneMounthAgo(String currentDate) {
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
}
