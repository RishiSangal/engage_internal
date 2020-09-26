package com.example.sew.helpers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.DoubleClick;
import com.example.sew.common.DoubleClickListener;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.Utils;
import com.example.sew.models.Para;
import com.example.sew.models.WordContainer;

import java.util.ArrayList;
import java.util.Collections;

public class RenderHelperOld {
    private static final String baseUrl = "file:///android_asset/";
    private static final String mimeType = "text/html; charset=UTF-8";
    private static final String historyUrl = "about:blank";
    private static final String encoding = null;

    private static void renderContent(ArrayList<Para> paras,
                                      BaseActivity activity,
                                      Enums.TEXT_ALIGNMENT textAlignment,
                                      final View.OnClickListener onWordClick,
                                      final View.OnLongClickListener onWordLongClick,
                                      LinearLayout layParaContainer, GestureFrameLayout zoomLayout, int textColor, int leftRightPadding,
                                      boolean isHTML, String htmlContent) {
        // Para para = paras.get(0);
        leftRightPadding += 5;
        int dp_5 = (int) Utils.pxFromDp(5);
        int dp_1 = (int) Utils.pxFromDp(1);
        if (layParaContainer == null)
            return;
        Typeface entTf, hindiTf, urduTf;
        entTf = ResourcesCompat.getFont(activity, R.font.merriweather_italic);
        hindiTf = ResourcesCompat.getFont(activity, R.font.laila_regular);
        urduTf = ResourcesCompat.getFont(activity, R.font.notonastaliqurdu_regular);
        final float maxFontSize = MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? 15f : 18f;
        if (isHTML) {
            layParaContainer.setVisibility(View.GONE);
//            txtWord.setText(MyHelper.fromHtml(htmlContent));
            TextView txtPlainContent = activity.findViewById(R.id.txtPlainContent);
            txtPlainContent.setVisibility(View.GONE);
            WebView webView = activity.findViewById(R.id.wvInfo);
            zoomLayout.getController().getSettings().setMaxZoom(1);
//            ViewTreeObserver viewTreeObserver = webView.getViewTreeObserver();
            webView.getSettings().setSupportZoom(false);
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
        } else {
            TextPaint paint = new TextPaint();
            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH) {//for urdu, we need to use roboto, otherwise custom font
                paint.setTypeface(entTf);
            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.HINDI) {//for urdu, we need to use roboto, otherwise custom font
                paint.setTypeface(hindiTf);
            } else if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {//for urdu, we need to use roboto, otherwise custom font
                paint.setTypeface(urduTf);
            }
            int desireScreenWidth = (int) (Utils.getScreenWidth() - Math.max(Utils.pxFromDp(5), leftRightPadding));
            float maxLength = 0;
            String maxContent = "";
            boolean maxLengthCalculated = false;
            int desiredFontSize = (int) maxFontSize;
            int maxLengthPara = 0;
            int maxLengthLine = 0;
            while (!maxLengthCalculated) {
                paint.setTextSize(Utils.pxFromDp(desiredFontSize));
                for (int i = 0; i < paras.size(); i++) {
                    Para para = paras.get(i);
                    for (int j = 0; j < para.getLines().size(); j++) {
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                            String lineFullText = para.getLines().get(j).getFullText();
                            ArrayList<WordContainer> wordContainers = para.getLines().get(j).getWordContainers();
                            String firstWord = wordContainers.get(0).getWord();
                            String secondWord = wordContainers.size() > 1 ? wordContainers.get(1).getWord() : "";
                            String lastWord = wordContainers.get(wordContainers.size() - 1).getWord();
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
                            int padding_horizontal = Math.round(Utils.pxFromDp(Math.round(desiredFontSize / 4f)));
                            padding_horizontal += Utils.pxFromDp(6);
                            currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * padding_horizontal) + (2 * padding_horizontal);
                        } else
                            currentMeasuredLength = currentMeasuredLength + (para.getLines().get(j).getWordContainers().size() * 2 * dp_1) + (2 * dp_5);
                        if (currentMeasuredLength > maxLength) {
                            maxLengthPara = i;
                            maxLengthLine = j;
                            maxLength = currentMeasuredLength;
                            maxContent = para.getLines().get(j).getFullText();
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
            desiredFontSize = Math.max(desiredFontSize, 6);
            desiredFontSize -= 1;
            if (zoomLayout != null) {
                if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU)
                    zoomLayout.getController().getSettings().setMaxZoom((maxFontSize / (float) desiredFontSize) * 2);
                else
                    zoomLayout.getController().getSettings().setMaxZoom((maxFontSize / (float) desiredFontSize) * 2);
            }
            if (layParaContainer.getParent() instanceof RelativeLayout) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                layParaContainer.setLayoutParams(layoutParams);
            } else if (layParaContainer.getParent() instanceof LinearLayout) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT);
                if (textAlignment == Enums.TEXT_ALIGNMENT.CENTER)
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                else
                    params.gravity = Gravity.START;
                layParaContainer.setLayoutParams(params);
            } else if (layParaContainer.getParent() instanceof FrameLayout)
                layParaContainer.setLayoutParams(new FrameLayout.LayoutParams((int) Math.ceil(maxLength), LinearLayout.LayoutParams.WRAP_CONTENT));
            layParaContainer.removeAllViews();
            int lineNumber = 0;
            try {
                for (int i = 0; i < paras.size(); i++) {
                    Para para = paras.get(i);
                    for (int j = 0; j < para.getLines().size(); j++) {
                        ++lineNumber;
                        LinearLayout lineView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.cell_line, null);
                        if (MyService.getSelectedLanguage() == Enums.LANGUAGE.ENGLISH)
                            lineView.setPadding(0, dp_5, 0, dp_5);
                        lineView.setGravity(Gravity.START);
//                    if (i == maxLengthPara && j == maxLengthLine)
//                        lineView.setBackgroundColor(Color.YELLOW);
                        layParaContainer.addView(lineView);
                        if (j == (para.getLines().size() - 1) && paras.size() > 1)
                            layParaContainer.addView(activity.getLayoutInflater().inflate(R.layout.cell_empty_line, null));
                        for (int k = 0; k < para.getLines().get(j).getWordContainers().size(); k++) {
                            TextView txtWord = (TextView) activity.getLayoutInflater().inflate(R.layout.cell_word, null);
                            txtWord.setTextColor(textColor);
                            /* to make ripple inside bounds */
                            lineView.setBackgroundColor(Color.parseColor("#01000000"));
                            if (MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU) {
                                int padding_vertical = Math.round(Utils.pxFromDp(Math.round(desiredFontSize / 4f) + 1));
                                int padding_horizontal = Math.round(Utils.pxFromDp(Math.round(desiredFontSize / 4f)));
                                padding_horizontal += Utils.pxFromDp(6);
                                padding_vertical += Utils.pxFromDp(2);
                                txtWord.setPadding(padding_horizontal, padding_vertical, padding_horizontal, padding_vertical);
                            }
                            txtWord.setTextSize(desiredFontSize);
                            txtWord.setText(String.format("%s ", para.getLines().get(j).getWordContainers().get(k).getWord()));
                            lineView.addView(txtWord);
                            LinearLayout.LayoutParams lp;
                            if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT) {
                                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            } else
                                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                            txtWord.setLayoutParams(lp);
                            txtWord.setTag(R.id.tag_word, para.getLines().get(j).getWordContainers().get(k));
                            txtWord.setTag(R.id.tag_line, para.getLines().get(j));
                            txtWord.setTag(R.id.tag_para, para);
                            txtWord.setTag(R.id.tag_line_number, lineNumber);
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
                            txtWord.setOnLongClickListener(v -> {
                                if (onWordLongClick != null)
                                    onWordLongClick.onLongClick(v);
                                return true;
                            });
                            if (textAlignment == Enums.TEXT_ALIGNMENT.LEFT)
                                txtWord.setGravity(Gravity.START);
                            else {
                                if (k == (para.getLines().get(j).getWordContainers().size() - 1))
                                    txtWord.setGravity(Gravity.END);
                                else if (k == 0)
                                    txtWord.setGravity(Gravity.START);
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
        BaseActivity.sendBroadCast(ICommonValues.BROADCAST_CONTENT_RENDERED);
    }

    private static String getStyledFontEnglish(String html) {
        return getStyledFont(html, "merriweather_italic_eng.ttf");
    }

    private static String getStyledFontHindi(String html) {
        return getStyledFont(html, "laila_regular_hin.ttf");
    }

    private static String getStyledFontUrdu(String html) {
        return getStyledFont(html, "noto_nastaliq_regular_urdu.ttf");
    }

    private static String getStyledFont(String html, String fontName) {
        boolean addBodyStart = !html.toLowerCase().contains("<body>");
        boolean addBodyEnd = !html.toLowerCase().contains("</body");
        return getStyle(fontName) + (addBodyStart ? "<body>" : "") + html + (addBodyEnd ? "</body>" : "");
    }

    private static String getStyle(String fontName) {
        return String.format("<style type=\"text/css\">@font-face {font-family: CustomFont;" +
                "src: url(\"file:///android_asset/fonts/%s\")}" +
                "body {font-family: CustomFont;font-size: medium;text-align: justify;}</style>", fontName);
    }

    public static class RenderContentBuilder {
        private ArrayList<Para> paras;
        private BaseActivity activity;
        private int textColor = Color.parseColor("#333333");
        private Enums.TEXT_ALIGNMENT textAlignment = Enums.TEXT_ALIGNMENT.CENTER;
        private View.OnClickListener onWordClick;
        private View.OnLongClickListener onWordLongClick;
        private LinearLayout layParaContainer;
        private GestureFrameLayout zoomLayout;
        private int leftRightPadding;
        private boolean isHTML;
        private String htmlContent = "";

        private RenderContentBuilder() {
        }

        public static RenderContentBuilder Builder(BaseActivity activity) {
            RenderContentBuilder renderContentBuilder = new RenderContentBuilder();
            renderContentBuilder.activity = activity;
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

        public void Build() {
            if (layParaContainer == null)
                throw new IllegalArgumentException("layParaContainer cannot be null");
            else if (activity == null)
                throw new IllegalArgumentException("activity cannot be null");
            else if (paras == null && !isHTML)
                throw new IllegalArgumentException("paras cannot be null");
            renderContent(paras, activity, textAlignment, onWordClick, onWordLongClick, layParaContainer, zoomLayout, textColor, leftRightPadding, isHTML, htmlContent);
        }
    }

}
