package proitappsolutions.com.rumosstore.QUIZ;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Model.User;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.telasActivity.HomeInicialActivity;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Toolbar toobarquiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_home);

        toobarquiz = findViewById(R.id.toobarquiz);
        toobarquiz.setTitle("Hora Do QUIZ");
        setSupportActionBar(toobarquiz);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        /*.getEmail().split("@")[0]*/

        Common.currentUser = new User (AppDatabase.getInstance().getUser().nomeCliente
                , "",
                proitappsolutions.com.rumosstore.Common.mCurrentUser.getEmail());

        verifConecxao();


        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.action_category:
                    new CategoryFragment(); //Instancia criada. Antes fazia um acesso direito
                    selectedFragment = CategoryFragment.newInstance();
                    break;
                case R.id.action_ranking:
                    new RankingFragment(); //Instancia criada. Antes fazia um acesso direito
                    selectedFragment = RankingFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (selectedFragment != null){
                transaction.replace(R.id.frame_layout,selectedFragment);
                transaction.commit();
            }
            return true;
        });
        setDefaultFragment();
    }

    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            mostrarMensagem(Home.this,R.string.txtMsgErroRede);
        }
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,CategoryFragment.newInstance());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Home.this, HomeInicialActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
