package proitappsolutions.com.rumosstore.telasActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.AppPref;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.MainActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.fragmentos.FragConcurso;
import proitappsolutions.com.rumosstore.fragmentos.FragHomeInicial;
import proitappsolutions.com.rumosstore.fragmentos.FragMediaRumo;
import proitappsolutions.com.rumosstore.fragmentos.FragMercado;
import proitappsolutions.com.rumosstore.fragmentos.FragMeuPerfil;
import proitappsolutions.com.rumosstore.fragmentos.FragRevistas;
import proitappsolutions.com.rumosstore.fragmentos.FragVanguarda;


public class HomeInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView circleImageView;
    private TextView txtName;
    private TextView txtEmail;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inicial);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Rumo Store");
        setSupportActionBar(toolbar);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); 
        View headerView = navigationView.getHeaderView(0);

        circleImageView = (CircleImageView) headerView.findViewById(R.id.iv_imagem_perfil);
        txtName = (TextView) headerView.findViewById(R.id.txtName);
        txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);

        //carregar dados do Usuario
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

    @Override
    public void onBackPressed() {
        toolbar.setTitle("Rumo Store");
        super.onBackPressed();
    }

    private void loaduserProfile(Usuario usuario){

        if (usuario !=null){

            txtName.setText(usuario.getNomeCliente());
            txtEmail.setText(usuario.getEmail());

            if (usuario.getFoto()!=null || !TextUtils.isEmpty(usuario.getFoto())){
                Picasso.with(HomeInicialActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(circleImageView);
            }

        } else {
            logOut();
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
            toolbar.setTitle("Rumo Store");
            FragHomeInicial fragHomeInicial = new FragHomeInicial();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragHomeInicial);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_meu_perfil) {
            Intent intent = new Intent(HomeInicialActivity.this,MeuPerfilActivity.class);
            startActivity(intent);
            /*toolbar.setTitle("Perfil");
            FragMeuPerfil fragMeuPerfil = new FragMeuPerfil();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMeuPerfil);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/

        } else if (id == R.id.nav_quiosque) {
            toolbar.setTitle("Quiosque");
            FragRevistas fragRevistas = new FragRevistas();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragRevistas);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_concurso) {
            toolbar.setTitle("Sorteio Media Rumo");
            FragConcurso fragConcurso = new FragConcurso();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragConcurso);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_mercado) {
            toolbar.setTitle("Rumo Store");
            FragMercado fragMercado = new FragMercado();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMercado);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_vanguarda) {
            toolbar.setTitle("Rumo Store");
            FragVanguarda fragVanguarda = new FragVanguarda();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragVanguarda);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_rumo) {
            toolbar.setTitle("Rumo Store");
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

        }  else if (id == R.id.nav_share) {

            shareTheApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut(){

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

    //Sharing the app
    private void shareTheApp() {

        final String appPackageName = getPackageName();
        String appName = getString(R.string.app_name);
        String appCategory = "Notícias e Revistas";

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String postData = "Obtenha " + appName + " app para ter acesso as " + appCategory +" recentes: "+ "https://play.google.com/store/apps/details?id=" + appPackageName;

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Baixar Agora!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, postData);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Post Via"));
    }


}
