package proitappsolutions.com.rumosstore.testeRealmDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import proitappsolutions.com.rumosstore.R;

public class RevistasAdapter extends BaseAdapter {

    private List<Revistas> revistasList;
    private Context mContext;

    public RevistasAdapter(List<Revistas> revistasList, Context mContext) {
        this.revistasList = revistasList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return revistasList.size();
    }

    @Override
    public Object getItem(int i) {
        return revistasList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null){
            rowView = LayoutInflater.from(mContext).inflate(R.layout.layout_item,null);

            TextView name = (TextView)rowView.findViewById(R.id.label);
            ImageView image = (ImageView)rowView.findViewById(R.id.image);

            //Set Data
            Revistas revistas = revistasList.get(i);

            Picasso.with(mContext).load(revistas.getFotoJornal()).placeholder(R.drawable.revista_placeholder).into(image);
            name.setText(revistas.getNome());
        }

        return rowView;
    }
}
