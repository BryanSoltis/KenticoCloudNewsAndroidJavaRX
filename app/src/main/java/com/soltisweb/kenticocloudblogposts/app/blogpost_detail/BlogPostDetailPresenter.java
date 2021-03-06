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

import android.support.annotation.NonNull;

import com.soltisweb.kenticocloudblogposts.data.models.BlogPost;
import com.soltisweb.kenticocloudblogposts.data.source.blogposts.BlogPostsDataSource;
import com.soltisweb.kenticocloudblogposts.data.source.blogposts.BlogPostsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

class BlogPostDetailPresenter implements BlogPostDetailContract.Presenter {

    private final String blogpostCodename;
    private final BlogPostsRepository repository;

    private final BlogPostDetailContract.View view;

    BlogPostDetailPresenter(@NonNull BlogPostsRepository repository, @NonNull BlogPostDetailContract.View view, @NonNull String blogpostCodename) {
        this.repository = checkNotNull(repository, "repository cannot be null");
        this.view = checkNotNull(view, "view cannot be null!");
        this.view.setPresenter(this);
        this.blogpostCodename = blogpostCodename;
    }

    @Override
    public void start() {
        loadBlogPost();
    }

    @Override
    public void loadBlogPost() {
        this.view.setLoadingIndicator(true);

        this.repository.getBlogPost(this.blogpostCodename, new BlogPostsDataSource.LoadBlogPostCallback() {

            @Override
            public void onItemLoaded(BlogPost item) {
                view.showBlogPost(item);
            }

            @Override
            public void onDataNotAvailable() {
                 view.showLoadingError();
            }

            @Override
            public void onError(Throwable e) {
                view.showLoadingError();
            }
        });
    }
}
