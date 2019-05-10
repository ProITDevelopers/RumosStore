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
}
