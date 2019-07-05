package proitappsolutions.com.rumosstore;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import proitappsolutions.com.rumosstore.telasActivity.WebViewActivity;

public class SobreNosActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView media_social_fb, media_social_inst, media_social_linked;
    private ImageView mercado_social_fb, mercado_social_inst, mercado_social_linked;
    private ImageView vanguarda_social_fb, vanguarda_social_inst, vanguarda_social_linked;
    private ImageView rumo_social_fb, rumo_social_inst, rumo_social_linked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nos);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sobre nós");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        //MEDIA
//        media_social_fb = findViewById(R.id.media_social_fb);
//        media_social_inst = findViewById(R.id.media_social_inst);
//        media_social_linked = findViewById(R.id.media_social_linked);
//
//        media_social_fb.setOnClickListener(this);
//        media_social_inst.setOnClickListener(this);
//        media_social_linked.setOnClickListener(this);

        //MERCADO
        mercado_social_fb = findViewById(R.id.mercado_social_fb);
        mercado_social_inst = findViewById(R.id.mercado_social_inst);
        mercado_social_linked = findViewById(R.id.mercado_social_linked);

        mercado_social_fb.setOnClickListener(this);
        mercado_social_inst.setOnClickListener(this);
        mercado_social_linked.setOnClickListener(this);

        //VANGUARDA
        vanguarda_social_fb = findViewById(R.id.vanguarda_social_fb);
        vanguarda_social_inst = findViewById(R.id.vanguarda_social_inst);
        vanguarda_social_linked = findViewById(R.id.vanguarda_social_linked);

        vanguarda_social_fb.setOnClickListener(this);
        vanguarda_social_inst.setOnClickListener(this);
        vanguarda_social_linked.setOnClickListener(this);

        //RUMO
        rumo_social_fb = findViewById(R.id.rumo_social_fb);
        rumo_social_inst = findViewById(R.id.rumo_social_inst);
        rumo_social_linked = findViewById(R.id.rumo_social_linked);

        rumo_social_fb.setOnClickListener(this);
        rumo_social_inst.setOnClickListener(this);
        rumo_social_linked.setOnClickListener(this);





    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

//            //MEDIA
//            case R.id.media_social_fb:
//                verifConecxao("https://www.facebook.com/jornalmercado/", "facebook", SobreNosActivity.this);
//                break;
//            case R.id.media_social_inst:
//                verifConecxao("https://www.instagram.com/explore/tags/jornalmercado/", "instagram", SobreNosActivity.this);
//                break;
//            case R.id.media_social_linked:
//                verifConecxao("https://pt.linkedin.com/company/media-rumo", "mercado", SobreNosActivity.this);
//                break;


            //MERCADO
            case R.id.mercado_social_fb:
                verifConecxao("https://www.facebook.com/jornalmercado/", "facebook", SobreNosActivity.this);
                break;
            case R.id.mercado_social_inst:
                verifConecxao("https://www.instagram.com/explore/tags/jornalmercado/", "instagram", SobreNosActivity.this);
                break;
            case R.id.mercado_social_linked:
                verifConecxao("https://pt.linkedin.com/company/media-rumo", "mercado", SobreNosActivity.this);
                break;

            //VANGUARDA
            case R.id.vanguarda_social_fb:
                verifConecxao("https://pt-br.facebook.com/pages/category/Media-News-Company/Vanguarda-1923156214577988/", "facebook", SobreNosActivity.this);
                break;
            case R.id.vanguarda_social_inst:
                verifConecxao("https://www.instagram.com/jornalvanguardaa/", "instagram", SobreNosActivity.this);
                break;
            case R.id.vanguarda_social_linked:
                verifConecxao("https://pt.linkedin.com/company/media-rumo", "vanguarda", SobreNosActivity.this);
                break;

            //RUMO
            case R.id.rumo_social_fb:
                verifConecxao("https://www.facebook.com/jornalmercado/", "facebook", SobreNosActivity.this);
                break;
            case R.id.rumo_social_inst:
                verifConecxao("https://www.instagram.com/revistarumo/", "instagram", SobreNosActivity.this);
                break;
            case R.id.rumo_social_linked:
                verifConecxao("https://pt.linkedin.com/showcase/revista-rumo", "rumo", SobreNosActivity.this);
                break;


        }
    }

    public void enviarLinkActivity(String s, String cor, Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("site", s);
        intent.putExtra("cor", cor);
        startActivity(intent);
    }

    private void verifConecxao(String s, String cor, Context context) {

        if (getBaseContext() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr!=null){
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    Toast.makeText(this, "Verifique a sua ligação à internet.", Toast.LENGTH_SHORT).show();
                }else{
                    enviarLinkActivity(s, cor, context);
                }
            }


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
