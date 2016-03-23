package com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.MovieDetailsListener;
import com.squareup.picasso.Picasso;

/**
 * novoa on 3/22/16.
 */
public class MovieDetailsTrailerVH extends RecyclerView.ViewHolder{

    private MovieDetailsListener mListener;
    private ImageView mTrailerThumbnail;
    private String mVideoId;

    public MovieDetailsTrailerVH(final View itemView, MovieDetailsListener listener) {
        super(itemView);
        this.mTrailerThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        this.mListener = listener;

        itemView.findViewById(R.id.ll_thumbnail_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openTrailerVideo(mVideoId);
            }
        });
    }

    public void bindContent(MovieTrailerModel trailer){
        this.mVideoId = trailer.getKey();
        Picasso.with(mTrailerThumbnail.getContext())
                .load(String.format(mTrailerThumbnail.getContext().getString(R.string.youtube_thumbnail_url), trailer.getKey()))
                .error(R.drawable.ic_no_poster_available)
                .placeholder(android.R.color.black)
                .into(mTrailerThumbnail);
    }
}
