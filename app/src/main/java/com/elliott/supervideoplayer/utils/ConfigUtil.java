package com.elliott.supervideoplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bwelco on 2017/5/20.
 */

public class ConfigUtil {


    public static final String CONFIG = "app_config";
    public static final String IP_CONFIG = "ip_config";
    public static final String PHONE_CONFIG = "phone_config";

    public static String getIp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Activity.MODE_PRIVATE);
        return sp.getString(IP_CONFIG, "192.168.10.189");
    }

    public static String getPhone(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Activity.MODE_PRIVATE);
        return sp.getString(PHONE_CONFIG, "18351936663");
    }

    public static void setIp(Context context, String ip) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Activity.MODE_PRIVATE);
        sp.edit().putString(IP_CONFIG, ip).apply();
    }

    public static void setPhone(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Activity.MODE_PRIVATE);
        sp.edit().putString(PHONE_CONFIG, phone).apply();
    }

}
