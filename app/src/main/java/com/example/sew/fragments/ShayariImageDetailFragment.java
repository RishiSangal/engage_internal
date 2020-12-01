package com.example.sew.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.binaryfork.spanny.Spanny;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.RenderContentActivity;
import com.example.sew.activities.SherTagOccasionActivity;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetShayariImageDetail;
import com.example.sew.common.Enums;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.TouchImageView;
import com.example.sew.common.Utils;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.helpers.RenderHelper;
import com.example.sew.models.Para;
import com.example.sew.models.ShayariImage;
import com.example.sew.models.ShayariImageDetail;
import com.example.sew.models.SherTag;
import com.example.sew.models.WordContainer;
import com.example.sew.views.TitleTextViewType2;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.VISIBLE;

public class ShayariImageDetailFragment extends BaseFragment {

    @BindView(R.id.imgShayariImage)
    TouchImageView imgShayariImage;
    @BindView(R.id.imgShare)
    ImageView imgShare;
    @BindView(R.id.imgSave)
    ImageView imgSave;
    @BindView(R.id.layPoetSherContainer)
    LinearLayout layPoetSherContainer;
    @BindView(R.id.txtPoetName)
    TitleTextViewType2 txtPoetName;
    @BindView(R.id.sher_ghazalIcon)
    ImageView sherGhazalIcon;
    @BindView(R.id.txtSeeGhazalTitle)
    TitleTextViewType2 txtSeeGhazalTitle;
    @BindView(R.id.laySeeGhazal)
    LinearLayout laySeeGhazal;
    @BindView(R.id.txtTagsTitle)
    TitleTextViewType2 txtTagsTitle;
    @BindView(R.id.layTags)
    LinearLayout layTags;
    @BindView(R.id.layBottomContainer)
    LinearLayout layBottomContainer;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;

    @OnClick(R.id.laySeeGhazal)
    void onSeeGhazalClick() {
        startActivity(RenderContentActivity.getInstance(getActivity(), shayariImage.getCoupletId()));
    }

    private View.OnClickListener onWordClickListener = v -> {
        WordContainer wordContainer = (WordContainer) v.getTag(R.id.tag_word);
//        MeaningBottomSheetFragment.getInstance(wordContainer.getWord(), wordContainer.getMeaning()).show(GetActivity().getSupportFragmentManager(), "MEANING");
        new MeaningBottomPopupWindow(GetActivity(), wordContainer.getWord(), wordContainer.getMeaning()).show();
    };

    public static ShayariImageDetailFragment getInstance(ShayariImage shayariImage) {
        ShayariImageDetailFragment poetsFragment = new ShayariImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHAYARI_IMAGE_OBJ, shayariImage.getJsonObject().toString());
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shayari_image_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private ShayariImage shayariImage;
    private ShayariImageDetail shayariImageDetail;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(SHAYARI_IMAGE_OBJ)) {
            try {
                shayariImage = new ShayariImage(new JSONObject(getArguments().getString(SHAYARI_IMAGE_OBJ, "{}")));
            } catch (JSONException e) {
                e.printStackTrace();
                GetActivity().finish();
            }
        } else
            GetActivity().finish();
        getShayariImageDetail();
        updateUI();

    }

    @Override
    public void onFavoriteUpdated() {
        super.onFavoriteUpdated();
        updateFavoriteIcon(imgFavorite, shayariImage.getId());
    }

    private void getShayariImageDetail() {
        new GetShayariImageDetail()
                .setShayariImgId(shayariImage.getId()).setTargetIdSlug(shayariImage.getSlug())
                .runAsync((BaseServiceable.OnApiFinishListener<GetShayariImageDetail>) getShayariImageDetail -> {
                    if (getShayariImageDetail.isValidResponse()) {
                        if (isDestroyed)
                            return;
                        shayariImageDetail = getShayariImageDetail.getShayariImageDetail();
                        updateUI();
                    } else
                        showToast(getShayariImageDetail.getErrorMessage());
                });
    }

    private void updateUI() {
        updateLanguageSpecificUi();
        addFavoriteClick(imgFavorite, shayariImage.getId(), Enums.FAV_TYPES.IMAGE_SHAYRI.getKey());
        updateFavoriteIcon(imgFavorite, shayariImage.getId());
        ImageHelper.setShayariImage(imgShayariImage, shayariImage.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
        if (shayariImageDetail != null) {
            layBottomContainer.setVisibility(VISIBLE);
            txtPoetName.setText(shayariImageDetail.getPoetName());
            txtSeeGhazalTitle.setText(MyHelper.getString(R.string.see_ghazal));
            if (!TextUtils.isEmpty(shayariImage.getCoupletId())) {
                laySeeGhazal.setVisibility(VISIBLE);
            } else
                laySeeGhazal.setVisibility(View.GONE);
            layTags.setVisibility(VISIBLE);
            txtTagsTitle.setMovementMethod(LinkMovementMethod.getInstance());
            switch (shayariImageDetail.getSherTags().size()) {
                case 0:
                    layTags.setVisibility(View.GONE);
                    break;
                case 1: {
                    SherTag sherTag_1 = shayariImageDetail.getSherTags().get(0);
                    txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherTag_1), boldSpan()));
                }
                break;
                case 2: {
                    SherTag sherTag_1 = shayariImageDetail.getSherTags().get(0);
                    SherTag sherTag_2 = shayariImageDetail.getSherTags().get(1);
                    txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherTag_1), boldSpan())
                            .append(", ")
                            .append(new Spanny(sherTag_2.getTagName(), new CustomClickableSpan(sherTag_2), boldSpan())));
                }
                break;
                default:
                    SherTag sherTag_1 = shayariImageDetail.getSherTags().get(0);
                    txtTagsTitle.setText(new Spanny(sherTag_1.getTagName(), new CustomClickableSpan(sherTag_1), boldSpan())
                            .append(" and ")
                            .append(new Spanny(String.format(Locale.getDefault(), "%d More", (shayariImageDetail.getSherTags().size() - 1)), new CustomClickableSpan(null), boldSpan()))
                    );
                    break;
            }

            RenderHelper.RenderContentBuilder.Builder(GetActivity())
                    .setLayParaContainer(layPoetSherContainer)
                    .setOnWordClick(onWordClickListener)
                    .setTextAlignment(Enums.TEXT_ALIGNMENT.LEFT)
                    .setParas(shayariImageDetail.getParas())
                    .setLeftRightPadding((int) Utils.pxFromDp(32))
                    .setOnWordLongClick(onWordLongClick)
                    .setTextColor(Color.WHITE)
                    .Build();
        } else {
            layBottomContainer.setVisibility(View.GONE);
        }
        updateSaveButton();
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

    private void updateSaveButton() {
        if (MyService.isImageShayariSaved(shayariImage.getId()))
            imgSave.setImageResource(R.drawable.ic_delete);
        else
            imgSave.setImageResource(R.drawable.ic_save);
    }

    private void updateLanguageSpecificUi() {
    }

    public class CustomClickableSpan extends ClickableSpan {
        SherTag sherTag;

        CustomClickableSpan(SherTag sherTag) {
            this.sherTag = sherTag;
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (sherTag == null) {
                Context wrapper = new ContextThemeWrapper(GetActivity(), R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, txtTagsTitle);
//                PopupMenu popup = new PopupMenu(GetActivity(), txtTagsTitle);
                for (int i = 1; i < shayariImageDetail.getSherTags().size(); i++) {
                    SherTag sherTag = shayariImageDetail.getSherTags().get(i);
                    popup.getMenu().add(R.id.menuGroup, R.id.group_detail, i, sherTag.getTagName());
                }
                popup.setOnMenuItemClickListener(item -> {
                    showTagSpecificSherCollection(shayariImageDetail.getSherTags().get(item.getOrder()));
                    return true;
                });
                popup.show();
            } else
                showTagSpecificSherCollection(sherTag);
        }

        void showTagSpecificSherCollection(SherTag sherTag) {
            startActivity(SherTagOccasionActivity.getInstance(GetActivity(), sherTag));
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.linkColor = getClickableTextColor();
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private int getClickableTextColor() {
        return ContextCompat.getColor(GetActivity(), R.color.dark_blue);
    }

    private StyleSpan boldSpan() {
        return new StyleSpan(android.graphics.Typeface.BOLD);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        shayariImageDetail = null;
        getShayariImageDetail();
        updateUI();
    }

    @OnClick({R.id.imgShare, R.id.imgSave, R.id.imgFavorite, R.id.imgShayariImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgShare:
                BitmapDrawable target = (BitmapDrawable) imgShayariImage.getDrawable();
                final Bitmap bitmap = target.getBitmap();
                shareImge(bitmap);
                break;
            case R.id.imgSave:
                if (shayariImage == null)
                    return;
                if (MyService.isImageShayariSaved(shayariImage.getId())) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Remove saved image")
                            .setMessage("Are you sure you want to remove saved image?")
                            .setPositiveButton("Cancel", null)
                            .setNegativeButton("Remove", (dialog, which) -> {
                                MyService.removeImageShayari(shayariImage.getId());
                                BaseActivity.sendBroadCast(BROADCAST_SAVED_IMAGE_SHAYARI_UPDATED);
                                updateSaveButton();
                            }).create().show();
                } else {
                    Dexter.withActivity(getActivity())
                            .withPermissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                if (imgShayariImage != null && imgShayariImage.getTag(R.id.shayari_image_bitmap) != null) {
                                    String imageLocalSavedPath = MyHelper.saveTempBitmap((Bitmap) imgShayariImage.getTag(R.id.shayari_image_bitmap));
                                    if (!TextUtils.isEmpty(imageLocalSavedPath)) {
                                        shayariImage.setLocalPath(imageLocalSavedPath);
                                        MyService.saveImageShayari(shayariImage);
                                        BaseActivity.sendBroadCast(BROADCAST_SAVED_IMAGE_SHAYARI_UPDATED);
                                        updateSaveButton();
                                        GetActivity().showToast("Image saved successfully");
                                    } else
                                        GetActivity().showToast("Error while saving image, please check if memory in your mobile.");
                                } else
                                    GetActivity().showToast("Please wait while the image is loaded.");
                            }else {
                                showToast("Please provide the permissions");
                                getActivity().finish();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
                }
                break;
            case R.id.imgFavorite:
                addFavoriteClick(imgFavorite, shayariImage.getId(), Enums.FAV_TYPES.IMAGE_SHAYRI.getKey());
                updateFavoriteIcon(imgFavorite, shayariImage.getId());
                break;
            case R.id.imgShayariImage:
                if (isHideBottomFirstTab) {
                    layBottomContainer.animate()
                            .alpha(0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    layBottomContainer.setVisibility(View.GONE);
                                }
                            });

                    isHideBottomFirstTab = false;
                } else {
                    layBottomContainer.animate()
                            .alpha(1f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    layBottomContainer.setVisibility(VISIBLE);
                                }
                            });
                    // layBottomContainer.setVisibility(VISIBLE);
                    isHideBottomFirstTab = true;
                }
                break;
        }
    }

    private boolean isHideBottomFirstTab = true;

    public void shareImge(final Bitmap bitmap) {
       // showDialog();
        new Thread(() -> {
            try {
                File file = new File(GetActivity().getExternalCacheDir(), "Rekhta ImageShayri.png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
//                file.setReadable(true, false);
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(GetActivity(), GetActivity().getApplicationContext().getPackageName() + ".provider", file));
                intent.putExtra(Intent.EXTRA_SUBJECT, "www.rekhta.org");
                intent.setType("image/png");
                GetActivity().startActivity(Intent.createChooser(intent, "Share image via"));
              //  dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (imgShayariImage != null && isVisibleToUser)
            imgShayariImage.resetZoom();
    }
}
