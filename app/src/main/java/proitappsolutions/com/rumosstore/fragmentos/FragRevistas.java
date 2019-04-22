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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.wang.avi.AVLoadingIndicatorView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class FragRevistas extends Fragment {

    private AVLoadingIndicatorView progressBar;
    private FeatureCoverFlow coverFlow;
    private FeatureCoverFlow coverFlow2;
    private FeatureCoverFlow coverFlow3;

    /*
    private KiosqueAdapter movieAdapter;
    private VanguardaAdapter movieAdapter2;
    private RumoAdapter movieAdapter3;
*/

    private TextSwitcher mTitle;
    private TextSwitcher mTitle2;
    private TextSwitcher mTitle3;
    private View view;


    /*
    public static RealmResults<Kiosque> mercadoList;
    public static RealmResults<Vangarda> vanguardaList;
    public static RealmResults<Rumo> rumoList;
*/
    public FragRevistas() {}



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_quiosque, container, false);

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


        //
        /*
        movieAdapter = new KiosqueAdapter(mercadoList,getContext());

        coverFlow = (FeatureCoverFlow)view.findViewById(R.id.coverflow);
        coverFlow.setAdapter(movieAdapter);


        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText((position + 1)+" de "+mercadoList.size());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i<mercadoList.size()){
                    mTitle.setText(mercadoList.get(i).getName());
//                    Intent intent = new Intent(getContext(), RevistaDetalheActivity.class);
//                    intent.putExtra("movie_index",i);
//                    intent.putExtra("type","mercado");
//                    startActivity(intent);
                }
            }
        });

        //=========================================================================================
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

        movieAdapter2 = new VanguardaAdapter(vanguardaList,getContext());

        coverFlow2 = (FeatureCoverFlow)view.findViewById(R.id.coverflow2);
        coverFlow2.setAdapter(movieAdapter2);


        coverFlow2.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle2.setText((position + 1)+" de "+vanguardaList.size());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (i<vanguardaList.size()){
                    mTitle2.setText(vanguardaList.get(i).getName());
//                    Intent intent = new Intent(getContext(), RevistaDetalheActivity.class);
//                    intent.putExtra("movie_index",i);
//                    intent.putExtra("type","vanguarda");
//                    startActivity(intent);
                }

            }
        });


        //=========================================================================================
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

        movieAdapter3 = new RumoAdapter(rumoList,getContext());


        coverFlow3 = (FeatureCoverFlow)view.findViewById(R.id.coverflow3);
        coverFlow3.setAdapter(movieAdapter3);


        coverFlow3.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle3.setText((position + 1)+" de "+rumoList.size());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i<rumoList.size()){
                    mTitle3.setText(rumoList.get(i).getName());
//                    Intent intent = new Intent(getContext(), RevistaDetalheActivity.class);
//                    intent.putExtra("movie_index",i);
//                    intent.putExtra("type","rumo");
//                    startActivity(intent);
                }
            }
        });

        if (mercadoList.size()>0 && vanguardaList.size()>0 && rumoList.size()>0){
            coverFlow.scrollToPosition(mercadoList.size());
            coverFlow2.scrollToPosition(vanguardaList.size());
            coverFlow3.scrollToPosition(rumoList.size());
        }

        */

        return view;

    }

    /*
    private void initData() {
        mercadoList = AppDatabase.getMercadoList();
        vanguardaList = AppDatabase.getVanguardaList();
        rumoList = AppDatabase.getRumoList();

    }
*/



}
