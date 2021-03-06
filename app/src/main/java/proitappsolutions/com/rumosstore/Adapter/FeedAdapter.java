package proitappsolutions.com.rumosstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.communs.CustomizarResultadoXml;
import proitappsolutions.com.rumosstore.rssFeed.RSSObjecto;
import proitappsolutions.com.rumosstore.rssFeed.rssInterface.ItemClickListener;
import proitappsolutions.com.rumosstore.telasActivity.WebViewActivity;

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{


    public TextView txtTitulo,txtDataPublicacao;
    //public TextView txtConteudo;
    public ImageView imgPublicacao;
    public ProgressBar progress_bar;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(View itemView){
        super(itemView);


        txtTitulo = itemView.findViewById(R.id.titulo);
        txtDataPublicacao = itemView.findViewById(R.id.dataPublicacao);
        progress_bar = itemView.findViewById(R.id.progress_bar);
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

        try {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }catch (Exception e){
            progress_bar.setVisibility(View.GONE);
            Log.i("falhaClick",e.getMessage());
        }

    }

    @Override
    public boolean onLongClick(View view) {
        try {
            itemClickListener.onClick(view,getAdapterPosition(),true);
        }catch (Exception e){
            e.getMessage();
        }
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

        try {
            CustomizarResultadoXml resultadoXml = new
                    CustomizarResultadoXml("<img src=",rssObjecto.getItems().get(i).getContent());
            feedViewHolder.txtTitulo.setText(rssObjecto.getItems().get(i).getTitle());
            feedViewHolder.txtDataPublicacao.setText(rssObjecto.getItems().get(i).getPubDate());
            //feedViewHolder.txtConteudo.setText(resultadoXmlConteudo.conteudo().get(resultadoXmlConteudo.conteudo().size()-1));

            try {
                Glide
                        .with(mContext)
                        .load(resultadoXml.comecar()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        feedViewHolder.progress_bar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        feedViewHolder.progress_bar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(feedViewHolder.imgPublicacao);

                feedViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (!isLongClick){
                            enviarLinkActivity(rssObjecto.getItems().get(i).getLink(),"mercado",mContext);
                        }
                    }
                });
            } catch (IndexOutOfBoundsException e) {
                feedViewHolder.progress_bar.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void enviarLinkActivity(String s, String cor, Context context) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("site",s);
        intent.putExtra("cor",cor);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        try {
            return rssObjecto.items.size();
        }catch (Exception e){
            return 0;
        }
    }
}