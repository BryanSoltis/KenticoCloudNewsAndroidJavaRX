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

import android.content.Context;
import android.support.annotation.NonNull;

import com.kenticocloud.delivery_core.callbacks.BaseCloudSource;
import com.kenticocloud.delivery_core.models.common.OrderType;
import com.kenticocloud.delivery_core.models.item.DeliveryItemListingResponse;
import com.kenticocloud.delivery_core.models.item.DeliveryItemResponse;
import com.soltisweb.kenticocloudblogposts.data.models.BlogPost;
import com.soltisweb.kenticocloudblogposts.injection.Injection;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BlogPostsCloudSource extends BaseCloudSource implements BlogPostsDataSource {

    private static BlogPostsCloudSource INSTANCE;

    // Prevent direct instantiation.
    private BlogPostsCloudSource(@NonNull Context context) {
        super(Injection.provideDeliveryService());
    }

    public static BlogPostsCloudSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BlogPostsCloudSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getBlogPosts(@NonNull final LoadBlogPostsCallback callback) {
        this.deliveryService.<BlogPost>items()
                .type(BlogPost.TYPE)
                .orderParameter("elements.date", OrderType.Desc)
                .limitParameter(10)
                .getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeliveryItemListingResponse<BlogPost>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeliveryItemListingResponse<BlogPost> response) {
                        List<BlogPost> items = (response.getItems());

                        if (items == null || items.size() == 0){
                            callback.onDataNotAvailable();
                            return;
                        }

                        callback.onItemsLoaded(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getBlogPost(@NonNull String codename, @NonNull final LoadBlogPostCallback callback) {
        this.deliveryService.<BlogPost>item(codename)
                .getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeliveryItemResponse<BlogPost>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeliveryItemResponse<BlogPost> response) {
                        if (response.getItem() == null){
                            callback.onDataNotAvailable();
                        }

                        callback.onItemLoaded(response.getItem());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
