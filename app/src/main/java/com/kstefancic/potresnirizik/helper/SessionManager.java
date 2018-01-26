package com.kstefancic.potresnirizik.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 3.11.2017..
 */

public class SessionManager {

    public static final String TAG = SessionManager.class.getSimpleName();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context mContext;

    int PRIVATE_MODE=0;

    private static final String PREF_NAME = "SessionControl";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_PASSWORD = "password";

    public SessionManager(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        this.editor=sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn, String userPassword){
        editor.putBoolean(KEY_LOGGED_IN,isLoggedIn);
        editor.putString(KEY_PASSWORD,userPassword);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_LOGGED_IN,false);
    }

    public String getPassword(){
        return sharedPreferences.getString(KEY_PASSWORD,"");
    }
}
