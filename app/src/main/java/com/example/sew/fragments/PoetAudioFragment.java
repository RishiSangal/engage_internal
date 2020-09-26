package com.example.sew.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.example.sew.R;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.adapters.PoetAudioAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAudioListWithPaging;
import com.example.sew.common.PagingListView;
import com.example.sew.helpers.AudioPlayerControls;
import com.example.sew.models.AudioContent;
import com.example.sew.models.BaseAudioContent;
import com.example.sew.models.PoetDetail;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class PoetAudioFragment extends BasePoetProfileFragment implements AudioPlayerControls.onAudioPlayerStateChanged {

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.lstPoetContent)
    PagingListView lstPoetContent;
    private AudioPlayerControls audioPlayerControls;

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail) {
        return getInstance(poetDetail, new PoetAudioFragment());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser)
            closeAudioPlayer();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeAudioPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        closeAudioPlayer();
    }

    private void closeAudioPlayer() {
        if (audioPlayerControls != null) {
            audioPlayerControls.controlView.findViewById(R.id.closePlayer).performClick();
        }
    }

    @OnItemClick(R.id.lstPoetContent)
    void onItemClicked(View convertView) {
        if (convertView.getTag(R.id.tag_data) instanceof AudioContent) {
            AudioContent audioContent = (AudioContent) convertView.getTag(R.id.tag_data);
            if (audioPlayerControls != null && audioContent != null)
                audioPlayerControls.playAudio(audioContents.indexOf(audioContent));
        }
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        poetAudioAdapter = null;
        updateUI();
        if (audioPlayerControls != null)
            audioPlayerControls.updateUI();
    }
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poet_content, container, false);
        ButterKnife.bind(this, view);
        audioPlayerControls = new AudioPlayerControls(GetActivity(), view);
        audioPlayerControls.setOnAudioPlayerStateChanged(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstPoetContent.setPagingableListener(() -> {
            if (getAudioListWithPaging != null) {
                getAudioListWithPaging.loadMoreData();
                lstPoetContent.setIsLoading(true);
            }
        });
        shimmerViewContainer.startShimmer();
        shimmerViewContainer.setVisibility(View.VISIBLE);
        getPoetAudios();
        ViewCompat.setNestedScrollingEnabled(lstPoetContent, true);
    }

    private GetAudioListWithPaging getAudioListWithPaging;

    private void getPoetAudios() {
        getAudioListWithPaging = new GetAudioListWithPaging();
        getAudioListWithPaging.setPoetId(getPoetDetail().getPoetId())
                .addPagination()
                .runAsync((BaseServiceable.OnApiFinishListener<GetAudioListWithPaging>) getAudioListWithPaging -> {
                    if (getAudioListWithPaging.isValidResponse()) {
                        lstPoetContent.setIsLoading(false);
                        shimmerViewContainer.stopShimmer();
                        shimmerViewContainer.setVisibility(View.GONE);
                        if (getAudioListWithPaging.isFirstPage())
                            audioContents.clear();
                        audioContents.addAll(getAudioListWithPaging.getAudioContents());
                        if (getAudioListWithPaging.getAudioContents() == null ||
                                getAudioListWithPaging.getAudioContents().size() == 0 ||
                                audioContents.size() == getAudioListWithPaging.getTotalCount())
                            lstPoetContent.setHasMoreItems(false);
                        else
                            lstPoetContent.setHasMoreItems(true);
                        updateUI();
                    } else
                        showToast(getAudioListWithPaging.getErrorMessage());
                });
    }

    private ArrayList<AudioContent> audioContents = new ArrayList<>();
    private PoetAudioAdapter poetAudioAdapter;

    private void updateUI() {
        if (audioPlayerControls != null)
            audioPlayerControls.setAudioContents(audioContents);
        if (poetAudioAdapter == null) {
            Parcelable state = lstPoetContent.onSaveInstanceState();
            poetAudioAdapter = new PoetAudioAdapter(GetActivity(), audioContents, getPoetDetail());
            lstPoetContent.setAdapter(poetAudioAdapter);
            lstPoetContent.onRestoreInstanceState(state);
        } else
            poetAudioAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAudioPause() {

    }

    @Override
    public void onAudioStart() {

    }

    @Override
    public void onAudioWindowClose() {
        if (poetAudioAdapter != null)
            poetAudioAdapter.setSelectedAudioContent(null);

    }

    @Override
    public void onAudioSelected(BaseAudioContent audioContent) {
        if (poetAudioAdapter != null && audioContent instanceof AudioContent)
            poetAudioAdapter.setSelectedAudioContent((AudioContent) audioContent);
    }
}
