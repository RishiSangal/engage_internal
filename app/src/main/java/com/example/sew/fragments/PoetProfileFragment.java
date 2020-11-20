package com.example.sew.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.PoetDetailActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetPoetProfile;
import com.example.sew.common.Enums;
import com.example.sew.common.JustifiedTextView;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.Utils;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.Para;
import com.example.sew.models.PoetCompleteProfile;
import com.example.sew.models.PoetDetail;
import com.example.sew.models.PoetSignatureSher;
import com.example.sew.models.PoetUsefulLink;
import com.example.sew.models.WordContainer;
import com.example.sew.views.SquareImageView;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoetProfileFragment extends BasePoetProfileFragment {
    @BindView(R.id.poetProfilePoetImg)
    SquareImageView poetProfilePoetImg;
    @BindView(R.id.txtPoetName)
    TextView txtPoetName;
    @BindView(R.id.txtPoetTenure)
    TextView txtPoetTenure;
    @BindView(R.id.txtPoetBioTitle)
    TextView txtPoetBioTitle;
    @BindView(R.id.txtPoetShortDescription)
    TextView txtPoetShortDescription;
    @BindView(R.id.layPoetDetailsPlaceholder)
    LinearLayout layPoetDetailsPlaceholder;
    @BindView(R.id.layPoetSherPlaceholder)
    LinearLayout layPoetSherPlaceholder;
    @BindView(R.id.txtPoetDescription)
    TextView txtPoetDescription;
    @BindView(R.id.txtReadMoreBio)
    TextView txtReadMoreBio;
    @BindView(R.id.btnEnglishLanguage)
    Button btnEnglishLanguage;
    @BindView(R.id.txtAnd1)
    TextView txtAnd1;
    @BindView(R.id.btnUrduLanguage)
    Button btnUrduLanguage;
    @BindView(R.id.txtAnd2)
    TextView txtAnd2;
    @BindView(R.id.btnHindiLanguage)
    Button btnHindiLanguage;
    @BindView(R.id.layContentUnavailable)
    LinearLayout layContentUnavailable;
    @BindView(R.id.txtUsefulLinksTitle)
    TextView txtUsefulLinksTitle;
    @BindView(R.id.layUsefulLinksPlaceholder)
    FlexboxLayout layUsefulLinksPlaceholder;
    @BindView(R.id.layUsefulLinks)
    LinearLayout layUsefulLinks;
    @BindView(R.id.layPoetShortDescription)
    LinearLayout layPoetShortDescription;
    @BindView(R.id.layPoetSherContainer)
    View layPoetSherContainer;
    @BindView(R.id.laySeeFullGhazal)
    View laySeeFullGhazal;
    @BindView(R.id.txtSeeFullGhazal)
    TextView txtSeeFullGhazal;
    @BindView(R.id.wvPoetDescription)
    WebView wvPoetDescription;
    @BindView(R.id.txtOneLinearDescription)
    TextView txtOneLinearDescription;
    @BindView(R.id.imgShareUrl)
    ImageView imgShareUrl;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;

    private static final String baseUrl = "file:///android_asset/";
    private static final String mimeType = "text/html; charset=UTF-8";
    private static final String historyUrl = "about:blank";
    private static final String encoding = null;

    @OnClick(R.id.laySeeFullGhazal)
    void onSeeFullGhazalClick() {
//        ContentType contentType = MyHelper.getContentBySlug(featured.getContentTypeSlug());
        if (poetCompleteProfile == null)
            return;
        GetActivity().startActivity(RenderContentActivity.getInstance(getActivity(), poetCompleteProfile.getPoetSignatureSher().getParentSlug()));
    }

    @OnClick({R.id.btnUrduLanguage, R.id.btnHindiLanguage, R.id.btnEnglishLanguage, R.id.imgShareUrl})
    void onLanguageChangeClick(View view) {
        switch (view.getId()) {
            case R.id.btnUrduLanguage:
                MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
                break;
            case R.id.btnHindiLanguage:
                MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
                break;
            case R.id.btnEnglishLanguage:
                MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
                break;
            case R.id.imgShareUrl:
                MyHelper.shareContent(poetCompleteProfile.getPoetDetail().getPoetName() + "\n" + poetCompleteProfile.getPoetDetailContentHeader().getShortUrl());
                break;
        }
        BaseActivity.sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
    }

    public static BasePoetProfileFragment getInstance(@NonNull PoetDetail poetDetail) {
        return getInstance(poetDetail, new PoetProfileFragment());
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        getPoetCompleteProfile();
    }

    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateUI();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poet_profile, container, false);
        ButterKnife.bind(this, view);
     // setSelectableItemForeground(btnEnglishLanguage, btnHindiLanguage, btnUrduLanguage);
        wvPoetDescription.setOnLongClickListener(v -> true);
        wvPoetDescription.setLongClickable(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPoetCompleteProfile();

    }

    private BaseActivity baseActivity;

    private void getPoetCompleteProfile() {
        if (GetActivity() == null)
            return;
        baseActivity = GetActivity();
        //showDialog();
        new GetPoetProfile()
                .setPoetId(getPoetDetail().getPoetId())
                .runAsync((BaseServiceable.OnApiFinishListener<GetPoetProfile>) getPoetProfile -> {
                    if (baseActivity != null)
                        baseActivity.dismissDialog();
                    if (getActivity() == null)
                        return;
                    if (getPoetProfile.isValidResponse()) {
                        poetCompleteProfile = getPoetProfile.getPoetCompleteProfile();
                        updateUI();
                    } else
                        showToast(getPoetProfile.getErrorMessage());
                });
    }

    private PoetCompleteProfile poetCompleteProfile;


    private void updateUI() {
        if (poetCompleteProfile == null)
            return;
        layPoetDetailsPlaceholder.removeAllViews();
        layContentUnavailable.setVisibility(View.GONE);
        btnEnglishLanguage.setVisibility(View.GONE);
        btnHindiLanguage.setVisibility(View.GONE);
        btnUrduLanguage.setVisibility(View.GONE);
        txtAnd1.setVisibility(View.GONE);
        txtAnd2.setVisibility(View.GONE);
        txtAnd1.setText(MyHelper.getString(R.string.and));
        txtAnd2.setText(MyHelper.getString(R.string.and));
        txtSeeFullGhazal.setText(MyHelper.getString(R.string.see_full_ghazal));
        PoetDetail poetDetail = poetCompleteProfile.getPoetDetail();
        if (TextUtils.isEmpty(poetDetail.getAdditionalInfo())) {
            layPoetShortDescription.setVisibility(View.GONE);
        } else {
            layPoetShortDescription.setVisibility(View.VISIBLE);
            txtPoetShortDescription.setText(poetDetail.getAdditionalInfo());
        }
        txtOneLinearDescription.setText(poetDetail.getShortDescription());
        txtPoetName.setText(poetDetail.getPoetName());
        txtUsefulLinksTitle.setText(MyHelper.getString(R.string.useful_links));

        boolean strictMode = true;
        String unescapedString = org.jsoup.parser.Parser.unescapeEntities(poetDetail.getDescription(), strictMode);
        String removeColor = unescapedString.replaceAll("color:#222222", "");
        Document doc = Jsoup.parse(removeColor);
        Elements divElements = doc.select("div");
        divElements.tagName("span");
        Elements pElements = doc.select("p");
        pElements.tagName("span");
        removeColor = doc.html();
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
            wvPoetDescription.setVisibility(View.VISIBLE);
            txtPoetDescription.setVisibility(View.GONE);
            wvPoetDescription.getSettings().setSupportZoom(false);
            wvPoetDescription.setBackgroundColor(Color.parseColor("#00000000"));
            wvPoetDescription.loadDataWithBaseURL(baseUrl, getStyledFontUrdu(removeColor), mimeType, encoding, historyUrl);
        } else {
            txtPoetDescription.setVisibility(View.VISIBLE);
            wvPoetDescription.setVisibility(View.GONE);
            txtPoetDescription.setText(MyHelper.fromHtml(unescapedString.trim()));
//            txtPoetDescription.setText(unescapedString);
        }
        txtPoetBioTitle.setText(poetDetail.getPoetName());
//        if (TextUtils.isEmpty(poetDetail.getShortDescription()))
//            layPoetShortDescription.setVisibility(View.GONE);
//        else
//            layPoetShortDescription.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(poetDetail.getPoetTenure()) && !TextUtils.isEmpty(poetDetail.getDomicile())) {
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                String[] split = poetDetail.getPoetTenure().split(" ");
                String result = "";
                for (int i = split.length - 1; i >= 0; i--) {
                    result += (split[i] + " ");
                }
                StringBuilder tenureBuilder = new StringBuilder();
                tenureBuilder.append(result.trim());
                tenureBuilder.append("   |   ");
                tenureBuilder.append(poetDetail.getDomicile());
                txtPoetTenure.setText(tenureBuilder);
            } else {
                StringBuilder tenureBuilder = new StringBuilder();
                tenureBuilder.append(poetDetail.getPoetTenure());
                tenureBuilder.append("   |   ");
                tenureBuilder.append(poetDetail.getDomicile());
                txtPoetTenure.setText(tenureBuilder);
            }
        } else {
            txtPoetTenure.setVisibility(View.GONE);
        }
        ImageHelper.setImage(poetProfilePoetImg, poetDetail.getPoetImage(), Enums.PLACEHOLDER_TYPE.PROFILE_LARGE);
        showPoetCustomFields();
        // justifyText(txtPoetDescription);
        justifyText(txtPoetShortDescription);
        if (!CollectionUtils.isEmpty(poetCompleteProfile.getPoetSignatureSher().getRenderText())) {
            layPoetSherContainer.setVisibility(View.VISIBLE);
            PoetSignatureSher poetSignatureSher = poetCompleteProfile.getPoetSignatureSher();
//            ContentType contentType = MyHelper.getContentBySlug(poetSignatureSher.getParentTypeSlug());
            if (TextUtils.isEmpty(poetSignatureSher.getParentTypeSlug()))
                laySeeFullGhazal.setVisibility(View.GONE);
            else
                laySeeFullGhazal.setVisibility(View.VISIBLE);
            RenderHelper.RenderContentBuilder.Builder(GetActivity())
                    .setLayParaContainer(layPoetSherPlaceholder)
                    .setTextAlignment(Enums.TEXT_ALIGNMENT.CENTER)
                    .setLeftRightPadding((int) Utils.pxFromDp(38))
                    .setParas(poetCompleteProfile.getPoetSignatureSher().getRenderText().get(0))
                    .setOnWordClick(onSherWordClick)
                    .setOnWordLongClick(onWordLongClick)
                    .Build();

        } else {
            layPoetSherContainer.setVisibility(View.GONE);
            laySeeFullGhazal.setVisibility(View.GONE);
        }
        layUsefulLinksPlaceholder.removeAllViews();
        if (!CollectionUtils.isEmpty(poetCompleteProfile.getPoetUsefulLinks())) {
            layUsefulLinks.setVisibility(View.VISIBLE);
            for (int i = 0; i < poetCompleteProfile.getPoetUsefulLinks().size(); i++) {
                PoetUsefulLink poetUsefulLink = poetCompleteProfile.getPoetUsefulLinks().get(i);
                View convertView = getInflatedView(R.layout.cell_useful_links);
                convertView.setTag(poetUsefulLink);
                convertView.findViewById(R.id.layUsefulLinkContainer).setTag(poetUsefulLink);
                TextView txtUsefulLinksTitle = convertView.findViewById(R.id.txtUsefulLinksTitle);
                txtUsefulLinksTitle.setText(TextUtils.isEmpty(poetUsefulLink.getDisplayText().trim()) ? poetUsefulLink.getUrl() : poetUsefulLink.getDisplayText());
                layUsefulLinksPlaceholder.addView(convertView);
                convertView.findViewById(R.id.layUsefulLinkContainer).setOnClickListener(v -> {
                    PoetUsefulLink usefulLink = (PoetUsefulLink) v.getTag();
                    String url = usefulLink.getUrl();
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse(url));
                    startActivity(in);
                });
            }
        } else
            layUsefulLinks.setVisibility(View.GONE);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                if (!poetDetail.isEngDescriptionAvailable())
                    showContentNotAvailableMessage();
                break;
            case HINDI:
                if (!poetDetail.isHinDescriptionAvailable())
                    showContentNotAvailableMessage();
                break;
            case URDU:
                if (!poetDetail.isUrDescriptionAvailable())
                    showContentNotAvailableMessage();
                break;
        }
        addFavoriteClick(imgFavorite, poetCompleteProfile.getPoetDetail().getPoetId(), Enums.FAV_TYPES.ENTITY.getKey());
        updateFavoriteIcon(imgFavorite, poetCompleteProfile.getPoetDetail().getPoetId(), getResources().getColor(R.color.white));
    }

    private String getStyledFontUrdu(String html) {
        return getStyledFont(html, "noto_nastaliq_regular_urdu.ttf");
    }

    private String getStyledFont(String html, String fontName) {
        boolean addBodyStart = !html.toLowerCase().contains("<body>");
        boolean addBodyEnd = !html.toLowerCase().contains("</body");
        html = html.replace("color:", "").replace("font-family:", "");
        return getStyle(fontName) + (addBodyStart ? "<body>" : "") + html + (addBodyEnd ? "</body>" : "");
    }

    private String getStyle(String fontName) {
        String int2string = Integer.toHexString(GetActivity().getDarkGreyTextColor());
        String htmlColor = "#" + int2string.substring(int2string.length() - 6, int2string.length());
        return String.format("<style type=\"text/css\">@font-face {font-family: CustomFont;" +
                "src: url(\"file:///android_asset/fonts/%s\")}" +
                "body {font-family: CustomFont;color:" + htmlColor + ";font-size: medium;text-align: right;}</style>", fontName);
    }

    private View.OnLongClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        if (para == null)
            return false;
        String shareContentText = MyHelper.getSherContentText(para);
        MyHelper.shareTheText(shareContentText, GetActivity());
        MyHelper.copyToClipBoard(shareContentText, GetActivity());
        return false;
    };
    private View.OnClickListener onSherWordClick = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(GetActivity().getSupportFragmentManager(), "");
        new MeaningBottomPopupWindow(GetActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    };

    private void showPoetCustomFields() {
        PoetDetail poetDetail = poetCompleteProfile.getPoetDetail();
        if (!TextUtils.isEmpty(poetDetail.getPenName()))
            addCustomFieldToView(MyHelper.getString(R.string.pen_name), new Spanny(String.format("'%s'", poetDetail.getPenName()), boldSpan()));
        if (!TextUtils.isEmpty(poetDetail.getRealName()))
            addCustomFieldToView(MyHelper.getString(R.string.real_name), new Spanny(poetDetail.getRealName(), boldSpan()));
        String dateOfBirth = "";
        if (!TextUtils.isEmpty(poetDetail.getDateOfBirth()))
            dateOfBirth = poetDetail.getDateOfBirth();
        else if (!TextUtils.isEmpty(poetDetail.getYearOfBirth()))
            dateOfBirth = poetDetail.getYearOfBirth();
        if (!TextUtils.isEmpty(dateOfBirth) || !TextUtils.isEmpty(poetDetail.getBirthPlace())) {
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                Spanny spanny = new Spanny();
                Spanny spanny_2 = new Spanny();
                spanny.append(poetDetail.getBirthPlace(), new ForegroundColorSpan(getClickableTextBlackColor()), boldSpan());
                spanny_2.append((TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(poetDetail.getBirthPlace())) ? "" : "  |  ", boldSpan());
                spanny_2.append(dateOfBirth, boldSpan());
                addCustomFieldToView(MyHelper.getString(R.string.born), spanny, spanny_2);
            } else {
                Spanny spanny = new Spanny();
                spanny.append(dateOfBirth, boldSpan());
                spanny.append((TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(poetDetail.getBirthPlace())) ? "" : "  |  ", boldSpan())
                        .append(poetDetail.getBirthPlace(), new ForegroundColorSpan(getClickableTextBlackColor()), boldSpan());
                addCustomFieldToView(MyHelper.getString(R.string.born), spanny);
            }

        }

        String dateOfDeath = "";
        if (!TextUtils.isEmpty(poetDetail.getDateOfDeath()))
            dateOfDeath = poetDetail.getDateOfDeath();
        else if (!TextUtils.isEmpty(poetDetail.getYearOfDeath()))
            dateOfDeath = poetDetail.getYearOfDeath();
        if (!TextUtils.isEmpty(dateOfDeath)) {
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {

                Spanny spanny = new Spanny();
                Spanny spanny_2 = new Spanny();
                spanny.append(poetDetail.getDeathPlace(), new ForegroundColorSpan(getClickableTextBlackColor()), boldSpan());
                spanny_2.append((TextUtils.isEmpty(dateOfDeath) || TextUtils.isEmpty(poetDetail.getDeathPlace())) ? "" : " | ", boldSpan());
                spanny_2.append(dateOfDeath, boldSpan());
                addCustomFieldToView(MyHelper.getString(R.string.died), spanny, spanny_2);

            } else {
                Spanny spanny = new Spanny(dateOfDeath, boldSpan());
                spanny.append((TextUtils.isEmpty(dateOfDeath) || TextUtils.isEmpty(poetDetail.getDeathPlace())) ? "" : " | ", boldSpan())
                        .append(poetDetail.getDeathPlace(), new ForegroundColorSpan(getClickableTextBlackColor()), boldSpan());
                addCustomFieldToView(MyHelper.getString(R.string.died), spanny);
            }

        }
        if (!CollectionUtils.isEmpty(poetCompleteProfile.getPoetRelatedEntities())) {
            Spanny spanny = new Spanny("");
            for (int i = 0; i < poetCompleteProfile.getPoetRelatedEntities().size(); i++) {
                spanny.append(poetCompleteProfile.getPoetRelatedEntities().get(i).getEntityName(),
                        boldSpan(),
                        new ForegroundColorSpan(getClickableTextColor()),
                        new CustomClickableSpan(poetCompleteProfile.getPoetRelatedEntities().get(i).getEntityId()));
                spanny.append(String.format(" (%s)", poetCompleteProfile.getPoetRelatedEntities().get(i).getEntityRelation()), normalSpan());
                if ((i + 1) < poetCompleteProfile.getPoetRelatedEntities().size())
                    spanny.append("\n");
            }
            addCustomFieldToView(MyHelper.getString(R.string.relatives), spanny);
        }
    }

    private void showContentNotAvailableMessage() {
        String descriptionNotAvailable = MyHelper.getString(R.string.content_not_available);
        txtPoetDescription.setText(descriptionNotAvailable);
        txtPoetDescription.setVisibility(View.VISIBLE);
        txtPoetDescription.setGravity(Gravity.CENTER);
        layContentUnavailable.setVisibility(View.VISIBLE);
        boolean isEnglishAvailable = poetCompleteProfile.getPoetDetail().isEngDescriptionAvailable();
        boolean isHindiAvailable = poetCompleteProfile.getPoetDetail().isHinDescriptionAvailable();
        boolean isUrduAvailable = poetCompleteProfile.getPoetDetail().isUrDescriptionAvailable();
        if (isEnglishAvailable)
            btnEnglishLanguage.setVisibility(View.VISIBLE);
        if (isHindiAvailable)
            btnHindiLanguage.setVisibility(View.VISIBLE);
        if (isUrduAvailable)
            btnUrduLanguage.setVisibility(View.VISIBLE);
        if (isEnglishAvailable && isUrduAvailable)
            txtAnd1.setVisibility(View.VISIBLE);
        else if (isEnglishAvailable && isHindiAvailable)
            txtAnd1.setVisibility(View.VISIBLE);
        else if (isHindiAvailable && isUrduAvailable)
            txtAnd2.setVisibility(View.VISIBLE);
    }

    public class CustomClickableSpan extends ClickableSpan {
        String poetId;

        CustomClickableSpan(String poetId) {
            this.poetId = poetId;
        }

        @Override
        public void onClick(@NonNull View widget) {
            startActivity(PoetDetailActivity.getInstance(GetActivity(), poetId));
            // GetActivity().finish();
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.linkColor = getClickableTextColor();
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private void addCustomFieldToView(String title, Spanny... detail) {
        View detailView = getInflatedView(R.layout.cell_profile_custom_fields);
        TextView txtTitle = detailView.findViewById(R.id.txtTitle);
        TextView txtDescription = detailView.findViewById(R.id.txtDescription);
        TextView txtDescription2 = detailView.findViewById(R.id.txtDescription2);
        txtDescription.setMovementMethod(LinkMovementMethod.getInstance());
        txtTitle.setText(String.format("%s: ", title));
        if (detail.length < 2) {
            txtDescription2.setVisibility(View.GONE);
            txtDescription.setText(detail[0]);
        } else {
            txtDescription2.setVisibility(View.VISIBLE);
            txtDescription.setText(detail[0]);
            txtDescription2.setText(detail[1]);
        }
        layPoetDetailsPlaceholder.addView(detailView);
    }

    private StyleSpan boldSpan() {
        return new StyleSpan(Typeface.BOLD);
    }

    private StyleSpan normalSpan() {
        return new StyleSpan(Typeface.NORMAL);
    }

    private int getClickableTextColor() {
        return ContextCompat.getColor(GetActivity(), R.color.poet_profile_clickable_text_color);
    }

    private int getClickableTextBlackColor() {
        return getPrimaryTextColor();
    }

    private void justifyText(TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            JustifiedTextView.justify(textView);
        }
    }
}
