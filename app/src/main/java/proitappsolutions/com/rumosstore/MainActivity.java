package proitappsolutions.com.rumosstore;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import proitappsolutions.com.rumosstore.telasIniciais.HomeInicialActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.statuscolor));
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(AppPref.getInstance().getAuthToken())) {
                    Intent intent = new Intent(MainActivity.this, MediaRumoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return;

                }
                launchHomeScreen();


            }
        }, 2000);

    }

    private void launchHomeScreen() {
        Intent intent = new Intent(MainActivity.this, HomeInicialActivity.class);
        startActivity(intent);
        finish();
    }
}
