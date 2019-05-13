package proitappsolutions.com.rumosstore.fragmentos;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.communs.RotateBitmap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class FragMeuPerfil extends Fragment implements View.OnClickListener {

    private static final int TIRAR_FOTO_CAMARA =  1, ESCOLHER_FOTO_GALERIA =  1995,PERMISSAO_FOTO =  3;
    private TextView txtName,txtEmail,numeroTelef,valorProvincia,valorMunicipio,
            valorRua,valorGenero,valorDataNasc,txtNameEditar,txtEmailEditar;
    private CircleImageView iv_imagem_perfil,iv_imagem_perfilEditar;
    private ImageView imagem_editar_foto;
    private Button btnEditarPerfil,btnCancelarEdicao,btnSalvarDados,btnCamara,btnGaleria,btnCancelar_dialog;
    private RelativeLayout relativeLayoutMeuPerfil,relativeLayoutEditarPerfil;
    private String telefone,cidade,municipio,rua,genero,dataNasc;
    private AppCompatEditText editTelefoneEditar,editCidadeEditar,editMunicipioEditar,
            editRuaEditar,editGeneroEditar,editDataNascEditar;
    private ProgressDialog progressDialog;
    private String id;
    private RelativeLayout erroLayout;
    private TextView btnVoltar;
    private Dialog caixa_dialogo_foto;
    private View view;
    private Uri selectedImage;
    private Bitmap bitmapImg;
    private String postPath;
    private Intent CropIntent;

    public FragMeuPerfil() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_meu_perfil, container, false);
        iv_imagem_perfil = view.findViewById(R.id.iv_imagem_perfil);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        numeroTelef = view.findViewById(R.id.numeroTelef);
        valorProvincia = view.findViewById(R.id.valorProvincia);
        valorMunicipio = view.findViewById(R.id.valorMunicipio);
        valorRua = view.findViewById(R.id.valorRua);
        valorGenero = view.findViewById(R.id.valorGenero);
        valorDataNasc = view.findViewById(R.id.valorDataNasc);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnCancelarEdicao = view.findViewById(R.id.btnCancelarEdicao);
        relativeLayoutMeuPerfil = view.findViewById(R.id.relativeLayoutMeuPerfil);
        relativeLayoutEditarPerfil = view.findViewById(R.id.relativeLayoutEditarPerfil);
        relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);
        erroLayout = view.findViewById(R.id.erroLayout);
        btnVoltar = view.findViewById(R.id.btn);
        btnVoltar.setText("Voltar");
        caixa_dialogo_foto = new Dialog(view.getContext());
        caixa_dialogo_foto.setContentView(R.layout.caixa_de_dialogo_foto);
        caixa_dialogo_foto.setCancelable(false);

        //Botão em caixa de dialogo foto
        btnCamara = caixa_dialogo_foto.findViewById(R.id.btnCamara);
        btnGaleria = caixa_dialogo_foto.findViewById(R.id.btnGaleria);
        btnCancelar_dialog = caixa_dialogo_foto.findViewById(R.id.btnCancelar_dialog);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        //editarPerfil layout
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
        iv_imagem_perfilEditar.setOnClickListener(FragMeuPerfil.this);
        btnEditarPerfil.setOnClickListener(FragMeuPerfil.this);
        btnCancelarEdicao.setOnClickListener(FragMeuPerfil.this);
        btnSalvarDados.setOnClickListener(FragMeuPerfil.this);
        btnCamara.setOnClickListener(FragMeuPerfil.this);
        btnGaleria.setOnClickListener(FragMeuPerfil.this);
        btnCancelar_dialog.setOnClickListener(FragMeuPerfil.this);

        //carregar dados do Usuario
        loaduserProfile(AppDatabase.getUser());

        return view;

    }

    private void loaduserProfile(Usuario usuario){

        if (usuario !=null){
            txtName.setText(usuario.getUsuarioNome());
            txtEmail.setText(usuario.getUsuarioEmail());
            txtNameEditar.setText(usuario.getUsuarioNome());
            txtEmailEditar.setText(usuario.getUsuarioEmail());
            id = usuario.getUsuarioId();

            if (usuario.getUsuarioPic()!=null || !TextUtils.isEmpty(usuario.getUsuarioPic())){
                Picasso.with(getContext())
                        .load(usuario.getUsuarioPic())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfil);
            }

        }


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnEditarPerfil:
                relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
                relativeLayoutMeuPerfil.setVisibility(View.GONE);
                btnEditarPerfil.setVisibility(View.GONE);
                break;
            case R.id.btnCancelarEdicao:
                esconderTeclado((Activity) view.getContext());
                relativeLayoutEditarPerfil.setVisibility(View.GONE);
                relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);
                btnEditarPerfil.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSalvarDados:
                if (verificarCampos()){
                    alterarDados();
                }
                break;
            case R.id.iv_imagem_perfilEditar:
                verificarPermissaoFotoCameraGaleria();
                break;
            case R.id.btnCamara:
                caixa_dialogo_foto.dismiss();
                pegarFotoCamara();
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

    private void pegarFotoGaleria() {
        Intent galeria = new Intent();
        galeria.setAction(Intent.ACTION_GET_CONTENT);
        galeria.setType("image/*");
        startActivityForResult(galeria,ESCOLHER_FOTO_GALERIA);
    }

    private void verificarPermissaoFotoCameraGaleria(){
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSAO_FOTO);

        }else {
        }

        if (ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(view.getContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(view.getContext(),"Precisa aceitar as permissões para escolher uma foto de perfil",Toast.LENGTH_SHORT).show();
        }else {
            caixa_dialogo_foto.show();
        }
    }

    private void pegarFotoCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,TIRAR_FOTO_CAMARA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOLHER_FOTO_GALERIA && resultCode == RESULT_OK && data != null){
            selectedImage = CropImage.getPickImageResultUri(view.getContext(),data);
            cortarImagemCrop(selectedImage);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String selectedFilePath = resultUri.getPath();
                iv_imagem_perfilEditar.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            Log.i("ressss","entroueeeeeee");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(),result.getUri());
                    iv_imagem_perfilEditar.setImageBitmap(bitmap);
                    imagem_editar_foto.setVisibility(View.GONE);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }else {
            Log.i("resss","erro" +  requestCode + "" + CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        if (requestCode==TIRAR_FOTO_CAMARA && data != null){
            Log.i("ressss","cameracap" +  requestCode + "" + CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                iv_imagem_perfilEditar.setImageBitmap(bitmap);
                imagem_editar_foto.setVisibility(View.GONE);
        }
    }

    private void cortarImagemCrop(Uri imagemUri){

        CropImage.activity(imagemUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start((Activity) view.getContext());
    }

    public static void esconderTeclado(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private boolean verificaUriFoto(){
        return selectedImage != null;
    }

    private boolean verificarCampos(){

        telefone = editTelefoneEditar.getText().toString().trim();
        cidade = editCidadeEditar.getText().toString().trim();
        municipio = editMunicipioEditar.getText().toString().trim();
        rua = editRuaEditar.getText().toString().trim();
        genero = editGeneroEditar.getText().toString().trim();
        dataNasc = editDataNascEditar.getText().toString().trim();

        if (telefone.isEmpty()){
            editTelefoneEditar.setError("Preencha o campo.");
            return false;
        }

        if (cidade.isEmpty()){
            editCidadeEditar.setError("Preencha o campo.");
            return false;
        }

        if (municipio.isEmpty()){
            editCidadeEditar.setError("Preencha o campo.");
            return false;
        }

        if (rua.isEmpty()){
            editCidadeEditar.setError("Preencha o campo.");
            return false;
        }

        if (genero.isEmpty()){
            editCidadeEditar.setError("Preencha o campo.");
            return false;
        }

        if (!genero.matches("Masculino|Feminino|masculino|feminino")){
            editGeneroEditar.requestFocus();
            editGeneroEditar.setError("Preencha com Masculino ou Feminino");
            return false;
        }

        if (dataNasc.isEmpty()){
            editCidadeEditar.setError("Preencha o campo.");
            return false;
        }

        return true;
    }

    private void alterarDados() {

        progressDialog.setMessage("Aguarde...!");
        progressDialog.show();
        erroLayout.setVisibility(View.GONE);
        relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<Void> call = apiInterface.atualizarDados(id,cidade,municipio,rua,genero,dataNasc,telefone);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Os dados foram alterados com sucesso.",Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Algum problema ocorreu. Tente novamente!",Toast.LENGTH_SHORT).show();
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
                switch (t.getMessage()){
                    case "timeout":
                        Toast.makeText(getContext(),
                                "Impossivel se comunicar. Internet lenta.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.d("errordaboy",t.getMessage());
                        Toast.makeText(getContext(),
                                "Algum problema aconteceu. Tente novamente.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void verifConecxao(){

        if (getActivity() != null){
            ConnectivityManager conMgr =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null){
                mostarMsnErro();
            }else{
            //Não aconteceu nada
            }
        }
    }

    private void mostarMsnErro(){

        if (erroLayout.getVisibility() == View.GONE){
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

}
