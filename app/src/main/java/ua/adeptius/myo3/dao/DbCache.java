package ua.adeptius.myo3.dao;


import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import ua.adeptius.myo3.model.Ip;
import ua.adeptius.myo3.model.Operation;
import ua.adeptius.myo3.model.Person;

public class DbCache {

    public static long getCurrentTime(){
        return new GregorianCalendar().getTimeInMillis();
    }

    private static int getPastMinutes(long createdTime){
        long currentTime = getCurrentTime();
        long sessionTime = currentTime - createdTime;
        return (int) sessionTime / 1000 / 60;
    }


    private static Person person;
    private static long personCreatedTime;

    public static Person getPerson() throws Exception {
        if (person == null) {
            person = GetInfo.getPersonInfo();
            personCreatedTime = getCurrentTime();
            return person;
        } else {
            if (getPastMinutes(personCreatedTime) > 5) {
                person = GetInfo.getPersonInfo();
                personCreatedTime = getCurrentTime();
                return getPerson();
            } else {
                return person;
            }
        }
    }

    public static void markPersonOld(){
        personCreatedTime = 0;
    }


    private static List<Ip> ips;
    private static long ipsCreatedTime;

    public static List<Ip> getIps() throws Exception {
        if (ips == null) {
            ips = GetInfo.getIP();
            ipsCreatedTime = getCurrentTime();
            return ips;
        } else {
            if (getPastMinutes(ipsCreatedTime) > 5) {
                ips = GetInfo.getIP();
                ipsCreatedTime = getCurrentTime();
                return getIps();
            } else {
                return ips;
            }
        }
    }

    public static void markIpOld(){
        personCreatedTime = 0;
    }



    private static String mountlyFee;
    private static long mountlyFeeCreatedTime;

    public static String getMountlyFeefromLK() throws Exception {
        if (mountlyFee == null) {
            mountlyFee = GetInfo.getMountlyFeefromLK();
            mountlyFeeCreatedTime = getCurrentTime();
            return mountlyFee;
        } else {
            if (getPastMinutes(mountlyFeeCreatedTime) > 5) {
                mountlyFee = GetInfo.getMountlyFeefromLK();
                mountlyFeeCreatedTime = getCurrentTime();
                return getMountlyFeefromLK();
            } else {
                return mountlyFee;
            }
        }
    }

    public static void markMountlyFeeOld(){
        mountlyFeeCreatedTime = 0;
    }


    private static String creditStatus;
    private static long creditStatusCreatedTime;

    public static String getCreditStatus() throws Exception {
        if (creditStatus == null) {
            creditStatus = GetInfo.getCreditStatus();
            creditStatusCreatedTime = getCurrentTime();
            return creditStatus;
        } else {
            if (getPastMinutes(creditStatusCreatedTime) > 5) {
                creditStatus = GetInfo.getCreditStatus();
                creditStatusCreatedTime = getCurrentTime();
                return getCreditStatus();
            } else {
                return creditStatus;
            }
        }
    }

    public static void markCreditStatusOld(){
        creditStatusCreatedTime = 0;
    }


 private static List<Operation> wildraws;
    private static long wildrawsCreatedTime;

    public static List<Operation> getWildraws() throws Exception {
        if (wildraws == null) {
            wildraws = GetInfo.getWildrowsByFewMonth(4);
            wildrawsCreatedTime = getCurrentTime();
            return wildraws;
        } else {
            if (getPastMinutes(wildrawsCreatedTime) > 5) {
                wildraws = GetInfo.getWildrowsByFewMonth(4);
                wildrawsCreatedTime = getCurrentTime();
                return getWildraws();
            } else {
                return wildraws;
            }
        }
    }

    public static void markWildrawsOld(){
        wildrawsCreatedTime = 0;
    }







}
