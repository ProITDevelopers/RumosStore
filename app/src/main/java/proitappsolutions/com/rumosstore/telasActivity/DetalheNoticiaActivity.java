package proitappsolutions.com.rumosstore.telasActivity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import proitappsolutions.com.rumosstore.R;

public class DetalheNoticiaActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView imagemDetalhe;
    private TextView detalhe_titulo,detalhe_data;

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
        imagemDetalhe = findViewById(R.id.detalhe_img_noticia);
        detalhe_titulo = findViewById(R.id.detalhe_titulo);
        detalhe_data = findViewById(R.id.detalhe_data);

    }
}
