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
import kat.android.com.movielist.rest.pojo.movie.Movie;

//custom adapter
public class MovieAdapter extends BaseAdapter {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.movie_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            holder.movieTitle = (TextView) convertView.findViewById(R.id.titleMovie);
            holder.movieVoteAvrView = (TextView) convertView.findViewById(R.id.voteAvgView);
            holder.movieVoteCountView = (TextView) convertView.findViewById(R.id.voteCntView);
            holder.movieDateView = (TextView) convertView.findViewById(R.id.dateView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = movies.get(position);
        //Some movies doesn't have icon , so i replaces it by ic_menu_help icon
        if (movie.getBackdrop_path() == null)
            holder.imageView.setImageResource(android.R.drawable.ic_menu_help);
        else  //Using picasso to load movie icon
            Picasso.with(context).load("https://image.tmdb.org/t/p/w130" + movie.getBackdrop_path()).into(holder.imageView);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieVoteAvrView.setText(movie.getVote_average() + "");
        holder.movieVoteCountView.setText(movie.getVote_count() + " votes");
        holder.movieDateView.setText(movie.getRelease_date());
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView movieTitle;
        TextView movieVoteAvrView;
        TextView movieVoteCountView;
        TextView movieDateView;
    }
}
