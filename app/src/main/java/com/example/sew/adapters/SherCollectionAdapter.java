package com.example.sew.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.SherViewHolder;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.SherCollectionActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.Utils;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.BaseSherTag;
import com.example.sew.models.Comment;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.Line;
import com.example.sew.models.OccasionCollection;
import com.example.sew.models.Para;
import com.example.sew.models.SherCollection;
import com.example.sew.models.SherContent;
import com.example.sew.models.SherTag;
import com.example.sew.models.WordContainer;
import com.example.sew.views.TitleTextViewType5;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.srodrigo.androidhintspinner.HintAdapter;

import static android.view.View.VISIBLE;

public class SherCollectionAdapter extends BaseMyAdapter {
    private ArrayList<SherContent> sherContents;
    private ArrayList<SherContent> selectedSher= new ArrayList<>();
    private View.OnClickListener onTagClick, onGhazalClick,onCitiqueClick;
    private Enums.SHER_COLLECTION_TYPE sherCollectionType;
    private SherCollection sherCollection;
    private HomeSherCollection homeSherCollection;
    private OccasionCollection occasionCollection;
    private BaseSherTag sherTag;
    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_CONTENT = 1;
    public static final int PAGE_TYPE_BASIC = 1;
    public static final int PAGE_TYPE_CRITIQUE_CONTROLS = 2;
    public static final int PAGE_TYPE_CRITIQUE_ENABLED = 3;
    public int currentPageType = PAGE_TYPE_BASIC;
    private int totalCount;
    public SherCollectionAdapter(BaseActivity activity, ArrayList<SherContent> sherContents, int totalCounts) {
        super(activity);
        this.sherContents = sherContents;
        totalCount = totalCounts;
    }

    @Override
    public int getCount() {
        return sherContents.size() + 1;
    }

    @Override
    public SherContent getItem(int position) {
        return sherContents.get(position - 1);
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    SherViewHolder sherViewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                SherCollectionHeaderViewHolder sherCollectionHeaderViewHolder;
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_sher_collection_haeder);
                    sherCollectionHeaderViewHolder = new SherCollectionHeaderViewHolder(convertView);
                } else
                    sherCollectionHeaderViewHolder = (SherCollectionHeaderViewHolder) convertView.getTag();
                sherCollectionHeaderViewHolder.txtSherTitle.setVisibility(View.GONE);
                convertView.setTag(sherCollectionHeaderViewHolder);
                sherCollectionHeaderViewHolder.linear.setVisibility(View.VISIBLE);
                switch (sherCollectionType) {
                    case TOP_20:
                        sherCollectionHeaderViewHolder.txtSherTitle.setVisibility(View.VISIBLE);
                        if (sherCollection != null) {
                            sherCollectionHeaderViewHolder.txtTitle.setText(String.format("%s 20", MyHelper.getString(R.string.top_20)));
                            sherCollectionHeaderViewHolder.txtSherTitle.setText(sherCollection.getTitle());
                            sherCollectionHeaderViewHolder.txtContentType.setText(MyHelper.getString(R.string.sher).toUpperCase());
                            sherCollectionHeaderViewHolder.txtCount.setText(String.valueOf(totalCount));
                        } else {
                            sherCollectionHeaderViewHolder.txtTitle.setText(String.format("%s 20", MyHelper.getString(R.string.top_20)));
                            sherCollectionHeaderViewHolder.txtSherTitle.setText(homeSherCollection.getName());
                            sherCollectionHeaderViewHolder.txtContentType.setText(MyHelper.getString(R.string.sher).toUpperCase());
                            sherCollectionHeaderViewHolder.txtCount.setText(String.valueOf(totalCount));
                        }
                        break;
                    case TAG:
                        sherCollectionHeaderViewHolder.txtTitle.setText(String.format("%s %s", sherTag.getTagName(), MyHelper.getString(R.string.shayari)));
                        sherCollectionHeaderViewHolder.txtContentType.setText(MyHelper.getString(R.string.sher).toUpperCase());
                        sherCollectionHeaderViewHolder.txtCount.setText(String.valueOf(totalCount));
                        break;
                    case OCCASIONS:
                        if (occasionCollection != null) {
                            ImageHelper.setImage(sherCollectionHeaderViewHolder.imgOccasionImage, occasionCollection.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
                            sherCollectionHeaderViewHolder.txtContentType.setText(MyHelper.getString(R.string.sher).toUpperCase());
                            sherCollectionHeaderViewHolder.txtCount.setText(String.valueOf(totalCount));
                        } else {
                            ImageHelper.setImage(sherCollectionHeaderViewHolder.imgOccasionImage, homeSherCollection.getOccasionImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_COLLECTION);
                            sherCollectionHeaderViewHolder.txtContentType.setText(MyHelper.getString(R.string.sher).toUpperCase());
                            sherCollectionHeaderViewHolder.txtCount.setText(String.valueOf(totalCount));
                        }
                        break;
                    case OTHER:
                        sherCollectionHeaderViewHolder.linear.setVisibility(View.GONE);
                        sherCollectionHeaderViewHolder.txtTitle.setVisibility(VISIBLE);
                        if (homeSherCollection != null)
                            sherCollectionHeaderViewHolder.txtTitle.setText(String.format("%s", homeSherCollection.getName()));
                        break;
                }
//                loadDataForPoetHeader(poetsProfileViewHolder);
                break;
            case VIEW_TYPE_CONTENT:
                // SherViewHolder sherViewHolder;
                SherContent sherContent = getItem(position);
                if (convertView == null) {
                    convertView = getInflatedView(R.layout.cell_poet_shers);
                    sherViewHolder = new SherViewHolder(convertView, getActivity());
                } else
                    sherViewHolder = (SherViewHolder) convertView.getTag();
                sherViewHolder.setAdapter(this);
                sherViewHolder.setOnGhazalClick(onGhazalClick);
                sherViewHolder.setOnEnableCritiqueClick(onCitiqueClick);
                sherViewHolder.setOnTagClick(onTagClick);
                convertView.setTag(sherViewHolder);
                convertView.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sher_clipbordIcon.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sher_shareIcon.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sherGhazalIcon.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sherHeartIcon.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sherTranslateIcon.setTag(R.id.tag_data, sherContent);
                sherViewHolder.sher_poet_name.setTag(R.id.tag_data, sherContent);
                sherViewHolder.imgCritiqueInfo.setTag(R.id.tag_data, sherContent);
                sherViewHolder.imgCritiqueInfo.setOnClickListener(onEnableCritiqueClickListener);
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
                sherViewHolder.sher_poet_name.setText(sherContent.getPoetName());
//                sherViewHolder.sherTagIcon.setVisibility(View.VISIBLE);
                sherViewHolder.sherFirstTagView.setVisibility(View.GONE);
                sherViewHolder.shermoreTagView.setVisibility(View.GONE);
                if (sherCollectionType == Enums.SHER_COLLECTION_TYPE.TAG) {
                    for (SherTag sherTag : sherContent.getSherTags())
                        if (sherTag.getTagId().contentEquals(SherCollectionAdapter.this.sherTag.getTagId())) {
                            sherContent.getSherTags().remove(sherTag);
                            break;
                        }
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
                addFavoriteClick(sherViewHolder.sherHeartIcon, sherContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
                updateFavoriteIcon(sherViewHolder.sherHeartIcon, sherContent.getId());
                if(MyService.getSelectedLanguage()== Enums.LANGUAGE.ENGLISH || MyService.getSelectedLanguage()== Enums.LANGUAGE.HINDI)
                    sherViewHolder.layBottom.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                else
                    sherViewHolder.layBottom.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                break;
        }

        return convertView;
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
    private View.OnClickListener onEnableCritiqueClickListener = v -> {
        SherContent sherContent = (SherContent) v.getTag(R.id.tag_data);
        selectedSher.add(sherContent);
        //   if (currentPageType == PAGE_TYPE_BASIC)
        if(isEnableCritique(sherContent))
            showCritiqueConfirmationDialog(sherContent);
        else{
            selectedSher.remove(sherContent);
            sherViewHolder.imgCritiqueInfo.setImageResource(R.drawable.ic_critique);
            sherViewHolder.imgCritiqueInfo.setColorFilter(getActivity().getAppIconColor());
        }
    };

    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
        Line line = (Line) v.getTag(R.id.tag_line);
        int lineNumber = (int) v.getTag(R.id.tag_line_number);
//        if (currentPageType == PAGE_TYPE_CRITIQUE_ENABLED) {
//            ShowCritiqueSubmitForm(lineNumber, line.getFullText());
//        } else {
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(getActivity().getSupportFragmentManager(), "MEANING");
            currentPageType = PAGE_TYPE_BASIC;
            new MeaningBottomPopupWindow(getActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    //    }

    };
    public void showCritiqueConfirmationDialog(SherContent sherContent) {
        final Dialog critiqueDialog = new Dialog(getActivity());
        critiqueDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        critiqueDialog.setContentView(R.layout.critique_custom_screen);
        critiqueDialog.setCanceledOnTouchOutside(false);
        critiqueDialog.setCancelable(false);
        TextView txtCritiqueOk = critiqueDialog.findViewById(R.id.txtCritiqueOk);
        TextView txtCritiqueOnText = critiqueDialog.findViewById(R.id.txtCritiqueOnText);
        TextView txtCritiqueMessage = critiqueDialog.findViewById(R.id.txtCritiqueMessage);
        txtCritiqueOk.setText(MyHelper.getString(R.string.okay));
        txtCritiqueOnText.setText(MyHelper.getString(R.string.critique_mode_on));
        txtCritiqueMessage.setText(MyHelper.getString(R.string.tap_on_any_word));
        txtCritiqueOk.setOnClickListener(view -> {
            currentPageType = PAGE_TYPE_CRITIQUE_ENABLED;
           // updateCritiqueUI(sherViewHolder.imgCritiqueInfo,sherContent);
            critiqueDialog.dismiss();
        });
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        critiqueDialog.show();
        critiqueDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public void setOnWordClickListener(View.OnClickListener onWordClickListener) {
        this.onWordClickListener = onWordClickListener;
    }

    public void setOnTagClick(View.OnClickListener onTagClick) {
        this.onTagClick = onTagClick;
    }

    public void setOnGhazalClickListener(View.OnClickListener onGhazalClick) {
        this.onGhazalClick = onGhazalClick;
    }

    private String getTranslationText(Para para) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Line line : para.getTranslations()) {
            stringBuilder.append(line.getFullText());
            if (para.getTranslations().indexOf(line) != (para.getTranslations().size() - 1))
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void setCollectionType(Enums.SHER_COLLECTION_TYPE sherCollectionType) {
        this.sherCollectionType = sherCollectionType;
    }

    public void setContent(BaseSherTag sherTag) {
        this.sherTag = sherTag;
    }

    public void setContent(SherCollection sherCollection) {
        this.sherCollection = sherCollection;
    }

    public void setContent(HomeSherCollection homeSherCollection) {
        this.homeSherCollection = homeSherCollection;
    }

    public void setContent(OccasionCollection occasionCollection) {
        this.occasionCollection = occasionCollection;
    }


    class SherCollectionHeaderViewHolder {
        @BindView(R.id.imgOccasionImage)
        ImageView imgOccasionImage;
        @BindView(R.id.txtTitle)
        TitleTextViewType5 txtTitle;
        @BindView(R.id.txtSherTitle)
        TitleTextViewType5 txtSherTitle;
        @BindView(R.id.viewHeaderSection)
        View viewHeaderSection;
        @BindView(R.id.txtContentType)
        TextView txtContentType;
        @BindView(R.id.txtCount)
        TextView txtCount;
        @BindView(R.id.linear)
        LinearLayout linear;

        SherCollectionHeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
            imgOccasionImage.setVisibility(View.GONE);
            txtTitle.setVisibility(View.GONE);
            viewHeaderSection.setVisibility(View.GONE);
            switch (sherCollectionType) {
                case TOP_20:
                case TAG:
                case OTHER:
                    txtTitle.setVisibility(View.VISIBLE);
                    viewHeaderSection.setVisibility(View.VISIBLE);
                    break;
                case OCCASIONS:
                    imgOccasionImage.setVisibility(View.VISIBLE);
                    break;
            }
        }
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

    private StyleSpan boldSpan() {
        //return new StyleSpan(android.graphics.Typeface.BOLD);
        return new StyleSpan(Typeface.NORMAL);
    }
    Dialog critiqueSubmitForm;
    public void ShowCritiqueSubmitForm(final int lineNumber, final String ghazalLine) {
        critiqueSubmitForm = new Dialog(new ContextThemeWrapper(getActivity(), R.style.Dialog));
        critiqueSubmitForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        critiqueSubmitForm.setContentView(R.layout.submit_critique_template);
        critiqueSubmitForm.setCanceledOnTouchOutside(false);
        critiqueSubmitForm.getWindow().getDecorView().setLayoutDirection(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        TextView critiqueLine = critiqueSubmitForm.findViewById(R.id.critiqueLine);
        TextView critiqueLineNumber = critiqueSubmitForm.findViewById(R.id.critiqueLineNumber);
        ImageView userProfile = critiqueSubmitForm.findViewById(R.id.imgProfile);
        TextInputLayout citiqueEMAIL = critiqueSubmitForm.findViewById(R.id.citiqueEMAIL);
        TextInputLayout citiqueUserName = critiqueSubmitForm.findViewById(R.id.citiqueUserName);
        TextInputLayout citiqueUserCOMMENT = critiqueSubmitForm.findViewById(R.id.citiqueUserCOMMENT);
        critiqueLine.setText(ghazalLine);
        final TextView critiqueSubmitText = critiqueSubmitForm.findViewById(R.id.critiqueSubmit);
        TextView critiqueCancelText = critiqueSubmitForm.findViewById(R.id.critiqe_cancel);
        TextView txtSubmitCritiqueTitle = critiqueSubmitForm.findViewById(R.id.txtSubmitCritiqueTitle);
        ImageView closeModal = critiqueSubmitForm.findViewById(R.id.closeModal);
        closeModal.setOnClickListener(view -> critiqueSubmitForm.dismiss());
        final EditText critiqueUserName = critiqueSubmitForm.findViewById(R.id.critique_UserName);
        final EditText critiqueUserEmail = critiqueSubmitForm.findViewById(R.id.critique_UserEmail);
        final EditText critiqueUserComment = critiqueSubmitForm.findViewById(R.id.critique_UserComment);

        critiqueUserName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        critiqueUserEmail.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        critiqueUserComment.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        txtSubmitCritiqueTitle.setText(MyHelper.getString(R.string.submit_critique));
        //critiqueUserEmail.setHint(MyHelper.getString(R.string.email));
        // critiqueUserName.setHint(MyHelper.getString(R.string.name).toUpperCase());
        citiqueEMAIL.setHint(MyHelper.getString(R.string.email).toUpperCase());
        citiqueUserName.setHint(MyHelper.getString(R.string.name).toUpperCase());
        //critiqueUserComment.setHint(MyHelper.getString(R.string.critique_comment).toUpperCase());
        citiqueUserCOMMENT.setHint(MyHelper.getString(R.string.critique_comment).toUpperCase());
        critiqueCancelText.setText(MyHelper.getString(R.string.button_cancel));
        critiqueSubmitText.setText(MyHelper.getString(R.string.button_submit));
        critiqueLineNumber.setText(String.format(Locale.getDefault(), "%s #%d", MyHelper.getString(R.string.line), lineNumber));

        critiqueUserComment.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                critiqueSubmitText.performClick();
                return true;
            }
            return false;
        });
        if (MyService.isUserLogin()) {
            critiqueUserName.setText(MyService.getUser().getDisplayName());
            critiqueUserEmail.setText(MyService.getEmail());
            citiqueEMAIL.setVisibility(View.GONE);
            userProfile.setVisibility(View.GONE);
            //ImageHelper.setImage(userProfile,MyService.getUser().);
            critiqueUserName.setEnabled(false);
            critiqueUserEmail.setEnabled(false);
        } else {
            critiqueUserName.setText("");
            critiqueUserEmail.setText("");
            userProfile.setVisibility(View.GONE);
            citiqueEMAIL.setVisibility(View.VISIBLE);
        }
        critiqueSubmitText.setOnClickListener(view -> {
            String email, name, message;
            message = getActivity().getEditTextData(critiqueUserComment);
            if (MyService.isUserLogin()) {
                email = MyService.getEmail();
                name = MyService.getUser().getDisplayName();
            } else {
                email = getActivity().getEditTextData(critiqueUserEmail);
                name = getActivity().getEditTextData(critiqueUserName);
                if (TextUtils.isEmpty(name)) {
                    getActivity().showToast(AppErrorMessage.please_enter_your_name);
                    return;
                } else if (!MyHelper.isValidEmail(email)) {
                    if (TextUtils.isEmpty(email))
                        getActivity().showToast(AppErrorMessage.please_enter_your_email);
                    else
                        getActivity().showToast(AppErrorMessage.please_enter_valid_email_address);
                    return;
                }
            }
            if (TextUtils.isEmpty(message))
                getActivity().showToast(AppErrorMessage.Please_enter_your_feedback);
            else {
                getActivity().showToast("HI");
//                showDialog();
//                new PostSubmitCritique()
//                        .setTypeOfQuery(Enums.CRITIQUE_TYPE.CRITIQUE)
//                        .setMessage(message)
//                        .setName(name)
//                        .setEmail(email)
//                        .setPageUrl(contentPageModel.getUrl())
//                        .setContentId(contentPageModel.getId())
//                        .setContentTitle(contentPageModel.getTitle())
//                        .setSubject(String.format(Locale.getDefault(), "LINE #%d %s", lineNumber, ghazalLine))
//                        .runAsync((BaseServiceable.OnApiFinishListener<PostSubmitCritique>) submitCritique -> {
//                            dismissDialog();
//                            if (submitCritique.isValidResponse()) {
//                                critiqueSubmitForm.dismiss();
//                                showToast(MyHelper.getString(R.string.thankyou_for_your_feedback));
//                                // ShowThankYouForCritique();
//                                currentPageType = PAGE_TYPE_BASIC;
//                                updateBottomControls();
//                            } else
//                                showToast(submitCritique.getErrorMessage());
//                        });
            }
        });
        critiqueCancelText.setOnClickListener(view ->{
            currentPageType = PAGE_TYPE_BASIC;
            critiqueSubmitForm.dismiss();
            updateCritiqueUI(sherViewHolder.imgCritiqueInfo,null);
        });
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        critiqueSubmitForm.show();
        critiqueSubmitForm.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void updateCritiqueUI(ImageView imageView,SherContent sherContent){
        if (currentPageType == PAGE_TYPE_BASIC) {
            imageView.setImageResource(R.drawable.ic_critique);
            imageView.setColorFilter(getActivity().getAppIconColor());
            currentPageType=PAGE_TYPE_CRITIQUE_ENABLED;
        } else {
            imageView.setImageResource(R.drawable.ic_critiquefilled);
            imageView.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
            currentPageType=PAGE_TYPE_BASIC;
        }
    }

    private boolean isEnableCritique(SherContent sherContent) {
        if (sherContent == null)
            return false;
        return selectedSher.contains(sherContent);
    }
}
