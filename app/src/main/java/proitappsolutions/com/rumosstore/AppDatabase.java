package proitappsolutions.com.rumosstore;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.testeRealmDB.CartItemRevistas;
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
    public static void saveResvistasList(final List<Revistas> revistasList) {
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
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("revistaCategoria", "mercado").findAll();
    }

    public static RealmResults<Revistas> getRevistasVanguardaList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("revistaCategoria", "vanguarda").findAll();
    }

    public static RealmResults<Revistas> getRevistasRumoList() {
        return Realm.getDefaultInstance().where(Revistas.class).equalTo("revistaCategoria", "rumo").findAll();
    }
    //==========================================================================//
    //==========================================================================//


    /**
     * Adding product to cart
     * Will create a new cart entry if there is no cart created yet
     * Will increase the product quantity count if the item exists already
     */

    public static void addRevistaItemToCart(Revistas revistas) {
        initNewCartRevistas(revistas);
    }

    private static void initNewCartRevistas(Revistas revistas) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            CartItemRevistas cartItem = realm.where(CartItemRevistas.class).equalTo("revistas.revistaID", revistas.getRevistaID()).findFirst();
            if (cartItem == null) {
                CartItemRevistas ci = new CartItemRevistas();
                ci.revistas = revistas;
                ci.quantity = 1;
                realm.copyToRealmOrUpdate(ci);
            } else {
                cartItem.quantity += 1;
                realm.copyToRealmOrUpdate(cartItem);
            }
        });
    }

    public static void removeRevistaCartItem(Revistas revistas) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            CartItemRevistas cartItem = realm.where(CartItemRevistas.class).equalTo("revistas.revistaID", revistas.getRevistaID()).findFirst();
            if (cartItem != null) {
                if (cartItem.quantity == 1) {
                    cartItem.deleteFromRealm();
                } else {
                    cartItem.quantity -= 1;
                    realm.copyToRealmOrUpdate(cartItem);
                }
            }
        });
    }

    public static void removeCartRevistaItem(CartItemRevistas cartItem) {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.where(CartItemRevistas.class).equalTo("revistas.revistaID", cartItem.revistas.getRevistaID()).findAll().deleteAllFromRealm());
    }




    public static void clearRevistaCart() {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.delete(CartItemRevistas.class));
    }




    public static void clearData() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}
