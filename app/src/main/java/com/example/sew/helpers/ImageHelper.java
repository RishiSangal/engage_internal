package com.example.sew.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Raman Kumar on 04-05-2017.
 */

public class ImageHelper {

    public static void setImage(ImageView imageView, String imageUrl) {
        setImage(imageView, imageUrl, Enums.PLACEHOLDER_TYPE.PROFILE);
    }

    private static ArrayList<String> failedImages = new ArrayList<>(100);

    public static void setImage(ImageView imageView, String imageUrl, Enums.PLACEHOLDER_TYPE placeholderType) {
        setImage(imageView, imageUrl, null, placeholderType);
    }

    static Transformation transformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = Utils.getScreenWidth() - (int) Utils.pxFromDp(22);
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };

    public static void setImage(final ImageView imageView, final String imageUrl, final ProgressBar progressBar, final Enums.PLACEHOLDER_TYPE placeholderType) {

        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        int placeHolderResId = R.drawable.icon;
        int errorResId = R.drawable.icon;
        if (placeholderType != null) {
            switch (placeholderType) {
                case PROFILE:
                    placeHolderResId = R.drawable.icon;
                    errorResId = R.drawable.icon;
                    break;
                case PROFILE_LARGE:
                    placeHolderResId = R.drawable.profile_placeholder_large;
                    errorResId = R.drawable.profile_placeholder_large;
                    break;
                case SHAYARI_IMAGE:
                case SHAYARI_COLLECTION:
                    placeHolderResId = R.drawable.collection_placeholder;
                    errorResId = R.drawable.collection_placeholder;
                    break;
                case USER_INFO_BANNER:
                    placeHolderResId = R.drawable.home;
                    errorResId = R.drawable.home;
                    break;
                case PROMOTIONAL_BANNER:
                    placeHolderResId = R.drawable.banner_placeholder;
                    errorResId = R.drawable.banner_placeholder;

            }
        }
        if (TextUtils.isEmpty(imageUrl)) {
            if (imageView != null)
                imageView.setImageResource(placeHolderResId);
            return;
        }
        if (imageView != null) {
            String finalImageUrl = imageUrl;
            if (failedImages.contains(imageUrl))
                finalImageUrl = getJPEGUrl(imageUrl);
            Picasso.get().load(finalImageUrl)
                    .placeholder(placeHolderResId)
                    .error(errorResId)
                    .transform(transformation)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBar != null)
                                progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            if (progressBar != null)
                                progressBar.setVisibility(View.GONE);
                            if (imageUrl.endsWith(".png")) {
                                if (!failedImages.contains(imageUrl))
                                    failedImages.add(imageUrl);
//                                String imageUrl1 = imageUrl.substring(0, imageUrl.lastIndexOf('.')) + ".jpg";
                                setImage(imageView, getJPEGUrl(imageUrl), progressBar, placeholderType);
                            }
                        }
                    });
        }
    }

    private static String getJPEGUrl(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl))
            return imageUrl;
        if (imageUrl.endsWith(".png"))
            return imageUrl.substring(0, imageUrl.lastIndexOf('.')) + ".jpg";
        return imageUrl;
    }

    public static void setShayariImage(final ImageView imageView, final String imageUrl, final Enums.PLACEHOLDER_TYPE placeholderType) {
        final ProgressBar progressBar = null;
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        int placeHolderResId = R.drawable.icon;
        int errorResId = R.drawable.icon;
        if (placeholderType != null) {
            switch (placeholderType) {
                case PROFILE:
                    placeHolderResId = R.drawable.icon;
                    errorResId = R.drawable.icon;
                    break;
                case SHAYARI_IMAGE:
                    placeHolderResId = R.drawable.collection_placeholder;
                    errorResId = R.drawable.collection_placeholder;
            }
        }
        if (TextUtils.isEmpty(imageUrl)) {
            if (imageView != null)
                imageView.setImageResource(placeHolderResId);
            return;
        }
        if (imageView != null) {
            Picasso.get().load(imageUrl)
                    .placeholder(placeHolderResId)
                    .error(errorResId)
                    .transform(transformation)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imageView.setImageBitmap(bitmap);
                            imageView.setTag(R.id.shayari_image_bitmap, bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            imageView.setImageDrawable(errorDrawable);
                            imageView.setTag(R.id.shayari_image_bitmap, null);
                            if (imageUrl.endsWith(".png")) {
                                String imageUrl1 = imageUrl.substring(0, imageUrl.lastIndexOf('.')) + ".jpg";
                                setShayariImage(imageView, imageUrl1, placeholderType);
                            }
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            imageView.setImageDrawable(placeHolderDrawable);
                            imageView.setTag(R.id.shayari_image_bitmap, null);
                        }
                    });
        }
    }

    public static String[] getHeightWidthOfImage(File file) {
        String[] heightWidth = new String[2];
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), o);
        int imageHeight = o.outHeight;
        int imageWidth = o.outWidth;
        heightWidth[0] = String.valueOf(imageWidth);
        heightWidth[1] = String.valueOf(imageHeight);
        return heightWidth;
    }
//
//    public static void resizeImageView(Image image, ImageView imageView) {
//        int imageWidth = ChamberHelper.convertToInt(image.getImage_width());
//        int imageHeight = ChamberHelper.convertToInt(image.getImage_height());
//        if (imageWidth == 0 || imageHeight == 0)
//            return;
//        float scaleFactor = ((float) BaseActivity.getScreenWidth() / imageWidth);
//        int calculatedHeight = (int) (imageHeight * scaleFactor);
////        calculatedHeight = calculatedHeight > BaseActivity.getScreenHeight() ? BaseActivity.getScreenHeight() : calculatedHeight;
//        if (imageView.getParent() instanceof RelativeLayout)
//            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calculatedHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : calculatedHeight));
//        else if (imageView.getParent() instanceof LinearLayout)
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calculatedHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : calculatedHeight));
//        else if (imageView.getParent() instanceof FrameLayout)
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calculatedHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : calculatedHeight));
//    }

//    public static void resizeImageView_HeightCap(Image image, ImageView imageView) {
//        int imageWidth = ChamberHelper.convertToInt(image.getImage_width());
//        int imageHeight = ChamberHelper.convertToInt(image.getImage_height());
//        if (imageWidth == 0 || imageHeight == 0)
//            return;
////        resizeImageView(image, imageView);
//        float scaleFactor = ((float) BaseActivity.getScreenWidth() / imageWidth);
//        int calculatedHeight = (int) (imageHeight * scaleFactor);
//        calculatedHeight = Math.min(calculatedHeight, (int) (BaseActivity.getScreenHeight() * (3f / 4f)));
//        if (imageView.getParent() instanceof RelativeLayout)
//            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calculatedHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : calculatedHeight));
//        else if (imageView.getParent() instanceof LinearLayout)
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calculatedHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : calculatedHeight));
//        else if (imageView.getParent() instanceof FrameLayout)
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, calculatedHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : calculatedHeight));
//    }
//
//    public static void resizeImageView_HeightCap(Image image, ImageView imageView, int requireImageWidth, int requireImageHeight) {
//        int imageWidth = ChamberHelper.convertToInt(image.getImage_width());
//        int imageHeight = ChamberHelper.convertToInt(image.getImage_height());
//        if (imageWidth == 0 || imageHeight == 0)
//            return;
////        resizeImageView(image, imageView);
//        float scaleFactor = ((float) requireImageWidth / imageWidth);
//        int calculatedHeight = (int) (imageHeight * scaleFactor);
//        calculatedHeight = Math.min(calculatedHeight, (int) (BaseActivity.getScreenHeight() * (3f / 4f)));
//        if (imageView.getParent() instanceof RelativeLayout)
//            imageView.setLayoutParams(new RelativeLayout.LayoutParams(requireImageWidth, requireImageHeight));
//        else if (imageView.getParent() instanceof LinearLayout)
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(requireImageWidth, requireImageHeight));
//        else if (imageView.getParent() instanceof FrameLayout)
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(requireImageWidth, requireImageHeight));
//    }

    public static float dpFromPx(final float px) {
        return px / MyApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * MyApplication.getContext().getResources().getDisplayMetrics().density;
    }
}
