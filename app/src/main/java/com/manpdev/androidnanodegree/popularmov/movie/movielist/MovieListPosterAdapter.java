package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/21/16
 */
public class MovieListPosterAdapter extends RecyclerView.Adapter<MovieListPosterAdapter.PosterViewHolder> {

    private List<MovieModel> mMovieList;
    private Picasso mPicasso;
    private final OnMoviePosterClick mItemClickListener;

    public MovieListPosterAdapter(Context context, List<MovieModel> movieList, OnMoviePosterClick itemClickListener) {
        this.mMovieList = movieList;
        this.mPicasso = Picasso.with(context);
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public MovieListPosterAdapter.PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie_poster, parent, false);

         return new PosterViewHolder(root, this.mItemClickListener);
    }

    @Override
    public void onBindViewHolder(MovieListPosterAdapter.PosterViewHolder holder, int position) {
        mPicasso.load(mMovieList.get(position).getPosterPath()).into(holder.getPosterView());
        holder.setmMovieId(mMovieList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }


    public static class PosterViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPoster;
        private OnMoviePosterClick mListener;
        private int mMovieId;

        public PosterViewHolder(View parent, OnMoviePosterClick listener) {
            super(parent);

            this.mListener = listener;
            this.mPoster = (ImageView) parent.findViewById(R.id.iv_poster);

            this.mPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onMoviePosterSelected(mMovieId);
                }
            });
        }

        public ImageView getPosterView() {
            return mPoster;
        }

        public void setmMovieId(int mMovieId) {
            this.mMovieId = mMovieId;
        }
    }

}
