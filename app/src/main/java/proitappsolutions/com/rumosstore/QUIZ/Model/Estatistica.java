package proitappsolutions.com.rumosstore.QUIZ.Model;

public class Estatistica {

    private String estado,dataAtual;

    public Estatistica(String estado, String dataAtual) {
        this.estado = estado;
        this.dataAtual = dataAtual;
    }

    public Estatistica() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(String dataAtual) {
        this.dataAtual = dataAtual;
    }
}
