package proitappsolutions.com.rumosstore.revistas;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CartItemKiosque extends RealmObject {

    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public Kiosque kiosque;
    public int quantity = 0;

    public CartItemKiosque() {}

    public CartItemKiosque(String id, Kiosque kiosque, int quantity) {
        this.id = id;
        this.kiosque = kiosque;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Kiosque getKiosque() {
        return kiosque;
    }

    public void setKiosque(Kiosque kiosque) {
        this.kiosque = kiosque;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
