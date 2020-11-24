package com.example.sew.models.home_view_holders;

import android.view.View;
import android.widget.ImageView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.Blogs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlogsViewHolder extends BaseHomeViewHolder {

    @BindView(R.id.imgBlogsImage)
    ImageView imgBlogsImage;

    @OnClick(R.id.imgBlogsImage)
    void onImageShayariClick() {
        if (blogs == null)
            return;

    }

    public BlogsViewHolder(BaseActivity baseActivity) {
        super(baseActivity);
    }

    private BlogsViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }

    private Blogs blogs;

    public BlogsViewHolder loadData(final Blogs blogs) {
        this.blogs = blogs;
        ImageHelper.setImage(imgBlogsImage, blogs.getImageUrl(), Enums.PLACEHOLDER_TYPE.PROMOTIONAL_BANNER);
        return this;
    }

    public static BlogsViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        BlogsViewHolder blogsViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_blogs, baseActivity);
            blogsViewHolder = new BlogsViewHolder(convertView, baseActivity);
        } else
            blogsViewHolder = (BlogsViewHolder) convertView.getTag();
        blogsViewHolder.setConvertView(convertView);
        convertView.setTag(blogsViewHolder);
        return blogsViewHolder;
    }
}
