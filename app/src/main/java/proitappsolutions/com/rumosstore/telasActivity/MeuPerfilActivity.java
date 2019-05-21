package proitappsolutions.com.rumosstore.telasActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.MainActivity;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import proitappsolutions.com.rumosstore.communs.RotateBitmap;
import proitappsolutions.com.rumosstore.fragmentos.FragMeuPerfil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static proitappsolutions.com.rumosstore.telasActivity.ImagemPegarActivity.INTENT_ASPECT_RATIO_X;
import static proitappsolutions.com.rumosstore.telasActivity.ImagemPegarActivity.INTENT_ASPECT_RATIO_Y;
import static proitappsolutions.com.rumosstore.telasActivity.ImagemPegarActivity.INTENT_BITMAP_MAX_HEIGHT;
import static proitappsolutions.com.rumosstore.telasActivity.ImagemPegarActivity.INTENT_BITMAP_MAX_WIDTH;
import static proitappsolutions.com.rumosstore.telasActivity.ImagemPegarActivity.INTENT_LOCK_ASPECT_RATIO;
import static proitappsolutions.com.rumosstore.telasActivity.ImagemPegarActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT;

public class MeuPerfilActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private static final int TIRAR_FOTO_CAMARA = 1, ESCOLHER_FOTO_GALERIA = 1995, PERMISSAO_FOTO = 3;
    private TextView txtName, txtEmail, numeroTelef, valorProvincia, valorMunicipio,
            valorRua, valorGenero, valorDataNasc, txtNameEditar, txtEmailEditar;
    private CircleImageView iv_imagem_perfil, iv_imagem_perfilEditar;
    private ImageView imagem_editar_foto;
    private Button btnEditarPerfil, btnCancelarEdicao, btnSalvarDados, btnCamara, btnGaleria, btnCancelar_dialog;
    private RelativeLayout relativeLayoutMeuPerfil, relativeLayoutEditarPerfil;
    private String telefone, cidade, municipio, rua, genero, dataNasc;
    private Spinner editGeneroEditar;
    private AppCompatEditText editTelefoneEditar, editCidadeEditar, editMunicipioEditar,
            editRuaEditar, editDataNascEditar;
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
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private byte[] mUploadBytes;
    private String valorGeneroItem;
    ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);

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


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genero, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGeneroEditar.setAdapter(adapter);

        editDataNascEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MeuPerfilActivity.this,
                        R.style.DialogTheme,
                        mDateSetListener,
                        ano,mes,dia);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;

                String date = dia + "-" + mes + "-" + ano;
                editDataNascEditar.setText(date);
            }
        };

        //clique
        iv_imagem_perfilEditar.setOnClickListener(MeuPerfilActivity.this);
        btnEditarPerfil.setOnClickListener(MeuPerfilActivity.this);
        btnCancelarEdicao.setOnClickListener(MeuPerfilActivity.this);
        btnSalvarDados.setOnClickListener(MeuPerfilActivity.this);
        btnCamara.setOnClickListener(MeuPerfilActivity.this);
        editGeneroEditar.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) MeuPerfilActivity.this);
        btnGaleria.setOnClickListener(MeuPerfilActivity.this);
        btnCancelar_dialog.setOnClickListener(MeuPerfilActivity.this);

        //carregar dados do Usuario
        loaduserProfile(AppDatabase.getUser());
    }

    /*AppDatabase.getUser().getFoto() == null || AppDatabase.getUser().getSexo() == null ||
                        AppDatabase.getUser().getTelefone() == null || AppDatabase.getUser().getDataNascimento() == null ||
                        AppDatabase.getUser().getProvincia() == null || AppDatabase.getUser().getMunicipio() == null ||
                        AppDatabase.getUser().getRua() == null){*/

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
            if (usuario.getTelefone() != null){
                numeroTelef.setText(usuario.getTelefone());
                editTelefoneEditar.setText(usuario.getTelefone());
            }
            if (usuario.getProvincia() != null){
                valorProvincia.setText(usuario.getProvincia());
                editCidadeEditar.setText(usuario.getProvincia());
            }
            if (usuario.getMunicipio() != null){
                valorMunicipio.setText(usuario.getMunicipio());
                editMunicipioEditar.setText(usuario.getMunicipio());
            }
            if (usuario.getRua() != null){
                valorRua.setText(usuario.getRua());
                editRuaEditar.setText(usuario.getRua());
            }
            if (usuario.getSexo() != null){
                valorGenero.setText(usuario.getSexo());
                String genero = usuario.getSexo().toUpperCase();
                if (genero.equals("MASCULINO")){
                    editGeneroEditar.setSelection(1);
                }else {
                    editGeneroEditar.setSelection(0);
                }
            }
            if (usuario.getDataNascimento() != null) {
                Log.d("PERFIL",usuario.getDataNascimento());
                String resultado = usuario.getDataNascimento();
                String[] partes = resultado.split("-");
                String ano = partes[0];
                String mes = partes[1];
                String dia = partes[2];
                Log.d("snansa",ano + "---" + mes + "---" + dia.substring(0,2));
                Log.d("snansa",usuario.getDataNascimento());
                editDataNascEditar.setText(dia.substring(0,2)+"-"+mes+"-"+ano);
                valorDataNasc.setText(dia.substring(0,2)+"-"+mes+"-"+ano);
            }

            if (usuario.getFoto() != null || !TextUtils.isEmpty(usuario.getFoto())) {
                Picasso.with(MeuPerfilActivity.this)
                        .load(usuario.getFoto())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfil);
            }

            if (usuario.getFoto() != null || !TextUtils.isEmpty(usuario.getFoto())) {
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
                View viewAtual = this.getCurrentFocus();
                if (viewAtual != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    .setActivityTitle("RUMOSTORE")
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL)
                    .start(MeuPerfilActivity.this);
        }

        public static void esconderTeclado (Activity activity){
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
        }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOLHER_FOTO_GALERIA && resultCode == RESULT_OK && data != null) {
            Log.i("escolherFoto", "entroueeeeeee");
            selectedImage = CropImage.getPickImageResultUri(MeuPerfilActivity.this, data);
            cortarImagemCrop(selectedImage);
        }

       /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
        }*/

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.i("ressss", "entroueeeeeee");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && data !=null) {
                Uri selectedImage = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    iv_imagem_perfilEditar.setImageBitmap(bitmap);
                    imagem_editar_foto.setVisibility(View.GONE);
                    postPath = selectedImage.getPath();
                    Log.i("ressss", "entroueeeeeeeSalvando");
                    salvarFoto();
                } catch (IOException e) {
                    Log.i("ressss", "entroueeeeeeeSalvandoTRY");
                    e.printStackTrace();
                }
            }else {
                Log.i("ressss", "resultFalhou");
            }
        }else {
            Log.i("ressss", "erorr");
        }

        if (requestCode == TIRAR_FOTO_CAMARA &&  resultCode == RESULT_OK  && data != null){

            //selectedImage = data.getData();
            Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
            Log.i("urirranadka",data.getExtras().get("data") + "algumacoisa");
            //try {
                // You can update this bitmap to your server
                //bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Bitmap fotoReduzida = reduzirImagem(bitmap1,300);
                Log.i("urirranadka",bitmap1.getWidth() + "algumacoisa");
                iv_imagem_perfilEditar.setImageBitmap(fotoReduzida);
                imagem_editar_foto.setVisibility(View.GONE);

                Uri tempUri = getImageUri(getApplicationContext(), fotoReduzida);
                selectedImage = tempUri;
                //Uri tempUri = getImageUri(getApplicationContext(), fotoReduzida);
                /*File foto = salvarBitmap(fotoReduzida,tempUri.getPath());
                postPath = foto.getPath();*/
                salvarFotoComprimida(selectedImage,fotoReduzida);
                //salvarFoto();
            /*} catch (IOException e) {
                e.printStackTrace();
                Log.i("urirranadka",selectedImage + "nadaaa");
            }*/

        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }


    private boolean verificaUriFoto(){
            return selectedImage != null;
        }

        private boolean verificarCampos () {

            telefone = editTelefoneEditar.getText().toString().trim();
            cidade = editCidadeEditar.getText().toString().trim();
            municipio = editMunicipioEditar.getText().toString().trim();
            rua = editRuaEditar.getText().toString().trim();
            genero = valorGeneroItem;
            dataNasc = editDataNascEditar.getText().toString().trim();
            Log.i("datanasccc",dataNasc);
            String resultado = dataNasc;
            String[] partes = resultado.split("-");
            String dia = partes[0];
            String mes = partes[1];
            String ano = partes[2];
            dataNasc = ano+"-"+mes+"-"+dia;
            Log.d("snansaVxxxx",ano + "---" + mes + "---" + dia+ "----------------:>" + dataNasc);

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

            /*if (!genero.matches("Masculino|Feminino|masculino|feminino")) {
                editGeneroEditar.requestFocus();
                editGeneroEditar.setError("Preencha com Masculino ou Feminino");
                return false;
            }*/

            if (dataNasc.isEmpty()) {
                editCidadeEditar.setError("Preencha o campo.");
                return false;
            }

            return true;
        }

        private void salvarFoto(){
            File file = new File(postPath);
            RequestBody filepart = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)),file);
            MultipartBody.Part file1 = MultipartBody.Part.createFormData("imagem",file.getName(),filepart);

            if (verificaUriFoto()){
                progressDialog.setMessage("Salvando a foto de perfil.");
                progressDialog.show();
                Call<ResponseBody> enviarFoto = apiInterface.enviarFoto(Integer.valueOf(id),file1);

                enviarFoto.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MeuPerfilActivity.this,
                                    "Foto atualizada com sucesso.!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            /*Toast.makeText(MeuPerfilActivity.this,
                                    "errado",
                                    Toast.LENGTH_SHORT).show();*/
                            try {
                                Log.d("sbaksanR", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("sbaksan", response.code() + " dbsbfksbfks");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        verifConecxao();
                        progressDialog.dismiss();
                        switch (t.getMessage()) {
                            case "timeout":
                                Toast.makeText(MeuPerfilActivity.this,
                                        "Impossivel se comunicar. Internet lenta.",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.d("errordaboy", t.getMessage() + "fdbfjsbfkbs" + "FAILURE");
                                Toast.makeText(MeuPerfilActivity.this,
                                        "Algum problema aconteceu. Tente novamente.",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }

        }

        private void alterarDados () {

            progressDialog.setMessage("Aguarde...!");
            progressDialog.show();
            erroLayout.setVisibility(View.GONE);

            relativeLayoutEditarPerfil.setVisibility(View.VISIBLE);
            Call<Void> call = apiInterface.atualizarDados(id, cidade, municipio, rua, genero, dataNasc, telefone);

            //------------------------
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MeuPerfilActivity.this,HomeInicialActivity.class);
                        Toast.makeText(MeuPerfilActivity.this, "Os dados foram alterados com sucesso.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
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
                            Log.d("errordaboy", t.getMessage() + "SALVAR DADOS TXT");
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

        //------------------------------------------------------------------------
        private class BackgroundImageResize extends AsyncTask<Uri,Integer,byte[]> {

            Bitmap mBitmap;

            public BackgroundImageResize(Bitmap bitmap){
                if (bitmap != null){
                    this.mBitmap=bitmap;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(MeuPerfilActivity.this,"Comprimindo a imagem",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected byte[] doInBackground(Uri... uris) {
                Log.d("TAGdadakdnak","doInBackground: started");
                if (mBitmap ==null){
                    try {
                        //MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uris[0]);
                        RotateBitmap rotateBitmap = new RotateBitmap();
                        mBitmap = rotateBitmap.HandleSamplingAndRotationBitmap(MeuPerfilActivity.this,uris[0]);
                    }catch (IOException e){
                        Log.e("TAGBIGMAT","doInBackground: IOException: " + e.getMessage());
                    }
                }else {
                    Log.d("TAGEROOO","doInBackground: mwgabytes before compression: nagaaaa " );
                }
                byte[] bytes = null;
                Log.d("TAGEROOO","doInBackground: mwgabytes before compression: " + mBitmap.getByteCount() / 1000000);
                bytes = getBytesFromBitmap(mBitmap,50);
                Log.d("TAG","doInBackground: mwgabytes before compression: " + bytes.length / 1000000);
                return bytes;
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                mUploadBytes = bytes;

                //SALVAR FOTOOOOOO
                Bitmap imagemComprimida = BitmapFactory.decodeByteArray(mUploadBytes, 0, mUploadBytes.length);
                progressDialog.dismiss();
                Uri tempUri = getImageUri(getApplicationContext(), imagemComprimida);
                selectedImage = tempUri;
                //Uri tempUri = getImageUri(getApplicationContext(), fotoReduzida);
                File foto = salvarBitmap(imagemComprimida,tempUri.getPath());
                postPath = foto.getPath();
                //execute the upload task
                salvarFoto();
            }
        }
        //------------------------------------------------------------------------

    private void salvarFotoComprimida(Uri imagePath,Bitmap bitmap) {
        Log.d("TAG","uploadNewPhoto: uploading a new image uri to storage.");
        BackgroundImageResize resize = new BackgroundImageResize(bitmap);
        resize.execute(imagePath);

    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap,int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return stream.toByteArray();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        valorGeneroItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MeuPerfilActivity.this,HomeInicialActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }}

