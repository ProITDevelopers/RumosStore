package proitappsolutions.com.rumosstore;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.revistas.CartItemKiosque;
import proitappsolutions.com.rumosstore.revistas.Kiosque;
import proitappsolutions.com.rumosstore.revistas.Rumo;
import proitappsolutions.com.rumosstore.revistas.Vangarda;

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

    public static void saveMercadoList(final List<Kiosque> kiosques) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            for (Kiosque kiosque : kiosques) {
                realm.copyToRealmOrUpdate(kiosque);
            }
        });
    }

    public static RealmResults<Kiosque> getMercadoList() {
        return Realm.getDefaultInstance().where(Kiosque.class).findAll();
    }


    public static void saveVanguardaList(final List<Vangarda> kiosques2) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            for (Vangarda kiosque2 : kiosques2) {
                realm.copyToRealmOrUpdate(kiosque2);
            }
        });
    }

    public static RealmResults<Vangarda> getVanguardaList() {
        return Realm.getDefaultInstance().where(Vangarda.class).findAll();
    }


    public static void saveRumoList(final List<Rumo> kiosques3) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            for (Rumo kiosque3 : kiosques3) {
                realm.copyToRealmOrUpdate(kiosque3);
            }
        });
    }

    public static RealmResults<Rumo> getRumoList() {
        return Realm.getDefaultInstance().where(Rumo.class).findAll();
    }


    /**
     * Adding product to cart
     * Will create a new cart entry if there is no cart created yet
     * Will increase the product quantity count if the item exists already
     */
    public static void addKiosqueItemToCart(Kiosque kiosque) {
        initNewCart(kiosque);
    }

    private static void initNewCart(Kiosque kiosque) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            CartItemKiosque cartItem = realm.where(CartItemKiosque.class).equalTo("kiosque.kiosqueID", kiosque.getKiosqueID()).findFirst();
            if (cartItem == null) {
                CartItemKiosque ci = new CartItemKiosque();
                ci.kiosque = kiosque;
                ci.quantity = 1;
                realm.copyToRealmOrUpdate(ci);
            } else {
                cartItem.quantity += 1;
                realm.copyToRealmOrUpdate(cartItem);
            }
        });
    }

    public static void removeCartItem(Kiosque kiosque) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            CartItemKiosque cartItem = realm.where(CartItemKiosque.class).equalTo("kiosque.kiosqueID", kiosque.getKiosqueID()).findFirst();
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

    public static void removeCartItem(CartItemKiosque cartItem) {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.where(CartItemKiosque.class).equalTo("kiosque.kiosqueID", cartItem.kiosque.getKiosqueID()).findAll().deleteAllFromRealm());
    }

    public static void clearCart() {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.delete(CartItemKiosque.class));
    }


    public static void clearData() {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.deleteAll());
    }
}
