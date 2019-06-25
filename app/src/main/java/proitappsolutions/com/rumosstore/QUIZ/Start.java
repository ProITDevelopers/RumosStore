package proitappsolutions.com.rumosstore.QUIZ;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import dmax.dialog.SpotsDialog;
import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Model.Question;
import proitappsolutions.com.rumosstore.R;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_start);
        
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        dialog.setMessage("A carregar....");
        dialog.show();

        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
                finish();
            }
        });
        
        loadQuestion(Common.categoryId);
    }

    private void loadQuestion(String categoryId) {

        if (Common.questionList.size()>0)
            Common.questionList.clear(); //Primeiro limpar a lista se tiver valores

        questions.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionList.add(ques);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //Randomizar a lista
        Collections.shuffle(Common.questionList);

    }
}
