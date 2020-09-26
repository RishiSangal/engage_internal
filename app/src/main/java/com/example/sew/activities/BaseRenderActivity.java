package com.example.sew.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.State;
import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.example.sew.R;
import com.example.sew.common.DoubleClick;
import com.example.sew.common.DoubleClickListener;
import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentPageModel;
import com.example.sew.models.RenderContent;
import com.example.sew.models.WordContainer;

import java.util.ArrayList;
import java.util.Collections;

public abstract class BaseRenderActivity extends BaseActivity {
    private String descriptionNotAvailable;

//    public enum Enums.TEXT_ALIGNMENT {LEFT, CENTER}

    //    TextView txtTitle, txtAuthor;
    private View clickedLayout;
    LinearLayout layMainContainer, layParaContainer, layTopContainer, layBottomContainer1;
    Typeface engtf, hinditf, urdutf;
    //    TextView txtTitle, txtAuthor;
    private GestureFrameLayout layout;
    TextView txtContentNotAvailable;
    View.OnClickListener onWordClick, onWordLongClick;
    View author_name;
    private TextView txtAnd1;
    private TextView txtAnd2;
    private Button btnEnglishLanguage;
    private Button btnHindiLanguage;
    private Button btnUrduLanguage;
    private LinearLayout layContentUnavailable;
    private SharedPreferences DataPrefs;

    public final void setOnWordClickListener(View.OnClickListener onWordClick) {
        this.onWordClick = onWordClick;
    }

    public final void setOnWordLongClickListener(View.OnClickListener onWordLongClick) {
        this.onWordLongClick = onWordLongClick;
    }

    private boolean shouldCancelSingleClick;

    public final void initializeZoomParameters() {
        engtf = Typeface.createFromAsset(getAssets(), "fonts/MerriweatherExtended-LightItalic.ttf");
        hinditf = Typeface.createFromAsset(getAssets(), "fonts/Laila-Regular.ttf");
        urdutf = Typeface.createFromAsset(this.getAssets(), "fonts/NotoNastaliqUrdu-Regular.ttf");
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        final RelativeLayout.LayoutParams layoutParamsRel = new RelativeLayout.LayoutParams(Utils.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        final FrameLayout.LayoutParams layoutParamsFrame = new FrameLayout.LayoutParams(Utils.getScreenWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        layout = findViewById(R.id.frame_layout);
        layMainContainer = findViewById(R.id.layMainContainer);
        layParaContainer = findViewById(R.id.layParaContainer);
        layTopContainer = findViewById(R.id.layTopContainer);
        txtContentNotAvailable = findViewById(R.id.txtContentNotAvailable);
        layTopContainer.setPadding((int) Utils.pxFromDp(10), layTopContainer.getPaddingTop(), (int) Utils.pxFromDp(10), layTopContainer.getPaddingBottom());
        layBottomContainer1 = findViewById(R.id.layBottomContainer1);
        author_name = findViewById(R.id.txtHeaderPoetName);
//        txtAuthor = viewGroup.findViewById(R.id.txtAuthor);
//        txtTitle = viewGroup.findViewById(R.id.txtTitle);
        layMainContainer.setLayoutParams(layoutParams);
        layout.getController().addOnStateChangeListener(new GestureController.OnStateChangeListener() {
            @Override
            public void onStateChanged(State state) {
                Matrix matrix = new Matrix();
                state.get(matrix);
                float bottomPosition = (int) ((float) layout.getChildAt(0).getHeight() * state.getZoom());
                float visibleBottom = layout.getHeight() - state.getY();
                int difference = (int) (bottomPosition - visibleBottom);
                layTopContainer.animate().translationY(state.getY()).setDuration(0);
                layBottomContainer1.animate().scaleX(1 / state.getZoom()).setDuration(0);
                layBottomContainer1.animate().scaleY(1 / state.getZoom()).setDuration(0);
//                layBottomContainer1.animate().translationX((((layout.getWidth() - state.getX()) * state.getZoom()) - layout.getWidth()));
                if (state.getX() == 0)
                    layBottomContainer1.animate().translationX(-(((layout.getWidth() * state.getZoom()) - layout.getWidth()) / (state.getZoom() * 2))).setDuration(0);
                else
                    layBottomContainer1.animate().translationX(-(((layout.getWidth() * state.getZoom()) - layout.getWidth()) / (state.getZoom() * 2)) - (state.getX() / state.getZoom())).setDuration(0);
            }

            @Override
            public void onStateReset(State oldState, State newState) {

            }
        });
        layout.getController().setOnGesturesListener(new GestureController.OnGestureListener() {
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
//                if (clickedLayout != null && onWordClick != null) {
//                    onWordClick.onClick(clickedLayout);
//                    clickedLayout = null;
//                    return true;
//                }
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
        layout.getController().getSettings().setOverzoomFactor(1f);
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
        layContentUnavailable.setVisibility(View.GONE);
        btnEnglishLanguage.setVisibility(View.GONE);
        btnHindiLanguage.setVisibility(View.GONE);
        btnUrduLanguage.setVisibility(View.GONE);
        txtAnd1.setVisibility(View.GONE);
        txtAnd2.setVisibility(View.GONE);
        descriptionNotAvailable = MyHelper.getString(R.string.content_not_available);
        txtAnd1.setText(MyHelper.getString(R.string.and));
        txtAnd2.setText(MyHelper.getString(R.string.and));
        btnEnglishLanguage.setOnClickListener(v -> {
            MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
            sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
        });
        btnHindiLanguage.setOnClickListener(v -> {
            MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
            sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
        });
        btnUrduLanguage.setOnClickListener(v -> {
            MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
            sendBroadCast(BROADCAST_LANGUAGE_CHANGED);
        });

        layContentUnavailable.setVisibility(View.GONE);
    }

    public final void renderContent(final ContentPageModel contentPageModel, final Enums.TEXT_ALIGNMENT textAlignment) {
        int dp_5 = (int) Utils.pxFromDp(5);
        layContentUnavailable.setVisibility(View.GONE);
        btnEnglishLanguage.setVisibility(View.GONE);
        btnHindiLanguage.setVisibility(View.GONE);
        btnUrduLanguage.setVisibility(View.GONE);
        txtAnd1.setVisibility(View.GONE);
        txtAnd2.setVisibility(View.GONE);
        if (contentPageModel == null || textAlignment == null)
            return;
//        String descriptionNotAvailable = "";
        boolean isEnglishAvailable = contentPageModel.getHaveEn().contentEquals("true");
        boolean isHindiAvailable = contentPageModel.getHaveHi().contentEquals("true");
        boolean isUrduAvailable = contentPageModel.getHaveUr().contentEquals("true");
//        if (isEnglishAvailable || isHindiAvailable || isUrduAvailable) {
//            if (!isEnglishAvailable && !isHindiAvailable && !isUrduAvailable)
//                descriptionNotAvailable = "Description not available";
//            else {
//                String firstText = isEnglishAvailable ? "english" : (isHindiAvailable ? "hindi" : "urdu");
//                String secondText = isEnglishAvailable ? (isHindiAvailable ? "and hindi" : isUrduAvailable ? "and urdu" : "") : isHindiAvailable ? (isUrduAvailable ? "and urdu" : "") : "";
//                descriptionNotAvailable = String.format(Locale.getDefault(), "Content is available only in %s %s language.", firstText, secondText);
//            }
//        }
        TextPaint paint = new TextPaint();
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(engtf);
        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(hinditf);
        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(urdutf);
        }

        if (author_name != null) {
            author_name.setTag(contentPageModel);
            author_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentPageModel contentPageModel = (ContentPageModel) v.getTag();
                    if (contentPageModel.getPoet() != null && !TextUtils.isEmpty(contentPageModel.getPoet().getPoetID())) {
                        Intent intent = PoetDetailActivity.getInstance(getActivity(), contentPageModel.getPoet().getPoetID());
                        if (TextUtils.isEmpty(contentPageModel.getTypeSlug()))
                            intent.putExtra("shouldLandOnProfile", true);
                        else if (contentPageModel.getTypeSlug().contentEquals("nazm") || contentPageModel.getTypeSlug().contentEquals("nazms"))
                            intent.putExtra("shouldLandOnNazm", true);
                        else if (contentPageModel.getTypeSlug().contentEquals("ghazals"))
                            intent.putExtra("shouldLandOnGhazal", true);
                        else if (contentPageModel.getTypeSlug().contentEquals("couplets"))
                            intent.putExtra("shouldLandOnSher", true);
                        else
                            intent.putExtra("shouldLandOnProfile", true);
                        startActivity(intent);
                    }
                }
            });
        }
        int desireScreenWidth = (int) (Utils.getScreenWidth() - Utils.pxFromDp(20));
        float maxLength = 0;
        String maxContent = "";
        boolean maxLengthCalculated = false;
        int desiredFontSize = 15;
        while (!maxLengthCalculated) {
            paint.setTextSize(Utils.pxFromDp(desiredFontSize));
            for (int i = 0; i < contentPageModel.getRender().getParas().size(); i++) {
                for (int j = 0; j < contentPageModel.getRender().getParas().get(i).getLines().size(); j++) {
                    if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                        String lineFullText = contentPageModel.getRender().getParas().get(i).getLines().get(j).getFullText();
                        ArrayList<WordContainer> wordContainers = contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers();
                        String firstWord = wordContainers.get(0).getWord();
                        String secondWord = wordContainers.size() > 1 ? wordContainers.get(1).getWord() : "";
                        String lastWord = wordContainers.get(wordContainers.size() - 1).getWord();
                        if (firstWord.trim().contentEquals(lastWord.trim())) {
                            String lineFullTextNew = lineFullText.replaceAll(firstWord, "").trim();
                            if (lineFullTextNew.startsWith(secondWord))
                                Collections.reverse(contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers());
                        } else if (lineFullText.startsWith(firstWord.trim()))
                            Collections.reverse(contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers());
                        Collections.reverse(contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers());
                    }
                    float currentMeasuredLength = paint.measureText(contentPageModel.getRender().getParas().get(i).getLines().get(j).getFullText());
                    currentMeasuredLength = currentMeasuredLength + (contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().size() * 2 * dp_5) + (2 * dp_5);

                    if (currentMeasuredLength > maxLength) {
                        maxLength = currentMeasuredLength;
                        maxContent = contentPageModel.getRender().getParas().get(i).getLines().get(j).getFullText();
                    }
                }
            }
            if (maxLength < desireScreenWidth)
                maxLengthCalculated = true;
            else {
                --desiredFontSize;
                maxLength = 0;
            }
        }
        float max_zoom = 2f;
        layout.getController().getSettings().setMaxZoom((15f / (float) desiredFontSize) * 2);
        layParaContainer.removeAllViews();
//        txtAuthor.setText(contentPageModel.getPoet().getTagName());
//        txtTitle.setText(contentPageModel.getTitle());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT);
        layParaContainer.setLayoutParams(layoutParams);

        if (txtContentNotAvailable != null)
            txtContentNotAvailable.setVisibility(View.GONE);
        if ((MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH && !isEnglishAvailable) || (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI && !isHindiAvailable) || (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU && !isUrduAvailable)) {
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
            layout.getController().getSettings().setMaxZoom(1);
            txtContentNotAvailable.setVisibility(View.VISIBLE);
            txtContentNotAvailable.setText(descriptionNotAvailable);
//            txtContentNotAvailable.setText(descriptionNotAvailable);
//            if (txtContentNotAvailable != null) {
//                txtContentNotAvailable.setVisibility(View.VISIBLE);
//                txtContentNotAvailable.setText(descriptionNotAvailable);
//                layout.getController().getSettings().setMaxZoom(1);
//            }
        } else

            try {
                for (int i = 0; i < contentPageModel.getRender().getParas().size(); i++) {
                    for (int j = 0; j < contentPageModel.getRender().getParas().get(i).getLines().size(); j++) {
                        LinearLayout lineView = (LinearLayout) getLayoutInflater().inflate(R.layout.cell_line, null);
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH)
                            lineView.setPadding(0, (int) Utils.pxFromDp(5), 0, (int) Utils.pxFromDp(5));
//                    if (CommonUtil.languageCode == 3)
//                        lineView.setGravity(Gravity.END | Gravity.RIGHT);
//                    else
                        lineView.setGravity(Gravity.START | Gravity.LEFT);
                        layParaContainer.addView(lineView);
                        if (j == (contentPageModel.getRender().getParas().get(i).getLines().size() - 1))
                            layParaContainer.addView(getLayoutInflater().inflate(R.layout.cell_empty_line, null));
                        for (int k = 0; k < contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().size(); k++) {
                            TextView txtWord = (TextView) getLayoutInflater().inflate(R.layout.cell_word, null);
//                        if (CommonUtil.languageCode != 3) {//for urdu, we need to use roboto, otherwise custom font
//                            Typeface textTypeface = Typeface.createFromAsset(getAssets(), "fonts/Merriweather-Light.ttf");
//                            txtWord.setTypeface(textTypeface);
//                        }
                            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
                                txtWord.setTypeface(engtf);
                            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
                                txtWord.setTypeface(hinditf);
                            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
                                txtWord.setTypeface(urdutf);
                            }
                            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
                                txtWord.setTypeface(engtf);
                            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
                                txtWord.setTypeface(hinditf);
                            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
                                txtWord.setTypeface(urdutf);
                                txtWord.setPadding(dp_5, dp_5, dp_5, dp_5);
                            }
                            txtWord.setTextSize(desiredFontSize);
                            txtWord.setText(String.format("%s ", contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().get(k).getWord()));
                            lineView.addView(txtWord);
                            LinearLayout.LayoutParams lp;
                            if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT) {
                                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            } else
                                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                            txtWord.setLayoutParams(lp);
//                        txtWord.setTag(contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().get(k).getWord());
                            txtWord.setTag(R.id.tag_word, contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().get(k));
                            txtWord.setTag(R.id.tag_line, contentPageModel.getRender().getParas().get(i).getLines().get(j));
                            txtWord.setTag(R.id.tag_para, contentPageModel.getRender().getParas().get(i));
                            txtWord.setTag(R.id.tag_line_number, j + 1);
                            txtWord.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                                @Override
                                public void onSingleClick(final View v) {
                                    if (!shouldCancelSingleClick && onWordClick != null)
                                        onWordClick.onClick(v);
                                }

                                @Override
                                public void onDoubleClick(View view) {

                                }
                            }));
                            txtWord.setOnLongClickListener(v -> {
//                                v.setTag(strBuilder.toString());
                                if (onWordLongClick != null)
                                    onWordLongClick.onClick(v);
//                                setClipboard(getActivity(), strBuilder.toString());
//                                Toast.makeText(getActivity(), "para copied to clipboard", Toast.LENGTH_SHORT).show();
                                return true;
                            });
                            if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT)
                                txtWord.setGravity(Gravity.START | Gravity.LEFT);
                            else {
                                if (k == (contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().size() - 1))
                                    txtWord.setGravity(Gravity.END | Gravity.RIGHT);
                                else if (k == 0)
                                    txtWord.setGravity(Gravity.START | Gravity.LEFT);
                                else
                                    txtWord.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//        layout.post(new Runnable() {
//            @Override
//            public void run() {
//                layout.getChildAt(0).setMinimumHeight(layout.getHeight());
//            }
//        });

        layout.post(new Runnable() {
            @Override
            public void run() {
                layout.getChildAt(0).setMinimumHeight(layout.getHeight());
                ViewGroup paraHolder = (ViewGroup) layMainContainer.getParent();
                int desiredTopPadding = (int) Math.max(Utils.pxFromDp(88), layTopContainer.getHeight());
                paraHolder.setPadding(paraHolder.getPaddingLeft(), desiredTopPadding, paraHolder.getPaddingRight(), paraHolder.getPaddingBottom());
            }
        });
    }

    long lastClickTime = 0;

    private void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public static void renderContent(RenderContent renderContent,
                                     BaseActivity activity,
                                     Enums.TEXT_ALIGNMENT textAlignment,
                                     final View.OnClickListener onWordClick,
                                     final View.OnClickListener onWordLongClick,
                                     LinearLayout layParaContainer) {

        int dp_5 = (int) Utils.pxFromDp(5);

        if (layParaContainer == null)
            return;
//        layParaContainer.setPadding((int) Utils.pxFromDp(activity, 20),
//                layParaContainer.getPaddingTop(),
//                (int) Utils.pxFromDp(activity, 20),
//                layParaContainer.getPaddingBottom());
        Typeface engtf, hinditf, urdutf;
        engtf = Typeface.createFromAsset(activity.getAssets(), "fonts/MerriweatherExtended-LightItalic.ttf");
        hinditf = Typeface.createFromAsset(activity.getAssets(), "fonts/Laila-Regular.ttf");
        urdutf = Typeface.createFromAsset(activity.getAssets(), "fonts/NotoNastaliqUrdu-Regular.ttf");

        TextPaint paint = new TextPaint();
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(engtf);
        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(hinditf);
        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(urdutf);
        }
//        int desireScreenWidth = (int) (Utils.getScreenWidth() - Utils.pxFromDp(activity, 70));
        int desireScreenWidth = (int) (Utils.getScreenWidth() - Utils.pxFromDp(5));
        float maxLength = 0;
        String maxContent = "";
        boolean maxLengthCalculated = false;
        int desiredFontSize = 15;
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
            desiredFontSize = 12;
        while (!maxLengthCalculated) {
            paint.setTextSize(Utils.pxFromDp(desiredFontSize));
            for (int i = 0; i < renderContent.getParas().size(); i++) {
                for (int j = 0; j < renderContent.getParas().get(i).getLines().size(); j++) {
                    if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                        String lineFullText = renderContent.getParas().get(i).getLines().get(j).getFullText();
                        ArrayList<WordContainer> wordContainers = renderContent.getParas().get(i).getLines().get(j).getWordContainers();
                        String firstWord = wordContainers.get(0).getWord();
                        String secondWord = wordContainers.size() > 1 ? wordContainers.get(1).getWord() : "";
                        String lastWord = wordContainers.get(wordContainers.size() - 1).getWord();
                        if (firstWord.trim().contentEquals(lastWord.trim())) {
                            String lineFullTextNew = lineFullText.replaceAll(firstWord, "").trim();
                            if (lineFullTextNew.startsWith(secondWord))
                                Collections.reverse(renderContent.getParas().get(i).getLines().get(j).getWordContainers());
                        } else if (lineFullText.startsWith(firstWord.trim()))
                            Collections.reverse(renderContent.getParas().get(i).getLines().get(j).getWordContainers());
                        Collections.reverse(renderContent.getParas().get(i).getLines().get(j).getWordContainers());
                    }
                    float currentMeasuredLength = paint.measureText(renderContent.getParas().get(i).getLines().get(j).getFullText());
                    currentMeasuredLength = currentMeasuredLength + (renderContent.getParas().get(i).getLines().get(j).getWordContainers().size() * 2 * dp_5) + (2 * dp_5);
                    if (currentMeasuredLength > maxLength) {
                        maxLength = currentMeasuredLength;
                        maxContent = renderContent.getParas().get(i).getLines().get(j).getFullText();
                    }
                }
            }
            if (maxLength < desireScreenWidth)
                maxLengthCalculated = true;
            else {
                --desiredFontSize;
                maxLength = 0;
            }
        }
        if (layParaContainer.getParent() instanceof RelativeLayout)
            layParaContainer.setLayoutParams(new RelativeLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT));
        else if (layParaContainer.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            layParaContainer.setLayoutParams(params);
        } else if (layParaContainer.getParent() instanceof FrameLayout)
            layParaContainer.setLayoutParams(new FrameLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT));
        layParaContainer.removeAllViews();
//        txtAuthor.setText(contentPageModel.getPoet().getTagName());
//        txtTitle.setText(contentPageModel.getTitle());
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT);
//        layParaContainer.setLayoutParams(layoutParams);
        try {
            for (int i = 0; i < renderContent.getParas().size(); i++) {
                for (int j = 0; j < renderContent.getParas().get(i).getLines().size(); j++) {
                    LinearLayout lineView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cell_line, null);
                    if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH)
                        lineView.setPadding(0, dp_5, 0, dp_5);
//                    if (CommonUtil.languageCode == 3)
//                        lineView.setGravity(Gravity.END | Gravity.RIGHT);
//                    else
                    lineView.setGravity(Gravity.START | Gravity.LEFT);
                    layParaContainer.addView(lineView);
//                    if (j == (renderContent.getParas().get(i).getLines().size() - 1))
//                        layParaContainer.addView(activity.getLayoutInflater().inflate(R.layout.cell_empty_line, null));
                    for (int k = 0; k < renderContent.getParas().get(i).getLines().get(j).getWordContainers().size(); k++) {
                        TextView txtWord = (TextView) activity.getLayoutInflater().inflate(R.layout.cell_word, null);
//                        if (CommonUtil.languageCode != 3) {//for urdu, we need to use roboto, otherwise custom font
//                            Typeface textTypeface = Typeface.createFromAsset(getAssets(), "fonts/Merriweather-Light.ttf");
//                            txtWord.setTypeface(textTypeface);
//                        }
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
                            txtWord.setTypeface(engtf);
                        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
                            txtWord.setTypeface(hinditf);
                        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
                            txtWord.setTypeface(urdutf);
                            txtWord.setPadding(dp_5, dp_5, dp_5, dp_5);
                        }
                        txtWord.setTextSize(desiredFontSize);
                        txtWord.setText(String.format("%s ", renderContent.getParas().get(i).getLines().get(j).getWordContainers().get(k).getWord()));
                        lineView.addView(txtWord);
                        LinearLayout.LayoutParams lp;
                        if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT) {
                            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        } else
                            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                        txtWord.setLayoutParams(lp);
//                        txtWord.setTag(contentPageModel.getRender().getParas().get(i).getLines().get(j).getWordContainers().get(k).getWord());
                        txtWord.setTag(R.id.tag_word, renderContent.getParas().get(i).getLines().get(j).getWordContainers().get(k));
                        txtWord.setTag(R.id.tag_line, renderContent.getParas().get(i).getLines().get(j));
                        txtWord.setTag(R.id.tag_para, renderContent.getParas().get(i));
                        txtWord.setTag(R.id.tag_line_number, j + 1);
                        txtWord.setOnClickListener(new DoubleClick(new DoubleClickListener() {
                            @Override
                            public void onSingleClick(final View v) {
                                if (onWordClick != null)
                                    onWordClick.onClick(v);
                            }

                            @Override
                            public void onDoubleClick(View view) {

                            }
                        }));
                        txtWord.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
//                                v.setTag(strBuilder.toString());
                                if (onWordLongClick != null)
                                    onWordLongClick.onClick(v);
//                                setClipboard(getActivity(), strBuilder.toString());
//                                Toast.makeText(getActivity(), "para copied to clipboard", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
                        if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT)
                            txtWord.setGravity(Gravity.START | Gravity.LEFT);
                        else {
                            if (k == (renderContent.getParas().get(i).getLines().get(j).getWordContainers().size() - 1))
                                txtWord.setGravity(Gravity.END | Gravity.RIGHT);
                            else if (k == 0)
                                txtWord.setGravity(Gravity.START | Gravity.LEFT);
                            else
                                txtWord.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
