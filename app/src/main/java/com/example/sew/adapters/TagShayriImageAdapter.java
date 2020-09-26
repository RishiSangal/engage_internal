package com.example.sew.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.FileProvider;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.models.ShayariImage;
import com.example.sew.views.TitleTextViewType2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagShayriImageAdapter extends BaseMyAdapter {
    private ArrayList<ShayariImage> shayariImages;
    public TagShayriImageAdapter(BaseActivity activity, ArrayList<ShayariImage> shayariImages) {
        super(activity);
        this.shayariImages = shayariImages;
    }

    @Override
    public int getCount() {
        return shayariImages.size();
    }
    @Override
    public ShayariImage getItem(int position) {
        return shayariImages.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (isLayoutDirectionMismatched(convertView))
            convertView = null;
        ShayariImageViewHolder shayariImageViewHolder;
        ShayariImage shayariImage = getItem(position);
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_tag_shayari_image);
            shayariImageViewHolder = new ShayariImageViewHolder(convertView);
        } else
            shayariImageViewHolder = (ShayariImageViewHolder) convertView.getTag();
        convertView.setTag(shayariImageViewHolder);
        convertView.setTag(R.id.tag_data, shayariImage);
        shayariImageViewHolder.imgShare.setTag(R.id.tag_data,shayariImage);
        if (!TextUtils.isEmpty(shayariImage.getImageLocalSavedPath()))
            ImageHelper.setImage(shayariImageViewHolder.imgShayariImage, String.format("file://%s", shayariImage.getImageLocalSavedPath()), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
        else
            ImageHelper.setImage(shayariImageViewHolder.imgShayariImage, shayariImage.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
        shayariImageViewHolder.imgShare.setOnClickListener(view -> {
            ShayariImage currShayriImage= (ShayariImage)view.getTag(R.id.tag_data);
            BitmapDrawable target = (BitmapDrawable) shayariImageViewHolder.imgShayariImage.getDrawable();
            final Bitmap bitmap = target.getBitmap();
            if(bitmap.getByteCount()==4000000)
                getActivity().showToast("Please wait while the image is loaded.");
            else
                shareImage(bitmap);

//            Dexter.withActivity(getActivity())
//                    .withPermissions(
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ).withListener(new MultiplePermissionsListener() {
//                @Override
//                public void onPermissionsChecked(MultiplePermissionsReport report) {
//                    if (report.areAllPermissionsGranted()) {
//                        BitmapDrawable target = (BitmapDrawable) shayariImageViewHolder.imgShayariImage.getDrawable();
//                        final Bitmap bitmap = target.getBitmap();
//                        if(bitmap.getByteCount()==4000000)
//                            getActivity().showToast("Please wait while the image is loaded.");
//                        else
//                            shareImage(bitmap);
//
//                    } else {
//                        getActivity().showToast("Please provide the permissions");
//                    }
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                    token.continuePermissionRequest();
//                }
//            }).check();
        });

//        if(shayariImage.getFavoriteCount()==0){
//            shayariImageViewHolder.txtLikeCount.setVisibility(View.GONE);
//        }else {
//            shayariImageViewHolder.txtLikeCount.setVisibility(View.VISIBLE);
//            shayariImageViewHolder.txtLikeCount.setText(String.valueOf(shayariImage.getFavoriteCount()));
//        }
//
//        if(shayariImage.getShareCount()==0){
//            shayariImageViewHolder.txtShareCount.setVisibility(View.GONE);
//        }else {
//            shayariImageViewHolder.txtShareCount.setVisibility(View.VISIBLE);
//            shayariImageViewHolder.txtShareCount.setText(String.valueOf(shayariImage.getShareCount()));
//        }
        getActivity().addFavoriteClick(shayariImageViewHolder.imgFavorite, shayariImage.getId(), Enums.FAV_TYPES.IMAGE_SHAYRI.getKey());
        getActivity().updateFavoriteIcon(shayariImageViewHolder.imgFavorite, shayariImage.getId());
        return convertView;
    }


    static class ShayariImageViewHolder {
        @BindView(R.id.imgShayariImage)
        ImageView imgShayariImage;
        @BindView(R.id.imgFavorite)
        ImageView imgFavorite;
        @BindView(R.id.txtLikeCount)
        TitleTextViewType2 txtLikeCount;
        @BindView(R.id.imgShare)
        ImageView imgShare;
        @BindView(R.id.txtShareCount)
        TitleTextViewType2 txtShareCount;
        @BindView(R.id.viewHo)
        LinearLayout viewHo;
        @BindView(R.id.poetTemplate)
        RelativeLayout poetTemplate;

        ShayariImageViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
    private void shareImage(final Bitmap bitmap) {
        //  getActivity().showDialog();
        new Thread(() -> {
            try {
                File file = new File(getActivity().getExternalCacheDir(), "Rekhta ImageShayri.png");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
//                file.setReadable(true, false);
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file));
                intent.putExtra(Intent.EXTRA_SUBJECT, "www.rekhta.org");
                intent.setType("image/png");
                getActivity().startActivity(Intent.createChooser(intent, "Share image via"));
                //  getActivity().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}

