package proitappsolutions.com.rumosstore.QUIZ.Model;

public class Estatistica {

    private String respostasCertas,totalPerguntas;

    public Estatistica() {
    }

    public Estatistica(String respostasCertas, String totalPerguntas) {
        this.respostasCertas = respostasCertas;
        this.totalPerguntas = totalPerguntas;
    }

    public String getRespostasCertas() {
        return respostasCertas;
    }

    public void setRespostasCertas(String respostasCertas) {
        this.respostasCertas = respostasCertas;
    }

    public String getTotalPerguntas() {
        return totalPerguntas;
    }

    public void setTotalPerguntas(String totalPerguntas) {
        this.totalPerguntas = totalPerguntas;
    }
}
