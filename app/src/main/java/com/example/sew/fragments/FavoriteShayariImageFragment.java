package com.example.sew.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sew.R;
import com.example.sew.activities.ShayariImageDetailActivity;
import com.example.sew.adapters.FavoriteShayariImageAdapter;
import com.example.sew.apis.GetShayariImageWithSearch;
import com.example.sew.apis.PostRemoveAllFavoriteListByType;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.ShayariImage;
import com.google.android.gms.common.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteShayariImageFragment extends BaseFragment {

    @BindView(R.id.rvFavImageShayari)
    RecyclerView rvFavImageShayari;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.txtRemoveAll)
    TextView txtRemoveAll;
    @BindView(R.id.txtRemoveSelected)
    TextView txtRemoveSelected;

    @OnClick(R.id.txtRemoveSelected)
    void onRemoveSelectedClicked() {
        if (currentSelectionType != SELECTION_TYPE_MULTIPLE)
            return;
        new AlertDialog.Builder(GetActivity())
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage(MyHelper.getString(R.string.remove_shayari_image_warning))
                .setPositiveButton(MyHelper.getString(R.string.yes), (dialog, which) -> {
                    //showDialog();
                    new PostRemoveAllFavoriteListByType()
                            .setShayariImages(selectedShayariImages)
                            .runAsync(null);
                    dialog.dismiss();
                })
                .setNegativeButton(MyHelper.getString(R.string.no),(dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();

    }
    @OnClick(R.id.txtRemoveAll)
    void onRemoveAllClicked() {
        if (currentSelectionType != SELECTION_TYPE_SINGLE)
            return;
        new AlertDialog.Builder(GetActivity())
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage(MyHelper.getString(R.string.remove_shayari_image_warning))
                .setPositiveButton("Yes", (dialog, which) -> {
                    //showDialog();
                    new PostRemoveAllFavoriteListByType()
                            .setShayariImages(shayariImages)
                            .runAsync(null);
                    dialog.dismiss();
                })
                .setNegativeButton("No",(dialog, which) -> {
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
            if (currentSelectionType == SELECTION_TYPE_SINGLE) {
                selectedShayariImages.clear();
                currentSelectionType = SELECTION_TYPE_MULTIPLE;
                ShayariImage shayariImage = (ShayariImage) v.getTag(R.id.tag_data);
                selectedShayariImages.add(shayariImage);
                updateUI();
                GetActivity().getWindow().getDecorView().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
            return true;
        }
    };

    public static FavoriteShayariImageFragment getInstance() {
        FavoriteShayariImageFragment poetsFragment = new FavoriteShayariImageFragment();
        Bundle bundle = new Bundle();
        poetsFragment.setArguments(bundle);
        return poetsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_shayari_image, container, false);
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
        getFavoritedImageShayari();
    }

    private GetShayariImageWithSearch getShayariImageWithSearch;

    private void getFavoritedImageShayari() {
        shayariImages.clear();
        shayariImages.addAll(MyService.getAllFavoriteShayariImage());
        updateUI();
    }

    private void updateUI() {
        txtRemoveAll.setText(MyHelper.getString(R.string.remove_all));
        txtRemoveSelected.setText(MyHelper.getString(R.string.remove_selected));
        if (CollectionUtils.isEmpty(selectedShayariImages))
            currentSelectionType = SELECTION_TYPE_SINGLE;
        if (shayariImageAdapter == null) {
            sortShayariImagesList(shayariImages);
            shayariImageAdapter = new FavoriteShayariImageAdapter(GetActivity(), shayariImages, selectedShayariImages);
            shayariImageAdapter.setOnItemClickListener(onItemClick);
            shayariImageAdapter.setOnItemLongClickListener(onItemLongClick);
            rvFavImageShayari.setAdapter(shayariImageAdapter);
        } else {
            shayariImageAdapter.notifyDataSetChanged();
        }
        if (currentSelectionType == SELECTION_TYPE_MULTIPLE) {
            txtRemoveSelected.setTextColor(MyHelper.getPrimaryTextColor(getActivity()));
            txtRemoveSelected.setBackgroundResource(R.drawable.touch_effect);
            txtRemoveAll.setTextColor(MyHelper.getAppDividerColor(getActivity()));
            txtRemoveAll.setBackgroundColor(Color.TRANSPARENT);
        } else {
//            txtRemoveSelected.setTextColor(Color.argb(77, 0, 0, 0));
            txtRemoveSelected.setTextColor(MyHelper.getAppDividerColor(getActivity()));
            txtRemoveSelected.setBackgroundColor(Color.TRANSPARENT);
            txtRemoveAll.setTextColor(MyHelper.getPrimaryTextColor(getActivity()));
            txtRemoveAll.setBackgroundResource(R.drawable.touch_effect);
        }
        updateLanguageSpecificUi();
    }

    private void sortShayariImagesList(ArrayList<ShayariImage> favContentPageModels) {
        try {
            Collections.sort(favContentPageModels, new ShayariComparator());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public class ShayariComparator implements Comparator<ShayariImage> {
        @Override
        public int compare(ShayariImage shayariImage, ShayariImage t1) {
            try {
                SimpleDateFormat sdf_billdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                long f1 = sdf_billdate.parse(t1.getDateCreated()).getTime();
                long f2 = sdf_billdate.parse(shayariImage.getDateCreated()).getTime();
                if (f1 > f2) return 1;
                else if (f1 < f2) return -1;
                return 0;
            } catch (Exception e) {
                // TODO: handle exception
                return 0;
            }
        }
    }

    private void updateLanguageSpecificUi() {
        txtNoData.setText(MyHelper.getString(R.string.no_records_found));
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }
}
