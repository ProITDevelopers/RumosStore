package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    private EmSessao emSessao;

    public Data() {
    }

    public EmSessao getEmSessao() {
        return emSessao;
    }

    public void setEmSessao(EmSessao emSessao) {
        this.emSessao = emSessao;
    }
}
