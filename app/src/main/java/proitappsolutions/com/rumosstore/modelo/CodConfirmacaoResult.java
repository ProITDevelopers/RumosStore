package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class CodConfirmacaoResult {

    @SerializedName("token")
    private String token;

    @SerializedName("expiresIn")
    private String expiresIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
}
