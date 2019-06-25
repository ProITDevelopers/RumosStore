package proitappsolutions.com.rumosstore.QUIZ;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import proitappsolutions.com.rumosstore.QUIZ.Adaptador.AdapterResFinal;
import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Model.Estatistica;
import proitappsolutions.com.rumosstore.QUIZ.Model.QuestionStore;
import proitappsolutions.com.rumosstore.R;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore,getTxtResultQuestion,tv_quiz;
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterResFinal adapter;
    FirebaseDatabase database;
    DatabaseReference question_score,catgoria_estatistica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");
        catgoria_estatistica = database.getReference("Cat_Est");
        tv_quiz = findViewById(R.id.tv_quiz);
        txtResultScore = findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestion);
        progressBar = findViewById(R.id.doneProgressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(Done.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
                Common.questErradasList.clear();
            }
        });

        //Pega os dados do bundle
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("Pontuação : %d",score));
            getTxtResultQuestion.setText(String.format("Acertadas : %d / %d",correctAnswer,totalQuestion));

            if (Common.questErradasList.size()>0){
                adapter = new AdapterResFinal(Common.questErradasList,Done.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tv_quiz.setText("Perguntas Erradas - " + Common.questErradasList.size());
            }else {
                tv_quiz.setText("Perguntas Erradas - " + 0);
            }

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            Log.i("pontuacao",score + " pontos");
            question_score.child(String.format("%s_%s",Common.currentUser.getUserName(),Common.categoryId))
                    .setValue(new QuestionStore(String.format("%s_%s",Common.currentUser.getUserName(),
                            Common.categoryId),
                            Common.currentUser.getUserName(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));

            catgoria_estatistica
                    .child(String.valueOf(Common.categoryId))
                    .child(Common.currentUser.getUserName())
                    .setValue(new Estatistica(String.valueOf(correctAnswer),
                    String.valueOf(totalQuestion)));
        }
    }
}