package proitappsolutions.com.rumosstore;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import proitappsolutions.com.rumosstore.modelo.Data;

public class Usuario extends RealmObject {

    @PrimaryKey
    private String usuarioId;
    private String usuarioEmail;
    private String usuarioNome;
    @SerializedName("usuarioPic")
    private String usuarioPic;

    private String usuarioTelef,usuarioCidade,usuarioMunicipio,usuarioRua,usuarioGenero,usuarioDataNasc;
    private String id_utilizador,nomeCliente,email,foto,sexo,telefone,dataNascimento,provincia,municipio,rua;

    public Usuario(String id_utilizador, String nomeCliente, String email, String foto, String sexo,
                   String telefone, String dataNascimento, String provincia, String municipio, String rua) {
        this.id_utilizador = id_utilizador;
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.foto = foto;
        this.sexo = sexo;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.provincia = provincia;
        this.municipio = municipio;
        this.rua = rua;
    }

    public Usuario() {}

    public Usuario(String usuarioId, String usuarioPic) {
        this.usuarioId = usuarioId;
        this.usuarioPic = usuarioPic;
    }


    public Usuario(String usuarioId, String usuarioEmail, String usuarioNome) {
        this.usuarioId = usuarioId;
        this.usuarioEmail = usuarioEmail;
        this.usuarioNome = usuarioNome;
    }

    public Usuario(String usuarioId, String usuarioTelef, String usuarioGenero, String usuarioDataNasc, String usuarioCidade, String usuarioMunicipio, String usuarioRua) {
        this.usuarioId = usuarioId;
        this.usuarioTelef = usuarioTelef;
        this.usuarioGenero = usuarioGenero;
        this.usuarioDataNasc = usuarioDataNasc;
        this.usuarioCidade = usuarioCidade;
        this.usuarioMunicipio = usuarioMunicipio;
        this.usuarioRua = usuarioRua;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getUsuarioPic() {
        return usuarioPic;
    }

    public void setUsuarioPic(String usuarioPic) {
        this.usuarioPic = usuarioPic;
    }

    public String getUsuarioTelef() {
        return usuarioTelef;
    }

    public void setUsuarioTelef(String usuarioTelef) {
        this.usuarioTelef = usuarioTelef;
    }

    public String getUsuarioCidade() {
        return usuarioCidade;
    }

    public void setUsuarioCidade(String usuarioCidade) {
        this.usuarioCidade = usuarioCidade;
    }

    public String getUsuarioMunicipio() {
        return usuarioMunicipio;
    }

    public void setUsuarioMunicipio(String usuarioMunicipio) {
        this.usuarioMunicipio = usuarioMunicipio;
    }

    public String getUsuarioRua() {
        return usuarioRua;
    }

    public void setUsuarioRua(String usuarioRua) {
        this.usuarioRua = usuarioRua;
    }

    public String getUsuarioGenero() {
        return usuarioGenero;
    }

    public void setUsuarioGenero(String usuarioGenero) {
        this.usuarioGenero = usuarioGenero;
    }

    public String getUsuarioDataNasc() {
        return usuarioDataNasc;
    }

    public void setUsuarioDataNasc(String usuarioDataNasc) {
        this.usuarioDataNasc = usuarioDataNasc;
    }

    //---------------------------------------------------


    public String getId_utilizador() {
        return id_utilizador;
    }

    public void setId_utilizador(String id_utilizador) {
        this.id_utilizador = id_utilizador;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
}
