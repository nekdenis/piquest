package ru.thevhod.picquest;

import android.content.Context;
import android.content.SharedPreferences;


public class SPHelper {

    private static final String PREFERENCES_IP = "PREFERENCES_IP";
    private Context context;
    private SharedPreferences sharedPreferences;

    public SPHelper(Context context) {
        this.context = context;
    }

    public String getIp() {
        return preferences().getString(PREFERENCES_IP, "");
    }

    public void putIp(String value) {
        editor().putString(PREFERENCES_IP, value).commit();
    }

    private SharedPreferences.Editor editor() {
        return preferences().edit();
    }

    private SharedPreferences preferences() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("shared_pref",
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

}
