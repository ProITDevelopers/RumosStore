package proitappsolutions.com.rumosstore.QUIZ;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Model.PerguntaErrada;
import proitappsolutions.com.rumosstore.R;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "PlayingDebug";
    final static long INTERVAL = 1000; //UM segundo
    final static long TIMEOUT = 15000; //SET segundo
    int progressValue = 0;
    String pergunta_passada;

    CountDownTimer mCountDown;

    int index=0,score=0,thisQuestion=0,totalQuestion,correctAnswer;

    ProgressBar progresssBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD;
    TextView txtScore,txtQuestionNum,question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_playing);

        //Views
        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestion);
        question_text = findViewById(R.id.question_text);
        question_image = findViewById(R.id.question_image);

        progresssBar = findViewById(R.id.progressBar);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(Playing.this);
        btnB.setOnClickListener(Playing.this);
        btnC.setOnClickListener(Playing.this);
        btnD.setOnClickListener(Playing.this);

        verifConecxao();
    }

    private void verifConecxao() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Log.i(TAG, "Internet Sem internet");
            mostrarMensagem(Playing.this,R.string.txtMsgErroRede);
        }
    }

    @Override
    public void onClick(View view) {

        mCountDown.cancel();

        if (index<totalQuestion){ //se ainda tiver elementos na lista
            Button clickedButton = (Button) view;
            pergunta_passada = clickedButton.getText().toString();
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())){
                //Escolhe a pergunta atual
                score +=10;
                correctAnswer++;
                Common.questPerguntaCerta.add(Common.questionList.get(index).getQuestion());
                showQuestion(++index); //Proxima questão
            }else {
                PerguntaErrada perguntaEoutros = new PerguntaErrada(
                        Common.questionList.get(index).getQuestion(),
                        clickedButton.getText().toString(),
                        Common.questionList.get(index).getCorrectAnswer()
                );
                Common.questErradasList.add(perguntaEoutros);
                showQuestion(++index);
            }
            txtScore.setText(String.format(Locale.US,"%d",score));
        }
    }
    //ATT CASO PROBLEMA AO MOSTRAR PERGUNTAS O index primeiro não estava a ser usado
    private void showQuestion(int index) {
        pergunta_passada = null;
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format(Locale.US,"%d / %d", thisQuestion, totalQuestion));
            progresssBar.setProgress(0);
            progressValue = 0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                //se existe imagem
                //Picasso.get().load(Common.questionList.get(index).getQuestion()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            } else {
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            mCountDown.start(); //Comecar timer
        } else {
           /* for (int i1 = 0; i1 < Common.questErradasList.size(); i1++) {
                Log.i(TAG, "Certa - " + Common.questErradasList.get(i1).getpCerta());
                Log.i(TAG, "Errada - " + Common.questErradasList.get(i1).getpErrada());
                Log.i(TAG, "Feita - " + Common.questErradasList.get(i1).getpFeita());
            } */
            //Se for a ultima questao
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion = Common.questionList.size();
        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long l) {
                progresssBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                PerguntaErrada perguntaEoutros = new PerguntaErrada(
                        Common.questionList.get(index).getQuestion(),
                        pergunta_passada,
                        Common.questionList.get(index).getCorrectAnswer()
                );
                Common.questErradasList.add(perguntaEoutros);
                showQuestion(++index);
            }
        };

        showQuestion(index);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDown.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDown.cancel();
        Log.i(TAG, "Activity destruida");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCountDown.cancel();
    }
}
