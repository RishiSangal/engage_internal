package com.example.sew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.fragments.MeaningBottomSheetFragment;
import com.example.sew.helpers.MyHelper;
import com.example.sew.views.SquareTextView;
import com.example.sew.views.TitleTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoetryActivity extends BaseHomeActivity {

    @BindView(R.id.txtPoets)
    SquareTextView txtPoets;
    @BindView(R.id.txtSher)
    SquareTextView txtSher;
    @BindView(R.id.txtGhazal)
    SquareTextView txtGhazal;
    @BindView(R.id.txtNazm)
    SquareTextView txtNazm;
    @BindView(R.id.txtPoetry)
    TitleTextView txtPoetry;
    @BindView(R.id.container)
    LinearLayout container;

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, PoetryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poetry);
        ButterKnife.bind(this);
        setSelectableItemForeground(txtPoets, txtSher, txtGhazal,txtNazm);
        initBottomNavigation(Enums.BOTTOM_TYPE.HOME_3);
        setLanguageSpecificTexts();

    }

    @OnClick({R.id.txtPoets, R.id.txtSher, R.id.txtGhazal, R.id.txtNazm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtPoets:
                startActivity(PoetsActivity.getInstance(getActivity()));
                break;
            case R.id.txtSher:
                startActivity(ShayariActivity.getInstance(getActivity()));
//                MeaningBottomSheetFragment.getInstance().show(getSupportFragmentManager(), String.valueOf(System.currentTimeMillis()));
                break;
            case R.id.txtGhazal:
                startActivity(GhazalsActivity.getInstance(getActivity()));
                break;
            case R.id.txtNazm:
                startActivity(NazmActivity.getInstance(getActivity()));
                break;
        }
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        setLanguageSpecificTexts();
    }

    private void setLanguageSpecificTexts() {
        txtPoets.setText(MyHelper.getString(R.string.poets));
        txtPoetry.setText(MyHelper.getString(R.string.poetry));
        txtGhazal.setText(MyHelper.getString(R.string.ghazal));
        txtNazm.setText(MyHelper.getString(R.string.nazm));
        txtSher.setText(MyHelper.getString(R.string.sher));
    }
}
