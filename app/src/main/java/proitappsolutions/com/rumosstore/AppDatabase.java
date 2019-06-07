package proitappsolutions.com.rumosstore;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;



public class AppDatabase {

    private static AppDatabase singleTonInstance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREF_NAME = "app_prefs";
    private static final int PRIVATE_MODE = 0;
    private static final String KEY_AUTH_TOKEN = "auth_user_token";
    private static final String KEY_USER = "myuser";
    private Gson gson;
    private Usuario mUsuario;
    private String json;

    public static AppDatabase getInstance() {
        if (singleTonInstance == null) {
            singleTonInstance = new AppDatabase(MyApplication.getInstance().getApplicationContext());
        }
        return singleTonInstance;
    }

    private AppDatabase(Context context) {
        super();
        this.mUsuario = new Usuario();
        this.gson = new Gson();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void saveUser(Usuario usuario){

        json = gson.toJson(usuario);
        editor.putString(KEY_USER, json);
        editor.commit();

    }



    public Usuario getUser(){

        json = sharedPreferences.getString(KEY_USER, "");
        mUsuario = gson.fromJson(json, Usuario.class);

        return mUsuario;

    }


    public void saveAuthToken(String authToken) {
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.commit();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public void clearData() {
        editor.clear().commit();
    }

}
