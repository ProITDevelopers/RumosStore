package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class RecuperarSenha {

    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
