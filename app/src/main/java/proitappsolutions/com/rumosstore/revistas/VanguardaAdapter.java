package proitappsolutions.com.rumosstore.revistas;

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

public class VanguardaAdapter extends BaseAdapter {

    private List<Vangarda> movieList;
    private Context mContext;

    public VanguardaAdapter(List<Vangarda> movieList, Context mContext) {
        this.movieList = movieList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieList.get(i);
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
            Vangarda movie = movieList.get(i);

            Picasso.with(mContext).load(movie.getImageURL()).placeholder(R.drawable.ic_avatar).into(image);
            name.setText(movie.getName());
        }

        return rowView;
    }
}
