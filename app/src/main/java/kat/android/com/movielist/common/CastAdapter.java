package kat.android.com.movielist.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kat.android.com.movielist.R;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.images.Cast;

public class CastAdapter extends BaseAdapter {

    private Context context;
    private List<Cast> cast;

    public CastAdapter(Context context, List<Cast> cast) {
        this.context = context;
        this.cast = cast;
    }

    @Override
    public int getCount() {
        return cast.size();
    }

    @Override
    public Object getItem(int position) {
        return cast.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.cast_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.castImageView);
            holder.textView = (TextView) convertView.findViewById(R.id.castNameText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cast actor = cast.get(position);

        if (actor.getProfile_path() == null)
            holder.imageView.setImageResource(android.R.drawable.ic_menu_help);
        else  //Using picasso to load movie images
            Picasso.with(context).load(RestClient.IMAGE_ROOT + "/w130" + actor.getProfile_path()).into(holder.imageView);

        holder.textView.setText(actor.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
