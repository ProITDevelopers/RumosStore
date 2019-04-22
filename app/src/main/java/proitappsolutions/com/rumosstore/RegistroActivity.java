package proitappsolutions.com.rumosstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorResponce;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorUtils;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.modelo.Erro;
import proitappsolutions.com.rumosstore.modelo.UsuarioApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNomeRegistro,editTextEmailRegistro,editTextPassRegistro,editTextPassRegistro2;
    private Button btnRegistrar,btnLoginInicial;
    private String nome,email,senha,senhaConf;
    private ProgressDialog progressDialog;
    private RelativeLayout linearLayout;
    private RelativeLayout errorLayout;
    private TextView btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextNomeRegistro = findViewById(R.id.editTextNomeRegistro);
        editTextEmailRegistro = findViewById(R.id.editTextEmailRegistro);
        editTextPassRegistro = findViewById(R.id.editTextPassRegistro);
        editTextPassRegistro2 = findViewById(R.id.editTextPassRegistro2);

        linearLayout = findViewById(R.id.linearLayout);
        errorLayout = findViewById(R.id.erroLayout);
        btnRetry = findViewById(R.id.btnRecarregar);
        btnRetry.setText("Voltar");

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnLoginInicial = findViewById(R.id.btnLoginInicial);

        btnRegistrar.setOnClickListener(RegistroActivity.this);
        btnLoginInicial.setOnClickListener(RegistroActivity.this);

        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnRegistrar:
                if (verificarCampo()){
                    registrarUsuario();
                }
                break;

            case R.id.btnLoginInicial:
                Intent intentEntrar = new Intent(RegistroActivity.this,MediaRumoActivity.class);
                intentEntrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentEntrar);
                break;

        }

    }

    private boolean verificarCampo() {

        nome = editTextNomeRegistro.getText().toString();
        email = editTextEmailRegistro.getText().toString().trim();
        senha = editTextPassRegistro.getText().toString().trim();
        senhaConf = editTextPassRegistro2.getText().toString().trim();



        if (nome.isEmpty()){
            editTextNomeRegistro.setError("Preencha o campo.");
            return false;
        }

        if (!nome.matches("[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+")){
            editTextNomeRegistro.requestFocus();
            editTextNomeRegistro.setError("Preencha o campo com letras apenas");
            return false;
        }

        if (email.isEmpty()){
            editTextEmailRegistro.setError("Preencha o campo.");
            return false;
        }

        if (!MetodosComuns.validarEmail(email)) {
            editTextEmailRegistro.requestFocus();
            editTextEmailRegistro.setError("Preencha o campo com um email.");
            return false;
        }

        if (senha.isEmpty()){
            editTextPassRegistro.setError("Preencha o campo.");
            return false;
        }

        if (senha.length()<5){
            editTextPassRegistro.setError("Senha fraca..");
            return false;
        }

        if (senhaConf.length()<5){
            editTextPassRegistro2.setError("Senha fraca..");
            return false;
        }

        if (!senha.equals(senhaConf)){
            editTextPassRegistro2.setError("As senhas devem ser iguais.");
            return false;
        }

        return true;

    }

    private void registrarUsuario(){

        errorLayout.setVisibility(View.GONE);
        progressDialog.setMessage("Quase Pronto...!");
        progressDialog.show();

        UsuarioApi usuarioApi = new UsuarioApi(editTextNomeRegistro.getText().toString(),email = editTextEmailRegistro.getText().toString()
                ,editTextPassRegistro.getText().toString());

        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> call = apiInterface.registrarCliente(usuarioApi);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (!response.isSuccessful()){

                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    Toast.makeText(RegistroActivity.this, errorResponce.getError(),
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(RegistroActivity.this,"Registro efetuado com sucesso",Toast.LENGTH_SHORT).show();
                    Intent intentEntrar = new Intent(RegistroActivity.this,MediaRumoActivity.class);
                    intentEntrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentEntrar);
                    Log.i("verificacaoCerta","certo" + response.code());
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(RegistroActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RegistroActivity.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                Log.d("autenticacaoVerifFalhoy",t.getMessage());
            }
        });

    }

    private void verifConecxao(){
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
            linearLayout.setVisibility(View.GONE);
        }

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }
        });
    }

}
