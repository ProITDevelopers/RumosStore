package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class EmSessao {

    @SerializedName("nomeCliente")
    private String nome;
    @SerializedName("email")
    private String email;

    public EmSessao() {
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
}
