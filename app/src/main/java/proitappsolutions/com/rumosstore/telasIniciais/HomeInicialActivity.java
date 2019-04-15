package proitappsolutions.com.rumosstore.telasIniciais;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.AppPref;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.MainActivity;
import proitappsolutions.com.rumosstore.MediaRumoActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.fragmentos.FragConcurso;
import proitappsolutions.com.rumosstore.fragmentos.FragHomeInicial;
import proitappsolutions.com.rumosstore.fragmentos.FragMediaRumo;
import proitappsolutions.com.rumosstore.fragmentos.FragMercado;
import proitappsolutions.com.rumosstore.fragmentos.FragQuiosque;
import proitappsolutions.com.rumosstore.fragmentos.FragVanguarda;
import proitappsolutions.com.rumosstore.modelo.EmSessao;

public class HomeInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView circleImageView;
    private TextView txtName;
    private TextView txtEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inicial);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Rumos Store");
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);

        circleImageView = (CircleImageView) headerView.findViewById(R.id.iv_imagem_perfil);
        txtName = (TextView) headerView.findViewById(R.id.txtName);
        txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);


        loaduserProfile(AppDatabase.getUser());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_abre, R.string.navigation_drawer_abre){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragHomeInicial()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    private void loaduserProfile(Usuario usuario){

        if (usuario !=null){
            Picasso.with(HomeInicialActivity.this)
                    .load(usuario.getUsuarioPic())
                    .placeholder(R.drawable.ic_avatar)
                    .into(circleImageView);

            txtName.setText(usuario.getUsuarioNome());
            txtEmail.setText(usuario.getUsuarioEmail());
        }else {
            Intent dados = getIntent();
            txtName.setText(dados.getStringExtra("nome"));
            txtEmail.setText(dados.getStringExtra("email"));
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rumos_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logOut();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();


        if (id == R.id.nav_home) {
            FragHomeInicial fragHomeInicial = new FragHomeInicial();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragHomeInicial);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_quiosque) {

            FragQuiosque fragQuiosque = new FragQuiosque();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragQuiosque);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }


        else if (id == R.id.nav_concurso) {

            FragConcurso fragConcurso = new FragConcurso();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragConcurso);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }


        else if (id == R.id.nav_mercado) {

            FragMercado fragMercado = new FragMercado();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMercado);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_vanguarda) {

            FragVanguarda fragVanguarda = new FragVanguarda();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragVanguarda);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_rumo) {

            FragMediaRumo fragMediaRumo = new FragMediaRumo();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMediaRumo);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_instagram) {
            openInstagram(Common.SOCIAL_INSTAGRAM);
        }  else if (id == R.id.nav_facebook) {

            openFbUrl(Common.SOCIAL_FACEBOOK);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut(){
        LoginManager.getInstance().logOut();
        AppDatabase.clearData();
        AppPref.getInstance().clearData();


        Intent intent = new Intent(HomeInicialActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void openInstagram(String username) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + username);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + username)));
        }
    }

    protected void openFbUrl(String username){
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/" + username)));
    }
}
