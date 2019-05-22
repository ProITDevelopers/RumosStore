package proitappsolutions.com.rumosstore.fragmentos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.wang.avi.AVLoadingIndicatorView;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class FragConcurso extends Fragment {

    private RelativeLayout coordinatorLayout;
    private RelativeLayout errorLayout;
    private TextView btnTentarDeNovo;

    private LinearLayout progressBar;
    RingProgressBar anelprogressbar;

    View view;

    public FragConcurso() {}



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_concurso, container, false);

        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        errorLayout = view.findViewById(R.id.erroLayout);
        btnTentarDeNovo = view.findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

        progressBar = view.findViewById(R.id.linearProgresso);
        anelprogressbar = view.findViewById(R.id.progressbar_1);

        verifConecxao();

        return view;

    }

    private void verifConecxao() {

        if (getActivity() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }else{
                carregarWebView();
            }
        }

    }

    private void carregarWebView(){
        WebView webView = new WebView(getContext());
        webView = view.findViewById(R.id.webViewSorteio);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://sorteio.mediarumo.net/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    anelprogressbar.setProgress(progress);
                }

                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    anelprogressbar.setProgress(progress);
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                if(progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                    anelprogressbar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coordinatorLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                verifConecxao();
            }
        });
    }


}
