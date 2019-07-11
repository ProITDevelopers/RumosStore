package proitappsolutions.com.rumosstore.QUIZ;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.QUIZ.Adaptador.AdapterResFinal;
import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Model.Estatistica;
import proitappsolutions.com.rumosstore.QUIZ.Model.QuestionStore;
import proitappsolutions.com.rumosstore.R;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.txtPerguntasErradas;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore,getTxtResultQuestion,tv_quiz;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference question_score,catgoria_estatistica;
    Date c;
    SimpleDateFormat df;
    String dataAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_done);

        c = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        dataAtual = df.format(c);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");
        catgoria_estatistica = database.getReference("Estatisticas");
        tv_quiz = findViewById(R.id.tv_quiz);
        txtResultScore = findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestion);
        progressBar = findViewById(R.id.doneProgressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Done.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        btnTryAgain.setOnClickListener(view -> {
            Intent intent = new Intent(Done.this,Home.class);
            startActivity(intent);
            finish();
            Common.questErradasList.clear();
        });

        verifConecxao();

        //Pega os dados do bundle
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format(Locale.US,"Pontuação : %d",score));
            getTxtResultQuestion.setText(String.format(Locale.US,"Acertadas : %d / %d",correctAnswer,totalQuestion));

            if (Common.questErradasList.size()>0){
                AdapterResFinal adapter = new AdapterResFinal(Common.questErradasList, Done.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tv_quiz.setText(txtPerguntasErradas.concat(String.valueOf(Common.questErradasList.size())));

                //Estatistivas P Erradas
                for (int i1=0;i1<=Common.questionList.size()-1;i1++){
                    for (int i=0;i<=Common.questErradasList.size()-1;i++){
                      if (Common.questionList.get(i1).getQuestion().equals(Common.questErradasList.get(i).getpFeita())){
                          Estatistica estatistica = new Estatistica("errada",dataAtual);
                          catgoria_estatistica
                                  .child(String.valueOf(Common.categoryId))
                                  .child(Common.chaveDasPerguntas.get(i1))
                                  .push()
                          .setValue(estatistica).addOnCompleteListener(task -> {

                          });
                      }
                    }
                    //Estatistivas P CERTAS
                    for (int i=0;i<=Common.questPerguntaCerta.size()-1;i++){
                        if (Common.questionList.get(i1).getQuestion().equals(Common.questPerguntaCerta.get(i))){
                            Estatistica estatistica = new Estatistica("certa",dataAtual);
                            catgoria_estatistica
                                  .child(String.valueOf(Common.categoryId))
                                  .child(Common.chaveDasPerguntas.get(i1))
                                  .push()
                                  .setValue(estatistica);
                        }
                    }
                }
                Common.questPerguntaCerta.clear();
                //questPerguntaCerta
            }else {
                tv_quiz.setText(txtPerguntasErradas.concat("0"));

                //Estatistivas P CERTAS
                for (int i1=0;i1<=Common.questionList.size()-1;i1++){
                    for (int i=0;i<=Common.questPerguntaCerta.size()-1;i++){
                        if (Common.questionList.get(i1).getQuestion().equals(Common.questPerguntaCerta.get(i))){
                            Estatistica estatistica = new Estatistica("certa",dataAtual);
                            catgoria_estatistica
                                    .child(String.valueOf(Common.categoryId))
                                    .child(Common.chaveDasPerguntas.get(i1))
                                    .push()
                                    .setValue(estatistica);
                        }
                    }
                }
                Common.questPerguntaCerta.clear();
            }

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);
            try {
                String nome = AppDatabase.getInstance().getUser().nomeCliente;
                nome = nome.replace(" ","_");
                Log.i("pontuacao",score + " pontos");
                question_score.child(String.format("%s_%s",nome,Common.categoryId))
                        .setValue(new QuestionStore(String.format("%s_%s",nome,
                                Common.categoryId),
                                nome,
                                String.valueOf(score),
                                Common.categoryId,
                                Common.categoryName));
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }


    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            mostrarMensagem(Done.this,R.string.txtMsgErroRede);
        }
    }
}