package com.soltisweb.kenticocloudblogposts.data.models;

import com.kenticocloud.delivery_core.elements.TextElement;
import com.kenticocloud.delivery_core.models.item.ContentItem;
import com.kenticocloud.delivery_core.models.item.ElementMapping;

/**
 * Created by bryan on 2/15/2018.
 */

public final class Author extends ContentItem
{
    public static final String TYPE = "author";

    @ElementMapping("name")
    public TextElement name;

    public String getName() {
        return name.getValue();
    }
}
