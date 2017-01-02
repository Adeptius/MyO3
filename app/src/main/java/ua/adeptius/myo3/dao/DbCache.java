package ua.adeptius.myo3.dao;


import java.util.List;

import ua.adeptius.myo3.model.ip.Ip;
import ua.adeptius.myo3.model.persons.Operation;
import ua.adeptius.myo3.model.persons.Person;

public class DbCache {
//
//
//    // PERSON
//    private static Person person;
//
//    public static Person getPerson() throws Exception {
//        if (person == null) {
//            loadPerson();
//        }
//        return person;
//    }
//
//    public static void loadPerson() throws Exception {
//        person = GetInfo.getPersonInfo();
//    }
//
//    public static void reLoadPersonNoException() {
//        try {
//            person = GetInfo.getPersonInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //IPS
//    private static List<Ip> ips;
//
//    public static List<Ip> getIps() throws Exception {
//        if (ips == null) {
//            loadIps();
//        }
//        return ips;
//    }
//
//    public static void loadIps() throws Exception {
//        ips = GetInfo.getIP();
//    }
//
//    public static void reLoadIpsNoException() {
//        try {
//            ips = GetInfo.getIP();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //MOUNTLYFEE
//    private static String mountlyFee;
//
//    public static String getMountlyFee() throws Exception {
//        if (mountlyFee == null) {
//            loadMountlyFee();
//        }
//        return mountlyFee;
//    }
//
//    public static void loadMountlyFee() throws Exception {
//        mountlyFee = GetInfo.getMountlyFeefromLK();
//    }
//
//    public static void reLoadMountlyFeeNoException() {
//        try {
//            mountlyFee = GetInfo.getMountlyFeefromLK();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //CREDITSTATUS
//    private static String creditStatus;
//
//    public static String getCreditStatus() throws Exception {
//        if (creditStatus == null) {
//            loadCreditStatus();
//        }
//        return creditStatus;
//    }
//
//    public static void loadCreditStatus() throws Exception {
//        creditStatus = GetInfo.getCreditStatus();
//    }
//
//    public static void reLoadCreditStatusNoException(){
//        try {
//            creditStatus = GetInfo.getCreditStatus();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    //BALANCE
//    private static List<Operation> operations;
//
//    public static List<Operation> getOperations() throws Exception{
//        if (operations == null){
//            loadOperations();
//        }
//        return operations;
//    }
//
//    public static void loadOperations() throws Exception{
//        operations = GetInfo.getWildrowsByFewMonth(5);
//    }
//
//    public static void reLoadOperationsNoException(){
//        try {
//            operations = GetInfo.getWildrowsByFewMonth(5);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
