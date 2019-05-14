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
    public static void saveRevistasList(final List<Revistas> revistasList) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            for (Revistas revistas : revistasList) {
                realm.copyToRealmOrUpdate(revistas);
            }
        });
    }

    public static RealmResults<Revistas> getTodasRevistasList() {
        return Realm.getDefaultInstance().where(Revistas.class).findAll();
    }

    public static RealmResults<Revistas> getRevistasMercadoList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("categoria", "mercado").or().equalTo("categoria", "Mercado").findAll();
    }

    public static RealmResults<Revistas> getRevistasVanguardaList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("categoria", "vanguarda").or().equalTo("categoria", "Vanguarda").findAll();
    }

    public static RealmResults<Revistas> getRevistasRumoList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("categoria", "rumo").or().equalTo("categoria", "Rumo").findAll();
    }
    //==========================================================================//
    //==========================================================================//




    public static void clearData() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}
