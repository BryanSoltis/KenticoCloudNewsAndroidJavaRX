/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.soltisweb.kenticocloudblogposts.app.blogpost_detail;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soltisweb.kenticocloudblogposts.R;
import com.soltisweb.kenticocloudblogposts.app.core.BaseFragment;
import com.soltisweb.kenticocloudblogposts.data.models.BlogPost;
import com.soltisweb.kenticocloudblogposts.util.PicassoImageGetter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BlogPostDetailFragment extends BaseFragment<BlogPostDetailContract.Presenter> implements BlogPostDetailContract.View {

    public BlogPostDetailFragment() {
        // Requires empty public constructor
    }

    public static BlogPostDetailFragment newInstance() {
        return new BlogPostDetailFragment();
    }

    @Override
    protected int getFragmentId(){
        return R.layout.blogpost_detail_frag;
    }

    @Override
    protected int getViewId(){
        return R.id.blogpostDetailLL;
    }

    @Override
    protected boolean hasScrollSwipeRefresh() {
        return true;
    }

    @Override
    protected void onScrollSwipeRefresh() {
        this.presenter.loadBlogPost();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showBlogPost(BlogPost blogpost) {
        View view = getView();

        if (view == null){
            return;
        }

        // Update activity title
        getActivity().setTitle(blogpost.getTitle());

        // Set up blogpost topic
        TextView blogpostTopicTV = (TextView) view.findViewById(R.id.blogpostDetailTopicTV);
        blogpostTopicTV.setText(blogpost.getTopic().toUpperCase());
        setLoadingIndicator(false);
        
        // Set up blogpost detail view
        TextView blogpostTitleTV = (TextView) view.findViewById(R.id.blogpostDetailTitleTV);
        blogpostTitleTV.setText(blogpost.getTitle());
        setLoadingIndicator(false);

        // image
        final ImageView teaserIV = (ImageView) view.findViewById(R.id.blogpostDetailTeaserIV);
        Picasso.with(view.getContext()).load(blogpost.getTeaserImageUrl()).into(teaserIV);

        // author
        TextView blogpostAuthorTV = (TextView) view.findViewById(R.id.blogpostDetailAuthorTV);
        blogpostAuthorTV.setText(blogpost.getAuthor());
        setLoadingIndicator(false);

        // release date
        TextView postDateTV = (TextView) view.findViewById(R.id.blogpostDetailPostDateTV);
        SimpleDateFormat postDf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        postDateTV.setText(postDf.format(blogpost.getPostDate()));

        // body
        PicassoImageGetter imageGetter = new PicassoImageGetter(view.getContext());
        TextView bodyCopyTV = (TextView) view.findViewById(R.id.blogpostDetailBodyCopyTV);
        bodyCopyTV.setText(Html.fromHtml(blogpost.getBody(), imageGetter, null));

        this.fragmentView.setVisibility(View.VISIBLE);

    }
}
