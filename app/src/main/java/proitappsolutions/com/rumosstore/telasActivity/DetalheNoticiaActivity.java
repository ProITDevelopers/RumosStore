package proitappsolutions.com.rumosstore.telasActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import proitappsolutions.com.rumosstore.R;

public class DetalheNoticiaActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView detalhe_img_noticia;
    private TextView detalhe_titulo,detalhe_data,detalhe_comteudo;
    private String imagem,titulo,data,conteudo;
    private boolean isHadeToBarView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_noticia);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) this);
        detalhe_img_noticia = findViewById(R.id.detalhe_img_noticia);
        detalhe_titulo = findViewById(R.id.detalhe_titulo);
        detalhe_data = findViewById(R.id.detalhe_data);
        detalhe_comteudo = findViewById(R.id.detalhe_comteudo);

        //atribuição dos valores
        Intent intent = getIntent();
        imagem = intent.getStringExtra("imagem");
        titulo = intent.getStringExtra("titulo");
        data = intent.getStringExtra("data");
        conteudo = intent.getStringExtra("conteudo");

        detalhe_titulo.setText(titulo);
        detalhe_data.setText(data);
        detalhe_comteudo.setText(conteudo);

        Glide
                .with(DetalheNoticiaActivity.this)
                .load(imagem).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                //feedViewHolder.progress_bar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //feedViewHolder.progress_bar.setVisibility(View.GONE);
                return false;
            }
        }).into(detalhe_img_noticia);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i)/(float) maxScroll;

        if (percentage == 1f && isHadeToBarView){
            isHadeToBarView =! isHadeToBarView;

        }else if (percentage < 1f && isHadeToBarView){
            isHadeToBarView =! isHadeToBarView;
        }
    }
}
