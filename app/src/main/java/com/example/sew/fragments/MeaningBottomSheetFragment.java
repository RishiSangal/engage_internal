package com.example.sew.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetWordMeaning;
import com.example.sew.helpers.MyService;
import com.example.sew.models.WordMeaning;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sew.common.ICommonValues.WORD;
import static com.example.sew.common.ICommonValues.WORD_MEANING;

public class MeaningBottomSheetFragment extends BottomSheetDialogFragment {
    @BindView(R.id.closeImg)
    ImageView closeImg;
    @BindView(R.id.mainText)
    TextView txtMainWord;
    @BindView(R.id.mainword2)
    TextView txtMainWord2;
    @BindView(R.id.mainword3)
    TextView txtMainWord3;
    @BindView(R.id.txtDetail)
    TextView txtDetail;

    public static MeaningBottomSheetFragment getInstance(String word, String wordMeaning) {
        MeaningBottomSheetFragment fragment = new MeaningBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(WORD, word);
        args.putString(WORD_MEANING, wordMeaning);
        fragment.setArguments(args);
        return fragment;
    }

    private String word = "";
    private String wordMeaningStr = "";
    private WordMeaning wordMeaning;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(true);
        View convertView = inflater.inflate(R.layout.fragment_word_meaning, container, false);
        ButterKnife.bind(this, convertView);
        if (getArguments() != null) {
            word = getArguments().getString(WORD, "");
            wordMeaningStr = getArguments().getString(WORD_MEANING, "");
        }
        return convertView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWordMeaning();
        setLayoutDirection(view);
    }

    private void getWordMeaning() {
        new GetWordMeaning()
                .setWord(wordMeaningStr)
                .runAsync((BaseServiceable.OnApiFinishListener<GetWordMeaning>) getWordMeaning -> {
                    if (getWordMeaning.isValidResponse()) {
                        wordMeaning = getWordMeaning.getWordMeaning();
                        updateUI();
                    } else {
                        Toast.makeText(getActivity(), getWordMeaning.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
    }

    private void updateUI() {
        txtMainWord.setText(word);
        String meaning = "";
        String mainWord2 = "";
        String mainWord3 = "";
        if (!TextUtils.isEmpty(wordMeaning.getMeaning1Eng()))
            meaning = wordMeaning.getMeaning1Eng();
        else if (!TextUtils.isEmpty(wordMeaning.getMeaning1Hin()))
            meaning = wordMeaning.getMeaning1Hin();
        else if (!TextUtils.isEmpty(wordMeaning.getMeaning1Ur()))
            meaning = wordMeaning.getMeaning1Ur();
        txtDetail.setText(meaning);
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                mainWord2 = wordMeaning.getHindiWord();
                mainWord3 = wordMeaning.getUrduWord();
                break;
            case HINDI:
                mainWord2 = wordMeaning.getEnglishWord();
                mainWord3 = wordMeaning.getUrduWord();
                break;
            case URDU:
                mainWord2 = wordMeaning.getEnglishWord();
                mainWord3 = wordMeaning.getHindiWord();
                break;
        }
        txtMainWord2.setText(mainWord2);
        txtMainWord3.setText(mainWord3);
    }

    @OnClick(R.id.closeImg)
    void onCloseClicked() {
        dismiss();
    }

    private void setLayoutDirection(View rootView) {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
            case HINDI:
                rootView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                break;
            case URDU:
                rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                break;
        }
    }
}
