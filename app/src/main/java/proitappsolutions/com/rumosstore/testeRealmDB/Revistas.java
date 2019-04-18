package proitappsolutions.com.rumosstore.testeRealmDB;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Revistas extends RealmObject {

    @PrimaryKey
    private int revistaID;
    private String revistaNome;
    @SerializedName("revistaLink")
    private String revistaLink;
    private float revistaPreco;
    private String revistaCategoria;

    public Revistas() {}

    public Revistas(int revistaID, String revistaNome, String revistaLink, float revistaPreco, String revistaCategoria) {
        this.revistaID = revistaID;
        this.revistaNome = revistaNome;
        this.revistaLink = revistaLink;
        this.revistaPreco = revistaPreco;
        this.revistaCategoria = revistaCategoria;
    }

    public int getRevistaID() {
        return revistaID;
    }

    public void setRevistaID(int revistaID) {
        this.revistaID = revistaID;
    }

    public String getRevistaNome() {
        return revistaNome;
    }

    public void setRevistaNome(String revistaNome) {
        this.revistaNome = revistaNome;
    }

    public String getRevistaLink() {
        return revistaLink;
    }

    public void setRevistaLink(String revistaLink) {
        this.revistaLink = revistaLink;
    }

    public float getRevistaPreco() {
        return revistaPreco;
    }

    public void setRevistaPreco(float revistaPreco) {
        this.revistaPreco = revistaPreco;
    }

    public String getRevistaCategoria() {
        return revistaCategoria;
    }

    public void setRevistaCategoria(String revistaCategoria) {
        this.revistaCategoria = revistaCategoria;
    }
}
