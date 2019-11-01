package proitappsolutions.com.rumosstore;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import proitappsolutions.com.rumosstore.telasActivity.HomeInicialActivity;
import proitappsolutions.com.rumosstore.telasActivity.MeuPerfilActivity;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.statuscolor));
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {

            if (TextUtils.isEmpty(AppDatabase.getInstance().getAuthToken())) {
                Intent intent = new Intent(MainActivity.this, MediaRumoActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            if (!TextUtils.isEmpty(AppDatabase.getInstance().getAuthToken())) {

                if (AppDatabase.getInstance().getUser().getSexo() == null ||
                        AppDatabase.getInstance().getUser().getTelefone() == null || AppDatabase.getInstance().getUser().getDataNascimento() == null ||
                        AppDatabase.getInstance().getUser().getProvincia() == null || AppDatabase.getInstance().getUser().getMunicipio() == null ||
                        AppDatabase.getInstance().getUser().getRua() == null){
                    Intent intent = new Intent(MainActivity.this, MeuPerfilActivity.class);
                    startActivity(intent);
                    finish();
                    mostrarMensagem(MainActivity.this,R.string.txtTerminarEditPerfil);
                }else {
                    launchHomeScreen();
                }
            }
        }, 2000);
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(MainActivity.this, HomeInicialActivity.class);
        startActivity(intent);
        finish();
    }


}
