package proitappsolutions.com.rumosstore.communs;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.util.regex.Pattern;

public class MetodosComuns {

    public static String bearerApi = "Bearer ";
    public static String msgErro = "Preencha o campo.";
    public static String msgErroSenha = "Senha fraca.";
    public static String msgQuasePronto = "Quase Pronto...!";
    public static String msgSenhaAlterada = "A sua senha foi alterada com sucesso.!";
    public static String msgErroSenhaDiferente = "As senhas devem ser iguais";
    public static String msgAprocessar = "A processar...!";
    public static String msgDadosAlterados = "Os dados foram alterados com sucesso.";
    public static String msgSalvandoFoto = "Salvando a foto de perfil.";
    public static String msgDesejaTerminarSessao = "Deseja terminar a sessão ?";
    public static String msgReenviarNumTelef = "A reenviar o Nº Telefone..";
    public static String msgAEnviarEmail = "A enviar o e-mail..";
    public static String msgVerificando = "Verificando...";
    public static String msgTentarDeNovo = "Tentar de Novo";
    public static String msgVoltar = "Voltar";
    public static String msgCamposIguais = "Os campos devem ser iguais.";
    public static String msgCamposDiferentes = "Os campos devem ser diferentes.";
    public static String msgErroSEmail = "Preencha o campo com um email.";
    public static String msgErroSTelefone = "Preencha o campo com um nº telefone.";
    public static String msgErroTelefone = "Preencha com um número válido";
    public static String msgEnviandoCodigo = "Enviando o código de confirmação..!";
    public static String msgSenhaFracaAjuda = "Senha fraca.O campo precisa de ter mais de 6 caracteres.";

    public static boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void esconderTeclado(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static boolean temTrafegoNaRede(Context mContexto) {
        ConnectivityManager cm =
                (ConnectivityManager) mContexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void mostrarMensagem(Context mContexto, int mensagem) {
        Toast.makeText(mContexto,mensagem,Toast.LENGTH_SHORT).show();
    }

    public static void mostrarMensagem(Context mContexto, String mensagem) {
        Toast.makeText(mContexto,mensagem,Toast.LENGTH_SHORT).show();
    }

    public static boolean conexaoInternetTrafego(Context context){
        String site = "www.google.com";
        WebView webViewInternet = new WebView(context);
        final boolean[] valorRetorno = new boolean[1];

        webViewInternet.setWebViewClient(new WebViewClient());
        webViewInternet.loadUrl(site);

        webViewInternet.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String descricaoErro, String failingUrl) {
                super.onReceivedError(view, errorCode, descricaoErro, failingUrl);
                if (errorCode == -2) {
                    valorRetorno[0] = false;
                }
            }
        });
        webViewInternet.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                valorRetorno[0] = true;
            }
        });

        return valorRetorno[0];
    }

}
