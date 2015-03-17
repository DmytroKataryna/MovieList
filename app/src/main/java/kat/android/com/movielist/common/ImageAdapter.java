package kat.android.com.movielist.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kat.android.com.movielist.R;
import kat.android.com.movielist.rest.pojo.images.Backdrop;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<Backdrop> images;

    public ImageAdapter(Context context, List<Backdrop> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.backdrops_image, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.backdropsImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Backdrop image = images.get(position);

        if (image.getFile_path() == null)
            holder.imageView.setImageResource(android.R.drawable.ic_menu_help);
        else  //Using picasso to load movie images
            Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + image.getFile_path()).into(holder.imageView);
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
