package proitappsolutions.com.rumosstore.testeRealmDB;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Revistas extends RealmObject {

    @PrimaryKey
    private int id_jornal;
    private String nome;
    @SerializedName("fotoJornal")
    private String fotoJornal;
    @SerializedName("link")
    private String link;
    private String categoria;

    public Revistas() {}

    public Revistas(int id_jornal, String nome, String fotoJornal, String link, String categoria) {
        this.id_jornal = id_jornal;
        this.nome = nome;
        this.fotoJornal = fotoJornal;
        this.link = link;
        this.categoria = categoria;
    }

    public int getId_jornal() {
        return id_jornal;
    }

    public void setId_jornal(int id_jornal) {
        this.id_jornal = id_jornal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFotoJornal() {
        return fotoJornal;
    }

    public void setFotoJornal(String fotoJornal) {
        this.fotoJornal = fotoJornal;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    //    @PrimaryKey
//    private int revistaID;
//    private String revistaNome;
//    @SerializedName("revistaLink")
//    private String revistaLink;
//    @SerializedName("revistaPDFLink")
//    private String revistaPDFLink;
//    private String revistaCategoria;
//
//    public Revistas() {}
//
//    public Revistas(int revistaID, String revistaNome, String revistaLink, String revistaPDFLink, String revistaCategoria) {
//        this.revistaID = revistaID;
//        this.revistaNome = revistaNome;
//        this.revistaLink = revistaLink;
//        this.revistaPDFLink = revistaPDFLink;
//        this.revistaCategoria = revistaCategoria;
//    }
//
//    public int getRevistaID() {
//        return revistaID;
//    }
//
//    public void setRevistaID(int revistaID) {
//        this.revistaID = revistaID;
//    }
//
//    public String getRevistaNome() {
//        return revistaNome;
//    }
//
//    public void setRevistaNome(String revistaNome) {
//        this.revistaNome = revistaNome;
//    }
//
//    public String getRevistaLink() {
//        return revistaLink;
//    }
//
//    public void setRevistaLink(String revistaLink) {
//        this.revistaLink = revistaLink;
//    }
//
//    public String getRevistaPDFLink() {
//        return revistaPDFLink;
//    }
//
//    public void setRevistaPDFLink(String revistaPDFLink) {
//        this.revistaPDFLink = revistaPDFLink;
//    }
//
//    public String getRevistaCategoria() {
//        return revistaCategoria;
//    }
//
//    public void setRevistaCategoria(String revistaCategoria) {
//        this.revistaCategoria = revistaCategoria;
//    }
}
