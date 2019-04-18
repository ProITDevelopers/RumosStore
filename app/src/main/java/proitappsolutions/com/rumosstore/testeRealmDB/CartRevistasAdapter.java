package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import proitappsolutions.com.rumosstore.R;

public class CartRevistasAdapter extends RecyclerView.Adapter<CartRevistasAdapter.MyViewHolder> {

    private Context context;
    private List<CartItemRevistas> cartItems = Collections.emptyList();
    private CartRevistasAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        @BindView(R.id.btn_remove)
        Button btnRemove;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public CartRevistasAdapter(Context context, CartRevistasAdapterListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<CartItemRevistas> cartItems) {
        if (cartItems == null) {
            this.cartItems = Collections.emptyList();
        }

        this.cartItems = cartItems;

        notifyDataSetChanged();
    }

    @Override
    public CartRevistasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new CartRevistasAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartItemRevistas cartItem = cartItems.get(position);
        Revistas revistas = cartItem.revistas;
        holder.name.setText(revistas.getRevistaNome());
        holder.price.setText(holder.name.getContext().getString(R.string.lbl_item_price_quantity, context.getString(R.string.price_with_currency, revistas.getRevistaPreco()), cartItem.quantity));
//        GlideApp.with(context).load(product.imageUrl).into(holder.thumbnail);
        Glide.with(context).load(revistas.getRevistaLink()).into(holder.thumbnail);

        if (listener != null)
            holder.btnRemove.setOnClickListener(view -> listener.onCartItemRemoved(position, cartItem));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public interface CartRevistasAdapterListener {
        void onCartItemRemoved(int index, CartItemRevistas cartItem);
    }
}
