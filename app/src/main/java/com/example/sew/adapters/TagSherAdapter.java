package com.example.sew.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.Line;
import com.example.sew.models.Para;
import com.example.sew.models.SherContent;
import com.example.sew.models.SherTag;
import com.example.sew.views.TitleTextViewType6;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.view.View.VISIBLE;

public class TagSherAdapter extends BaseMyAdapter {
    private ArrayList<SherContent> sherContents;
    private View.OnClickListener onTagClick, onGhazalClick, onPoetNameClick;

    public TagSherAdapter(BaseActivity activity, ArrayList<SherContent> sherContents) {
        super(activity);
        this.sherContents = sherContents;
    }

    @Override
    public int getCount() {
        return sherContents.size();
    }

    @Override
    public SherContent getItem(int position) {
        return sherContents.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SherViewHolder sherViewHolder;
        SherContent sherContent = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_tag_shers);
            sherViewHolder = new SherViewHolder(convertView);
        } else
            sherViewHolder = (SherViewHolder) convertView.getTag();
        convertView.setTag(sherViewHolder);
        convertView.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sher_clipbordIcon.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sher_shareIcon.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sherGhazalIcon.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sherHeartIcon.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sherTranslateIcon.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sherPoetName.setTag(R.id.tag_data, sherContent);
        sherViewHolder.sherPoetName.setText(sherContent.getPoetName());
        if (!TextUtils.isEmpty(sherContent.getGhazalID()))
            sherViewHolder.sherGhazalIcon.setVisibility(View.VISIBLE);
        else
            sherViewHolder.sherGhazalIcon.setVisibility(View.GONE);
        if (sherContent.isShouldShowTranslation()) {
            if (sherContent.getRenderText().size() > 0 && sherContent.getRenderText().get(0).getTranslations().size() > 0) {
                sherViewHolder.sherTranslateIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                sherViewHolder.translatedtextsher.setVisibility(View.VISIBLE);
                sherViewHolder.translatedtextsher.setText(getTranslationText(sherContent.getRenderText().get(0)));
            } else {
                sherViewHolder.sherTranslateIcon.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                sherViewHolder.translatedtextsher.setVisibility(View.GONE);
            }
        } else {
            sherViewHolder.sherTranslateIcon.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
            sherViewHolder.translatedtextsher.setVisibility(View.GONE);
        }

        if (CollectionUtils.isEmpty(sherContent.getRenderText())) {
            sherViewHolder.laySher.removeAllViews();
            sherViewHolder.sherTranslateIcon.setVisibility(View.GONE);
        } else {
            RenderHelper.RenderContentBuilder.Builder(getActivity())
                    .setLayParaContainer(sherViewHolder.laySher)
                    .setParas(sherContent.getRenderText().get(0))
                    .setLeftRightPadding((int) Utils.pxFromDp(32))
                    .setOnWordClick(onWordClickListener)
                    .setOnWordLongClick(onWordLongClick)
                    .Build();
            if (sherContent.getRenderText().size() > 0 && sherContent.getRenderText().get(0).getTranslations().size() > 0)
                sherViewHolder.sherTranslateIcon.setVisibility(View.VISIBLE);
            else
                sherViewHolder.sherTranslateIcon.setVisibility(View.GONE);
        }
//                setTags(sherViewHolder, sherContent);

        sherViewHolder.layTags.setVisibility(VISIBLE);
        sherViewHolder.txtTagsTitle.setMovementMethod(LinkMovementMethod.getInstance());

        switch (sherContent.getSherTags().size()) {
            case 0:
                sherViewHolder.layTags.setVisibility(View.GONE);
                break;
            case 1: {
                SherTag sherTag_1 = sherContent.getSherTags().get(0);
                sherViewHolder.txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherContent, sherTag_1, sherViewHolder.txtTagsTitle), boldSpan()));
            }
            break;
            case 2: {
                SherTag sherTag_1 = sherContent.getSherTags().get(0);
                SherTag sherTag_2 = sherContent.getSherTags().get(1);
                sherViewHolder.txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherContent, sherTag_1, sherViewHolder.txtTagsTitle), boldSpan())
                        .append(", ")
                        .append(new Spanny(sherTag_2.getTagName(), new CustomClickableSpan(sherContent, sherTag_2, sherViewHolder.txtTagsTitle), boldSpan())));
            }
            break;
            default:
                SherTag sherTag_1 = sherContent.getSherTags().get(0);
                sherViewHolder.txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherContent, sherTag_1, sherViewHolder.txtTagsTitle), boldSpan())
                        .append(String.format(" %s ", MyHelper.getString(R.string.and)))
                        .append(new Spanny(String.format(Locale.getDefault(), "%d %s", (sherContent.getSherTags().size() - 1), MyHelper.getString(R.string.more_extra)), new CustomClickableSpan(sherContent, null, sherViewHolder.txtTagsTitle), boldSpan()))
                );
                break;
        }


        addFavoriteClick(sherViewHolder.sherHeartIcon, sherContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(sherViewHolder.sherHeartIcon, sherContent.getId());


        return convertView;
    }

    private StyleSpan boldSpan() {
        //return new StyleSpan(android.graphics.Typeface.BOLD);
        return new StyleSpan(Typeface.NORMAL);
    }

    public class CustomClickableSpan extends ClickableSpan {
        SherTag sherTag;
        SherContent sherContent;
        View txtTagsTitle;

        CustomClickableSpan(SherContent sherContent, SherTag sherTag, final View txtTagsTitle) {
            this.sherTag = sherTag;
            this.sherContent = sherContent;
            this.txtTagsTitle = txtTagsTitle;
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (sherTag == null) {
                Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, txtTagsTitle);

//                PopupMenu popup = new PopupMenu(getActivity(), txtTagsTitle);
                for (int i = 1; i < sherContent.getSherTags().size(); i++) {
                    SherTag sherTag = sherContent.getSherTags().get(i);
                    popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, sherTag.getTagName());
                }
                popup.setOnMenuItemClickListener(item -> {
                    showTagSpecificSherCollection(sherContent.getSherTags().get(item.getOrder()));
                    return true;
                });
                popup.show();
            } else
                showTagSpecificSherCollection(sherTag);
        }

        void showTagSpecificSherCollection(SherTag sherTag) {
            // getActivity().startActivity(SherCollectionActivity.getInstance(getActivity(), sherTag));
            getActivity().startActivity(SherTagOccasionActivity.getInstance(getActivity(), sherTag));
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.linkColor = getClickableTextColor();
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private int getClickableTextColor() {
        return ContextCompat.getColor(getActivity(), R.color.dark_blue);
    }

    private View.OnLongClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        if (para == null)
            return false;
        String shareContentText = MyHelper.getSherContentText(para);
        MyHelper.shareTheText(shareContentText, getActivity());
        MyHelper.copyToClipBoard(shareContentText, getActivity());
        return false;
    };

    private String getTagName(SherContent sherContent, int position) {
        if (sherContent == null || position < 0)
            return "";
        return sherContent.getSherTags().get(position).getTagName();
    }

    View.OnClickListener onWordClickListener;

    public void setOnWordClickListener(View.OnClickListener onWordClickListener) {
        this.onWordClickListener = onWordClickListener;
    }

    public void setOnTagClick(View.OnClickListener onTagClick) {
        this.onTagClick = onTagClick;
    }

    public void setOnGhazalClickListener(View.OnClickListener onGhazalClick) {
        this.onGhazalClick = onGhazalClick;
    }

    public void setOnPoetNameClickListener(View.OnClickListener onPoetNameClick) {
        this.onPoetNameClick = onPoetNameClick;
    }

    private String getTranslationText(Para para) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (Line line : para.getTranslations()) {
            stringBuilder.append(line.getFullText());
            if (para.getTranslations().indexOf(line) != (para.getTranslations().size() - 1))
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    class SherViewHolder {

        @BindView(R.id.txtTagsTitle)
        TitleTextViewType6 txtTagsTitle;
        @BindView(R.id.layTags)
        LinearLayout layTags;

        @BindView(R.id.laySher)
        LinearLayout laySher;
        @BindView(R.id.translatedtextsher)
        TextView translatedtextsher;
        @BindView(R.id.sher_heartIcon)
        ImageView sherHeartIcon;
        @BindView(R.id.sher_ghazalIcon)
        ImageView sherGhazalIcon;
        @BindView(R.id.sher_translateIcon)
        ImageView sherTranslateIcon;
        @BindView(R.id.sherTagIcon)
        ImageView sherTagIcon;
        @BindView(R.id.sher_poet_name)
        TextView sherPoetName;


        @BindView(R.id.sher_shareIcon)
        ImageView sher_shareIcon;
        @BindView(R.id.sher_clipbordIcon)
        ImageView sher_clipbordIcon;

        @BindView(R.id.sherFirstTagView)
        TitleTextViewType6 sherFirstTagView;
        @BindView(R.id.shersandText)
        TitleTextViewType6 shersandText;
        @BindView(R.id.shermoreTagView)
        TitleTextViewType6 shermoreTagView;
        @BindView(R.id.tagContainer)
        View tagContainer;

        @OnClick({R.id.sher_heartIcon, R.id.sher_shareIcon, R.id.sher_clipbordIcon, R.id.sher_ghazalIcon, R.id.sher_translateIcon, R.id.sherTagIcon, R.id.sher_poet_name})
        public void onViewClicked(View view) {
            SherContent sherContent = null;
            if (view.getTag(R.id.tag_data) != null && view.getTag(R.id.tag_data) instanceof SherContent)
                sherContent = (SherContent) view.getTag(R.id.tag_data);
            switch (view.getId()) {
                case R.id.sher_heartIcon:
                    break;
                case R.id.sher_shareIcon: {
                    if (sherContent == null)
                        return;
                    StringBuilder stringBuilder = new StringBuilder();
                    String sherContentText = MyHelper.getSherContentText(sherContent.getRenderText());
                    if (TextUtils.isEmpty(sherContentText))
                        getActivity().showToast("Sher not found");
                    else
                        stringBuilder.append(sherContentText);
                    stringBuilder.append("");
                    stringBuilder.append(sherContent.getPoetName());
                    stringBuilder.append("\n");
                    stringBuilder.append(sherContent.getLink());
                    BaseActivity.shareTheUrl(stringBuilder.toString(), getActivity());
                }
                break;
                case R.id.sher_clipbordIcon:

                    String sherContentText = "";
                    if (sherContent != null)
                        sherContentText = MyHelper.getSherContentText(sherContent.getRenderText());
                    if (TextUtils.isEmpty(sherContentText))
                        getActivity().showToast("Sher not found");
                    else
                        copyToClipBoard(sherContentText);
                    break;
                case R.id.sher_ghazalIcon:
                    if (onGhazalClick != null)
                        onGhazalClick.onClick(view);
                    break;
                case R.id.sher_poet_name:
                    if (onPoetNameClick != null)
                        onPoetNameClick.onClick(view);
                    break;
                case R.id.sher_translateIcon:
                    if (sherContent != null) {
                        sherContent.setShouldShowTranslation(!sherContent.isShouldShowTranslation());
                        notifyDataSetChanged();
                    }
                    break;
                case R.id.sherTagIcon:
                    break;
            }
        }

        SherViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void copyToClipBoard(String sherContentText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Sher", sherContentText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), AppErrorMessage.poetsher_adapter_copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}
