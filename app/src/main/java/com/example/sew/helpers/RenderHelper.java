package com.example.sew.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.DoubleClick;
import com.example.sew.common.DoubleClickListener;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MeaningBottomPopupWindow;
import com.example.sew.common.Utils;
import com.example.sew.fragments.BaseFragment;
import com.example.sew.models.Line;
import com.example.sew.models.Para;
import com.example.sew.models.WordContainer;
import com.google.android.gms.common.util.CollectionUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class RenderHelper {
    private static final String baseUrl = "file:///android_asset/";
    private static final String mimeType = "text/html; charset=UTF-8";
    private static final String historyUrl = "about:blank";
    private static final String encoding = null;

    private static final String TAG = "ZOOM_LAYOUT";
    private static GestureFrameLayout zoomLayout;

    interface OnMaxLengthCalculatedListener {
        void onMaxLengthCalculated(float maxLength, int desiredFontSize);
    }

    interface OnParaViewsCreatedListener {
        void onParaViewsCreated(ArrayList<View> views);
    }

    static int textColors;

    private static void renderContent(ArrayList<Para> paras,
                                      BaseActivity activity,
                                      Enums.TEXT_ALIGNMENT textAlignment,
                                      final View.OnClickListener onWordClick,
                                      final View.OnLongClickListener onWordLongClick,
                                      LinearLayout layParaContainer, GestureFrameLayout zoomLayout, int textColor, int leftRightPadding,
                                      boolean isHTML, String htmlContent, boolean showLoadingDialog, boolean showTranslation) {
        // Para para = paras.get(0);
        leftRightPadding += 5;
        int dp_5 = (int) Utils.pxFromDp(5);
        int dp_1 = (int) Utils.pxFromDp(1);
        if (layParaContainer == null)
            return;
        Typeface entTf, hindiTf, urduTf;
        entTf = ResourcesCompat.getFont(activity, R.font.merriweather_extended_light_italic_eng);
        hindiTf = ResourcesCompat.getFont(activity, R.font.laila_regular_hin);
        urduTf = ResourcesCompat.getFont(activity, R.font.noto_nastaliq_regular_urdu);
        final float maxFontSize = MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? 16f : 18f;
        if (isHTML) {
            layParaContainer.setVisibility(View.GONE);
//            txtWord.setText(MyHelper.fromHtml(htmlContent));
            TextView txtPlainContent = activity.findViewById(R.id.txtPlainContent);
            txtPlainContent.setVisibility(View.GONE);
            WebView webView = activity.findViewById(R.id.wvInfo);
            webView.setVisibility(View.VISIBLE);
            addCustomWebViewClick(webView, activity);
            zoomLayout.getController().getSettings().setMaxZoom(1);
            webView.setBackgroundColor(Color.parseColor("#00000000"));
//            if(MyService.getDarkTheme()==DARK_MODE_NO)
//                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
//            else if(MyService.getDarkTheme()==DARK_MODE_YES)
//                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
//            else
//                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_AUTO);
//            ViewTreeObserver viewTreeObserver = webView.getViewTreeObserver();

            textColors = textColor;
            webView.getSettings().setSupportZoom(false);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (showLoadingDialog)
                        activity.dismissDialog();
                    BaseActivity.sendBroadCast(ICommonValues.BROADCAST_CONTENT_RENDERED);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    },100);
                }
            });
            switch (MyService.getSelectedLanguage()) {
                case ENGLISH:
                    txtPlainContent.setTypeface(entTf);
                    webView.loadDataWithBaseURL(baseUrl, getStyledFontEnglish(htmlContent), mimeType, encoding, historyUrl);
                    break;
                case HINDI:
                    txtPlainContent.setTypeface(hindiTf);
                    webView.loadDataWithBaseURL(baseUrl, getStyledFontHindi(htmlContent), mimeType, encoding, historyUrl);
                    break;
                case URDU:
                    txtPlainContent.setTypeface(urduTf);
                    webView.loadDataWithBaseURL(baseUrl, getStyledFontUrdu(htmlContent), mimeType, encoding, historyUrl);
                    break;
            }
            txtPlainContent.setText(MyHelper.fromHtml(htmlContent));
            txtPlainContent.setBackgroundColor(activity.getAppBackgroundColor());


        } else {
            int finalLeftRightPadding = leftRightPadding;
            calculateMaxLength(activity, paras, leftRightPadding, showLoadingDialog, (maxLength, desiredFontSize) -> {
                if (zoomLayout != null) {
                    if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                        zoomLayout.getController().getSettings().setMaxZoom((maxFontSize / (float) desiredFontSize) * 2);
                    else
                        zoomLayout.getController().getSettings().setMaxZoom((maxFontSize / (float) desiredFontSize) * 2);
                }
                if (layParaContainer.getParent() instanceof RelativeLayout) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.layTopContainer);
                    layParaContainer.setLayoutParams(layoutParams);
                } else if (layParaContainer.getParent() instanceof LinearLayout) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (textAlignment == Enums.TEXT_ALIGNMENT.CENTER)
                        params.gravity = Gravity.CENTER_HORIZONTAL;
                    else {
                        params.gravity = Gravity.START;
                        params.setMarginStart(finalLeftRightPadding / 2);
                    }
                    layParaContainer.setLayoutParams(params);
                } else if (layParaContainer.getParent() instanceof FrameLayout)
                    layParaContainer.setLayoutParams(new FrameLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT));

                createParaViews(activity, paras, onWordClick, onWordLongClick, textAlignment, desiredFontSize, textColor, showLoadingDialog, showTranslation, views -> {
                    activity.dismissDialog();
                    if (!CollectionUtils.isEmpty(views)) {
                        layParaContainer.removeAllViews();
                        ArrayList<View> currentSectionLines = new ArrayList<>();
                        for (View currView : views) {
                            if (isEmptyLine(currView)) {
/*                                LinearLayout linearLayout = new LinearLayout(activity);
                                linearLayout.setPadding(finalLeftRightPadding / 4, 0, finalLeftRightPadding / 4, 0);
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMarginStart(finalLeftRightPadding / 4);
                                params.setMarginEnd(finalLeftRightPadding / 4);
                                params.gravity = Gravity.CENTER_HORIZONTAL;
                                linearLayout.setLayoutParams(params);
                                linearLayout.setBackgroundResource(R.drawable.render_content_selected_background);*/
                                LinearLayout linearLayout = getContainerView(finalLeftRightPadding, activity, (int) Math.ceil(maxLength));
                                Para para = (currView.getTag(R.id.tag_para) instanceof Para) ? (Para) currView.getTag(R.id.tag_para) : null;
                                if (para != null)
                                    para.setContainerView(linearLayout);
                                for (View view : currentSectionLines)
                                    linearLayout.addView(view);
                                linearLayout.setTag(R.id.tag_para, para);
                                layParaContainer.addView(linearLayout);
                                layParaContainer.addView(currView);
                                currentSectionLines.clear();
                            } else
                                currentSectionLines.add(currView);
                        }
                        if (!CollectionUtils.isEmpty(currentSectionLines)) {
/*                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setPadding(finalLeftRightPadding / 4, 0, finalLeftRightPadding / 4, 0);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMarginStart(finalLeftRightPadding / 4);
                            params.setMarginEnd(finalLeftRightPadding / 4);
                            params.gravity = Gravity.CENTER_HORIZONTAL;*/

                            LinearLayout linearLayout = getContainerView(finalLeftRightPadding, activity, (int) Math.ceil(maxLength));
                            Para para = null;
                            for (View view : currentSectionLines) {
                                para = (Para) view.getTag(R.id.tag_para);
                                linearLayout.addView(view);
                            }
                            if (para != null)
                                para.setContainerView(linearLayout);
                            linearLayout.setTag(R.id.tag_para, para);
                            layParaContainer.addView(linearLayout);
                            currentSectionLines.clear();
                        }
                    }
                    BaseActivity.sendBroadCast(ICommonValues.BROADCAST_CONTENT_RENDERED);
                });
            });
        }
    }

    private static LinearLayout getContainerView(int leftRightPadding, Activity activity, int maxWidth) {
        int desireScreenWidth = (int) (Utils.getScreenWidth() - Math.ceil(leftRightPadding / 2f));
        int desiredPaddingLeftRight = (desireScreenWidth - maxWidth) / 2;
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setPadding(desiredPaddingLeftRight, 0, desiredPaddingLeftRight, 0);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(leftRightPadding / 4);
        params.setMarginEnd(leftRightPadding / 4);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    private static boolean isEmptyLine(View currView) {
        return currView.findViewById(R.id.layEmptyLine) != null;
    }

    private static String getTranslationText(Para para) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Line line : para.getTranslations()) {
            stringBuilder.append(line.getFullText());
            if (para.getTranslations().indexOf(line) != (para.getTranslations().size() - 1))
                stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static void createParaViews(BaseActivity activity, ArrayList<Para> paras,
                                        final View.OnClickListener onWordClick,
                                        final View.OnLongClickListener onWordLongClick,
                                        final Enums.TEXT_ALIGNMENT textAlignment,
                                        final int desiredFontSize,
                                        final int textColor,
                                        final boolean showLoadingDialog,
                                        final boolean showTranslation,
                                        final OnParaViewsCreatedListener onParaViewsCreatedListener) {
        if (showLoadingDialog)
            activity.showDialog();
        final int dp_5 = (int) Utils.pxFromDp(5);
        final ArrayList<View> views = new ArrayList<>(paras.size() * 2);
        final DoubleClick doubleClick = new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(final View v) {
                if (onWordClick != null)
                    onWordClick.onClick(v);
            }

            @Override
            public void onDoubleClick(View view) {

            }
        });
        new Thread(() -> {
            int lineNumber = 0;
            try {
                for (int i = 0; i < paras.size(); i++) {
                    Para para = paras.get(i);
                    for (int j = 0; j < para.getLines().size(); j++) {
                        ++lineNumber;
                        LinearLayout lineView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cell_line, null);
                        lineView.setTag(R.id.tag_para, para);
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH)
                            lineView.setPadding(0, dp_5, 0, dp_5);
                        lineView.setGravity(Gravity.START | Gravity.BOTTOM);
                        views.add(lineView);
                        if (j == (para.getLines().size() - 1) && paras.size() > 1) {
                            TextView txtWord = null;
                            TextView txtEmptyWord = null;
                            LinearLayout emptyLineView = null;
                            emptyLineView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cell_empty_line, null);
                            emptyLineView.setTag(R.id.tag_para, para);
                            txtEmptyWord = emptyLineView.findViewById(R.id.txtEmptyWord);
                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, desiredFontSize, activity.getResources().getDisplayMetrics());
                            txtEmptyWord.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels);
                            if (showTranslation) {
                                String translationText = getTranslationText(para);
                                if (!TextUtils.isEmpty(translationText.trim())) {
                                    txtWord = emptyLineView.findViewById(R.id.txtWord);
                                    txtWord.setVisibility(View.VISIBLE);
                                    txtWord.setText(translationText);
//                                    txtWord.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels);
                                }
                            }
                            views.add(emptyLineView);
                        }

                        for (int k = 0; k < para.getLines().get(j).getWordContainers().size(); k++) {
                            TextView txtWord = null;
                            LinearLayout layWordContainer = null;
                            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                                layWordContainer = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cell_word_ur, null);
                                txtWord = layWordContainer.findViewById(R.id.txtWord);
                            } else {
                                layWordContainer = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cell_word, null);
                                txtWord = layWordContainer.findViewById(R.id.txtWord);
                            }
                            if (textColor != activity.getDarkGreyTextColor())
                                txtWord.setTextColor(textColor);
                            /* to make ripple inside bounds */
                            lineView.setBackgroundColor(Color.parseColor("#01000000"));
                            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                                int padding_horizontal = (int) Utils.pxFromDp(2);
                                layWordContainer.setPadding(padding_horizontal, 0, padding_horizontal, 0);
                            }
                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, desiredFontSize, activity.getResources().getDisplayMetrics());
                            txtWord.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels);
                            // txtWord.setTextSize(desiredFontSize);
                            txtWord.setText(String.format("%s ", para.getLines().get(j).getWordContainers().get(k).getWord()));
                            lineView.addView(layWordContainer);

                            LinearLayout.LayoutParams lp;
                            if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT) {
                                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            } else
                                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                            layWordContainer.setLayoutParams(lp);
                            txtWord.setTag(R.id.tag_word, para.getLines().get(j).getWordContainers().get(k));
                            txtWord.setTag(R.id.tag_line, para.getLines().get(j));
                            txtWord.setTag(R.id.tag_para, para);
                            txtWord.setTag(R.id.tag_line_number, lineNumber);
                            txtWord.setOnClickListener(doubleClick);
                            txtWord.setOnLongClickListener(v -> {
                                if (onWordLongClick != null)
                                    onWordLongClick.onLongClick(v);
                                return true;
                            });
                            if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT)
                                layWordContainer.setGravity(Gravity.START);
                            else {
                                if (k == (para.getLines().get(j).getWordContainers().size() - 1))
                                    layWordContainer.setGravity(Gravity.END);
                                else if (k == 0) {
                                    layWordContainer.setGravity(Gravity.START);
                                } else
                                    layWordContainer.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }
                    }
                }
                activity.runOnUiThread(() -> onParaViewsCreatedListener.onParaViewsCreated(views));
            } catch (Exception e) {
                if (showLoadingDialog)
                    activity.dismissDialog();
                e.printStackTrace();
            }

        }).start();
    }

    private static void calculateMaxLength(final BaseActivity activity,
                                           ArrayList<Para> paras,
                                           int leftRightPadding,
                                           boolean showLoadingDialog,
                                           final OnMaxLengthCalculatedListener onMaxLengthCalculatedListener) {
        if (showLoadingDialog)
            activity.showDialog();
        final int[] desiredFontSize = {0};
        final float[] maxLength = {0};
        final float maxHindiFontSize = 15f;
        final float maxOtherFontSize = 18f;
        int finalLeftRightPadding = leftRightPadding;
        new Thread(() -> {
            int dp_5 = (int) Utils.pxFromDp(5);
            int dp_1 = (int) Utils.pxFromDp(1);
            Typeface entTf, hindiTf, urduTf;
            entTf = ResourcesCompat.getFont(activity, R.font.merriweather_extended_light_italic_eng);
            hindiTf = ResourcesCompat.getFont(activity, R.font.laila_regular_hin);
            urduTf = ResourcesCompat.getFont(activity, R.font.noto_nastaliq_regular_urdu);
            final float maxFontSize = MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI ? maxHindiFontSize : maxOtherFontSize;
            TextPaint paint = new TextPaint();
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
                paint.setTypeface(entTf);
            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
                paint.setTypeface(hindiTf);
            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
                paint.setTypeface(urduTf);
            }
            int desireScreenWidth = (int) (Utils.getScreenWidth() - Math.max(Utils.pxFromDp(5), finalLeftRightPadding));
            String maxContent = "";
            boolean maxLengthCalculated = false;
            int maxLengthPara = 0;
            int maxLengthLine = 0;
            desiredFontSize[0] = (int) maxFontSize;
            while (!maxLengthCalculated) {
                paint.setTextSize(Utils.pxFromDp(desiredFontSize[0]));
                for (int i = 0; i < paras.size(); i++) {
                    Para para = paras.get(i);
                    for (int j = 0; j < para.getLines().size(); j++) {
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                            String lineFullText = para.getLines().get(j).getFullText();
                            ArrayList<WordContainer> wordContainers = para.getLines().get(j).getWordContainers();
                            String firstWord = "";
                            String secondWord = "";
                            String lastWord = "";
                            if (CollectionUtils.isEmpty(wordContainers) || wordContainers.size() < 2) {
                                if (!CollectionUtils.isEmpty(wordContainers))
                                    firstWord = wordContainers.get(0).getWord();
                            } else {
                                firstWord = wordContainers.get(0).getWord();
                                secondWord = wordContainers.size() > 1 ? wordContainers.get(1).getWord() : "";
                                lastWord = wordContainers.get(wordContainers.size() - 1).getWord();
                            }

                            if (firstWord.trim().contentEquals(lastWord.trim())) {
                                String lineFullTextNew = lineFullText.replace(firstWord, "").trim();
                                if (lineFullTextNew.startsWith(secondWord))
                                    Collections.reverse(para.getLines().get(j).getWordContainers());
                            } else if (lineFullText.startsWith(firstWord.trim()))
                                Collections.reverse(para.getLines().get(j).getWordContainers());
                            Collections.reverse(para.getLines().get(j).getWordContainers());
                        }
                        float currentMeasuredLength = paint.measureText(para.getLines().get(j).getFullText());
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
//                        int padding_vertical = Math.round(Utils.pxFromDp(Math.round(font_size_dp / 4f) + 1));
//                            int padding_horizontal = Math.round(Utils.pxFromDp(Math.round(desiredFontSize[0] / 4f)));
//                            padding_horizontal += Utils.pxFromDp(6);
//                            currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * padding_horizontal) + (2 * padding_horizontal);
                            currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * dp_1) + (2 * Math.max(dp_5, finalLeftRightPadding));
                        } else
                            currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * dp_1) + (2 * dp_5);
                        if (currentMeasuredLength > maxLength[0]) {
                            maxLengthPara = i;
                            maxLengthLine = j;
                            maxLength[0] = currentMeasuredLength;
                            maxContent = para.getLines().get(j).getFullText();
                        }
                    }
                }
                if (maxLength[0] < desireScreenWidth)
                    maxLengthCalculated = true;
                else {
                    --desiredFontSize[0];
                    maxLength[0] = 0;
                }
            }
            desiredFontSize[0] = Math.max(desiredFontSize[0], 6);
//            desiredFontSize[0] -= 1;


            /*
              New functionality, need to increase font size if content is small
             */
            if (desiredFontSize[0] == (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI ? maxHindiFontSize : maxOtherFontSize)) {
                int maxUpperLimit = (int) (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI ? maxHindiFontSize + 7 : maxOtherFontSize + 7);
                maxLengthCalculated = false;
                int newScreenWidth = 0;
                float newMaxLength = maxLength[0];
                for (int i = desiredFontSize[0] + 1; i <= maxUpperLimit; i++) {
                    newScreenWidth = i;
                    float maxLengthToFillThisText = getMaxLengthToFillThisText(activity, paras, i, desireScreenWidth, finalLeftRightPadding);
                    boolean canTextRender = maxLengthToFillThisText < desireScreenWidth;
                    if (!canTextRender) {
                        newScreenWidth -= 1;
                        break;
                    } else
                        newMaxLength = maxLengthToFillThisText;
                }
                desiredFontSize[0] = newScreenWidth;
                maxLength[0] = newMaxLength;
            }

            Log.d(TAG, "calculateMaxLength: " + desiredFontSize[0]);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onMaxLengthCalculatedListener.onMaxLengthCalculated(maxLength[0], desiredFontSize[0]);
                }
            });
        })
                .start();

    }

    private static float getMaxLengthToFillThisText(BaseActivity activity, ArrayList<Para> paras, int desiredFontSize, int desireScreenWidth, int finalLeftRightPadding) {
        float maxLength = 0f;
        int dp_5 = (int) Utils.pxFromDp(5);
        int dp_1 = (int) Utils.pxFromDp(1);
        Typeface entTf, hindiTf, urduTf;
        entTf = ResourcesCompat.getFont(activity, R.font.merriweather_extended_light_italic_eng);
        hindiTf = ResourcesCompat.getFont(activity, R.font.laila_regular_hin);
        urduTf = ResourcesCompat.getFont(activity, R.font.noto_nastaliq_regular_urdu);
        TextPaint paint = new TextPaint();
        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(entTf);
        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(hindiTf);
        } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
            paint.setTypeface(urduTf);
        }
        paint.setTextSize(Utils.pxFromDp(desiredFontSize));
        for (int i = 0; i < paras.size(); i++) {
            Para para = paras.get(i);
            for (int j = 0; j < para.getLines().size(); j++) {
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                    String lineFullText = para.getLines().get(j).getFullText();
                    ArrayList<WordContainer> wordContainers = para.getLines().get(j).getWordContainers();
                    String firstWord = "";
                    String secondWord = "";
                    String lastWord = "";
                    if (CollectionUtils.isEmpty(wordContainers) || wordContainers.size() < 2) {
                        if (!CollectionUtils.isEmpty(wordContainers))
                            firstWord = wordContainers.get(0).getWord();
                    } else {
                        firstWord = wordContainers.get(0).getWord();
                        secondWord = wordContainers.size() > 1 ? wordContainers.get(1).getWord() : "";
                        lastWord = wordContainers.get(wordContainers.size() - 1).getWord();
                    }

                    if (firstWord.trim().contentEquals(lastWord.trim())) {
                        String lineFullTextNew = lineFullText.replace(firstWord, "").trim();
                        if (lineFullTextNew.startsWith(secondWord))
                            Collections.reverse(para.getLines().get(j).getWordContainers());
                    } else if (lineFullText.startsWith(firstWord.trim()))
                        Collections.reverse(para.getLines().get(j).getWordContainers());
                    Collections.reverse(para.getLines().get(j).getWordContainers());
                }
                float currentMeasuredLength = paint.measureText(para.getLines().get(j).getFullText());
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
//                        int padding_vertical = Math.round(Utils.pxFromDp(Math.round(font_size_dp / 4f) + 1));
//                            int padding_horizontal = Math.round(Utils.pxFromDp(Math.round(desiredFontSize[0] / 4f)));
//                            padding_horizontal += Utils.pxFromDp(6);
//                            currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * padding_horizontal) + (2 * padding_horizontal);
                    currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * dp_1) + (2 * Math.max(dp_5, finalLeftRightPadding));
                } else
                    currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * dp_1) + (2 * dp_5);
                if (currentMeasuredLength > maxLength) {
                    maxLength = currentMeasuredLength;
                }
            }
        }
        return maxLength;
//        if (maxLength[0] < desireScreenWidth)
//            maxLengthCalculated = true;
//        else {
//            --desiredFontSize[0];
//            maxLength[0] = 0;
//        }
    }

    private static String getStyledFontEnglish(String html) {
        return getStyledFont(html, "merriweather_italic_eng.ttf");
    }

    private static String getStyledFontHindi(String html) {
        return getStyledFont(html, "noto_devanagari_hin.ttf");
    }

    private static String getStyledFontUrdu(String html) {
        return getStyledFont(html, "noto_nastaliq_regular_urdu.ttf");
    }

    private static void addCustomWebViewClick(WebView webView, BaseActivity activity) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface           // For API 17+
            public void performClick(String wordId) {
                activity.runOnUiThread(() -> new MeaningBottomPopupWindow(activity, "", wordId).show());
            }
        }, "onWordClick");
    }

    private static String getStyledFont(String html, String fontName) {
        String unescapedString = org.jsoup.parser.Parser.unescapeEntities(html, true);
        Document doc = Jsoup.parse(unescapedString);
        Elements links = doc.getElementsByTag("span");
        for (Element element : links) {
            element.attr("onclick", "onWordClick.performClick('" + element.attr("data-m") + "')");
            element.attr("href", "#");
        }
        html = doc.html();
        boolean addBodyStart = !html.toLowerCase().contains("<body>");
        boolean addBodyEnd = !html.toLowerCase().contains("</body");
        return getStyle(fontName) + (addBodyStart ? "<body>" : "") + html + (addBodyEnd ? "</body>" : "");
    }

    private static String getStyle(String fontName) {
        String textDirection = MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? "right" : "left";
        String int2string = Integer.toHexString(textColors);
        String HtmlColor = "#" + int2string.substring(int2string.length() - 6, int2string.length());
        return String.format("<style type=\"text/css\">@font-face {font-family: CustomFont;" +
                "src: url(\"file:///android_asset/fonts/%s\")}" +
                "body {font-family: CustomFont;color:" + HtmlColor + ";font-size: medium;text-align: " + textDirection + ";}</style>", fontName);
    }


    public static class RenderContentBuilder {
        private ArrayList<Para> paras;
        private BaseActivity activity;
        private int textColor = Color.parseColor("#333333");
        //        private int textColor= getDarkGreyTextColor();
        private Enums.TEXT_ALIGNMENT textAlignment = Enums.TEXT_ALIGNMENT.CENTER;
        private View.OnClickListener onWordClick;
        private View.OnLongClickListener onWordLongClick;
        private LinearLayout layParaContainer;
        private GestureFrameLayout zoomLayout;
        private int leftRightPadding;
        private boolean isHTML;
        private String htmlContent = "";
        private boolean showLoadingDialog = false;
        private boolean showTranslation = false;

        private RenderContentBuilder() {
        }

        public static RenderContentBuilder Builder(BaseActivity activity) {
            RenderContentBuilder renderContentBuilder = new RenderContentBuilder();
            renderContentBuilder.activity = activity;
            renderContentBuilder.textColor = activity.getDarkGreyTextColor();
            return renderContentBuilder;
        }

        public RenderContentBuilder setParas(ArrayList<Para> paras) {
            this.paras = paras;
            return this;
        }

        public RenderContentBuilder setParas(Para para) {
            if (para == null)
                return this;
            ArrayList<Para> paras = new ArrayList<>();
            paras.add(para);
            this.paras = paras;
            return this;
        }

        public RenderContentBuilder setShowTranslation(boolean showTranslation) {
            this.showTranslation = showTranslation;
            return this;
        }

        public RenderContentBuilder setLeftRightPadding(int leftRightPadding) {
            this.leftRightPadding = leftRightPadding;
            return this;
        }

        public RenderContentBuilder setTextAlignment(Enums.TEXT_ALIGNMENT textAlignment) {
            this.textAlignment = textAlignment;
            return this;
        }

        public RenderContentBuilder setOnWordClick(View.OnClickListener onWordClick) {
            this.onWordClick = onWordClick;
            return this;
        }

        public RenderContentBuilder setOnWordLongClick(View.OnLongClickListener onWordLongClick) {
            this.onWordLongClick = onWordLongClick;
            return this;
        }

        public RenderContentBuilder setLayParaContainer(LinearLayout layParaContainer) {
            this.layParaContainer = layParaContainer;
            return this;
        }

        public RenderContentBuilder setZoomLayout(GestureFrameLayout zoomLayout) {
            this.zoomLayout = zoomLayout;
            return this;
        }

        public RenderContentBuilder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public RenderContentBuilder setIsHTML(boolean isHTML) {
            this.isHTML = isHTML;
            return this;
        }

        public RenderContentBuilder setHtmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
            return this;
        }

        public RenderContentBuilder setLoadingDialog(boolean showLoadingDialog) {
            this.showLoadingDialog = showLoadingDialog;
            return this;
        }

        public void Build() {
            if (layParaContainer == null)
                throw new IllegalArgumentException("layParaContainer cannot be null");
            else if (activity == null)
                throw new IllegalArgumentException("activity cannot be null");
            else if (paras == null && !isHTML)
                throw new IllegalArgumentException("paras cannot be null");
            renderContent(paras, activity, textAlignment, onWordClick, onWordLongClick, layParaContainer, zoomLayout, textColor, leftRightPadding, isHTML, htmlContent, showLoadingDialog, showTranslation);
        }
    }

}