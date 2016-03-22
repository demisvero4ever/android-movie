package com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;

/**
 * novoa on 3/22/16.
 */
public class MovieDetailsReviewVH extends RecyclerView.ViewHolder{

    private TextView mReviewContent;
    private TextView mReviewAuthor;
    private ViewGroup mContainer;
    private String mReviewUrl;

    public MovieDetailsReviewVH(View itemView) {
        super(itemView);
        this.mContainer = (ViewGroup) itemView.findViewById(R.id.ll_review_container);
        this.mReviewContent = (TextView) itemView.findViewById(R.id.tv_movie_review_content);
        this.mReviewAuthor = (TextView) itemView.findViewById(R.id.tv_movie_review_author);

        this.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/22/16 Call listener and pass the url
            }
        });
    }

    public void bindContent(MovieReviewModel review){
        this.mReviewUrl = review.getUrl();
        this.mReviewContent.setText(review.getContent());
        this.mReviewAuthor.setText(review.getAuthor());
    }
}
