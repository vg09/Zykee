package com.example.omarla.food2u_repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hp on 3/29/2018.
 */

public class SharedGetterSetter {

    static final String PREF_USER_NAME= "username";
    static final String UserId="hello";
    static final String UserType="user_type";
    static final String UPH_NO="989080980980";
    static final String UMAIL="XXX@gmail.com";
    static final String IMG_URL="IMAGEFSHRH";
    static final String STALL_ID="stallid";
    static final String PASSWORD="password";
    static  final  String FEED_STALL_ID="feedback_stall_id";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setUserId(Context ctx, String userid)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserId, userid);
        editor.commit();

    }

    public static String getUserId(Context ctx) {
        return getSharedPreferences(ctx).getString(UserId, "");
    }


    public static void setUserType(Context ctx, String userType)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UserType, userType);
        editor.commit();

    }

    public static String getUserType(Context ctx) {
        return getSharedPreferences(ctx).getString(UserType, "");
    }


    public static void setUserMobile(Context ctx, String userMobile)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UPH_NO,userMobile);
        editor.commit();

    }

    public static String getUserMail(Context ctx) {
        return getSharedPreferences(ctx).getString(UMAIL, "");
    }
    public static void setUserMail(Context ctx, String userMail)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UMAIL,userMail);
        editor.commit();

    }

    public static String getUserMobile(Context ctx) {
        return getSharedPreferences(ctx).getString(UPH_NO, "");
    }


    public static String getImgUrl(Context ctx) {
        return getSharedPreferences(ctx).getString(IMG_URL,"");
    }

    public static void setImgUrl(Context ctx, String imageurl)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(IMG_URL,imageurl);
        editor.commit();

    }


    public static void setStall_id(Context ctx, String stall_id)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(STALL_ID,stall_id);
        editor.commit();

    }

    public static String getStall_id(Context ctx) {
        return getSharedPreferences(ctx).getString(STALL_ID, "");
    }

    public static String getPASSWORD(Context ctx) {
        return getSharedPreferences(ctx).getString(PASSWORD,"");
    }

    public static void setPassword(Context ctx, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PASSWORD,password);
        editor.commit();

    }

    public static String getFeedStallId(Context ctx) {
        return getSharedPreferences(ctx).getString(STALL_ID, "");
    }
    public static void setFeedStallId(Context ctx, String stallid)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(FEED_STALL_ID,stallid);
        editor.commit();

    }
}
