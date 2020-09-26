package com.example.sew.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.example.sew.activities.BaseActivity;
import com.example.sew.apis.GetHomePageCollection;
import com.example.sew.models.HomeLookingMore;
import com.example.sew.models.HomeShayariImage;
import com.example.sew.models.home_view_holders.BannerCarouselViewHolder;
import com.example.sew.models.home_view_holders.BaseHomeViewHolder;
import com.example.sew.models.home_view_holders.CollectionTemplateViewHolder;
import com.example.sew.models.home_view_holders.DidYouKowViewHolder;
import com.example.sew.models.home_view_holders.FeaturedPoetViewHolder;
import com.example.sew.models.home_view_holders.ImageShayariViewHolder;
import com.example.sew.models.home_view_holders.LookingMoreViewHolder;
import com.example.sew.models.home_view_holders.MoreImageShayariAndMoreVideoViewHolder;
import com.example.sew.models.home_view_holders.PromotionalBannerViewHolder;
import com.example.sew.models.home_view_holders.SupportRekhtaViewHolder;
import com.example.sew.models.home_view_holders.TodayTopViewHolder;
import com.example.sew.models.home_view_holders.VideoViewHolder;
import com.example.sew.models.home_view_holders.WordOfTheDayViewHolder;

import java.util.ArrayList;

public class HomePageAdapter extends BaseMyAdapter {
    private GetHomePageCollection homePageCollection;
    public static final int VIEW_TYPE_PROMOTION = 0;
    public static final int VIEW_TYPE_CAROUSELS = 1;
    public static final int VIEW_TYPE_TOP_POETS = 2;
    public static final int VIEW_TYPE_WORD_OF_THE_DAY = 3;
    public static final int VIEW_TYPE_OTHER_IMG_SHAYARI = 4;
    public static final int VIEW_TYPE_VIDEO = 5;
    public static final int VIEW_TYPE_MORE_VIDEOS = 6;
    public static final int VIEW_TYPE_FEATURED = 7;
    public static final int VIEW_TYPE_TODAYS_TOP = 8;
    public static final int VIEW_TYPE_IMG_SHAYARI = 9;
    public static final int VIEW_TYPE_DID_YOU_KNOW= 10;
    //    public static final int VIEW_TYPE_T20_SERIES = 10;
    public static final int VIEW_TYPE_SHAYARI_COLLECTION = 11;
    public static final int VIEW_TYPE_DISCOVER = 12;
    public static final int VIEW_TYPE_SHER_COLLECTION = 13;
    public static final int VIEW_TYPE_PROSE_COLLECTION = 14;
    public static final int VIEW_TYPE_LOOKING_MORE = 15;
    public static final int VIEW_TYPE_SUPPORT_REKHTA = 16;
    public static final int VIEW_TYPE_OTHER = 17;
    public static final int TOTAL_ITEM_COUNT = VIEW_TYPE_OTHER + 1;
    private ArrayList<Object> data;

    public HomePageAdapter(BaseActivity activity, GetHomePageCollection homePageCollection, ArrayList<Object> data) {
        super(activity);
        this.homePageCollection = homePageCollection;
        this.data = data;
    }

    @Override
    public int getViewTypeCount() {
        return TOTAL_ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Integer)
            return (int) getItem(position);
        return VIEW_TYPE_OTHER;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    private ArrayList<HomeShayariImage> moreShayariImages = new ArrayList<>();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHomeViewHolder baseHomeViewHolder = null;
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_CAROUSELS:
                baseHomeViewHolder = BannerCarouselViewHolder.getInstance(convertView, getActivity()).loadData(homePageCollection.getBannerCollections());
                break;
            case VIEW_TYPE_PROMOTION:
                baseHomeViewHolder = PromotionalBannerViewHolder.getInstance(convertView, getActivity()).loadData(homePageCollection.getPromotionalBanners().get(0));
                break;
            case VIEW_TYPE_TODAYS_TOP:
                TodayTopViewHolder todayTopViewHolder = TodayTopViewHolder.getInstance(convertView, getActivity());
                baseHomeViewHolder = todayTopViewHolder.loadData(homePageCollection.getTodayTops());
                break;
            case VIEW_TYPE_TOP_POETS:
                baseHomeViewHolder = CollectionTemplateViewHolder.getInstance(convertView, getActivity()).loadTopPoets(homePageCollection.getTopPoets());
                break;
            case VIEW_TYPE_WORD_OF_THE_DAY:
                baseHomeViewHolder = WordOfTheDayViewHolder.getInstance(convertView, getActivity()).loadData(homePageCollection.getOtherWorldOfTheDays(), homePageCollection.getWordOfTheDay());
                break;
            case VIEW_TYPE_SHER_COLLECTION:
                baseHomeViewHolder = CollectionTemplateViewHolder.getInstance(convertView, getActivity()).loadSherCollection(homePageCollection.getSherCollections());
                break;
            case VIEW_TYPE_FEATURED:
                baseHomeViewHolder = FeaturedPoetViewHolder.getInstance(convertView, getActivity()).loadData(homePageCollection.getFeatureds().get(0));
                break;
            case VIEW_TYPE_DID_YOU_KNOW:
                baseHomeViewHolder= DidYouKowViewHolder.getInstance(convertView,getActivity()).loadData(homePageCollection.getDidYouKnow());
                break;
            case VIEW_TYPE_SHAYARI_COLLECTION:
                baseHomeViewHolder = CollectionTemplateViewHolder.getInstance(convertView, getActivity()).loadShayri(homePageCollection.getShayaris());
                break;
            case VIEW_TYPE_IMG_SHAYARI:
                baseHomeViewHolder = ImageShayariViewHolder.getInstance(convertView, getActivity()).loadData(homePageCollection.getShayariImages().get(0));
                break;
            case VIEW_TYPE_OTHER_IMG_SHAYARI:
                moreShayariImages.clear();
                moreShayariImages.addAll(homePageCollection.getShayariImages().subList(1, homePageCollection.getShayariImages().size() - 1));
                baseHomeViewHolder = MoreImageShayariAndMoreVideoViewHolder.getInstance(convertView, getActivity()).loadMoreImageShayari(moreShayariImages);
                // baseHomeViewHolder = CollectionTemplateViewHolder.getInstance(convertView, getActivity()).loadMoreImageShayari(moreShayariImages);
                break;
            case VIEW_TYPE_VIDEO:
                baseHomeViewHolder = VideoViewHolder.getInstance(convertView, getActivity()).loadData(homePageCollection.getVideo());
                break;
            case VIEW_TYPE_MORE_VIDEOS:
                baseHomeViewHolder = MoreImageShayariAndMoreVideoViewHolder.getInstance(convertView, getActivity()).loadMoreVideo(homePageCollection.getVideos());

                //baseHomeViewHolder = CollectionTemplateViewHolder.getInstance(convertView, getActivity()).loadMoreVideo(homePageCollection.getVideos());
                break;
            case VIEW_TYPE_PROSE_COLLECTION:
                baseHomeViewHolder = CollectionTemplateViewHolder.getInstance(convertView, getActivity()).loadProseCollection(homePageCollection.getProseCollections());
                break;
            case VIEW_TYPE_SUPPORT_REKHTA:
                baseHomeViewHolder = SupportRekhtaViewHolder.getInstance(convertView, getActivity()).loadData();
                break;
            case VIEW_TYPE_OTHER:
                if (getItem(position) instanceof HomeLookingMore) {
                    boolean showTitleHeader = false;
                    if (position != 0)
                        showTitleHeader = !(getItem(position - 1) instanceof HomeLookingMore);
                    HomeLookingMore lookingMore = (HomeLookingMore) getItem(position);
                    baseHomeViewHolder = LookingMoreViewHolder.getInstance(convertView, getActivity()).loadData(lookingMore, showTitleHeader);
                }
                break;
        }

        return baseHomeViewHolder.getConvertView();
    }

}
