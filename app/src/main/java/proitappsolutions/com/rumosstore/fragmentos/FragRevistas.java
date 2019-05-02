package proitappsolutions.com.rumosstore.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wang.avi.AVLoadingIndicatorView;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.testeRealmDB.RevistaViewActivity;
import proitappsolutions.com.rumosstore.testeRealmDB.RevistasAdapter;

public class FragRevistas extends Fragment {

    private AVLoadingIndicatorView progressBar;
    private FeatureCoverFlow coverFlow;
    private FeatureCoverFlow coverFlow2;
    private FeatureCoverFlow coverFlow3;

    private RevistasAdapter revistasAdapter;

    private TextSwitcher mTitle;
    private TextSwitcher mTitle2;
    private TextSwitcher mTitle3;
    private View view;


    public FragRevistas() {}



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_revistas, container, false);

        progressBar = view.findViewById(R.id.progress);


        //initData();

        mTitle = (TextSwitcher)view.findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });

        Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_bottom);

        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        revistasAdapter = new RevistasAdapter(AppDatabase.getRevistasMercadoList(),getContext());
        coverFlow = (FeatureCoverFlow)view.findViewById(R.id.coverflow);
        coverFlow.setAdapter(revistasAdapter);


        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText((position + 1)+" de "+AppDatabase.getRevistasMercadoList().size());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i<AppDatabase.getRevistasMercadoList().size()){
                    mTitle.setText(AppDatabase.getRevistasMercadoList().get(i).getRevistaNome());
                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",AppDatabase.getRevistasMercadoList().get(i).getRevistaPDFLink());
                    startActivity(intent);
                }
            }
        });

        //===========================================VANGUARDA==============================================
        //=========================================================================================

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

        revistasAdapter = new RevistasAdapter(AppDatabase.getRevistasVanguardaList(),getContext());

        coverFlow2 = (FeatureCoverFlow)view.findViewById(R.id.coverflow2);
        coverFlow2.setAdapter(revistasAdapter);


        coverFlow2.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle2.setText((position + 1)+" de "+AppDatabase.getRevistasVanguardaList().size());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (i<AppDatabase.getRevistasVanguardaList().size()){
                    mTitle2.setText(AppDatabase.getRevistasVanguardaList().get(i).getRevistaNome());
                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",AppDatabase.getRevistasVanguardaList().get(i).getRevistaPDFLink());
                    startActivity(intent);
                }

            }
        });


        //==============================================RUMO===========================================
        //=========================================================================================

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

        revistasAdapter = new RevistasAdapter(AppDatabase.getRevistasRumoList(),getContext());
        coverFlow3 = (FeatureCoverFlow)view.findViewById(R.id.coverflow3);
        coverFlow3.setAdapter(revistasAdapter);


        coverFlow3.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle3.setText((position + 1)+" de "+AppDatabase.getRevistasRumoList().size());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i<AppDatabase.getRevistasRumoList().size()){
                    mTitle3.setText(AppDatabase.getRevistasRumoList().get(i).getRevistaNome());
                    Intent intent = new Intent(getContext(), RevistaViewActivity.class);
                    intent.putExtra("ViewType",AppDatabase.getRevistasRumoList().get(i).getRevistaPDFLink());
                    startActivity(intent);
                }
            }
        });

        if (AppDatabase.getRevistasRumoList().size()>0 && AppDatabase.getRevistasRumoList().size()>0 && AppDatabase.getRevistasRumoList().size()>0){
            coverFlow.scrollToPosition(AppDatabase.getRevistasMercadoList().size());
            coverFlow2.scrollToPosition(AppDatabase.getRevistasVanguardaList().size());
            coverFlow3.scrollToPosition(AppDatabase.getRevistasRumoList().size());
        }


        return view;

    }





}
