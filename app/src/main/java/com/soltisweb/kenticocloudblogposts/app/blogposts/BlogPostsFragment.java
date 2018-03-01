/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.soltisweb.kenticocloudblogposts.app.blogposts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.soltisweb.kenticocloudblogposts.R;
import com.soltisweb.kenticocloudblogposts.app.blogpost_detail.BlogPostDetailActivity;
import com.soltisweb.kenticocloudblogposts.app.core.BaseFragment;
import com.soltisweb.kenticocloudblogposts.app.shared.CommunicationHub;
import com.soltisweb.kenticocloudblogposts.data.models.BlogPost;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

public class BlogPostsFragment extends BaseFragment<BlogPostsContract.Presenter> implements BlogPostsContract.View{

    private BlogPostsAdapter adapter;

    public BlogPostsFragment() {
        // Requires empty public constructor
    }

    @Override
    protected int getFragmentId(){
        return R.layout.blogposts_frag;
    }

    @Override
    protected int getViewId(){
        return R.id.blogpostsLL;
    }

    @Override
    protected boolean hasScrollSwipeRefresh() {
        return true;
    }

    @Override
    protected void onScrollSwipeRefresh() {
        this.presenter.loadBlogPosts();
    }

    @Override
    protected View scrollUpChildView() {
        return this.root.findViewById(R.id.blogpostsLV);
    }

    public static BlogPostsFragment newInstance() {
        return new BlogPostsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new BlogPostsAdapter(new ArrayList<BlogPost>(0), blogpostItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ListView listView = (ListView) this.root.findViewById(R.id.blogpostsLV);
        listView.setAdapter(adapter);

        return this.root;
    }

    @Override
    public void showBlogPosts(List<BlogPost> blogposts) {
        adapter.replaceData(blogposts);
        fragmentView.setVisibility(View.VISIBLE);
    }

    /**
     * Listener for clicks on items in the ListView.
     */
    BlogPostItemListener blogpostItemListener = new BlogPostItemListener() {
        @Override
        public void onBlogPostClick(BlogPost clickedBlogPost) {
            Intent blogpostDetailIntent = new Intent(getContext(), BlogPostDetailActivity.class);
            blogpostDetailIntent.putExtra(CommunicationHub.BlogPostCodename.toString(), clickedBlogPost.getSystem().getCodename());
            startActivity(blogpostDetailIntent);
        }
    };

    private static class BlogPostsAdapter extends BaseAdapter {

        private List<BlogPost> _blogposts;
        private BlogPostItemListener _blogpostItemListener;

        BlogPostsAdapter(List<BlogPost> blogposts, BlogPostItemListener itemListener) {
            setList(blogposts);
            _blogpostItemListener = itemListener;
        }

        void replaceData(List<BlogPost> blogposts) {
            setList(blogposts);
            notifyDataSetChanged();
        }

        private void setList(List<BlogPost> blogposts) {
            _blogposts = checkNotNull(blogposts);
        }

        @Override
        public int getCount() {
            return _blogposts.size();
        }

        @Override
        public BlogPost getItem(int i) {
            return _blogposts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.blogpost_item, viewGroup, false);
            }

            final BlogPost blogpost = getItem(i);

            // title
            TextView titleTV = (TextView) rowView.findViewById(R.id.blogpostTitleTV);
            titleTV.setText(blogpost.getTitle());

            // image
            final ImageView teaserIV = (ImageView) rowView.findViewById(R.id.blogpostTeaserIV);
            Picasso.with(viewGroup.getContext()).load(blogpost.getTeaserImageUrl()).into(teaserIV);

            // author
            TextView blogpostAuthorTV = (TextView) rowView.findViewById(R.id.blogpostAuthorTV);
            blogpostAuthorTV.setText(blogpost.getAuthor());

            // release date
            TextView postDateTV = (TextView) rowView.findViewById(R.id.blogpostPostDateTV);
            SimpleDateFormat postDf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            postDateTV.setText(postDf.format(blogpost.getPostDate()));

            // perex
            TextView summaryTV = (TextView) rowView.findViewById(R.id.blogpostSummaryTV);
            summaryTV.setText(Html.fromHtml(blogpost.getPerex()));

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _blogpostItemListener.onBlogPostClick(blogpost);
                }
            });

            return rowView;
        }
    }

    interface BlogPostItemListener {

        void onBlogPostClick(BlogPost clickedBlogPost);
    }
}
