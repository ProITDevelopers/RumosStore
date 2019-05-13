package proitappsolutions.com.rumosstore.testeRealmDB;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.AppDatabase;
import proitappsolutions.com.rumosstore.Common;
import proitappsolutions.com.rumosstore.R;
import proitappsolutions.com.rumosstore.api.ApiClient;
import proitappsolutions.com.rumosstore.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragTeste extends Fragment {

    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_teste, container, false);

        textView = view.findViewById(R.id.txtLista);
        carregarRevistas();



        
        return view;

    }

    private void carregarRevistas() {
        ApiInterface apiInterface = ApiClient.apiClient().create(ApiInterface.class);
        Call<List<Revistas>> revistas = apiInterface.getRevistas();
        revistas.enqueue(new Callback<List<Revistas>>() {
            @Override
            public void onResponse(Call<List<Revistas>> call, Response<List<Revistas>> response) {



                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Not Successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                AppDatabase.saveRevistasList(response.body());


                if (AppDatabase.getTodasRevistasList()!=null){
                    renderProducts();
                }




            }

            @Override
            public void onFailure(Call<List<Revistas>> call, Throwable t) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderProducts() {
        RealmResults<Revistas> products = AppDatabase.getTodasRevistasList();
        textView.setText(products.toString());
    }

}
