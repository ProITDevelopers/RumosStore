package proitappsolutions.com.rumosstore.revistas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.fragmentos.FragQuiosque;

public class RevistaDetalheActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView image;
    TextView nome,preco;
    String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revista_detalhe);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Review Screen");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = (ImageView) findViewById(R.id.image);
        nome = (TextView)findViewById(R.id.nome);
        preco = (TextView)findViewById(R.id.preco);

        if (getIntent() != null){
            int movie_index = getIntent().getIntExtra("movie_index",-1);
            categoria = getIntent().getStringExtra("type");


            if (movie_index != -1)
                loadDetail(movie_index);
        }
    }

    private void loadDetail(int index) {

        if (categoria.equals("mercado")){
            Kiosque movie = FragQuiosque.mercadoList.get(index);

            Picasso.with(this).load(movie.getImageURL()).into(image);
            nome.setText(movie.getName());
            preco.setText(String.valueOf(movie.getPreco())+" KWZ");
        }

        if (categoria.equals("vanguarda")){

            Vangarda movie = FragQuiosque.vanguardaList.get(index);

            Picasso.with(this).load(movie.getImageURL()).into(image);
            nome.setText(movie.getName());
            preco.setText(String.valueOf(movie.getPreco())+" KWZ");
        }

        if (categoria.equals("rumo")){
            Rumo movie = FragQuiosque.rumoList.get(index);

            Picasso.with(this).load(movie.getImageURL()).into(image);
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
}
