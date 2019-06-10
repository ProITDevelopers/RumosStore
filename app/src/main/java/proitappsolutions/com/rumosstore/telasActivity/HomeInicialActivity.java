package proitappsolutions.com.rumosstore.telasActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import proitappsolutions.com.rumosstore.fragmentos.FragVanguarda;
import proitappsolutions.com.rumosstore.modelo.DataUserApi;
import proitappsolutions.com.rumosstore.telasActivitysSenha.AtualizarSenhaSenhaActivity;
import proitappsolutions.com.rumosstore.testeRealmDB.FragRevistasTeste;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CircleImageView circleImageView;
    private TextView txtName,tv_inicial_nome;
    private TextView txtEmail,diagolo_titulo;
    private Toolbar toolbar;
    private Button btn_cancelar,btnSim,btnNao,btnCancelar_dialog;
    private Dialog caixa_dialogo_cancelar;
    public DataUserApi dataUserApi  = new DataUserApi();
    ActionBarDrawerToggle toggle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_inicial);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Media Rumo");
        setSupportActionBar(toolbar);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); 
        View headerView = navigationView.getHeaderView(0);

        circleImageView = (CircleImageView) headerView.findViewById(R.id.iv_imagem_perfil);
        txtName = (TextView) headerView.findViewById(R.id.txtName);
        txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);
        tv_inicial_nome = (TextView) headerView.findViewById(R.id.tv_inicial_nome);

        caixa_dialogo_cancelar = new Dialog(HomeInicialActivity.this);
        caixa_dialogo_cancelar.setContentView(R.layout.caixa_de_dialogo_redif_senha);
        caixa_dialogo_cancelar.setCancelable(false);

        btnCancelar_dialog = caixa_dialogo_cancelar.findViewById(R.id.btnCancelar_dialog);
        btnSim = caixa_dialogo_cancelar.findViewById(R.id.btnSim);
        btnNao = caixa_dialogo_cancelar.findViewById(R.id.btnNao);
        diagolo_titulo = caixa_dialogo_cancelar.findViewById(R.id.diagolo_titulo);
        btnCancelar_dialog.setOnClickListener(HomeInicialActivity.this);
        diagolo_titulo.setText("Deseja terminar a sessão ?");
        btnSim.setOnClickListener(HomeInicialActivity.this);
        btnNao.setOnClickListener(HomeInicialActivity.this);

        //carregar dados do Usuario
        Common.mCurrentUser = AppDatabase.getInstance().getUser();
        loaduserProfile(Common.mCurrentUser);

        if (Common.mCurrentUser!=null){
            verifConecxao(Common.mCurrentUser);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
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

                tv_inicial_nome.setVisibility(View.GONE);
                Picasso.with(HomeInicialActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(circleImageView);
            }else{
                tv_inicial_nome.setText(String.valueOf(usuario.getNomeCliente().charAt(0)).toUpperCase());
            }


        } else {
            logOut();
        }
    }

    private void verifConecxao(Usuario usuario) {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr!=null){
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo != null){
                carregarDadosdoUserApi(usuario);
            }
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



                        if (dataUserApi.getDataDados().getId_utilizador() != null )
                            Common.mCurrentUser.setId_utilizador(dataUserApi.getDataDados().getId_utilizador());

                        if (dataUserApi.getDataDados().getNomeCliente() != null )
                            Common.mCurrentUser.setNomeCliente(dataUserApi.getDataDados().getNomeCliente());

                        if (dataUserApi.getDataDados().getEmail() != null )
                            Common.mCurrentUser.setEmail(dataUserApi.getDataDados().getEmail());

                        if (dataUserApi.getDataDados().getFoto() != null )
                            Common.mCurrentUser.setFoto(dataUserApi.getDataDados().getFoto());

                        if (dataUserApi.getDataDados().getSexo() != null )
                            Common.mCurrentUser.setSexo(dataUserApi.getDataDados().getSexo());

                        if (dataUserApi.getDataDados().getTelefone() != null )
                            Common.mCurrentUser.setTelefone(dataUserApi.getDataDados().getTelefone());

                        if (dataUserApi.getDataDados().getDataNascimento() != null )
                            Common.mCurrentUser.setDataNascimento(dataUserApi.getDataDados().getDataNascimento());

                        if (dataUserApi.getDataDados().getProvincia() != null )
                            Common.mCurrentUser.setProvincia(dataUserApi.getDataDados().getProvincia());

                        if (dataUserApi.getDataDados().getMunicipio() != null )
                            Common.mCurrentUser.setMunicipio(dataUserApi.getDataDados().getMunicipio());

                        if (dataUserApi.getDataDados().getRua() != null )
                            Common.mCurrentUser.setRua(dataUserApi.getDataDados().getRua());


                        AppDatabase.getInstance().saveUser(Common.mCurrentUser);
                        AppDatabase.getInstance().saveAuthToken(Common.mCurrentUser.getId_utilizador());

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
        getMenuInflater().inflate(R.menu.rumos_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_alterar_senha:
                Intent atualizarSenhaIntent = new Intent(HomeInicialActivity.this,AtualizarSenhaSenhaActivity.class);
                startActivity(atualizarSenhaIntent);
                break;
            case R.id.action_logout:
                caixa_dialogo_cancelar.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            if (getSupportActionBar() != null){
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
                toolbar.setTitle("");
                ColorDrawable corBranca = new ColorDrawable(ContextCompat.getColor(this, R.color.cor_principal));
                getSupportActionBar().setBackgroundDrawable(corBranca);
            }
            toolbar.setTitle("Media Rumo");
            FragHomeInicial fragHomeInicial = new FragHomeInicial();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragHomeInicial);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_meu_perfil) {
            Intent intent = new Intent(HomeInicialActivity.this,MeuPerfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_quiosque) {
            if (getSupportActionBar() != null){
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
                toolbar.setTitle("");
                ColorDrawable corBranca = new ColorDrawable(ContextCompat.getColor(this, R.color.cor_principal));
                getSupportActionBar().setBackgroundDrawable(corBranca);
            }
            toolbar.setTitle("Quiosque");
            FragRevistasTeste fragRevistas = new FragRevistasTeste();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragRevistas);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_concurso) {
            if (getSupportActionBar() != null){
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
                toolbar.setTitle("");
                ColorDrawable corBranca = new ColorDrawable(ContextCompat.getColor(this, R.color.cor_principal));
                getSupportActionBar().setBackgroundDrawable(corBranca);
            }
            toolbar.setTitle("Sorteio Media Rumo");
            FragConcurso fragConcurso = new FragConcurso();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragConcurso);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_mercado) {
            enviarLinkActivity("https://mercado.co.ao/","mercado");
        } else if (id == R.id.nav_vanguarda) {
            enviarLinkActivity("https://www.vanguarda.co.ao/","vanguarda");
        } else if (id == R.id.nav_rumo) {
            enviarLinkActivity("https://mediarumo.com/","rumo");
        }else if (id == R.id.nav_instagram) {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
            if (getSupportActionBar() != null){
                toolbar.setTitle("");
            ColorDrawable corBranca = new ColorDrawable(ContextCompat.getColor(this, R.color.white));
            getSupportActionBar().setBackgroundDrawable(corBranca);
        }
            FragInstagram fragInstagram = new FragInstagram();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragInstagram);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }  else if (id == R.id.nav_facebook) {
            if (getSupportActionBar() != null){
                toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
                toolbar.setTitle("");
                ColorDrawable corBranca = new ColorDrawable(ContextCompat.getColor(this, R.color.facebook));
                getSupportActionBar().setBackgroundDrawable(corBranca);
            }
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

    private void enviarLinkActivity(String s,String cor) {
        Intent intent = new Intent(HomeInicialActivity.this,WebViewActivity.class);
        intent.putExtra("site",s);
        intent.putExtra("cor",cor);
        startActivity(intent);
    }

    private void logOut(){

        AppDatabase.getInstance().clearData();
//        AppPref.getInstance().clearData();
        caixa_dialogo_cancelar.dismiss();

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
        startActivity(Intent.createChooser(shareIntent, "Partilhar Link da App"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSim:
                logOut();
                break;
            case R.id.btnNao:
                caixa_dialogo_cancelar.dismiss();
                break;
            case R.id.btnCancelar_dialog:
                caixa_dialogo_cancelar.dismiss();
                break;
        }
    }
}
