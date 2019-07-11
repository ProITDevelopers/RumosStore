package proitappsolutions.com.rumosstore.testeRealmDB;



import java.util.Comparator;



public class Revistas implements Comparator<Revistas> {


    public int id_jornal;
    public String nome;
    public String fotoJornal;
    public String link;
    public String categoria;
    public String dataEdicao;
    public String descricao;

    public Revistas() {}

    public Revistas(int id_jornal, String nome, String fotoJornal, String link, String categoria, String dataEdicao, String descricao) {
        this.id_jornal = id_jornal;
        this.nome = nome;
        this.fotoJornal = fotoJornal;
        this.link = link;
        this.categoria = categoria;
        this.dataEdicao = dataEdicao;
        this.descricao = descricao;
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

    public String getDataEdicao() {
        return dataEdicao;
    }

    public void setDataEdicao(String dataEdicao) {
        this.dataEdicao = dataEdicao;
    }

    @Override
    public int compare(Revistas o1, Revistas o2) {
        // Order ascending.
        int ret = o1.getDataEdicao().compareTo(o2.getDataEdicao());

        return ret;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
