package proitappsolutions.com.rumosstore.telasActivitysSenha;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.io.IOException;

import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.AppPref;
import proitappsolutions.com.rumosstore.MainActivity;
import proitappsolutions.com.rumosstore.MediaRumoActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.telasActivity.HomeInicialActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtualizarSenhaSenhaActivity extends AppCompatActivity implements View.OnClickListener {

    private ShowHidePasswordEditText edtSenhaNova,edtsenhaAntiga;
    private String senha1,senha2;
    private ProgressDialog progressDialog;
    private RelativeLayout errorLayout;
    private RelativeLayout relativeLayout;
    private TextView btnTentarDeNovo;
    private Button btn_cancelar,btnSim,btnNao,btnCancelar_dialog;
    private Dialog caixa_dialogo_cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_senha);

        android.support.v7.widget.Toolbar toolbar_redif_senha = findViewById(R.id.toolbar_redif_senha_email);
        toolbar_redif_senha.setTitle("");
        setSupportActionBar(toolbar_redif_senha);
        if (getSupportActionBar() != null)
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_redif_senha = findViewById(R.id.btn_redif_senha);
        edtSenhaNova = findViewById(R.id.edtSenhaNova);
        btn_cancelar = findViewById(R.id.btn_cancelar);
        edtsenhaAntiga = findViewById(R.id.edtSenhaAntiga);
        progressDialog = new ProgressDialog(AtualizarSenhaSenhaActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);
        errorLayout = findViewById(R.id.erroLayout);
        relativeLayout = findViewById(R.id.relativeLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText("Voltar");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

        caixa_dialogo_cancelar = new Dialog(AtualizarSenhaSenhaActivity.this);
        caixa_dialogo_cancelar.setContentView(R.layout.caixa_de_dialogo_redif_senha);
        caixa_dialogo_cancelar.setCancelable(false);

        btnCancelar_dialog = caixa_dialogo_cancelar.findViewById(R.id.btnCancelar_dialog);
        btnSim = caixa_dialogo_cancelar.findViewById(R.id.btnSim);
        btnNao = caixa_dialogo_cancelar.findViewById(R.id.btnNao);
        btnCancelar_dialog.setOnClickListener(AtualizarSenhaSenhaActivity.this);
        btnSim.setOnClickListener(AtualizarSenhaSenhaActivity.this);
        btnNao.setOnClickListener(AtualizarSenhaSenhaActivity.this);

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
                caixa_dialogo_cancelar.show();
            }
        });
        
    }


    private void salvarSenhaNova() {

        //enviarNovaSenha
        errorLayout.setVisibility(View.GONE);
        progressDialog.setMessage("A processar...!");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> enviarSenhaNova = apiInterface.atualizarSenha(Integer.parseInt(AppDatabase.getUser().getId_utilizador()),senha1,senha2); //AQUI

        enviarSenhaNova.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(AtualizarSenhaSenhaActivity.this,"A sua senha foi alterada com sucesso.!",Toast.LENGTH_SHORT).show();
                    logOut();
                }else {
                    progressDialog.dismiss();
                    try {
                        Log.i("erroCod",response.errorBody().string());
                        Log.i("erroCod",response.code() + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AtualizarSenhaSenhaActivity.this,"Algum problema inesperado ocorreu.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("skansaksas",t.getMessage() + "failed");
                verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(AtualizarSenhaSenhaActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(AtualizarSenhaSenhaActivity.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private boolean verificarCampo() {
        
        senha1 = edtSenhaNova.getText().toString().trim();
        senha2 = edtsenhaAntiga.getText().toString().trim();

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
            edtsenhaAntiga.setError("Preencha o campo.");
            return false;
        }

        if (senha2.length() < 5){
            edtsenhaAntiga.setError("Senha fraca. Precisa de ter mais de 6 caracteres.");
            edtsenhaAntiga.requestFocus();
            return false;
        }

        if (senha1.equals(senha2)){
            edtsenhaAntiga.setError("Os campos devem ser diferentes.");
            edtsenhaAntiga.requestFocus();
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

    private void logOut(){

        AppDatabase.clearData();
        AppPref.getInstance().clearData();

        Intent intent = new Intent(AtualizarSenhaSenhaActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        //configurar onClick
        switch (view.getId()){
            case R.id.btnSim:
                Intent intent = new Intent(AtualizarSenhaSenhaActivity.this,HomeInicialActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btnNao:
                caixa_dialogo_cancelar.dismiss();
                break;
            case R.id.btnCancelar_dialog:
                caixa_dialogo_cancelar.dismiss();
                break;
        }
    }
}
