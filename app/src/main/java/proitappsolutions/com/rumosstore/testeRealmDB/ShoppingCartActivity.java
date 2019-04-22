package proitappsolutions.com.rumosstore.testeRealmDB;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class ShoppingCartActivity extends AppCompatActivity implements CartRevistasAdapter.CartRevistasAdapterListener {

    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btn_checkout)
    Button btnCheckout;


    private Realm realm;
    private CartRevistasAdapter mAdapter;
    private RealmResults<CartItemRevistas> cartItems;
    private RealmChangeListener<RealmResults<CartItemRevistas>> cartItemRealmChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        realm = Realm.getDefaultInstance();
        cartItems = realm.where(CartItemRevistas.class).findAllAsync();

        cartItemRealmChangeListener = cartItems -> {
            mAdapter.setData(cartItems);
            setTotalPrice();
        };

        cartItems.addChangeListener(cartItemRealmChangeListener);


    }

    private void init() {
        mAdapter = new CartRevistasAdapter(this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setTotalPrice();
    }

    private void setTotalPrice() {
        if (cartItems != null) {
            float price = AppDatabase.getCartPrice(cartItems);
            if (price > 0) {
                btnCheckout.setText(getString(R.string.btn_checkout, getString(R.string.price_with_currency, price)));
            } else {
                // if the price is zero, dismiss the dialog
                btnCheckout.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Carrinho vazio", Toast.LENGTH_SHORT).show();
                finish();
            }
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cartItems != null) {
            cartItems.removeChangeListener(cartItemRealmChangeListener);
        }

        if (realm != null) {
            realm.close();
        }
    }



    @OnClick(R.id.btn_checkout)
    void onCheckoutClick() {

        if (!Common.isConnected(10000))
            Toast.makeText(this, "Check your internet connection!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCartItemRemoved(int index, CartItemRevistas cartItem) {
        AppDatabase.removeCartRevistaItem(cartItem);
    }

    private void clearCart() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remover todos items");
        dialog.setMessage("Deseja continuar?");

        //Set button
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                AppDatabase.clearRevistaCart();

            }
        });



        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });





        dialog.show();
    }
}
