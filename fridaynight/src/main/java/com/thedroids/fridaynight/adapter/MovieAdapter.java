package com.thedroids.fridaynight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.thedroids.fridaynight.R;
import com.thedroids.fridaynight.model.Movie;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    static private ImageLoader imageLoader;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.movie_item, null);
        }

        Movie movie = getItem(position);

        ImageView ivPosterImage = (ImageView) view.findViewById(R.id.ivPosterImage);
        imageLoader.displayImage(movie.getPosterOriginal(), ivPosterImage);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(movie.getTitle());

        TextView tvSynopsis = (TextView) view.findViewById(R.id.tvSynopsis);
        tvSynopsis.setText(movie.getSynopsis());

        return view;
    }
}
