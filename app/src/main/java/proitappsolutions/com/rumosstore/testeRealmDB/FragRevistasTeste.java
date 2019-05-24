package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragRevistasTeste extends Fragment {

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
    int firstIndex;


    private View view;


    public FragRevistasTeste() {}



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_revistas_teste, container, false);


        initView();

        verifConecxaoRevistas();



        return view;

    }

    private void initView() {

        in = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_top);
        out = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_bottom);
        errorLayout = view.findViewById(R.id.erroLayout);
        btnTentarDeNovo = view.findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

        linearLayoutCarregando =view.findViewById(R.id.linearLayoutCarregando);
        linearCarregarMercado =view.findViewById(R.id.linearCarregarMercado);
        linearCarregarVanguarda =view.findViewById(R.id.linearCarregarVanguarda);
        linearCarregarRumo =view.findViewById(R.id.linearCarregarRumo);
        progressMercado =view.findViewById(R.id.progressMercado);
        progressVanguarda =view.findViewById(R.id.progressVanguarda);
        progressRumo =view.findViewById(R.id.progressRumo);
        txtCarregandoMercado =view.findViewById(R.id.txtCarregandoMercado);
        txtCarregandoVanguarda =view.findViewById(R.id.txtCarregandoVanguarda);
        txtCarregandoRumo =view.findViewById(R.id.txtCarregandoRumo);
        cardMercado =view.findViewById(R.id.cardMercado);
        cardVanguarda =view.findViewById(R.id.cardVanguarda);
        cardRumo =view.findViewById(R.id.cardRumo);

        txtCarregandoMercado.setText("Carregando...");
        txtCarregandoVanguarda.setText("Carregando...");
        txtCarregandoRumo.setText("Carregando...");



        mTitle = (TextSwitcher)view.findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        mTitle2 = (TextSwitcher)view.findViewById(R.id.title2);
        mTitle2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });
        mTitle2.setInAnimation(in);
        mTitle2.setOutAnimation(out);

        mTitle3 = (TextSwitcher)view.findViewById(R.id.title3);
        mTitle3.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });

        mTitle3.setInAnimation(in);
        mTitle3.setOutAnimation(out);

        coverFlow = (FeatureCoverFlow)view.findViewById(R.id.coverflow);
        coverFlow2 = (FeatureCoverFlow)view.findViewById(R.id.coverflow2);
        coverFlow3 = (FeatureCoverFlow)view.findViewById(R.id.coverflow3);



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




    private void verifConecxaoRevistas() {

        if (getActivity() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();

            } else {
                carregarRevistas();
            }

        }

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
                    progressMercado.setVisibility(View.INVISIBLE);
                    progressVanguarda.setVisibility(View.INVISIBLE);
                    progressRumo.setVisibility(View.INVISIBLE);
                    txtCarregandoMercado.setText("Sem resultados!");
                    txtCarregandoVanguarda.setText("Sem resultados!");
                    txtCarregandoRumo.setText("Sem resultados!");
                } else {



                    if (response.body()!=null){

                        filtrarRevistas(response.body());

                    } else {

                        progressMercado.setVisibility(View.INVISIBLE);
                        progressVanguarda.setVisibility(View.INVISIBLE);
                        progressRumo.setVisibility(View.INVISIBLE);
                        txtCarregandoMercado.setText("Sem resultados!");
                        txtCarregandoVanguarda.setText("Sem resultados!");
                        txtCarregandoRumo.setText("Sem resultados!");
                    }





                }




            }

            @Override
            public void onFailure(Call<List<Revistas>> call, Throwable t) {
                progressMercado.setVisibility(View.INVISIBLE);
                progressVanguarda.setVisibility(View.INVISIBLE);
                progressRumo.setVisibility(View.INVISIBLE);
                txtCarregandoMercado.setText("Sem resultados!");
                txtCarregandoVanguarda.setText("Sem resultados!");
                txtCarregandoRumo.setText("Sem resultados!");
            }
        });



    }


    private void filtrarRevistas(List<Revistas> revistasList){

        for (int i = 0; i <revistasList.size() ; i++) {
            revistas = revistasList.get(i);

            if (revistas.getCategoria().equals("mercado") || revistas.getCategoria().equals("Mercado")){
                mercadoList.add(revistas);
            }

            if (revistas.getCategoria().equals("vanguarda") || revistas.getCategoria().equals("Vanguarda")){
                vanguardaList.add(revistas);
            }

            if (revistas.getCategoria().equals("rumo") || revistas.getCategoria().equals("Rumo")){
                rumoList.add(revistas);
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



            firstIndex = mercadoList.indexOf(mercadoList.get(0));


            if (getContext()!=null){
                revistasMercadoAdapter = new RevistasAdapter(mercadoList,getContext());
                coverFlow.setAdapter(revistasMercadoAdapter);
            }


//            revistasMercadoAdapter.notifyDataSetChanged();

            coverFlow.scrollToPosition(firstIndex);

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



            coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                    if (i<mercadoList.size()){
                        Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                        intent.putExtra("ViewType",mercadoList.get(i).getLink());
                        startActivity(intent);
                    }

                    if (i>=mercadoList.size()){
                        i = indexMercado;
                        Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                        intent.putExtra("ViewType",mercadoList.get(i).getLink());
                        startActivity(intent);
                    }



                }
            });

        cardMercado.setVisibility(View.VISIBLE);
        linearCarregarMercado.setVisibility(View.INVISIBLE);



    }

    private void setVanguardaAdapter(List<Revistas> vanguardaList){


        //===========================================VANGUARDA==============================================
        //=========================================================================================

            firstIndex = vanguardaList.indexOf(vanguardaList.get(0));


            if (getContext()!=null){
                revistasVanguardaAdapter = new RevistasAdapter(vanguardaList,getContext());
                coverFlow2.setAdapter(revistasVanguardaAdapter);
            }
//            revistasVanguardaAdapter.notifyDataSetChanged();

            coverFlow2.scrollToPosition(firstIndex);

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
            coverFlow2.setOnItemClickListener((adapterView, view, i, l) -> {


                if (i<vanguardaList.size()){

                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",vanguardaList.get(i).getLink());
                    startActivity(intent);

                }

                if (i>=vanguardaList.size()){

                    i = indexVanguarda;

                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",vanguardaList.get(i).getLink());
                    startActivity(intent);
                }

            });

        cardVanguarda.setVisibility(View.VISIBLE);
        linearCarregarVanguarda.setVisibility(View.INVISIBLE);

    }

    private void setRumoAdapter(List<Revistas> rumoList){



            //==============================================RUMO===========================================
            //=========================================================================================

            firstIndex = rumoList.indexOf(rumoList.get(0));


        if (getContext()!=null){
            revistasRumoAdapter = new RevistasAdapter(rumoList,getContext());
            coverFlow3.setAdapter(revistasRumoAdapter);
        }


//            revistasRumoAdapter.notifyDataSetChanged();

            coverFlow3.scrollToPosition(firstIndex);
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
            coverFlow3.setOnItemClickListener((adapterView, view, i, l) -> {

                if (i<rumoList.size()){
                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",rumoList.get(i).getLink());
                    startActivity(intent);
                }

                if (i>=rumoList.size()){

                    i = indexRumo;

                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",rumoList.get(i).getLink());
                    startActivity(intent);
                }
            });

        cardRumo.setVisibility(View.VISIBLE);
        linearCarregarRumo.setVisibility(View.GONE);


    }


}
