package proitappsolutions.com.rumosstore.telasActivitysSenha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.modelo.CodConfirmacaoResult;
import proitappsolutions.com.rumosstore.modelo.RecuperarSenha;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnviarCodConfActivityTelefone extends AppCompatActivity {

    private String TAG = "EnviarCodConfActivity";
    private EditText editCod1,editCod2,editCod3,editCod4,editCod5,editCod6;
    private TextView tv_email;
    private String id,email,telefone,codigo1,codigo2,codigo3,codigo4,codigo5,codigo6;
    private Button btn_enviar_cod_reset;
    private ProgressDialog progressDialog;
    private RelativeLayout errorLayout;
    private RelativeLayout relativeLayout;
    private TextView btnTentarDeNovo,receberDeNovo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_cod_conf);

        editCod1 = findViewById(R.id.editCod1);
        editCod2 = findViewById(R.id.editCod2);
        editCod3 = findViewById(R.id.editCod3);
        editCod4 = findViewById(R.id.editCod4);
        editCod5 = findViewById(R.id.editCod5);
        editCod6 = findViewById(R.id.editCod6);
        tv_email = findViewById(R.id.tv_email);
        receberDeNovo = findViewById(R.id.receberDeNovo);
        btn_enviar_cod_reset = findViewById(R.id.btn_enviar_cod_reset);
        errorLayout = findViewById(R.id.erroLayout);
        relativeLayout = findViewById(R.id.relativeLayout);
        btnTentarDeNovo = findViewById(R.id.btn);
        btnTentarDeNovo.setText("Voltar");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));
        progressDialog = new ProgressDialog(EnviarCodConfActivityTelefone.this);
        progressDialog.setCancelable(false);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        //email = intent.getStringExtra("email");
        telefone = intent.getStringExtra("telefone");

        //if (email==null){
        tv_email.setText(telefone);
        //}else {
          //  tv_email.setText(telefone);
        //}
        controleEditText();

        btn_enviar_cod_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampo())
                    enviarCodRedifinicao();
            }
        });

        receberDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mandarTeledResetSenha();
            }
        });
    }

    private void mandarTeledResetSenha() {

        errorLayout.setVisibility(View.GONE);

        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<RecuperarSenha> enviarEmailReset = apiInterface.enviarTelefone(telefone);
        progressDialog.setMessage("A enviar o código para " + telefone);
        progressDialog.show();
        enviarEmailReset.enqueue(new Callback<RecuperarSenha>() {
            @Override
            public void onResponse(Call<RecuperarSenha> call, Response<RecuperarSenha> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(EnviarCodConfActivityTelefone.this,"O código foi reenviado.",Toast.LENGTH_SHORT).show();
                    Log.i("skansaksas",response.body().getId() + "sucess");
                }else {
                    Toast.makeText(EnviarCodConfActivityTelefone.this,"Por algum motivo esta operação falhou..",Toast.LENGTH_SHORT).show();
                    Log.i("skansaksas",response.code() + "error");
                    Log.i("skansaksas",response.code() + telefone);
                    try {
                        Log.i("skansaksas",response.errorBody().string() + telefone);
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
                        Toast.makeText(EnviarCodConfActivityTelefone.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(EnviarCodConfActivityTelefone.this,
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private void enviarCodRedifinicao() {

        errorLayout.setVisibility(View.GONE);

        progressDialog.setMessage("Enviando o código de confirmação..!");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<CodConfirmacaoResult> enviarCod = apiInterface.enviarConfirCodigo(id,codigo1+codigo2+codigo3+codigo4+codigo5+codigo6);
        enviarCod.enqueue(new Callback<CodConfirmacaoResult>() {
            @Override
            public void onResponse(Call<CodConfirmacaoResult> call, Response<CodConfirmacaoResult> response) {
                if (!response.isSuccessful()){
                    try {
                        Toast.makeText(EnviarCodConfActivityTelefone.this,"Código de redifinição Incorrecto.",Toast.LENGTH_SHORT).show();
                        //response.body().
                        Log.i(TAG,"certo" + response.errorBody().string());
                        //erro = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();

                }else {
                    if (response.body() != null){
                        Intent intent = new Intent(EnviarCodConfActivityTelefone.this,RedifinirSenhaActivity.class);
                        intent.putExtra("token",response.body().getToken());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Log.i(TAG,"certo" + response.code());
                        Log.i(TAG,"certo" + response.body().getExpiresIn());
                        Log.i(TAG,"certo" + response.body().getToken());
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CodConfirmacaoResult> call, Throwable t) {
                progressDialog.dismiss();

                Log.i("skansaksas",t.getMessage() + "failed");
                verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(EnviarCodConfActivityTelefone.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(EnviarCodConfActivityTelefone.this,
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
            mostarMsnErro();
        }else{
        }
    }


    private boolean verificarCampo() {
        codigo1 = editCod1.getText().toString().trim();
        codigo2 = editCod2.getText().toString().trim();
        codigo3 = editCod3.getText().toString().trim();
        codigo4 = editCod4.getText().toString().trim();
        codigo5 = editCod5.getText().toString().trim();
        codigo6 = editCod6.getText().toString().trim();

        if (codigo1.isEmpty()){
            editCod1.setError("Preencha o campo.");
            return false;
        }

        if (codigo2.isEmpty()){
            editCod2.setError("Preencha o campo.");
            return false;
        }

        if (codigo3.isEmpty()){
            editCod3.setError("Preencha o campo.");
            return false;
        }

        if (codigo4.isEmpty()){
            editCod4.setError("Preencha o campo.");
            return false;
        }
        if (codigo5.isEmpty()){
            editCod5.setError("Preencha o campo.");
            return false;
        }

        if (codigo6.isEmpty()){
            editCod6.setError("Preencha o campo.");
            return false;
        }

        return true;

    }

    private void controleEditText() {

        codigo1 = editCod1.getText().toString();
        codigo2 = editCod2.getText().toString();
        codigo3 = editCod3.getText().toString();
        codigo4 = editCod4.getText().toString();
        codigo5 = editCod5.getText().toString();
        codigo6 = editCod6.getText().toString();

        editCod1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editCod1.getText())){
                    editCod2.requestFocus();
                }
            }
        });

        editCod2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editCod2.getText())){
                    editCod3.requestFocus();
                }else {
                    editCod1.requestFocus();
                }
            }
        });

        editCod3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editCod3.getText())){
                    editCod4.requestFocus();
                }else {
                editCod2.requestFocus();
            }
            }
        });

        editCod4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editCod4.getText())){
                    editCod5.requestFocus();
                }else {
                    editCod3.requestFocus();
                }
            }
        });
        editCod5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editCod5.getText())){
                    editCod6.requestFocus();
                }else {
                    editCod4.requestFocus();
                }
            }
        });

        editCod6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editCod6.getText())){

                }else {
                    editCod5.requestFocus();
                }
            }
        });

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

}
