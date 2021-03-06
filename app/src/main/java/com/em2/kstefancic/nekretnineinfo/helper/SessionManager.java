package com.em2.kstefancic.nekretnineinfo.helper;

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

    public SessionManager(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences=mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        this.editor=sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_LOGGED_IN,isLoggedIn);

        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_LOGGED_IN,false);
    }
}
