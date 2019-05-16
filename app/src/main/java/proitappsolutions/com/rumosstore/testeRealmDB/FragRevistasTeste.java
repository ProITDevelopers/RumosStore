package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragRevistasTeste extends Fragment {

    private AVLoadingIndicatorView progressBar;
    private FeatureCoverFlow coverFlow;
    private FeatureCoverFlow coverFlow2;
    private FeatureCoverFlow coverFlow3;

    private RevistasAdapter revistasMercadoAdapter,revistasVanguardaAdapter,revistasRumoAdapter;

    private TextSwitcher mTitle;
    private TextSwitcher mTitle2;
    private TextSwitcher mTitle3;

    public Revistas revistas;


    Animation in,out;

    private RelativeLayout errorLayout;
    private LinearLayout linearLayout,linearLayoutCarregando;
    private TextView btnTentarDeNovo,txtCarregando;

    List<Revistas> revistasList, mercadoList,vanguardaList,rumoList;

    int indexMercado,indexVanguarda,indexRumo=0;
    int firstIndex;
    int lastIndex;

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
        mercadoList = new ArrayList<>();
        vanguardaList = new ArrayList<>();
        rumoList = new ArrayList<>();

        in = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_top);
        out = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_bottom);

        progressBar = view.findViewById(R.id.progress);
        errorLayout = view.findViewById(R.id.erroLayout);
        linearLayout = view.findViewById(R.id.linearLayout);
        linearLayoutCarregando = view.findViewById(R.id.linearLayout2);
        txtCarregando = view.findViewById(R.id.txtCarregando);
        btnTentarDeNovo = view.findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

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
            linearLayout.setVisibility(View.INVISIBLE);
            linearLayoutCarregando.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.INVISIBLE);
                txtCarregando.setText("Carregando...");
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

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Bem-vindo "+AppDatabase.getUser().getNomeCliente(), Toast.LENGTH_SHORT).show();
                } else {

                    revistasList = response.body();

                    if (revistasList!=null){
                        setRevistasAdapter(revistasList);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        txtCarregando.setTextSize(22);
                        txtCarregando.setText("Sem resultados!");
                    }





                }




            }

            @Override
            public void onFailure(Call<List<Revistas>> call, Throwable t) {

            }
        });



    }

    private void setRevistasAdapter(List<Revistas> revistasList){

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

        //===========================================MERCADO==============================================
        //=========================================================================================

        firstIndex = mercadoList.indexOf(mercadoList.get(0));
        lastIndex = mercadoList.lastIndexOf(mercadoList.get(mercadoList.size()-1));


        revistasMercadoAdapter = new RevistasAdapter(mercadoList,getContext());
        revistasMercadoAdapter.notifyDataSetChanged();
        coverFlow.setAdapter(revistasMercadoAdapter);

        coverFlow.scrollToPosition(firstIndex);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                indexMercado = position;
                mTitle.setText((position + 1)+" de "+mercadoList.size());
            }

            @Override
            public void onScrolling() {

            }
        });



        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                if (i<mercadoList.size()){
                    mTitle2.setText(mercadoList.get(i).getNome());
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


        //===========================================VANGUARDA==============================================
        //=========================================================================================

        firstIndex = vanguardaList.indexOf(vanguardaList.get(0));
        lastIndex = vanguardaList.lastIndexOf(vanguardaList.get(vanguardaList.size()-1));

        revistasVanguardaAdapter = new RevistasAdapter(vanguardaList,getContext());
        revistasVanguardaAdapter.notifyDataSetChanged();
        coverFlow2.setAdapter(revistasVanguardaAdapter);

        coverFlow2.scrollToPosition(firstIndex);

        coverFlow2.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                indexVanguarda = position;
                mTitle2.setText((position + 1)+" de "+vanguardaList.size());
            }

            @Override
            public void onScrolling() {

            }
        });
        coverFlow2.setOnItemClickListener((adapterView, view, i, l) -> {


            if (i<vanguardaList.size()){

                mTitle2.setText(vanguardaList.get(i).getNome());
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


        //==============================================RUMO===========================================
        //=========================================================================================

        firstIndex = rumoList.indexOf(rumoList.get(0));
        lastIndex = rumoList.lastIndexOf(rumoList.get(rumoList.size()-1));


        revistasRumoAdapter = new RevistasAdapter(rumoList,getContext());
        revistasRumoAdapter.notifyDataSetChanged();
        coverFlow3.setAdapter(revistasRumoAdapter);

        coverFlow3.scrollToPosition(firstIndex);
        coverFlow3.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                indexRumo = position;
                mTitle3.setText((position + 1)+" de "+rumoList.size());
            }

            @Override
            public void onScrolling() {

            }
        });
        coverFlow3.setOnItemClickListener((adapterView, view, i, l) -> {

            if (i<rumoList.size()){
                mTitle3.setText(rumoList.get(i).getNome());
                Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                intent.putExtra("ViewType",rumoList.get(i).getLink());
                startActivity(intent);
            }

            if (i>=rumoList.size()){

                i = indexRumo;

                Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                intent.putExtra("ViewType",vanguardaList.get(i).getLink());
                startActivity(intent);
            }
        });
        

        linearLayout.setVisibility(View.VISIBLE);
        linearLayoutCarregando.setVisibility(View.GONE);


    }







}
