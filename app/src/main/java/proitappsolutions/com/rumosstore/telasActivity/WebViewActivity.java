package proitappsolutions.com.rumosstore.telasActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgTentarDeNovo;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends AppCompatActivity  {

    private RelativeLayout coordinatorLayout;
    private RelativeLayout errorLayout;
    private TextView btnTentarDeNovo;

    private LinearLayout progressBar;
    RingProgressBar anelprogressbar;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = findViewById(R.id.toolbar_activyt);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = new WebView(WebViewActivity.this);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        errorLayout = findViewById(R.id.erroLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText(msgTentarDeNovo);
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorExemplo));

        progressBar = findViewById(R.id.linearProgresso);
        anelprogressbar = findViewById(R.id.progressbar_1);

        if (getIntent().getExtras() != null){
            String cor = getIntent().getStringExtra("cor");
            verifConecxao(getIntent().getStringExtra("site"));
            switch (cor) {
                case "mercado":
                    Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.mercado));
                    ColorDrawable corMerdado = new ColorDrawable(ContextCompat.getColor(this, R.color.mercado));
                    getSupportActionBar().setBackgroundDrawable(corMerdado);
                    setSupportActionBar(toolbar);
                    break;
                case "vanguarda":
                    Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.vanguarda));
                    ColorDrawable corVanguarda = new ColorDrawable(ContextCompat.getColor(this, R.color.vanguarda));
                    getSupportActionBar().setBackgroundDrawable(corVanguarda);
                    break;
                case "rumo": {
                    Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.white));
                    ColorDrawable corRumo = new ColorDrawable(ContextCompat.getColor(this, R.color.white));
                    getSupportActionBar().setBackgroundDrawable(corRumo);
                    break;
                }
                case "instagram": {
                    Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.white));
                    ColorDrawable corRumo = new ColorDrawable(ContextCompat.getColor(this, R.color.white));
                    getSupportActionBar().setBackgroundDrawable(corRumo);
                    break;
                }
                case "facebook": {
                    Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.facebook_webview));
                    ColorDrawable corRumo = new ColorDrawable(ContextCompat.getColor(this, R.color.facebook_webview));
                    getSupportActionBar().setBackgroundDrawable(corRumo);
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void verifConecxao(String site) {

            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr!=null){
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null){
                    mostarMsnErro(site);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }else{
                    carregarWebView(site);
                }
            }

    }

    private void carregarWebView(String site){

        webView = findViewById(R.id.webViewMercado);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(site);

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

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    anelprogressbar.setTextColor(getResources().getColor(R.color.black));
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

    private void mostarMsnErro(String site){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(view -> {
            coordinatorLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            verifConecxao(site);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
