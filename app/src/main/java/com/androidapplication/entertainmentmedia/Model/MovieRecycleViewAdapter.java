package com.androidapplication.entertainmentmedia.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidapplication.entertainmentmedia.Controller.MovieDetailsActivity;
import com.androidapplication.entertainmentmedia.Data.Movie;
import com.androidapplication.entertainmentmedia.R;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by ahmed on 11/7/2017.
 */

@SuppressWarnings("ConstantConditions")
public class MovieRecycleViewAdapter extends RecyclerView.Adapter<MovieRecycleViewAdapter.ViewHolder>{

    //Instance variables
    private Context context;
    private List<Movie> movieList;

    public MovieRecycleViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movieList = movies;
    }

    @Override
    public MovieRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MovieRecycleViewAdapter.ViewHolder holder, int position) {

        Movie movie = movieList.get(position);

        holder.movieTitle.setText(movie.getTitle());
        holder.movieType.setText(movie.getMovieType());


        Picasso.with(context)
                .load(movie.getPoster())
                .placeholder(android.R.drawable.ic_media_play)
                .into( holder.moviePoster);

        holder.movieYear.setText(movie.getYear());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       private TextView movieTitle;
       private ImageView moviePoster;
       private TextView movieYear;
       private TextView movieType;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            movieTitle = (TextView) itemView.findViewById(R.id.movieTitleTextView);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePosterImageView);
            movieYear = (TextView) itemView.findViewById(R.id.movieYearTextView);
            movieType = (TextView) itemView.findViewById(R.id.movieTypeTextView);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Movie movie = movieList.get(getAdapterPosition());

                    Intent intent = new Intent(context, MovieDetailsActivity.class);

                    intent.putExtra("movie", movie);
                    ctx.startActivity(intent);
                }
            });
        }
    }
}
