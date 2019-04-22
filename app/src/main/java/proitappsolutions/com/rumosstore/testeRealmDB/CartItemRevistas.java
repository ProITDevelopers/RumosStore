package proitappsolutions.com.rumosstore.testeRealmDB;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CartItemRevistas extends RealmObject {

    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public Revistas revistas;
    public int quantity = 0;

    public CartItemRevistas() {}

    public CartItemRevistas(String id, Revistas revistas, int quantity) {
        this.id = id;
        this.revistas = revistas;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Revistas getRevistas() {
        return revistas;
    }

    public void setRevistas(Revistas revistas) {
        this.revistas = revistas;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
