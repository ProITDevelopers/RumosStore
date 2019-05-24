package proitappsolutions.com.rumosstore;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.testeRealmDB.Revistas;

public class AppDatabase {

    public AppDatabase() {
    }

    public static void saveUser(Usuario user) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.delete(Usuario.class); //deleting previous user data
            realm.copyToRealmOrUpdate(user);
        });
    }

    public static Usuario getUser() {
        return Realm.getDefaultInstance().where(Usuario.class).findFirst();
    }
    //==========================================================================//
    //==========================================================================//
    public static void saveRevistasList(List<Revistas> revistas) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            for (Revistas revista : revistas) {
                realm.copyToRealmOrUpdate(revista);
            }
        });
    }

    public static RealmResults<Revistas> getTodasRevistasList() {
        return Realm.getDefaultInstance().where(Revistas.class).findAll();
    }

    public static RealmResults<Revistas> getRevistasMercadoList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("categoria", "mercado").findAll();
    }

    public static RealmResults<Revistas> getRevistasVanguardaList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("categoria", "vanguarda").findAll();
    }

    public static RealmResults<Revistas> getRevistasRumoList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("categoria", "rumo").findAll();
    }
    //==========================================================================//
    //==========================================================================//
    public static void clearData() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}
