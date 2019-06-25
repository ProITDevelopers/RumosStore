package proitappsolutions.com.rumosstore.QUIZ.Model;

public class PerguntaErrada {


    String pFeita,pErrada,pCerta;

    public PerguntaErrada(String pFeita, String pErrada, String pCerta) {
        this.pFeita = pFeita;
        this.pErrada = pErrada;
        this.pCerta = pCerta;
    }

    public PerguntaErrada() {
    }

    public String getpFeita() {
        return pFeita;
    }

    public void setpFeita(String pFeita) {
        this.pFeita = pFeita;
    }

    public String getpErrada() {
        return pErrada;
    }

    public void setpErrada(String pErrada) {
        this.pErrada = pErrada;
    }

    public String getpCerta() {
        return pCerta;
    }

    public void setpCerta(String pCerta) {
        this.pCerta = pCerta;
    }
}
