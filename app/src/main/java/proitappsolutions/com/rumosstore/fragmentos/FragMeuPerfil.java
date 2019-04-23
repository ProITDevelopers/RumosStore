package proitappsolutions.com.rumosstore.fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.Usuario;

public class FragMeuPerfil extends Fragment implements View.OnClickListener {

    private TextView txtName,txtEmail,numeroTelef,valorProvincia,valorMunicipio,valorBairro,
            valorRua,valorGenero,valorDataNasc,txtNameEditar,txtEmailEditar;
    private CircleImageView iv_imagem_perfil,iv_imagem_perfilEditar;
    private Button btnEditarPerfil,btnCancelarEdicao,btnSalvarDados;
    private RelativeLayout relativeLayoutMeuPerfil,relativeLayoutEditarPerfil;
    private String telefone,cidade,municipio,bairro,rua,genero,dataNasc;
    private AppCompatEditText editTelefoneEditar,editCidadeEditar,editMunicipioEditar,
            editBairroEditar,editRuaEditar,editGeneroEditar,editDataNascEditar;

    public FragMeuPerfil() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_meu_perfil, container, false);

        iv_imagem_perfil = view.findViewById(R.id.iv_imagem_perfil);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        numeroTelef = view.findViewById(R.id.numeroTelef);
        valorProvincia = view.findViewById(R.id.valorProvincia);
        valorMunicipio = view.findViewById(R.id.valorMunicipio);
        valorBairro = view.findViewById(R.id.valorBairro);
        valorRua = view.findViewById(R.id.valorRua);
        valorGenero = view.findViewById(R.id.valorGenero);
        valorDataNasc = view.findViewById(R.id.valorDataNasc);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
        btnCancelarEdicao = view.findViewById(R.id.btnCancelarEdicao);
        relativeLayoutMeuPerfil = view.findViewById(R.id.relativeLayoutMeuPerfil);
        relativeLayoutEditarPerfil = view.findViewById(R.id.relativeLayoutEditarPerfil);
        relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);

        loaduserProfile(AppDatabase.getUser());

        //editarPerfil layout
        editTelefoneEditar = relativeLayoutEditarPerfil.findViewById(R.id.editTelefoneEditar);
        editCidadeEditar = relativeLayoutEditarPerfil.findViewById(R.id.editCidadeEditar);
        editMunicipioEditar = relativeLayoutEditarPerfil.findViewById(R.id.editMunicipioEditar);
        editBairroEditar = relativeLayoutEditarPerfil.findViewById(R.id.editBairroEditar);
        editRuaEditar = relativeLayoutEditarPerfil.findViewById(R.id.editRuaEditar);
        editGeneroEditar = relativeLayoutEditarPerfil.findViewById(R.id.editGeneroEditar);
        editDataNascEditar = relativeLayoutEditarPerfil.findViewById(R.id.editDataNascEditar);
        btnSalvarDados = relativeLayoutEditarPerfil.findViewById(R.id.btnSalvarDados);

        //clique
        btnEditarPerfil.setOnClickListener(FragMeuPerfil.this);
        btnCancelarEdicao.setOnClickListener(FragMeuPerfil.this);
        btnSalvarDados.setOnClickListener(FragMeuPerfil.this);

        return view;

    }

    private void loaduserProfile(Usuario usuario){

        if (usuario !=null){

            if (usuario.getUsuarioLoginFrom().equals("userApi")){
                txtName.setText(usuario.getUsuarioNome());
                txtEmail.setText(usuario.getUsuarioEmail());
            } else {

                Picasso.with(getContext())
                        .load(usuario.getUsuarioPic())
                        .placeholder(R.drawable.ic_avatar)
                        .into(iv_imagem_perfil);

                txtName.setText(usuario.getUsuarioNome());
                txtEmail.setText(usuario.getUsuarioEmail());

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
                esconderTeclado(getActivity());
                relativeLayoutEditarPerfil.setVisibility(View.GONE);
                relativeLayoutMeuPerfil.setVisibility(View.VISIBLE);
                btnEditarPerfil.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSalvarDados:
                verificarCampos();
                break;
        }
    }

    public static void esconderTeclado(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private boolean verificarCampos(){

        telefone = editTelefoneEditar.getText().toString().trim();
        cidade = editCidadeEditar.getText().toString().trim();
        municipio = editMunicipioEditar.getText().toString().trim();
        bairro = editBairroEditar.getText().toString().trim();
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

        if (bairro.isEmpty()){
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

}
