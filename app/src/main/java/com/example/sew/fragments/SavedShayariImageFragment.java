package com.example.sew.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.ShayariImageDetailActivity;
import com.example.sew.adapters.FavoriteShayariImageAdapter;
import com.example.sew.apis.GetShayariImageWithSearch;
import com.example.sew.apis.PostRemoveAllFavoriteListByType;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariImage;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavedShayariImageFragment extends BaseFragment {

    @BindView(R.id.rvFavImageShayari)
    RecyclerView rvFavImageShayari;
    @BindView(R.id.txtRemoveAll)
    TextView txtRemoveAll;
    @BindView(R.id.txtRemoveSelected)
    TextView txtRemoveSelected;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtNoSavedImage)
    TextView txtNoSavedImage;

    @OnClick(R.id.txtRemoveSelected)
    void onRemoveSelectedClicked() {
        if (currentSelectionType != SELECTION_TYPE_MULTIPLE)
            return;
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage(MyHelper.getString(R.string.remove_shayari_image_warning))
                .setPositiveButton(MyHelper.getString(R.string.yes), (dialog, which) -> {
                    //showDialog();
                    new PostRemoveAllFavoriteListByType()
                            .setShayariImages(selectedShayariImages)
                            .runAsync(null);
                    dialog.dismiss();
                })
                .setNegativeButton(MyHelper.getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();

    }

    @OnClick(R.id.txtRemoveAll)
    void onRemoveAllClicked() {
        if (currentSelectionType != SELECTION_TYPE_SINGLE)
            return;
        new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage(MyHelper.getString(R.string.remove_shayari_image_warning))
                .setPositiveButton("Yes", (dialog, which) -> {
                    //showDialog();
                    new PostRemoveAllFavoriteListByType()
                            .setShayariImages(shayariImages)
                            .runAsync(null);
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();

    }

    private static final int SELECTION_TYPE_SINGLE = 0,
            SELECTION_TYPE_MULTIPLE = 1;

    @IntDef({SELECTION_TYPE_SINGLE, SELECTION_TYPE_MULTIPLE})
    private @interface SelectionType {
    }

    @SelectionType
    private int currentSelectionType = SELECTION_TYPE_SINGLE;
    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShayariImage shayariImage = (ShayariImage) v.getTag(R.id.tag_data);
            if (currentSelectionType == SELECTION_TYPE_SINGLE)
                startActivity(ShayariImageDetailActivity.getInstance(getActivity(), shayariImages, shayariImage));
            else {
                if (selectedShayariImages.contains(shayariImage))
                    selectedShayariImages.remove(shayariImage);
                else
                    selectedShayariImages.add(shayariImage);
                updateUI();
            }
        }
    };
    private View.OnLongClickListener onItemLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
//            if (currentSelectionType == SELECTION_TYPE_SINGLE) {
//                selectedShayariImages.clear();
//                currentSelectionType = SELECTION_TYPE_MULTIPLE;
//                ShayariImage shayariImage = (ShayariImage) v.getTag(R.id.tag_data);
//                selectedShayariImages.add(shayariImage);
//                updateUI();
//                GetActivity().getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
//            }
            return true;
        }
    };

    public static SavedShayariImageFragment getInstance() {
        SavedShayariImageFragment poetsFragment = new SavedShayariImageFragment();
        Bundle bundle = new Bundle();
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_shayari_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private FavoriteShayariImageAdapter shayariImageAdapter;
    private ArrayList<ShayariImage> shayariImages = new ArrayList<>();
    private ArrayList<ShayariImage> selectedShayariImages = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvFavImageShayari.setLayoutManager(gridLayoutManager);
        registerBroadcastListener(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getSavedImageShayari();
            }
        }, BROADCAST_SAVED_IMAGE_SHAYARI_UPDATED);
        getSavedImageShayari();
    }

    private GetShayariImageWithSearch getShayariImageWithSearch;

    private void getSavedImageShayari() {
        shayariImages.clear();
        shayariImages.addAll(MyService.getAllSavedImageShayari());
        updateUI();
    }

    private void updateUI() {
        txtRemoveAll.setText(MyHelper.getString(R.string.remove_all));
        txtRemoveSelected.setText(MyHelper.getString(R.string.remove_selected));
        if (CollectionUtils.isEmpty(selectedShayariImages))
            currentSelectionType = SELECTION_TYPE_SINGLE;
        if (shayariImageAdapter == null) {
            shayariImageAdapter = new FavoriteShayariImageAdapter(GetActivity(), shayariImages, selectedShayariImages);
            shayariImageAdapter.setOnItemClickListener(onItemClick);
            shayariImageAdapter.setOnItemLongClickListener(onItemLongClick);
            rvFavImageShayari.setAdapter(shayariImageAdapter);
        } else {
            shayariImageAdapter.notifyDataSetChanged();
        }
        if (currentSelectionType == SELECTION_TYPE_MULTIPLE) {
            txtRemoveSelected.setTextColor(Color.BLACK);
            txtRemoveSelected.setBackgroundResource(R.drawable.touch_effect);
            txtRemoveAll.setTextColor(Color.argb(77, 0, 0, 0));
            txtRemoveAll.setBackgroundColor(Color.TRANSPARENT);
        } else {
            txtRemoveSelected.setTextColor(Color.argb(77, 0, 0, 0));
            txtRemoveSelected.setBackgroundColor(Color.TRANSPARENT);
            txtRemoveAll.setTextColor(Color.BLACK);
            txtRemoveAll.setBackgroundResource(R.drawable.touch_effect);
        }
        updateLanguageSpecificUi();
        if (CollectionUtils.isEmpty(shayariImages))
            txtNoSavedImage.setVisibility(View.VISIBLE);
        else
            txtNoSavedImage.setVisibility(View.GONE);
    }

    private void updateLanguageSpecificUi() {
        txtNoSavedImage.setText(MyHelper.getString(R.string.saved_image_will_be_show_here));
        txtTitle.setText(MyHelper.getString(R.string.saved_images).toUpperCase());
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }
}
