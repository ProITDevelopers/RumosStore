package proitappsolutions.com.rumosstore.fragmentos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class FragMercado extends Fragment {

    private AVLoadingIndicatorView progressBar;
    private TextView tvPorcento;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mercado, container, false);

        progressBar = view.findViewById(R.id.progress);
        tvPorcento = view.findViewById(R.id.tvPorcento);
        tvPorcento.setVisibility(View.VISIBLE);

        if (Common.isConnected(10000)){


        WebView webView = new WebView(getContext());
        webView = view.findViewById(R.id.webViewMercado);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mercado.co.ao/");
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {

                if (progress < 100){
                    Log.i("valorprogress","  " + progress);
                    tvPorcento.setText(progress + " %");
                }

                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                if(progress == 100) {
                    tvPorcento.setVisibility(View.GONE);
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        }else{
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            tvPorcento.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Verifique a sua ligação à internet", Toast.LENGTH_SHORT).show();
        }

        return view;

    }

}
