package proitappsolutions.com.rumosstore.telasActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;

import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.fragmentos.FragMeuPerfil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeuPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TIRAR_FOTO_CAMARA = 1, ESCOLHER_FOTO_GALERIA = 1995, PERMISSAO_FOTO = 3;
    private TextView txtName, txtEmail, numeroTelef, valorProvincia, valorMunicipio,
            valorRua, valorGenero, valorDataNasc, txtNameEditar, txtEmailEditar;
    private CircleImageView iv_imagem_perfil, iv_imagem_perfilEditar;
    private ImageView imagem_editar_foto;
    private Button btnEditarPerfil, btnCancelarEdicao, btnSalvarDados, btnCamara, btnGaleria, btnCancelar_dialog;
    private RelativeLayout relativeLayoutMeuPerfil, relativeLayoutEditarPerfil;
    private String telefone, cidade, municipio, rua, genero, dataNasc;
    private AppCompatEditText editTelefoneEditar, editCidadeEditar, editMunicipioEditar,
            editRuaEditar, editGeneroEditar, editDataNascEditar;
    private ProgressDialog progressDialog;
    private String id;
    private RelativeLayout erroLayout;
    private TextView btnVoltar;
    private Dialog caixa_dialogo_foto;
    private Uri selectedImage;
    private Bitmap bitmapImg;
    private String postPath;
    final int CROP_PIC = 55;
    private Intent CropIntent;
    private Toolbar toolbar_meu_perfil;
    private Toolbar toolbar_editPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        toolbar_meu_perfil = findViewById(R.id.toolbar_meu_perfil);
        setSupportActionBar(toolbar_meu_perfil);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        iv_imagem_perfil = findViewById(R.id.iv_imagem_perfil);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        numeroTelef = findViewById(R.id.numeroTelef);
        valorProvincia = findViewById(R.id.valorProvincia);
        valorMunicipio = findViewById(R.id.valorMunicipio);
        valorRua = findViewById(R.id.valorRua);
        valorGenero = findViewById(R.id.valorGenero);
        valorDataNasc = findViewById(R.id.valorDataNasc);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCancelarEdicao = findViewById(R.id.btnCancelarEdicao);
        relativeLayoutMeuPerfil = findViewById(R.id.relativeLayoutMeuPerfil);
        relativeLayoutEditarPerfil = findViewById(R.id.relativeLayoutEditarPerfil);
        relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);
        erroLayout = findViewById(R.id.erroLayout);
        btnVoltar = findViewById(R.id.btn);
        btnVoltar.setText("Voltar");
        caixa_dialogo_foto = new Dialog(MeuPerfilActivity.this);
        caixa_dialogo_foto.setContentView(R.layout.caixa_de_dialogo_foto);
        caixa_dialogo_foto.setCancelable(false);

        //Botão em caixa de dialogo foto
        btnCamara = caixa_dialogo_foto.findViewById(R.id.btnCamara);
        btnGaleria = caixa_dialogo_foto.findViewById(R.id.btnGaleria);
        btnCancelar_dialog = caixa_dialogo_foto.findViewById(R.id.btnCancelar_dialog);

        progressDialog = new ProgressDialog(MeuPerfilActivity.this);
        progressDialog.setCancelable(false);

        //editarPerfil layout
        toolbar_editPerfil = relativeLayoutEditarPerfil.findViewById(R.id.toolbar_editPerfil);
        iv_imagem_perfilEditar = relativeLayoutEditarPerfil.findViewById(R.id.iv_imagem_perfilEditar);
        imagem_editar_foto = relativeLayoutEditarPerfil.findViewById(R.id.imagem_editar_foto);
        txtNameEditar = relativeLayoutEditarPerfil.findViewById(R.id.txtNameEditar);
        txtEmailEditar = relativeLayoutEditarPerfil.findViewById(R.id.txtEmailEditar);
        editTelefoneEditar = relativeLayoutEditarPerfil.findViewById(R.id.editTelefoneEditar);
        editCidadeEditar = relativeLayoutEditarPerfil.findViewById(R.id.editCidadeEditar);
        editMunicipioEditar = relativeLayoutEditarPerfil.findViewById(R.id.editMunicipioEditar);
        editRuaEditar = relativeLayoutEditarPerfil.findViewById(R.id.editRuaEditar);
        editGeneroEditar = relativeLayoutEditarPerfil.findViewById(R.id.editGeneroEditar);
        editDataNascEditar = relativeLayoutEditarPerfil.findViewById(R.id.editDataNascEditar);
        btnSalvarDados = relativeLayoutEditarPerfil.findViewById(R.id.btnSalvarDados);

        //clique
        iv_imagem_perfilEditar.setOnClickListener(MeuPerfilActivity.this);
        btnEditarPerfil.setOnClickListener(MeuPerfilActivity.this);
        btnCancelarEdicao.setOnClickListener(MeuPerfilActivity.this);
        btnSalvarDados.setOnClickListener(MeuPerfilActivity.this);
        btnCamara.setOnClickListener(MeuPerfilActivity.this);
        btnGaleria.setOnClickListener(MeuPerfilActivity.this);
        btnCancelar_dialog.setOnClickListener(MeuPerfilActivity.this);

        //carregar dados do Usuario
        loaduserProfile(AppDatabase.getUser());
    }

    private void loaduserProfile(Usuario usuario) {

        if (usuario != null) {
            id = usuario.getId_utilizador();

            txtName.setText(usuario.getNomeCliente());
            txtEmail.setText(usuario.getEmail());
            txtNameEditar.setText(usuario.getNomeCliente());
            txtEmailEditar.setText(usuario.getEmail());

            //Visualizar valores do usuario
            numeroTelef.setText(usuario.getTelefone());
            valorProvincia.setText(usuario.getProvincia());
            valorMunicipio.setText(usuario.getMunicipio());
            valorRua.setText(usuario.getRua());
            valorGenero.setText(usuario.getSexo());
            valorDataNasc.setText(usuario.getDataNascimento());

            //Visualizar valores ao editar INFO do usuario
            editTelefoneEditar.setText(usuario.getTelefone());
            editCidadeEditar.setText(usuario.getProvincia());
            editMunicipioEditar.setText(usuario.getMunicipio());
            editRuaEditar.setText(usuario.getRua());
            editGeneroEditar.setText(usuario.getSexo());
            editDataNascEditar.setText(usuario.getDataNascimento());

            if (usuario.getFoto() != null || !TextUtils.isEmpty(usuario.getFoto())) {
                Picasso.with(MeuPerfilActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfil);

                Picasso.with(MeuPerfilActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfilEditar);
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditarPerfil:
                setSupportActionBar(toolbar_editPerfil);
                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
                relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
                relativeLayoutMeuPerfil.setVisibility(View.GONE);
                btnEditarPerfil.setVisibility(View.GONE);
                break;
            case R.id.btnCancelarEdicao:
                setSupportActionBar(toolbar_meu_perfil);

                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
                esconderTeclado((Activity) view.getContext());
                relativeLayoutEditarPerfil.setVisibility(View.GONE);
                relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);
                btnEditarPerfil.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSalvarDados:
                if (verificarCampos()) {
                    alterarDados();
                }
                break;
            case R.id.iv_imagem_perfilEditar:
                verificarPermissaoFotoCameraGaleria();
                break;
            case R.id.btnCamara:
                caixa_dialogo_foto.dismiss();
                //pegarFotoCamara();
                break;
            case R.id.btnGaleria:
                caixa_dialogo_foto.dismiss();
                pegarFotoGaleria();
                break;
            case R.id.btnCancelar_dialog:
                caixa_dialogo_foto.dismiss();
                break;
        }
    }

        private void pegarFotoGaleria () {
            Intent galeria = new Intent();
            galeria.setAction(Intent.ACTION_GET_CONTENT);
            galeria.setType("image/*");
            startActivityForResult(galeria, ESCOLHER_FOTO_GALERIA);
        }

        private void verificarPermissaoFotoCameraGaleria () {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_FOTO);

            } else {
            }

            if (ContextCompat.checkSelfPermission(MeuPerfilActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MeuPerfilActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(view.getContext(),"Precisa aceitar as permissões para escolher uma foto de perfil",Toast.LENGTH_SHORT).show();
            } else {
                caixa_dialogo_foto.show();
            }
        }

        private void pegarFotoCamara () {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TIRAR_FOTO_CAMARA);
        }

        private void cortarImagemCrop (Uri imagemUri){
            CropImage.activity(imagemUri)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(MeuPerfilActivity.this);
        }

        public static void esconderTeclado (Activity activity){
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOLHER_FOTO_GALERIA && resultCode == RESULT_OK && data != null) {
            selectedImage = CropImage.getPickImageResultUri(MeuPerfilActivity.this, data);
            cortarImagemCrop(selectedImage);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.i("ressssErroResult", result.getUri().toString());
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String selectedFilePath = resultUri.getPath();
                iv_imagem_perfilEditar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(MeuPerfilActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("ressssErro", error.getMessage());
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.i("ressss", "entroueeeeeee");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    iv_imagem_perfilEditar.setImageBitmap(bitmap);
                    imagem_editar_foto.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == TIRAR_FOTO_CAMARA &&  resultCode == RESULT_OK  && data != null){
            selectedImage = CropImage.getPickImageResultUri(MeuPerfilActivity.this, data);
            cortarImagemCrop(selectedImage);
        }
    }

        private boolean verificaUriFoto(){
            return selectedImage != null;
        }

        private boolean verificarCampos () {

            telefone = editTelefoneEditar.getText().toString().trim();
            cidade = editCidadeEditar.getText().toString().trim();
            municipio = editMunicipioEditar.getText().toString().trim();
            rua = editRuaEditar.getText().toString().trim();
            genero = editGeneroEditar.getText().toString().trim();
            dataNasc = editDataNascEditar.getText().toString().trim();

            if (telefone.isEmpty()) {
                editTelefoneEditar.setError("Preencha o campo.");
                return false;
            }

            if (cidade.isEmpty()) {
                editCidadeEditar.setError("Preencha o campo.");
                return false;
            }

            if (municipio.isEmpty()) {
                editCidadeEditar.setError("Preencha o campo.");
                return false;
            }

            if (rua.isEmpty()) {
                editCidadeEditar.setError("Preencha o campo.");
                return false;
            }

            if (genero.isEmpty()) {
                editCidadeEditar.setError("Preencha o campo.");
                return false;
            }

            if (!genero.matches("Masculino|Feminino|masculino|feminino")) {
                editGeneroEditar.requestFocus();
                editGeneroEditar.setError("Preencha com Masculino ou Feminino");
                return false;
            }

            if (dataNasc.isEmpty()) {
                editCidadeEditar.setError("Preencha o campo.");
                return false;
            }

            return true;
        }

        private void alterarDados () {

            progressDialog.setMessage("Aguarde...!");
            progressDialog.show();
            erroLayout.setVisibility(View.GONE);
            relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
            ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
            Call<Void> call = apiInterface.atualizarDados(id, cidade, municipio, rua, genero, dataNasc, telefone);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(MeuPerfilActivity.this, "Os dados foram alterados com sucesso.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MeuPerfilActivity.this, "Algum problema ocorreu. Tente novamente!", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("sbaksanR", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    verifConecxao();
                    progressDialog.dismiss();
                    switch (t.getMessage()) {
                        case "timeout":
                            Toast.makeText(MeuPerfilActivity.this,
                                    "Impossivel se comunicar. Internet lenta.",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.d("errordaboy", t.getMessage());
                            Toast.makeText(MeuPerfilActivity.this,
                                    "Algum problema aconteceu. Tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

        private void verifConecxao () {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    mostarMsnErro();
                } else {
                    //Não aconteceu nada
                }
        }

        private void mostarMsnErro () {

            if (erroLayout.getVisibility() == View.GONE) {
                erroLayout.setVisibility(View.VISIBLE);
                relativeLayoutEditarPerfil.setVisibility(View.GONE);
            }

            btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
                    erroLayout.setVisibility(View.GONE);
                }
            });
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }}

