package proitappsolutions.com.rumosstore.telasActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.RegistroActivity;
import proitappsolutions.com.rumosstore.modelo.Usuario;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorResponce;
import proitappsolutions.com.rumosstore.api.erroApi.ErrorUtils;
import proitappsolutions.com.rumosstore.communs.MetodosComuns;
import proitappsolutions.com.rumosstore.communs.RotateBitmap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.conexaoInternetTrafego;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgAprocessar;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgDadosAlterados;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgErro;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgSalvandoFoto;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.msgVoltar;

public class MeuPerfilActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String TAG = "MeuPerfilActivityDebug";
    private static final int TIRAR_FOTO_CAMARA = 1, ESCOLHER_FOTO_GALERIA = 1995, PERMISSAO_FOTO = 3;
    private TextView txtName, txtEmail, numeroTelef, valorProvincia, valorMunicipio,
            valorRua, valorGenero, valorDataNasc, txtNameEditar, txtEmailEditar, tv_inicial_nome, tv_inicial_nome_edit;
    private CircleImageView iv_imagem_perfil, iv_imagem_perfilEditar;
    private ImageView imagem_editar_foto;
    private Button btnEditarPerfil;
    private RelativeLayout relativeLayoutMeuPerfil, relativeLayoutEditarPerfil;
    private String telefone, cidade, municipio, rua, genero, dataNasc;
    private Spinner editGeneroEditar,editCidadeEditar;
    private AppCompatEditText editTelefoneEditar, editMunicipioEditar,
            editRuaEditar, editDataNascEditar;
    private ProgressDialog progressDialog;
    private String id;
    private RelativeLayout erroLayout;
    private TextView btnVoltar;
    private Dialog caixa_dialogo_foto;
    private Uri selectedImage;
    private String postPath;
    private Toolbar toolbar_meu_perfil;
    private Toolbar toolbar_editPerfil;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String valorGeneroItem,valorCidadeItem;
    ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
    ArrayAdapter<CharSequence> adapterCidade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        toolbar_meu_perfil = findViewById(R.id.toolbar_meu_perfil);
        toolbar_meu_perfil.setTitle("Meu Perfil");
        setSupportActionBar(toolbar_meu_perfil);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        iv_imagem_perfil = findViewById(R.id.iv_imagem_perfil);
        txtName = findViewById(R.id.txtName);
        tv_inicial_nome = findViewById(R.id.tv_inicial_nome);
        txtEmail = findViewById(R.id.txtEmail);
        numeroTelef = findViewById(R.id.numeroTelef);
        valorProvincia = findViewById(R.id.valorProvincia);
        valorMunicipio = findViewById(R.id.valorMunicipio);
        valorRua = findViewById(R.id.valorRua);
        valorGenero = findViewById(R.id.valorGenero);
        valorDataNasc = findViewById(R.id.valorDataNasc);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        Button btnCancelarEdicao = findViewById(R.id.btnCancelarEdicao);
        relativeLayoutMeuPerfil = findViewById(R.id.relativeLayoutMeuPerfil);
        relativeLayoutEditarPerfil = findViewById(R.id.relativeLayoutEditarPerfil);
        relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);
        erroLayout = findViewById(R.id.erroLayout);
        btnVoltar = findViewById(R.id.btn);
        btnVoltar.setText(msgVoltar);
        caixa_dialogo_foto = new Dialog(MeuPerfilActivity.this);
        caixa_dialogo_foto.setContentView(R.layout.caixa_de_dialogo_foto);
        caixa_dialogo_foto.setCancelable(false);

        //Botão em caixa de dialogo foto
        Button btnCamara = caixa_dialogo_foto.findViewById(R.id.btnCamara);
        Button btnGaleria = caixa_dialogo_foto.findViewById(R.id.btnGaleria);
        Button btnCancelar_dialog = caixa_dialogo_foto.findViewById(R.id.btnCancelar_dialog);

        progressDialog = new ProgressDialog(MeuPerfilActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);

        //editarPerfil layout
        toolbar_editPerfil = relativeLayoutEditarPerfil.findViewById(R.id.toolbar_editPerfil);
        iv_imagem_perfilEditar = relativeLayoutEditarPerfil.findViewById(R.id.iv_imagem_perfilEditar);
        tv_inicial_nome_edit = relativeLayoutEditarPerfil.findViewById(R.id.tv_inicial_nome_edit);
        imagem_editar_foto = relativeLayoutEditarPerfil.findViewById(R.id.imagem_editar_foto);
        txtNameEditar = relativeLayoutEditarPerfil.findViewById(R.id.txtNameEditar);
        txtEmailEditar = relativeLayoutEditarPerfil.findViewById(R.id.txtEmailEditar);
        editTelefoneEditar = relativeLayoutEditarPerfil.findViewById(R.id.editTelefoneEditar);
        editMunicipioEditar = relativeLayoutEditarPerfil.findViewById(R.id.editMunicipioEditar);
        editRuaEditar = relativeLayoutEditarPerfil.findViewById(R.id.editRuaEditar);
        editCidadeEditar = relativeLayoutEditarPerfil.findViewById(R.id.editCidadeSpiner);
        editGeneroEditar = relativeLayoutEditarPerfil.findViewById(R.id.editGeneroEditar);
        editDataNascEditar = relativeLayoutEditarPerfil.findViewById(R.id.editDataNascEditar);
        Button btnSalvarDados = relativeLayoutEditarPerfil.findViewById(R.id.btnSalvarDados);

        adapterCidade = ArrayAdapter.createFromResource(MeuPerfilActivity.this,
                R.array.cidade, android.R.layout.simple_spinner_item);
        adapterCidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCidadeEditar.setAdapter(adapterCidade);
        editCidadeEditar.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genero, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGeneroEditar.setAdapter(adapter);

        editDataNascEditar.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int ano = cal.get(Calendar.YEAR);
            int mes = cal.get(Calendar.MONTH);
            int dia = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    MeuPerfilActivity.this,
                    R.style.DialogTheme,
                    mDateSetListener,
                    ano, mes, dia);
            dialog.show();
        });

        mDateSetListener = (datePicker, ano, mes, dia) -> {
            mes = mes + 1;
            String date = dia + "-" + mes + "-" + ano;
            editDataNascEditar.setText(date);
        };

        //clique
        iv_imagem_perfilEditar.setOnClickListener(MeuPerfilActivity.this);
        btnEditarPerfil.setOnClickListener(MeuPerfilActivity.this);
        btnCancelarEdicao.setOnClickListener(MeuPerfilActivity.this);
        btnSalvarDados.setOnClickListener(MeuPerfilActivity.this);
        btnCamara.setOnClickListener(MeuPerfilActivity.this);
        editGeneroEditar.setOnItemSelectedListener(MeuPerfilActivity.this);
        btnGaleria.setOnClickListener(MeuPerfilActivity.this);
        btnCancelar_dialog.setOnClickListener(MeuPerfilActivity.this);

        //carregar dados do Usuario
        Common.mCurrentUser = AppDatabase.getInstance().getUser();
        loaduserProfile(Common.mCurrentUser);
    }

    private void loaduserProfile(Usuario usuario) {

        if (usuario != null) {
            txtName.setText(usuario.getNomeCliente());
            txtEmail.setText(usuario.getEmail());
            txtNameEditar.setText(usuario.getNomeCliente());
            txtEmailEditar.setText(usuario.getEmail());
            id = usuario.getId_utilizador();

            iv_imagem_perfilEditar = relativeLayoutEditarPerfil.findViewById(R.id.iv_imagem_perfilEditar);
            imagem_editar_foto = relativeLayoutEditarPerfil.findViewById(R.id.imagem_editar_foto);
            txtEmailEditar = relativeLayoutEditarPerfil.findViewById(R.id.txtEmailEditar);

            //--
            if (usuario.getTelefone() != null) {
                numeroTelef.setText(usuario.getTelefone());
                editTelefoneEditar.setText(usuario.getTelefone());
            }
            if (usuario.getProvincia() != null) {
                valorProvincia.setText(usuario.getProvincia());
                String valorCidade = usuario.getProvincia();
                /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cidades, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
                editCidadeEditar.setAdapter(adapterCidade);
                if (valorCidade != null) {
                    int posicaoValor = adapterCidade.getPosition(valorCidade);
                    editCidadeEditar.setSelection(posicaoValor);
                }
            }
            if (usuario.getMunicipio() != null) {
                valorMunicipio.setText(usuario.getMunicipio());
                editMunicipioEditar.setText(usuario.getMunicipio());
            }
            if (usuario.getRua() != null) {
                valorRua.setText(usuario.getRua());
                editRuaEditar.setText(usuario.getRua());
            }
            if (usuario.getSexo() != null) {
                valorGenero.setText(usuario.getSexo());
                String genero = usuario.getSexo().toUpperCase();
                if (genero.equals("MASCULINO")) {
                    editGeneroEditar.setSelection(1);
                } else {
                    editGeneroEditar.setSelection(0);
                }
            }
            if (usuario.getDataNascimento() != null) {
                String resultado = usuario.getDataNascimento();
                String[] partes = resultado.split("-");
                String ano = partes[0];
                String mes = partes[1];
                String dia = partes[2];
                String valorFormatado = dia.substring(0, 2) + "-" + mes + "-" + ano;
                editDataNascEditar.setText(valorFormatado);
                valorDataNasc.setText(valorFormatado);
            }

            if (usuario.getFoto() != null || !TextUtils.isEmpty(usuario.getFoto())) {
                tv_inicial_nome_edit.setVisibility(View.GONE);
                Picasso.with(MeuPerfilActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfil);
            } else {
                tv_inicial_nome_edit.setText(String.valueOf(usuario.getNomeCliente().charAt(0)).toUpperCase());
            }

            if (usuario.getFoto() != null || !TextUtils.isEmpty(usuario.getFoto())) {
                tv_inicial_nome.setVisibility(View.GONE);
                Picasso.with(MeuPerfilActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfilEditar);
            } else {
                tv_inicial_nome.setText(String.valueOf(usuario.getNomeCliente().charAt(0)).toUpperCase());
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditarPerfil:
                setSupportActionBar(toolbar_editPerfil);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
                relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
                relativeLayoutMeuPerfil.setVisibility(View.GONE);
                btnEditarPerfil.setVisibility(View.GONE);
                break;
            case R.id.btnCancelarEdicao:
                setSupportActionBar(toolbar_meu_perfil);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
                View viewAtual = this.getCurrentFocus();
                if (viewAtual != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
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
        startActivityForResult(galeria, ESCOLHER_FOTO_GALERIA);
    }

    private void verificarPermissaoFotoCameraGaleria() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_FOTO);
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

    private void pegarFotoCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TIRAR_FOTO_CAMARA);
    }

    private void cortarImagemCrop(Uri imagemUri) {
        CropImage.activity(imagemUri)
                .setActivityTitle("RUMOSTORE")
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL)
                .start(MeuPerfilActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOLHER_FOTO_GALERIA && resultCode == RESULT_OK && data != null) {
            selectedImage = CropImage.getPickImageResultUri(MeuPerfilActivity.this, data);
            cortarImagemCrop(selectedImage);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    iv_imagem_perfilEditar.setImageBitmap(bitmap);
                    imagem_editar_foto.setVisibility(View.GONE);
                    postPath = selectedImage.getPath();
                    salvarFoto();
                } catch (IOException e) {
                    Log.i(TAG, "ERRO CROPIMAGE" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == TIRAR_FOTO_CAMARA && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                Bitmap fotoReduzida = reduzirImagem(bitmap1, 300);
                Log.i("urirranadka", bitmap1.getWidth() + "algumacoisa");
                iv_imagem_perfilEditar.setImageBitmap(fotoReduzida);
                imagem_editar_foto.setVisibility(View.GONE);

                selectedImage = getImageUri(getApplicationContext(), fotoReduzida);
                salvarFotoComprimida(selectedImage, fotoReduzida);
            } catch (Exception e) {
                Log.i(TAG, "Erro onActivityResult" + e.getMessage());
            }

        }
    }

    private File salvarBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "foto_mediaRumo", null);
        return Uri.parse(path);
    }

    public Bitmap reduzirImagem(Bitmap image, int maxSize) {
        int width = 10;
        int height = 10;

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    private boolean verificaUriFoto() {
        return selectedImage != null;
    }

    private boolean verificarCampos() {

        try {
            telefone = editTelefoneEditar.getText().toString().trim();
            cidade = valorCidadeItem;
            municipio = editMunicipioEditar.getText().toString().trim();
            rua = editRuaEditar.getText().toString().trim();
            genero = valorGeneroItem;
            dataNasc = editDataNascEditar.getText().toString().trim();
        } catch (Exception e) {
            Log.i(TAG, "erroVerifCampo" + e.getMessage());
        }

        //Se dataNasc estiver vazia -> String[] partes gera Array_IndexOutofbounds_Exception
        if (!dataNasc.equals("") || !TextUtils.isEmpty(dataNasc)) {
            String resultado = dataNasc;
            String[] partes = resultado.split("-");
            String dia = partes[0];
            String mes = partes[1];
            String ano = partes[2];
            dataNasc = ano + "-" + mes + "-" + dia;
        }

        if (telefone.isEmpty()) {
            editTelefoneEditar.setError(msgErro);
            return false;
        }


        if (municipio.isEmpty()) {
            editMunicipioEditar.setError(msgErro);
            return false;
        }

        if (rua.isEmpty()) {
            editRuaEditar.setError(msgErro);
            return false;
        }


        return true;
    }

    private void salvarFoto() {
        tv_inicial_nome_edit.setVisibility(View.GONE);
        File file = new File(postPath);
        try {
            RequestBody filepart = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)), file);
            MultipartBody.Part file1 = MultipartBody.Part.createFormData("imagem", file.getName(), filepart);
            if (verificaUriFoto()) {
                progressDialog.setMessage(msgSalvandoFoto);
                progressDialog.show();
                Call<ResponseBody> enviarFoto = apiInterface.enviarFoto(Integer.valueOf(id), file1);

                enviarFoto.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            mostrarMensagem(MeuPerfilActivity.this, R.string.txtFotoAtualizada);
                            tv_inicial_nome.setVisibility(View.INVISIBLE);
                            tv_inicial_nome_edit.setVisibility(View.INVISIBLE);
                        } else {
                            progressDialog.dismiss();
                            ErrorResponce errorResponce = ErrorUtils.parseError(response);
                            mostrarMensagem(MeuPerfilActivity.this, errorResponce.getError());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        verifConecxao();
                        progressDialog.dismiss();
                        if (!conexaoInternetTrafego(MeuPerfilActivity.this)) {
                            mostrarMensagem(MeuPerfilActivity.this, R.string.txtMsg);
                        } else if ("timeout".equals(t.getMessage())) {
                            mostrarMensagem(MeuPerfilActivity.this, R.string.txtTimeout);
                        } else {
                            mostrarMensagem(MeuPerfilActivity.this, R.string.txtProblemaMsg);
                        }
                        Log.i(TAG, "onFailure" + t.getMessage());
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void alterarDados() {

        progressDialog.setMessage(msgAprocessar);
        progressDialog.show();
        erroLayout.setVisibility(View.GONE);
        relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
        Call<Void> call = apiInterface.atualizarDados(id, cidade, municipio, rua, genero, dataNasc, telefone);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(MeuPerfilActivity.this, HomeInicialActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mostrarMensagem(MeuPerfilActivity.this, msgDadosAlterados);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    ErrorResponce errorResponce = ErrorUtils.parseError(response);
                    mostrarMensagem(MeuPerfilActivity.this, errorResponce.getError());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                verifConecxao();
                if (!conexaoInternetTrafego(MeuPerfilActivity.this)) {
                    mostrarMensagem(MeuPerfilActivity.this, R.string.txtMsg);
                } else if ("timeout".equals(t.getMessage())) {
                    mostrarMensagem(MeuPerfilActivity.this, R.string.txtTimeout);
                } else {
                    mostrarMensagem(MeuPerfilActivity.this, R.string.txtProblemaMsg);
                }
                Log.i(TAG, "onFailure" + t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void verifConecxao() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            mostarMsnErro();
        } else {
            if (!MetodosComuns.temTrafegoNaRede(MeuPerfilActivity.this))
                MetodosComuns.mostrarMensagem(MeuPerfilActivity.this, R.string.txtMsg);
            else
                MetodosComuns.mostrarMensagem(MeuPerfilActivity.this, R.string.txtProblemaMsg);
        }
    }

    private void mostarMsnErro() {

        if (erroLayout.getVisibility() == View.GONE) {
            erroLayout.setVisibility(View.VISIBLE);
            relativeLayoutEditarPerfil.setVisibility(View.GONE);
        }

        btnVoltar.setOnClickListener(view -> {
            relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
            erroLayout.setVisibility(View.GONE);
        });
    }

    //------------------------------------------------------------------------
    private class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if (bitmap != null) {
                this.mBitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MeuPerfilActivity.this, "Comprimindo a imagem", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.d("TAGdadakdnak", "doInBackground: started");
            if (mBitmap == null) {
                try {
                    //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uris[0]);
                    RotateBitmap rotateBitmap = new RotateBitmap();
                    mBitmap = rotateBitmap.HandleSamplingAndRotationBitmap(MeuPerfilActivity.this, uris[0]);
                } catch (IOException e) {
                    Log.e("TAGBIGMAT", "doInBackground: IOException: " + e.getMessage());
                }
            } else {
                Log.d("TAGEROOO", "doInBackground: mwgabytes before compression: nagaaaa ");
            }
            byte[] bytes = null;
            Log.d("TAGEROOO", "doInBackground: mwgabytes before compression: " + mBitmap.getByteCount() / 1000000);
            bytes = getBytesFromBitmap(mBitmap, 50);
            Log.d("TAG", "doInBackground: mwgabytes before compression: " + bytes.length / 1000000);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            byte[] mUploadBytes = bytes;

            //SALVAR FOTOOOOOO
            Bitmap imagemComprimida = BitmapFactory.decodeByteArray(mUploadBytes, 0, mUploadBytes.length);
            progressDialog.dismiss();
            Uri tempUri = getImageUri(getApplicationContext(), imagemComprimida);
            selectedImage = tempUri;
            //Uri tempUri = getImageUri(getApplicationContext(), fotoReduzida);
            File foto = salvarBitmap(imagemComprimida, tempUri.getPath());
            postPath = foto.getPath();
            //execute the upload task
            salvarFoto();
        }
    }
    //------------------------------------------------------------------------

    private void salvarFotoComprimida(Uri imagePath, Bitmap bitmap) {
        Log.d("TAG", "uploadNewPhoto: uploading a new image uri to storage.");
        BackgroundImageResize resize = new BackgroundImageResize(bitmap);
        resize.execute(imagePath);

    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //parent.getItemAtPosition(position);
        /*Toast.makeText(MeuPerfilActivity.this,"lwslasadadada",Toast.LENGTH_SHORT).show();
        Log.i("valorpos",position + "");
        Log.i("valorpos",parent.getId() + "");*/
        switch (parent.getId())
        {
            case R.id.editCidadeSpiner:
                valorCidadeItem = parent.getItemAtPosition(position).toString();
                break;

            case R.id.editGeneroEditar:
                valorGeneroItem = parent.getItemAtPosition(position).toString();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MeuPerfilActivity.this, HomeInicialActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MeuPerfilActivity.this, HomeInicialActivity.class);
        startActivity(intent);
        finish();
    }
}

