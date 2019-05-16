package proitappsolutions.com.rumosstore.telasActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.AppPref;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.MainActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.fragmentos.FragConcurso;
import proitappsolutions.com.rumosstore.fragmentos.FragFacebook;
import proitappsolutions.com.rumosstore.fragmentos.FragHomeInicial;
import proitappsolutions.com.rumosstore.fragmentos.FragInstagram;
import proitappsolutions.com.rumosstore.fragmentos.FragMediaRumo;
import proitappsolutions.com.rumosstore.fragmentos.FragMercado;
import proitappsolutions.com.rumosstore.fragmentos.FragRevistas;
import proitappsolutions.com.rumosstore.fragmentos.FragVanguarda;
import proitappsolutions.com.rumosstore.modelo.DataUserApi;
import proitappsolutions.com.rumosstore.testeRealmDB.FragRevistasTeste;
import proitappsolutions.com.rumosstore.testeRealmDB.Revistas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView circleImageView;
    private TextView txtName;
    private TextView txtEmail;
    private Toolbar toolbar;
    public DataUserApi dataUserApi  = new DataUserApi();

    List<Revistas> revistasList;



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
        verifConecxao();
//        verifConecxaoRevistas();
//        loaduserProfile(AppDatabase.getUser());

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
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragHomeInicial()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragConcurso()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            this.moveTaskToBack(true);
        }
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

    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            loaduserProfile(AppDatabase.getUser());
        }else{
            carregarDadosdoUserApi(AppDatabase.getUser());
        }

    }

    private void carregarDadosdoUserApi(Usuario usuario) {

            ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
            retrofit2.Call<DataUserApi> callApiDados = apiInterface.getUsuarioDados(usuario.getId_utilizador());

            callApiDados.enqueue(new Callback<DataUserApi>() {
                @Override
                public void onResponse(Call<DataUserApi> call, Response<DataUserApi> response) {
                    if (response.isSuccessful()){
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

                        if (dataUserApi.getDataDados().getDataNascimento() != null )
                            usuario.setDataNascimento(dataUserApi.getDataDados().getDataNascimento());

                        if (dataUserApi.getDataDados().getProvincia() != null )
                            usuario.setProvincia(dataUserApi.getDataDados().getProvincia());

                        if (dataUserApi.getDataDados().getMunicipio() != null )
                            usuario.setMunicipio(dataUserApi.getDataDados().getMunicipio());

                        if (dataUserApi.getDataDados().getRua() != null )
                            usuario.setRua(dataUserApi.getDataDados().getRua());

                        Common.mCurrentUser = usuario;
                        AppDatabase.saveUser(Common.mCurrentUser);

                        loaduserProfile(Common.mCurrentUser);

                    }
                }

                @Override
                public void onFailure(Call<DataUserApi> call, Throwable t) {

                }
            });
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

        switch (id){
            case R.id.action_alterar_senha:
                break;

            case R.id.action_logout:
                showLogOutAlert();
                break;
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
            fragmentTransaction.commit();
        }else if (id == R.id.nav_meu_perfil) {
            Intent intent = new Intent(HomeInicialActivity.this,MeuPerfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_quiosque) {
            toolbar.setTitle("Quiosque");
//            FragRevistas fragRevistas = new FragRevistas();
//            android.support.v4.app.FragmentTransaction fragmentTransaction =
//                    getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.container,fragRevistas);
//            fragmentTransaction.commit();
            FragRevistasTeste fragRevistas = new FragRevistasTeste();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragRevistas);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_concurso) {
            toolbar.setTitle("Sorteio Media Rumo");
            FragConcurso fragConcurso = new FragConcurso();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragConcurso);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_mercado) {
            toolbar.setTitle("Mercado");
            FragMercado fragMercado = new FragMercado();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMercado);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_vanguarda) {
            toolbar.setTitle("Vanguarda");
            FragVanguarda fragVanguarda = new FragVanguarda();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragVanguarda);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_rumo) {
            toolbar.setTitle("Media Rumo");
            FragMediaRumo fragMediaRumo = new FragMediaRumo();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMediaRumo);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_instagram) {
            toolbar.setTitle("Instagram");
            FragInstagram fragInstagram = new FragInstagram();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragInstagram);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }  else if (id == R.id.nav_facebook) {
            toolbar.setTitle("Facebook");
            FragFacebook fragFacebook = new FragFacebook();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragFacebook);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }  else if (id == R.id.nav_share) {

            shareTheApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void showLogOutAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Terminar a sessão");
        dialog.setMessage("Deseja continuar?");

        //Set button
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                logOut();

            }
        });



        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });





        dialog.show();
    }

    private void logOut(){

        AppDatabase.clearData();
        AppPref.getInstance().clearData();

        Intent intent = new Intent(HomeInicialActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


    }


    //Sharing the app
    private void shareTheApp() {

        final String appPackageName = getPackageName();
        String appName = getString(R.string.app_name);
        String appCategory = "Notícias e Revistas";

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String postData = "Obtenha o aplicativo " + appName + " para ter acesso as " + appCategory +" recentes: "+ "https://play.google.com/store/apps/details?id=" + appPackageName;

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Baixar Agora!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, postData);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Post Via"));
    }

    private void verifConecxaoRevistas() {

        if (getBaseContext() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo != null){
                carregarRevistas();
            }

        }

    }

    private void carregarRevistas() {
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<List<Revistas>> rv = apiInterface.getRevistas();
        rv.enqueue(new Callback<List<Revistas>>() {
            @Override
            public void onResponse(@NonNull Call<List<Revistas>> call, @NonNull Response<List<Revistas>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Bem-vindo "+AppDatabase.getUser().getNomeCliente(), Toast.LENGTH_SHORT).show();
                } else {

                    revistasList = response.body();

                    AppDatabase.saveRevistasList(revistasList);
                }




            }

            @Override
            public void onFailure(Call<List<Revistas>> call, Throwable t) {

            }
        });



    }

}
