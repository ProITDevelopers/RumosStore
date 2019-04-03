package proitappsolutions.com.rumosstore.fragmentos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wang.avi.AVLoadingIndicatorView;

import proitappsolutions.com.rumosstore.R;

public class FragMediaRumo extends Fragment {

    private AVLoadingIndicatorView progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_media_rumo, container, false);

        progressBar = view.findViewById(R.id.progress);

        WebView webView = new WebView(getContext());
        webView = view.findViewById(R.id.webViewMediaRumo);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mediarumo.com/");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                if(progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });

        return view;

    }

}
