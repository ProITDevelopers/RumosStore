package proitappsolutions.com.rumosstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.text.Normalizer;

import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorResponce;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorUtils;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.modelo.UsuarioApi;
import proitappsolutions.com.rumosstore.modelo.UsuarioFire;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.conexaoInternetTrafego;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErro;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErroLetras;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErroSenha;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErroSenhaDiferente;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgQuasePronto;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.removeAcentos;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "RegistroActivityDebug";
    private EditText editTextNomeRegistro,editTextEmailRegistro;
    private ShowHidePasswordEditText editTextPassRegistro,editTextPassRegistro2;
    private ProgressDialog progressDialog;
    private RelativeLayout errorLayout;
    private RelativeLayout relLativeLayout;
    private TextView btnTentarDeNovo;
    View raiz;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        errorLayout = findViewById(R.id.erroLayout);
        relLativeLayout = findViewById(R.id.relLativeLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText(R.string.txtVoltar);
        editTextNomeRegistro = findViewById(R.id.editTextNomeRegistro);
        editTextEmailRegistro = findViewById(R.id.editTextEmailRegistro);
        editTextPassRegistro = findViewById(R.id.editTextPassRegistro);
        editTextPassRegistro2 = findViewById(R.id.editTextPassRegistro2);
        editTextNomeRegistro = findViewById(R.id.editTextNomeRegistro);
        raiz = findViewById(android.R.id.content);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnLoginInicial = findViewById(R.id.btnLoginInicial);

        btnRegistrar.setOnClickListener(RegistroActivity.this);
        btnLoginInicial.setOnClickListener(RegistroActivity.this);

        progressDialog = new ProgressDialog(RegistroActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Userss");

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
                startActivity(intentEntrar);
                finish();
                break;

        }

    }

    private boolean verificarCampo() {

        String nome = editTextNomeRegistro.getText().toString().trim();
        String email = editTextEmailRegistro.getText().toString().trim();
        String senha = editTextPassRegistro.getText().toString().trim();
        String senhaConf = editTextPassRegistro2.getText().toString().trim();

        try {
            nome = removeAcentos(nome);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (nome.isEmpty()){
            editTextNomeRegistro.setError(msgErro);
            return false;
        }

        if (!nome.matches("^[a-zA-Z\\s]+$")){
            editTextNomeRegistro.setError(msgErroLetras);
            return false;
        }

        if (email.isEmpty()){
            editTextEmailRegistro.setError(msgErro);
            return false;
        }

        if (!MetodosComuns.validarEmail(email)){
            editTextEmailRegistro.setError(msgErro);
            return false;
        }

        if (senha.isEmpty()){
            editTextPassRegistro.setError(msgErro);
            return false;
        }

        if (senha.length()<5){
            editTextPassRegistro.setError(msgErroSenha);
            return false;
        }

        if (senhaConf.length()<5){
            editTextPassRegistro2.setError(msgErroSenha);
            return false;
        }

        if (!senha.equals(senhaConf)){
            editTextPassRegistro2.setError(msgErroSenhaDiferente);
            return false;
        }
        return true;
    }

    private void registrarUsuario(){

        errorLayout.setVisibility(View.GONE);
        progressDialog.setMessage(msgQuasePronto);
        progressDialog.show();

        UsuarioApi usuarioApi = new UsuarioApi(editTextNomeRegistro.getText().toString().trim(),editTextEmailRegistro.getText().toString().trim()
                ,editTextPassRegistro.getText().toString().trim());
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> call = apiInterface.registrarCliente(usuarioApi);

       call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    String nome = editTextNomeRegistro.getText().toString();
                    String nomeValor;
                    nomeValor = nome.replace(" ","_");
                    UsuarioFire usuarioFire = new UsuarioFire(nomeValor
                            ,editTextEmailRegistro.getText().toString().trim());
                    users.child(nome.toUpperCase())
                            .setValue(usuarioFire).addOnCompleteListener(task -> {
                                Toast.makeText(RegistroActivity.this,R.string.txtEfetuadoregistro,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intentEntrar = new Intent(RegistroActivity.this,MediaRumoActivity.class);
                                intentEntrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentEntrar);
                            });
                } else {
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    progressDialog.dismiss();

                    try {
                        Snackbar
                                .make(raiz, errorResponce.getError(), 4000)
                                .setActionTextColor(Color.MAGENTA)
                                .show();
                    }catch (Exception e){
                        Log.i(TAG,"autenticacaoVerif snakBar" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                verifConecxao();
                progressDialog.dismiss();
                if (!conexaoInternetTrafego(RegistroActivity.this)){
                    mostrarMensagem(RegistroActivity.this,R.string.txtMsg);
                }else  if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(RegistroActivity.this,R.string.txtTimeout);
                }else {
                    mostrarMensagem(RegistroActivity.this,R.string.txtProblemaMsg);
                }
                Log.i(TAG,"onFailure" + t.getMessage());

            }
        });

    }

    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
                mostarMsnErro();
        }
    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            relLativeLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(view -> {
            relLativeLayout.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        });
    }

}