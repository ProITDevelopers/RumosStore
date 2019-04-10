package proitappsolutions.com.rumosstore.fragmentos;

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

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.revistas.Kiosque;
import proitappsolutions.com.rumosstore.revistas.KiosqueAdapter;



public class FragQuiosque extends Fragment {

    private AVLoadingIndicatorView progressBar;
    private FeatureCoverFlow coverFlow;
    private FeatureCoverFlow coverFlow2;
    private FeatureCoverFlow coverFlow3;

    private KiosqueAdapter movieAdapter;
    private KiosqueAdapter movieAdapter2;
    private KiosqueAdapter movieAdapter3;


    private TextSwitcher mTitle;
    private TextSwitcher mTitle2;
    private TextSwitcher mTitle3;
    private View view;

    public FragQuiosque() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_quiosque, container, false);

        progressBar = view.findViewById(R.id.progress);

        initData();
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
        movieAdapter = new KiosqueAdapter(Common.mercadoList,getContext());

        coverFlow = (FeatureCoverFlow)view.findViewById(R.id.coverflow);
        coverFlow.setAdapter(movieAdapter);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(Common.mercadoList.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i<Common.mercadoList.size()){
                    Toast.makeText(getContext(), Common.mercadoList.get(i).getName(), Toast.LENGTH_SHORT).show();
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

        movieAdapter2 = new KiosqueAdapter(Common.vanguardaList,getContext());

        coverFlow2 = (FeatureCoverFlow)view.findViewById(R.id.coverflow2);
        coverFlow2.setAdapter(movieAdapter2);

        coverFlow2.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle2.setText(Common.vanguardaList.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (i<Common.vanguardaList.size()){
                    Toast.makeText(getContext(), Common.vanguardaList.get(i).getName(), Toast.LENGTH_SHORT).show();
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

        movieAdapter3 = new KiosqueAdapter(Common.rumoList,getContext());


        coverFlow3 = (FeatureCoverFlow)view.findViewById(R.id.coverflow3);
        coverFlow3.setAdapter(movieAdapter3);

        coverFlow3.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle3.setText(Common.rumoList.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });

        coverFlow3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i<Common.rumoList.size()){
                    Toast.makeText(getContext(), Common.rumoList.get(i).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;

    }

    private void initData() {
        Common.mercadoList.add(new Kiosque("Mercado: Bodiva","http://www.meiosepublicidade.pt/wp-content/uploads/2015/04/Mercado.jpg"));
        Common.mercadoList.add(new Kiosque("Mercado: Orçamentos plurianuais","https://thumbs.web.sapo.io/?pic=http://ia.imgs.sapo.pt/01/images/6/6a/10035_6a4bd2af5992c06127cdaa6c653fc13e.png&W=562&H=687&delay_optim=1&tv=2"));
        Common.mercadoList.add(new Kiosque("Mercado: Carteira de Crédito","https://thumbs.web.sapo.io/?epic=ZTNhspFkzW05QypyBy2e6sOqiN+xc46eRqGmPGJwELSfWoaGPJeUzV2d9FnnZWHQ2GpuI7kXE/scGBJnFfU1nO59kvGBD3m90mqwSBY4FkV5cx6hysVQuiEw5PqaXjHRALjT&W=1050&H=0&delay_optim=1&tv=1"));
        Common.mercadoList.add(new Kiosque("Mercado: Ministro da geologia e minas","https://thumbs.web.sapo.io/?epic=YzMxJA+sW1YQCQvx8WGAAXdKwUL0yC3atRL34wj12lhHUV3K5k8eQfFrL89Y+CEMSQO6nHFHW59ESBRHHrHpv9JdhnDk2FFdni9i8wBsmA6h44wGwx11M9ZwBZRuFeU3o5Mr&W=1050&H=0&delay_optim=1&tv=1"));
        Common.mercadoList.add(new Kiosque("Mercado: Novo sistema cambial","https://thumbs.web.sapo.io/?epic=MTdkPzkQXvRUIglnK9fA3CA8LJE1RL/1OjTa+akpBPytCK/0EarlHXUAFscjJDYF8+4fLe0ygG6EHM5HLFeW6bfbM15jG1PkrMe6mA9eJTfXNUJYz227XeyEt91mT4tBMjht&W=1050&H=0&png=1&delay_optim=1&tv=1"));
        Common.mercadoList.add(new Kiosque("Mercado: BFA recupera divisas","https://thumbs.web.sapo.io/?epic=Yjgwn6GtlzJekPGMdf8w+gO/VjQANT01rfyeDyFyZUj2c0DbBFNIE3GR/MmnyEP0EYrBfhbjtcd9moSUbLk2yE7q6GY0I4JCi5WuC3qqMZi58sJR3E3i/cEkIUjvrT+vLKV6&W=240&H=0&png=1&delay_optim=1&tv=1"));

        Common.vanguardaList.add(new Kiosque("Vanguarda: Revolução Pacífica ","https://cdn.worldpresstitles.com/image/portugal/default/MDMwNDIwMTglN2N2YW5ndWFyZGE1YWMzMmU4NTUzN2Ux"));
        Common.vanguardaList.add(new Kiosque("Vanguarda: Custo político","https://scontent-frt3-2.cdninstagram.com/vp/279d9c1a7a12a9f699ce516dbce47b9f/5D1247E0/t51.2885-15/e35/51330807_2555187241220551_4165445216191147393_n.jpg?_nc_ht=instagram.fbne2-1.fna.fbcdn.net"));
        Common.vanguardaList.add(new Kiosque("Vanguarda: Estado de graça","https://4.bp.blogspot.com/-18kmZLxWybU/WfTFere-PyI/AAAAAAAAasQ/bBGFL11lwdMkYko_aqf-kUlGyyz_JuPggCLcBGAs/s1600/V39_2017Out20_CIRGL_entrevista_capa.jpg"));
        Common.vanguardaList.add(new Kiosque("Vanguarda: Presidente prudente","https://pbs.twimg.com/media/DClyyr2W0AA7z4M.jpg:large"));
        Common.vanguardaList.add(new Kiosque("Vanguarda: Separação de poderes","https://scontent-lax3-1.cdninstagram.com/vp/a04b066b9d3a71a78abe43b1f9057926/5CEA4ECE/t51.2885-15/e35/c0.93.750.750/s480x480/49379001_139197417016578_7533300775527334327_n.jpg?_nc_ht=scontent-lax3-1.cdninstagram.com"));

        Common.rumoList.add(new Kiosque("Rumo: Gestora dos desafios","https://thumbs.web.sapo.io/?epic=YmI0NVX/QnIv8cIeq/m94SU8Ade3vfIa4DmOhN5TLKNcys+s8FA/CnxRzHbiboyRMf3jM20FQs6Qq+XHbbjDQwCdBfSAsOfxWk9VnAhDNrCHoNE=&W=240&H=0&png=1&delay_optim=1&tv=1"));
        Common.rumoList.add(new Kiosque("Rumo: Cultura de Mudança","https://thumbs.web.sapo.io/?epic=ZjA2jNEocMhQzAnZgaRmAIws1oOU5ZlTCUlGKPmh8rr2Zv8WdkJ2WCHaj5g85GvqlsqcmOH280uGXOb+DUbFHueWLl/uP2ncB4s16zWHue/iBsY=&W=240&H=0&png=1&delay_optim=1&tv=1"));
        Common.rumoList.add(new Kiosque("Rumo: Banca Nacional","https://thumbs.web.sapo.io/?epic=ZTUyzf05nwJFJ0JRe8zPxHFZL8rVSme7Az9w0s09JEkffF39+jbXxlX1EIpk1VVGuzdmEd6KdN0A2S/Mm7OaF84FnknPBTDiCr0/4pi6yElVE04=&W=240&H=0&png=1&delay_optim=1&tv=1"));
        Common.rumoList.add(new Kiosque("Rumo: Empreendedor","https://scontent-iad3-1.cdninstagram.com/vp/5cd76a465303cb09a4c603f430f3960e/5CE22540/t51.2885-15/e35/40304937_166341154294670_2602381243250387311_n.jpg?_nc_ht=scontent-iad3-1.cdninstagram.com"));
        Common.rumoList.add(new Kiosque("Rumo: Cabeto","http://www.grupocabeto.com/portals/6/imagens/rumo%20ed.%2029%20maro%202012.jpg"));
        Common.rumoList.add(new Kiosque("Rumo: A Srª ANIP","http://www.meiosepublicidade.pt/wp-content/uploads/2012/02/rumo-2.jpg"));



    }
}
