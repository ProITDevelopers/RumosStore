package proitappsolutions.com.rumosstore.modelo;

import com.google.gson.annotations.SerializedName;

public class DataUserApi {

    @SerializedName("data")
    private DataDados dataDados;

    public DataUserApi() {
    }

    public DataUserApi(DataDados dataDados) {
        this.dataDados = dataDados;
    }

    public DataDados getDataDados() {
        return dataDados;
    }

    public void setDataDados(DataDados dataDados) {
        this.dataDados = dataDados;
    }
}
