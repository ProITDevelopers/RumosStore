package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class EmSessao {

    @SerializedName("nomeCliente")
    private String nome;
    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private String id;

    public EmSessao() {
    }

    public EmSessao(String nome, String email, String id) {
        this.nome = nome;
        this.email = email;
        this.id = id;
    }

    public EmSessao(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
