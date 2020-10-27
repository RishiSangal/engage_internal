package com.example.sew.adapters;

import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.PostSubmitCritique;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.ContentType;
import com.example.sew.models.Line;
import com.example.sew.models.Para;
import com.example.sew.models.SherContent;
import com.example.sew.models.SherTag;
import com.example.sew.views.TitleTextViewType6;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.view.View.VISIBLE;

public class TagQuoteAdapter extends BaseMyAdapter {
    private ArrayList<SherContent> sherContents;
    private View.OnClickListener onTagClick, onGhazalClick;
    private ContentType contentType;
    private int totalContentCount;


    public TagQuoteAdapter(BaseActivity activity, ArrayList<SherContent> sherContents) {
        super(activity);
        this.sherContents = sherContents;

    }

    public void setTotalContentCount(int totalContentCount) {
        this.totalContentCount = totalContentCount;
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
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isLayoutDirectionMismatched(convertView))
            convertView = null;

        QuoteViewHolder quoteViewHolder;
        SherContent sherContent = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_tag_quotes);
            quoteViewHolder = new QuoteViewHolder(convertView);
        } else
            quoteViewHolder = (QuoteViewHolder) convertView.getTag();
        convertView.setTag(quoteViewHolder);
        convertView.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.sher_clipbordIcon.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.sher_shareIcon.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.sherGhazalIcon.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.sherHeartIcon.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.sherTranslateIcon.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.imgCritiqueInfo.setTag(R.id.tag_data, sherContent);
        quoteViewHolder.imgCritiqueInfo.setOnClickListener(onEnableCritiqueClickListener);
        if (!TextUtils.isEmpty(sherContent.getGhazalID()))
            quoteViewHolder.sherGhazalIcon.setVisibility(View.VISIBLE);
        else
            quoteViewHolder.sherGhazalIcon.setVisibility(View.GONE);
        if (sherContent.isShouldShowTranslation()) {
            if (sherContent.getRenderText().size() > 0 && sherContent.getRenderText().get(0).getTranslations().size() > 0) {
                quoteViewHolder.sherTranslateIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                quoteViewHolder.translatedtextsher.setVisibility(View.VISIBLE);
                quoteViewHolder.translatedtextsher.setText(getTranslationText(sherContent.getRenderText().get(0)));
            } else {
                quoteViewHolder.sherTranslateIcon.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
                quoteViewHolder.translatedtextsher.setVisibility(View.GONE);
            }
        } else {
            quoteViewHolder.sherTranslateIcon.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
            quoteViewHolder.translatedtextsher.setVisibility(View.GONE);
        }

        if (CollectionUtils.isEmpty(sherContent.getRenderText())) {
            quoteViewHolder.laySher.removeAllViews();
            quoteViewHolder.sherTranslateIcon.setVisibility(View.GONE);
        } else {
            RenderHelper.RenderContentBuilder.Builder(getActivity())
                    .setLayParaContainer(quoteViewHolder.laySher)
                    .setParas(sherContent.getRenderText().get(0))
                    .setLeftRightPadding((int) Utils.pxFromDp(32))
                    .setIsQuote(true)
                    .setTextAlignment(Enums.TEXT_ALIGNMENT.LEFT)
                    .setOnWordClick(onWordClickListener)
                    .setOnWordLongClick(onWordLongClick)
                    .Build();
            if (sherContent.getRenderText().size() > 0 && sherContent.getRenderText().get(0).getTranslations().size() > 0)
                quoteViewHolder.sherTranslateIcon.setVisibility(View.VISIBLE);
            else
                quoteViewHolder.sherTranslateIcon.setVisibility(View.GONE);
        }
//                setTags(quoteViewHolder, sherContent);

        quoteViewHolder.layTags.setVisibility(VISIBLE);
        quoteViewHolder.txtTagsTitle.setMovementMethod(LinkMovementMethod.getInstance());

        switch (sherContent.getSherTags().size()) {
            case 0:
                quoteViewHolder.layTags.setVisibility(View.GONE);
                break;
            case 1: {
                SherTag sherTag_1 = sherContent.getSherTags().get(0);
                quoteViewHolder.txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherContent, sherTag_1, quoteViewHolder.txtTagsTitle), boldSpan()));
            }
            break;
            case 2: {
                SherTag sherTag_1 = sherContent.getSherTags().get(0);
                SherTag sherTag_2 = sherContent.getSherTags().get(1);
                quoteViewHolder.txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherContent, sherTag_1, quoteViewHolder.txtTagsTitle), boldSpan())
                        .append(", ")
                        .append(new Spanny(sherTag_2.getTagName(), new CustomClickableSpan(sherContent, sherTag_2, quoteViewHolder.txtTagsTitle), boldSpan())));
            }
            break;
            default:
                SherTag sherTag_1 = sherContent.getSherTags().get(0);
                quoteViewHolder.txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherContent, sherTag_1, quoteViewHolder.txtTagsTitle), boldSpan())
                        .append(String.format(" %s ", MyHelper.getString(R.string.and)))
                        .append(new Spanny(String.format(Locale.getDefault(), "%d %s", (sherContent.getSherTags().size() - 1), MyHelper.getString(R.string.more_extra)), new CustomClickableSpan(sherContent, null, quoteViewHolder.txtTagsTitle), boldSpan()))
                );
                break;
        }


        addFavoriteClick(quoteViewHolder.sherHeartIcon, sherContent.getId(), Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(quoteViewHolder.sherHeartIcon, sherContent.getId());


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
            //getActivity().startActivity(SherCollectionActivity.getInstance(getActivity(), sherTag));
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

    private View.OnClickListener onEnableCritiqueClickListener = v -> {
        SherContent sherContent = (SherContent) v.getTag(R.id.tag_data);
        enableCritiqueWordContainer(sherContent);
    };

    private void enableCritiqueWordContainer(SherContent sherContent) {
        Para para = sherContent.getRenderText().get(0);
        Line line = para.getLines().get(0);
        ShowCritiqueSubmitForm(1, line.getFullText(), sherContent);
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

    private void setTags(QuoteViewHolder quoteViewHolder, SherContent sherContent) {
        quoteViewHolder.shersandText.setText("");
        quoteViewHolder.sherTagIcon.setVisibility(View.VISIBLE);
        quoteViewHolder.sherFirstTagView.setVisibility(View.VISIBLE);
        quoteViewHolder.shermoreTagView.setVisibility(View.VISIBLE);
        switch (sherContent.getSherTags().size()) {
            case 0:
                quoteViewHolder.sherFirstTagView.setVisibility(View.GONE);
                quoteViewHolder.sherTagIcon.setVisibility(View.GONE);
                quoteViewHolder.shermoreTagView.setVisibility(View.GONE);
                break;
            case 1:
                quoteViewHolder.shermoreTagView.setVisibility(View.GONE);
                quoteViewHolder.shersandText.setText(" ");
                quoteViewHolder.sherFirstTagView.setText(getTagName(sherContent, 0));
                quoteViewHolder.sherFirstTagView.setTag(R.id.tag_data, sherContent.getSherTags().get(0));
                quoteViewHolder.sherFirstTagView.setOnClickListener(onTagClick);
                break;
            case 2:
                quoteViewHolder.shersandText.setText(",");
                quoteViewHolder.sherFirstTagView.setText(getTagName(sherContent, 0));
                quoteViewHolder.sherFirstTagView.setTag(R.id.tag_data, sherContent.getSherTags().get(0));
                quoteViewHolder.sherFirstTagView.setOnClickListener(onTagClick);
                quoteViewHolder.shermoreTagView.setText(getTagName(sherContent, 1));
                quoteViewHolder.shermoreTagView.setTag(R.id.tag_data, sherContent.getSherTags().get(1));
                quoteViewHolder.shermoreTagView.setOnClickListener(onTagClick);
                break;
            default:
                quoteViewHolder.shersandText.setText(MyHelper.getString(R.string.and));
                quoteViewHolder.shermoreTagView.setText(String.format(Locale.getDefault(), "%d %s", sherContent.getSherTags().size() - 1, MyHelper.getString(R.string.more_extra)));
                quoteViewHolder.sherFirstTagView.setText(getTagName(sherContent, 0));
                quoteViewHolder.sherFirstTagView.setTag(R.id.tag_data, sherContent.getSherTags().get(0));
                quoteViewHolder.sherFirstTagView.setOnClickListener(onTagClick);
                quoteViewHolder.shermoreTagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context wrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
                        PopupMenu popup = new PopupMenu(wrapper, quoteViewHolder.sherFirstTagView);

//                        PopupMenu popup = new PopupMenu(getActivity(), quoteViewHolder.sherFirstTagView);
                        for (int i = 1; i < sherContent.getSherTags().size(); i++) {
                            popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, getTagName(sherContent, i));
                        }
//                                popup.getMenuInflater().inflate(R.menu.sher_tag_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                View tmpView = new View(getActivity());
                                tmpView.setTag(R.id.tag_data, sherContent.getSherTags().get(item.getOrder()));
                                if (onTagClick != null)
                                    onTagClick.onClick(tmpView);
                                return true;
                            }
                        });

                        popup.show();
                    }
                });

                break;
        }
    }

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

    private String getTranslationText(Para para) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (Line line : para.getTranslations()) {
            stringBuilder.append(line.getFullText());
            if (para.getTranslations().indexOf(line) != (para.getTranslations().size() - 1))
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    
    

    class QuoteViewHolder {

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
        @BindView(R.id.imgCritiqueInfo)
        ImageView imgCritiqueInfo;

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

        @OnClick({R.id.sher_heartIcon, R.id.sher_shareIcon, R.id.sher_clipbordIcon, R.id.sher_ghazalIcon, R.id.sher_translateIcon, R.id.sherTagIcon})
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

        QuoteViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public void copyToClipBoard(String sherContentText) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Sher", sherContentText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), AppErrorMessage.poetsher_adapter_copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    Dialog critiqueSubmitForm;

    public void ShowCritiqueSubmitForm(final int lineNumber, final String ghazalLine, SherContent sherContent) {
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
                getActivity().showDialog();
                new PostSubmitCritique()
                        .setTypeOfQuery(Enums.CRITIQUE_TYPE.CRITIQUE)
                        .setMessage(message)
                        .setName(name)
                        .setEmail(email)
                        .setPageUrl(sherContent.getLink())
                        .setContentId(sherContent.getId())
                        .setContentTitle(sherContent.getTitle())
                        .setSubject(String.format(Locale.getDefault(), "LINE #%d %s", lineNumber, ghazalLine))
                        .runAsync((BaseServiceable.OnApiFinishListener<PostSubmitCritique>) submitCritique -> {
                            getActivity().dismissDialog();
                            if (submitCritique.isValidResponse()) {
                                critiqueSubmitForm.dismiss();
                                getActivity().showToast(MyHelper.getString(R.string.thankyou_for_your_feedback));
                                // ShowThankYouForCritique();
                            } else
                                getActivity().showToast(submitCritique.getErrorMessage());
                        });
            }
        });
        critiqueCancelText.setOnClickListener(view -> {
            critiqueSubmitForm.dismiss();
            //  updateCritiqueUI(quoteViewHolder.imgCritiqueInfo, null);
        });
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        critiqueSubmitForm.show();
        critiqueSubmitForm.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
