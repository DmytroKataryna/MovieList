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
import kat.android.com.movielist.rest.pojo.person.Person;

public class PersonAdapter extends BaseAdapter {

    Context context;
    List<Person> persons;

    public PersonAdapter(Context context, List<Person> persons) {
        this.context = context;
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.person_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.profileImage);
            holder.personName = (TextView) convertView.findViewById(R.id.personNameTextView);
            holder.mark = (TextView) convertView.findViewById(R.id.popularityTextView);
            holder.knownMovieFirst = (TextView) convertView.findViewById(R.id.movieNameTextFirst);
            holder.knownMovieSecond = (TextView) convertView.findViewById(R.id.movieNameTextSecond);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person = persons.get(position);

        //profile image
        if (person.getProfile_path() == null)
            holder.imageView.setImageResource(android.R.drawable.ic_menu_help);
        else
            Picasso.with(context).load("https://image.tmdb.org/t/p/w130" + person.getProfile_path()).into(holder.imageView);

        holder.personName.setText(person.getName());
        holder.mark.setText((int) person.getPopularity() + "");

        //person know for movies
        if (person.getKnown_for().size() > 1) {
            holder.knownMovieFirst.setText(person.getKnown_for().get(0).getTitle());
            holder.knownMovieSecond.setText(person.getKnown_for().get(1).getTitle());
        } else if (person.getKnown_for().size() > 0)
            holder.knownMovieFirst.setText(person.getKnown_for().get(0).getTitle());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView personName;
        TextView mark;
        TextView knownMovieFirst;
        TextView knownMovieSecond;
    }
}
