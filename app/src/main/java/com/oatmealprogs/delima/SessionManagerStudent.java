package com.oatmealprogs.delima;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManagerStudent {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String SESSION_LOGIN = "sessionStudent";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USERID = "userID";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_CLASSNAME = "className";

    /**
     * @param preferencesID Preferences ID to use.
     *                      Paste here predefined preferences ID from com.example.test.SessionManager class.
     *                      Each ID starts with SESSION_ prefix.
     */
    public SessionManagerStudent(Context _context, String preferencesID) {
        this.context = _context;
        userSession = context.getSharedPreferences(preferencesID, Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String userID, String password, String firstName, String lastName, String className) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERID, userID);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_CLASSNAME, className);

        editor.apply();
    }

    public HashMap<String, String> getLoginSession() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(KEY_USERID, userSession.getString(KEY_USERID, null));
        hashMap.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        hashMap.put(KEY_FIRSTNAME, userSession.getString(KEY_FIRSTNAME, null));
        hashMap.put(KEY_LASTNAME, userSession.getString(KEY_LASTNAME, null));
        hashMap.put(KEY_CLASSNAME, userSession.getString(KEY_CLASSNAME, null));

        return hashMap;
    }

    public boolean isLoggedIn() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
