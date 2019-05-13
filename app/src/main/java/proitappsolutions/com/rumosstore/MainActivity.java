package proitappsolutions.com.rumosstore;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.telasActivity.HomeInicialActivity;
import proitappsolutions.com.rumosstore.testeRealmDB.Revistas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AVLoadingIndicatorView progressBar;
    private RelativeLayout errorLayout;
    private LinearLayout linearLayout;
    private TextView btnTentarDeNovo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.statuscolor));
        setContentView(R.layout.activity_main);

        progressBar = (AVLoadingIndicatorView)findViewById(R.id.progress);
        errorLayout = (RelativeLayout)findViewById(R.id.erroLayout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btnTentarDeNovo = (TextView)findViewById(R.id.btn);
        btnTentarDeNovo.setText("Tentar de Novo");
        btnTentarDeNovo.setTextColor(getResources().getColor(R.color.colorBotaoLogin));

//        carregarLocal();

        if (TextUtils.isEmpty(AppPref.getInstance().getAuthToken())) {
            Intent intent = new Intent(MainActivity.this, MediaRumoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;

        }
        verifConecxao();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if (TextUtils.isEmpty(AppPref.getInstance().getAuthToken())) {
//                    Intent intent = new Intent(MainActivity.this, MediaRumoActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                    return;
//
//                }
//                launchHomeScreen();
//
//
//
//            }
//        }, 2000);
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(MainActivity.this, HomeInicialActivity.class);
        startActivity(intent);
        finish();
    }


    private void carregarRevistas() {
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<List<Revistas>> revistas = apiInterface.getRevistas();
        revistas.enqueue(new Callback<List<Revistas>>() {
            @Override
            public void onResponse(Call<List<Revistas>> call, Response<List<Revistas>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Not Successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppDatabase.saveRevistasList(response.body());
                launchHomeScreen();


            }

            @Override
            public void onFailure(Call<List<Revistas>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void verifConecxao() {

        if (getBaseContext() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();
            }else{
                carregarRevistas();
            }
        }


    }

    private void mostarMsnErro(){

        if (errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }

        btnTentarDeNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                verifConecxao();
            }
        });
    }


}
