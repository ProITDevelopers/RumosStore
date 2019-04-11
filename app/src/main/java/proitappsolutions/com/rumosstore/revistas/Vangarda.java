package proitappsolutions.com.rumosstore.revistas;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Vangarda extends RealmObject {

    @PrimaryKey
    private int kiosqueID;
    private String Name;

    @SerializedName("produto_imageLink")
    private String ImageURL;

    private float preco;

    public Vangarda() {}

    public Vangarda(int kiosqueID, String name, String imageURL, float preco) {
        this.kiosqueID = kiosqueID;
        this.Name = name;
        this.ImageURL = imageURL;
        this.preco = preco;

    }

    public int getKiosqueID() {
        return kiosqueID;
    }

    public void setKiosqueID(int kiosqueID) {
        this.kiosqueID = kiosqueID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }
}
