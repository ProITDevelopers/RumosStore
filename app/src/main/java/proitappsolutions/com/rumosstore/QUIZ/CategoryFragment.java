package proitappsolutions.com.rumosstore.QUIZ;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import proitappsolutions.com.rumosstore.QUIZ.Common.Common;
import proitappsolutions.com.rumosstore.QUIZ.Interface.ItemClickListener;
import proitappsolutions.com.rumosstore.QUIZ.Model.Category;
import proitappsolutions.com.rumosstore.QUIZ.ViewHolder.CategoryViewHolder;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.RegistroActivity;

import static proitappsolutions.com.rumosstore.communs.MetodosComuns.conexaoInternetTrafego;
import static proitappsolutions.com.rumosstore.communs.MetodosComuns.mostrarMensagem;

public class CategoryFragment extends Fragment {

    View myFragment;
    RecyclerView listCategory;
    ProgressBar progress_quiz;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter ;
    FirebaseDatabase database;
    DatabaseReference categories;

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.quiz_fragment_category,container,false);
        //Carrega o menu
        progress_quiz =  myFragment.findViewById(R.id.progress_quiz);
        progress_quiz.setVisibility(View.VISIBLE);
        listCategory =  myFragment.findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        listCategory.setLayoutManager(layoutManager);

        loadCategories();

        return myFragment;
    }

    private void loadCategories() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(categories.orderByChild("Status").equalTo("activo"), Category.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model) {
                progress_quiz.setVisibility(View.GONE);
                    holder.category_name.setText(model.getName());;
                try{
                    Glide
                            .with(getActivity())
                            .load(model.getImage()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.category_progress_bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.category_progress_bar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.category_image);
                }catch (Exception e){
                    Log.e("erro",e.getMessage());
                }

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent startGame = new Intent(getActivity(),Start.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName = model.getName();
                        startActivity(startGame);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.quiz_category_layout, viewGroup, false);
                return new CategoryViewHolder(view);
            }
        };
        listCategory.setAdapter(adapter);
        adapter.startListening();

    }

}

