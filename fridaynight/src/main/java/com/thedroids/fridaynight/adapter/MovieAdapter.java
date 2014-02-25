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

    private final int MAX_SYNOPSIS = 150;

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
        tvSynopsis.setText(StringUtil.ellipsize(movie.getSynopsis(), MAX_SYNOPSIS));

        return view;
    }
}

/**
 * Lifted from http://stackoverflow.com/a/3657496/197507
 */
class StringUtil {
    private final static String NON_THIN = "[^iIl1\\.,']";

    private static int textWidth(String str) {
        return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }

    public static String ellipsize(String text, int max) {
        if (textWidth(text) <= max)
            return text;

        // Start by chopping off at the word before max
        // This is an over-approximation due to thin-characters...
        int end = text.lastIndexOf(' ', max - 3);

        // Just one long word. Chop it off.
        if (end == -1)
            return text.substring(0, max-3) + "...";

        // Step forward as long as textWidth allows.
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);

            // No more spaces.
            if (newEnd == -1)
                newEnd = text.length();

        } while (textWidth(text.substring(0, newEnd) + "...") < max);

        return text.substring(0, end) + "...";
    }
}
