package proitappsolutions.com.rumosstore.telasActivitysSenha;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.io.IOException;

import proitappsolutions.com.rumosstore.MediaRumoActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.modelo.CodConfirmacaoResult;
import proitappsolutions.com.rumosstore.telasActivity.MeuPerfilActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedifinirSenhaActivity extends AppCompatActivity implements View.OnClickListener {

    private ShowHidePasswordEditText edtSenhaNova,edtConfSenha;
    private String senha1,senha2,token;
    private ProgressDialog progressDialog;
    private RelativeLayout errorLayout;
    private RelativeLayout relativeLayout;
    private TextView btnTentarDeNovo;
    private Button btn_cancelar,btnSim,btnNao,btnCancelar_dialog;
    private Dialog caixa_dialogo_cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redifinir_senha);

        android.support.v7.widget.Toolbar toolbar_redif_senha = findViewById(R.id.toolbar_redif_senha_email);
        toolbar_redif_senha.setTitle("");
        setSupportActionBar(toolbar_redif_senha);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Button btn_redif_senha = findViewById(R.id.btn_redif_senha);
        edtSenhaNova = findViewById(R.id.edtSenhaNova);
        btn_cancelar = findViewById(R.id.btn_cancelar);
        edtConfSenha = findViewById(R.id.edtConfSenha);
        progressDialog = new ProgressDialog(RedifinirSenhaActivity.this);
        progressDialog.setCancelable(false);
        errorLayout = findViewById(R.id.erroLayout);
        relativeLayout = findViewById(R.id.relativeLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText("Voltar");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

        caixa_dialogo_cancelar = new Dialog(RedifinirSenhaActivity.this);
        caixa_dialogo_cancelar.setContentView(R.layout.caixa_de_dialogo_redif_senha);
        caixa_dialogo_cancelar.setCancelable(false);

        btnCancelar_dialog = caixa_dialogo_cancelar.findViewById(R.id.btnCancelar_dialog);
        btnSim.setOnClickListener(RedifinirSenhaActivity.this);
        btnNao.setOnClickListener(RedifinirSenhaActivity.this);

        btn_redif_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampo())
                    salvarSenhaNova();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caixaDeDialogo();
            }
        });
        
    }

    private void caixaDeDialogo() {



    }

    private void salvarSenhaNova() {

        //enviarNovaSenha
        errorLayout.setVisibility(View.GONE);
        progressDialog.setMessage("A processar...!");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> enviarSenhaNova = apiInterface.enviarNovaSenha("Bearer "+token,senha1);

        enviarSenhaNova.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(RedifinirSenhaActivity.this,MediaRumoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RedifinirSenhaActivity.this,"A sua senha foi alterada com sucesso.!",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    try {
                        Log.i("erroCod",token);
                        Log.i("erroCod",response.errorBody().string());
                        Log.i("erroCod",response.code() + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RedifinirSenhaActivity.this,"Algum problema inesperado ocorreu.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("skansaksas",t.getMessage() + "failed");
                verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(RedifinirSenhaActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RedifinirSenhaActivity.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private boolean verificarCampo() {
        
        senha1 = edtSenhaNova.getText().toString().trim();
        senha2 = edtConfSenha.getText().toString().trim();

        if (senha1.isEmpty()){
            edtSenhaNova.setError("Preencha o campo.");
            return false;
        }

        if (senha1.length() <5){
            edtSenhaNova.setError("Senha fraca. Precisa de ter mais de 6 caracteres.");
            edtSenhaNova.requestFocus();
            return false;
        }

        if (senha2.isEmpty()){
            edtConfSenha.setError("Preencha o campo.");
            return false;
        }

        if (senha2.length() < 5){
            edtConfSenha.setError("Senha fraca. Precisa de ter mais de 6 caracteres.");
            edtConfSenha.requestFocus();
            return false;
        }

        if (!senha1.equals(senha2)){
            edtConfSenha.setError("Os campos devem ser iguais.");
            edtConfSenha.requestFocus();
            return false;
        }

        return true;

    }

    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            mostarMsnErro();
        }else{
        }
    }


    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }


    @Override
    public void onClick(View view) {
        //configurar onClick
    }
}
