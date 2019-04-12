package proitappsolutions.com.rumosstore.fragmentos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import proitappsolutions.com.rumosstore.Adapter.FeedAdapter;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.communs.HTTPDataHandler;
import proitappsolutions.com.rumosstore.rssFeed.RSSObjecto;

public class FragHomeInicial extends Fragment {

    RecyclerView recyclerView;
    RSSObjecto rssObjecto;

    private final String RSS_link = "https://mercado.co.ao/rss/newsletter.xml";
    private final String RSS_PARA_JSON_API = " https://api.rss2json.com/v1/api.json?rss_url=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_inicial, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        carregarRss();
        
        return view;

    }

    private void carregarRss() {

        AsyncTask<String,String,String> carregaAsync = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(getContext());

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Aguarde");
                mDialog.show();
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
                mDialog.dismiss();
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

}
