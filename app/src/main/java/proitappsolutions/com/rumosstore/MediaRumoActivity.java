package proitappsolutions.com.rumosstore;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorResponce;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorUtils;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.modelo.CodConfirmacaoResult;
import proitappsolutions.com.rumosstore.modelo.Data;
import proitappsolutions.com.rumosstore.modelo.DataUserApi;
import proitappsolutions.com.rumosstore.modelo.RecuperarSenha;
import proitappsolutions.com.rumosstore.modelo.Usuario;
import proitappsolutions.com.rumosstore.telasActivity.MeuPerfilActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.bearerApi;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.conexaoInternetTrafego;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.esconderTeclado;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgAEnviarEmail;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgAEnviarTelefone;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgAprocessar;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgCamposIguais;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgEnviandoCodigo;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErro;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErroSEmail;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErroSTelefone;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErroTelefone;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgReenviarNumTelef;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgSenhaFracaAjuda;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgSupporte;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgVerificando;


public class MediaRumoActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MediaRumoActivityDebug";
    private EditText editTextEmailLogin;
    private ShowHidePasswordEditText editTextPasslLogin;

    private ProgressDialog progressDialog;
    public Data data = new Data();
    public DataUserApi dataUserApi = new DataUserApi();

    //Esqueceu a senha? ----Componentes interface da caixa de dialogo
    private Dialog dialogOpcaoSenha;

    //Esqueceu a senha? ----Componentes interface da caixa de dialogo Enviar Email
    private Dialog dialogOpcaoSenhaEnviarEmail;
    private AppCompatEditText dialog_editEmail_email;
    private String emailRedif_senha;

    //Esqueceu a senha? ----Componentes interface da caixa de dialogo Enviar Numero Telefone
    private Dialog dialogOpcaoSenhaEnviarTelefone;
    private AppCompatEditText dialog_editTelefone_telefone;
    private String telefoneRedif_senha;
    private Pinview pinCodigoConfirmacaoTelef;

    //Esqueceu a senha? ----EnviarCodRedifinicao
    private Dialog dialogSenhaEnviarEmailCodReset;
    private Pinview pinCodigoConfirmacaoEmail;
    private TextView tv_email;
    private String id;
    private String emailReceberDeNovo;
    private String codigoConfEmail;

    //Esqueceu a senha? ----EnviarCodRedifinicao Telefone
    private Dialog dialogSenhaEnviarTelefoneCodReset;
    private TextView tv_telefone;
    private String idTelef;
    private String telefoneReceberDeNovo;
    private String codigoConfTelef;

    //Esqueceu a senha? ----SalvarSenha
    private Dialog dialogSenhaEnviarEmailSenhaNova;
    private ShowHidePasswordEditText edtSenhaNova, edtConfSenha;
    private String senha1;
    private String token;
    View raiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_rumo);

        inicializar();

        verifConecxao();
    }

    private void inicializar() {

        editTextEmailLogin = findViewById(R.id.editTextEmaiLogin);
        editTextPasslLogin = findViewById(R.id.editTextPasslLogin);
        Button btnEntrar = findViewById(R.id.btnEntrar);
        Button btnRegistrate = findViewById(R.id.btnRegistrate);
        Button btn_alterar_senha = findViewById(R.id.btn_alterar_senha);
        btnEntrar.setOnClickListener(MediaRumoActivity.this);
        btnRegistrate.setOnClickListener(MediaRumoActivity.this);
        btn_alterar_senha.setOnClickListener(MediaRumoActivity.this);
        progressDialog = new ProgressDialog(MediaRumoActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);
        raiz = findViewById(android.R.id.content);

        dialogOpcaoSenha = new Dialog(MediaRumoActivity.this);
        dialogOpcaoSenha.setContentView(R.layout.dialogo_activity_opcao_rec_senha);
        Button dialog_btn_email_redif = dialogOpcaoSenha.findViewById(R.id.dialog_btn_email_redif);
        Button dialog_btn_telef_redif = dialogOpcaoSenha.findViewById(R.id.dialog_btn_telef_redif);
        dialog_btn_email_redif.setOnClickListener(MediaRumoActivity.this);
        dialog_btn_telef_redif.setOnClickListener(MediaRumoActivity.this);

        dialogOpcaoSenhaEnviarEmail = new Dialog(MediaRumoActivity.this);
        dialogOpcaoSenhaEnviarEmail.setContentView(R.layout.dialogo_activity_opcao_rec_senha_email);
        Button dialog_btn_email_redif_enviar_email = dialogOpcaoSenhaEnviarEmail.findViewById(R.id.dialog_btn_email_redif_enviar_email);
        dialog_editEmail_email = dialogOpcaoSenhaEnviarEmail.findViewById(R.id.dialog_editEmail_email);
        Button dialog_btn_cancelar_enviar_email = dialogOpcaoSenhaEnviarEmail.findViewById(R.id.dialog_btn_cancelar_enviar_email);
        dialogOpcaoSenhaEnviarEmail.setCancelable(false);
        dialog_btn_cancelar_enviar_email.setOnClickListener(MediaRumoActivity.this);
        dialog_btn_email_redif_enviar_email.setOnClickListener(MediaRumoActivity.this);

        dialogOpcaoSenhaEnviarTelefone = new Dialog(MediaRumoActivity.this);
        dialogOpcaoSenhaEnviarTelefone.setContentView(R.layout.dialogo_activity_opcao_rec_senha_telefone);
        Button dialog_btn_cancelar_enviar_telefone = dialogOpcaoSenhaEnviarTelefone.findViewById(R.id.dialog_btn_cancelar_enviar_telefone);
        dialog_editTelefone_telefone = dialogOpcaoSenhaEnviarTelefone.findViewById(R.id.dialog_editTelefone_telefone);
        Button dialog_btn_telefone_redif_enviar_telefone = dialogOpcaoSenhaEnviarTelefone.findViewById(R.id.dialog_btn_telefone_redif_enviar_telefone);
        dialogOpcaoSenhaEnviarTelefone.setCancelable(false);
        dialog_btn_cancelar_enviar_telefone.setOnClickListener(MediaRumoActivity.this);
        dialog_btn_telefone_redif_enviar_telefone.setOnClickListener(MediaRumoActivity.this);

        //Dialog enviar codigo de confirmacao
        dialogSenhaEnviarEmailCodReset = new Dialog(MediaRumoActivity.this);
        dialogSenhaEnviarEmailCodReset.setContentView(R.layout.dialogo_activity_op_email_reset);
        dialogSenhaEnviarEmailCodReset.setCancelable(false);
        pinCodigoConfirmacaoEmail = dialogSenhaEnviarEmailCodReset.findViewById(R.id.pinCodigoConfirmacaoEmail);
        tv_email = dialogSenhaEnviarEmailCodReset.findViewById(R.id.tv_email);
        TextView receberDeNovo = dialogSenhaEnviarEmailCodReset.findViewById(R.id.receberDeNovo);
        Button btn_enviar_cod_reset = dialogSenhaEnviarEmailCodReset.findViewById(R.id.btn_enviar_cod_reset);
        LinearLayout linearBtnFechar = dialogSenhaEnviarEmailCodReset.findViewById(R.id.linearBtnFechar);
        receberDeNovo.setOnClickListener(MediaRumoActivity.this);
        btn_enviar_cod_reset.setOnClickListener(MediaRumoActivity.this);
        linearBtnFechar.setOnClickListener(MediaRumoActivity.this);

        //-------------------------------------------------------------
        //Dialog enviar codigo de confirmacao telefone
        dialogSenhaEnviarTelefoneCodReset = new Dialog(MediaRumoActivity.this);
        dialogSenhaEnviarTelefoneCodReset.setContentView(R.layout.dialogo_activity_op_telefone_reset);
        dialogSenhaEnviarTelefoneCodReset.setCancelable(false);
        tv_telefone = dialogSenhaEnviarTelefoneCodReset.findViewById(R.id.tv_telefone);
        pinCodigoConfirmacaoTelef = dialogSenhaEnviarTelefoneCodReset.findViewById(R.id.pinCodigoConfirmacaoTelef);
        TextView receberDeNovoTelefone = dialogSenhaEnviarTelefoneCodReset.findViewById(R.id.receberDeNovoTelefone);
        Button btn_enviar_cod_resetTelef = dialogSenhaEnviarTelefoneCodReset.findViewById(R.id.btn_enviar_cod_resetTelef);
        LinearLayout linearBtnFecharTelef = dialogSenhaEnviarTelefoneCodReset.findViewById(R.id.linearBtnFecharTelef);
        receberDeNovoTelefone.setOnClickListener(MediaRumoActivity.this);
        btn_enviar_cod_resetTelef.setOnClickListener(MediaRumoActivity.this);
        linearBtnFecharTelef.setOnClickListener(MediaRumoActivity.this);
        //-------------------------------------------------------------

        //Dialogo enviar senha nova email
        dialogSenhaEnviarEmailSenhaNova = new Dialog(MediaRumoActivity.this);
        dialogSenhaEnviarEmailSenhaNova.setContentView(R.layout.dialogo_activity_op_email_senha);
        dialogSenhaEnviarEmailSenhaNova.setCancelable(false);
        token = null;
        edtSenhaNova = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.edtSenhaNova);
        edtConfSenha = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.edtConfSenha);
        Button btn_redif_senha = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.btn_redif_senha);
        Button btn_cancelar = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.btn_cancelar);
        btn_redif_senha.setOnClickListener(MediaRumoActivity.this);
        btn_cancelar.setOnClickListener(MediaRumoActivity.this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnEntrar:
                autenticacaoLoginApi();
                esconderTeclado(MediaRumoActivity.this);
                break;

            case R.id.btnRegistrate:
                Intent intentRegistrar = new Intent(MediaRumoActivity.this, RegistroActivity.class);
                startActivity(intentRegistrar);
                break;

            case R.id.btn_alterar_senha:
                dialogOpcaoSenha.show();
                break;

            case R.id.dialog_btn_email_redif:
                inserirEmailRedif();
                break;

            case R.id.dialog_btn_telef_redif:
                inserirTelefRedif();
                break;

            case R.id.dialog_btn_email_redif_enviar_email:
                enviarEmailRedif();
                esconderTeclado(MediaRumoActivity.this);
                break;

            case R.id.dialog_btn_telefone_redif_enviar_telefone:
                esconderTeclado(MediaRumoActivity.this);
                enviarTelefonelRedif();
                break;

            case R.id.dialog_btn_cancelar_enviar_email:
                cancelarEnvioEmail();
                esconderTeclado(MediaRumoActivity.this);
                break;

            case R.id.dialog_btn_cancelar_enviar_telefone:
                esconderTeclado(MediaRumoActivity.this);
                cancelarEnvioTelefone();
                break;

            case R.id.receberDeNovo:
                if (!TextUtils.isEmpty(emailReceberDeNovo)) {
                    esconderTeclado(MediaRumoActivity.this);
                    enviarEmailRedifDeNovo();
                } else {
                    esconderTeclado(MediaRumoActivity.this);
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTentarmaistarde);
                }
                break;
            case R.id.receberDeNovoTelefone:
                if (!TextUtils.isEmpty(telefoneReceberDeNovo)) {
                    enviarTelefoneRedifDeNovo();
                } else {
                    esconderTeclado(MediaRumoActivity.this);
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTentarmaistarde);
                }
                break;
            case R.id.btn_enviar_cod_reset:
                if (verificarCampo()){
                    esconderTeclado(MediaRumoActivity.this);
                    enviarCodRedifinicao();
                }
                break;
            case R.id.btn_enviar_cod_resetTelef:
                if (verificarCampoTelef()){
                    esconderTeclado(MediaRumoActivity.this);
                    enviarCodRedifinicaoTelef();
                }
                break;
            case R.id.linearBtnFechar:
                esconderTeclado(MediaRumoActivity.this);
                dialogSenhaEnviarEmailCodReset.cancel();
                limparPinView(pinCodigoConfirmacaoEmail);
                break;
            case R.id.linearBtnFecharTelef:
                esconderTeclado(MediaRumoActivity.this);
                dialogSenhaEnviarTelefoneCodReset.cancel();
                limparPinView(pinCodigoConfirmacaoTelef);
                break;
            case R.id.btn_redif_senha:
                if (verificarCampoSenhas()) {
                    esconderTeclado(MediaRumoActivity.this);
                    salvarSenhaNova();
                }
                break;

            case R.id.btn_cancelar:
                cancelarAlterarSenhaEmail();
                esconderTeclado(MediaRumoActivity.this);
                break;
        }
    }

    private void enviarEmailRedifDeNovo() {

        dialog_editEmail_email.setError(null);
        String email = emailReceberDeNovo.trim();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<RecuperarSenha> enviarEmailReset = apiInterface.enviarEmail(email);
        progressDialog.setMessage(msgAEnviarEmail);
        progressDialog.show();
        enviarEmailReset.enqueue(new Callback<RecuperarSenha>() {
            @Override
            public void onResponse(@NonNull Call<RecuperarSenha> call, @NonNull Response<RecuperarSenha> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtCodigoenviado);
                } else {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    mostrarMensagem(MediaRumoActivity.this, errorResponce.getError());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecuperarSenha> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });

    }

    private void enviarTelefoneRedifDeNovo() {

        dialog_editTelefone_telefone.setError(null);
        String telefone = telefoneReceberDeNovo.trim();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<RecuperarSenha> enviarYelefoneReset = apiInterface.enviarTelefone(telefone);
        progressDialog.setMessage(msgReenviarNumTelef);
        progressDialog.show();
        enviarYelefoneReset.enqueue(new Callback<RecuperarSenha>() {
            @Override
            public void onResponse(@NonNull Call<RecuperarSenha> call, @NonNull Response<RecuperarSenha> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtCodigoenviado);
                } else {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    Toast.makeText(MediaRumoActivity.this, errorResponce.getError(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecuperarSenha> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });

    }

    private void cancelarAlterarSenhaEmail() {
        dialogSenhaEnviarEmailSenhaNova.cancel();
        edtSenhaNova.getText().clear();
        edtConfSenha.getText().clear();
    }

    private void salvarSenhaNova() {

        //enviarNovaSenha
        progressDialog.setMessage(msgAprocessar);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> enviarSenhaNova = apiInterface.enviarNovaSenha(bearerApi + token, senha1);

        enviarSenhaNova.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    dialogSenhaEnviarEmailSenhaNova.cancel();
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtSenhaalterada);
                    apagarCamposDialogResetCodeSenhas();
                } else {
                    progressDialog.dismiss();
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    mostrarMensagem(MediaRumoActivity.this, errorResponce.getError());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });
    }

    private void apagarCamposDialogResetCodeSenhas() {
        edtSenhaNova.getText().clear();
        edtConfSenha.getText().clear();
    }

    private boolean verificarCampoSenhas() {

        senha1 = edtSenhaNova.getText().toString().trim();
        String senha2 = edtConfSenha.getText().toString().trim();

        if (senha1.isEmpty()) {
            edtSenhaNova.setError(msgErro);
            return false;
        }

        if (senha1.length() < 5) {
            edtSenhaNova.setError(msgSenhaFracaAjuda);
            edtSenhaNova.requestFocus();
            return false;
        }

        if (senha2.isEmpty()) {
            edtConfSenha.setError(msgErro);
            return false;
        }

        if (senha2.length() < 5) {
            edtConfSenha.setError(msgSenhaFracaAjuda);
            edtConfSenha.requestFocus();
            return false;
        }

        if (!senha1.equals(senha2)) {
            edtConfSenha.setError(msgCamposIguais);
            edtConfSenha.requestFocus();
            return false;
        }

        return true;


    }

    private boolean verificarCampo() {
        codigoConfEmail = pinCodigoConfirmacaoEmail.getValue();
        if (codigoConfEmail.isEmpty()) {
            mostrarMensagem(MediaRumoActivity.this, R.string.txtMsgCondConf);
            return false;
        }
        if (codigoConfEmail.length() !=6) {
            mostrarMensagem(MediaRumoActivity.this, R.string.txtMsgCondConfCod);
            return false;
        }
        return true;

    }

    private boolean verificarCampoTelef() {
        codigoConfTelef = pinCodigoConfirmacaoTelef.getValue();
        if (codigoConfTelef.isEmpty()) {
            mostrarMensagem(MediaRumoActivity.this, R.string.txtMsgCondConf);
            return false;
        }
        if (codigoConfTelef.length() !=6) {
            mostrarMensagem(MediaRumoActivity.this, R.string.txtMsgCondConfCod);
            return false;
        }
        return true;

    }

    private void enviarCodRedifinicao() {

        progressDialog.setMessage(msgEnviandoCodigo);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<CodConfirmacaoResult> enviarCod = apiInterface.enviarConfirCodigo(id, codigoConfEmail);
        enviarCod.enqueue(new Callback<CodConfirmacaoResult>() {
            @Override
            public void onResponse(@NonNull Call<CodConfirmacaoResult> call, @NonNull Response<CodConfirmacaoResult> response) {
                if (!response.isSuccessful()) {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    mostrarMensagem(MediaRumoActivity.this, errorResponce.getError());
                    progressDialog.dismiss();
                } else {
                    if (response.body() != null) {
                        token = response.body().getToken();
                        dialogSenhaEnviarEmailCodReset.cancel();
                        dialogSenhaEnviarEmailSenhaNova.show();
                        limparPinView(pinCodigoConfirmacaoEmail);
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CodConfirmacaoResult> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });

    }

    private void enviarCodRedifinicaoTelef() {
        Log.i(TAG, "codConfi" + codigoConfTelef);
        progressDialog.setMessage(msgEnviandoCodigo);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<CodConfirmacaoResult> enviarCod = apiInterface.enviarConfirCodigo(idTelef, codigoConfTelef);
        enviarCod.enqueue(new Callback<CodConfirmacaoResult>() {
            @Override
            public void onResponse(@NonNull Call<CodConfirmacaoResult> call, @NonNull Response<CodConfirmacaoResult> response) {
                if (!response.isSuccessful()) {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    mostrarMensagem(MediaRumoActivity.this, errorResponce.getError());
                    progressDialog.cancel();
                } else {
                    if (response.body() != null) {
                        token = response.body().getToken();
                        dialogSenhaEnviarTelefoneCodReset.cancel();
                        dialogSenhaEnviarEmailSenhaNova.show();
                        limparPinView(pinCodigoConfirmacaoTelef);
                    }
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(@NonNull Call<CodConfirmacaoResult> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });

    }

    private void limparPinView(Pinview pinCodigoConfirmacaoTelef) {
        try {
            for (int i = 0;i < pinCodigoConfirmacaoTelef.getPinLength();i++) {
                pinCodigoConfirmacaoTelef.onKey(pinCodigoConfirmacaoTelef.getFocusedChild(), KeyEvent.KEYCODE_DEL, new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DEL));
            }
        }catch (Exception e){
            Log.i(TAG,"falha limInput" + e.getMessage());
        }
    }

    private void enviarEmailRedif() {
        if (verificarEmail()) {
            mandarEmailResetSenha(emailRedif_senha);
        }
    }

    private void enviarTelefonelRedif() {
        if (verificarTelefone()) {
            mandarTelefoneResetSenha(telefoneRedif_senha);
        }
    }

    private boolean verificarTelefone() {

        if (dialog_editTelefone_telefone.getText() != null) {
            telefoneRedif_senha = dialog_editTelefone_telefone.getText().toString().trim();
            if (!telefoneRedif_senha.matches("9[1-9][1-9]\\d{6}")) {
                dialog_editTelefone_telefone.setError(msgErroTelefone);
                return false;
            }else {
                return true;
            }
        }else {
            dialog_editTelefone_telefone.setError(msgErroSTelefone);
            return false;
        }
    }

    private void mandarEmailResetSenha(String email) {
        dialog_editEmail_email.setError(null);
        emailReceberDeNovo = email;
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<RecuperarSenha> enviarEmailReset = apiInterface.enviarEmail(email);
        progressDialog.setMessage(msgAEnviarEmail);
        progressDialog.show();
        enviarEmailReset.enqueue(new Callback<RecuperarSenha>() {
            @Override
            public void onResponse(@NonNull Call<RecuperarSenha> call, @NonNull Response<RecuperarSenha> response) {
                if (response.isSuccessful() && response.body() != null) {
                    id = response.body().getId();
                    progressDialog.cancel();
                    dialog_editEmail_email.setText(null);
                    dialogOpcaoSenhaEnviarEmail.cancel();
                    tv_email.setText(msgSupporte.concat(email));
                    dialogSenhaEnviarEmailCodReset.show();
                } else {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    dialog_editEmail_email.setError(errorResponce.getError());
                    progressDialog.cancel();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecuperarSenha> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MediaRumoActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
            }
        });
    }

    private void mandarTelefoneResetSenha(String telefone) {
        dialog_editTelefone_telefone.setError(null);
        telefoneReceberDeNovo = telefone;
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<RecuperarSenha> enviarTelefoneReset = apiInterface.enviarTelefone(telefone);
        progressDialog.setMessage(msgAEnviarTelefone);
        progressDialog.show();
        enviarTelefoneReset.enqueue(new Callback<RecuperarSenha>() {
            @Override
            public void onResponse(@NonNull Call<RecuperarSenha> call, @NonNull Response<RecuperarSenha> response) {
                if (response.isSuccessful() && response.body() != null) {
                    idTelef = response.body().getId();
                    progressDialog.cancel();
                    dialog_editTelefone_telefone.setText(null);
                    dialogOpcaoSenhaEnviarTelefone.cancel();
                    tv_telefone.setText(msgSupporte.concat(telefone));
                    dialogSenhaEnviarTelefoneCodReset.show();
                } else {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    progressDialog.dismiss();
                    dialog_editTelefone_telefone.setError(errorResponce.getError());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecuperarSenha> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(MediaRumoActivity.this)){
                    mostrarMensagem(MediaRumoActivity.this,R.string.txtMsg);
                }else  if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MediaRumoActivity.this,R.string.txtTimeout);
                }else {
                    mostrarMensagem(MediaRumoActivity.this,R.string.txtProblemaMsg);
                }
                Log.i(TAG,"onFailure" + t.getMessage());
            }
        });
    }

    private Boolean verificarEmail() {

        if (dialog_editEmail_email.getText() != null)
            emailRedif_senha = dialog_editEmail_email.getText().toString().trim();

        if (!MetodosComuns.validarEmail(emailRedif_senha)) {
            dialog_editEmail_email.requestFocus();
            dialog_editEmail_email.setError(msgErroSEmail);
            return false;
        }

        return true;
    }

    private void cancelarEnvioEmail() {
        dialog_editEmail_email.setText(null);
        dialog_editEmail_email.setError(null);
        dialogOpcaoSenhaEnviarEmail.dismiss();
        dialogOpcaoSenhaEnviarEmail.cancel();
    }

    private void cancelarEnvioTelefone() {
        dialog_editTelefone_telefone.setText(null);
        dialog_editTelefone_telefone.setError(null);
        dialogOpcaoSenhaEnviarTelefone.dismiss();
        dialogOpcaoSenhaEnviarTelefone.cancel();
    }

    private void inserirTelefRedif() {
        dialogOpcaoSenha.dismiss();
        dialogOpcaoSenhaEnviarTelefone.show();
    }

    private void inserirEmailRedif() {
        dialogOpcaoSenha.dismiss();
        dialogOpcaoSenhaEnviarEmail.show();
    }

    private void autenticacaoLoginApi() {

        if (verificarCampos()) {

            progressDialog.setMessage(msgVerificando);
            progressDialog.show();

            ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
            retrofit2.Call<Data> call = apiInterface.autenticarCliente(editTextEmailLogin.getText().toString(), editTextPasslLogin.getText().toString());
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Data> call, @NonNull Response<Data> response) {

                    //response.body()==null
                    if (response.isSuccessful() && response.body() != null) {
                        data = response.body();
                        retrofit2.Call<DataUserApi> callApiDados = apiInterface.getUsuarioDados(data.getEmSessao().getId());
                        callApiDados.enqueue(new Callback<DataUserApi>() {
                            @Override
                            public void onResponse(@NonNull Call<DataUserApi> call, @NonNull Response<DataUserApi> response) {
                                if (response.isSuccessful()) {
                                    progressDialog.dismiss();
                                    dataUserApi = response.body();

                                    Common.mCurrentUser = new Usuario();

                                    if (dataUserApi.getDataDados().getId_utilizador() != null)
                                        Common.mCurrentUser.setId_utilizador(dataUserApi.getDataDados().getId_utilizador());

                                    if (dataUserApi.getDataDados().getNomeCliente() != null)
                                        Common.mCurrentUser.setNomeCliente(dataUserApi.getDataDados().getNomeCliente());

                                    if (dataUserApi.getDataDados().getEmail() != null)
                                        Common.mCurrentUser.setEmail(dataUserApi.getDataDados().getEmail());

                                    if (dataUserApi.getDataDados().getFoto() != null)
                                        Common.mCurrentUser.setFoto(dataUserApi.getDataDados().getFoto());

                                    if (dataUserApi.getDataDados().getSexo() != null)
                                        Common.mCurrentUser.setSexo(dataUserApi.getDataDados().getSexo());

                                    if (dataUserApi.getDataDados().getTelefone() != null)
                                        Common.mCurrentUser.setTelefone(dataUserApi.getDataDados().getTelefone());

                                    if (dataUserApi.getDataDados().getDataNascimento() != null) {
                                        String resultado = dataUserApi.getDataDados().getDataNascimento();
                                        String[] partes = resultado.split("-");
                                        String ano = partes[0];
                                        String mes = partes[1];
                                        String dia = partes[2];
                                        Common.mCurrentUser.setDataNascimento(ano + "-" + mes + "-" + dia.substring(0, 2));
                                    }

                                    if (dataUserApi.getDataDados().getProvincia() != null)
                                        Common.mCurrentUser.setProvincia(dataUserApi.getDataDados().getProvincia());

                                    if (dataUserApi.getDataDados().getMunicipio() != null)
                                        Common.mCurrentUser.setMunicipio(dataUserApi.getDataDados().getMunicipio());

                                    if (dataUserApi.getDataDados().getRua() != null)
                                        Common.mCurrentUser.setRua(dataUserApi.getDataDados().getRua());


                                    AppDatabase.getInstance().saveUser(Common.mCurrentUser);
                                    AppDatabase.getInstance().saveAuthToken(Common.mCurrentUser.getId_utilizador());

                                    if (dataUserApi.getDataDados().getSexo() == null ||
                                            dataUserApi.getDataDados().getTelefone() == null ||
                                            dataUserApi.getDataDados().getDataNascimento() == null ||
                                            dataUserApi.getDataDados().getProvincia() == null ||
                                            dataUserApi.getDataDados().getMunicipio() == null ||
                                            dataUserApi.getDataDados().getRua() == null) {

                                        Intent intent = new Intent(MediaRumoActivity.this, MeuPerfilActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(MediaRumoActivity.this, "Por favor termina de editar o perfil.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        launchHomeScreen();
                                    }
                                } else {
                                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                                    progressDialog.dismiss();
                                    try {
                                        Snackbar
                                                .make(raiz, errorResponce.getError(), 4000)
                                                .setActionTextColor(Color.MAGENTA)
                                                .show();
                                    } catch (Exception e) {
                                        Log.d("autenticacaoVerif", String.valueOf(e.getMessage()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DataUserApi> call, @NonNull Throwable t) {
                                progressDialog.dismiss();
                                if (!conexaoInternetTrafego(MediaRumoActivity.this)){
                                    mostrarMensagem(MediaRumoActivity.this,R.string.txtMsg);
                                }else  if ("timeout".equals(t.getMessage())) {
                                    mostrarMensagem(MediaRumoActivity.this,R.string.txtTimeout);
                                }else {
                                    mostrarMensagem(MediaRumoActivity.this,R.string.txtProblemaMsg);
                                }
                                Log.i(TAG,"onFailure" + t.getMessage());
                            }
                        });
                    } else {
                        ErrorResponce errorResponce = ErrorUtils.parseError(response);
                        progressDialog.dismiss();

                        try {
                            Snackbar
                                    .make(raiz, errorResponce.getError(), 4000)
                                    .setActionTextColor(Color.MAGENTA)
                                    .show();
                        } catch (Exception e) {
                            Log.d(TAG, String.valueOf(e.getMessage()));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Data> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    if (!conexaoInternetTrafego(MediaRumoActivity.this)){
                        mostrarMensagem(MediaRumoActivity.this,R.string.txtMsg);
                    }else  if ("timeout".equals(t.getMessage())) {
                        mostrarMensagem(MediaRumoActivity.this,R.string.txtTimeout);
                    }else {
                        mostrarMensagem(MediaRumoActivity.this,R.string.txtProblemaMsg);
                    }
                    Log.i(TAG,"onFailure" + t.getMessage());
                }
            });
        }

    }

    private boolean verificarCampos() {

        String email = editTextEmailLogin.getText().toString().trim();
        String palavraPass = editTextPasslLogin.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmailLogin.requestFocus();
            editTextEmailLogin.setError(msgErro);
            return false;
        }

        if (!MetodosComuns.validarEmail(email)) {
            editTextEmailLogin.requestFocus();
            editTextEmailLogin.setError(msgErroSEmail);
            return false;
        }

        if (palavraPass.isEmpty()) {
            editTextPasslLogin.requestFocus();
            editTextPasslLogin.setError(msgErro);
            return false;
        }

        editTextEmailLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);

        return true;
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(MediaRumoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void verifConecxao() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            mostrarMensagem(MediaRumoActivity.this,R.string.txtMsg);
        }
    }
}