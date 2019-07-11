package proitappsolutions.com.rumosstore.QUIZ;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import proitappsolutions.com.rumosstore.QUIZ.Model.QuestionStore;
import proitappsolutions.com.rumosstore.QUIZ.ViewHolder.SceneDetailMenuViewHolder;
import proitappsolutions.com.rumosstore.R;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionStore, SceneDetailMenuViewHolder> adapter ;

    String viewUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_score_detail);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        scoreList = findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);

        if (getIntent() != null){
            viewUser = getIntent().getStringExtra("viewUser");
            if (!viewUser.isEmpty()){
                loadScoreDetail(viewUser);
            }
        }
    }

    private void loadScoreDetail(String viewUser) {

        FirebaseRecyclerOptions<QuestionStore> options =
                new FirebaseRecyclerOptions.Builder<QuestionStore>()
                        .setQuery(question_score.orderByChild("user").equalTo(viewUser), QuestionStore.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<QuestionStore, SceneDetailMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SceneDetailMenuViewHolder holder, int position, @NonNull QuestionStore model) {
                holder.txt_name.setText(model.getCategoryName());
                holder.txt_score.setText(model.getScore());
            }

            @NonNull
            @Override
            public SceneDetailMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.quiz_score_detail_layout, viewGroup, false);
                return new SceneDetailMenuViewHolder(view);
            }
        };
        scoreList.setAdapter(adapter);
        adapter.startListening();

    }
}
