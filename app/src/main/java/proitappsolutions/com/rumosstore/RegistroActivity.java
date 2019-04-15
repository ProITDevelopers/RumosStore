package proitappsolutions.com.rumosstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
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
    private Erro erro = new Erro();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextNomeRegistro = findViewById(R.id.editTextNomeRegistro);
        editTextEmailRegistro = findViewById(R.id.editTextEmailRegistro);
        editTextPassRegistro = findViewById(R.id.editTextPassRegistro);
        editTextPassRegistro2 = findViewById(R.id.editTextPassRegistro2);
        editTextNomeRegistro = findViewById(R.id.editTextNomeRegistro);

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
                    if (Common.isConnected(10000))
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
        email = editTextEmailRegistro.getText().toString();
        senha = editTextPassRegistro.getText().toString();
        senhaConf = editTextPassRegistro2.getText().toString();



        if (nome.isEmpty()){
            //editTextNomeRegistro.setErrorEnabled(true);
            editTextNomeRegistro.setError("Preencha o campo.");
            return false;
        }

        if (email.isEmpty()){
            //editTextEmailRegistro.setErrorEnabled(true);
            editTextEmailRegistro.setError("Preencha o campo.");
            return false;
        }

        if (senha.isEmpty()){
            //editTextEmailRegistro.setErrorEnabled(true);
            editTextPassRegistro.setError("Preencha o campo.");
            return false;
        }

        if (!senha.equals(senhaConf)){
            //editTextEmailRegistro.setErrorEnabled(true);
            editTextPassRegistro2.setError("As senhas devem ser iguais.");
            return false;
        }

        return true;

    }

    private void registrarUsuario(){

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

                    Toast.makeText(RegistroActivity.this,"Email já existe.", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i("verificacao","certo" + response.errorBody().string());
                        //erro = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(RegistroActivity.this,"Registro efetuado com sucesso",Toast.LENGTH_SHORT).show();
                    Intent intentEntrar = new Intent(RegistroActivity.this,MediaRumoActivity.class);
                    intentEntrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentEntrar);
                    Log.i("verificacao","certo" + response.code());
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("onFailure","erro" + t.getMessage());
                Toast.makeText(RegistroActivity.this,"Algum problema aconteceu.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}
