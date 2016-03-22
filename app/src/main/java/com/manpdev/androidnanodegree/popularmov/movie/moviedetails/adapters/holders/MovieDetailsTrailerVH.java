package com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.holders;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;
import com.squareup.picasso.Picasso;

/**
 * novoa on 3/22/16.
 */
public class MovieDetailsTrailerVH extends RecyclerView.ViewHolder{

    private ImageView mTrailerThumbnail;
    private String mVideoId;

    public MovieDetailsTrailerVH(final View itemView) {
        super(itemView);
        this.mTrailerThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);

        itemView.findViewById(R.id.ll_thumbnail_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/22/16 change
                Intent intent  = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v="+ mVideoId));
                itemView.getContext().startActivity(intent);
            }
        });
    }

    public void bindContent(MovieTrailerModel trailer){
        this.mVideoId = trailer.getKey();
        Picasso.with(mTrailerThumbnail.getContext())
                .load(String.format("http://img.youtube.com/vi/%s/0.jpg", trailer.getKey()))
                .error(mTrailerThumbnail.getContext().getResources().getDrawable(R.drawable.ic_no_poster_available))
                .into(mTrailerThumbnail);
    }
}
