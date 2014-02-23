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
    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        Context context = getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_item, null);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        Movie movie = getItem(position);

        ImageView ivPosterImage = (ImageView) convertView.findViewById(R.id.ivPosterImage);
        ivPosterImage.setImageResource(android.R.color.transparent);
        ImageLoader.getInstance().displayImage(movie.getPosterOriginal(), ivPosterImage);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(movie.getTitle());

        return convertView;
    }
}
