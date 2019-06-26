package proitappsolutions.com.rumosstore.QUIZ;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Interface.ItemClickListener;
import proitappsolutions.com.rumosstore.QUIZ.Interface.RankingCallBack;
import proitappsolutions.com.rumosstore.QUIZ.Model.QuestionStore;
import proitappsolutions.com.rumosstore.QUIZ.Model.Ranking;
import proitappsolutions.com.rumosstore.QUIZ.ViewHolder.RankingViewHolder;
import proitappsolutions.com.rumosstore.R;


public class RankingFragment extends Fragment {

    View myFragment;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter ;
    FirebaseDatabase database;
    ProgressBar progress_quiz_ranking;
    DatabaseReference questionScore,rankingTbl;
    int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.quiz_fragment_ranking,container,false);
        progress_quiz_ranking =  myFragment.findViewById(R.id.progress_quiz_ranking);
        progress_quiz_ranking.setVisibility(View.VISIBLE);
        rankingList =  myFragment.findViewById(R.id.rankingList);
        rankingList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        //rankingList.setLayoutManager(layoutManager);
        //Como o firebase vai organizar a lista do ascendente, então srá necessario reverter a
        //ordem do recyclerview
        //a partir do layoutManager
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //  showRanking(); //Depois de atalizar, mostrar o resultado do ranking ordenado
            }
        });

        FirebaseRecyclerOptions<Ranking> options =
                new FirebaseRecyclerOptions.Builder<Ranking>()
                        .setQuery(rankingTbl.orderByChild("score"), Ranking.class)
                        .build();


        //configurando o adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RankingViewHolder holder, int position, @NonNull final Ranking model) {
                progress_quiz_ranking.setVisibility(View.GONE);
                // Log.i("valormax",valorMaximo + "---" + model.getScore());
                holder.txt_name.setText(model.getUserName());
                holder.txt_score.setText(String.valueOf(model.getScore()));

                //resolvendo o erro quando o usario clica no item
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",model.getUserName());
                        startActivity(scoreDetail);
                    }
                });

            }

            @NonNull
            @Override
            public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.quiz_layout_ranking, viewGroup, false);
                return new RankingViewHolder(view);
            }
        };
        rankingList.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        adapter.startListening();
        
        return myFragment;
    }


    //Aqui vai se criar uma interface callback para processar o valor
    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        Log.i("valordoResultado","antes fo for" + "sum ->" + sum);
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Log.i("valordoResultado",data.getValue().getClass().getName() + "sum ->" + sum);
                            QuestionStore ques = data.getValue(QuestionStore.class);
                            sum+= Integer.valueOf(ques.getScore());
                        }
                        /*Depois de somar toda pontuacao, precisa-se processar o somatorio com variaveis aqui
                        porque o firebase é assincono e se processar fora a variavel 'sum' será redifinida para 0
                        */
                        Ranking ranking = new Ranking(userName,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
