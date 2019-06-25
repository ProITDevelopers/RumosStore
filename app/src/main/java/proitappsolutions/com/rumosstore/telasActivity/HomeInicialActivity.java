package proitappsolutions.com.rumosstore.telasActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.AppPref;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.MainActivity;
import proitappsolutions.com.rumosstore.MediaRumoActivity;
import proitappsolutions.com.rumosstore.QUIZ.Home;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorResponce;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorUtils;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
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
import proitappsolutions.com.rumosstore.testeRealmDB.QuiosqueActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CircleImageView circleImageView;
    private TextView txtName,tv_inicial_nome,atualizar_senha_dialg;
    private TextView txtEmail;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private Button btn_cancelar,btn_cancelar_dialog,btn_redif_senha_dialog;
    private ShowHidePasswordEditText edtSenhaAntiga,edtConfSenhaNova;
    private Dialog caixa_dialogo_cancelar,dialogSenhaEnviarEmailSenhaNova;
    public DataUserApi dataUserApi  = new DataUserApi();
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorBotaoLogin));
        setContentView(R.layout.activity_home_inicial);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Media Rumo");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(HomeInicialActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);

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

        Button btnCancelar_dialog = caixa_dialogo_cancelar.findViewById(R.id.btnCancelar_dialog);
        Button btnSim = caixa_dialogo_cancelar.findViewById(R.id.btnSim);
        Button btnNao = caixa_dialogo_cancelar.findViewById(R.id.btnNao);
        TextView diagolo_titulo = caixa_dialogo_cancelar.findViewById(R.id.diagolo_titulo);
        btnCancelar_dialog.setOnClickListener(HomeInicialActivity.this);
        diagolo_titulo.setText("Deseja terminar a sessão ?");
        btnSim.setOnClickListener(HomeInicialActivity.this);
        btnNao.setOnClickListener(HomeInicialActivity.this);


        //Dialogo enviar senha nova email
        dialogSenhaEnviarEmailSenhaNova = new Dialog(HomeInicialActivity.this);
        dialogSenhaEnviarEmailSenhaNova.setContentView(R.layout.dialogo_activity_atualizar_senha);
        dialogSenhaEnviarEmailSenhaNova.setCancelable(false);
        atualizar_senha_dialg = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.atualizar_senha);
        edtSenhaAntiga = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.edtSenhaNova);
        edtConfSenhaNova = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.edtConfSenha);
        btn_redif_senha_dialog = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.btn_redif_senha);
        btn_cancelar_dialog = dialogSenhaEnviarEmailSenhaNova.findViewById(R.id.btn_cancelar);
        btn_redif_senha_dialog.setOnClickListener(HomeInicialActivity.this);
        btn_cancelar_dialog.setOnClickListener(HomeInicialActivity.this);
        atualizar_senha_dialg.setText(R.string.hit_atualizar_senha);

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

                        Common.mCurrentUser = new Usuario();
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
                alterarSenha();
                /*Intent atualizarSenhaIntent = new Intent(HomeInicialActivity.this,AtualizarSenhaSenhaActivity.class);
                startActivity(atualizarSenhaIntent);*/
                break;
            case R.id.action_logout:
                caixa_dialogo_cancelar.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alterarSenha() {
        dialogSenhaEnviarEmailSenhaNova.show();
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
        } else if (id == R.id.nav_quiz) {
            Intent intent = new Intent(HomeInicialActivity.this, Home.class);
            startActivity(intent);
        } else if (id == R.id.nav_quiosque) {

            Intent intent = new Intent(HomeInicialActivity.this, QuiosqueActivity.class);
            startActivity(intent);

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
            enviarLinkActivity("https://mercado.co.ao/","mercado",HomeInicialActivity.this);
        } else if (id == R.id.nav_vanguarda) {
            enviarLinkActivity("https://www.vanguarda.co.ao/","vanguarda",HomeInicialActivity.this);
        } else if (id == R.id.nav_rumo) {
            enviarLinkActivity("https://mediarumo.com/","rumo",HomeInicialActivity.this);
        }else if (id == R.id.nav_instagram) {

            enviarLinkActivity("https://www.instagram.com/jornalvanguardaa/","instagram",HomeInicialActivity.this);

        }  else if (id == R.id.nav_facebook) {

            enviarLinkActivity("https://www.facebook.com/jornalmercado/","facebook",HomeInicialActivity.this);

        }  else if (id == R.id.nav_share) {

            shareTheApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void enviarLinkActivity(String s, String cor, Context context) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("site",s);
        intent.putExtra("cor",cor);
        startActivity(intent);
    }

    private void logOut(){

        AppDatabase.getInstance().clearData();

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
            case R.id.btn_redif_senha:
                if (verificarCamposDialogo()){
                    redefinirSenha();
                }
                break;
            case R.id.btn_cancelar:
                cancelarAtualizarSenha();
                break;
                
            
        }
    }

    private void redefinirSenha() {

        //enviarNovaSenha
        progressDialog.setMessage("A processar...!");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> enviarSenhaNova = apiInterface.atualizarSenha(Integer.parseInt(AppDatabase
                .getInstance()
                .getUser()
                .getId_utilizador()),
                edtConfSenhaNova.getText().toString(),
                edtSenhaAntiga.getText().toString()); //AQUI

        enviarSenhaNova.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(HomeInicialActivity.this,"A sua senha foi alterada com sucesso.!",Toast.LENGTH_SHORT).show();
                    cancelarAtualizarSenha();
                    logOut();
                }else {
                    progressDialog.dismiss();
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    Toast.makeText(HomeInicialActivity.this,errorResponce.getError(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("skansaksas",t.getMessage() + "failed");
                //verifConecxao();
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(HomeInicialActivity.this,
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(HomeInicialActivity.this,
                                "Algum problema aconteceu.Verifica a conexão com a internet.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private boolean verificarCamposDialogo() {
        if (TextUtils.isEmpty(edtSenhaAntiga.getText())){
            edtSenhaAntiga.setError("Preencha o campo");
            return false;
        }

        if (TextUtils.isEmpty(edtConfSenhaNova.getText())){
            edtConfSenhaNova.setError("Preencha o campo");
            return false;
        }


        if (edtSenhaAntiga.equals(edtConfSenhaNova)){
            edtConfSenhaNova.setError("Os campos devem ser diferentes.");
            return false;
        }

        if (edtConfSenhaNova.getText().length() < 5){
            edtConfSenhaNova.setError("Senha fraca.");
            return false;
        }
        return true;
    }

    private void cancelarAtualizarSenha() {
        edtSenhaAntiga.getText().clear();
        edtConfSenhaNova.getText().clear();
        edtSenhaAntiga.clearFocus();
        edtConfSenhaNova.clearFocus();
        edtConfSenhaNova.setError(null);
        edtSenhaAntiga.setError(null);
        dialogSenhaEnviarEmailSenhaNova.dismiss();
    }
}
