package proitappsolutions.com.rumosstore.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.QUIZ.Model.Ranking;
import proitappsolutions.com.rumosstore.R;

public class AdapterClassificacaoJogador extends RecyclerView.Adapter<AdapterClassificacaoJogador.MyViewHolder>{

    private List<Ranking> rankings;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int lugar = 1;
    String  TAG = "AdapterDescontoDebug";
    private String nomeUsuario;


    public AdapterClassificacaoJogador(List<Ranking> rankings, Context context) {
        this.rankings = rankings;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_layout_ranking,viewGroup,false);
        try{
            nomeUsuario = AppDatabase.getInstance().getUser().nomeCliente;
            nomeUsuario = nomeUsuario.replace(" ","_");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new MyViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        Ranking ranking = rankings.get(i);
        Log.i("valoresDebug", i + "");

        if (i==rankings.size()-1){
            viewHolder.txt_lugar.setText("#".concat(String.valueOf(lugar)));
            viewHolder.txt_name.setText(ranking.getUserName());
            viewHolder.txt_score.setText(String.valueOf(ranking.getScore()));
            viewHolder.trofeu_ouro.setVisibility(View.VISIBLE);
            viewHolder.trofeu_prata.setVisibility(View.GONE);
            viewHolder.trofeu_bronze.setVisibility(View.GONE);
        }else if(i==rankings.size()-2) {
            viewHolder.txt_lugar.setText("#".concat(String.valueOf(lugar)));
            viewHolder.txt_name.setText(ranking.getUserName());
            viewHolder.txt_score.setText(String.valueOf(ranking.getScore()));
            viewHolder.trofeu_prata.setVisibility(View.VISIBLE);
            viewHolder.trofeu_ouro.setVisibility(View.GONE);
            viewHolder.trofeu_bronze.setVisibility(View.GONE);
        }else if(i==rankings.size()-3) {
            viewHolder.txt_lugar.setText("#".concat(String.valueOf(lugar)));
            viewHolder.txt_name.setText(ranking.getUserName());
            viewHolder.txt_score.setText(String.valueOf(ranking.getScore()));
            viewHolder.trofeu_bronze.setVisibility(View.VISIBLE);
            viewHolder.trofeu_prata.setVisibility(View.GONE);
            viewHolder.trofeu_ouro.setVisibility(View.GONE);
        }else {
            viewHolder.txt_lugar.setText("#".concat(String.valueOf(lugar)));
            viewHolder.txt_name.setText(ranking.getUserName());
            viewHolder.txt_score.setText(String.valueOf(ranking.getScore()));
            viewHolder.trofeu_ouro.setVisibility(View.INVISIBLE);
            viewHolder.trofeu_prata.setVisibility(View.GONE);
            viewHolder.trofeu_bronze.setVisibility(View.GONE);
        }
        try{
            if (nomeUsuario.equals(ranking.getUserName())){
                viewHolder.txt_name.setTextColor(context.getResources().getColor(R.color.colorBotaoLogin));
                viewHolder.txt_score.setTextColor(context.getResources().getColor(R.color.colorBotaoLogin));
                viewHolder.txt_lugar.setTextColor(context.getResources().getColor(R.color.colorBotaoLogin));
            }else {
                viewHolder.txt_name.setTextColor(context.getResources().getColor(R.color.black));
                viewHolder.txt_score.setTextColor(context.getResources().getColor(R.color.black));
                viewHolder.txt_lugar.setTextColor(context.getResources().getColor(R.color.black));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        lugar++;
    }


    @Override
    public int getItemCount() {
        return rankings.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txt_name,txt_score,txt_lugar;
        ImageView trofeu_ouro,trofeu_prata,trofeu_bronze;
        LinearLayout linearLayoutItens;
        OnItemClickListener onItemClickListener;

        MyViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);
            itemView.setOnClickListener(this);
            linearLayoutItens = itemView.findViewById(R.id.linearLayoutItens);
            txt_lugar = itemView.findViewById(R.id.txt_lugar);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_score = itemView.findViewById(R.id.txt_score);
            trofeu_ouro = itemView.findViewById(R.id.trofeu_ouro);
            trofeu_prata = itemView.findViewById(R.id.trofeu_prata);
            trofeu_bronze = itemView.findViewById(R.id.trofeu_bronze);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View view) {
            try {
                onItemClickListener.OnItemClick(view,getAdapterPosition());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
