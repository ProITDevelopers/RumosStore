package proitappsolutions.com.rumosstore;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AppDatabase {

    public AppDatabase() {
    }

    public static void saveUser(Usuario user) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.delete(Usuario.class); // deleting previous user data
            realm.copyToRealmOrUpdate(user);
        });
    }

    public static Usuario getUser() {
        return Realm.getDefaultInstance().where(Usuario.class).findFirst();
    }


    public static void clearData() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}
