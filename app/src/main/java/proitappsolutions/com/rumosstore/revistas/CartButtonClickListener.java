package proitappsolutions.com.rumosstore.revistas;

public interface CartButtonClickListener {

    void onKiosqueAddedCart(int index, Kiosque product);
    void onKiosqueRemovedFromCart(int index, Kiosque product);

    void onVanguardaAddedCart(int index, Vangarda product);
    void onVanguardaRemovedFromCart(int index, Vangarda product);

    void onRumoAddedCart(int index, Rumo product);
    void onRumoRemovedFromCart(int index, Rumo product);
}
