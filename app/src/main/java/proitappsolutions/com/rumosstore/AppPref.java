package proitappsolutions.com.rumosstore;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.google.gson.Gson;

public class AppPref {

    private static AppPref singleTonInstance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREF_NAME = "app_prefs";
    private static final int PRIVATE_MODE = 0;
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private Gson gson;
    String json;
    AccessToken accessToken;

    public static AppPref getInstance() {
        if (singleTonInstance == null) {
            singleTonInstance = new AppPref(MyApplication.getInstance().getApplicationContext());
        }
        return singleTonInstance;
    }

    private AppPref(Context context) {
        super();
        this.gson = new Gson();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void saveAuthToken(AccessToken authToken) {
        json = gson.toJson(authToken);
        editor.putString(KEY_AUTH_TOKEN, json);
        editor.commit();
    }

    public AccessToken getAuthToken() {

        json = sharedPreferences.getString(KEY_AUTH_TOKEN, "");
        accessToken = gson.fromJson(json, AccessToken.class);

        return accessToken;

    }

    public void clearData() {
        editor.clear().commit();
    }
}
