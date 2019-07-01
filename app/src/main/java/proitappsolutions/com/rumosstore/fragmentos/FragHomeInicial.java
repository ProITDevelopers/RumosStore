package proitappsolutions.com.rumosstore.fragmentos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import proitappsolutions.com.rumosstore.Adapter.FeedAdapter;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.communs.HTTPDataHandler;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.rssFeed.RSSObjecto;

public class FragHomeInicial extends Fragment {

    RecyclerView recyclerView;
    RSSObjecto rssObjecto;
    private ProgressBar progress_amarela;
    private RelativeLayout errorLayout;
    private LinearLayout linearLayout;
    private TextView btnTentarDeNovo;
    private MinhaAsyncTask minhaAsyncTask;
    private final String RSS_link = "https://mercado.co.ao/rss/newsletter.xml";
    private final String RSS_link_vanguarda = "https://vanguarda.co.ao/rss/newsletter";
    private final String RSS_PARA_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_inicial, container, false);
        progress_amarela = view.findViewById(R.id.progress_amarela);
        recyclerView = view.findViewById(R.id.recyclerView);
        errorLayout = view.findViewById(R.id.erroLayout);
        linearLayout = view.findViewById(R.id.linearLayout);
        btnTentarDeNovo = view.findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        verifConecxao();

        return view;

    }

    public class MinhaAsyncTask extends AsyncTask<String,String,String>{


        public MinhaAsyncTask(Context mContext){
        }

        @Override
        protected String doInBackground(String... strings) {
            String result;
            HTTPDataHandler httpDataHandler = new HTTPDataHandler();
            result = httpDataHandler.GetHTTPData(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progress_amarela.setVisibility(View.GONE);
            rssObjecto = new Gson().fromJson(s, RSSObjecto.class);
            FeedAdapter adapter = new FeedAdapter(rssObjecto, getContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    /* private void carregarRss() {

          AsyncTask<String, String, String> carregaAsync = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... strings) {
                String result;
                HTTPDataHandler httpDataHandler = new HTTPDataHandler();
                result = httpDataHandler.GetHTTPData(strings[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                progress_amarela.setVisibility(View.GONE);
                rssObjecto = new Gson().fromJson(s, RSSObjecto.class);
                FeedAdapter adapter = new FeedAdapter(rssObjecto, getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_PARA_JSON_API);
        url_get_data.append(RSS_link);
        //url_get_data.append(RSS_link_vanguarda);
        carregaAsync.execute(url_get_data.toString());
    }*/

    private void verifConecxao() {

        if (getActivity() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr!=null){
                progress_amarela.setVisibility(View.GONE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    mostarMsnErro();
                }else{
                    if (!MetodosComuns.temTrafegoNaRede(getActivity()))
                        MetodosComuns.mostrarMensagem(getActivity(),R.string.txtMsg);
                    else
                        progress_amarela.setVisibility(View.VISIBLE);
                    minhaAsyncTask = new MinhaAsyncTask(getContext());
                    minhaAsyncTask.execute(RSS_PARA_JSON_API + RSS_link);
                    //        carregarRss();
                }
            }
        }
    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                verifConecxao();
            }
        });
    }

    @Override
    public void onDestroy() {
        minhaAsyncTask.cancel(true);
        super.onDestroy();
    }
}