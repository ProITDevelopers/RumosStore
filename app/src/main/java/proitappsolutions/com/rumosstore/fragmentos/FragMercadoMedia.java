package proitappsolutions.com.rumosstore.fragmentos;

import android.app.Activity;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import proitappsolutions.com.rumosstore.R;

public class FragMercadoMedia extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private View view;
    private WebView webView;
    private LinearLayout progressBar;
    RingProgressBar anelprogressbar;


    public FragMercadoMedia() {
        // Required empty public constructor
    }


    public static FragMercadoMedia newInstance(String param1, String param2) {
        FragMercadoMedia fragment = new FragMercadoMedia();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_mercado_media, container, false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        carregarWebView(view);
    }

    private void carregarWebView(View view){

        webView = view.findViewById(R.id.webViewMercado);
        RelativeLayout coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        progressBar = view.findViewById(R.id.linearProgresso);
        anelprogressbar = view.findViewById(R.id.progressbar_1);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://mercado.co.ao/multimedia/videos");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String descricaoErro, String failingUrl) {
                super.onReceivedError(view, errorCode, descricaoErro, failingUrl);
                if (errorCode == -2) {
                    String mensagemCustomizada = "<html><body><div align=\"center\" >"
                            + "Falha ao carregar a página : " + failingUrl + "<br>" +
                            "A rede 3G ou WI-FI não possui tranferência de dados." + "<br>" + "</div></body>";
                    webView.loadDataWithBaseURL(null, mensagemCustomizada,
                            "text/html", "UTF-8", null);
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView, int progress) {

                    if (progress < 100){
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        anelprogressbar.setTextColor(Color.parseColor("#000000"));
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


}
