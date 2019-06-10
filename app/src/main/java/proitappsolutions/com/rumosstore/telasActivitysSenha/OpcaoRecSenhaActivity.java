package proitappsolutions.com.rumosstore.telasActivitysSenha;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;

public class OpcaoRecSenhaActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorBotaoLogin));
        setContentView(R.layout.activity_opcao_rec_senha);

        android.support.v7.widget.Toolbar toolbar_redif_senha = findViewById(R.id.toolbar_redif_senha);
        toolbar_redif_senha.setTitle("");
        setSupportActionBar(toolbar_redif_senha);
        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_email_redif = findViewById(R.id.btn_email_redif);
        Button btn_telef_redif = findViewById(R.id.btn_telef_redif);
        btn_email_redif.setOnClickListener(OpcaoRecSenhaActivity.this);
        btn_telef_redif.setOnClickListener(OpcaoRecSenhaActivity.this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_email_redif:
                Intent intentRecEmail = new Intent(OpcaoRecSenhaActivity.this,RecuperarSenhaEmailActivity.class);
                startActivity(intentRecEmail);
                break;
            case R.id.btn_telef_redif:
                Intent intentRecTelef = new Intent(OpcaoRecSenhaActivity.this,RecuperarSenhaTelefoneActivity.class);
                startActivity(intentRecTelef);
                break;

        }
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
