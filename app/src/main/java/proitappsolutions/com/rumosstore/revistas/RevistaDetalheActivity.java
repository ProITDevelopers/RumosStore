package proitappsolutions.com.rumosstore.revistas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.fragmentos.FragQuiosque;

public class RevistaDetalheActivity extends AppCompatActivity implements CartButtonClickListener {
    Toolbar toolbar;

    ImageView thumbnail;
    TextView nome,preco;
    String categoria;


    ImageView icAdd;
    ImageView icRemove;
    TextView lblQuantity;

    private RealmResults<CartItemKiosque> cartItemKiosques;



    private Realm realm;
    private RealmChangeListener<RealmResults<CartItemKiosque>> cartRealmChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_detalhe);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Review Screen");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        realm = Realm.getDefaultInstance();
        cartItemKiosques = realm.where(CartItemKiosque.class).findAllAsync();

        thumbnail = (ImageView) findViewById(R.id.image);
        nome = (TextView)findViewById(R.id.nome);
        preco = (TextView)findViewById(R.id.preco);


        icAdd = (ImageView) findViewById(R.id.ic_add);
        icRemove = (ImageView) findViewById(R.id.ic_remove);
        lblQuantity = (TextView)findViewById(R.id.product_count);

        if (getIntent() != null){
            int movie_index = getIntent().getIntExtra("movie_index",-1);
            categoria = getIntent().getStringExtra("type");


            if (movie_index != -1)
                loadDetail(movie_index);



            cartRealmChangeListener = cartItems -> {

                if (cartItems != null && cartItems.size() > 0) {
                    Toast.makeText(this, "Cart items changed! " + this.cartItemKiosques.size(), Toast.LENGTH_SHORT).show();
//
//                    Timber.d("Cart items changed! " + this.cartItems.size());
//                    setCartInfoBar(cartItems);
//                    toggleCartBar(true);
                } else {
//                    toggleCartBar(false);
                    Toast.makeText(this, "toggleCartBar(false)", Toast.LENGTH_SHORT).show();
                }

                setCartItems(cartItems);
            };


        }




    }

    private void loadDetail(int index) {

        if (categoria.equals("mercado")){
            Kiosque kiosque = FragQuiosque.mercadoList.get(index);

            Picasso.with(this).load(kiosque.getImageURL()).into(thumbnail);
            nome.setText(kiosque.getName());
            preco.setText(String.valueOf(kiosque.getPreco())+" KWZ");

            kiosQueCheckItems(kiosque);

            icAdd.setOnClickListener(view -> {
               onKiosqueAddedCart(index, kiosque);

                kiosQueCheckItems(kiosque);

            });

            icRemove.setOnClickListener(view -> {
                onKiosqueRemovedFromCart(index, kiosque);

                kiosQueCheckItems(kiosque);
            });




        }

        if (categoria.equals("vanguarda")){

            Vangarda movie = FragQuiosque.vanguardaList.get(index);

            Picasso.with(this).load(movie.getImageURL()).into(thumbnail);
            nome.setText(movie.getName());
            preco.setText(String.valueOf(movie.getPreco())+" KWZ");
        }

        if (categoria.equals("rumo")){
            Rumo movie = FragQuiosque.rumoList.get(index);

            Picasso.with(this).load(movie.getImageURL()).into(thumbnail);
            nome.setText(movie.getName());
            preco.setText(String.valueOf(movie.getPreco())+" KWZ");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onKiosqueAddedCart(int index, Kiosque product) {
        AppDatabase.addKiosqueItemToCart(product);
        if (cartItemKiosques != null) {
            updateItem(index, cartItemKiosques);
        }
    }

    @Override
    public void onKiosqueRemovedFromCart(int index, Kiosque product) {
        AppDatabase.removeCartItem(product);
        if (cartItemKiosques != null) {
            updateItem(index, cartItemKiosques);
        }
    }

    @Override
    public void onVanguardaAddedCart(int index, Vangarda product) {

    }

    @Override
    public void onVanguardaRemovedFromCart(int index, Vangarda product) {

    }

    @Override
    public void onRumoAddedCart(int index, Rumo product) {

    }

    @Override
    public void onRumoRemovedFromCart(int index, Rumo product) {

    }

    public void setCartItems(RealmResults<CartItemKiosque> cartItems) {
        this.cartItemKiosques = cartItems;

    }



    public void updateItem(int position, RealmResults<CartItemKiosque> cartItems) {
        this.cartItemKiosques = cartItems;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartItemKiosques != null) {
            cartItemKiosques.addChangeListener(cartRealmChangeListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartItemKiosques != null) {
            cartItemKiosques.addChangeListener(cartRealmChangeListener);
        }
        if (realm != null) {
            realm.close();
        }
    }


    private void kiosQueCheckItems(Kiosque kiosque){
        if (cartItemKiosques != null) {
            CartItemKiosque cartItem = cartItemKiosques.where().equalTo("kiosque.kiosqueID", kiosque.getKiosqueID()).findFirst();
            if (cartItem != null) {
                lblQuantity.setText(String.valueOf(cartItem.quantity));
                icRemove.setVisibility(View.VISIBLE);
                lblQuantity.setVisibility(View.VISIBLE);
            } else {
                lblQuantity.setText(String.valueOf(0));
                icRemove.setVisibility(View.GONE);
                lblQuantity.setVisibility(View.GONE);
            }
        }
    }
}
