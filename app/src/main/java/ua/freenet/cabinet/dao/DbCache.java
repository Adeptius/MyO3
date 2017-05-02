package ua.freenet.cabinet.dao;


import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.freenet.cabinet.model.BonusServiceSpending;
import ua.freenet.cabinet.model.ChannelDivan;
import ua.freenet.cabinet.model.ChannelMegogo;
import ua.freenet.cabinet.model.ChannelOllTv;
import ua.freenet.cabinet.model.City;
import ua.freenet.cabinet.model.DrWebSubscribe;
import ua.freenet.cabinet.model.FriendInvite;
import ua.freenet.cabinet.model.Ip;
import ua.freenet.cabinet.model.MegogoPts;
import ua.freenet.cabinet.model.Operation;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.model.Servise;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

public class DbCache {

    public static long getCurrentTime() {
        return new GregorianCalendar().getTimeInMillis();
    }

    private static boolean cacheDisabled = false;

    private static int getPastMinutes(long createdTime) {
        long currentTime = getCurrentTime();
        long sessionTime = currentTime - createdTime;
        return Math.abs((int) sessionTime / 1000 / 60);
    }

    private static final long FUTURE_TIME = 3000000000000L;


    public static Person person;
    private static long personCreatedTime;

    public static Person getPerson() throws Exception {
        if (cacheDisabled || person == null || getPastMinutes(personCreatedTime) > 5) {
            person = GetInfo.getPersonInfo();
            personCreatedTime = getCurrentTime();
        }
        return person;
    }

    public static void markPersonOld() {
        personCreatedTime = FUTURE_TIME;
        person = null;
    }


    private static List<Ip> ips;
    private static long ipsCreatedTime;

    public static List<Ip> getIps() throws Exception {
        if (cacheDisabled || ips == null || getPastMinutes(ipsCreatedTime) > 5) {
            ips = GetInfo.getIP();
            ipsCreatedTime = getCurrentTime();
        }
        return ips;
    }

    private static String mountlyFee;
    private static long mountlyFeeCreatedTime;

    public static String getMountlyFeefromLK() throws Exception {
//        Мой личный расчёт абонплаты
//        List<Servise> servises = getServises();
//        int tempFee = 0;
//        for (Servise servise : servises) {
//            tempFee += servise.getCostForCustomer();
//        }
//        mountlyFee = tempFee + "";

        if (cacheDisabled || mountlyFee == null || getPastMinutes(mountlyFeeCreatedTime) > 3) {
            mountlyFee = GetInfo.getMountlyFeefromLK();
            mountlyFeeCreatedTime = getCurrentTime();
        }
        return mountlyFee;
    }

    public static void markMountlyFeeOld() {
        mountlyFeeCreatedTime = FUTURE_TIME;
        mountlyFee = null;
    }


    private static String creditStatus;
    private static long creditStatusCreatedTime;

    public static String getCreditStatus() throws Exception {
        if (cacheDisabled || creditStatus == null || getPastMinutes(creditStatusCreatedTime) > 0) {
            creditStatus = GetInfo.getCreditStatus();
            creditStatusCreatedTime = getCurrentTime();
        }
        return creditStatus;
    }

    public static void markCreditStatusOld() {
        creditStatusCreatedTime = FUTURE_TIME;
        creditStatus = null;
    }


    private static List<Operation> wildraws;
    private static long wildrawsCreatedTime;

    public static List<Operation> getWildraws() throws Exception {
        if (cacheDisabled || wildraws == null || getPastMinutes(wildrawsCreatedTime) > 1) {
            wildraws = GetInfo.getWildrowsByFewMonth(2);
            wildrawsCreatedTime = getCurrentTime();
        }
        return wildraws;
    }

    private static List<Servise> services;
    private static long servicesCreatedTime;

    public static List<Servise> getServises() throws Exception {
        if (cacheDisabled || services == null || getPastMinutes(servicesCreatedTime) > 5) {
            services = GetInfo.getServises();
            servicesCreatedTime = getCurrentTime();
        }
        return services;
    }

    public static void markServicesOld() {
        servicesCreatedTime = FUTURE_TIME;
        services = null;
    }


    private static Map<String, Integer> freeDayInfo;
    private static long freeDayInfoCreatedTime;

    public static Map<String, Integer> getFreeDayInfo() throws Exception {
        if (cacheDisabled || freeDayInfo == null || getPastMinutes(freeDayInfoCreatedTime) > 0) {
            freeDayInfo = GetInfo.getFreeDayInfo();
            freeDayInfoCreatedTime = getCurrentTime();
        }
        return freeDayInfo;
    }

    public static void markFreeDayInfoOld() {
        freeDayInfoCreatedTime = FUTURE_TIME;
        freeDayInfo = null;
    }


    private static List<String> freeDayStatistics;
    private static long freeDayStatisticsCreatedTime;

    public static List<String> getFreeDayStatistics() throws Exception {
        if (cacheDisabled || freeDayStatistics == null || getPastMinutes(freeDayStatisticsCreatedTime) > 5) {
            freeDayStatistics = GetInfo.getFreeDayStatistics();
            freeDayStatisticsCreatedTime = getCurrentTime();
        }
        return freeDayStatistics;
    }

    public static void markFreeDayStatisticsOld() {
        freeDayStatisticsCreatedTime = FUTURE_TIME;
        freeDayStatistics = null;
    }


    private static List<String> turboDayStatistics;
    private static long turboDayStatisticsCreatedTime;

    public static List<String> getTurboDayStatistics() throws Exception {
        if (cacheDisabled || turboDayStatistics == null || getPastMinutes(turboDayStatisticsCreatedTime) > 5) {
            turboDayStatistics = GetInfo.getTurboDayStatistics();
            turboDayStatisticsCreatedTime = getCurrentTime();
        }
        return turboDayStatistics;
    }

    public static void markTurboDayStatisticsOld() {
        turboDayStatisticsCreatedTime = FUTURE_TIME;
        turboDayStatistics = null;
    }


    private static String garantedServiceStatus;
    private static long garantedServiceStatusCreatedTime;

    public static String garantedServiceStatus() throws Exception {
        if (cacheDisabled || garantedServiceStatus == null || getPastMinutes(garantedServiceStatusCreatedTime) > 5) {
            garantedServiceStatus = GetInfo.getGarantedServiceStatus();
            garantedServiceStatusCreatedTime = getCurrentTime();
        }
        return garantedServiceStatus;
    }

    public static void markGarantedServiceStatusOld() {
        garantedServiceStatusCreatedTime = FUTURE_TIME;
        garantedServiceStatus = null;
    }

    private static Boolean[] internetSwitches;
    private static long internetSwitchesCreatedTime;

    public static Boolean[] getInternetSwitches() throws Exception {
        if (cacheDisabled || internetSwitches == null || getPastMinutes(internetSwitchesCreatedTime) > -1) {
            internetSwitches = GetInfo.getInternetSwitches();
            internetSwitchesCreatedTime = getCurrentTime();
        }
        return internetSwitches;
    }

    public static void markInternetSwitchesOld() {
        internetSwitchesCreatedTime = FUTURE_TIME;
        internetSwitches = null;
    }


    private static List<DrWebSubscribe> drWebServices;
    private static long drWebServicesCreatedTime;

    public static List<DrWebSubscribe> getDrWebServices() throws Exception {
        if (cacheDisabled || drWebServices == null || getPastMinutes(drWebServicesCreatedTime) > 5) {
            drWebServices = GetInfo.getDrWebServices();
            drWebServicesCreatedTime = getCurrentTime();
        }
        return drWebServices;
    }

    public static void markDrWebServicesOld() {
        drWebServicesCreatedTime = FUTURE_TIME;
        drWebServices = null;
    }


    private static List<MegogoPts> megogoPts;
    private static long megogoPtsCreatedTime;

    public static List<MegogoPts> getMegogoPts() throws Exception {
        if (cacheDisabled || megogoPts == null || getPastMinutes(megogoPtsCreatedTime) > 5) {
            megogoPts = GetInfo.getMegogoPts();
            megogoPtsCreatedTime = getCurrentTime();
        }
        return megogoPts;
    }

    public static void markMegogoPtsOld() {
        megogoPtsCreatedTime = FUTURE_TIME;
        megogoPts = null;
    }

    private static List<FriendInvite> friendInvites;
    private static long friendInvitesCreatedTime;

    public static List<FriendInvite> getFriendInvites() throws Exception {
        if (cacheDisabled || friendInvites == null || getPastMinutes(friendInvitesCreatedTime) > 5) {
            friendInvites = GetInfo.getFriendInvites();
            friendInvitesCreatedTime = getCurrentTime();
        }
        return friendInvites;
    }

    public static void markFriendInvitesOld() {
        friendInvitesCreatedTime = FUTURE_TIME;
        friendInvites = null;
    }

    private static boolean[] bonusesStatus;
    private static long bonusesStatusCreatedTime;

    public static boolean[] getBonusesStatus() throws Exception {
        if (cacheDisabled || bonusesStatus == null || getPastMinutes(bonusesStatusCreatedTime) > 5) {
            bonusesStatus = GetInfo.getBonusesStatus();
            bonusesStatusCreatedTime = getCurrentTime();
        }
        return bonusesStatus;
    }

    public static void markBonusesStatusOld() {
        bonusesStatusCreatedTime = FUTURE_TIME;
        bonusesStatus = null;
    }


    private static Integer countOfBonuses;
    private static long countOfBonusesCreatedTime;

    public static int getCountOfBonuses() throws Exception {
        if (cacheDisabled || countOfBonuses == null || getPastMinutes(countOfBonusesCreatedTime) > -1) {
            countOfBonuses = GetInfo.getCountOfBonuses();
            countOfBonusesCreatedTime = getCurrentTime();
        }
        return countOfBonuses;
    }

    public static void markCountOfBonusesOld() {
        countOfBonusesCreatedTime = FUTURE_TIME;
        countOfBonuses = null;
    }


    private static List<BonusServiceSpending> bonusesSpending;
    private static long bonusesSpendingCreatedTime;

    public static List<BonusServiceSpending> getBonusesSpending() throws Exception {
        if (cacheDisabled || bonusesSpending == null || getPastMinutes(bonusesSpendingCreatedTime) > -1) {
            bonusesSpending = GetInfo.getBonusesSpending();
            bonusesSpendingCreatedTime = getCurrentTime();
        }
        return bonusesSpending;
    }

    public static void markBonusesSpendingOld() {
        bonusesSpendingCreatedTime = FUTURE_TIME;
        bonusesSpending = null;
    }



    private static HashMap<String,List<ChannelDivan>> divanList = new HashMap<>();

    public static List<ChannelDivan> getDivanChanels(String url) throws Exception{
        List<ChannelDivan> list = divanList.get(url);
        if (list == null){
            list = GetInfo.getDivanChanels(url);
            divanList.put(url, list);
        }
        return list;
    }




    public static String getUrlOfCityPhones(String city) throws Exception {
        return Settings.getUrlPhone().equals("")? GetInfo.getUrlOfCityPhones(city) : Settings.getUrlPhone();
    }

    public static String getUrlOfCityAccii(String city) throws Exception {
        return Settings.getUrlAccii().equals("")? GetInfo.getUrlOfCityAccii(city) : Settings.getUrlAccii();
    }

    public static String getUrlOfCityNews(String city) throws Exception {
        return Settings.getUrlNews().equals("")? GetInfo.getUrlOfCityNews(city) : Settings.getUrlNews();
    }



    private static City cityPhones;

    public static City getCityPhones(String url) throws Exception {
        if (cityPhones!= null) return cityPhones;
        try {
            cityPhones = GetInfo.getCityPhones(url);
            Settings.saveCity(cityPhones);
            return cityPhones;
        }catch (Exception e){
            Utilits.networkLog("Не удалось загрузить телефоны с сайта. Загружаю из файла.");
        }
        cityPhones = Settings.loadCity();
        if (cityPhones == null) throw new Exception();
        return cityPhones;
    }















    private static List<ChannelMegogo> megogoMainChannels;

    public static List<ChannelMegogo> getMegogoMainChannels() throws Exception {
//        if (megogoMainChannels == null) {
            megogoMainChannels = GetInfo.getMegogoChannels("http://megogo.net/ru/tv/channels/8701-light-tv-online");
//        }
        return megogoMainChannels;
    }


    private static List<ChannelMegogo> megogoFilmBoxChannels;

    public static List<ChannelMegogo> getMegogoFilmBoxChannels() throws Exception {
//        if (megogoFilmBoxChannels == null) {
            megogoFilmBoxChannels = GetInfo.getMegogoChannels("http://megogo.net/ru/tv/channels/2691-filmbox-tv-online");
//        }
        return megogoFilmBoxChannels;
    }


    private static List<ChannelMegogo> megogoviasatChannels;

    public static List<ChannelMegogo> getMegogoViasatChannels() throws Exception {
//        if (megogoviasatChannels == null) {
            megogoviasatChannels = GetInfo.getMegogoChannels("http://megogo.net/ru/tv/channels/2701-tv1000premium-tv-online");
//        }
        return megogoviasatChannels;
    }


    private static List<ChannelOllTv> ollTvStartChannels;

    public static List<ChannelOllTv> getOllTvStartChannels() throws Exception {
//        if (ollTvStartChannels == null) {
            ollTvStartChannels = GetInfo.getOllTvChanels("http://oll.tv/partners/pack/1095");
//        }
        return ollTvStartChannels;
    }


    private static List<ChannelOllTv> ollTvOptimalChannels;

    public static List<ChannelOllTv> getOllTvOptimalChannels() throws Exception {
//        if (ollTvOptimalChannels == null) {
            ollTvOptimalChannels = GetInfo.getOllTvChanels("http://oll.tv/partners/pack/1097");
//        }
        return ollTvOptimalChannels;
    }


    private static List<ChannelOllTv> ollTvPremiumChannels;

    public static List<ChannelOllTv> getOllTvPremiumChannels() throws Exception {
//        if (ollTvPremiumChannels == null) {
            ollTvPremiumChannels = GetInfo.getOllTvChanels("http://oll.tv/partners/pack/2062");
//        }
        return ollTvPremiumChannels;
    }


}
