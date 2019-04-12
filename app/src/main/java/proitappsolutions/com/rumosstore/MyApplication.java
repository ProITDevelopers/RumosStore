package proitappsolutions.com.rumosstore;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static MyApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        mInstance = this;


        initRealm();
    }



    private void initRealm() {
        Realm.init(this);
        RealmConfiguration defaultRealmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name(Common.DB_REALM)
                .build();
        Realm.setDefaultConfiguration(defaultRealmConfiguration);
        Realm.compactRealm(defaultRealmConfiguration);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}
