package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class RevistasDetalheActivity extends AppCompatActivity implements CartAddRemoveClickListener{

    Toolbar toolbar;
    ImageView thumbnail;
    TextView nome,preco;
    ImageView icAdd;
    ImageView icRemove;
    TextView lblQuantity;
    String categoria;
    int index;

    Button btn_addCart,btn_comprar;

    private Realm realm;
    private RealmResults<CartItemRevistas> cartItemKiosques;
    private RealmChangeListener<RealmResults<CartItemRevistas>> cartRealmChangeListener;

    @BindView(R.id.cart_info_bar)
    CartInfoBar cartInfoBar;
    Revistas revistas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revistas_detalhe);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        thumbnail = (ImageView) findViewById(R.id.image);
        nome = (TextView)findViewById(R.id.nome);
        preco = (TextView)findViewById(R.id.preco);


        icAdd = (ImageView) findViewById(R.id.ic_add);
        icRemove = (ImageView) findViewById(R.id.ic_remove);
        lblQuantity = (TextView)findViewById(R.id.product_count);

        btn_addCart = (Button) findViewById(R.id.btn_addCart);
        btn_comprar = (Button) findViewById(R.id.btn_comprar);

        realm = Realm.getDefaultInstance();
        cartItemKiosques = realm.where(CartItemRevistas.class).findAllAsync();

        cartInfoBar.setListener(() -> showCart());

        if (getIntent() != null){
            index = getIntent().getIntExtra("index",-1);
            categoria = getIntent().getStringExtra("categoria");

            toolbar.setTitle("Revista: " + categoria);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (index != -1)
                loadDetail(index);

            cartRealmChangeListener = cartItems -> {

                if (cartItems != null && cartItems.size() > 0) {

                    setCartInfoBar(cartItems);
                    toggleCartBar(true);
                } else {
                    toggleCartBar(false);

                }

                setCartItems(cartItems);
            };


        }







    }


    private void loadDetail(int index) {

        if (categoria.equals("mercado")){
            revistas = AppDatabase.getRevistasMercadoList().get(index);
            Picasso.with(this).load(revistas.getRevistaLink()).into(thumbnail);
            nome.setText(revistas.getRevistaNome());
            preco.setText(String.valueOf(revistas.getRevistaPreco())+" KWZ");

            revistasCheckItems(revistas);

            icAdd.setOnClickListener(view -> {

                onRevistaAddedCart(index, revistas);
                revistasCheckItems(revistas);

            });

            icRemove.setOnClickListener(view -> {

                onRevistaRemovedFromCart(index, revistas);
                revistasCheckItems(revistas);
            });

        }

        if (categoria.equals("vanguarda")){

            revistas = AppDatabase.getRevistasVanguardaList().get(index);
            Picasso.with(this).load(revistas.getRevistaLink()).into(thumbnail);
            nome.setText(revistas.getRevistaNome());
            preco.setText(String.valueOf(revistas.getRevistaPreco())+" KWZ");

            revistasCheckItems(revistas);

            icAdd.setOnClickListener(view -> {

                onRevistaAddedCart(index, revistas);
                revistasCheckItems(revistas);

            });

            icRemove.setOnClickListener(view -> {

                onRevistaRemovedFromCart(index, revistas);
                revistasCheckItems(revistas);
            });
        }

        if (categoria.equals("rumo")){
            revistas = AppDatabase.getRevistasRumoList().get(index);
            Picasso.with(this).load(revistas.getRevistaLink()).into(thumbnail);
            nome.setText(revistas.getRevistaNome());
            preco.setText(String.valueOf(revistas.getRevistaPreco())+" KWZ");

            revistasCheckItems(revistas);

            icAdd.setOnClickListener(view -> {

                onRevistaAddedCart(index, revistas);
                revistasCheckItems(revistas);

            });

            icRemove.setOnClickListener(view -> {

                onRevistaRemovedFromCart(index, revistas);
                revistasCheckItems(revistas);
            });
        }




    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_frag_revista, menu);
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

//        if (item.getItemId() == R.id.menu_cart) {
//            Intent i = new Intent(this, ShoppingCartActivity.class);
//            startActivity(i);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    private void setCartInfoBar(RealmResults<CartItemRevistas> cartItems) {
        int itemCount = 0;
        for (CartItemRevistas cartItem : cartItems) {
            itemCount += cartItem.quantity;
        }
        cartInfoBar.setData(itemCount, String.valueOf(Common.getCartPrice(cartItems)));
    }

    void showCart() {
        Intent i = new Intent(this, ShoppingCartActivity.class);
        startActivity(i);
    }

    private void toggleCartBar(boolean show) {
        if (show)
            cartInfoBar.setVisibility(View.VISIBLE);
        else
            cartInfoBar.setVisibility(View.GONE);
    }

    @Override
    public void onRevistaAddedCart(int index, Revistas revistas) {
        AppDatabase.addRevistaItemToCart(revistas);
        if (cartItemKiosques != null) {
            updateItem(index, cartItemKiosques);
        }
    }

    @Override
    public void onRevistaRemovedFromCart(int index, Revistas revistas) {
        AppDatabase.removeRevistaCartItem(revistas);
        if (cartItemKiosques != null) {
            updateItem(index, cartItemKiosques);
        }
    }


    public void setCartItems(RealmResults<CartItemRevistas> cartItems) {
        this.cartItemKiosques = cartItems;

    }



    public void updateItem(int position, RealmResults<CartItemRevistas> cartItems) {
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


    private void revistasCheckItems(Revistas revistas){
        if (cartItemKiosques != null) {
            CartItemRevistas cartItem = cartItemKiosques.where().equalTo("revistas.revistaID", revistas.getRevistaID()).findFirst();
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
