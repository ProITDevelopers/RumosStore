package proitappsolutions.com.rumosstore.QUIZ.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import proitappsolutions.com.rumosstore.QUIZ.Interface.ItemClickListener;
import proitappsolutions.com.rumosstore.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txt_score;
    public ImageView trofeu_ouro,trofeu_prata,trofeu_bronze;

    private ItemClickListener itemClickListener;

    public RankingViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_score = itemView.findViewById(R.id.txt_score);
        trofeu_ouro = itemView.findViewById(R.id.trofeu_ouro);
        trofeu_prata = itemView.findViewById(R.id.trofeu_prata);
        trofeu_bronze = itemView.findViewById(R.id.trofeu_bronze);

        itemView.setOnClickListener(this);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setTxt_name(TextView txt_name){
        this.txt_name = txt_name;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }


}
