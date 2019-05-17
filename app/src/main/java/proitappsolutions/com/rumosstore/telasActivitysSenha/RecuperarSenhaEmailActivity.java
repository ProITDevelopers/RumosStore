package proitappsolutions.com.rumosstore.telasActivitysSenha;

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

import java.io.IOException;

import okhttp3.ResponseBody;
import proitappsolutions.com.rumosstore.MediaRumoActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.modelo.RecuperarSenha;
import proitappsolutions.com.rumosstore.modelo.UsuarioApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.esconderTeclado;

public class RecuperarSenhaEmailActivity extends AppCompatActivity {

    private String TAG = "RecuperarSenhaEmailActivity" ;
    private AppCompatEditText editEmail;
    private Button btn_email_redif;
    private String email;
    private ProgressDialog progressDialog;
    private RelativeLayout errorLayout;
    private RelativeLayout relativeLayout;
    private TextView btnTentarDeNovo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha_email);

        android.support.v7.widget.Toolbar toolbar_redif_senha = findViewById(R.id.toolbar_redif_senha_email);
        toolbar_redif_senha.setTitle("");
        setSupportActionBar(toolbar_redif_senha);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editEmail = findViewById(R.id.editEmail);
        btn_email_redif = findViewById(R.id.btn_email_redif);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        errorLayout = findViewById(R.id.erroLayout);
        relativeLayout = findViewById(R.id.relativeLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText("Voltar");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

        btn_email_redif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarEmail()){
                    mandarEmailResetSenha();
                }
            }
        });
    }

    private void mandarEmailResetSenha() {

        errorLayout.setVisibility(View.GONE);

        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<RecuperarSenha> enviarEmailReset = apiInterface.enviarEmail(email);
        progressDialog.setMessage("A enviar o e-mail..");
        progressDialog.show();
        enviarEmailReset.enqueue(new Callback<RecuperarSenha>() {
            @Override
            public void onResponse(Call<RecuperarSenha> call, Response<RecuperarSenha> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(RecuperarSenhaEmailActivity.this,EnviarCodConfActivity.class);
                    intent.putExtra("email",email);
                    try {
                        intent.putExtra("id",response.body().getId());
                    }catch (Exception e){
                        Log.i(TAG,e.getMessage());
                    }
                    startActivity(intent);
                    finish();
                    Log.i("skansaksas",response.body().getId() + "sucess");
                }else {
                    editEmail.setError("E-mail não encontrado.");
                        Log.i("skansaksas",response.code() + "error");
                        Log.i("skansaksas",response.code() + email);
                    try {
                        Log.i("skansaksas",response.errorBody().string() + email);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RecuperarSenha> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("skansaksas",t.getMessage() + "failed");
                verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(RecuperarSenhaEmailActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RecuperarSenhaEmailActivity.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            esconderTeclado(RecuperarSenhaEmailActivity.this);
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

    private Boolean verificarEmail(){

        if (editEmail.getText() != null)
        email = editEmail.getText().toString().trim();

        if (!MetodosComuns.validarEmail(email)){
            editEmail.requestFocus();
            editEmail.setError("Preencha o campo com um email.");
            return false;
        }

        return true;
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
}