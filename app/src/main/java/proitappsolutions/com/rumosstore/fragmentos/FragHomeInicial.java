package proitappsolutions.com.rumosstore.fragmentos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import proitappsolutions.com.rumosstore.Adapter.FeedAdapter;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.communs.HTTPDataHandler;
import proitappsolutions.com.rumosstore.rssFeed.RSSObjecto;

public class FragHomeInicial extends Fragment {

    RecyclerView recyclerView;
    RSSObjecto rssObjecto;
    private RelativeLayout errorLayout;
    private LinearLayout linearLayout;
    private TextView btnTentarDeNovo;

    private final String RSS_link = "https://mercado.co.ao/rss/newsletter.xml";
    private final String RSS_PARA_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_inicial, container, false);
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

    private void carregarRss() {

        AsyncTask<String,String,String> carregaAsync = new AsyncTask<String, String, String>() {

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
                rssObjecto = new Gson().fromJson(s,RSSObjecto.class);
                FeedAdapter adapter = new FeedAdapter(rssObjecto,getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_PARA_JSON_API);
        url_get_data.append(RSS_link);
        carregaAsync.execute(url_get_data.toString());
    }

    private void verifConecxao() {

        if (getActivity() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();
            }else{
                carregarRss();
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


}
