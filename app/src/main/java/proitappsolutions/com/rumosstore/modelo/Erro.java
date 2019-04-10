package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class Erro {

    @SerializedName("error")
    private String mensagem;

    public Erro() {
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
