package proitappsolutions.com.rumosstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.communs.CustomizarResultadoXml;
import proitappsolutions.com.rumosstore.rssFeed.RSSObjecto;
import proitappsolutions.com.rumosstore.rssFeed.rssInterface.ItemClickListener;

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{


    public TextView txtTitulo,txtDataPublicacao;
    //public TextView txtConteudo;
    public ImageView imgPublicacao;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(View itemView){
        super(itemView);


        txtTitulo = itemView.findViewById(R.id.titulo);
        txtDataPublicacao = itemView.findViewById(R.id.dataPublicacao);
        //txtConteudo = itemView.findViewById(R.id.conteudo);
        imgPublicacao = itemView.findViewById(R.id.imgPublicacao);


        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),true);
        return true;
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private RSSObjecto rssObjecto;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(RSSObjecto rssObjecto, Context mContext) {
        this.rssObjecto = rssObjecto;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.linha,viewGroup,false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {

        CustomizarResultadoXml resultadoXml = new
                CustomizarResultadoXml("<img src=",rssObjecto.getItems().get(i).getContent());

        CustomizarResultadoXml resultadoXmlConteudo = new
                CustomizarResultadoXml("<p>",rssObjecto.getItems().get(i).getContent());


        feedViewHolder.txtTitulo.setText(rssObjecto.getItems().get(i).getTitle());
        feedViewHolder.txtDataPublicacao.setText(rssObjecto.getItems().get(i).getPubDate());
        //feedViewHolder.txtConteudo.setText(resultadoXmlConteudo.conteudo().get(resultadoXmlConteudo.conteudo().size()-1));

        Glide
                .with(mContext)
                .load(resultadoXml.comecar())
                .into(feedViewHolder.imgPublicacao);

        feedViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick){
                    Intent browserItent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObjecto.getItems().get(i).getLink()));
                    mContext.startActivity(browserItent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssObjecto.items.size();
    }
}
