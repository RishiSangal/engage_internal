package com.example.sew.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.adapters.RenderContentAudioAdapter;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.AudioPlayerControls;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderActivityAudioPlayerControls;
import com.example.sew.models.AudioContent;
import com.example.sew.models.BaseAudioContent;
import com.example.sew.models.PlayingAudioItem;
import com.example.sew.models.RenderContentAudio;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static com.example.sew.activities.BaseActivity.sendBroadCast;

public class RenderContentAudioFragment extends BottomSheetDialogFragment implements ICommonValues, RenderActivityAudioPlayerControls.onAudioPlayerStateChanged {

    @BindView(R.id.layAudioPlayerParent)
    CardView layAudioPlayerParent;
    @BindView(R.id.lstAudioContent)
    ListView lstAudioContent;
    private RenderActivityAudioPlayerControls audioPlayerControls;
    boolean isRenderingPage;
//    RenderContentAudio selectedAudio;
//    JSONArray jsonSelectedAudioArray;

    @OnItemClick(R.id.lstAudioContent)
    void onItemClicked(View convertView) {
        if (convertView.getTag(R.id.tag_data) instanceof RenderContentAudio) {
            RenderContentAudio audioContent = (RenderContentAudio) convertView.getTag(R.id.tag_data);
//            jsonSelectedAudioArray = new JSONArray();
//            jsonSelectedAudioArray.put(audioContent.getJsonObject());
//            selectedAudio = audioContent;
            if (audioPlayerControls != null && audioContent != null) {
                audioPlayerControls.playAudio(contentAudios.indexOf(audioContent));
            }
        }
    }

    public static RenderContentAudioFragment getInstance(ArrayList<RenderContentAudio> contentAudios, int audioPlayPosition, PlayingAudioItem playingAudioItem, boolean isRenderPage) {
        RenderContentAudioFragment fragment = new RenderContentAudioFragment();
        JSONArray jsonArray = new JSONArray();
        for (RenderContentAudio contentAudio : contentAudios)
            jsonArray.put(contentAudio.getJsonObject());
        Bundle args = new Bundle();
        args.putString(AUDIO_CONTENT_ARRAY, jsonArray.toString());
        args.putBoolean(IS_RENDERING_PAGE, isRenderPage);
        args.putString(SELECTED_AUDIO, playingAudioItem.getJsonObject().toString());
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<RenderContentAudio> contentAudios = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_render_content_audio, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    private RenderContentAudioAdapter renderContentAudioAdapter;
    private int selectedAudioStartTime = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int currAudioSelectedPosition = 0;
        if (getArguments() != null && getArguments().containsKey(AUDIO_CONTENT_ARRAY)) {
            try {
                isRenderingPage = getArguments().getBoolean(IS_RENDERING_PAGE, false);
                PlayingAudioItem playingAudioItem = new PlayingAudioItem(new JSONObject(getArguments().getString(SELECTED_AUDIO, "{}")));
                selectedAudioStartTime = playingAudioItem.getCurrentPlayingPosition();
                JSONArray jsonArray = new JSONArray(getArguments().getString(AUDIO_CONTENT_ARRAY, "[]"));
                for (int i = 0; i < jsonArray.length(); i++)
                    contentAudios.add(new RenderContentAudio(jsonArray.optJSONObject(i % jsonArray.length())));
                RenderContentAudio renderContentAudio = new RenderContentAudio(playingAudioItem.getAudioContent().getJsonObject());
                for (int i = 0; i < contentAudios.size(); i++)
                    if (renderContentAudio.getId().contentEquals(contentAudios.get(i).getId())) {
                        currAudioSelectedPosition = i;
                        break;
                    }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }
        }
        layAudioPlayerParent.setVisibility(View.VISIBLE);
        setLayoutDirection(view);
        lstAudioContent.setAdapter(renderContentAudioAdapter = new RenderContentAudioAdapter((BaseActivity) getActivity(), contentAudios));
        renderContentAudioAdapter.setSelectedAudioContent(contentAudios.get(currAudioSelectedPosition));
        audioPlayerControls = new RenderActivityAudioPlayerControls((BaseActivity) getActivity(), view,contentAudios);
        audioPlayerControls.setOnAudioPlayerStateChanged(this);
//        audioPlayerControls.playAudio(currAudioSelectedPosition, selectedAudioStartTime);
//        if (contentAudios.size() == 1)
//            lstAudioContent.setVisibility(View.GONE);
//        else
//            lstAudioContent.setVisibility(View.VISIBLE);
        setCancelable(false);
    }

    private void setLayoutDirection(View rootView) {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
            case HINDI:
                rootView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                break;
            case URDU:
                rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                break;
        }
    }

    @Override
    public void onAudioPause() {

    }

    @Override
    public void onAudioStart() {

    }

    @Override
    public void onAudioWindowClose() {
//        if (isRenderingPage && audioPlayerControls != null) {
//            Intent intent = new Intent();
//            intent.putExtra(SELECTED_AUDIO, audioPlayerControls.getCurrentlyPlayingItem().getJsonObject().toString());
////            if (audioPlayerControls.getMediaPlayer() != null)
////                intent.putExtra(SELECTED_AUDIO_START_TIME, audioPlayerControls.getMediaPlayer().getCurrentPosition());
//            sendBroadCast(BROADCAST_AUDIO_REFRESHED, intent);
//        }
        dismiss();
    }

    @Override
    public void onAudioSelected(RenderContentAudio audioContent) {
        if (renderContentAudioAdapter != null && audioContent instanceof RenderContentAudio)
            renderContentAudioAdapter.setSelectedAudioContent((RenderContentAudio) audioContent);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
//        audioPlayerControls.stopAudioAndCloseWindow();
    }

    @Override
    public void onPause() {
        super.onPause();
//        audioPlayerControls.stopAudioAndCloseWindow();
    }
}
