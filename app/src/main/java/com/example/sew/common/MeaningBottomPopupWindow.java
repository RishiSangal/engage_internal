package com.example.sew.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetWordMeaning;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.WordMeaning;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sew.common.ICommonValues.BROADCAST_FAVORITE_UPDATED;

public class MeaningBottomPopupWindow extends PopupWindow {
    @BindView(R.id.mainText)
    TextView txtWordOfTheDayMainText;
    @BindView(R.id.mainword2)
    TextView txtWordOfTheDaySecondaryText1;
    @BindView(R.id.mainword3)
    TextView txtWordOfTheDaySecondaryText2;
    @BindView(R.id.txtDetail)
    TextView txtDetail;
    @BindView(R.id.imgWordFavorite)
    ImageView imgWordFavorite;
    @BindView(R.id.imgWordAudio)
    ImageView imgWordAudio;
    @BindView(R.id.txtNoWord)
    TextView txtNoWord;
    @BindView(R.id.layMainText)
    LinearLayout layMainText;
    @BindView(R.id.txtPoetTenureDivider)
    TextView txtPoetTenureDivider;
    @BindView(R.id.imgDot)
    ImageView imgDot;
    private BaseActivity activity;
    private String word;
    private String wordMeaningStr = "";
    private WordMeaning wordMeaning;
    private View convertView;
    @BindView(R.id.closeImg)
    TextView imgClose;
    @BindView(R.id.relClose)
    RelativeLayout relClose;
    @BindView(R.id.linaerpara2)
    LinearLayout linaerpara2;


    public MeaningBottomPopupWindow(BaseActivity context, String word, String wordMeaning) {
        this.activity = context;
        this.word = word;
        this.wordMeaningStr = wordMeaning;
        View convertView = LayoutInflater.from(context).inflate(R.layout.fragment_word_meaning, null);
        this.convertView = convertView;
        setContentView(convertView);
        setLayoutDirection(convertView);
        ButterKnife.bind(this, convertView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
        getWordMeaning();
        //imgClose.setVisibility(View.GONE);
        context.registerBroadcastListener(broadcastReceiver, BROADCAST_FAVORITE_UPDATED);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (wordMeaning != null)
                updateUI();
        }
    };

    public void show() {
        showAtLocation(convertView, Gravity.BOTTOM, 0, 0);
    }

    public BaseActivity getActivity() {
        return activity;
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

        String mainWord = "",
                secondaryWord1 = "",
                secondaryWord2 = "";
        String meaningWord = "";
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.merriweather_extended_light_italic_eng));
                txtWordOfTheDaySecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                txtWordOfTheDaySecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                if (wordMeaning.getEnglishWord() != null && !TextUtils.isEmpty(wordMeaning.getEnglishWord())) {
                    mainWord = wordMeaning.getEnglishWord();
                } else {
                    mainWord = "";
                }
                secondaryWord1 = wordMeaning.getHindiWord();
                secondaryWord2 = wordMeaning.getUrduWord();
                break;
            case HINDI:
                txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                txtWordOfTheDaySecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.merriweather_extended_light_italic_eng));
                txtWordOfTheDaySecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                if (wordMeaning.getHindiWord() != null && !TextUtils.isEmpty(wordMeaning.getHindiWord())) {
                    mainWord = wordMeaning.getHindiWord();
                } else {
                    mainWord = "";
                }
                secondaryWord1 = wordMeaning.getEnglishWord();
                secondaryWord2 = wordMeaning.getUrduWord();
                break;
            case URDU:
                txtWordOfTheDayMainText.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.noto_nastaliq_regular_urdu));
                txtWordOfTheDaySecondaryText1.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
                txtWordOfTheDaySecondaryText2.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.merriweather_extended_light_italic_eng));
                if (wordMeaning.getUrduWord() != null && !TextUtils.isEmpty(wordMeaning.getUrduWord())) {
                    mainWord = wordMeaning.getUrduWord();
                } else {
                    mainWord = "";
                }
                secondaryWord1 = wordMeaning.getHindiWord();
                secondaryWord2 = wordMeaning.getEnglishWord();
                break;
        }
        String meaning = "";
        int index = 1;
        if (!TextUtils.isEmpty(wordMeaning.getMeaning1Eng())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning1Eng());
            ++index;
        }
        if (!TextUtils.isEmpty(wordMeaning.getMeaning2Eng())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning2Eng());
            ++index;
        }
        if (!TextUtils.isEmpty(wordMeaning.getMeaning3Eng())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning3Eng());
            ++index;
        }

        if (!TextUtils.isEmpty(wordMeaning.getMeaning1Hin())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning1Hin());
            ++index;
        }
        if (!TextUtils.isEmpty(wordMeaning.getMeaning2Hin())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning2Hin());
            ++index;
        }
        if (!TextUtils.isEmpty(wordMeaning.getMeaning3Hin())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning3Hin());
            ++index;
        }

        if (!TextUtils.isEmpty(wordMeaning.getMeaning1Ur())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning1Ur());
            ++index;
        }
        if (!TextUtils.isEmpty(wordMeaning.getMeaning2Ur())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning2Ur());
            ++index;
        }
        if (!TextUtils.isEmpty(wordMeaning.getMeaning3Ur())) {
            meaning += String.format(Locale.getDefault(), "\n%d. %s", index, wordMeaning.getMeaning3Ur());
            ++index;
        }
        txtNoWord.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.laila_regular_hin));
        if (TextUtils.isEmpty(meaning)) {
            layMainText.setVisibility(View.GONE);
            txtNoWord.setVisibility(View.VISIBLE);
            txtNoWord.setText("Meaning not Found");
            imgDot.setVisibility(View.GONE);
        } else {
            txtWordOfTheDayMainText.setText(mainWord);
            txtNoWord.setVisibility(View.GONE);
            layMainText.setVisibility(View.VISIBLE);
            imgDot.setVisibility(View.VISIBLE);
            imgWordFavorite.setVisibility(View.VISIBLE);
            txtWordOfTheDaySecondaryText1.setText(secondaryWord1);
            txtWordOfTheDaySecondaryText2.setText(secondaryWord2);
        }


        if (wordMeaning.isAudioShow()) {
            imgWordAudio.setVisibility(View.VISIBLE);
        } else
            imgWordAudio.setVisibility(View.INVISIBLE);
        txtDetail.setText(meaning);
        getActivity().updateFavoriteIcon(imgWordFavorite, wordMeaning.getId());
        getActivity().addFavoriteClick(imgWordFavorite, wordMeaning.getId(), Enums.FAV_TYPES.WORD.getKey());
    }

    @OnClick(R.id.relClose)
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

    @OnClick(R.id.imgWordFavorite)
    public void onWordFavoriteClicked() {
    }

    @OnClick(R.id.imgWordAudio)
    public void onWordAudioClicked() {
        if (wordMeaning != null)
            MyHelper.playAudio(wordMeaning.getAudioUrl(), getActivity(), imgWordAudio);
    }

    @Override
    public void dismiss() {
        Window window = activity.getWindow();
        if (window == null)
            return;
        View decor = window.getDecorView();
        if (decor != null && decor.getParent() != null) {
            super.dismiss();
        }
        //  super.dismiss();
        try {
            activity.unregisterReceiver(broadcastReceiver);
        } catch (Exception ignored) {
        }
    }
}
