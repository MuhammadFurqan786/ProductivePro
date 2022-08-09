package eu.faircode.netguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context context;
    private static final String PREF_NAME = "Freezer";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_Id = "ID";
    private static final String KEY_Email = "Email";
    private static final String KEY_name = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PLATFORM = "platform";


    public SessionManager(Context context) {
        if (context != null) {
            this.context = context;
            pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }


    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLogin() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setId(String id) {
        editor.putString(KEY_Id, id);
        editor.apply();
    }

    public String getId() {
        return pref.getString(KEY_Id, "");
    }


    public void setEmail(String email) {
        editor.putString(KEY_Email, email);
        editor.apply();
    }

    public String getEmail() {
        return pref.getString(KEY_Email, "");
    }

    public void setName(String name) {
        editor.putString(KEY_name, name);
        editor.apply();
    }

    public String getName() {
        return pref.getString(KEY_name, "");
    }

    public void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, "");
    }

}