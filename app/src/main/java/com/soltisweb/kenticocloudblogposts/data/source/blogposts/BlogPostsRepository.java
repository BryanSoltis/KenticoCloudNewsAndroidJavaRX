/*
 * Copyright 2017 Kentico s.r.o. and Richard Sustek
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.soltisweb.kenticocloudblogposts.data.source.blogposts;

import android.support.annotation.NonNull;

import com.soltisweb.kenticocloudblogposts.data.models.BlogPost;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class BlogPostsRepository implements  BlogPostsDataSource {

    private static BlogPostsRepository INSTANCE = null;

    private final BlogPostsDataSource dataSource;

    // Prevent direct instantiation.
    private BlogPostsRepository(@NonNull BlogPostsDataSource dataSource){
        this.dataSource = checkNotNull(dataSource);
    }

    @Override
    public void getBlogPosts(@NonNull final LoadBlogPostsCallback callback) {
        this.dataSource.getBlogPosts(new LoadBlogPostsCallback() {
            @Override
            public void onItemsLoaded(List<BlogPost> blogposts) {
                callback.onItemsLoaded(blogposts);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void getBlogPost(@NonNull String codename, @NonNull final LoadBlogPostCallback callback) {
        this.dataSource.getBlogPost(codename, new LoadBlogPostCallback() {
            @Override
            public void onItemLoaded(BlogPost blogpost) {
                callback.onItemLoaded(blogpost);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param dataSource the backend data source
     * @return the {@link BlogPostsRepository} instance
     */
    public static BlogPostsRepository getInstance(BlogPostsDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BlogPostsRepository(dataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}