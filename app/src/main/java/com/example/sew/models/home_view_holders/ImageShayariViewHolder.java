package com.example.sew.models.home_view_holders;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.ShayariImageDetailActivity;
import com.example.sew.common.Enums;
import com.example.sew.helpers.ImageHelper;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.HomeShayariImage;
import com.example.sew.models.ShayariImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageShayariViewHolder extends BaseHomeViewHolder {
    private HomeShayariImage homeShayariImage;
    @BindView(R.id.txtImageShayariTitle)
    TextView txtImageShayariTitle;
    @BindView(R.id.imgShayariImage)
    ImageView imgShayariImage;
    @BindView(R.id.imgShare)
    ImageView imgShare;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;

    @OnClick(R.id.imgShayariImage)
    void onImageShayariClick() {
        if (homeShayariImage == null)
            return;
        ShayariImage shayariImage = new ShayariImage(homeShayariImage.getJsonObject());
        getActivity().startActivity(ShayariImageDetailActivity.getInstance(getActivity(), shayariImage));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (imgFavorite != null && homeShayariImage != null && getActivity() != null)
                getActivity().updateFavoriteIcon(imgFavorite, homeShayariImage.getId());
        }
    };

    public static ImageShayariViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        ImageShayariViewHolder imageShayariViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_image_shayari, baseActivity);
            imageShayariViewHolder = new ImageShayariViewHolder(convertView, baseActivity);
        } else
            imageShayariViewHolder = (ImageShayariViewHolder) convertView.getTag();
        convertView.setTag(imageShayariViewHolder);
        imageShayariViewHolder.setConvertView(convertView);
        return imageShayariViewHolder;
    }

    private ImageShayariViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
        getActivity().registerBroadcastListener(broadcastReceiver, BROADCAST_FAVORITE_UPDATED);
    }

    public BaseHomeViewHolder loadData(HomeShayariImage shayariImage) {
        this.homeShayariImage = shayariImage;
        txtImageShayariTitle.setText(MyHelper.getString(R.string.image_shayari));
        ImageHelper.setImage(imgShayariImage, shayariImage.getImageUrl(), Enums.PLACEHOLDER_TYPE.SHAYARI_IMAGE);
        getActivity().addFavoriteClick(imgFavorite, shayariImage.getId(), Enums.FAV_TYPES.IMAGE_SHAYRI.getKey());
        getActivity().updateFavoriteIcon(imgFavorite, shayariImage.getId());
        return this;
    }

    @OnClick({R.id.imgShare, R.id.imgFavorite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgShare:
                BitmapDrawable target = (BitmapDrawable) imgShayariImage.getDrawable();
                final Bitmap bitmap = target.getBitmap();
                shareImage(bitmap);
               // checkPermissionAndShare();
                break;
            case R.id.imgFavorite:
                getActivity().showToast("Favorite");
                break;
        }
    }

    private void checkPermissionAndShare() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    BitmapDrawable target = (BitmapDrawable) imgShayariImage.getDrawable();
                    final Bitmap bitmap = target.getBitmap();
                    shareImage(bitmap);
                } else {
                    getActivity().showToast("Please provide the permissions");
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void shareImage(final Bitmap bitmap) {
       // getActivity().showDialog();
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
         //       getActivity().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
