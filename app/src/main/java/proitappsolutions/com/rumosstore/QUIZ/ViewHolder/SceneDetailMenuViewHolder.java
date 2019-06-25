package proitappsolutions.com.rumosstore.QUIZ.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import proitappsolutions.com.rumosstore.R;

public class SceneDetailMenuViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_name,txt_score;

    public SceneDetailMenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_name = itemView.findViewById(R.id.txt_name);
        txt_score = itemView.findViewById(R.id.txt_score);
    }
}
