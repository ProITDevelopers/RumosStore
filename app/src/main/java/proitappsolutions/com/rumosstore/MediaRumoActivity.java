package proitappsolutions.com.rumosstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import proitappsolutions.com.rumosstore.telasIniciais.HomeInicialActivity;

public class MediaRumoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailLogin,editTextPasslLogin;
    private ImageView btnLogFb;
    private Button btnEntrar,btnRegistrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_rumo);
        //comit
        inicializar();

    }

    private void inicializar() {
        editTextEmailLogin = findViewById(R.id.editTextEmaiLogin);
        editTextPasslLogin= findViewById(R.id.editTextPasslLogin);
        btnLogFb = findViewById(R.id.btnLogFb);
        btnEntrar= findViewById(R.id.btnEntrar);
        btnRegistrate = findViewById(R.id.btnRegistrate);
        btnEntrar.setOnClickListener(MediaRumoActivity.this);
        btnRegistrate.setOnClickListener(MediaRumoActivity.this);
        btnLogFb.setOnClickListener(MediaRumoActivity.this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnEntrar:
                Intent intentEntrar = new Intent(MediaRumoActivity.this,HomeInicialActivity.class);
                intentEntrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentEntrar);
                break;

            case R.id.btnRegistrate:
                Intent intentRegistrar = new Intent(MediaRumoActivity.this,RegistroActivity.class);
                startActivity(intentRegistrar);
                break;

            case R.id.btnLogFb:
                break;
        }

    }
}
