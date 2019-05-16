package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class DataDados {

    @SerializedName("id_utilizador")
    private String id_utilizador;
    @SerializedName("nomeCliente")
    private String nomeCliente;
    @SerializedName("email")
    private String email;

    //--
    @SerializedName("foto")
    private String foto;
    @SerializedName("sexo")
    private String sexo;
    @SerializedName("telefone")
    private String telefone;
    //---
    @SerializedName("dataNascimento")
    private String dataNascimento;
    @SerializedName("provincia")
    private String provincia;
    @SerializedName("municipio")
    private String municipio;
    @SerializedName("rua")
    private String rua;

    public DataDados(String id_utilizador, String nomeCliente, String email,
                     String foto, String sexo, String telefone,
                     String dataNascimento, String provincia, String municipio, String rua) {
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

    public DataDados() {
    }

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
