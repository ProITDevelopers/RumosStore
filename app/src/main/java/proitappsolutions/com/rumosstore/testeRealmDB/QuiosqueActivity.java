package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.telasActivity.HomeInicialActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuiosqueActivity extends AppCompatActivity {

    Toolbar toolbarQuiosque;

    private RelativeLayout errorLayout;
    private TextView btnTentarDeNovo;


    private LinearLayout linearLayoutCarregando,linearCarregarMercado,linearCarregarVanguarda,linearCarregarRumo;
    private AVLoadingIndicatorView progressMercado,progressVanguarda,progressRumo;
    private TextView txtCarregandoMercado,txtCarregandoVanguarda,txtCarregandoRumo;
    private CardView cardMercado,cardVanguarda,cardRumo;

    private TextSwitcher mTitle,mTitle2,mTitle3;
    private FeatureCoverFlow coverFlow,coverFlow2,coverFlow3;
    private Animation in,out;

    private Revistas revistas;
    private List<Revistas> mercadoList,vanguardaList,rumoList;
    private RevistasAdapter revistasMercadoAdapter,revistasVanguardaAdapter,revistasRumoAdapter;



    int indexMercado,indexVanguarda,indexRumo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiosque);

        toolbarQuiosque = findViewById(R.id.toolbarQuiosque);
        toolbarQuiosque.setTitle("Quiosque");
        setSupportActionBar(toolbarQuiosque);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();

        verifConecxaoRevistas();
    }

    private void initView() {

        in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.slide_in_top);
        out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.slide_out_bottom);
        errorLayout = findViewById(R.id.erroLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorExemplo));

        linearLayoutCarregando = findViewById(R.id.linearLayoutCarregando);
        linearCarregarMercado = findViewById(R.id.linearCarregarMercado);
        linearCarregarVanguarda = findViewById(R.id.linearCarregarVanguarda);
        linearCarregarRumo = findViewById(R.id.linearCarregarRumo);
        progressMercado = findViewById(R.id.progressMercado);
        progressVanguarda = findViewById(R.id.progressVanguarda);
        progressRumo = findViewById(R.id.progressRumo);
        txtCarregandoMercado = findViewById(R.id.txtCarregandoMercado);
        txtCarregandoVanguarda = findViewById(R.id.txtCarregandoVanguarda);
        txtCarregandoRumo = findViewById(R.id.txtCarregandoRumo);
        cardMercado = findViewById(R.id.cardMercado);
        cardVanguarda = findViewById(R.id.cardVanguarda);
        cardRumo = findViewById(R.id.cardRumo);

        txtCarregandoMercado.setText("Carregando...");
        txtCarregandoVanguarda.setText("Carregando...");
        txtCarregandoRumo.setText("Carregando...");



        mTitle = (TextSwitcher)findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        mTitle2 = (TextSwitcher)findViewById(R.id.title2);
        mTitle2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });
        mTitle2.setInAnimation(in);
        mTitle2.setOutAnimation(out);

        mTitle3 = (TextSwitcher)findViewById(R.id.title3);
        mTitle3.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });

        mTitle3.setInAnimation(in);
        mTitle3.setOutAnimation(out);

        coverFlow = (FeatureCoverFlow)findViewById(R.id.coverflow);
        coverFlow2 = (FeatureCoverFlow)findViewById(R.id.coverflow2);
        coverFlow3 = (FeatureCoverFlow)findViewById(R.id.coverflow3);



    }

    private void verifConecxaoRevistas() {

        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr!=null) {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();
            } else {
                carregarRevistas();
            }
        }

    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            linearLayoutCarregando.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutCarregando.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                verifConecxaoRevistas();
            }
        });
    }

    private void carregarRevistas() {
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<List<Revistas>> rv = apiInterface.getRevistas();
        rv.enqueue(new Callback<List<Revistas>>() {
            @Override
            public void onResponse(@NonNull Call<List<Revistas>> call, @NonNull Response<List<Revistas>> response) {

                mercadoList = new ArrayList<>();
                vanguardaList = new ArrayList<>();
                rumoList = new ArrayList<>();

                if (!response.isSuccessful()) {
                    progressMercado.setVisibility(View.GONE);
                    progressVanguarda.setVisibility(View.GONE);
                    progressRumo.setVisibility(View.GONE);
                    txtCarregandoMercado.setText("Sem resultados!");
                    txtCarregandoVanguarda.setText("Sem resultados!");
                    txtCarregandoRumo.setText("Sem resultados!");
                } else {



                    if (response.body()!=null){

                        filtrarRevistas(response.body());

                    } else {

                        progressMercado.setVisibility(View.GONE);
                        progressVanguarda.setVisibility(View.GONE);
                        progressRumo.setVisibility(View.GONE);
                        txtCarregandoMercado.setText("Sem resultados!");
                        txtCarregandoVanguarda.setText("Sem resultados!");
                        txtCarregandoRumo.setText("Sem resultados!");
                    }





                }




            }

            @Override
            public void onFailure(Call<List<Revistas>> call, Throwable t) {
                progressMercado.setVisibility(View.GONE);
                progressVanguarda.setVisibility(View.GONE);
                progressRumo.setVisibility(View.GONE);
                txtCarregandoMercado.setText("Sem resultados!");
                txtCarregandoVanguarda.setText("Sem resultados!");
                txtCarregandoRumo.setText("Sem resultados!");
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(QuiosqueActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(QuiosqueActivity.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });



    }


    private void filtrarRevistas(List<Revistas> revistasList){

        // Order the list by regist date.
        Collections.sort(revistasList, new Revistas());

        for (int i = 0; i <revistasList.size() ; i++) {
            revistas = new Revistas(Integer.parseInt(String.valueOf(revistasList.get(i).id_jornal)),
                    String.valueOf(revistasList.get(i).nome),String.valueOf(revistasList.get(i).fotoJornal),
                    String.valueOf(revistasList.get(i).link),String.valueOf(revistasList.get(i).categoria),
                    String.valueOf(revistasList.get(i).dataEdicao),String.valueOf(revistasList.get(i).descricao));

//            if (revistas.getCategoria().equals("mercado") || revistas.getCategoria().equals("Mercado")){
            if (revistas.getCategoria().equalsIgnoreCase("mercado")){

                if(!revistas.getNome().equals("") && !revistas.getNome().isEmpty() ||
                        !revistas.getFotoJornal().equals("") && !revistas.getFotoJornal().isEmpty() ||
                        !revistas.getLink().equals("") &&!revistas.getLink().isEmpty() ||
                        !revistas.getDataEdicao().equals("") &&!revistas.getDataEdicao().isEmpty()){

                    mercadoList.add(revistas);
                }

            }

            if (revistas.getCategoria().equalsIgnoreCase("vanguarda")){

                if(!revistas.getNome().equals("") && !revistas.getNome().isEmpty() ||
                        !revistas.getFotoJornal().equals("") && !revistas.getFotoJornal().isEmpty() ||
                        !revistas.getLink().equals("") &&!revistas.getLink().isEmpty() ||
                        !revistas.getDataEdicao().equals("") &&!revistas.getDataEdicao().isEmpty()){

                    vanguardaList.add(revistas);
                }
            }

            if (revistas.getCategoria().equalsIgnoreCase("rumo")){

                if(!revistas.getNome().equals("") && !revistas.getNome().isEmpty() ||
                        !revistas.getFotoJornal().equals("") && !revistas.getFotoJornal().isEmpty() ||
                        !revistas.getLink().equals("") &&!revistas.getLink().isEmpty()||
                        !revistas.getDataEdicao().equals("") &&!revistas.getDataEdicao().isEmpty()){


                    rumoList.add(revistas);
                }
            }

        }
        revistasList.clear();


        if (mercadoList.size()>0){
            setMercadoAdapter(mercadoList);
        } else {
            cardMercado.setVisibility(View.INVISIBLE);
            progressMercado.setVisibility(View.GONE);
            txtCarregandoMercado.setText("Sem resultados dos jornais Mercado!");

        }

        if (vanguardaList.size()>0){
            setVanguardaAdapter(vanguardaList);
        } else {
            cardVanguarda.setVisibility(View.INVISIBLE);
            progressVanguarda.setVisibility(View.GONE);
            txtCarregandoVanguarda.setText("Sem resultados dos jornais Vanguarda!");

        }

        if (rumoList.size()>0){
            setRumoAdapter(rumoList);
        } else {
            cardRumo.setVisibility(View.INVISIBLE);
            progressRumo.setVisibility(View.GONE);
            txtCarregandoRumo.setText("Sem resultados das revista Rumo!");

        }



    }


    private void setMercadoAdapter(List<Revistas> mercadoList){

        //===========================================MERCADO==============================================
        //===============================================================================================

        // Order the list by regist date.
        Collections.reverse(mercadoList);

        revistasMercadoAdapter = new RevistasAdapter(mercadoList,this);
        revistasMercadoAdapter.notifyDataSetChanged();
        coverFlow.setAdapter(revistasMercadoAdapter);


        coverFlow.scrollToPosition(mercadoList.size());
        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                indexMercado = position;
//                    mTitle.setText((position + 1)+" de "+mercadoList.size());
                mTitle.setText(String.valueOf(mercadoList.get(position).getNome()));
            }

            @Override
            public void onScrolling() {

            }
        });



//        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//
////                if (i<mercadoList.size()){
////                    Intent intent = new Intent(QuiosqueActivity.this, RevistaViewActivity.class);
////                    intent.putExtra("ViewType",mercadoList.get(i).getLink());
////                    startActivity(intent);
////                }
////
////                if (i>=mercadoList.size()){
////                    i = indexMercado;
////                    Intent intent = new Intent(QuiosqueActivity.this, RevistaViewActivity.class);
////                    intent.putExtra("ViewType",mercadoList.get(i).getLink());
////                    startActivity(intent);
////                }
//
//
//
//            }
//        });

        cardMercado.setVisibility(View.VISIBLE);
        linearCarregarMercado.setVisibility(View.GONE);




    }

    private void setVanguardaAdapter(List<Revistas> vanguardaList){


        //===========================================VANGUARDA==============================================
        //=========================================================================================

        // Order the list by regist date.
        Collections.reverse(vanguardaList);

        revistasVanguardaAdapter = new RevistasAdapter(vanguardaList,this);
        revistasVanguardaAdapter.notifyDataSetChanged();
        coverFlow2.setAdapter(revistasVanguardaAdapter);




        coverFlow2.scrollToPosition(vanguardaList.size());
        coverFlow2.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                indexVanguarda = position;
//                    mTitle2.setText((position + 1)+" de "+vanguardaList.size());
                mTitle2.setText(String.valueOf(vanguardaList.get(position).getNome()));
            }

            @Override
            public void onScrolling() {

            }
        });
//        coverFlow2.setOnItemClickListener((adapterView, view, i, l) -> {
//
//
////            if (i<vanguardaList.size()){
////
////                Intent intent = new Intent(QuiosqueActivity.this, RevistaViewActivity.class);
////                intent.putExtra("ViewType",vanguardaList.get(i).getLink());
////                startActivity(intent);
////
////            }
////
////            if (i>=vanguardaList.size()){
////
////                i = indexVanguarda;
////
////                Intent intent = new Intent(QuiosqueActivity.this, RevistaViewActivity.class);
////                intent.putExtra("ViewType",vanguardaList.get(i).getLink());
////                startActivity(intent);
////            }
//
//        });

        cardVanguarda.setVisibility(View.VISIBLE);
        linearCarregarVanguarda.setVisibility(View.GONE);


    }

    private void setRumoAdapter(List<Revistas> rumoList){

        //==============================================RUMO===========================================
        //=========================================================================================

        // Order the list by regist date.
        Collections.reverse(rumoList);

        revistasRumoAdapter = new RevistasAdapter(rumoList,this);
        revistasRumoAdapter.notifyDataSetChanged();
        coverFlow3.setAdapter(revistasRumoAdapter);


        coverFlow3.scrollToPosition(rumoList.size());
        coverFlow3.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                indexRumo = position;
//                    mTitle3.setText((position + 1)+" de "+rumoList.size());
                mTitle3.setText(String.valueOf(rumoList.get(position).getNome()));
            }

            @Override
            public void onScrolling() {

            }
        });
//        coverFlow3.setOnItemClickListener((adapterView, view, i, l) -> {
//
////            if (i<rumoList.size()){
////                Intent intent = new Intent(QuiosqueActivity.this, RevistaViewActivity.class);
////                intent.putExtra("ViewType",rumoList.get(i).getLink());
////                startActivity(intent);
////            }
////
////            if (i>=rumoList.size()){
////
////                i = indexRumo;
////
////                Intent intent = new Intent(QuiosqueActivity.this, RevistaViewActivity.class);
////                intent.putExtra("ViewType",rumoList.get(i).getLink());
////                startActivity(intent);
////            }
//        });

        cardRumo.setVisibility(View.VISIBLE);
        linearCarregarRumo.setVisibility(View.GONE);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
