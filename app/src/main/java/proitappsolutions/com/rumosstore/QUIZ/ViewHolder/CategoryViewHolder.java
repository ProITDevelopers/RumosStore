package proitappsolutions.com.rumosstore.QUIZ.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import proitappsolutions.com.rumosstore.QUIZ.Interface.ItemClickListener;
import proitappsolutions.com.rumosstore.R;


public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView category_image;
    public TextView category_name;
    public ProgressBar category_progress_bar;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_image = itemView.findViewById(R.id.category_image);
        category_name = itemView.findViewById(R.id.category_name);
        category_progress_bar = itemView.findViewById(R.id.categoria_progress_bar);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
