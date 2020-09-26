package com.example.sew;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.adapters.BaseMyAdapter;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.SherContent;
import com.example.sew.views.TitleTextViewType6;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public class SherViewHolder {
    BaseActivity activity;
    private View.OnClickListener onTagClick, onGhazalClick, onEnableCritique;
    BaseMyAdapter adapter;
    @BindView(R.id.laySher)
    public LinearLayout laySher;
    @BindView(R.id.translatedtextsher)
    public TextView translatedtextsher;
    @BindView(R.id.sher_heartIcon)
    public ImageView sherHeartIcon;
    @BindView(R.id.sher_ghazalIcon)
    public ImageView sherGhazalIcon;
    @BindView(R.id.sher_translateIcon)
    public ImageView sherTranslateIcon;
    @BindView(R.id.sherTagIcon)
    public ImageView sherTagIcon;
    @BindView(R.id.sher_poet_name)
    public TextView sher_poet_name;
    @BindView(R.id.imgCritiqueInfo)
    public ImageView imgCritiqueInfo;

    @BindView(R.id.sher_shareIcon)
    public ImageView sher_shareIcon;
    @BindView(R.id.sher_clipbordIcon)
    public ImageView sher_clipbordIcon;

    @BindView(R.id.sherFirstTagView)
    public TextView sherFirstTagView;
    @BindView(R.id.shersandText)
    public TextView shersandText;
    @BindView(R.id.shermoreTagView)
    public TextView shermoreTagView;
    @BindView(R.id.tagContainer)
    public View tagContainer;

    @BindView(R.id.txtTagsTitle)
    public TextView txtTagsTitle;
    @BindView(R.id.layTags)
    public LinearLayout layTags;
    @BindView(R.id.layBottom)
    public LinearLayout layBottom;
    public static final int PAGE_TYPE_BASIC = 1;
    public static final int PAGE_TYPE_CRITIQUE_ENABLED = 3;
    public int currentPageType = PAGE_TYPE_BASIC;
    public BaseActivity getActivity() {
        return activity;
    }

    @OnClick({R.id.sher_heartIcon, R.id.sher_clipbordIcon, R.id.sher_ghazalIcon, R.id.sher_translateIcon, R.id.sherTagIcon, R.id.sher_shareIcon, R.id.sher_poet_name
            , R.id.imgCritiqueInfo})
    public void onViewClicked(View view) {
        SherContent sherContent = null;
        if (view.getTag(R.id.tag_data) != null && view.getTag(R.id.tag_data) instanceof SherContent)
            sherContent = (SherContent) view.getTag(R.id.tag_data);
        switch (view.getId()) {
            case R.id.sher_shareIcon: {
                if (sherContent == null)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                String sherContentText = MyHelper.getSherContentText(sherContent.getRenderText());
                if (TextUtils.isEmpty(sherContentText))
                    getActivity().showToast("Sher not found");
                else
                    stringBuilder.append(sherContentText);
                stringBuilder.append("\n\n");
                stringBuilder.append(sherContent.getPoetName());
                stringBuilder.append("\n");
                stringBuilder.append(sherContent.getLink());
                BaseActivity.shareTheUrl(stringBuilder.toString(), getActivity());
            }
            break;
            case R.id.sher_clipbordIcon:
                if (sherContent == null)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                String sherContentText = MyHelper.getSherContentText(sherContent.getRenderText());
                if (TextUtils.isEmpty(sherContentText))
                    getActivity().showToast("Sher not found");
                else
                    stringBuilder.append(sherContentText);
                copyToClipBoard(sherContentText);
                break;
            case R.id.sher_ghazalIcon:
                if (onGhazalClick != null)
                    onGhazalClick.onClick(view);
                break;
            case R.id.imgCritiqueInfo:
                if (onEnableCritique != null)
                    onEnableCritique.onClick(view);
                break;
            case R.id.sher_translateIcon:
                if (sherContent != null) {
                    sherContent.setShouldShowTranslation(!sherContent.isShouldShowTranslation());
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                }
                break;
            case R.id.sherTagIcon:
                break;
            case R.id.sher_poet_name:
                getActivity().startActivity(PoetDetailActivity.getInstance(getActivity(), sherContent.getPI()));
                break;
        }
    }

    public void copyToClipBoard(String sherContentText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Sher", sherContentText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), AppErrorMessage.poetsher_adapter_copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    public SherViewHolder(View view, BaseActivity activity) {
        ButterKnife.bind(this, view);
        this.activity = activity;
    }

    public void setAdapter(BaseMyAdapter adapter) {
        this.adapter = adapter;
    }

    public void setOnTagClick(View.OnClickListener onTagClick) {
        this.onTagClick = onTagClick;
    }

    public void setOnGhazalClick(View.OnClickListener onGhazalClick) {
        this.onGhazalClick = onGhazalClick;
    }

    public void setOnEnableCritiqueClick(View.OnClickListener onCritiqueClick) {
        this.onEnableCritique = onCritiqueClick;
    }
    private void updateCritiqueUI(){
        if (currentPageType == PAGE_TYPE_BASIC) {
            imgCritiqueInfo.setImageResource(R.drawable.ic_critiquefilled);
            imgCritiqueInfo.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
            currentPageType=PAGE_TYPE_CRITIQUE_ENABLED;
        } else {
            imgCritiqueInfo.setImageResource(R.drawable.ic_critique);
            imgCritiqueInfo.setColorFilter(getActivity().getAppIconColor());
            currentPageType=PAGE_TYPE_BASIC;
        }
    }
}