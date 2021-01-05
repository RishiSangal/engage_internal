package com.example.sew.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.State;
import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAllCommentsByTargetId;
import com.example.sew.apis.GetBottomContentById;
import com.example.sew.apis.GetContentById;
import com.example.sew.apis.GetCountingSummaryByTargetid;
import com.example.sew.apis.GetMarkLikeDislike;
import com.example.sew.apis.PostSubmitCritique;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.DoubleClick;
import com.example.sew.common.DoubleClickListener;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.MyConstants;
import com.example.sew.common.Utils;
import com.example.sew.fragments.RenderContentAudioFragment;
import com.example.sew.fragments.RenderContentVideoFragment;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderActivityAudioPlayerControls;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.helpers.ServiceManager;
import com.example.sew.models.Comment;
import com.example.sew.models.ContentPageModel;
import com.example.sew.models.ContentPoet;
import com.example.sew.models.CountSummary;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.KeepReading;
import com.example.sew.models.Line;
import com.example.sew.models.MaybeLike;
import com.example.sew.models.Para;
import com.example.sew.models.PlayingAudioItem;
import com.example.sew.models.PluralContentName;
import com.example.sew.models.PreviousNextContent;
import com.example.sew.models.RenderContentAudio;
import com.example.sew.models.RenderContentTag;
import com.example.sew.models.User;
import com.example.sew.models.WordContainer;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class RenderContentActivity extends BaseActivity implements RenderActivityAudioPlayerControls.onAudioPlayerStateChanged {
    @BindView(R.id.layLoadingPlaceholder)
    View layLoadingPlaceholder;
    @BindView(R.id.layBottomShareFavorite)
    View layBottomShareFavorite;
    @BindView(R.id.layDataLoading)
    View layDataLoading;
    @BindView(R.id.txtDataLoading)
    TextView txtDataLoading;
    @BindView(R.id.layParaContainer)
    LinearLayout layParaContainer;
    @BindView(R.id.txtContentNotAvailable)
    TextView txtContentNotAvailable;
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
    @BindView(R.id.txtNextTitle)
    TextView txtNextTitle;
    @BindView(R.id.txtNextContentTitle)
    TextView txtNextContentTitle;
    @BindView(R.id.txtNextPoetName)
    TextView txtNextPoetName;
    @BindView(R.id.layNext)
    LinearLayout layNext;
    @BindView(R.id.txtPrevContentTitle)
    TextView txtPrevContentTitle;
    @BindView(R.id.txtPrevTitle)
    TextView txtPrevTitle;
    @BindView(R.id.txtPrevPoetName)
    TextView txtPrevPoetName;
    @BindView(R.id.prevLinearLayout)
    LinearLayout prevLinearLayout;
    @BindView(R.id.txtViewAllContent)
    TextView txtViewAllContent;
    @BindView(R.id.authorImg)
    ImageView authorImg;
    @BindView(R.id.txtFooterAuthorName)
    TextView txtFooterAuthorName;
    @BindView(R.id.txtFooterPoetTenure)
    TextView txtFooterPoetTenure;
    @BindView(R.id.txtFooterPoetPlace)
    TextView txtFooterPoetPlace;
    @BindView(R.id.txtDobPlaceSeparator)
    TextView txtDobPlaceSeparator;
    @BindView(R.id.txtPoetDescription)
    TextView txtPoetDescription;
    @BindView(R.id.txtFooterViewPoetProfile)
    TextView txtFooterViewPoetProfile;

    @BindView(R.id.layBottomContainer1)
    LinearLayout layBottomContainer1;
    @BindView(R.id.layMainContainer)
    LinearLayout layMainContainer;
    @BindView(R.id.frame_layout)
    GestureFrameLayout zoomLayout;
    @BindView(R.id.layFrame)
    FrameLayout layFrame;
    @BindView(R.id.txtHeaderContentTitle)
    TextView txtHeaderContentTitle;
    @BindView(R.id.txtHeaderPoetName)
    TextView txtHeaderPoetName;
    @BindView(R.id.imgHeaderPopularChoice)
    ImageView imgHeaderPopularChoice;
    @BindView(R.id.imgHEaderEditorChoice)
    ImageView imgHEaderEditorChoice;
    @BindView(R.id.layTopContainer)
    LinearLayout layTopContainer;
    @BindView(R.id.imgFooterMoreOption)
    ImageView imgFooterMoreOption;
    @BindView(R.id.imgFooterAudio)
    ImageView imgFooterAudio;
    @BindView(R.id.layAudioSection)
    LinearLayout layAudioSection;
    @BindView(R.id.imgFooterVideo)
    ImageView imgFooterVideo;
    @BindView(R.id.imgFooterShare)
    ImageView imgFooterShare;
    @BindView(R.id.imgFooterMultiShare)
    ImageView imgFooterMultiShare;
    @BindView(R.id.txtFooterMultiCopy)
    TextView txtFooterMultiCopy;
    @BindView(R.id.imgFooterShareClose)
    ImageView imgFooterShareClose;
    @BindView(R.id.imgFooterFavorite)
    ImageView imgFooterFavorite;
    @BindView(R.id.txtFavoriteFooterCount)
    TextView txtFavoriteFooterCount;

    @BindView(R.id.layInterestingFact)
    LinearLayout layInterestingFact;
    @BindView(R.id.txtInterestingFactTitle)
    TextView txtInterestingFactTitle;
    @BindView(R.id.txtInterestingFact)
    TextView txtInterestingFact;

    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.footerCrcitcOnText)
    TextView footerCrcitcOnText;
    @BindView(R.id.imgCritiqueOff)
    TextView imgCritiqueOff;
    @BindView(R.id.layCritiqueMode)
    RelativeLayout layCritiqueMode;
    @BindView(R.id.imgCritiqueClose)
    ImageView imgCritiqueClose;
    @BindView(R.id.imgCritiqueInfo)
    ImageView imgCritiqueInfo;
    @BindView(R.id.imgCritiqueShare)
    ImageView imgCritiqueShare;
    @BindView(R.id.txtFavoriteCritiqueCount)
    TextView txtFavoriteCritiqueCount;
    @BindView(R.id.imgCritiqueFav)
    ImageView imgCritiqueFav;
    @BindView(R.id.mainView)
    RelativeLayout mainView;
    @BindView(R.id.viewDividerNextPrevious)
    View viewDividerNextPrevious;
    @BindView(R.id.viewDividerLineBelowPrevious)
    View viewDividerLineBelowPrevious;
    @BindView(R.id.mainFooterContent)
    View mainFooterContent;
    @BindView(R.id.critiqueFooter)
    View critiqueFooter;
    @BindView(R.id.audioFooterBar)
    View audioFooterBar;
    @BindView(R.id.shareFooter)
    View shareFooter;
    @BindView(R.id.imgFlower)
    ImageView imgFlower;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.layFooterVideo)
    LinearLayout layFooterVideo;
    TextView txtKeepReadingTitle;
    TextView txtKeepReadingSubtitle;
    TextView txtYouMayLikeTitle;
    TextView txtYouMayLikeSubtitle;

    TextView txtRelatedContentTitle;
    LinearLayout layRelatedContentPlaceholder;
    TextView txtCommentCount;
    TextView txtComment;
    LinearLayout layKeepReadingContentPlaceholder;
    LinearLayout layYouMayLikeContentPlaceholder;
    LinearLayout layCommentPlaceholder;
    View layCommentSection;
    View layKeepReading;
    View layYouMayLike;
    View poetProfileSection;
    View layRelatedContent;
    @BindView(R.id.flexTags)
    FlexboxLayout flexTags;
    @BindView(R.id.layContentTags)
    LinearLayout layContentTags;
    @BindView(R.id.txtContentTagsTitle)
    TextView txtContentTagsTitle;
    @BindView(R.id.audioTitle)
    TextView audioTitle;
    @BindView(R.id.txtPerformedBy)
    TextView txtPerformedBy;
    @Nullable
    @BindView(R.id.poetDetailFwdBtn)
    ImageView poetDetailFwdBtn;
    @Nullable
    @BindView(R.id.poetDetailBkdBtn)
    ImageView poetDetailBkdBtn;
    @Nullable
    @BindView(R.id.imgPoetAudioImage)
    CircleImageView imgPoetAudioImage;
    @BindView(R.id.imgUpArrow)
    ImageView imgUpArrow;
    @BindView(R.id.layTranslate)
    LinearLayout layTranslate;
    @BindView(R.id.imgTranslate)
    ImageView imgTranslate;


    boolean showingTranslation = false;
    private boolean shouldCancelSingleClick;
    private String descriptionNotAvailable = MyHelper.getString(R.string.content_not_available);
    private View clickedLayout;
    boolean isMainContentLoaded;
    boolean isBottomContentLoaded;
    public static final int PAGE_TYPE_BASIC = 1;
    public static final int PAGE_TYPE_CRITIQUE_CONTROLS = 2;
    public static final int PAGE_TYPE_CRITIQUE_ENABLED = 3;
    public static final int PAGE_TYPE_AUDIO_PLAY = 4;
    public static final int PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY = 5;
    public static final int PAGE_TYPE_SHARE_SELECTION_ENABLED = 6;
    public static final int PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY = 7;
    private int desiredBottomPadding, desiredTopPadding;

    public final int REPLY_TYPE_DEFAULT = 0;
    public final int REPLY_TYPE_PARENT = 1;
    public final int REPLY_TYPE_CHILD = 2;
    public final int REPLY_TYPE_PARENT_EDIT = 3;
    public final int REPLY_TYPE_CHILD_EDIT = 4;
    public int currentViewType = REPLY_TYPE_DEFAULT;
    public static final int AUDIO_PLAY_STATE_PLAYING = 1;
    public static final int AUDIO_PLAY_STATE_NOT_PLAYING = 2;
    public static final int AUDIO_PLAY_STATE_PAUSE = 3;

    @IntDef(value = {AUDIO_PLAY_STATE_PLAYING, AUDIO_PLAY_STATE_NOT_PLAYING, AUDIO_PLAY_STATE_PAUSE})
    @interface AudioPlayingState {
    }

    @AudioPlayingState
    public int currentAudioPlayState = AUDIO_PLAY_STATE_NOT_PLAYING;

    @IntDef(value = {PAGE_TYPE_BASIC, PAGE_TYPE_CRITIQUE_CONTROLS, PAGE_TYPE_CRITIQUE_ENABLED, PAGE_TYPE_AUDIO_PLAY, PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY, PAGE_TYPE_SHARE_SELECTION_ENABLED, PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY})
    @interface PageType {
    }

    @PageType
    public int currentPageType = PAGE_TYPE_BASIC;

    public static Intent getInstance(Activity activity, String contentId) {
        Intent intent = new Intent(activity, RenderContentActivity.class);
        intent.putExtra(CONTENT_ID, contentId);
        return intent;
    }

    public static Intent getInstance(Activity activity, String contentId, String slugId) {
        Intent intent = new Intent(activity, RenderContentActivity.class);
        intent.putExtra(CONTENT_ID, contentId);
        intent.putExtra(SLUG_ID, slugId);
        return intent;
    }

    String contentId;
    String slugId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_content);
        ButterKnife.bind(this);
        layKeepReading = findViewById(R.id.layKeepReading);
        layYouMayLike = findViewById(R.id.layYouMayLike);
        layCommentSection = findViewById(R.id.layCommentSection);
        poetProfileSection = findViewById(R.id.poetProfileSection);
        layRelatedContent = findViewById(R.id.layRelatedContent);
        txtKeepReadingTitle = layKeepReading.findViewById(R.id.txtKeepReadingLikeTitle);
        txtKeepReadingSubtitle = layKeepReading.findViewById(R.id.txtKeepReadingLikeSubtitle);
        txtYouMayLikeTitle = layYouMayLike.findViewById(R.id.txtKeepReadingLikeTitle);
        txtYouMayLikeSubtitle = layYouMayLike.findViewById(R.id.txtKeepReadingLikeSubtitle);
        layKeepReadingContentPlaceholder = layKeepReading.findViewById(R.id.layContentPlaceholder);
        layYouMayLikeContentPlaceholder = layYouMayLike.findViewById(R.id.layContentPlaceholder);
        txtCommentCount = layCommentSection.findViewById(R.id.txtCommentCount);
        txtComment = layCommentSection.findViewById(R.id.txtComment);
        layRelatedContentPlaceholder = layRelatedContent.findViewById(R.id.layRelatedContentPlaceholder);
        txtRelatedContentTitle = layRelatedContent.findViewById(R.id.txtRelatedContentTitle);

        layLoadingPlaceholder.setVisibility(View.VISIBLE);
        contentId = getIntent().getStringExtra(CONTENT_ID);
        slugId = getIntent().getStringExtra(SLUG_ID);
        //  setSelectableItemForeground(btnEnglishLanguage, btnUrduLanguage, btnHindiLanguage);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getAllComments();
            }
        }, BROADCAST_RENDER_CONTENT_COMMENT_UPDATE);
        if (MyService.isUserLogin()) {
            User user = MyService.getUser();

        }

        if (isOffline()) {
            layBottomContainer1.setVisibility(GONE);
            layBottomShareFavorite.setVisibility(GONE);
            if (MyService.isFavoriteContentDetailAvailable(contentId)) {
                favContentPageModel = MyService.getDetailedFavorite(contentId);
                updateUIOffline();
            } else {
                showToast("Detailed content not available for offline, please try later");
                finish();
            }
        } else {
            layBottomContainer1.setVisibility(View.VISIBLE);
            layBottomShareFavorite.setVisibility(View.VISIBLE);
            getRenderContent();
        }

        initializeZoomParameters();
        setHeaderTitle("");
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                layDataLoading.setVisibility(View.GONE);
                if (isOffline()) {
                    if (favContentPageModel != null) {
                        boolean isEnglishAvailable = favContentPageModel.getHaveEn().contentEquals("true");
                        boolean isHindiAvailable = favContentPageModel.getHaveHi().contentEquals("true");
                        boolean isUrduAvailable = favContentPageModel.getHaveUr().contentEquals("true");
                        checkAndSetLanguageChangeValues(isEnglishAvailable, isHindiAvailable, isUrduAvailable);
                    }
                }
                if (contentPageModel != null)
                    for (Para currPara : contentPageModel.getRender().getParas()) {
                        if (currPara.getContainerView() != null) {
                            currPara.getContainerView().setOnLongClickListener(v -> {
                                onWordLongClick.onClick(v);
                                return true;
                            });
                        }
                    }
                layLoadingPlaceholder.setVisibility(GONE);
            }
        }, BROADCAST_CONTENT_RENDERED);
/*        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                contentAudioGlobal.clear();
                try {
                    PlayingAudioItem playingAudioItem = new PlayingAudioItem(new JSONObject(intent.getStringExtra(SELECTED_AUDIO)));
                    RenderContentAudio renderContentAudio = new RenderContentAudio(playingAudioItem.getAudioContent().getJsonObject());
                    for (int i = 0; i < contentPageModel.getContentAudios().size(); i++)
                        if (renderContentAudio.getId().contentEquals(contentPageModel.getContentAudios().get(i).getId())) {
                            currAudioSelectedPosition = i;
                            break;
                        }
                    onFooterAudioIconClick(currAudioSelectedPosition, playingAudioItem);
//                    playAudioBottomBar(contentAudioGlobal, audioSelectedPosition, selectedAudioStartTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }, BROADCAST_AUDIO_REFRESHED);*/

    }

    private boolean isOffline() {
        return !new ServiceManager(getActivity()).isNetworkAvailable();
    }

    int currAudioSelectedPosition = 0;
    ContentPageModel contentPageModel;
    ContentPoet contentPoet;
    PluralContentName pluralContentName;
    private PreviousNextContent nextContent;
    private PreviousNextContent previousContent;
    private ArrayList<MaybeLike> maybeLikes;
    private ArrayList<KeepReading> keepReadings;
    private ArrayList<Comment> comment;
    private ArrayList<Comment> threeCommentList;
    private ArrayList<Comment> selectedLiked = new ArrayList<Comment>();
    private ArrayList<Comment> selectedDisLiked = new ArrayList<Comment>();

    private void getRenderContent() {
        isMainContentLoaded = false;
        showDialog();
        new GetContentById()
                .setContentId(contentId)
                .runAsync((BaseServiceable.OnApiFinishListener<GetContentById>) getContentById -> {
                    if (getContentById.isValidResponse()) {
                        contentPageModel = getContentById.getContentPageModel();
                        if (contentPageModel == null)
                            finish();
                        isMainContentLoaded = true;
                        contentId = contentPageModel.getId();
                        getCountSummarybyTargetId();
                        // getBottomContent();
                        updateUI();
                    } else {
                        finish();
                        showToast(getContentById.getErrorMessage());
                    }
                });
    }

    CountSummary countSummary;

    private void getCountSummarybyTargetId() {
        new GetCountingSummaryByTargetid().setTargetId(contentId).runAsync((BaseServiceable.OnApiFinishListener<GetCountingSummaryByTargetid>) getCountSummarybyTargetId -> {
            if (getCountSummarybyTargetId.isValidResponse()) {
                countSummary = getCountSummarybyTargetId.getCountSummary();
                getBottomContent();
                updateUI();
            }
        });
    }

    private void getBottomContent() {
//        showDialog();
        isBottomContentLoaded = false;
        new GetBottomContentById()
                .setContentId(contentId).setListSlug(slugId == null ? "" : slugId)
                .runAsync((BaseServiceable.OnApiFinishListener<GetBottomContentById>) getContentById -> {
//                    dismissDialog();
                    if (getContentById.isValidResponse()) {
                        contentPoet = getContentById.getContentPoet();
                        previousContent = getContentById.getPreviousContent();
                        nextContent = getContentById.getNextContent();
                        maybeLikes = getContentById.getMaybeLikes();
                        keepReadings = getContentById.getKeepReadings();
                        pluralContentName = getContentById.getPluralContentNames();
                        isBottomContentLoaded = true;
                        getAllComments();
                        updateUI();
                    } else
                        showToast(getContentById.getErrorMessage());
                });
    }

    GetAllCommentsByTargetId getAllCommentsByTargetId;

    @SuppressLint("SetTextI18n")
    private void getAllComments() {
        getAllCommentsByTargetId = new GetAllCommentsByTargetId();
        getAllCommentsByTargetId.setTargetId(contentId).setSortBy(Enums.FORUM_SORT_FIELDS.POPULARITY.getKey())
                .setIsAsc(Enums.COMMENT_SORT_LIST.DESCENDING.getKey()).addPagination().runAsync((BaseServiceable.OnApiFinishListener<GetAllCommentsByTargetId>) getAllCommentsByTargetIds -> {
            if (getAllCommentsByTargetIds.isValidResponse()) {
                comment = getAllCommentsByTargetIds.getComment();
                if (comment.size() == 0) {
                    txtCommentCount.setTextSize(13);
                    txtCommentCount.setText(MyHelper.getString(R.string.add_comment));
                    txtComment.setVisibility(GONE);
                } else {
                    if (getAllCommentsByTargetId.getTotalCommentsCount().length() > 2) {
                        txtCommentCount.setTextSize(15);
                    }
                    txtCommentCount.setTextSize(14);
                    txtComment.setVisibility(View.VISIBLE);
                    txtComment.setText(getAllCommentsByTargetId.getTotalCommentsCount().equalsIgnoreCase("1") ? MyHelper.getString(R.string.comment) : MyHelper.getString(R.string.comments));
                    txtCommentCount.setText(getAllCommentsByTargetId.getTotalCommentsCount());
                }

            }
        });
    }

    FavContentPageModel favContentPageModel;

    private void updateUIOffline() {
//        layDataLoading.setVisibility(View.VISIBLE);
        // RenderHelper.RenderContentBuilder.Builder(getActivity())
        RenderHelper.RenderContentBuilder renderContentBuilder = RenderHelper.RenderContentBuilder.Builder(getActivity())
                .setLayParaContainer(layParaContainer)
                .setParas(favContentPageModel.getRenderContent().getParas())
                .setTextAlignment(favContentPageModel.getAlignment())
                .setLeftRightPadding((int) Utils.pxFromDp(30))
//                .setOnWordClick(onWordClickIntermediate)
                .setZoomLayout(zoomLayout)
                .setLoadingDialog(true)
                .setOnWordLongClick(onWordLongClickIntermediate);
        if (favContentPageModel.isHTML()) {
//            dismissDialog();
            renderContentBuilder.setIsHTML(favContentPageModel.isHTML())
                    .setHtmlContent(favContentPageModel.getJsonOrHtmlContent());
        }
        renderContentBuilder.Build();
        if (TextUtils.isEmpty(favContentPageModel.getInterestingFactEn()) && TextUtils.isEmpty(favContentPageModel.getInterestingFactHin())
                && TextUtils.isEmpty(favContentPageModel.getInterestingFactUrdu())) {
            layInterestingFact.setVisibility(GONE);
        } else {
            layInterestingFact.setVisibility(View.VISIBLE);
            txtInterestingFactTitle.setText(MyHelper.getString(R.string.interesting_fact));
            switch (MyService.getSelectedLanguage()) {
                case ENGLISH:
                    if (favContentPageModel.isHaveFactEng()) {
                        txtInterestingFact.setTypeface(getEngFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactEn());
                    } else if (favContentPageModel.isHaveFactHin()) {
                        txtInterestingFact.setTypeface(getHinFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactHin());
                    } else if (favContentPageModel.isHaveFactUrdu()) {
                        txtInterestingFact.setTypeface(getUrduFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactUrdu());
                    }
                    break;
                case HINDI:
                    if (favContentPageModel.isHaveFactHin()) {
                        txtInterestingFact.setTypeface(getHinFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactHin());
                    } else if (favContentPageModel.isHaveFactEng()) {
                        txtInterestingFact.setTypeface(getEngFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactEn());
                    } else if (favContentPageModel.isHaveFactUrdu()) {
                        txtInterestingFact.setTypeface(getUrduFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactUrdu());
                    }
                    break;
                case URDU:
                    if (favContentPageModel.isHaveFactUrdu()) {
                        txtInterestingFact.setTypeface(getUrduFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactUrdu());
                    } else if (favContentPageModel.isHaveFactHin()) {
                        txtInterestingFact.setTypeface(getHinFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactHin());
                    } else if (favContentPageModel.isHaveFactEng()) {
                        txtInterestingFact.setTypeface(getEngFont(getActivity()));
                        txtInterestingFact.setText(favContentPageModel.getInterestingFactEn());
                    }
                    break;

            }

        }

        layContentTags.setVisibility(GONE);
        boolean isEnglishAvailable = favContentPageModel.getHaveEn().contentEquals("true");
        boolean isHindiAvailable = favContentPageModel.getHaveHi().contentEquals("true");
        boolean isUrduAvailable = favContentPageModel.getHaveUr().contentEquals("true");
        checkAndSetLanguageChangeValues(isEnglishAvailable, isHindiAvailable, isUrduAvailable);

        txtFooterViewPoetProfile.setText(MyHelper.getString(R.string.view_profile));
        txtPrevTitle.setText(MyHelper.getString(R.string.previous));
        txtNextTitle.setText(MyHelper.getString(R.string.next));
        txtHeaderPoetName.setText(favContentPageModel.getPoetName());
        txtFooterAuthorName.setText(favContentPageModel.getPoetName());
        txtFooterPoetTenure.setText(favContentPageModel.getPoetName());
        txtHeaderContentTitle.setText(favContentPageModel.getTitle());

        setHeaderTitle(favContentPageModel.getTitle());
        imgHEaderEditorChoice.setVisibility(favContentPageModel.isEditorChoice() ? View.VISIBLE : GONE);
        imgHeaderPopularChoice.setVisibility(favContentPageModel.isPopularChoice() ? View.VISIBLE : GONE);
        zoomLayout.post(() -> {
            zoomLayout.getChildAt(0).setMinimumHeight(zoomLayout.getHeight());
            ViewGroup paraHolder = (ViewGroup) layMainContainer.getParent();
            desiredTopPadding = (int) Math.max(Utils.pxFromDp(88), layTopContainer.getHeight());
//            desiredBottomPadding = (int) Math.max(Utils.pxFromDp(88), layBottomContainer1.getHeight());
            desiredBottomPadding = 0;
            paraHolder.setPadding(paraHolder.getPaddingLeft(), 0, paraHolder.getPaddingRight(), paraHolder.getPaddingBottom());
        });
    }

    private void updateUI() {
        if (!isMainContentLoaded || !isBottomContentLoaded)
            return;
//        dismissDialog();
//        layDataLoading.setVisibility(View.VISIBLE);
        RenderHelper.RenderContentBuilder renderContentBuilder = RenderHelper.RenderContentBuilder.Builder(getActivity())
                .setLayParaContainer(layParaContainer)
                .setParas(contentPageModel.getRender().getParas())
                .setTextAlignment(contentPageModel.getAlignment())
                .setOnWordClick(onWordClickIntermediate)
                .setLeftRightPadding((int) Utils.pxFromDp(30))
                .setZoomLayout(zoomLayout)
                .setShowTranslation(showingTranslation)
                .setOnWordLongClick(onWordLongClickIntermediate)
                .setLoadingDialog(true);
        if (contentPageModel.isHTML()) {
//            dismissDialog();
            renderContentBuilder.setIsHTML(contentPageModel.isHTML())
                    .setHtmlContent(contentPageModel.getHtmlOrJsonRenderContent());
        }
        renderContentBuilder.Build();
        boolean isEnglishAvailable = contentPageModel.getHaveEn().contentEquals("true");
        boolean isHindiAvailable = contentPageModel.getHaveHi().contentEquals("true");
        boolean isUrduAvailable = contentPageModel.getHaveUr().contentEquals("true");
        checkAndSetLanguageChangeValues(isEnglishAvailable, isHindiAvailable, isUrduAvailable);

        if (contentPageModel.isHTML())
            imgCritiqueInfo.setVisibility(GONE);
        txtFooterViewPoetProfile.setText(MyHelper.getString(R.string.view_profile));
//        txtPrevTitle.setText(MyHelper.getString(R.string.previous));
//        txtNextTitle.setText(MyHelper.getString(R.string.next));

        if (!TextUtils.isEmpty(contentPageModel.getFootNote())) {
            layInterestingFact.setVisibility(View.VISIBLE);
            txtInterestingFactTitle.setText(MyHelper.getString(R.string.interesting_fact));
            switch (MyService.getSelectedLanguage()) {
                case ENGLISH:
                    if (contentPageModel.isHaveFactEng())
                        txtInterestingFact.setTypeface(getEngFont(getActivity()));
                    else if (contentPageModel.isHaveFactHin())
                        txtInterestingFact.setTypeface(getHinFont(getActivity()));
                    else if (contentPageModel.isHaveFactUrdu())
                        txtInterestingFact.setTypeface(getUrduFont(getActivity()));
                    break;
                case HINDI:
                    if (contentPageModel.isHaveFactHin())
                        txtInterestingFact.setTypeface(getHinFont(getActivity()));
                    else if (contentPageModel.isHaveFactEng())
                        txtInterestingFact.setTypeface(getEngFont(getActivity()));
                    else if (contentPageModel.isHaveFactUrdu())
                        txtInterestingFact.setTypeface(getUrduFont(getActivity()));
                    break;
                case URDU:
                    if (contentPageModel.isHaveFactUrdu())
                        txtInterestingFact.setTypeface(getUrduFont(getActivity()));
                    else if (contentPageModel.isHaveFactHin())
                        txtInterestingFact.setTypeface(getHinFont(getActivity()));
                    else if (contentPageModel.isHaveFactEng())
                        txtInterestingFact.setTypeface(getEngFont(getActivity()));
                    break;

            }
            txtInterestingFact.setText(contentPageModel.getFootNote());
        } else
            layInterestingFact.setVisibility(GONE);
        txtHeaderPoetName.setText(contentPageModel.getPoet().getName());
        txtFooterAuthorName.setText(contentPageModel.getPoet().getName());
        txtHeaderContentTitle.setText(contentPageModel.getTitle());
        txtContentTagsTitle.setText(MyHelper.getString(R.string.tagged_under));
        setHeaderTitle(contentPageModel.getTitle());
        imgHEaderEditorChoice.setVisibility(contentPageModel.isEditorChoice() ? View.VISIBLE : GONE);
        imgHeaderPopularChoice.setVisibility(contentPageModel.isPopularChoice() ? View.VISIBLE : GONE);
        txtPoetDescription.setVisibility(!TextUtils.isEmpty(contentPoet.getShortDescription()) ? View.VISIBLE : GONE);
        txtPoetDescription.setText(contentPoet.getShortDescription());
        txtFooterPoetTenure.setVisibility(!TextUtils.isEmpty(contentPoet.getPoetTenure().trim()) ? View.VISIBLE : GONE);
        txtFooterPoetTenure.setText(contentPoet.getPoetTenure());
        txtPerformedBy.setText(MyHelper.getString(R.string.performed_by));
        if (TextUtils.isEmpty(contentPoet.getPoetPlace()))
            txtDobPlaceSeparator.setVisibility(GONE);
        txtFooterPoetPlace.setText(contentPoet.getPoetPlace());
        ImageHelper.setImage(authorImg, contentPoet.getImageUrl());
        if (TextUtils.isEmpty(contentPoet.getPoetID()))
            poetProfileSection.setVisibility(GONE);
        else
            poetProfileSection.setVisibility(View.VISIBLE);
        if (previousContent != null) {
            prevLinearLayout.setVisibility(View.VISIBLE);
            txtPrevPoetName.setText(previousContent.getPoetName());
            txtPrevContentTitle.setText(previousContent.getContentTitle());
            txtPrevTitle.setText(previousContent.getPreviousTitle());
        } else
            prevLinearLayout.setVisibility(GONE);
        if (nextContent != null) {
            layNext.setVisibility(View.VISIBLE);
            txtNextPoetName.setText(nextContent.getPoetName());
            txtNextContentTitle.setText(nextContent.getContentTitle());
            txtNextTitle.setText(nextContent.getNextTitle());
        } else {
            layNext.setVisibility(GONE);
            viewDividerLineBelowPrevious.setVisibility(GONE);
            viewDividerNextPrevious.setVisibility(GONE);
        }
        if (previousContent == null)
            viewDividerNextPrevious.setVisibility(GONE);
        else
            viewDividerNextPrevious.setVisibility(View.VISIBLE);
        txtViewAllContent.setText(String.format(MyHelper.getString(R.string.see_all_template), pluralContentName.getPluralName()));

        txtKeepReadingSubtitle.setText(String.format(MyHelper.getString(R.string.keep_reading_sub_title), contentPageModel.getPoet().getName(), pluralContentName.getPluralName()));
        txtDataLoading.setText(MyHelper.getString(R.string.content_loading_please_wait));
        txtKeepReadingTitle.setText(MyHelper.getString(R.string.keep_reading_title));
        txtYouMayLikeTitle.setText(MyHelper.getString(R.string.you_may_like_title));
        txtYouMayLikeSubtitle.setText(MyHelper.getString(R.string.you_may_like_sub_title));
        layKeepReadingContentPlaceholder.removeAllViews();
        if (CollectionUtils.isEmpty(keepReadings))
            layKeepReading.setVisibility(GONE);
        else {
            layKeepReading.setVisibility(View.VISIBLE);
            for (int i = 0; i < keepReadings.size(); i++) {
                KeepReading keepReading = keepReadings.get(i);
                KeepReadingYouMayLikeViewHolder viewHolder = new KeepReadingYouMayLikeViewHolder(getInflatedView(R.layout.cell_keep_reading_you_may_like_item));
                viewHolder.txtPoetName.setVisibility(GONE);
                viewHolder.imgPoetProfile.setVisibility(GONE);
                if (i == (keepReadings.size() - 1))
                    viewHolder.layDivider.setVisibility(GONE);
                else
                    viewHolder.layDivider.setVisibility(View.VISIBLE);
                viewHolder.txtTitle.setText(keepReading.getContentTitle());
                if (CollectionUtils.isEmpty(keepReading.getRenderContent()))
                    viewHolder.txtSher.setVisibility(GONE);
                else {
                    viewHolder.txtSher.setVisibility(GONE);
                    String sher = "";
                    for (Line line : keepReading.getRenderContent().get(0).getLines()) {
                        if (TextUtils.isEmpty(sher))
                            sher = String.format("%s%s", sher, line.getFullText());
                        else
                            sher = String.format("%s%s", sher, String.format("\n%s", line.getFullText()));
                    }
                    if (TextUtils.isEmpty(sher))
                        viewHolder.txtSher.setVisibility(GONE);
                    else {
                        viewHolder.txtSher.setVisibility(GONE);
                        viewHolder.txtSher.setText(sher);
                    }
                }

                viewHolder.txtLikeCount.setText(keepReading.getTotalFavorite());
                viewHolder.convertView.setTag(keepReading.getId());
                viewHolder.convertView.setOnClickListener(onKeepReadingYouMayLikeClick);
                layKeepReadingContentPlaceholder.addView(viewHolder.convertView);
            }
        }
// related content code here
//        layRelatedContentPlaceholder.removeAllViews();
//        for (int i = 0; i < 4; i++){
//            RelatedContentViewHolder viewHolder= new RelatedContentViewHolder(getInflatedView(R.layout.cell_related_content_item));
//            layRelatedContentPlaceholder.addView(viewHolder.convertView);
//        }


        layYouMayLikeContentPlaceholder.removeAllViews();
        if (CollectionUtils.isEmpty(maybeLikes))
            layYouMayLike.setVisibility(GONE);
        else {
            layYouMayLike.setVisibility(View.VISIBLE);
            for (int i = 0; i < maybeLikes.size(); i++) {
                MaybeLike maybeLike = maybeLikes.get(i);
                KeepReadingYouMayLikeViewHolder viewHolder = new KeepReadingYouMayLikeViewHolder(getInflatedView(R.layout.cell_keep_reading_you_may_like_item));
                if (i == (maybeLikes.size() - 1))
                    viewHolder.layDivider.setVisibility(GONE);
                else
                    viewHolder.layDivider.setVisibility(View.VISIBLE);
                viewHolder.txtPoetName.setVisibility(View.VISIBLE);
                viewHolder.imgPoetProfile.setVisibility(View.VISIBLE);
                viewHolder.txtTitle.setText(maybeLike.getContentTitle());
                viewHolder.txtPoetName.setText(maybeLike.getPoetName());
                viewHolder.txtLikeCount.setText(maybeLike.getTotalFavorite());
                ImageHelper.setImage(viewHolder.imgPoetProfile, maybeLike.getImageUrl());
                if (CollectionUtils.isEmpty(maybeLike.getRenderContent()))
                    viewHolder.txtSher.setVisibility(GONE);
                else {
                    viewHolder.txtSher.setVisibility(GONE);
                    String sher = "";
                    for (Line line : maybeLike.getRenderContent().get(0).getLines()) {
                        if (TextUtils.isEmpty(sher))
                            sher = String.format("%s%s", sher, line.getFullText());
                        else
                            sher = String.format("%s%s", sher, String.format("\n%s", line.getFullText()));
                    }
                    if (TextUtils.isEmpty(sher))
                        viewHolder.txtSher.setVisibility(GONE);
                    else {
                        viewHolder.txtSher.setVisibility(GONE);
                        viewHolder.txtSher.setText(sher);
                    }
                }
                viewHolder.convertView.setTag(maybeLike.getId());
                viewHolder.convertView.setOnClickListener(onKeepReadingYouMayLikeClick);
                layYouMayLikeContentPlaceholder.addView(viewHolder.convertView);
            }
        }
        if (!CollectionUtils.isEmpty(contentPageModel.getContentTags())) {
            layContentTags.setVisibility(View.VISIBLE);
            flexTags.removeAllViews();
            for (RenderContentTag renderContentTag : contentPageModel.getContentTags()) {
                View view = getInflatedView(R.layout.cell_render_content_tag);
                TextView txtTagName = view.findViewById(R.id.txtTagName);
                txtTagName.setText(renderContentTag.getTagName());
                view.setTag(renderContentTag);
                view.setOnClickListener(v -> {
                    RenderContentTag tag1 = (RenderContentTag) v.getTag();
                    getActivity().startActivity(SherTagOccasionActivity.getInstance(getActivity(), tag1));
                });
                flexTags.addView(view);
            }
        } else
            layContentTags.setVisibility(GONE);
        zoomLayout.post(() -> {
            zoomLayout.getChildAt(0).setMinimumHeight(zoomLayout.getHeight());
            ViewGroup paraHolder = (ViewGroup) layMainContainer.getParent();
            desiredTopPadding = (int) Math.max(Utils.pxFromDp(88), layTopContainer.getHeight());
            desiredBottomPadding = (int) Math.max(Utils.pxFromDp(88), layBottomContainer1.getHeight());
            paraHolder.setPadding(paraHolder.getPaddingLeft(), 0, paraHolder.getPaddingRight(), paraHolder.getPaddingBottom());
        });
        updateBottomControls();
    }

    private void checkAndSetLanguageChangeValues(boolean isEnglishAvailable, boolean isHindiAvailable, boolean isUrduAvailable) {
        txtContentNotAvailable.setVisibility(GONE);
        layContentUnavailable.setVisibility(GONE);
        imgFlower.setVisibility(GONE);
        if ((MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH && !isEnglishAvailable)
                || (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI && !isHindiAvailable)
                || (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU && !isUrduAvailable)) {
            imgFlower.setVisibility(GONE);
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {
                layContentUnavailable.setVisibility(View.VISIBLE);
                if (isHindiAvailable)
                    btnHindiLanguage.setVisibility(View.VISIBLE);
                if (isUrduAvailable)
                    btnUrduLanguage.setVisibility(View.VISIBLE);
                if (isHindiAvailable && isUrduAvailable)
                    txtAnd2.setVisibility(View.VISIBLE);
            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {
                layContentUnavailable.setVisibility(View.VISIBLE);
                if (isEnglishAvailable)
                    btnEnglishLanguage.setVisibility(View.VISIBLE);
                if (isUrduAvailable)
                    btnUrduLanguage.setVisibility(View.VISIBLE);
                if (isEnglishAvailable && isUrduAvailable)
                    txtAnd1.setVisibility(View.VISIBLE);
            } else {
                layContentUnavailable.setVisibility(View.VISIBLE);
                if (isHindiAvailable)
                    btnHindiLanguage.setVisibility(View.VISIBLE);
                if (isEnglishAvailable)
                    btnEnglishLanguage.setVisibility(View.VISIBLE);
                if (isHindiAvailable && isEnglishAvailable)
                    txtAnd1.setVisibility(View.VISIBLE);
            }
            zoomLayout.getController().getSettings().setMaxZoom(1);
            txtContentNotAvailable.setVisibility(View.VISIBLE);
            txtContentNotAvailable.setText(descriptionNotAvailable);
        }
    }

    private View.OnClickListener onKeepReadingYouMayLikeClick = v -> {
        String id = v.getTag().toString();
        startActivity(RenderContentActivity.getInstance(getActivity(), id));
        finish();
    };

    private void updateBottomControls() {
        footerCrcitcOnText.setText(MyHelper.getString(R.string.critique_mode_on));
        imgCritiqueOff.setText(MyHelper.getString(R.string.turn_off));
        if (contentPageModel != null) {
            if (!CollectionUtils.isEmpty(contentPageModel.getContentVideos()))
                layFooterVideo.setVisibility(View.VISIBLE);
            else
                layFooterVideo.setVisibility(GONE);
        }
        if (contentPageModel != null) {
            if (!CollectionUtils.isEmpty(contentPageModel.getContentAudios()))
                layAudioSection.setVisibility(View.VISIBLE);
            else
                layAudioSection.setVisibility(GONE);
        }
        if (contentPageModel != null) {
            layTranslate.setVisibility(contentPageModel.isHaveTranslation() ? View.VISIBLE : GONE);
        }

        if (showingTranslation)
            imgTranslate.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
        else
            imgTranslate.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);

        mainFooterContent.setVisibility(GONE);
        critiqueFooter.setVisibility(GONE);
        layCritiqueMode.setVisibility(GONE);
        audioFooterBar.setVisibility(GONE);
        shareFooter.setVisibility(GONE);
        imgFooterAudio.setColorFilter(getAppIconColor(), PorterDuff.Mode.SRC_IN);
        switch (currentPageType) {
            case PAGE_TYPE_BASIC:
                imgFooterAudio.setVisibility(View.VISIBLE);
                mainFooterContent.setVisibility(View.VISIBLE);
                break;
            case PAGE_TYPE_CRITIQUE_ENABLED:
                layCritiqueMode.setVisibility(View.VISIBLE);
            case PAGE_TYPE_CRITIQUE_CONTROLS:
                critiqueFooter.setVisibility(View.VISIBLE);
                break;
            case PAGE_TYPE_AUDIO_PLAY:
                imgFooterAudio.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                mainFooterContent.setVisibility(View.VISIBLE);
                audioFooterBar.setVisibility(View.VISIBLE);
                break;
            case PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY:
                critiqueFooter.setVisibility(View.VISIBLE);
                audioFooterBar.setVisibility(View.VISIBLE);
                break;
            case PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY:
                shareFooter.setVisibility(View.VISIBLE);
                audioFooterBar.setVisibility(View.VISIBLE);
                break;
            case PAGE_TYPE_SHARE_SELECTION_ENABLED:
                shareFooter.setVisibility(View.VISIBLE);
                break;

        }
        if (currentPageType == PAGE_TYPE_CRITIQUE_ENABLED) {
            imgCritiqueInfo.setImageResource(R.drawable.ic_critiquefilled);
            imgCritiqueInfo.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            imgCritiqueInfo.setImageResource(R.drawable.ic_critique);
            imgCritiqueInfo.setColorFilter(getActivity().getAppIconColor());
        }
        if (countSummary != null) {
            txtFavoriteFooterCount.setText(countSummary.getFavCount());
            txtFavoriteCritiqueCount.setText(countSummary.getFavCount());
        } else {
            txtFavoriteCritiqueCount.setText("");
            txtFavoriteFooterCount.setText("");
        }

        addFavoriteClick(imgFooterFavorite, contentId, Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(imgFooterFavorite, contentId);

        addFavoriteClick(imgCritiqueFav, contentId, Enums.FAV_TYPES.CONTENT.getKey());
        updateFavoriteIcon(imgCritiqueFav, contentId);
    }

    boolean isRecreateRequired = false;

    private void recreateIfRequired() {
//        if (activityVisible) {
//            isRecreateRequired = false;
//            startActivity(getIntent());
//            finish();
//        } else
//            isRecreateRequired = true;


        mainView.setVisibility(GONE);
        if (activityVisible) {
            startActivity(getIntent());
            finish();
        } else
            recreate();
    }

    @Override
    public void onLanguageChanged() {
        descriptionNotAvailable = MyHelper.getString(R.string.content_not_available);
        recreateIfRequired();

//        if (!new ServiceManager(getActivity()).isNetworkAvailable())
//            updateUIOffline();
//        else
//            getRenderContent();
    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        if (contentPageModel != null)
            updateBottomControls();
    }

    //    ArrayList<RenderContentAudio> contentAudioGlobal = new ArrayList<>();
    private RenderActivityAudioPlayerControls audioPlayerControls;
    private boolean isOpenKeyboard = false;

    @OnClick({R.id.imgCritiqueOff, R.id.imgFooterMoreOption, R.id.btnEnglishLanguage, R.id.btnUrduLanguage, R.id.btnHindiLanguage, R.id.txtHeaderPoetName, R.id.layNext,
            R.id.prevLinearLayout, R.id.txtViewAllContent, R.id.imgFooterAudio, R.id.imgFooterVideo, R.id.imgFooterShare, R.id.imgCritiqueClose, R.id.imgCritiqueInfo,
            R.id.imgCritiqueShare, R.id.txtFooterViewPoetProfile, R.id.audioFooterBar, R.id.imgUpArrow, R.id.layCommentSection, R.id.imgTranslate
            , R.id.imgFooterMultiShare, R.id.txtFooterMultiCopy, R.id.imgFooterShareClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnEnglishLanguage:
                break;
            case R.id.btnUrduLanguage:
                break;
            case R.id.btnHindiLanguage:
                break;
            case R.id.txtHeaderPoetName:
            case R.id.txtFooterViewPoetProfile:
                if (contentPageModel != null && contentPageModel.getPoet() != null)
                    startActivity(PoetDetailActivity.getInstance(getActivity(), contentPageModel.getPoet().getPoetID(), MyHelper.getDummyContentTypeProfile()));
                break;
            case R.id.layNext:
                if (nextContent != null) {
                    startActivity(getInstance(getActivity(), nextContent.getId()));
                    finish();
                }
                break;
            case R.id.prevLinearLayout:
                if (previousContent != null) {
                    startActivity(getInstance(getActivity(), previousContent.getId()));
                    finish();
                }
                break;
            case R.id.txtViewAllContent:
                if (contentPageModel != null)
                    startActivity(PoetDetailActivity.getInstance(getActivity(), contentPageModel.getPoet().getPoetID(), contentPageModel.getContentType()));
                break;
            case R.id.imgFooterAudio:
                onFooterAudioIconClick(0, null);

//                if (contentPageModel != null)
//                    if (!CollectionUtils.isEmpty(contentPageModel.getContentAudios())) {
//                        RenderContentAudioFragment.getInstance(contentPageModel.getContentAudios()).show(getSupportFragmentManager(), "");
//                    }
                break;
            case R.id.imgFooterVideo:
                closeAudioBarUI();
                if (!CollectionUtils.isEmpty(contentPageModel.getContentVideos()))
                    RenderContentVideoFragment.getInstance(contentPageModel.getContentVideos(), contentPageModel.getTitle()).show(getSupportFragmentManager(), "");
                break;
            case R.id.imgCritiqueShare:
            case R.id.imgFooterShare:
                closeAudioBarUI();
                shareRenderContent();
                break;
            case R.id.imgCritiqueOff:
            case R.id.imgCritiqueClose:
                if (currentPageType == PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY) {
                    currentPageType = PAGE_TYPE_AUDIO_PLAY;
                    updateBottomControls();
                } else {
                    closeAudioBarUI();
                    currentPageType = PAGE_TYPE_BASIC;
                    updateBottomControls();
                }
                break;
            case R.id.imgFooterMoreOption:
                if (currentPageType == PAGE_TYPE_AUDIO_PLAY) {
                    currentPageType = PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY;
                    updateBottomControls();
                } else {
                    closeAudioBarUI();
                    currentPageType = PAGE_TYPE_CRITIQUE_CONTROLS;
                    updateBottomControls();
                }
                break;
            case R.id.imgCritiqueInfo:
                if (currentPageType == PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY) {
                    closeAudioBarUI();
                    currentPageType = PAGE_TYPE_BASIC;
                    updateBottomControls();
                    showCritiqueConfirmationDialog();
                } else {
                    if (currentPageType != PAGE_TYPE_CRITIQUE_ENABLED)
                        showCritiqueConfirmationDialog();
                }
                break;
            //  case R.id.audioFooterBar:
            case R.id.imgUpArrow:
                if (currentAudioPlayState != AUDIO_PLAY_STATE_PAUSE && currentAudioPlayState != AUDIO_PLAY_STATE_PLAYING)
                    return;
                if (contentPageModel != null)
                    if (!CollectionUtils.isEmpty(contentPageModel.getContentAudios())) {
                        PlayingAudioItem playingAudioItem = new PlayingAudioItem(null);
                        playingAudioItem.setAudioContent(contentPageModel.getContentAudios().get(currAudioSelectedPosition));
                        RenderContentAudioFragment.getInstance(contentPageModel.getContentAudios(), currAudioSelectedPosition, playingAudioItem, true).show(getSupportFragmentManager(), "");
//                        onFooterAudioIconClick(0, null);
                    }
                break;
            case R.id.layCommentSection:
                isOpenKeyboard = false;
                clearShareSelection();
                checkAndModifyShareSelectionIfNecessary();
                startActivity(AddCommentActivity.getInstance(this, contentId, isOpenKeyboard, contentPageModel.getTitle(), contentPageModel.getContentTyeName()));
                break;
            case R.id.imgTranslate:
                showingTranslation = !showingTranslation;
                if (isOffline())
                    updateUIOffline();
                else
                    updateUI();
                break;
            case R.id.imgFooterMultiShare:
                String shareContentWithPoetName = MyHelper.getSherContentText(shareSelectionParas) +
                        "\n\n" +
                        contentPageModel.getPoet().getName() +
                        "\n" +
                        contentPageModel.getUrl();
                MyHelper.shareTheText(shareContentWithPoetName, getActivity());
                break;
            case R.id.txtFooterMultiCopy:
                MyHelper.copyToClipBoard(MyHelper.getSherContentText(shareSelectionParas), getActivity());
                break;
            case R.id.imgFooterShareClose:
                clearShareSelection();
                checkAndModifyShareSelectionIfNecessary();
                break;
        }
    }


    private void playAudioBottomBar(int audioPlayPosition, PlayingAudioItem playingAudioItem) {
        ImageHelper.setImage(imgPoetAudioImage, contentPageModel.getContentAudios().get(audioPlayPosition).getImageUrl());
        audioTitle.setText(contentPageModel.getContentAudios().get(audioPlayPosition).getAuthorName());
        audioPlayerControls.playAudio(audioPlayPosition);
    }


    private View.OnClickListener onWordLongClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);

/*        public static final int PAGE_TYPE_BASIC = 1;
        public static final int PAGE_TYPE_CRITIQUE_CONTROLS = 2;
        public static final int PAGE_TYPE_CRITIQUE_ENABLED = 3;
        public static final int PAGE_TYPE_AUDIO_PLAY = 4;
        public static final int PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY = 5;
        public static final int PAGE_TYPE_SHARE_SELECTION_ENABLED = 6;
        public static final int PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY = 7;*/
        switch (currentPageType) {
            case PAGE_TYPE_CRITIQUE_CONTROLS_WITH_AUDIO_PLAY:
            case PAGE_TYPE_AUDIO_PLAY:
                currentPageType = PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY;
                toggleShareSelection(para);
                break;
            case PAGE_TYPE_CRITIQUE_ENABLED:
                showToast("Please disable critique mode before proceeding.");
                break;
            default:
                if (showingTranslation)
                    showToast("Please disable translation mode before proceeding.");
                else {
                    if (currentPageType != PAGE_TYPE_SHARE_SELECTION_ENABLED)
                        enableShareSelection();
                    currentPageType = PAGE_TYPE_SHARE_SELECTION_ENABLED;
                    toggleShareSelection(para);
                }
                break;
        }
//        if (para == null)
//            return;
//        MyHelper.shareTheText(MyHelper.getSherContentText(para), getActivity());
    };
    ArrayList<Para> shareSelectionParas = new ArrayList<>();

    private void toggleShareSelection(Para para) {
        if (para.getContainerView() != null) {
            if (shareSelectionParas.contains(para)) {
                shareSelectionParas.remove(para);
                para.getContainerView().setBackgroundResource(R.drawable.render_content_unselected_background);
                para.getContainerView().setSelected(false);
            } else {
                shareSelectionParas.add(para);
                para.getContainerView().setBackgroundResource(R.drawable.render_content_selected_background);
                para.getContainerView().setSelected(true);
            }
        }
        checkAndModifyShareSelectionIfNecessary();
    }

    private void checkAndModifyShareSelectionIfNecessary() {
        if (shareSelectionParas.isEmpty()) {
            clearShareSelection();
            if (currentPageType == PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY)
                currentPageType = PAGE_TYPE_AUDIO_PLAY;
            else
                currentPageType = PAGE_TYPE_BASIC;
        }
        updateBottomControls();
    }

    private void enableShareSelection() {
        if (contentPageModel == null)
            return;
        for (Para currPara : contentPageModel.getRender().getParas()) {
            if (currPara.getContainerView() != null) {
                currPara.getContainerView().setBackgroundResource(R.drawable.render_content_unselected_background);
                currPara.getContainerView().setOnClickListener(onParaClick);
            }
        }
    }

    private void clearShareSelection() {
        if (contentPageModel == null)
            return;
        for (Para currPara : contentPageModel.getRender().getParas()) {
            if (currPara.getContainerView() != null) {
                currPara.getContainerView().setBackgroundColor(Color.TRANSPARENT);
                currPara.getContainerView().setSelected(false);
                currPara.getContainerView().setOnClickListener(null);
            }
        }
        shareSelectionParas.clear();
    }

    private View.OnLongClickListener onWordLongClickIntermediate = v -> {
        if (onWordLongClick != null)
            onWordLongClick.onClick(v);
        return false;
    };
    private View.OnClickListener onWordClick = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
        Line line = (Line) v.getTag(R.id.tag_line);
        int lineNumber = (int) v.getTag(R.id.tag_line_number);
        if (currentPageType == PAGE_TYPE_CRITIQUE_ENABLED) {
            ShowCritiqueSubmitForm(lineNumber, line.getFullText());
            hideCriticOption();
        } else if (currentPageType == PAGE_TYPE_SHARE_SELECTION_ENABLED_WITH_AUDIO_PLAY || currentPageType == PAGE_TYPE_SHARE_SELECTION_ENABLED) {
            RenderContentActivity.this.onParaClick.onClick(v);
        } else {
//                ShowCritiqueSubmitForm(lineNumber, line.getFullText());
//            MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(getSupportFragmentManager(), "");
            new MeaningBottomPopupWindow(getActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
        }
    };
    private View.OnClickListener onParaClick = v -> {
        Para para = (Para) v.getTag(R.id.tag_para);
        toggleShareSelection(para);
    };

    private void hideCriticOption() {

    }


    private View.OnClickListener onWordClickIntermediate = new DoubleClick(new DoubleClickListener() {
        @Override
        public void onSingleClick(final View v) {
            if (!shouldCancelSingleClick && onWordClick != null)
                onWordClick.onClick(v);
        }

        @Override
        public void onDoubleClick(View view) {

        }
    });

    public final void initializeZoomParameters() {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getScreenWidth(), LinearLayout.LayoutParams.MATCH_PARENT);
        final RelativeLayout.LayoutParams layoutParamsRel = new RelativeLayout.LayoutParams(Utils.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        final FrameLayout.LayoutParams layoutParamsFrame = new FrameLayout.LayoutParams(Utils.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        layTopContainer.setPadding((int) Utils.pxFromDp(10), layTopContainer.getPaddingTop(), (int) Utils.pxFromDp(10), layTopContainer.getPaddingBottom());
        layMainContainer.setLayoutParams(layoutParams);
        zoomLayout.getController().getSettings().setMaxZoom(2f);
        zoomLayout.getController().addOnStateChangeListener(new GestureController.OnStateChangeListener() {
            @Override
            public void onStateChanged(State state) {

                // remove vertical spacing from top when zoom
                float gapBetweenParaAndTop = desiredTopPadding * state.getZoom();
                gapBetweenParaAndTop = gapBetweenParaAndTop - desiredTopPadding;
                layTopContainer.animate().translationY(gapBetweenParaAndTop / (2 * state.getZoom()));
                // remove vertical spacing from top when zoom

                // remove vertical spacing from bottom when zoom
                float getBetweenParaAndBottom = desiredBottomPadding * state.getZoom();
                getBetweenParaAndBottom = getBetweenParaAndBottom - desiredBottomPadding;
                layBottomContainer1.animate().translationY(-getBetweenParaAndBottom / (2 * state.getZoom()));
                // remove vertical spacing from bottom when zoom

                //manage top padding so that view can't over scroll to extra zoom area
                float viewPortHeight = zoomLayout.getController().getSettings().getViewportH();
                float viewPortWidth = zoomLayout.getController().getSettings().getViewportW();
                zoomLayout.getController().getSettings().setMovementArea((int) viewPortWidth, (int) (viewPortHeight + getBetweenParaAndBottom));
                layMainContainer.setPadding(0, (int) (-gapBetweenParaAndTop / (state.getZoom())), 0, isOffline() ? (int) (gapBetweenParaAndTop / (state.getZoom())) : 0);
                //manage top padding so that view can't over scroll to extra zoom area

                // prevent top and bottom layout from zooming
                layTopContainer.animate().scaleX(1 / state.getZoom()).setDuration(0);
                layTopContainer.animate().scaleY(1 / state.getZoom()).setDuration(0);
                layBottomContainer1.animate().scaleX(1 / state.getZoom()).setDuration(0);
                layBottomContainer1.animate().scaleY(1 / state.getZoom()).setDuration(0);
                // prevent top and bottom layout from zooming

                // prevent top and bottom layout from moving left-right
                if (state.getX() == 0)
                    layBottomContainer1.animate().translationX(-(((zoomLayout.getWidth() * state.getZoom()) - zoomLayout.getWidth()) / (state.getZoom() * 2))).setDuration(0);
                else
                    layBottomContainer1.animate().translationX(-(((zoomLayout.getWidth() * state.getZoom()) - zoomLayout.getWidth()) / (state.getZoom() * 2)) - (state.getX() / state.getZoom())).setDuration(0);
                if (state.getX() == 0)
                    layTopContainer.animate().translationX(-(((zoomLayout.getWidth() * state.getZoom()) - zoomLayout.getWidth()) / (state.getZoom() * 2))).setDuration(0);
                else
                    layTopContainer.animate().translationX(-(((zoomLayout.getWidth() * state.getZoom()) - zoomLayout.getWidth()) / (state.getZoom() * 2)) - (state.getX() / state.getZoom())).setDuration(0);
                // prevent top and bottom layout from moving left-right


            }

            @Override
            public void onStateReset(State oldState, State newState) {

            }
        });
        zoomLayout.getController().setOnGesturesListener(new GestureController.OnGestureListener() {
            @Override
            public void onDown(@NonNull MotionEvent event) {

            }

            @Override
            public void onUpOrCancel(@NonNull MotionEvent event) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent event) {
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent event) {
                shouldCancelSingleClick = false;
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent event) {
            }

            @Override
            public boolean onDoubleTap(@NonNull MotionEvent event) {
                shouldCancelSingleClick = true;
                return false;
            }
        });
//        setDefaultSettings(layout.getController().getSettings());
        zoomLayout.getController().getSettings().setOverzoomFactor(1f);
        if (layTopContainer.getParent() instanceof LinearLayout)
            layTopContainer.setLayoutParams(layoutParams);
        else if (layTopContainer.getParent() instanceof RelativeLayout)
            layTopContainer.setLayoutParams(layoutParamsRel);
        else
            layTopContainer.setLayoutParams(layoutParamsFrame);
        txtAnd1 = findViewById(R.id.txtAnd1);
        txtAnd2 = findViewById(R.id.txtAnd2);
        btnEnglishLanguage = findViewById(R.id.btnEnglishLanguage);
        btnHindiLanguage = findViewById(R.id.btnHindiLanguage);
        btnUrduLanguage = findViewById(R.id.btnUrduLanguage);
        layContentUnavailable = findViewById(R.id.layContentUnavailable);
        layContentUnavailable.setVisibility(GONE);
        btnEnglishLanguage.setVisibility(GONE);
        btnHindiLanguage.setVisibility(GONE);
        btnUrduLanguage.setVisibility(GONE);
        txtAnd1.setVisibility(GONE);
        txtAnd2.setVisibility(GONE);
        String descriptionNotAvailable = MyHelper.getString(R.string.content_not_available);
        txtAnd1.setText(MyHelper.getString(R.string.and));
        txtAnd2.setText(MyHelper.getString(R.string.and));
        btnEnglishLanguage.setOnClickListener(v -> {
            mainView.setVisibility(GONE);
            MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
        });
        btnHindiLanguage.setOnClickListener(v -> {
            mainView.setVisibility(GONE);
            MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
        });
        btnUrduLanguage.setOnClickListener(v -> {
            mainView.setVisibility(GONE);
            MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
        });
        layContentUnavailable.setVisibility(GONE);
    }

    private void shareRenderContent() {
        if (contentPageModel == null)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        String contentId = contentPageModel.getContentType().getContentId();
        if (MyConstants.SHER_ID.contentEquals(contentId) || MyConstants.DOHA_ID.contentEquals(contentId)) {
            stringBuilder.append(MyHelper.getSherContentText(contentPageModel.getRender().getParas()));
            stringBuilder.append("\n\n");
            stringBuilder.append(contentPageModel.getPoet().getName());
        } else if (MyConstants.NAZM_ID.contentEquals(contentId)) {
            stringBuilder.append(contentPageModel.getTitle());
            stringBuilder.append("\n");
            stringBuilder.append(contentPageModel.getSubTitle());
            stringBuilder.append("\n\n");
            stringBuilder.append(contentPageModel.getPoet().getName());
            stringBuilder.append("\n");
            stringBuilder.append(contentPageModel.getUrl());
        } else {
            stringBuilder.append(contentPageModel.getTitle());
            stringBuilder.append("\n\n");
            stringBuilder.append(contentPageModel.getPoet().getName());
            stringBuilder.append("\n");
            stringBuilder.append(contentPageModel.getUrl());
        }
        MyHelper.shareTheText(stringBuilder.toString(), getActivity());
    }

    Dialog critiqueSubmitForm;

    public void showCritiqueConfirmationDialog() {
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
            updateBottomControls();
            critiqueDialog.dismiss();
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        critiqueDialog.show();
        critiqueDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

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
            citiqueEMAIL.setVisibility(GONE);
            userProfile.setVisibility(GONE);
            //ImageHelper.setImage(userProfile,MyService.getUser().);
            critiqueUserName.setEnabled(false);
            critiqueUserEmail.setEnabled(false);
        } else {
            critiqueUserName.setText("");
            critiqueUserEmail.setText("");
            userProfile.setVisibility(GONE);
            citiqueEMAIL.setVisibility(View.VISIBLE);
        }
        critiqueSubmitText.setOnClickListener(view -> {
            String email, name, message;
            message = getEditTextData(critiqueUserComment);
            if (MyService.isUserLogin()) {
                email = MyService.getEmail();
                name = MyService.getUser().getDisplayName();
            } else {
                email = getEditTextData(critiqueUserEmail);
                name = getEditTextData(critiqueUserName);
                if (TextUtils.isEmpty(name)) {
                    showToast(AppErrorMessage.please_enter_your_name);
                    return;
                } else if (!MyHelper.isValidEmail(email)) {
                    if (TextUtils.isEmpty(email))
                        showToast(AppErrorMessage.please_enter_your_email);
                    else
                        showToast(AppErrorMessage.please_enter_valid_email_address);
                    return;
                }
            }
            if (TextUtils.isEmpty(message))
                showToast(AppErrorMessage.Please_enter_your_feedback);
            else {
                showDialog();
                new PostSubmitCritique()
                        .setTypeOfQuery(Enums.CRITIQUE_TYPE.CRITIQUE)
                        .setMessage(message)
                        .setName(name)
                        .setEmail(email)
                        .setPageUrl(contentPageModel.getUrl())
                        .setContentId(contentPageModel.getId())
                        .setContentTitle(contentPageModel.getTitle())
                        .setSubject(String.format(Locale.getDefault(), "LINE #%d %s", lineNumber, ghazalLine))
                        .runAsync((BaseServiceable.OnApiFinishListener<PostSubmitCritique>) submitCritique -> {
                            dismissDialog();
                            if (submitCritique.isValidResponse()) {
                                critiqueSubmitForm.dismiss();
                                showToast(MyHelper.getString(R.string.thankyou_for_your_feedback));
                                // ShowThankYouForCritique();
                                currentPageType = PAGE_TYPE_BASIC;
                                updateBottomControls();
                            } else
                                showToast(submitCritique.getErrorMessage());
                        });
            }
        });
        critiqueCancelText.setOnClickListener(view -> critiqueSubmitForm.dismiss());
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        critiqueSubmitForm.show();
        critiqueSubmitForm.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void ShowThankYouForCritique() {
        final Dialog critiqueThankYouDialog = new Dialog(this, R.style.Theme_Dialog);
        critiqueThankYouDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        critiqueThankYouDialog.setContentView(R.layout.critique_thanks_dialog_template);
        critiqueThankYouDialog.setCanceledOnTouchOutside(false);
        TextView thankYouText = critiqueThankYouDialog.findViewById(R.id.thanksCritiqueText);
        thankYouText.setOnClickListener(view -> critiqueThankYouDialog.dismiss());
        critiqueThankYouDialog.show();
        critiqueThankYouDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                critiqueThankYouDialog.dismiss();
                t.cancel();
            }
        }, 2000);
    }

    static class RelatedContentViewHolder {
        View convertView;
        @BindView(R.id.txtContentType)
        TextView txtContentType;
        @BindView(R.id.txtBody)
        TextView txtBody;
        @BindView(R.id.txtAuthor)
        TextView txtAuthor;

        RelatedContentViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.convertView = view;
        }
    }


    static class KeepReadingYouMayLikeViewHolder {
        View convertView;
        @BindView(R.id.imgPoetProfile)
        CircleImageView imgPoetProfile;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;
        @BindView(R.id.txtLikeCount)
        TextView txtLikeCount;
        @BindView(R.id.txtSher)
        TextView txtSher;
        @BindView(R.id.txtPoetName)
        TextView txtPoetName;
        @BindView(R.id.layDivider)
        View layDivider;

        KeepReadingYouMayLikeViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.convertView = view;
        }
    }

    class CommentSectionViewHolder {
        View convertView;
        @BindView(R.id.imgImage)
        CircleImageView imgImage;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.txtComment)
        TextView txtComment;
        @BindView(R.id.txtLikeCount)
        TextView txtLikeCount;
        @BindView(R.id.txtDislikeCount)
        TextView txtDislikeCount;
        @BindView(R.id.txtCommentCount)
        TextView txtCommentCount;
        @BindView(R.id.imgMore)
        ImageView imgMore;
        @BindView(R.id.txtShowReplies)
        TextView txtShowReplies;
        @BindView(R.id.layLike)
        LinearLayout layLike;
        @BindView(R.id.layDislike)
        LinearLayout layDislike;
        @BindView(R.id.layComment)
        LinearLayout layComment;
        @BindView(R.id.imgLike)
        ImageView imgLike;
        @BindView(R.id.imgDislike)
        ImageView imgDislike;
        @BindView(R.id.txtFirstCharacterName)
        TextView txtFirstCharacterName;

        CommentSectionViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.convertView = view;
        }

        @OnClick({R.id.layLike, R.id.layDislike})
        public void onViewClicked(View view) {
            Comment currComment = (Comment) view.getTag(R.id.tag_data);
            switch (view.getId()) {
                case R.id.layLike:
                    if (MyService.isUserLogin()) {
                        if (isLikeChecked(currComment)) {
                            imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                            imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.REMOVE_STATUS)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
//                                    imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedLiked.remove(currComment);
                                    selectedLiked.clear();
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        } else {
                            imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                            imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.LIKE)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
//                                    imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedLiked.add(currComment);
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        }
                    } else {
                        startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    }
                    break;
                case R.id.layDislike:
                    if (MyService.isUserLogin()) {
                        if (isDisLikeChecked(currComment)) {
                            imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                            imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.REMOVE_STATUS)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
//                                    imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedDisLiked.remove(currComment);
                                    selectedDisLiked.clear();
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        } else {
                            imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
                            imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                            new GetMarkLikeDislike().setCommentId(currComment.getId()).setMarkStatus(Enums.MARK_STATUS_LIKE_DISLIKE.DISLIKE)
                                    .setLangauge(String.valueOf(MyService.getSelectedLanguageInt())).runAsync((BaseServiceable.OnApiFinishListener<GetMarkLikeDislike>) getMarkLikeDislikes -> {
                                if (getMarkLikeDislikes.isValidResponse()) {
//                                    imgDislike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.dark_blue), PorterDuff.Mode.SRC_IN);
//                                    imgLike.setColorFilter(ContextCompat.getColor(MyApplication.getContext(), R.color.md_grey_1200), PorterDuff.Mode.SRC_IN);
                                    txtLikeCount.setText(getMarkLikeDislikes.getTotalLike());
                                    txtDislikeCount.setText(getMarkLikeDislikes.getTotalDisLike());
                                    selectedDisLiked.add(currComment);
                                } else
                                    showToast(getMarkLikeDislikes.getErrorMessage());

                            });
                        }
                    } else {
                        startActivity(LoginActivity.getInstance(getActivity()));
                        BaseActivity.showToast("Please login");
                    }
                    break;


            }
        }
    }

    boolean activityVisible;

    @Override
    protected void onPause() {
        super.onPause();
        closeAudioBarUI();
        activityVisible = false;
        if (audioPlayerControls != null)
            audioPlayerControls.stopAudioAndCloseWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityVisible = true;
        if (isRecreateRequired)
            recreateIfRequired();
    }

    private void onFooterAudioIconClick(int audioPlayPosition, PlayingAudioItem playingAudioItem) {
        if (currentPageType != PAGE_TYPE_AUDIO_PLAY) {
            audioPlayerControls = new RenderActivityAudioPlayerControls(getActivity(), audioFooterBar, contentPageModel.getContentAudios());
            audioPlayerControls.setOnAudioPlayerStateChanged(this);
            currentPageType = PAGE_TYPE_AUDIO_PLAY;
            updateBottomControls();
            if (contentPageModel != null) {
                playAudioBottomBar(audioPlayPosition, playingAudioItem);
            }
        } else {
            // contentAudioGlobal.clear();
            closeAudioBarUI();
            currentPageType = PAGE_TYPE_BASIC;
            updateBottomControls();
        }
    }

    private void closeAudioBarUI() {
        if (currentPageType == PAGE_TYPE_AUDIO_PLAY)
            currentPageType = PAGE_TYPE_BASIC;
        audioFooterBar.setVisibility(GONE);
        imgFooterAudio.setColorFilter(getAppIconColor(), PorterDuff.Mode.SRC_IN);
        if (audioPlayerControls != null)
            audioPlayerControls.stopAudioAndCloseWindow();
//        if (contentPageModel != null && audioFooterBar != null) {
//            RenderActivityAudioPlayer.createNewInstance(contentPageModel.getContentAudios());
//            audioPlayerControls = new RenderActivityAudioPlayerControls(getActivity(), audioFooterBar, contentPageModel.getContentAudios());
//            audioPlayerControls.setOnAudioPlayerStateChanged(this);
    }
//    }

    @Override
    public void onAudioPause() {
//        closeAudioBarUI();
        currentAudioPlayState = AUDIO_PLAY_STATE_PAUSE;
    }

    @Override
    public void onAudioStart() {
        currentAudioPlayState = AUDIO_PLAY_STATE_PLAYING;
    }

    private boolean isLikeChecked(Comment comment) {
        if (comment == null)
            return false;
        return selectedLiked.contains(comment);
    }

    private boolean isDisLikeChecked(Comment comment) {
        if (comment == null)
            return false;
        return selectedDisLiked.contains(comment);
    }

    @Override
    public void onAudioWindowClose() {
        currentAudioPlayState = AUDIO_PLAY_STATE_NOT_PLAYING;
    }

    @Override
    public void onAudioSelected(RenderContentAudio audioContent) {
        for (RenderContentAudio currAudio : contentPageModel.getContentAudios())
            if (currAudio.getId().contentEquals(audioContent.getId())) {
                currAudioSelectedPosition = contentPageModel.getContentAudios().indexOf(currAudio);
                break;
            }
        ImageHelper.setImage(imgPoetAudioImage, audioContent.getImageUrl());
        audioTitle.setText(audioContent.getAuthorName());
    }

    private static Typeface getEngFont(final BaseActivity activity) {
        return ResourcesCompat.getFont(activity, R.font.lato_regular_eng);
    }

    private static Typeface getHinFont(final BaseActivity activity) {
        return ResourcesCompat.getFont(activity, R.font.noto_devanagari_hin);
    }

    private static Typeface getUrduFont(final BaseActivity activity) {
        return ResourcesCompat.getFont(activity, R.font.noto_nastaliq_regular_urdu);
    }
}
