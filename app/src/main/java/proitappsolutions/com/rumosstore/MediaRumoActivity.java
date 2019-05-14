package proitappsolutions.com.rumosstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.modelo.Autenticacao;
import proitappsolutions.com.rumosstore.modelo.Data;
import proitappsolutions.com.rumosstore.modelo.DataUserApi;
import proitappsolutions.com.rumosstore.modelo.EmSessao;
import proitappsolutions.com.rumosstore.telasActivity.MeuPerfilActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaRumoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailLogin;
//    private EditText editTextPasslLogin;
    private ShowHidePasswordEditText editTextPasslLogin;
    private Button btnEntrar,btnRegistrate;

    private ProgressDialog progressDialog;
    private RelativeLayout errorLayout;
    private RelativeLayout relativeLayout;
    private TextView btnTentarDeNovo;
    public Data data = new Data();
    public DataUserApi dataUserApi  = new DataUserApi();
    public EmSessao emSessao = new EmSessao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.statuscolor));
        setContentView(R.layout.activity_media_rumo);

        inicializar();


        verifConecxao();
    }

    private void inicializar() {

        editTextEmailLogin = findViewById(R.id.editTextEmaiLogin);
        editTextPasslLogin= findViewById(R.id.editTextPasslLogin);
        btnEntrar= findViewById(R.id.btnEntrar);
        btnRegistrate = findViewById(R.id.btnRegistrate);
        btnEntrar.setOnClickListener(MediaRumoActivity.this);
        btnRegistrate.setOnClickListener(MediaRumoActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);



        errorLayout = findViewById(R.id.erroLayout);
        relativeLayout = findViewById(R.id.relativeLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText("Voltar");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnEntrar:
                    autenticacaoLoginApi();
                break;

            case R.id.btnRegistrate:
                Intent intentRegistrar = new Intent(MediaRumoActivity.this,RegistroActivity.class);
                startActivity(intentRegistrar);
                break;

        }

    }

    private void autenticacaoLoginApi() {

        errorLayout.setVisibility(View.GONE);

        if (verificarCampos()){

        progressDialog.setMessage("Verificando...");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        retrofit2.Call<Data> call = apiInterface.autenticarCliente(editTextEmailLogin.getText().toString(),editTextPasslLogin.getText().toString());
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(retrofit2.Call<Data> call, Response<Data> response) {

                //response.body()==null
                if (response.isSuccessful()){
                    data = response.body();
                    /*Log.d("autenticacaoVerif",data.getEmSessao().getId());
                    Log.d("autenticacaoVerif",data.getEmSessao().getNome());
                    Log.d("autenticacaoVerif",data.getEmSessao().getEmail());*/

                    retrofit2.Call<DataUserApi> callApiDados = apiInterface.getUsuarioDados(data.getEmSessao().getId());

                    callApiDados.enqueue(new Callback<DataUserApi>() {
                        @Override
                        public void onResponse(Call<DataUserApi> call, Response<DataUserApi> response) {
                            if (response.isSuccessful()){
                                progressDialog.dismiss();
                                dataUserApi = response.body();
                                Usuario usuario = new Usuario();

                                if (dataUserApi.getDataDados().getId_utilizador() != null )
                                    usuario.setId_utilizador(dataUserApi.getDataDados().getId_utilizador());

                                if (dataUserApi.getDataDados().getNomeCliente() != null )
                                    usuario.setNomeCliente(dataUserApi.getDataDados().getNomeCliente());

                                if (dataUserApi.getDataDados().getEmail() != null )
                                    usuario.setEmail(dataUserApi.getDataDados().getEmail());

                                if (dataUserApi.getDataDados().getFoto() != null )
                                    usuario.setFoto(dataUserApi.getDataDados().getFoto());

                                if (dataUserApi.getDataDados().getSexo() != null )
                                    usuario.setSexo(dataUserApi.getDataDados().getSexo());

                                if (dataUserApi.getDataDados().getTelefone() != null )
                                    usuario.setTelefone(dataUserApi.getDataDados().getTelefone());

                                String resultado = dataUserApi.getDataDados().getDataNascimento();
                                String[] partes = resultado.split("-");
                                String ano = partes[0];
                                String mes = partes[1];
                                String dia = partes[2];
                                Log.i("snaksnas", ano + mes + dia);
                                if (dataUserApi.getDataDados().getDataNascimento() != null )
                                    usuario.setDataNascimento(ano+"-"+mes+"-"+dia.substring(0,2));

                                if (dataUserApi.getDataDados().getProvincia() != null )
                                    usuario.setProvincia(dataUserApi.getDataDados().getProvincia());

                                if (dataUserApi.getDataDados().getMunicipio() != null )
                                    usuario.setMunicipio(dataUserApi.getDataDados().getMunicipio());

                                if (dataUserApi.getDataDados().getRua() != null )
                                    usuario.setRua(dataUserApi.getDataDados().getRua());

                                Common.mCurrentUser = usuario;
                                AppDatabase.saveUser(Common.mCurrentUser);
                                AppPref.getInstance().saveAuthToken("ksaksnaksa");

                                if (dataUserApi.getDataDados().getFoto() == null ||
                                        dataUserApi.getDataDados().getSexo() == null ||
                                        dataUserApi.getDataDados().getTelefone() == null ||
                                        dataUserApi.getDataDados().getDataNascimento() == null ||
                                        dataUserApi.getDataDados().getProvincia() == null ||
                                        dataUserApi.getDataDados().getMunicipio() == null ||
                                        dataUserApi.getDataDados().getRua() == null){

                                    Intent intent = new Intent(MediaRumoActivity.this, MeuPerfilActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(MediaRumoActivity.this,"Por favor termina de editar o perfil.",Toast.LENGTH_SHORT).show();
                                }else {
                                    launchHomeScreen();
                                    /*Log.d("autenticacaoVerif",dataUserApi.getDataDados().getDataNascimento());
                                    Log.d("autenticacaoVerif",dataUserApi.getDataDados().getSexo());
                                    Log.d("autenticacaoVerif", "" + dataUserApi.getDataDados().getFoto());*/
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DataUserApi> call, Throwable t) {
                            verifConecxao();
                            switch (t.getMessage()){
                                case "timeout":
                                    Toast.makeText(MediaRumoActivity.this,
                                            "Impossivel se comunicar. Internet lenta.",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MediaRumoActivity.this,
                                            "Algum problema aconteceu. Tente novamente.",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }else {
                    progressDialog.dismiss();
                    Snackbar
                            .make(getCurrentFocus(), "Email ou senha inválidos", 4000)
                            .setActionTextColor(Color.MAGENTA)
                            .show();
                    Log.d("autenticacaoVerif", String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Data> call, Throwable t) {
                progressDialog.dismiss();
                verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(MediaRumoActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MediaRumoActivity.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        }

    }

    private boolean verificarCampos() {

        String email = editTextEmailLogin.getText().toString().trim();
        String palavraPass = editTextPasslLogin.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmailLogin.requestFocus();
            editTextEmailLogin.setError("Preencha o campo.");
            return false;
        }

        if (!MetodosComuns.validarEmail(email)){
            editTextEmailLogin.requestFocus();
            editTextEmailLogin.setError("Preencha o campo com um email.");
            return false;
        }

        if (palavraPass.isEmpty()){
            editTextPasslLogin.requestFocus();
            editTextPasslLogin.setError("Preencha o campo.");
            return false;
        }

        editTextEmailLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);
        //editTextPasslLogin.onEditorAction(EditorInfo.IME_ACTION_DONE);


        return true;
    }



    private void launchHomeScreen() {
        Intent intent = new Intent(MediaRumoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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


}
